
/**
 *  Turntable Mediator
 *
 */
load('game/ui/layer/coingame/CoinGameMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let mdt = Mediator.extend({
        mediatorName: 'CoinGameMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.UPDATE_PROPSYNC,
                GameEvent.USERDATA
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.UPDATE_PROPSYNC:
                    this.view.onUpdatePropsData(body)
                    break
                case GameEvent.USERDATA:
                    this.view.onUpdatePropsData(body)
                    break
                default:
                    break

            }
        },

        onRegister: function () {

            this.initView()
        },

        onRemove: function () {
        },

        initView: function () {
            let data = {
                'coin' : appInstance.dataManager().getUserData().coin,
                'diamonds' : appInstance.dataManager().getUserData().diamonds,
            }
            this.view.initView(data)
        },
    })
    return mdt
})