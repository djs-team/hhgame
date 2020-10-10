
/**
 * 比赛总结算消息
 */
load('game/msghandler/MatchResultBigProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let GameConfig = include('game/config/GameConfig')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event
    let proto = baseProto.extend({
        _name: 'MatchResultBigProto',
        _offMsgId: 39,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = []

            let playerProto = [
                { key: 'pid', type: this._byteType.Int},
                { key: 'pCoins', type: this._byteType.Int},
                { key: 'pOffsetCoins', type: this._byteType.Int},
                { key: 'pSeatID', type: this._byteType.Byte},

                { key: 'pZuoZhuang', type: this._byteType.Int},//坐庄次数
                { key: 'pHuTimes', type: this._byteType.Int},//胡次数
                { key: 'pDianPao', type: this._byteType.Int},//点炮次数
                { key: 'pMoBao', type: this._byteType.Int},//摸宝次数
                { key: 'pBaozhongBao', type: this._byteType.Int},//宝中宝次数

                { key: 'pGameResultExtend', type: this._byteType.UTF8},//json  扩展字段
            ]

            this._reData = [
                { key: 'code', type: this._byteType.Byte},//0 成功
                { key: 'pPlayer', type: this._byteType.Barray, proto: playerProto},
                { key: 'pGameResultExtend', type: this._byteType.UTF8},//扩展字段 winPid   dianPaoPid   tableId
            ]
        }
    })
    return proto
})