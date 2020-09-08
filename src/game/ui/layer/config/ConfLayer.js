
load('game/ui/layer/config/ConfLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let confLayer = BaseLayer.extend({
        _className: 'confLayer',
        ctor: function () {
            this._super(ResConfig.View.ConfLayer)
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
        onClose: function () {
            appInstance.uiManager().removeUI(this)
        }
    })
    return confLayer
})
