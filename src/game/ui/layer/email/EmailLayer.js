
load('game/ui/layer/email/EmailLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let EmailLayer = BaseLayer.extend({
        _className: 'EmailLayer',
        ctor: function () {
            this._super(ResConfig.View.EmailLayer)
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
    return EmailLayer
})
