
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
                TableEvent.TableHostingProto,
                TableEvent.GameResultProto,
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
                case TableEvent.TableHostingProto:
                    this.TableHostingProto(body)
                    break
                case TableEvent.GameResultProto:
                    this.view.UpdateTableHosting(2)
                    break
            }
        },

        TableHostingProto: function (info) {
            let pData = appInstance.dataManager().getPlayData()
            let selfInfo = pData.getSelfInfo()
            let pSeatID = info.pSeatID
            if (pSeatID === selfInfo.pSeatID) {
                this.view.UpdateTableHosting(info.pHosting)
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
            this.UpdateView()
        },

        onRegister: function () {
            this.initView()


        },

        onRemove: function () {
        }

    })

    return DeskMdt
})