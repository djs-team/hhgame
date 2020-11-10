
/**
 * 出牌消息结构体
 */
load('game/msghandler/GamingProto', function () {
    let baseProto = include('public/network/BaseProto')
    //let Event = include('module/mahjong/common/TableConfig').Event
    let proto = baseProto.extend({
        _name: 'GamingProto',
        _offMsgId: 50,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            //appInstance.gameAgent().Tips('取消成功！')
            console.log('====================gamingProto'+JSON.stringify(msg));
            //appInstance.sendNotification(Event.GamingProto, msg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME_Re(this._offMsgId)
            this._seData = [
                { key: 'type', type: this._byteType.Int},//1代表常用语2代表普通表情3.代表魔法表情
                { key: 'num', type: this._byteType.Int},//语音和文字的编号
                { key: 'toPid', type: this._byteType.Int},//针对哪个玩家发送，表情专用
                { key: 'tableId', type: this._byteType.UTF8},//牌桌号
            ]

            this._reData = [
                { key: 'type', type: this._byteType.Int},//1代表语言2代表表情3代表魔法表情
                { key: 'num', type: this._byteType.Int},//表情和文字的编号
                { key: 'toPid', type: this._byteType.Int},//针对哪个玩家发送，魔法表情专用
                { key: 'fromPid', type: this._byteType.Int},//来自哪个玩家
                { key: 'code', type: this._byteType.Int},//0正常其他失败(这个错误码到时候维护一个公共的。到时候统一发给你的)
                { key: 'coin', type: this._byteType.UTF8},//0正常其他失败(这个错误码到时候维护一个公共的。到时候统一发给你的)
            ]
        }
    })
    return proto
})