
/**
 * 选择消息结构体
 */
load('game/msghandler/PlayerSelectProto', function () {
    let baseProto = include('public/network/BaseProto')
    let Event = include('module/mahjong/common/TableConfig').Event
    let proto = baseProto.extend({
        _name: 'PlayerSelectProto',
        _offMsgId: 6,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)
            let pData = appInstance.dataManager().getPlayData()
            let tData = pData.tableData
            let players = pData.players


            for (let i = 0; i < msg.pActions.length; ++i) {
                if (msg.pActions[i].pActionID === -1) {
                    msg.pActions.splice(i,1)
                } else if (msg.pActions[i].pActionID === 10) {
                    tData.pMustOutCard = msg.pActions[i].pCards
                    msg.pActions.splice(i,1)
                }
            }

            tData.pTStatus = msg.pTStatus
            tData.pCurSeatID = msg.pCurSeatID
            tData.pActions = msg.pActions

            let pActionID = msg.pActionID
            let card = {}
            card.nCardColor = msg.nCardColor
            card.nCardNumber = msg.nCardNumber
            let pCards = msg.pCards
            let pLastSelectSeatID = msg.pLastSelectSeatID
            let nLastTakeOutSeatID = msg.nLastTakeOutSeatID

            let lastOutPlayer = players[nLastTakeOutSeatID]
            let lastSelectPlayer = players[pLastSelectSeatID]

            let updatePutCard = {
                'isNeed': false,
                'seatID': nLastTakeOutSeatID,
                'card': card
            }

            let updateHandCard = {
                'isNeed': false,
                'seatID': pLastSelectSeatID
            }
            let cardGroup = {}
            switch (pActionID) {
                case 20:
                    // appInstance.gameAgent().Tips('======胡牌了======')
                    break
                case 110://碰
                case 40: //碰听
                    updatePutCard.isNeed = true
                    updateHandCard.isNeed = true

                    cardGroup.pShowType = 2
                    cardGroup.pActionID = pActionID
                    cardGroup.pPengCardColor = pCards[0].nCardColor
                    cardGroup.pPengCardNumber = pCards[0].nCardNumber
                    lastSelectPlayer.showCards.push(cardGroup)

                    if (lastSelectPlayer.handCards.length) {
                        appInstance.gameAgent().mjUtil().removeCard(lastSelectPlayer.handCards, pCards[0])
                        appInstance.gameAgent().mjUtil().removeCard(lastSelectPlayer.handCards, pCards[0])
                    } else {
                        lastSelectPlayer.handCardCount -= 2
                    }
                    break
                case 50: // 吃听
                case 120: // 吃
                    updatePutCard.isNeed = true
                    updateHandCard.isNeed = true
                    cardGroup.pShowType = 1
                    cardGroup.pActionID = pActionID
                    cardGroup.pChiCardColor = msg.nCardColor
                    cardGroup.pBeginIndex = Math.min(pCards[0].nCardNumber, pCards[1].nCardNumber)
                    if (cardGroup.pBeginIndex > msg.nCardNumber) {
                        cardGroup.pBeginIndex = msg.nCardNumber
                    }
                    lastSelectPlayer.showCards.push(cardGroup)

                    if (lastSelectPlayer.handCards.length) {
                        for ( let i = 0 ; i < msg.pCards.length; ++i) {
                            appInstance.gameAgent().mjUtil().removeCard(lastSelectPlayer.handCards, pCards[i])
                        }
                    } else {
                        lastSelectPlayer.handCardCount -= 2
                    }
                    break
                case 80: //暗杠
                    updateHandCard.isNeed = true
                    cardGroup.pShowType = 3
                    cardGroup.pActionID = pActionID
                    cardGroup.pGangType = 1
                    cardGroup.pGangCardColor = pCards[0].nCardColor
                    cardGroup.pGangCardNumber = pCards[0].nCardNumber
                    lastSelectPlayer.showCards.push(cardGroup)

                    if (lastSelectPlayer.handCards.length) {
                        appInstance.gameAgent().mjUtil().removeCard(lastSelectPlayer.handCards, pCards[0])
                        appInstance.gameAgent().mjUtil().removeCard(lastSelectPlayer.handCards, pCards[0])
                        appInstance.gameAgent().mjUtil().removeCard(lastSelectPlayer.handCards, pCards[0])
                        appInstance.gameAgent().mjUtil().removeCard(lastSelectPlayer.handCards, pCards[0])
                    } else {
                        lastSelectPlayer.handCardCount -= 4
                    }
                    break
                case 90://加杠
                    updateHandCard.isNeed = true

                    let showCards = lastSelectPlayer.showCards
                    for (let i = 0; i < showCards.length; ++i) {
                        if (showCards[i].pShowType === 2) {
                            if (showCards[i].pPengCardColor === pCards[0].nCardColor && showCards[i].pPengCardNumber === pCards[0].nCardNumber) {
                                showCards.splice(i,1)
                            }
                        }
                    }

                    cardGroup.pShowType = 3
                    cardGroup.pActionID = pActionID
                    cardGroup.pGangType = 2
                    cardGroup.pGangCardColor = pCards[0].nCardColor
                    cardGroup.pGangCardNumber = pCards[0].nCardNumber
                    lastSelectPlayer.showCards.push(cardGroup)

                    if (lastSelectPlayer.handCards.length) {
                        appInstance.gameAgent().mjUtil().removeCard(lastSelectPlayer.handCards, pCards[0])
                    } else {
                        lastSelectPlayer.handCardCount -= 1
                    }
                    break
                case 100://明杠
                    updatePutCard.isNeed = true
                    updateHandCard.isNeed = true

                    cardGroup.pShowType = 3
                    cardGroup.pActionID = pActionID
                    cardGroup.pGangType = 0
                    cardGroup.pGangCardColor = msg.nCardColor
                    cardGroup.pGangCardNumber = msg.nCardNumber
                    lastSelectPlayer.showCards.push(cardGroup)

                    if (lastSelectPlayer.handCards.length) {
                        appInstance.gameAgent().mjUtil().removeCard(lastSelectPlayer.handCards, pCards[0])
                        appInstance.gameAgent().mjUtil().removeCard(lastSelectPlayer.handCards, pCards[0])
                        appInstance.gameAgent().mjUtil().removeCard(lastSelectPlayer.handCards, pCards[0])
                    } else {
                        lastSelectPlayer.handCardCount -= 3
                    }
                    break
                case 130: //过

                    tData.pMustOutCard = []
                    break
            }

            switch (pActionID) {
                case 40:
                case 50:
                case 70:
                    updateHandCard.isNeed = true
                    lastSelectPlayer.pIsTing = true
                    break
            }

            cc.log('====================lastSelectPlayer==============' + JSON.stringify(lastSelectPlayer))

            if (updatePutCard.isNeed) {
                appInstance.gameAgent().mjUtil().removeCard(lastOutPlayer.putCards, card)
            }

            let eventMsg = {}
            eventMsg.updatePutCard = updatePutCard
            eventMsg.updateHandCard = updateHandCard
            eventMsg._msg = msg
            appInstance.sendNotification(Event.PlayerSelectProto, eventMsg)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_GAME_MATCHGAME_Re(this._offMsgId)
            this._seData = [
                { key: 'pTableID', type: this._byteType.UTF8}, //
                { key: 'nSeatID', type: this._byteType.Int}, //玩家选择的seatID
                { key: 'pActionID', type: this._byteType.Int}, //动作
                { key: 'pCards', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbCardsInfo()}, //
                { key: 'pNoCards', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbCardsInfo()}, //旋风杠19杠的值
            ]

            this._reData = [
                { key: 'pLastSelectSeatID', type: this._byteType.Int}, //上一个选择的座位ID
                { key: 'pActionID', type: this._byteType.Int}, //动作ID
                { key: 'pCards', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbCardsInfo()}, //操作的牌
                { key: 'pNoCards', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbCardsInfo()}, //旋风杠19杠的值
                { key: 'handCardCount', type: this._byteType.Int}, //操作玩家的手牌数量
                { key: 'pTStatus', type: this._byteType.Int},
                { key: 'pCurSeatID', type: this._byteType.Int},//下一个操作座次
                { key: 'pActions', type: this._byteType.Barray, proto: appInstance.protoArrays().getTbSelectAction()},//可供选择的信息
                { key: 'nLastTakeOutSeatID', type: this._byteType.Int},//上一个出牌的座次
                { key: 'nCardColor', type: this._byteType.Int},
                { key: 'nCardNumber', type: this._byteType.Int},
            ]
        }
    })
    return proto
})