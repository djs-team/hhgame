
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
                'pnl/LoadingBar': { },
                'pnl/StateTxt': { }
            }
        },
        ctor: function () {
            this._super(ResConfig.View.UpdateScene)
            this.registerMediator(new UpdateMdt(this))
        },

        showView: function () {
            // this.goLoginScene()
        },

        onEnter: function () {
            this._super()
            this.initData()
            this.initView()
            this.showView()
        },

        onExit: function () {
            this._super()
        },

        initData: function () {
            this._percent = 0
            this._totalSize = 0
            this._downloadSize = 0

        },

        initView: function () {
            this.StateTxt.setString('正在检查热更...')
            this.LoadingBar.setPercent(0)
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
        }
    })

    return UpdateScene
})
