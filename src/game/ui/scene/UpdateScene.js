
/**
 * UpdateScene更新的场景
 */
load('game/ui/scene/UpdateScene', function () {
    let BaseScene = include('public/ui/BaseScene')
    let ResConfig = include('game/config/ResConfig')
    let HotUpdate = include('public/suport/HotUpdate')
    let HttpType = include('public/http/HttpType')
    let UpdateMdt =  include('game/ui/scene/UpdateMdt')
    let AppConfig = include('game/public/AppConfig')

    let UpdateScene = BaseScene.extend({
        _className: 'UpdateScene',
        _allReadyOk: false,
        RES_BINDING: function () {
            return {
                'bg': { },
                'pnl': { },
                'Txtpnl': { },
                'middlePnl': { },
                'bottomPnl': { },
                'pnl/LoadingBar': { },
                'pnl/StateTxt': { },
                'pnl/VersionTxt': { },
                'AniPnl': { },
                'AniPnl/ArmatureNode': { },
            }
        },
        ctor: function () {
            this._super(ResConfig.View.UpdateScene)
            this.registerMediator(new UpdateMdt(this))
        },

        showView: function () {
            this.bg.setVisible(false)
            this.pnl.setVisible(false)
            this.AniPnl.setVisible(true)
            this.Txtpnl.setVisible(false)
            this.middlePnl.setVisible(false)
            this.bottomPnl.setVisible(false)

            appInstance.audioManager().playEffect('res/sound/tg_logo.mp3')
            this.runAction(cc.sequence(cc.DelayTime(2.7), cc.CallFunc(function() {
                this.AniPnl.setVisible(false)
                this.Txtpnl.setVisible(true)
                this.middlePnl.setVisible(true)
                this.bottomPnl.setVisible(true)
            }.bind(this)), cc.DelayTime(0.3), cc.CallFunc(function() {
                this.bg.setVisible(true)
                this.AniPnl.setVisible(false)
                this.Txtpnl.setVisible(false)
                this.middlePnl.setVisible(false)
                this.bottomPnl.setVisible(false)
                this.pnl.setVisible(true)
                this.checkForce()
            }.bind(this))))
        },


        checkForce: function () {
            appInstance.httpFactory().createHttpRequest('GET', AppConfig.webUrl + 'forceCheck.json', null, function (result) {
                if (result.code === 0) {
                    let info = result.data.android
                    if (cc.sys.OS_IOS === cc.sys.os) {
                        info = result.data.ios
                    }
                    if (info.isForceCheck) {
                        cc.loader.loadJson('res/project.manifest', function (er, data) {
                            if (er || !data.version) {
                                this.goLoginScene()
                            } else {
                                let serverVersionArray = info.version.split('.')
                                let localVersionArray = data.version.split('.')
                                if (serverVersionArray[0] <= localVersionArray[0] && serverVersionArray[1] <= localVersionArray[1]) {
                                    this.startUpdate()
                                } else {
                                    let dialogMsg = {
                                        ViewType: 1,
                                        SayText: '您的版本过低，请前往下载最新版本！',
                                        MidBtnName: '前往'
                                    }
                                    dialogMsg.MidBtnClick = function () {
                                        cc.sys.openURL(info.downUrl)
                                        cc.director.end()
                                        cc.game.end()
                                    }
                                    appInstance.gameAgent().addDialogUINoPop(dialogMsg)
                                }
                            }
                        }.bind(this))
                    } else {
                        this.goLoginScene()
                    }

                } else {
                    this.goLoginScene()
                }

            }.bind(this))
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
            this.VersionTxt.setVisible(false)
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
            if (this._percent > 0 && !this._allReadyOk) {
                cc.log('=========this.percent===' + this._percent)
                this.LoadingBar.setPercent(this._percent)
                this.StateTxt.setString('已下载%' + this._percent)
                if (this._percent >= 100) {
                    this._allReadyOk = true
                }
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
            this.VersionTxt.setVisible(true)
            this.VersionTxt.setString(version)
            updater.setProgressCallback(function (percent) {
                cc.log('==========热更过程中=====' + percent)
                self._percent = percent
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
