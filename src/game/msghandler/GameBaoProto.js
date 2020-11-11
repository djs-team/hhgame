
/**
 * 换宝消息结构体
 */
load('game/msghandler/GameBaoProto', function () {
    let baseProto = include('public/network/BaseProto')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event
    let proto = baseProto.extend({
        _name: 'GameBaoProto',
        _offMsgId: 10,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            appInstance.sendNotification(TableEvent.GameBaoProto, msg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME_Re(this._offMsgId)
            this._seData = [

            ]

            this._reData = [
                { key: 'nCardColor', type: this._byteType.Int}, //摸牌花色
                { key: 'nCardNumber', type: this._byteType.Int}, //牌值
            ]
        }
    })
    return proto
})