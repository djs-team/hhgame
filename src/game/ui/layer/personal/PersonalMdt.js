/**
 *  Turntable Mediator
 *
 */
load('game/ui/layer/personal/PersonalMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let mdt = Mediator.extend({
        mediatorName: 'PersonalMdt',
        ctor: function (view) {
            this._super(this.mediatorName, view)
        },
        getNotificationList: function () {
            return [
                GameEvent.UPDATE_USERNAME
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.UPDATE_USERNAME:
                    this.updateUserName(body)
                    break
            }
        },

        updateUserName: function (body) {

            let data = {}
            data.pname = appInstance.dataManager().getUserData().pname
            data._nameUpdate = appInstance.dataManager().getUserData().nameUpdate
            this.view.updateUserName(data)
        },

        onRegister: function () {

            this.initView()
        },

        onRemove: function () {
        },

        initView: function () {
            var sdkUrl = appInstance.dataManager().getUserData().sdkphotourl;
            let data = {}
            data.pname = appInstance.dataManager().getUserData().pname
            data.pid = appInstance.dataManager().getUserData().pid
            data.coin = appInstance.dataManager().getUserData().coin
            data.diamonds = appInstance.dataManager().getUserData().diamonds
            data.fuKa = appInstance.dataManager().getUserData().fuKa
            data.pRole = appInstance.dataManager().getUserData().pRole
            data.photo = sdkUrl
            data._nameUpdate = appInstance.dataManager().getUserData().nameUpdate
            this.view.onInitUserData(data)

        }

    })
    return mdt
})