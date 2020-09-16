
/**
 * 加岗牌桌同步消息结构体
 */
load('game/msghandler/JiaGangTableProto', function () {
    let baseProto = include('public/network/BaseProto')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event
    let proto = baseProto.extend({
        _name: 'JiaGangTableProto',
        _offMsgId: 21,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            let pData = appInstance.dataManager().getPlayData()
            let selfInfo = pData.getSelfInfo()
            let tData = pData.tableData
            for (let i = 0; i < msg.pActions.length; ++i) {
                if (msg.pActions[i].pActionID === -1) {
                    msg.pActions.splice(i,1)
                } else if (msg.pActions[i].pActionID === 10) {
                    tData.pMustOutCard = msg.pActions[i].pCards
                    msg.pActions.splice(i,1)
                }
            }


            let pPlayer = msg.pPlayer
            for (let i = 0; i < pPlayer.length; ++i) {
                let player = pData.getPlayer(pPlayer[i].pSeatID)
                global.mergeData(player, pPlayer[i])
            }

            let playInfo = msg.playInfo
            for (let i = 0; i < playInfo.length; ++i) {
                let player = pData.getPlayer(playInfo[i].pSeatID)
                appInstance.gameAgent().mjUtil().sortCard(playInfo[i].handCards)
                global.mergeData(player, playInfo[i])
            }

            tData.lastPutCard = {
                nCardColor: msg.lastTakeoutCardColor,
                nCardNumber: msg.lastTakeoutCardNumber
            }

            delete msg.lastTakeoutCardColor
            delete msg.lastTakeoutCardNumber

            delete msg.playInfo
            delete msg.pPlayer

            global.mergeData(tData, msg)

            if (msg.pMySeatID === msg.pCurSeatID) {
                let lastDrawCard = {
                    nCardColor: selfInfo.pLastDrawCardColor,
                    nCardNumber: selfInfo.pLastDrawCardNumber
                }
                if (selfInfo.pIsTing && !tData.pMustOutCard.length) {
                    tData.pMustOutCard.push(lastDrawCard)
                }
                if ((selfInfo.handCards.length % 3 ) === 2) {
                    appInstance.gameAgent().mjUtil().removeCard(selfInfo.handCards, lastDrawCard)
                    selfInfo.handCards.unshift(lastDrawCard)
                }
            }

            let players = pData.players
            let delPengGroup = {}
            for (let seatid in players) {
                let showCards = players[seatid].showCards
                for (let showIndex = 0; showIndex < showCards.length; ++showIndex) {
                    if (showCards[showIndex].pShowType === 3 && showCards[showIndex].pGangType === 2 ) {
                        delPengGroup[seatid] = delPengGroup[seatid] || []
                        let card = {
                            nCardColor: showCards[showIndex].pGangCardColor,
                            nCardNumber: showCards[showIndex].pGangCardNumber,
                        }
                        delPengGroup[seatid].push(card)
                    }
                }
            }
            for (let seatid in delPengGroup) {
                let showCards = players[seatid].showCards
                for (let delIndex = showCards.length - 1; delIndex >= 0 ; --delIndex) {
                    if (showCards[delIndex].pShowType === 2) {
                        let card = {
                            nCardColor: showCards[delIndex].pPengCardColor,
                            nCardNumber: showCards[delIndex].pPengCardNumber,
                        }
                        if (appInstance.gameAgent().mjUtil().isCardInArray(delPengGroup[seatid], card)) {
                            showCards.splice(delIndex,1)
                        }
                    }
                }
            }

            cc.log('======================================playerdata==========' + JSON.stringify(pData))
            appInstance.sendNotification(TableEvent.UpdateView)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME_Re(this._offMsgId)
            this._seData = [
            ]

            let showCardArray = [
                { key: 'pShowType', type: this._byteType.Int},
                { key: 'pJiangCardColor', type: this._byteType.Int},
                { key: 'pJiangCardNumber', type: this._byteType.Int},//jiang
                { key: 'pChiCardColor', type: this._byteType.Int},
                { key: 'pBeginIndex', type: this._byteType.Int},
                { key: 'pEndIndex', type: this._byteType.Int},//chi
                { key: 'pPengCardColor', type: this._byteType.Int},
                { key: 'pPengCardNumber', type: this._byteType.Int},//碰
                { key: 'pGangCardColor', type: this._byteType.Int},
                { key: 'pGangCardNumber', type: this._byteType.Int},//gang
                { key: 'pGangType', type: this._byteType.Int},//0:明杠or1:暗杠or2:加杠or3:旋风杠or4:幺腰杠or5:幺九杠6：喜儿 7:甩幺
                { key: 'pGangCards', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbCardsInfo()},// gang
                { key: 'pFromCardColor', type: this._byteType.Int},
                { key: 'pFromCardNumber', type: this._byteType.Int},//laizi
                { key: 'pFromSeatID', type: this._byteType.Int},
                { key: 'pBeiChiCardColor', type: this._byteType.Int},
                { key: 'pBeiChiCardNumber', type: this._byteType.Int},//被吃的牌
            ]

            let playersArray = [
                { key: 'pid', type: this._byteType.Int},
                { key: 'nickName', type: this._byteType.UTF8},
                { key: 'pCoins', type: this._byteType.UTF8},
                { key: 'robotModel', type: this._byteType.Byte},
                { key: 'age', type: this._byteType.Byte},
                { key: 'sex', type: this._byteType.Byte},
                { key: 'pPhoto', type: this._byteType.UTF8},
                { key: 'win', type: this._byteType.Int},
                { key: 'lose', type: this._byteType.Int},
                { key: 'exp', type: this._byteType.Int},
                { key: 'vip', type: this._byteType.Byte},
                { key: 'pSeatID', type: this._byteType.Byte},
                { key: 'netIp', type: this._byteType.UTF8},
                { key: 'isReady', type: this._byteType.Int},
            ]

            let playInfoArray = [
                { key: 'pSeatID', type: this._byteType.Int},
                { key: 'handCards', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbCardsInfo()},//手牌
                { key: 'putCards', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbCardsInfo()},//出过的牌
                { key: 'showCards', type: this._byteType.Barray, proto: showCardArray},//牌面
                { key: 'pIsTing', type: this._byteType.Int},//玩家是否听  0  没听  1 听
                { key: 'handCardCount', type: this._byteType.Int},//玩家手牌数量
                { key: 'pLastDrawCardColor', type: this._byteType.Int},
                { key: 'pLastDrawCardNumber', type: this._byteType.Int},//最后摸的牌
                { key: 'pIsJiaGang', type: this._byteType.Int},//加钢相关字段， 0尚未选择加钢 1加钢 2不加钢
                { key: 'pExtend', type: this._byteType.UTF8},//预留字段
            ]

            this._reData = [
                { key: 'playMode', type: this._byteType.UTF8},//玩法
                { key: 'pRoomID', type: this._byteType.UTF8},//房间ID
                { key: 'pTableID', type: this._byteType.UTF8},//桌子ID
                { key: 'pMySeatID', type: this._byteType.Int},//自己的座位id
                { key: 'zhuangSeatId', type: this._byteType.Int},//庄的座位号
                { key: 'nDeckCardNum', type: this._byteType.Int},//牌池数量
                { key: 'curCardPos', type: this._byteType.Int},//牌池的指针
                { key: 'pTStatusParent', type: this._byteType.Int},//当前阶段
                { key: 'pTStatus', type: this._byteType.Int},//当前子阶段
                { key: 'pLastTStatus', type: this._byteType.Int},//上一个子阶段
                { key: 'pCurSeatID', type: this._byteType.Int},//当前操作的座位
                { key: 'lastSelectSeatID', type: this._byteType.Int},//最近一次选择的玩家座位
                { key: 'lastSelectActionID', type: this._byteType.Int},//最近一次选择的玩家选择的actionID
                { key: 'lastGangCardColor', type: this._byteType.Int},
                { key: 'lastGangCardNumber', type: this._byteType.Int},//最近一次杠的牌
                { key: 'lastTakeoutCardSeatID', type: this._byteType.Int},//最近出牌玩家的座位ID
                { key: 'lastTakeoutCardColor', type: this._byteType.Int},
                { key: 'lastTakeoutCardNumber', type: this._byteType.Int},//最近出的牌
                { key: 'isJiaGang', type: this._byteType.Int},//牌桌加钢信息， 0 加钢 1不加钢
                { key: 'IsCanReceiveMagic', type: this._byteType.Int},//是否允许接收魔法表情0.可以接收1.不可以
                { key: 'extend', type: this._byteType.UTF8},//预留字段
                { key: 'pActions', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbSelectAction()},//当前的选择
                { key: 'pPlayer', type: this._byteType.Barray, proto: playersArray},
                { key: 'playInfo', type: this._byteType.Barray, proto: playInfoArray}, //玩家数据
            ]
        }
    })
    return proto
})