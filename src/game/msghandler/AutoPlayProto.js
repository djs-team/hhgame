
/**
 * 自动出牌消息结构体
 */
load('game/msghandler/AutoPlayProto', function () {
    let baseProto = include('public/network/BaseProto')
    let Event = include('module/mahjong/common/TableConfig').Event
    let proto = baseProto.extend({
        _name: 'AutoPlayProto',
        _offMsgId: 25,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            if (msg.pChoiceType === 1) {
                return
            }
            let pData = appInstance.dataManager().getPlayData()
            let selfInfo = pData.getSelfInfo()

            if (msg.pPutSeatID !== selfInfo.pSeatID) {
                return
            }

            let card = {
                nCardColor: msg.nCardColor,
                nCardNumber: msg.nCardNumber
            }

            appInstance.gameAgent().mjUtil().removeCard(selfInfo.handCards, card)
            appInstance.gameAgent().mjUtil().sortCard(selfInfo.handCards)

            appInstance.sendNotification(Event.AutoPlayProto)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME_Re(this._offMsgId)
            this._seData = []

            this._reData = [
                { key: 'pPutSeatID', type: this._byteType.Int}, //当前人座位号
                { key: 'pChoiceType', type: this._byteType.Int},//1 选项  2出牌
                { key: 'pChoiceSelection', type: this._byteType.Int},//选项 选择的action
                { key: 'nCardColor', type: this._byteType.Int},
                { key: 'nCardNumber', type: this._byteType.Int},
                { key: 'pAutoExtend', type: this._byteType.UTF8}//冗余字段
            ]
        }
    })
    return proto
})