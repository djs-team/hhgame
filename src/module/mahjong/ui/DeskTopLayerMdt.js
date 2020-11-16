
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
                TableEvent.PlayerSelectProto,
                TableEvent.JiaGangTableProto,
                TableEvent.GamingProto,
                TableEvent.MatchResultProto,
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
                case TableEvent.PutCardProto:
                case TableEvent.DrawCardProto:
                case TableEvent.JiaGangTableProto:
                    this.updateAction()
                    this.updateRemainingCard()
                    break
                case TableEvent.TableHostingProto:
                    this.TableHostingProto(body)
                    break
                case TableEvent.GameResultProto:
                    this.view.UpdateTableHosting(2)
                    this.view.onCloseChuang()
                    break
                case TableEvent.MatchResultProto:
                    this.view.onCloseChuang()
                    break
                case TableEvent.GamingProto:
                    this.view.toExpressionView(body);
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

        updateRemainingCard: function () {
            let pData = appInstance.dataManager().getPlayData()
            let tData = pData.tableData
            this.view.updateRemainingCard(tData.nDeckCardNum)
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