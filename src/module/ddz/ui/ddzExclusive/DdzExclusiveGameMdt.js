/**
 *  Turntable Mediator
 *
 */
load('module/ddz/ui/ddzExclusive/DdzExclusiveGameMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let mdt = Mediator.extend({
        mediatorName: 'DdzExclusiveGameMdt',
        ctor: function (view) {
            this._super(this.mediatorName, view)
        },
        getNotificationList: function () {
            return [
                // GameEvent.UPDATE_PROPSYNC,

            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            // switch (name) {
            //     case GameEvent.UPDATE_PROPSYNC:
            //         this.view.onUpdatePropsData(body)
            //         break
            //     default:
            //         break
            //
            // }
        },

        onRegister: function () {

            this.initView()
        },

        onRemove: function () {
        },

        /**
         * 初始化商城灯信息
         */
        initView: function () {

            // appInstance.gameAgent().httpGame().COINSSHOPDATASReq()
            // let data = {
            //     'coin': appInstance.dataManager().getUserData().coin,
            //     'diamonds': appInstance.dataManager().getUserData().diamonds,
            //     'fuKa': appInstance.dataManager().getUserData().fuKa,
            // }
            //
            // this.view.onUpdatePropsData(data)


        },



    })
    return mdt
})
