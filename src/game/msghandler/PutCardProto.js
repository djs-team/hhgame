
/**
 * 出牌消息结构体
 */
load('game/msghandler/PutCardProto', function () {
    let baseProto = include('public/network/BaseProto')
    let Event = include('module/mahjong/common/TableConfig').Event
    let proto = baseProto.extend({
        _name: 'PutCardProto',
        _offMsgId: 7,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            let pData = appInstance.dataManager().getPlayData()
            if ( msg.pResult === 0) {
                let saveKey = [
                    'pPutSeatID',
                    'pTStatus',
                    'pCurSeatID',
                ]
                pData.saveTableData(msg,saveKey)

                let card = {
                    nCardColor: msg.nCardColor,
                    nCardNumber: msg.nCardNumber
                }

                let putPlayer = pData.players[msg.pPutSeatID]
                putPlayer.handCardCount = msg.handCardCount
                putPlayer.putCards.push(card)

                pData.tableData.pActions = msg.pActions
                pData.tableData.lastPutCard = card
                pData.tableData.pMustOutCard = []

                let tmpMsg = {}
                tmpMsg.card = card
                appInstance.sendNotification(Event.PutCardProto, tmpMsg)
            } else {
                // appInstance.gameAgent().Tips('出牌异常！ code is ' + msg.pResult)
                appInstance.gameAgent().tcpGame().playerOnline()
                // appInstance.gameNet().connect()
                // let card = {
                //     nCardColor: msg.nCardColor,
                //     nCardNumber: msg.nCardNumber
                // }
                // let selfInfo = pData.getSelfInfo()
                // if (selfInfo.pSeatID === msg.pPutSeatID && selfInfo.pSeatID === msg.pCurSeatID) {
                //     selfInfo.handCards.push(card)
                //     appInstance.gameAgent().mjUtil().sortCard(selfInfo.handCards)
                //     appInstance.sendNotification(Event.updateSelfHandCard)
                // }
            }
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME_Re(this._offMsgId)
            this._seData = [
                { key: 'pTableID', type: this._byteType.UTF8},
                { key: 'nSeatID', type: this._byteType.Int},
                { key: 'nCardColor', type: this._byteType.Int}, //花色
                { key: 'nCardNumber', type: this._byteType.Int}, //牌值
            ]

            this._reData = [
                { key: 'pPutSeatID', type: this._byteType.Int}, //出牌人座位ID
                { key: 'nCardColor', type: this._byteType.Int},
                { key: 'nCardNumber', type: this._byteType.Int},
                { key: 'handCardCount', type: this._byteType.Int},// 出牌人手牌数量
                { key: 'pTStatus', type: this._byteType.Int},
                { key: 'pCurSeatID', type: this._byteType.Int}, //当前操作人座位
                { key: 'pActions', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbSelectAction()},
                { key: 'pResult', type: this._byteType.Int}
            ]
        }
    })
    return proto
})