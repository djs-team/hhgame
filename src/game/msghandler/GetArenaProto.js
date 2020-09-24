
/**
 * 出牌消息结构体
 */
load('game/msghandler/GetArenaProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let GameConfig = include('game/config/GameConfig')
    let proto = baseProto.extend({
        _name: 'GetArenaProto',
        _offMsgId: 23,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)

            appInstance.sendNotification(GameEvent.GET_ARENAMESSAGE,msg)

        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = [
                { key: 'channel', type: this._byteType.UTF8},//麻将 channel
                { key: 'matchType', type: this._byteType.Int},//1金币赛 2大奖赛 3会员赛
            ]

            let rewardInfoArray = [
                { key: 'propType', type: this._byteType.Int},//道具大类
                { key: 'propCode', type: this._byteType.Int},//道具编码
                { key: 'propNum', type: this._byteType.Int},//道具数量
            ]

            let rewardMessageArray = [
                { key: 'startRanking', type: this._byteType.Int},//开始排名
                { key: 'endRanking', type: this._byteType.Int},//结束排名
                { key: 'rewardInfoList', type: this._byteType.Barray, proto: rewardInfoArray}, //玩家数据
            ]

            let matchArray = [
                { key: 'matchId', type: this._byteType.UTF8},//比赛id
                { key: 'matchtitle', type: this._byteType.UTF8},//标题
                { key: 'wanFaID', type: this._byteType.UTF8},//服务器rule配置文件信息，如songyuankuaiting
                { key: 'wanFaName', type: this._byteType.UTF8},///服务器rule配置文件信息，如松原快听
                { key: 'starttime', type: this._byteType.UTF8},//开始时间
                { key: 'endtime', type: this._byteType.UTF8},//结束时间
                { key: 'format', type: this._byteType.UTF8},//赛制
                { key: 'rule', type: this._byteType.UTF8},//规则
                { key: 'gameplayoptions', type: this._byteType.UTF8},//玩法
                { key: 'consumptionType', type: this._byteType.Int},//比赛场入场费消耗类型 1金币2钻石
                { key: 'matchfee', type: this._byteType.Int},//比赛场入场费数量
                { key: 'state', type: this._byteType.Int},//状态 ；0 尚未开始比赛 不可报名  1正在比赛可以报名
                { key: 'matchplayersnum', type: this._byteType.Int},//参与人数
                { key: 'matchMode', type: this._byteType.Int},//服务器比赛mode
                { key: 'extens', type: this._byteType.UTF8},//预留字段
                { key: 'rewordmessageList', type: this._byteType.Barray, proto: rewardMessageArray}, //玩家数据
            ]

            this._reData = [

                { key: 'matchList', type: this._byteType.Barray, proto: matchArray}, //比赛集合
            ]

        }
    })
    return proto
})