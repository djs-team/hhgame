
/**
 *  Turntable Mediator
 *
 */
load('game/ui/layer/authentication/AuthenticationMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let mdt = Mediator.extend({
        mediatorName: 'AuthenticationMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.AUTHENICATION
            ]
        },
        handleNotification: function (notification) {

            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.AUTHENICATION:
                    this.view.onCloseClick()
                    break
            }
        },


        onRegister: function () {

            this.initView()
        },

        onRemove: function () {
        },

        initView: function () {


        }

    })
    return mdt
})