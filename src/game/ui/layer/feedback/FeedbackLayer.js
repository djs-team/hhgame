
load('game/ui/layer/feedback/FeedbackLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let feedbackLayer = BaseLayer.extend({
        _className: 'feedbackLayer',
        ctor: function () {
            this._super(ResConfig.View.FeedbackLayer)
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
    return feedbackLayer
})
