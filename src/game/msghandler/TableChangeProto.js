
/**
 * 出牌消息结构体
 */
load('game/msghandler/TableChangeProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event
    let proto = baseProto.extend({
        _name: 'TableChangeProto',
        _offMsgId: 8,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)

            if (msg.pMode === 1) {
                msg.pPlayerNum = 4
            } else if (msg.pMode === 2) {
                msg.pPlayerNum = 2
            }

            appInstance.dataManager().setPlayData(msg)

            let curSceneName = appInstance.sceneManager().getCurSceneName()
            if (curSceneName === 'MjPlayScene') {
                appInstance.sendNotification(TableEvent.clearTableView)
                appInstance.sendNotification(TableEvent.initTableView)
            } else {
                let PlayGameMj = include('module/mahjong/ui/MjPlayScene')
                appInstance.sceneManager().replaceScene(new PlayGameMj())
            }

            appInstance.sendNotification(GameEvent.DIALOG_HIDE_ALL)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = [
            ]

            let playerArray = [
                { key: 'pid', type: this._byteType.Int},//玩家ID
                { key: 'nickName', type: this._byteType.UTF8},//昵称
                { key: 'coins', type: this._byteType.UTF8},//金币数量
                { key: 'robotModel', type: this._byteType.Byte},//是否是机器人
                { key: 'age', type: this._byteType.Byte},//年龄
                { key: 'sex', type: this._byteType.Byte},//系别
                { key: 'photo', type: this._byteType.UTF8},//系统默认头像
                { key: 'sdkPhotoUrl', type: this._byteType.UTF8},//微信头像链接
                { key: 'exp', type: this._byteType.Int},//玩家
                { key: 'vip', type: this._byteType.Byte},//vip等级
                { key: 'pSeatID', type: this._byteType.Byte},//座位号
                { key: 'netIp', type: this._byteType.UTF8},// 玩家ip地址
                { key: 'win', type: this._byteType.Int},//赢得局数
                { key: 'lose', type: this._byteType.Int},//输的局数
                { key: 'pRole', type: this._byteType.Int},//角色
            ]

            this._reData = [
                { key: 'pPlayer', type: this._byteType.Barray, proto: playerArray},//玩家信息
                { key: 'pTableID', type: this._byteType.UTF8},//牌桌id
                { key: 'pMySeatID', type: this._byteType.Int},//玩家座位id
                { key: 'pChangePid', type: this._byteType.Int},//变化玩家的pid
                { key: 'pChangeSeatID', type: this._byteType.Int},//变化玩家的座位id
                { key: 'pEnterOrQuite', type: this._byteType.Int},//进入或者退出  1  进入  2  断线重连
                { key: 'pKeyPrivateTable', type: this._byteType.UTF8},//私房编号id
                { key: 'pSoundAppId', type: this._byteType.UTF8},//语音组链接id
                { key: 'pIsOpenSoundApp', type: this._byteType.Int},//是否开启语音   0 开  1 不开
                { key: 'pDongSeatID', type: this._byteType.Int},//东  位置 seatid
                { key: 'pChoiceRule', type: this._byteType.UTF8},//牌桌选择的规则
                { key: 'pTableStatus', type: this._byteType.Int},//牌桌阶段
                { key: 'pRoomID', type: this._byteType.UTF8},//房间类型R1和R2
                { key: 'pMode', type: this._byteType.Int},//房间mode1代表四人2代表两人
                { key: 'pPlayMode', type: this._byteType.UTF8},//规则 名称 id
                { key: 'pGameType', type: this._byteType.UTF8},//麻将 场次M1新手场M2代表初级场M3高级场M4大师场
                { key: 'pBaseCoin', type: this._byteType.Int},//底分
                { key: 'pTaskFlag', type: this._byteType.Int},//0：代表没有课领取任务1：代表有可领取任务
            ]
        }
    })
    return proto
})