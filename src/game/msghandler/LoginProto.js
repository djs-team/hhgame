
/**
 * 登录请求消息结构体
 */
load('game/msghandler/LoginProto', function () {
    let baseProto = include('public/network/BaseProto')
    let proto = baseProto.extend({
        _name: 'LoginProto',
        _offMsgId: 1,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            if (msg.status === 0) {
                let HallScene = include('game/ui/scene/HallScene')
                appInstance.sceneManager().replaceScene(new HallScene())
            }
            appInstance.gameAgent().setLoginOk(true)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_BASIC(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_BASIC_Re(this._offMsgId)
            this._seData = [
                { key: 'gid', type: this._byteType.Byte},
                { key: 'key', type: this._byteType.UTF8}, //秘钥
                { key: 'channel', type: this._byteType.Int} //渠道
            ]

            this._reData = [
                { key: 'status', type: this._byteType.Short}
            ]
        }
    })
    return proto
})