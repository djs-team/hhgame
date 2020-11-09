
/**
 * LoginScene
 */
load('module/mahjong/ui/MjPlayScene', function () {
    let BaseScene = include('public/ui/BaseScene')
    let MjPlaySceneMdt = include('module/mahjong/ui/MjPlaySceneMdt')
    let ResConfig = include('module/mahjong/common/ResConfig')
    let DeskBgLayer = include('module/mahjong/ui/DeskBgLayer')
    let DeskCardLayer = include('module/mahjong/ui/DeskCardLayer')
    let DeskHeadLayer = include('module/mahjong/ui/DeskHeadLayer')
    let DeskTopLayer = include('module/mahjong/ui/DeskTopLayer')
    let EffectLayer = include('module/mahjong/ui/EffectLayer')
    let DeskResultLayer = include('module/mahjong/ui/DeskResultLayer')
    let MatchJinjiLayer = include('module/mahjong/ui/MatchJinjiLayer')
    let MatchResultLayer = include('module/mahjong/ui/MatchResultLayer')
    let MatchBigResultLayer = include('module/mahjong/ui/MatchBigResultLayer')
    let Scene = BaseScene.extend({
        _className: 'MjPlayScene',
        RES_BINDING: function () {
            return {
            }
        },
        ctor: function () {
            this._super(ResConfig.View.MjPlayScene)
            this.registerMediator(new MjPlaySceneMdt(this))
        },

        initView: function () {
            this.registerEventListener('TCP_CLOSE', appInstance.gameAgent().onTcpClose)
            this.registerEventListener('RECONNECT_OVER_TIMES', appInstance.gameAgent().onReconnectError)

            this.DeskBgLayer = appInstance.uiManager().createUI(DeskBgLayer)
            this.addChild(this.DeskBgLayer)
            this.DeskHeadLayer = appInstance.uiManager().createUI(DeskHeadLayer)
            this.addChild(this.DeskHeadLayer)
            this.DeskCardLayer = appInstance.uiManager().createUI(DeskCardLayer)
            this.addChild(this.DeskCardLayer)
            this.DeskTopLayer = appInstance.uiManager().createUI(DeskTopLayer)
            this.addChild(this.DeskTopLayer)
            this.EffectLayer = appInstance.uiManager().createUI(EffectLayer)
            this.addChild(this.EffectLayer)
        },

        //为避免遮挡 晋级就不再这里面显示了 显示到大厅去
        showMatchJinjiLayer: function (msg) {
            if (!this.MatchJinjiLayer) {
                this.MatchJinjiLayer = appInstance.uiManager().createUI(MatchJinjiLayer)
                this.MatchJinjiLayer.setLocalZOrder(100)
                this.addChild(this.MatchJinjiLayer)
            }
            this.MatchJinjiLayer.updateView(msg)
        },

        showMatchResultLayer: function (msg) {
            cc.log('===================showMatchResultLayer===============' + JSON.stringify(msg))
            if (!this.MatchResultLayer) {
                this.MatchResultLayer = appInstance.uiManager().createUI(MatchResultLayer)
                this.MatchResultLayer.setLocalZOrder(300)
                this.addChild(this.MatchResultLayer)
            }
            this.MatchResultLayer.updateView(msg)



            // let showCall = function () {
            //     if (!this.MatchResultLayer) {
            //         this.MatchResultLayer = appInstance.uiManager().createUI(MatchResultLayer)
            //         this.MatchResultLayer.setLocalZOrder(300)
            //         this.addChild(this.MatchResultLayer)
            //     }
            //     this.MatchResultLayer.updateView(msg)
            // }.bind(this)
            // this.runAction(cc.Sequence(cc.DelayTime(2), cc.CallFunc(showCall)))
        },

        showMatchBigResultLayer: function (msg) {
            cc.log('===================showMatchBigResultLayer===============' + JSON.stringify(msg))
            if (!this.MatchBigResultLayer) {
                this.MatchBigResultLayer = appInstance.uiManager().createUI(MatchBigResultLayer)
                this.MatchBigResultLayer.setLocalZOrder(200)
                this.addChild(this.MatchBigResultLayer)
            }
            this.MatchBigResultLayer.updateView(msg)



            // let showCall = function () {
            //     if (!this.MatchBigResultLayer) {
            //         this.MatchBigResultLayer = appInstance.uiManager().createUI(MatchBigResultLayer)
            //         this.MatchBigResultLayer.setLocalZOrder(200)
            //         this.addChild(this.MatchBigResultLayer)
            //     }
            //     this.MatchBigResultLayer.updateView(msg)
            // }.bind(this)
            // this.runAction(cc.Sequence(cc.DelayTime(2), cc.CallFunc(showCall)))
        },

        clearTableView: function () {
            this.DeskHeadLayer.clearView()
            this.DeskCardLayer.clearView()
            this.DeskTopLayer.clearView()
            if (this.MatchBigResultLayer) {
                this.MatchBigResultLayer.removeFromParent()
                this.MatchBigResultLayer = null
                // appInstance.uiManager().removeUI(this.MatchBigResultLayer)
            }
            if (this.MatchJinjiLayer) {
                this.MatchJinjiLayer.removeFromParent()
                this.MatchJinjiLayer = null
                // appInstance.uiManager().removeUI(this.MatchJinjiLayer)
            }
            
        },

        clearGameResult: function () {
            if (this.DeskResultLayer) {
                appInstance.uiManager().removeUI(this.DeskResultLayer)
            }
        },

        showGameResultLayer: function (msg) {
            let DeskResultLayer = include('module/mahjong/ui/DeskResultLayer')
            this.DeskResultLayer = appInstance.uiManager().createUI(DeskResultLayer, msg)
            this.addChild(this.DeskResultLayer)

            // let showCall = function () {
            //     let DeskResultLayer = include('module/mahjong/ui/DeskResultLayer')
            //     this.DeskResultLayer = appInstance.uiManager().createUI(DeskResultLayer, msg)
            //     this.addChild(this.DeskResultLayer)
            // }.bind(this)
            // this.runAction(cc.Sequence(cc.DelayTime(2), cc.CallFunc(showCall)))
        },

        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
            appInstance.audioManager().playMusic(ResConfig.Sound.bg1, true)
            this.initView()
        },
        onExit: function () {
            this._super()
        }
    })
    return Scene
})
