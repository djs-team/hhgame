
/**
 * 牌局结算消息结构体
 */
load('game/msghandler/GameFenZhangProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let GameConfig = include('game/config/GameConfig')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event
    let proto = baseProto.extend({
        _name: 'GameFenZhangProto',
        _offMsgId: 14,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {

            appInstance.sendNotification(TableEvent.GameFenZhangProto, msg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME(this._offMsgId)
            this._seData = []


            this._reData = [
                /*{ key: 'seatID', type: this._byteType.Int},//座位号
                { key: 'nCardColor', type: this._byteType.Int},
                { key: 'nCardNumber', type: this._byteType.Int},*/
            ]
        }
    })
    return proto
})