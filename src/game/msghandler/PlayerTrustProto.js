
/**
 * 出牌消息结构体
 */
load('game/msghandler/PlayerTrustProto', function () {
    let baseProto = include('public/network/BaseProto')
    let proto = baseProto.extend({
        _name: 'PlayerTrustProto',
        _offMsgId: 13,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = [
            ]

            this._reData = [
                { key: 'nSeatID', type: this._byteType.Int},//托管玩家座位号
                { key: 'pTrustType', type: this._byteType.Byte},//托管状态
                { key: 'pTrustTIme', type: this._byteType.UTF8},
            ]
        }
    })
    return proto
})