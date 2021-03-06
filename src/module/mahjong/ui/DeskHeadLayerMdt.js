
/**
 *  DeskHeadLayerMdt Mediator
 *
 */
load('module/mahjong/ui/DeskHeadLayerMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event

    let DeskMdt = Mediator.extend({
        mediatorName: 'DeskHeadLayerMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                TableEvent.InitCardProto,
                TableEvent.UpdateView,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case TableEvent.UpdateView:
                    this.UpdateView()
                    break
                case TableEvent.InitCardProto:
                    this.InitCardProto()
                    break
            }
        },

        UpdateView: function () {
            // let handCards = appInstance.dataManager().getPlayData()
            // this.view.UpdateView()
        },

        InitCardProto: function () {
            // let handCards = appInstance.dataManager().getPlayData()
            // this.view.InitCardProto()
        },

        initView: function () {
            let tData = appInstance.dataManager().getPlayData()
            this.view.initView(tData)
        },

        onRegister: function () {
            this.initView()

        },

        onRemove: function () {
        }

    })

    return DeskMdt
})