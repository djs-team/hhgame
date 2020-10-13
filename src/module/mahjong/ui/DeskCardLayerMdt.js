
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
                    this.runDirection()
                    break
                case TableEvent.prePutCard:
                    this.prePutCard(body)
                    break
                case TableEvent.PlayerSelectProto:
                    this.PlayerSelectProto(body)
                    this.runDirection()
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

            this.updateDeckCard()
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
                let seatID = msg.updateHandCard.seatID
                let pCurSeatID = pData.tableData.pCurSeatID
                let isTurn = (pCurSeatID === parseInt(seatID))
                let player = players[seatID]
                let uiSeat = pData.seatId2UI(seatID)
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

            if (pPutSeatID !== pData.pMySeatID) {
                this.view.updateHandCard(uiSeat, players[pPutSeatID], pCurSeatID === pData.pMySeatID)
            }

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

            let nDeckCardNum = pData.tableData.nDeckCardNum
            this.updateDeckCard(nDeckCardNum)
        },

        InitCardProto: function () {
            let pData = appInstance.dataManager().getPlayData()
            let players = pData.players
            for (let k in players) {
                let uiSeat = pData.seatId2UI(k)
                players[k].showCards = []
                this.view.updateHandCard(uiSeat,players[k])
            }
            let nDeckCardNum = pData.tableData.nDeckCardNum
            this.updateDeckCard(nDeckCardNum)
        },

        updateDeckCard: function (nDeckCardNum) {
            nDeckCardNum = nDeckCardNum || appInstance.dataManager().getPlayData().tableData.nDeckCardNum
            this.view.updateDeckCard(nDeckCardNum)
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
        }

    })

    return DeskCardLayerMdt
})