
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
    let DeskResultLayer = include('module/mahjong/ui/DeskResultLayer')
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
            this.DeskBgLayer = appInstance.uiManager().createUI(DeskBgLayer)
            this.addChild(this.DeskBgLayer)
            this.DeskHeadLayer = appInstance.uiManager().createUI(DeskHeadLayer)
            this.addChild(this.DeskHeadLayer)
            this.DeskCardLayer = appInstance.uiManager().createUI(DeskCardLayer)
            this.addChild(this.DeskCardLayer)
            this.DeskTopLayer = appInstance.uiManager().createUI(DeskTopLayer)
            this.addChild(this.DeskTopLayer)
            // this.DeskResultLayer = appInstance.uiManager().createUI(DeskResultLayer)
            // this.addChild(this.DeskResultLayer)
        },

        clearTableView: function () {
            cc.log('============MjPlayScene============clearTableView============')
            this.DeskHeadLayer.clearView()
            this.DeskCardLayer.clearView()
            this.DeskTopLayer.clearView()
        },

        showGameResultLayer: function () {
            let DeskResultLayer = include('module/mahjong/ui/DeskResultLayer')
            let DeskResultLayerUi = appInstance.uiManager().createUI(DeskResultLayer)
            this.addChild(DeskResultLayerUi)
        },

        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
            this.initView()
        },
        onExit: function () {
            this._super()
        }
    })
    return Scene
})
