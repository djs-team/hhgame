
/**
 * 出牌消息结构体
 */
load('game/msghandler/MatchEnterTableCancelProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let GameConfig = include('game/config/GameConfig')
    let proto = baseProto.extend({
        _name: 'MatchEnterTableCancelProto',
        _offMsgId: 33,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)

            if (msg.code === 0) {
                appInstance.gameAgent().Tips('取消成功！')
            } else {
                appInstance.gameAgent().Tips('取消失败！error code: ' + msg.code)
            }

        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = [
            ]

            this._seData = [
                { key: 'gid', type: this._byteType.Int},
                { key: 'mjChannel', type: this._byteType.UTF8},//麻将 channel
                { key: 'roomId', type: this._byteType.UTF8},//比赛场 matchId
                { key: 'roomMode', type: this._byteType.Int},//房间mode
                { key: 'keyPrivateTable', type: this._byteType.UTF8},//如果是退出私房  需要加私房id
                { key: 'delPlayerId', type: this._byteType.Int},//被删玩家id
            ]

            this._reData = [
                { key: 'code', type: this._byteType.Byte},
                { key: 'extend', type: this._byteType.UTF8},//扩展字段
            ]
        }
    })
    return proto
})