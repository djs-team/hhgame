
load('game/ui/layer/fukashop/FukaShopLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let Layer = BaseLayer.extend({
        _className: 'FukaShopLayer',
        RES_BINDING: function () {
            return {
                'pnl': {  },
                'leftPnl': {  },
            }
        },
        ctor: function () {
            this._super(ResConfig.View.FukaShopLayer)
            this.initData()
            this.initView()
        },

        initData: function () {

        },

        initView: function () {

        },

        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        },
        onCloseClick: function () {
            appInstance.uiManager().removeUI(this)
        }
    })
    return Layer
})
