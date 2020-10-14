/**
 *  HallScene Mediator
 *
 */
load('game/ui/scene/HallMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let HallMdt = Mediator.extend({
        mediatorName: 'HallMdt',
        ctor: function (view) {
            this._super(this.mediatorName, view)
        },
        getNotificationList: function () {
            return [
                GameEvent.remoteConfigFinish_local,
                GameEvent.USERDATA,
                GameEvent.UPDATE_USERNAME,
                GameEvent.GET_CASHCOWNUM,
                GameEvent.UPDATE_PROPSYNC,
                GameEvent.UPDATE_USERPHOTO,
                GameEvent.MatchJinjiGaming,
                GameEvent.URLE_USED_UPDATE,

            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.remoteConfigFinish_local:
                    this.view.remoteConfigFinish_local(body)
                    break
                case GameEvent.USERDATA:
                    this.view.onUpdateUserData(body)
                    break
                case GameEvent.UPDATE_USERNAME:
                    this.view.onUpdateUserData(body)
                    break
                case GameEvent.GET_CASHCOWNUM:
                    this.view.onGoCashCowLayer()
                    break
                case GameEvent.UPDATE_PROPSYNC:
                    this.view.onUpdateUserData(body)
                    break
                case GameEvent.UPDATE_USERPHOTO:
                    this.view.onUpdateUserData(body)
                    break
                case GameEvent.MatchJinjiGaming:
                    this.view.showMatchJinjiLayer(body)
                    break
                case GameEvent.URLE_USED_UPDATE:
                    this.onChangeUsedRole()
                    break
                default:
                    break
            }
        },

        onRegister: function () {
            cc.log('--------------hallMdt----------------onRegister')
            let selfInfo = appInstance.dataManager().getUserData()
            this.view.initView(selfInfo)
        },

        onChangeUsedRole: function () {
            let pRole = appInstance.dataManager().getUserData().pRole
            this.view.updatePlayerAni(pRole)
        },

        onRemove: function () {
        }

    })

    return HallMdt
})