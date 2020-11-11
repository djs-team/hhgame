
/**
 *  ChooseCityMdt Mediator
 *
 */
load('game/ui/layer/choosecity/ChooseCityMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let mdt = Mediator.extend({
        mediatorName: 'ChooseCityMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.LoginProto
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.LoginProto:
                    this.view.onReturnBtnClick(body)
                    break
                default:
                    break

            }
        },

        onRegister: function () {

        },

        onRemove: function () {
        },

        initView: function () {
        }
    })
    return mdt
})