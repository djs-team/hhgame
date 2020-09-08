
/**
 * 出牌消息结构体
 */
load('game/msghandler/PlayerOnlineProto', function () {
    let baseProto = include('public/network/BaseProto')
    let proto = baseProto.extend({
        _name: 'PlayerOnlineProto',
        _offMsgId: 10,
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
                { key: 'cStatus', type: this._byteType.Byte}, //0 代表上线。
            ]

            this._reData = [
                { key: 'cStatus', type: this._byteType.Byte},// 0 代表啥牌桌都没有。
            ]
        }
    })
    return proto
})