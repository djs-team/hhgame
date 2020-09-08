
/**
 * 加岗消息结构体
 */
load('game/msghandler/JiaGangProto', function () {
    let baseProto = include('public/network/BaseProto')
    let proto = baseProto.extend({
        _name: 'JiaGangProto',
        _offMsgId: 20,
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
                { key: 'tableID', type: this._byteType.UTF8},
                { key: 'seatID', type: this._byteType.Int},
                { key: 'piaoScore', type: this._byteType.Int}
            ]

            this._reData = [
                { key: 'seatID', type: this._byteType.Int},
                { key: 'piaoScore', type: this._byteType.Int},
                { key: 'isSelectPiao', type: this._byteType.Int},
                { key: 'extends', type: this._byteType.UTF8}
            ]
        }
    })
    return proto
})