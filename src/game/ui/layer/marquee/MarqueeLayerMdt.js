
/**
 *  MarqueeLayerMdt Mediator
 *
 */
load('game/ui/layer/marquee/MarqueeLayerMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let MDT = Mediator.extend({
        mediatorName: 'MarqueeLayerMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.DIALOG_HIDE_ALL,
                GameEvent.DIALOG_HIDE_ONE,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.DIALOG_HIDE_ALL:
                    this.view.hideView(body)
                    break
                case GameEvent.DIALOG_HIDE_ONE:
                    this.view.hideByName(body)
                    break
            }
        },

        initView: function () {

        },

        onRegister: function () {
            this.initView()

        },

        onRemove: function () {
        }

    })

    return MDT
})