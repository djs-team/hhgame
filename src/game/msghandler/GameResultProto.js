
/**
 * 牌局结算消息结构体
 */
load('game/msghandler/GameResultProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let GameConfig = include('game/config/GameConfig')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event
    let proto = baseProto.extend({
        _name: 'GameResultProto',
        _offMsgId: 46,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            let pData = appInstance.dataManager().getPlayData()
            pData.isMatch = false

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


            let pPlayer = msg.pPlayer
            for (let i = 0; i < pPlayer.length; ++i) {
                let player = pData.getPlayer(pPlayer[i].pSeatID)
                global.mergeData(player, pPlayer[i])
                cc.log('=========player===========' + JSON.stringify(player))
            }

            cc.log('====GameResultProto=====pData===========' + JSON.stringify(pData))
            appInstance.sendNotification(TableEvent.GameResultProto, msg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_ROOM_MATCHGAME_Re(this._offMsgId)
            this._seData = []

            let playerProto = [
                { key: 'pid', type: this._byteType.Int},
                { key: 'pCoins', type: this._byteType.UTF8},
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
                { key: 'pMaxWatchNum', type: this._byteType.Int},//最大看视频次数
                { key: 'pAlreadyWatchNum', type: this._byteType.Int},//已经看过的次数
                { key: 'pGameResultExtend', type: this._byteType.UTF8},//json  扩展字段
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
                { key: 'pHuCardColor', type: this._byteType.Int},
                { key: 'pHuCardNumber', type: this._byteType.Int},
                { key: 'pBigWinSeatID', type: this._byteType.Int}, // 大赢家座位ID
                { key: 'pGameResultExtend', type: this._byteType.UTF8},
            ]
        }
    })
    return proto
})