
/**
 * 发牌消息结构体
 */
load('game/msghandler/InitCardProto', function () {
    let baseProto = include('public/network/BaseProto')
    let Event = include('module/mahjong/common/TableConfig').Event
    let proto = baseProto.extend({
        _name: 'InitCardProto',
        _offMsgId: 5,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)

            let pData = appInstance.dataManager().getPlayData()
            let tData = pData.tableData
            let players = pData.players

            for (let i = 0; i < players.length; ++i) {
                players[i].showCards = []
            }


            let saveKey = [
                'nZhuangSeatID',
                'nDeckCardNum'
            ]

            appInstance.dataManager().getPlayData().saveTableData(msg,saveKey)

            appInstance.gameAgent().mjUtil().sortCard(msg.handCards)
            appInstance.dataManager().getPlayData().getPlayer(msg.pMySeatID).handCards = msg.handCards
            appInstance.sendNotification(Event.InitCardProto)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME_Re(this._offMsgId)
            this._seData = [
            ]

            this._reData = [
                { key: 'handCards', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbCardsInfo()},
                { key: 'nZhuangSeatID', type: this._byteType.Int},
                { key: 'nDeckCardNum', type: this._byteType.Int},
                { key: 'pMySeatID', type: this._byteType.Int}
            ]
        }
    })
    return proto
})