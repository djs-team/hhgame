
/**
 * 取消匹配
 */
load('game/msghandler/EnterTableCancelProto', function () {
    let baseProto = include('public/network/BaseProto')
    let proto = baseProto.extend({
        _name: 'EnterTableCancelProto',
        _offMsgId: 3,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            if (msg.code === 0) {
                appInstance.gameAgent().Tips('取消成功！')
            } else {
                appInstance.gameAgent().Tips('取消失败！ error code :' + msg.code)
            }
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = [
            ]

            this._reData = [
                { key: 'code', type: this._byteType.Byte},//0 成功
            ]
        }
    })
    return proto
})