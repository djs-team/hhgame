
/**
 * 出牌消息结构体
 */
load('game/msghandler/MatchTableChangeProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event
    let proto = baseProto.extend({
        _name: 'MatchTableChangeProto',
        _offMsgId: 26,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)

            if (msg.pMode === 1 || msg.pMode === 9 || msg.pMode === 3) {
                msg.pPlayerNum = 4
            } else if (msg.pMode === 2) {
                msg.pPlayerNum = 2
            }

            appInstance.dataManager().setPlayData(msg)

            let curSceneName = appInstance.sceneManager().getCurSceneName()
            if (curSceneName === 'MjPlayScene') {
                appInstance.sendNotification(TableEvent.clearTableView)
                appInstance.sendNotification(TableEvent.initTableView)
                appInstance.sendNotification(TableEvent.TableChangeProto)
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
                { key: 'pPhoto', type: this._byteType.UTF8},//系统默认头像
                { key: 'win', type: this._byteType.Int},//赢得局数
                { key: 'lose', type: this._byteType.Int},//输的局数
                { key: 'exp', type: this._byteType.Int},//玩家
                { key: 'vip', type: this._byteType.Byte},//vip等级
                { key: 'pSeatID', type: this._byteType.Byte},//座位号
                { key: 'pRole', type: this._byteType.Int},//角色
                { key: 'netIp', type: this._byteType.UTF8},// 玩家ip地址
            ]

            this._reData = [
                { key: 'pPlayer', type: this._byteType.Barray, proto: playerArray},//玩家信息
                { key: 'pTableID', type: this._byteType.UTF8},//牌桌id
                { key: 'pMySeatID', type: this._byteType.Int},//玩家座位id
                { key: 'pChangePid', type: this._byteType.Int},//变化玩家的pid
                { key: 'pChangeSeatID', type: this._byteType.Int},//变化玩家的座位id
                { key: 'pEnterOrQuite', type: this._byteType.Int},//进入或者退出  1  进入  2  断线重连 0 退出
                { key: 'pKeyPrivateTable', type: this._byteType.UTF8},//私房编号id
                { key: 'pIsOpenSoundApp', type: this._byteType.Int},//是否开启语音   0 开  1 不开
                { key: 'pSoundAppId', type: this._byteType.UTF8},//语音组链接id
                { key: 'pDongSeatID', type: this._byteType.Int},//东  位置 seatid
                { key: 'pIsRound', type: this._byteType.Int},//是按 局打  还是按 圈打  0  圈  1 局
                { key: 'pAllRound', type: this._byteType.Int},//总的圈数或局数
                { key: 'pCurRound', type: this._byteType.Int},//当前圈数或局数
                { key: 'pChoiceRule', type: this._byteType.UTF8},//牌桌选择的规则
                { key: 'pTableStatus', type: this._byteType.Int},//牌桌阶段
                { key: 'pRoomID', type: this._byteType.UTF8},//房间类型R1和R2
                { key: 'pMode', type: this._byteType.Int},//房间mode1代表四人2代表两人
                { key: 'pPlayMode', type: this._byteType.UTF8},//规则 名称 id
                { key: 'isWaitingPlayerDissmissTable', type: this._byteType.Int},// 0  不是  1 是
                { key: 'isOpenGPS', type: this._byteType.Int},//是否开启GPS   0 开  1 不开
                { key: 'dismissTablePlayer', type: this._byteType.Int},//解散房间发起者   pid   -1 没有
                { key: 'timer', type: this._byteType.Int},//比赛场机器人等待时间
                { key: 'pMatchNum', type: this._byteType.UTF8},//比赛  多少进多少  轮局
            ]
        }
    })
    return proto
})