
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
                GameEvent.MARQUEE_HTTP_BACK,
                GameEvent.MARQUEE_TCP_BACK,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.MARQUEE_HTTP_BACK:
                    this.view.httpMsgBack(body)
                    break
                case GameEvent.MARQUEE_TCP_BACK:
                    this.view.tcpMsgBack(body)
                    break
            }
        },

        onRegister: function () {

        },

        onRemove: function () {
        }

    })

    return MDT
})