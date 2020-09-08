
/**
 *  UpdateScene Mediator
 *
 */
load('game/ui/scene/UpdateMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')

    let UpdateMdt = Mediator.extend({
        mediatorName: 'UpdateMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.remoteConfigFinish_local
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.remoteConfigFinish_local:
                    this.view.remoteConfigFinish_local(body)
                    break
            }
        },

        onRegister: function () {
        },

        onRemove: function () {
        }

    })

    return UpdateMdt
})