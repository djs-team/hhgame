
/**
 *  TaskMdt Mediator
 *
 */
load('game/ui/layer/fukashop/FukaShopMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let mdt = Mediator.extend({
        mediatorName: 'FukaShopMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [

            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {

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



        },




    })
    return mdt
})