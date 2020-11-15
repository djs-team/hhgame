
/**
 * 跑马灯消息构体
 */
load('game/msghandler/MarqueeProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let proto = baseProto.extend({
        _name: 'MarqueeProto',
        _offMsgId: 8,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            appInstance.sendNotification(GameEvent.MARQUEE_TCP_BACK, msg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_MANAGER_ID(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_MANAGER_ID_Re(this._offMsgId)
            this._seData = [
            ]

            this._reData = [
                { key: 'delayTime', type: this._byteType.Int},//间隔时间
                { key: 'showCount', type: this._byteType.Int},//显示次数
                { key: 'messageText', type: this._byteType.UTF8},//跑马灯信息
                { key: 'beginTime', type: this._byteType.UTF8},//开始时间
                { key: 'endTime', type: this._byteType.UTF8},//结束时间
                { key: 'grade', type: this._byteType.Int},//权重 1 最低 越大 越高    10000 清除 之前
                { key: 'pStatus', type: this._byteType.Int},//处理状态
            ]
        }
    })
    return proto
})