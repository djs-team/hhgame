
load('game/ui/layer/shop/ShopLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let GameUtil = include('game/public/GameUtil')
    let shopLayer = BaseLayer.extend({
        _className: 'shopLayer',
        ctor: function () {
            this._super(ResConfig.View.ShopLayer)
        },
        RES_BINDING: function () {
            return {
                'pnl/closeBtn': { onClicked: this.onClose }
            }
        },
        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        },
        onClose: function (sender) {
            GameUtil.delayBtn(sender);
            appInstance.uiManager().removeUI(this)
        }
    })
    return shopLayer
})
