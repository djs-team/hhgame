
/**
 * 比赛场准备消息
 */
load('game/msghandler/MatchReadyProto', function () {
    let baseProto = include('public/network/BaseProto')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let Event = TableConfig.Event
    let proto = baseProto.extend({
        _name: 'MatchReadyProto',
        _offMsgId: 9,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            appInstance.sendNotification(Event.MatchReadyProto, msg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = [
                { key: 'gid', type: this._byteType.Int},
                { key: 'pRoomID', type: this._byteType.UTF8},//房间类型R1和R2
                { key: 'pMode', type: this._byteType.Int},//房间mode
                { key: 'pTableID', type: this._byteType.UTF8},//牌桌实例id
                { key: 'pReady', type: this._byteType.Int},// 0 准备  1 不准备
            ]

            this._reData = [
                { key: 'fPlayer', type: this._byteType.Int},//准备的玩家
                { key: 'isReady', type: this._byteType.Int},//准备的状态  0 准备  1 不准备  2 玩家不存在  3 房间不存在  4 牌桌不存在  5 牌桌已经开始 6俱乐部已开房间已达最大值 7俱乐部已被禁用
                { key: 'pSeatId', type: this._byteType.Int},//玩家座位号
            ]
        }
    })
    return proto
})