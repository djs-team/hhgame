/**
 * 用于来存储并读取本地头像缓存文件的类
 * (主要为了避免频繁像腾讯 发现头像下载请求 导致后面下载被限制的问题，
 * 如果有自己的文件缓存服务器就可以启用这套方案，因为时间问题,并没有相关的人去负责这个问题暂时客户端使用这种方案替代）
 */
load('public/ui/HeadFileCache', function () {
    let HeadFileCache = cc.Class.extend({
        _maxNum: 50,
        _headDict: {},
        ctor: function (maxNum) {
            if (maxNum && maxNum > 0) {
                this._maxNum = maxNum
            }
            this._headDict = this._loadHeadDict()
        },
        /**
         * 读取用户缓存文件
         * @private
         */
        _loadHeadDict: function () {
            return global.localStorage.getDataForKey('headFileCache') || {}
        },
        /**
         * 写入用户缓存
         * @private
         */
        _storeHeadDict: function (value) {
            global.localStorage.setDataForKey('headFileCache', value)
        },
        /**
         * 判断是否需要重新下载头像
         * @param uid
         * @param url
         * @private
         */
        _needRedownload: function (uid, url) {
            if (this._headDict[uid] &&
                this._headDict[uid].url === url &&
                this._headDict[uid].path &&
                jsb.fileUtils.isFileExist(this._headDict[uid].path)) {
                return false
            }
            return true
        },
        /**
         * 用于下载并加载纹理
         * @param uid
         * @param url
         * @param cb
         * @param isSave
         * @returns {null}
         */
        loadImg: function (uid, url, cb) {
            if (!this._needRedownload(uid, url)) {
                cc.loader.loadImg(this._headDict[uid].path, cb)
            } else {
                global.gameFileUtil.downloadFile(url, function (data) {
                    try {
                        this._asynSave(uid, url, data, cb)
                    } catch (e) {
                        cc.error('Save %s head is faild! [%s]', uid, e)
                    }
                }.bind(this), function () {
                    cc.error('loadImg Error %s !', url)
                })
            }
            return null
        },
        /**
         * 因为IO操作尽量实现异步操作
         * 暂时同步后面追加
         * @param data
         * @private
         */
        _asynSave: function (uid, url, data, cb) {
            let savePath = this._getFileSavePath(uid)

            if (cc.textureCache.getTextureForKey(savePath)) {
                cc.textureCache.removeTextureForKey(savePath)
            }

            if (global.gameFileUtil.saveFile(data, savePath)) {
                if (!this._headDict[uid]) {
                    this._headDict[uid] = {}
                }

                this._headDict[uid].url = url
                this._headDict[uid].path = savePath
                cc.loader.loadImg(savePath, cb)
                this._storeHeadDict(this._headDict)
            }
        },
        /**
         * 根据uid 获得文件的保存路径
         * @private
         */
        _getFileSavePath: function (uid) {
            let writePath = jsb.fileUtils.getWritablePath() + 'headCache/'
            global.gameFileUtil.createDir(writePath)
            return writePath + 'Head_' + uid
        },
        /**
         * 清空缓存
         */
        clearAll: function () {
            this._storeHeadDict({})
            let dir = jsb.fileUtils.getWritablePath() + 'headCache/'
            global.gameFileUtil.removeDir(dir)
        }

    })
    return HeadFileCache
})