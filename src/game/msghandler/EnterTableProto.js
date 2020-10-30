
/**
 * 加入牌桌消息结构体
 */
load('game/msghandler/EnterTableProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let GameConfig = include('game/config/GameConfig')
    let proto = baseProto.extend({
        _name: 'EnterTableProto',
        _offMsgId: 2,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            if (msg.code !== 0) {
                let curSceneName = appInstance.sceneManager().getCurSceneName()
                if (curSceneName !== 'HallScene') {
                    let HallScene = include('game/ui/scene/HallScene')
                    appInstance.sceneManager().replaceScene(new HallScene())
                }
                if (msg.pExtend === 'gameResult') {
                    appInstance.gameAgent().Tips(GameConfig.mjError[msg.code])

                }
                // appInstance.gameAgent().Tips(GameConfig.mjError[msg.code])
                return
            }
            let dialogMsg = {
                ViewType: 1,
                SayText: '正在为您匹配玩家，请稍等！',
                MidBtnName: '取消'
            }
            dialogMsg.MidBtnClick = function () {
                appInstance.gameAgent().tcpGame().cancelEnterTable()
            }
            appInstance.gameAgent().addDialogUI(dialogMsg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = [
                { key: 'gid', type: this._byteType.Int},
                { key: 'mjChannel', type: this._byteType.UTF8},//麻将 channel
                { key: 'roomMode', type: this._byteType.Int},//房间mode1代表四人2代表两人
                { key: 'gameType', type: this._byteType.UTF8},//麻将 场次M1新手场M2代表初级场M3高级场M4大市场M5快速开始
                { key: 'roomId', type: this._byteType.UTF8},//房间人数标志R14人场次R2两人场次
                { key: 'ruleName', type: this._byteType.UTF8},//玩法名称例如肇源传zhaoyuan
                { key: 'pExtend', type: this._byteType.UTF8},//扩展
            ]

            this._reData = [
                { key: 'code', type: this._byteType.Byte},//0 成功
                { key: 'tableInstanceId', type: this._byteType.UTF8},//牌桌id
                { key: 'pExtend', type: this._byteType.UTF8},//扩展
            ]
        }
    })
    return proto
})