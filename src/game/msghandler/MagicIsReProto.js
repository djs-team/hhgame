
/**
 * 出牌消息结构体
 */
load('game/msghandler/MagicIsReProto', function () {
    let baseProto = include('public/network/BaseProto')
    let proto = baseProto.extend({
        _name: 'MagicIsReProto',
        _offMsgId: 51,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME_Re(this._offMsgId)
            this._seData = [
                { key: 'tableId', type: this._byteType.UTF8},
                { key: 'type', type: this._byteType.Int} //0.允许接收1.禁止接收
            ]

            this._reData = [
                { key: 'status', type: this._byteType.Int},
                { key: 'type', type: this._byteType.Int}
            ]
        }
    })
    return proto
})