
load('game/ui/layer/set/SetLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let layer = BaseLayer.extend({
        _className: 'SetLayer',
        ctor: function () {
            this._super(ResConfig.View.SetLayer)
        },
        RES_BINDING: function () {
            return {
                'pnl/CloseBtn': { onClicked: this.onCloseClick }
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
        onCloseClick: function () {
            appInstance.uiManager().removeUI(this)
        }
    })
    return layer
})
