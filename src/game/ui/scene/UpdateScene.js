
/**
 * UpdateScene更新的场景
 */
load('game/ui/scene/UpdateScene', function () {
    let BaseScene = include('public/ui/BaseScene')
    let ResConfig = include('game/config/ResConfig')
    let HotUpdate = include('public/suport/HotUpdate')
    let HttpType = include('public/http/HttpType')
    let UpdateMdt =  include('game/ui/scene/UpdateMdt')
    let UpdateScene = BaseScene.extend({
        _className: 'UpdateScene',
        RES_BINDING: function () {
            return {
                // 'info_pnl/slider_bg': { },
                // 'info_pnl/sliderPnl': { },
                // 'info_pnl/slider_bar': { },
                // 'info_pnl/slider_txt': { },
                // 'info_pnl/version_txt': { }
            }
        },
        ctor: function () {
            this._super(ResConfig.View.UpdateScene)
            this.registerMediator(new UpdateMdt(this))
        },

        onCreate: function () {
            this._super()
        },

        onEnter: function () {
            this._super()
            this.initData()
            this.initView()
            this.showView()

            this.goLoginScene()
        },

        onExit: function () {
            this._super()
        },

        initData: function () {

        },

        initView: function () {

        },

        showView: function () {

        },

        goLoginScene: function () {
            let LoginScene = include('game/ui/scene/LoginScene')
            appInstance.sceneManager().replaceScene(new LoginScene())
        },

        updateSuccess: function (isUpdated, curVersion) {
            appInstance.moduleManager().setMainLocalVer(curVersion)
            if (isUpdated) {
                appInstance.restartGame()
            } else {
                this.goLoginScene()
            }
        },
        onKeyBoard: function (keyCode, event) {
            this._super(keyCode, event)
        },
        onUpdate: function () {
            if (this._percent > 0) {
                // this.slider_bar.setPercent(this._percent)
                this.slider_bar.setPositionX(-this.slider_bar.width + this._percent*0.01 * this.slider_bar.width)
                this.slider_txt.setString('总大小:' + this._totalSize + 'MB 已下载:' + this._downloadedSize + 'MB')
            }
        },
        setVersionTxt: function (resVer, natieVer) {
            let verStr = 'v:' + resVer + '(' + natieVer + ')'
            this.version_txt.setString(verStr)
        },

        startUpdate: function () {
            // this.slider_bar
            this.slider_bar.setPositionX(-this.slider_bar.width)
            this.slider_txt.setString('')

            let self = this
            let istr = 'res/project.manifest'
            if (typeof ppgamecenter !== 'undefined') {
                istr = jsb.fileUtils.getWritablePath() + 'update/' + 'project.manifest'
            }

            let updater = new HotUpdate(istr, 'update')
            let version = updater.getLocalVersion()
            this.setVersionTxt(version, appInstance.nativeApi().getNativeVersion())
            updater.setProgressCallback(function (percent, totalSize, downloadedSize) {
                self._percent = percent
                self._totalSize = totalSize
                self._downloadedSize = downloadedSize
            })
            updater.setSuccessCallback(function (isUpdated, curVersion) {
                self.updateSuccess(isUpdated, curVersion)
            })
            updater.setFailureCallback(function (curVersion) {
                let ver = curVersion || '0'
                appInstance.moduleManager().setMainLocalVer(ver)
                utils.alertMsg('更新失败，请重试。', function () {
                    self.startUpdate()
                })
            })
            updater.update()
        },

        remoteConfigFinish_local: function () {
            if (cc.sys.isMobile) {
                this.startUpdate()
            } else {
                this.goLoginScene()
            }
        },

        afterForceUpdate: function () {
            appInstance.connectorApi().getRemoteConfig()
        },
        /**
         * 强制更新回调
         * @param result
         */
        forceUpdateCallBack: function (result) {
            if (result.code === HttpType.ResultCode.OK) {
                let data = result.data
                let localversion = appInstance.nativeApi().getNativeVersion()
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    data = data.android
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    data = data.ios
                }
                if (!data.isneed) {
                    this._forUpdateStatus = this._forceStatus.needntForce
                    this.afterForceUpdate()
                    return
                }
                if (utils.compareVersion(localversion, data.miniversion) < 0) {
                    utils.alertMsg(data.notice, function () {
                        this._forUpdateStatus = this._forceStatus.needForce
                        cc.Application.getInstance().openURL(data.forceurl)
                    })
                } else {
                    this._forUpdateStatus = this._forceStatus.dontForce
                    this.afterForceUpdate()
                }
            } else {
                this._forUpdateStatus = this._forceStatus.forceError
                this.afterForceUpdate()
            }
        },
        /**
         * 检查强更的逻辑
         */
        checkForceUpdate: function () {

        }
    })

    return UpdateScene
})
