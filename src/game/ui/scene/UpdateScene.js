
/**
 * UpdateScene更新的场景
 */
load('game/ui/scene/UpdateScene', function () {
    let BaseScene = include('public/ui/BaseScene')
    let ResConfig = include('game/config/ResConfig')
    let HotUpdate = include('public/suport/HotUpdate')
    let HttpType = include('public/http/HttpType')
    let UpdateMdt =  include('game/ui/scene/UpdateMdt')
    let isNeedUpdate = true
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
            if (isNeedUpdate) {
                this.startUpdate()
            } else {
                this.goLoginScene()
            }
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
            appInstance.gameAgent().Tips('热更成功')
            appInstance.moduleManager().setMainLocalVer(curVersion)
            if (isUpdated) {
                appInstance.gameAgent().Tips('需要重启游戏')
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
                cc.log('=========this.percent===' + this._percent)
                this.LoadingBar.setPercent(this._percent)
                this.StateTxt.setString('总大小:' + this._totalSize + 'MB 已下载:' + this._downloadSize + 'MB')
            }
        },
        setVersionTxt: function (resVer, natieVer) {
            let verStr = 'v:' + resVer + '(' + natieVer + ')'
            this.version_txt.setString(verStr)
        },

        startUpdate: function () {
            this.LoadingBar.setPercent(0)
            this.StateTxt.setString('开始热更')

            let self = this
            let istr = 'res/project.manifest'

            let updater = new HotUpdate(istr, 'update')
            let version = updater.getLocalVersion()
            // this.setVersionTxt(version, appInstance.nativeApi().getNativeVersion())
            updater.setProgressCallback(function (percent, totalSize, downloadedSize) {
                cc.log('==========热更回调=====')
                self._percent = percent
                self._totalSize = totalSize
                self._downloadSize = downloadedSize
            })
            updater.setSuccessCallback(function (isUpdated, curVersion) {
                cc.log('==========热更成功')
                self.updateSuccess(isUpdated, curVersion)
            })
            updater.setFailureCallback(function (curVersion) {
                cc.log('==========热更失败')
                let ver = curVersion || '0'
                appInstance.moduleManager().setMainLocalVer(ver)
                appInstance.gameAgent().Tips('===========热更失败======')
            })
            updater.update()
        }
    })

    return UpdateScene
})
