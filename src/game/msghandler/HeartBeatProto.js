
/**
 * 心跳消息
 */
load('game/msghandler/HeartBeatProto', function () {
    let baseProto = include('public/network/BaseProto')
    let proto = baseProto.extend({
        _name: 'HeartBeatProto',
        _offMsgId: 1,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            appInstance.gameAgent().reSetHeartBeatTimes()
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_MODULE_CLIENT(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_MODULE_CLIENT_Re(this._offMsgId)
            this._seData = [
            ]

            this._reData = [
            ]
        }
    })
    return proto
})