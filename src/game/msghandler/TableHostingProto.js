
/**
 * 出牌消息结构体
 */
load('game/msghandler/TableHostingProto', function () {
    let baseProto = include('public/network/BaseProto')
    let proto = baseProto.extend({
        _name: 'TableHostingProto',
        _offMsgId: 26,
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
                { key: 'pHosting', type: this._byteType.Int},//0解除托管状态 1托管
                { key: 'pTableID', type: this._byteType.UTF8},//桌子ID
                { key: 'pHostingExt', type: this._byteType.UTF8},//冗余字段
            ]

            this._reData = [
                { key: 'pHosting', type: this._byteType.Int},//1 托管  2取消托管成功
                { key: 'pSeatID', type: this._byteType.Int},//当前人座位号
                { key: 'pHostingExt', type: this._byteType.UTF8},//冗余字段
            ]
        }
    })
    return proto
})