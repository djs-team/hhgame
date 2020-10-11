
/**
 * 比赛状态消息
 */
load('game/msghandler/MatchResultProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let GameConfig = include('game/config/GameConfig')
    let Event = include('module/mahjong/common/TableConfig').Event
    let proto = baseProto.extend({
        _name: 'MatchResultProto',
        _offMsgId: 27,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)

            appInstance.sendNotification(Event.MatchResultProto, msg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = [
            ]

            let rewardList = [
                { key: 'propType', type: this._byteType.Int},//道具大类
                { key: 'propCode', type: this._byteType.Int},//道具编码
                { key: 'propNum', type: this._byteType.Int},//奖励道具数量
            ]

            this._reData = [
                { key: 'mcState', type: this._byteType.Int},//状态  0 获奖  1淘汰
                { key: 'tableRanking', type: this._byteType.Int},//牌桌排名 ，用于淘汰
                { key: 'oldPerNum', type: this._byteType.Int},//原人数
                { key: 'proPerNum', type: this._byteType.Int},//晋级人数
                { key: 'ranking', type: this._byteType.Int},//获奖排名
                { key: 'playerName', type: this._byteType.UTF8},//玩家姓名
                { key: 'matchName', type: this._byteType.UTF8},//比赛名称
                { key: 'pTableID', type: this._byteType.UTF8},// 牌桌id
                { key: 'mcExtend', type: this._byteType.UTF8},//冗余字段
                { key: 'mcRewardList', type: this._byteType.Barray, proto: rewardList},//获奖时的奖励信息
            ]
        }
    })
    return proto
})