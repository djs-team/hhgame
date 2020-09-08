
/**
 *  DeskHeadLayerMdt Mediator
 *
 */
load('module/mahjong/ui/DeskTopLayerMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event

    let DeskMdt = Mediator.extend({
        mediatorName: 'DeskTopLayerMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                TableEvent.InitCardProto,
                TableEvent.UpdateView,
                TableEvent.PutCardProto,
                TableEvent.DrawCardProto,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case TableEvent.UpdateView:
                    this.UpdateView()
                    break
                case TableEvent.PlayerSelectProto:
                    this.updateAction()
                    break
                case TableEvent.PutCardProto:
                    this.updateAction()
                    break
                case TableEvent.DrawCardProto:
                    this.updateAction()
                    break
            }
        },

        UpdateView: function () {
            this.updateAction()
        },

        updateAction: function () {
            let pData = appInstance.dataManager().getPlayData()
            let actionInfo = {}
            actionInfo.pActions = pData.tableData.pActions
            actionInfo.lastPutCard = pData.tableData.lastPutCard
            this.view.updateAction(actionInfo)
        },

        initView: function () {
            let pData = appInstance.dataManager().getPlayData()
            this.view.initView(pData)
        },

        onRegister: function () {
            this.initView()

        },

        onRemove: function () {
        }

    })

    return DeskMdt
})