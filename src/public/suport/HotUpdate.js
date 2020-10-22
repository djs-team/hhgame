
load('public/suport/HotUpdate', function () {
    let gDownloadUrls = null
    let curDownloadUrlIdx = -1

    /**
     * hot update class
     * @class HotUpdate
     */
    let HotUpdate = cc.Class.extend({
        _am: null,
        _manifestUrl: null,
        _storagePath: null,
        _manifestDownloadUrl: null,

        _updating: false,

        _checkListener: null,
        _updateListener: null,

        _eventPercent: 0,
        _realPercent: 0,

        _preDownloaded: 0,
        _totalSize: 0,
        _totalDownloaded: 0,

        _retryCount: 3,

        /**
         * 热更进度回调
         * @callback HotUpdate~progressCallback
         * @param {number} progressPercent - update progress percent[0-100].
         * @param {number} totalSize - The total byte size(MB) to be downloaded of the update.
         * @param {number} totalDownloaded - The current downloaded byte size(MB) of the update.
         */
        _progressCallback: null,

        /**
         * 热更成功回调
         * @callback HotUpdate~successCallback
         * @param {boolean} isUpdated - whether is updated or not
         * @param {boolean} curVersion - the current version of this assets
         */
        _successCallback: null,

        /**
         * 热更失败回调
         * @callback HotUpdate~failureCallback
         */
        _failureCallback: null,

        /**
         * HotUpdate construct
         * @constructs HotUpdate
         * @param manifestUrl - The url for the local manifest file.
         * @param storagePath - The storage path for downloaded assets which auto set in the writable path.
         * @param manifestDownloadUrl - The download url using for init manifest when the local manifest doesn't exist.
         * This param is mainly in order to download modular games.
         * @warning Please ensure the path of manifestUrl and storagePath are different,
         * otherwise we may can't get the correct local manifest if you set a custom search path same with that path.
         */
        ctor: function (manifestUrl, storagePath, manifestDownloadUrl) {
            cc.log('========manifestUrl==============' + manifestUrl)
            cc.log('========storagePath==============' + storagePath)
            this._manifestUrl = manifestUrl
            this._storagePath = ((jsb.fileUtils ? jsb.fileUtils.getWritablePath() : '/') + storagePath)
            this._manifestDownloadUrl = manifestDownloadUrl
            this.log('Storage path for remote asset : ' + this._storagePath)

            this._initAssetsManager()
            this.loadDefaultDownloadUrls()
        },

        _initAssetsManager: function () {
            this._am = new jsb.AssetsManager(this._manifestUrl, this._storagePath, this._manifestDownloadUrl)
            this._am.retain()
        },

        log: function (param) {
            cc.log('#HotUpdate# ' + param)
        },

        _checkCb: function (event) {
            this.log('Code: ' + event.getEventCode())
            switch (event.getEventCode()) {
                case jsb.EventAssetsManager.ERROR_NO_LOCAL_MANIFEST:
                    this.log('No local manifest file found, hot update skipped.')
                    break
                case jsb.EventAssetsManager.ERROR_DOWNLOAD_MANIFEST:
                    this.log('Fail to download manifest file, hot update skipped.')
                    break
                case jsb.EventAssetsManager.ERROR_PARSE_MANIFEST:
                    this.log('Fail to parse manifest file, hot update skipped.')
                    break
                case jsb.EventAssetsManager.ALREADY_UP_TO_DATE:
                    this.log('Already up to date with the latest remote version.')
                    break
                case jsb.EventAssetsManager.NEW_VERSION_FOUND:
                    this.log('New version found, please try to update.')
                    break
                default:
                    return
            }

            cc.eventManager.removeListener(this._checkListener)
            this._checkListener = null
            this._updating = false
        },

        _updateCb: function (event) {
            let failed = false
            let succeed = false
            let updated = false
            let eventCode = event.getEventCode()
            // this.log('_updateCb event code:' + eventCode)
            switch (eventCode) {
                case jsb.EventAssetsManager.ERROR_NO_LOCAL_MANIFEST:
                    this.log('No local manifest file found, hot update skipped.')
                    failed = true
                    break
                case jsb.EventAssetsManager.UPDATE_PROGRESSION:
                    let assetId = event.getAssetId()
                    if (assetId !== '@version' && assetId !== '@manifest') {
                        let eventPercent = parseInt(event.getPercent())
                        if (this._eventPercent !== eventPercent) {
                            this._eventPercent = eventPercent
                            if (!this._totalSize && this._totalSize < 0.0001) {
                                this._totalSize = this._am.getTotalBytes() / 1048576
                            }
                            this._totalDownloaded = this._am.getDownloadedBytes() / 1048576 + this._preDownloaded

                            let downloadedPercent = Math.min(parseInt(this._totalDownloaded / this._totalSize * 100 + 0.5), 100)
                            if (this._realPercent !== downloadedPercent) {
                                this._realPercent = downloadedPercent
                                this.log('Updated Progress: ' + this._realPercent + ' totalSize: ' + this._totalSize + ' totalDownloaded: ' + this._totalDownloaded)
                                this._progressCallback && this._progressCallback(this._realPercent, this._totalSize.toFixed(2), this._totalDownloaded.toFixed(2))
                            }
                        }
                    }
                    break
                case jsb.EventAssetsManager.ERROR_DOWNLOAD_MANIFEST:
                    this.log('Fail to download manifest file, hot update skipped.')
                    failed = true
                    break
                case jsb.EventAssetsManager.ERROR_PARSE_MANIFEST:
                    this.log('Fail to parse manifest file, hot update skipped.')
                    failed = true
                    break
                case jsb.EventAssetsManager.ALREADY_UP_TO_DATE:
                    this.log('Already up to date with the latest remote version.')
                    succeed = true
                    break
                case jsb.EventAssetsManager.UPDATE_FINISHED:
                    this.log('Update finished. ' + event.getMessage())
                    updated = true
                    succeed = true
                    break
                case jsb.EventAssetsManager.UPDATE_FAILED:
                    this.log('Update failed. ' + event.getMessage())
                    failed = true
                    break
                case jsb.EventAssetsManager.ERROR_UPDATING:
                    this.log('Asset update error: ' + event.getAssetId() + ', ' + event.getMessage())
                    // Don't set failed(true) here.
                    // Because the updating doesn't finish yet, whenever set failed here will make AssetsManager released and cause crash.
                    // The UPDATE_FAILED event will send in the end.
                    // failed = true
                    break
                case jsb.EventAssetsManager.ERROR_DECOMPRESS:
                    this.log('_updateCb ' + event.getMessage())
                    // Don't set failed(true) here.
                    // Because the updating doesn't finish yet, whenever set failed here will make AssetsManager released and cause crash.
                    // The UPDATE_FAILED event will send in the end.
                    // failed = true
                    break
                case jsb.EventAssetsManager.NEW_VERSION_FOUND:
                    this.log('New version found.')
                    break
                case jsb.EventAssetsManager.ASSET_UPDATED:
                    this.log('Asset updated.' + event.getAssetId() + ', ' + event.getMessage())
                    break
                default:
                    failed = true
                    break
            }

            if (failed) {
                this._updating = false
                this.switchDownloadUrl()

                if (this._retryCount > 0) {
                    this._retryCount--
                    this.log('retrying left count:' + this._retryCount)

                    this._preDownloaded = this._totalDownloaded

                    let self = this
                    setTimeout(function () {
                        self._initAssetsManager()
                        self.update()
                    }, 100)
                } else {
                    if (this._failureCallback) {
                        let self = this
                        // Must delay one frame at last, because do the callback immediately will cause potential crash risk.
                        let curVersion = this._am.getLocalManifest().getVersion()
                        setTimeout(function () {
                            self._failureCallback(curVersion)
                        }, 1)
                    }
                }

                this.destroy()
            }

            if (succeed) {
                let curVersion = this._am.getLocalManifest().getVersion()
                this.log('current version:' + curVersion)
                if (this._successCallback) {
                    let self = this
                    // Must delay one frame at last, because do the callback immediately will cause potential crash risk.
                    setTimeout(function () {
                        self._successCallback(updated, curVersion)
                    }, 1)
                }

                this.destroy()
            }
        },

        /**
         * Set a count to retry when update failed.
         * @param {number}count
         */
        setRetryCount: function (count) {
            this._retryCount = count
        },

        /**
         * 获得被动的程序版本号
         */
        getLocalVersion: function () {
            return this._am.getLocalManifest().getVersion() || '1.0.0'
        },

        /**
         * 检查热更
         */
        checkUpdate: function () {
            if (this._updating) {
                this.log('Checking or updating ...')
                return
            }

            if (!this._am.getLocalManifest() || !this._am.getLocalManifest().isLoaded()) {
                this.log('Failed to load local manifest ...')
                return
            }
            this._checkListener = new jsb.EventListenerAssetsManager(this._am, this._checkCb.bind(this))
            cc.eventManager.addListener(this._checkListener, 1)

            this.log('checkUpdate...')
            this._am.checkUpdate()
            this._updating = true
        },

        /**
         * 开始热更
         */
        update: function () {
            this.log('hotUpdate 1...')
            if (this._am && !this._updating) {
                this._updateListener = new jsb.EventListenerAssetsManager(this._am, this._updateCb.bind(this))
                cc.eventManager.addListener(this._updateListener, 1)

                this._am.update()
                this._updating = true
                this.log('hotUpdate 2...')
            }
        },

        /**
         * 替换热更地址
         * @param {string} newUrl - 新的热更地址
         */
        setReplaceUrl: function (newUrl) {
            cc.log('========setReplaceUrl=========' + newUrl)
            this.log('setReplaceUrl:' + newUrl)
            if (this._am) {
                if (typeof (newUrl) !== 'undefined' && newUrl != null) {
                    if (typeof (this._am.getLocalManifest().setReplaceUrl) !== 'undefined') {
                        this._am.getLocalManifest().setReplaceUrl(newUrl)
                    }
                }
            }
        },

        /**
         * Load default download urls from remote config
         */
        loadDefaultDownloadUrls: function () {
            if (!gDownloadUrls) {
                let urls = ['http://10.1.1.151:8000/main']
                if (urls && urls.length > 0) {
                    cc.log('==========loadDefaultDownloadUrls=======11111========')
                    gDownloadUrls = urls
                    // Switch to new download url when cdn urls loaded.
                    this.switchDownloadUrl()
                } else {
                    cc.log('==========loadDefaultDownloadUrls=======11111========')
                    this.setReplaceUrl('')
                }
            }
        },

        /**
         * Set urls for download project.manifest and asset files, not for version.manifest
         * @param {string[]} urls - url array
         */
        setGlobalDownloadUrls: function (urls) {
            cc.log('======setGlobalDownloadUrls========' + JSON.stringify(urls))
            gDownloadUrls = urls
        },

        /**
         * switch download url from gDownloadUrls
         */
        switchDownloadUrl: function () {
            if (gDownloadUrls && gDownloadUrls.length > 0) {
                curDownloadUrlIdx = (curDownloadUrlIdx + 1) % gDownloadUrls.length
                let url = gDownloadUrls[curDownloadUrlIdx]
                if (url && url.length > 0) {
                    cc.log('===========switchDownloadUrl=================' + JSON.stringify(gDownloadUrls))
                    this.setReplaceUrl(url)
                }
            }
        },

        /**
         * 热更对象是否可用
         * @returns {boolean}
         */
        isAvailable: function () {
            if (typeof (this._am) !== 'undefined' && this._am) {
                if (this._am.getLocalManifest().isLoaded()) {
                    this.log('isAvailable true')
                    return true
                }
            }
            this.log('isAvailable false')
            return false
        },

        /**
         * 主动销毁热更对象，长时间热更不成功时可主动调用
         */
        destroy: function () {
            this.log('destroy')
            if (this._updateListener) {
                try {
                    cc.eventManager.removeListener(this._updateListener)
                } catch (e) {
                }
                this._updateListener = null
            }
            if (this._checkListener) {
                try {
                    cc.eventManager.removeListener(this._checkListener)
                } catch (e) {
                }
                this._checkListener = null
            }
            if (this._am) {
                try {
                    this._am.release()
                } catch (e) {
                }
                this._am = null
            }
        },

        /**
         * 设置热更百分比回调
         * @param {HotUpdate~progressCallback} progressCallback - 回调时传递百分比参数，范围:[0-100]
         */
        setProgressCallback: function (progressCallback) {
            this._progressCallback = progressCallback
        },

        /**
         * 设置热更成功回调
         * @param {HotUpdate~successCallback} successCallback - 回调时会传递一个布尔值表示是否进行了热更
         */
        setSuccessCallback: function (successCallback) {
            this._successCallback = successCallback
        },

        /**
         * 设置热更失败回调，可以用来重试等操作
         * @param {HotUpdate~failureCallback} failureCallback - 热更失败回调
         */
        setFailureCallback: function (failureCallback) {
            this._failureCallback = failureCallback
        }
    })
    return HotUpdate
})
