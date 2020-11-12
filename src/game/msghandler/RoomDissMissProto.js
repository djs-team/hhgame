
/**
 * 出牌消息结构体
 */
load('game/msghandler/RoomDissMissProto', function () {
    let baseProto = include('public/network/BaseProto')
    let Event = include('module/mahjong/common/TableConfig').Event
    let proto = baseProto.extend({
        _name: 'RoomDissMissProto',
        _offMsgId: 16,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            //appInstance.sendNotification(Event.RoomDissMissProto, msg)
            appInstance.gameAgent().Tips('当前牌桌已由管理员解散，详情请咨询客服')
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = [
            ]


            this._reData = [
                { key: 'fromPlayerPid;', type: this._byteType.Int},

            ]
        }
    })
    return proto
})