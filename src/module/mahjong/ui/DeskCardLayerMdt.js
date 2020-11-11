
/**
 *  DeskCardLayer Mediator
 *
 */
load('module/mahjong/ui/DeskCardLayerMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event

    let DeskCardLayerMdt = Mediator.extend({
        mediatorName: 'DeskCardLayerMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                TableEvent.UpdateView,
                TableEvent.initTableView,
                TableEvent.InitCardProto,
                TableEvent.DrawCardProto,
                TableEvent.PutCardProto,
                TableEvent.prePutCard,
                TableEvent.PlayerSelectProto,
                TableEvent.AutoPlayProto,
                TableEvent.updateSelfHandCard,
                TableEvent.clearTableGaming,
                TableEvent.JiaGangTableProto,
                TableEvent.GameResultProto,
                TableEvent.GameFenZhangProto,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case TableEvent.initTableView:
                    this.initView()
                    break
                case TableEvent.UpdateView:
                case TableEvent.JiaGangTableProto:
                    this.updateView()
                    break
                case TableEvent.InitCardProto:
                    this.InitCardProto()
                    break
                case TableEvent.DrawCardProto:
                    this.DrawCardProto(body)
                    this.runDirection()
                    break
                case TableEvent.PutCardProto:
                    this.PutCardProto(body)
                    //this.runDirection()
                    break
                case TableEvent.prePutCard:
                    this.prePutCard(body)
                    break
                case TableEvent.PlayerSelectProto:
                    this.PlayerSelectProto(body)
                    this.runDirection()
                    // this.view.downSelfHandCard()
                    break
                case TableEvent.AutoPlayProto:
                    this.AutoPlayProto(body)
                    this.runDirection()
                    break
                case TableEvent.updateSelfHandCard:
                    this.updateSelfHandCard(body)
                    break
                case TableEvent.clearTableGaming:
                    this.view.clearTableGaming(body)
                    break
                case TableEvent.GameResultProto:
                    this.showLiuJu(body)
                    break
                case TableEvent.GameFenZhangProto:
                    this.view.showFenZhang()
                    break
            }
        },



        updateView: function () {
            let pData = appInstance.dataManager().getPlayData()
            let pCurSeatID = pData.tableData.pCurSeatID
            let players = pData.players
            for (let k in players) {
                let uiSeat = pData.seatId2UI(k)
                let isTurn = (pCurSeatID === parseInt(k))
                let player = players[k]
                this.view.updateHandCard(uiSeat, player, isTurn)

                this.view.updatePutCard(uiSeat, player.putCards )
            }
            this.runDirection()
        },

        runDirection: function () {
            let pData = appInstance.dataManager().getPlayData()
            let pCurSeatID = pData.tableData.pCurSeatID
            let pCurUiSeatId = pData.seatId2UI(pCurSeatID)
            this.view.runDirection(pCurUiSeatId)
        },

        PlayerSelectProto: function (msg) {
            cc.log('=======PlayerSelectProto==========' + JSON.stringify(msg))
            let pData = appInstance.dataManager().getPlayData()
            let players = pData.players
            if (msg.updatePutCard && msg.updatePutCard.isNeed) {
                let uiSeat = pData.seatId2UI(msg.updatePutCard.seatID)
                this.view.updatePutCard(uiSeat, pData.players[msg.updatePutCard.seatID].putCards)
            }

            if (msg.updateHandCard && msg.updateHandCard.isNeed) {
                let seatID = msg.updateHandCard.seatID//上一次选择的玩家座位号
                let pCurSeatID = pData.tableData.pCurSeatID//当前玩家座位号
                let isTurn = (pCurSeatID === parseInt(seatID))//当前操作玩家和上一个玩家是否是同一个人
                let player = players[seatID]//上一个操作玩家
                let uiSeat = pData.seatId2UI(seatID)//上一个操作玩家的UI座位号
                this.view.updateHandCard(uiSeat, player, isTurn)
            }
        },

        updateSelfHandCard: function () {
            let pData = appInstance.dataManager().getPlayData()
            let selfInfo = pData.getSelfInfo()
            let uiSeat = pData.seatId2UI(selfInfo.pSeatID)
            this.view.updateHandCard(uiSeat, selfInfo, (selfInfo.handCards.length % 3 ) === 2)
        },

        prePutCard: function (card) {
            let pData = appInstance.dataManager().getPlayData()
            let tData = pData.tableData
            tData.pMustOutCard = []

            let selfInfo = pData.getSelfInfo()
            let handCards = selfInfo.handCards
            appInstance.gameAgent().mjUtil().removeCard(handCards, card)
            appInstance.gameAgent().mjUtil().sortCard(handCards)
            let uiSeat = pData.seatId2UI(selfInfo.pSeatID)
            this.view.updateHandCard(uiSeat, selfInfo)
        },

        PutCardProto: function (msg) {
            let pData = appInstance.dataManager().getPlayData()

            let tData = pData.tableData
            let players = pData.players
            let pCurSeatID = tData.pCurSeatID
            let pPutSeatID = tData.pPutSeatID
            let uiSeat = pData.seatId2UI(pPutSeatID)
            let card = msg.card

            this.view.onePutCard(uiSeat, card)

            // if (pPutSeatID !== pData.pMySeatID) {
                this.view.updateHandCard(uiSeat, players[pPutSeatID], pCurSeatID === pData.pMySeatID)
            // } else {
            //
            // }

            appInstance.gameAgent().mjUtil().putCardSound(card)
        },

        AutoPlayProto: function () {
            let pData = appInstance.dataManager().getPlayData()
            let selfInfo = pData.getSelfInfo()
            let uiSeat = pData.seatId2UI(selfInfo.pSeatID)
            this.view.updateHandCard(uiSeat, selfInfo)
        },

        DrawCardProto: function (msg) {
            let pCurSeatID = msg.pCurSeatID
            let pData = appInstance.dataManager().getPlayData()
            let players = pData.players
            let uiSeat = pData.seatId2UI(pCurSeatID)
            this.view.updateHandCard(uiSeat,players[pCurSeatID], true)

        },

        InitCardProto: function () {
            let pData = appInstance.dataManager().getPlayData()
            let players = pData.players

            for (let k in players) {
                let uiSeat = pData.seatId2UI(k)
                this.view.updateHandCard(uiSeat,players[k])
            }
        },

        initView: function () {
            let tData = appInstance.dataManager().getPlayData()
            this.view.initView(tData)
        },

        onRegister: function () {
            this.initView()

            this.updateView()
        },

        onRemove: function () {
        },

        showLiuJu: function (body) {
            cc.log('--------------------------------------------showLiuJu--------------------------------------body: ' + JSON.stringify(body))
            let isLiuJu = body.pIsLiuJu == 1 ? true : false
            if(isLiuJu)
                this.view.showLiuJu()

        }

    })

    return DeskCardLayerMdt
})