
/**
 * 出牌消息结构体
 */
load('game/msghandler/DrawCardProto', function () {
    let baseProto = include('public/network/BaseProto')
    let Event = include('module/mahjong/common/TableConfig').Event
    let proto = baseProto.extend({
        _name: 'DrawCardProto',
        _offMsgId: 8,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            let pData = appInstance.dataManager().getPlayData()
            let tData = pData.tableData
            let saveKey = [
                'nDeckCardNum',
                'pCurSeatID',
                'pTStatus',
                'pActions'
            ]
            pData.saveTableData(msg,saveKey)

            let curPlayer = pData.players[msg.pCurSeatID]

            if (msg.pCurSeatID === pData.pMySeatID) {
                appInstance.gameAgent().mjUtil().sortCard(curPlayer.handCards)
                let card = {
                    'nCardColor': msg.nCardColor,
                    'nCardNumber': msg.nCardNumber,
                }
                curPlayer.handCards.unshift(card)
                if (curPlayer.pIsTing) {
                    tData.pMustOutCard = []
                    tData.pMustOutCard.push(card)
                }
            } else {
                curPlayer.handCardCount += 1
            }

            let tmpMsg = {}
            tmpMsg.pCurSeatID = msg.pCurSeatID
            appInstance.sendNotification(Event.DrawCardProto, tmpMsg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME_Re(this._offMsgId)
            this._seData = [
            ]

            this._reData = [
                { key: 'nDeckCardNum', type: this._byteType.Int}, //剩余牌墙数量
                { key: 'pCurSeatID', type: this._byteType.Int}, //座位号
                { key: 'nCardColor', type: this._byteType.Int}, //摸牌花色
                { key: 'nCardNumber', type: this._byteType.Int}, //牌值
                { key: 'handCardCount', type: this._byteType.Int}, //摸牌人手牌数量
                { key: 'pTStatus', type: this._byteType.Int}, //牌桌当前状态
                { key: 'pActions', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbSelectAction()}
            ]
        }
    })
    return proto
})