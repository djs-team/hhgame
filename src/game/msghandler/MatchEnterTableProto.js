
/**
 * 出牌消息结构体
 */
load('game/msghandler/MatchEnterTableProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let GameConfig = include('game/config/GameConfig')
    let Event = include('module/mahjong/common/TableConfig').Event
    let proto = baseProto.extend({
        _name: 'MatchEnterTableProto',
        _offMsgId: 24,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            let userData = appInstance.dataManager().getUserData()
            let dialogMsg

            switch (msg.mcState) {
                case 0:
                     dialogMsg = {
                        ViewType: 1,
                        SayText: '比赛不可取消 '
                    }
                    appInstance.sendNotification(GameEvent.DIALOG_HIDE_ALL)
                    appInstance.gameAgent().addDialogUI(dialogMsg)

                    break
                case 1:
                    //为避免遮挡 晋级就不再这里面显示了 显示到大厅去
                    appInstance.sendNotification(Event.MatchJinjiGaming, msg)
                    appInstance.dataManager().getUserData().MatchJinjiGaming = msg
                    break
                case 2:
                    appInstance.gameAgent().Tips('MatchEnterTableProto 这条消息state为 2 淘汰')
                    break
                case 3:
                     dialogMsg = {
                        ViewType: 1,
                        SayText: '比赛可以取消 ',
                        MidBtnName: '取消'
                    }
                    dialogMsg.MidBtnClick = function () {
                        let send = {}
                        send.gid = msg.mcGid
                        send.mjChannel = msg.mcMjChannel
                        send.roomId = msg.mcRoomId
                        send.roomMode = msg.mcRoomMode
                        send.keyPrivateTable = ''
                        send.delPlayerId = userData.pid
                        appInstance.gameAgent().tcpGame().cancelEnterTableMatch(send)
                    }
                    appInstance.sendNotification(GameEvent.DIALOG_HIDE_ALL)
                    appInstance.gameAgent().addDialogUI(dialogMsg)
                    break
                case 4:
                    appInstance.gameAgent().Tips('比赛已被销毁！！！')
                    break
            }


            appInstance.sendNotification(Event.UpdateView)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = [
            ]

            let playerProto = [
                { key: 'name', type: this._byteType.UTF8},
                { key: 'sex', type: this._byteType.Int},//0 男 1女
                { key: 'coins', type: this._byteType.Int},//本轮得分
                { key: 'curRanking', type: this._byteType.Int},//本轮排名
                { key: 'totalRanking', type: this._byteType.Int},//总排名
                { key: 'extend', type: this._byteType.UTF8},//冗余字段
            ]

            this._reData = [
                { key: 'mcState', type: this._byteType.Int},//0 报名 1晋级 2淘汰 3可以取消匹配  4 比赛未成形 被销毁
                { key: 'mcAllNum', type: this._byteType.Int},//当前晋级所需总人数
                { key: 'mcAllNums', type: this._byteType.UTF8},//晋级所需人数集合
                { key: 'mcEtcTable', type: this._byteType.Int},//晋级时等待结束的桌子数
                { key: 'mcGid', type: this._byteType.Int},
                { key: 'mcMjChannel', type: this._byteType.UTF8},
                { key: 'mcRoomId', type: this._byteType.UTF8},
                { key: 'mcRoomMode', type: this._byteType.Int},
                { key: 'mcExtend', type: this._byteType.UTF8},//冗余字段  consolebut是否显示取消按钮 0显示 1不显示  CountDown还差多少时间可以显示取消按钮
                { key: 'mcPlayers', type: this._byteType.Barray, proto: playerProto},//人员信息
                { key: 'mcCurRound', type: this._byteType.Int}, //当前轮数
            ]
        }
    })
    return proto
})