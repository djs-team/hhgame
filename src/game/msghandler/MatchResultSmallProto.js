
/**
 * 比赛小结算消息
 */
load('game/msghandler/MatchResultSmallProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let GameConfig = include('game/config/GameConfig')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event
    let proto = baseProto.extend({
        _name: 'MatchResultSmallProto',
        _offMsgId: 6,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            let pData = appInstance.dataManager().getPlayData()

            msg.pBaoCard = {
                nCardColor: msg.pBaoCardColor,
                nCardNumber: msg.pBaoCardNumber
            }
            msg.pHuCard = {
                nCardColor: msg.pHuCardColor,
                nCardNumber: msg.pHuCardNumber
            }

            let saveKey = [
                'nZhuangSeatID',
                'pWinSeatID',
                'pHuType',
                'pBaoCard',
                'pBaseScore',
                'pIsLiuJu',
                'pBigWinSeatID',
                'pGameResultExtend'
            ]
            pData.saveTableData(msg,saveKey)
            pData.isMatch = true


            let pPlayer = msg.pPlayer
            for (let i = 0; i < pPlayer.length; ++i) {
                let player = pData.getPlayer(pPlayer[i].pSeatID)
                global.mergeData(player, pPlayer[i])
                cc.log('=========player===========' + JSON.stringify(player))
            }

            cc.log('====MatchResultSmallProto=====pData===========' + JSON.stringify(pData))
            appInstance.sendNotification(TableEvent.GameResultProto, msg)
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
                { key: 'pChiList', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbChiInfo()},//吃牌
                { key: 'pPengList', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbCardsInfo()},//碰牌
                { key: 'pGangList', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbGangInfo()},//杠牌
                { key: 'handCards', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbCardsInfo()},//手牌
                { key: 'pIsQingYiSe', type: this._byteType.Byte},//是否清一色
                { key: 'pDoubleCount', type: this._byteType.Int},//玩家翻翻倍数
                { key: 'pDoubleList', type: this._byteType.Barray, proto:[{ key: 'pDouble', type: this._byteType.Int}]},//玩家翻倍 属性id
                { key: 'pMaxDoubleID', type: this._byteType.Int},//最大翻数  id
                { key: 'pJiangList', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbCardsInfo()},////盯掌对
                { key: 'pMatchResultExtend', type: this._byteType.UTF8},//json  扩展字段
            ]

            this._reData = [
                { key: 'code', type: this._byteType.Byte},//0 成功
                { key: 'nZhuangSeatID', type: this._byteType.Int},
                { key: 'pWinSeatID', type: this._byteType.Int},
                { key: 'pHuType', type: this._byteType.Barray, proto:[{ key: 'pHu', type: this._byteType.Int}]},
                { key: 'pBaoCardColor', type: this._byteType.Int},
                { key: 'pBaoCardNumber', type: this._byteType.Int},
                { key: 'pBaseScore', type: this._byteType.Int},//底分
                { key: 'pPlayer', type: this._byteType.Barray, proto: playerProto},
                { key: 'pIsLiuJu', type: this._byteType.Int},

                { key: 'isGamesOrRound', type: this._byteType.Int},//是按 局打  还是按 圈打  0  圈  1 局
                { key: 'totalRound', type: this._byteType.Int},//当前圈数或局数
                { key: 'curRound', type: this._byteType.Int},//当前圈数或局数

                { key: 'pHuCardColor', type: this._byteType.Int},
                { key: 'pHuCardNumber', type: this._byteType.Int},
                { key: 'pIsOver', type: this._byteType.Int},//是不是最后一局  0  不是  1 是
                { key: 'pBigWinSeatID', type: this._byteType.Int}, // 大赢家座位ID
                { key: 'pMatchResultExtend', type: this._byteType.UTF8},
            ]
        }
    })
    return proto
})