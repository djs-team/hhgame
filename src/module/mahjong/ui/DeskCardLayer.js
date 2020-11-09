
/**
 * 牌桌牌
 */
load('module/mahjong/ui/DeskCardLayer', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let DeskCardLayerMdt = include('module/mahjong/ui/DeskCardLayerMdt')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let Event = TableConfig.Event
    let TStatus =  TableConfig.TStatus
    let CardConfig = TableConfig.CardConfig
    let Layer = BaseLayer.extend({
        _className: 'DeskCardLayer',
        _outCardLen: 10,
        RES_BINDING: function () {
            return {
                'bgPnl/DirectionBg': {  },
                'bgPnl/DirectionBg/Direction0': {  },
                'bgPnl/DirectionBg/Direction1': {  },
                'bgPnl/DirectionBg/Direction2': {  },
                'bgPnl/DirectionBg/Direction3': {  },
                'CardPnl': {  },
                'liuJuBg': {  },
                'fenZhangBg': {  },
            }
        },
        ctor: function () {
            this._super(ResConfig.View.DeskCardLayer)
            this.registerMediator(new DeskCardLayerMdt(this))
        },

        onEnter: function () {
            this._super()
            this.liuJuBg.setVisible(false)
            this.fenZhangBg.setVisible(false)
        },

        initData: function (pData) {
            this._uiSeatArray = pData.uiSeatArray
            this._playerNum = pData.pPlayerNum
            this._uiSeatConfig = TableConfig.UiSeatArray['4']
            let tData = pData.tableData
            this._deckCardLen = CardConfig[tData.pPlayMode].deckCardLen
            this._allCardNum = CardConfig[tData.pPlayMode].allCardNum

            this._movingCard = false

            this._pMySeatID = pData.pMySeatID

            this._deckCardTag  = 1

            this._deckCardNd = []
            this._outCardNd = []
            this._handCardNd = []
            this._selfHandCard = []
            this._directionNd = []

            this.liuJuBg.setVisible(false)
            this.fenZhangBg.setVisible(false)



            for (let i = 0; i < 4; ++i) {
                this._deckCardNd.push(this.CardPnl.getChildByName('DeckNd' + i))
                this._outCardNd.push(this.CardPnl.getChildByName('OutNd' + i))
                this._handCardNd.push(this.CardPnl.getChildByName('HandCardNd' + i))
            }
        },

        initView: function (pData) {
            this.initData(pData)

            for (let i = 0; i < 4; ++i) {
                this._directionNd[i] = this['Direction' + i]
                this._directionNd[i].setVisible(false)
                this._deckCardNd[i].removeAllChildren()
                this._outCardNd[i].removeAllChildren()
                this._handCardNd[i].setVisible(true)
                for (let j = 0; j < this._deckCardLen; ++j) {
                    this.initDeckCard(this._uiSeatConfig[i], j, this._deckCardLen, this._deckCardNd[i], false)
                    this.initDeckCard(this._uiSeatConfig[i], j, this._deckCardLen, this._deckCardNd[i], true)
                }
                for (let j = 0; j < 14; ++j) {
                    let card = this._handCardNd[i].getChildByName('Card' + j)
                    card.setVisible(false)
                    if (j < 4) {
                        this._handCardNd[i].getChildByName('Group' + j).setVisible(false)
                    }
                    if (i === 0) {
                        this._selfHandCard.push(card)
                        card._beginPos = card.getPosition()
                        // let cardBg = card.getChildByName('CardBg')
                        // cardBg.addClickEventListener(function(sender, et) {
                        //     this.onSelfCardClick(sender)
                        // }.bind(this))
                    }
                }
            }

            this._handCardSize = this._selfHandCard[0].getContentSize()

            this._handCardNd[0].addTouchEventListener(function(sender,et) {
                this.onSelfCardTouch(sender,et)
            }.bind(this), this)
        },

        onSelfCardTouch: function (sender, et) {
            switch (et) {
                case ccui.Widget.TOUCH_BEGAN:

                    if (!appInstance.dataManager().getPlayData().isMyPutCard()) {
                        return false
                    }

                    if (this._movingCard) {
                        return false
                    }
                    let beginPos = sender.getTouchBeganPosition()
                    this._movingCard = this.getTouchCard(sender.convertToNodeSpace(beginPos))
                    if (this._movingCard && this._movingCard._isCanPut) {
                        return true
                    } else {
                        return false
                    }
                case ccui.Widget.TOUCH_MOVED:
                    if (this._movingCard && this._movingCard._isCanPut) {
                        this._movingCard.setPosition(sender.convertToNodeSpace(sender.getTouchMovePosition()))
                    }
                    break;
                case ccui.Widget.TOUCH_ENDED:
                case ccui.Widget.TOUCH_CANCELED:
                    if (this._movingCard && this._movingCard._isCanPut) {
                        let isTouchOut = false
                        let endPos = sender.convertToNodeSpace(sender.getTouchEndPosition())
                        if (endPos.y - 20 > this._movingCard._beginPos.y) {
                            isTouchOut = true
                        }


                        for (let i = 0; i < 14; ++i) {
                            if (this._selfHandCard[i] !== this._movingCard) {
                                this._selfHandCard[i].setPosition(this._selfHandCard[i]._beginPos)
                                this._selfHandCard[i]._doubleClick = false
                            }
                        }
                        if (this._movingCard._doubleClick || isTouchOut) {
                            this._movingCard._doubleClick = false
                            this._movingCard.setPosition(this._movingCard._beginPos)
                            let pData = appInstance.dataManager().getPlayData()
                            let tData = pData.tableData
                            let pCurSeatID = tData.pCurSeatID
                            if (pCurSeatID === this._pMySeatID && tData.pTStatus === TStatus.putCard ) {
                                let msg = {}
                                msg.nSeatID = this._pMySeatID
                                msg.nCardColor = this._movingCard._cardInfo.nCardColor
                                msg.nCardNumber = this._movingCard._cardInfo.nCardNumber

                                appInstance.sendNotification(Event.prePutCard, this._movingCard._cardInfo)
                                appInstance.gameAgent().tcpGame().putCardProto(msg)
                                this._movingCard.setPosition(this._movingCard._beginPos)
                            }
                        } else {
                            this._movingCard._doubleClick = true
                            this._movingCard.setPosition(cc.p(this._movingCard._beginPos.x, this._movingCard._beginPos.y + 20))
                        }
                    } else {
                        for (let i = 0; i < 14; ++i) {
                            this._selfHandCard[i].setPosition(this._selfHandCard[i]._beginPos)
                            this._selfHandCard[i]._doubleClick = false
                        }
                    }
                    this._movingCard = false
                    break;
            }
        },

        getTouchCard: function (pos) {
            for (let i = 0; i < 14; ++i) {
                if (this._selfHandCard[i].isVisible()) {
                    let tmpPos = this._selfHandCard[i].getPosition()
                    if (Math.abs(pos.x - tmpPos.x) * 2 < this._handCardSize.width && Math.abs(pos.y - tmpPos.y) * 2 < this._handCardSize.height) {
                        return this._selfHandCard[i]
                    }
                }
            }
            return false
        },

        runDirection: function (seatUI) {
            this.stopDirection()
            if ( typeof seatUI !== 'number') {
                return
            }
            this._directionNd[seatUI].setVisible(true)
            this._directionNd[seatUI].runAction(cc.repeatForever(cc.sequence(cc.fadeIn(0.8),cc.fadeOut(0.8))))
        },
        stopDirection: function () {
            for (let i = 0; i < 4; ++i) {
                this._directionNd[i].stopAllActions()
                this._directionNd[i].setVisible(false)
            }
        },
        onSelfCardClick: function (sender) {
            let card = sender.getParent()
            let isDoubleClick = false
            if (card.getPositionY() > 0) {
                isDoubleClick = true
            }
            for (let i = 0; i < 14; ++i) {
                this._selfHandCard[i].setPositionY(0)
            }
            if (!card._isCanPut) {
                return
            }
            card.setPositionY(20)
            if (isDoubleClick) {
                 let pData = appInstance.dataManager().getPlayData()
                 let tData = pData.tableData
                 let pCurSeatID = tData.pCurSeatID
                 if (pCurSeatID === this._pMySeatID && tData.pTStatus === TStatus.putCard ) {
                    cc.log('========出牌了========')
                    let msg = {}
                    msg.nSeatID = this._pMySeatID
                    msg.nCardColor = card._cardInfo.nCardColor
                    msg.nCardNumber = card._cardInfo.nCardNumber

                    appInstance.sendNotification(Event.prePutCard, card._cardInfo)
                    appInstance.gameAgent().tcpGame().putCardProto(msg)
                    card.setPositionY(0)
                }
            }
        },

        reBeginGame: function () {
            let pConfig = [
                2,3,0,1
            ]
            for ( let i = this._deckCardTag; i <= this._allCardNum; ++i) {
                let pIndex = Math.floor((i - 1) / this._deckCardLen / 2)
                let pNd = this._deckCardNd[pConfig[pIndex]]
                pNd.setVisible(true)
                pNd.getChildByTag(i).setVisible(true)
            }
        },

        updateDeckCard: function (nDeckCardNum) {
            let endTag = this._allCardNum - nDeckCardNum
            let pConfig = [
                2,3,0,1
            ]
            for ( let i = this._deckCardTag; i <= endTag; ++i) {
                let pIndex = Math.floor((i - 1) / this._deckCardLen / 2)
                let pNd = this._deckCardNd[pConfig[pIndex]]
                pNd.setVisible(true)
                pNd.getChildByTag(i).setVisible(false)
            }
        },

        downSelfHandCard: function () {
            let handNd = this._handCardNd[0]
            for (let i = 0; i < 14; ++i) {
                let card = handNd.getChildByName('Card' + i)
                card.setPosition(card._beginPos)
            }
        },

        updateHandCard: function (uiSeat, player, isTurn) {
            let handNd = this._handCardNd[uiSeat]
            handNd.setVisible(true)
            let handCards = player.handCards
            let handCardCount = player.handCardCount
            if (uiSeat === 0) {//修改自己的手牌
                if ((handCards.length % 3 ) !== 2) {
                    handCards = appInstance.gameAgent().mjUtil().sortCard(handCards)
                }

                for (let i = 0; i < 14; ++i) {
                    let card = handNd.getChildByName('Card' + i)
                    if (isTurn) {
                        if ((handCards.length % 3 ) === 2) {
                            if (i < handCards.length) {
                                card.setVisible(true)
                                this.updateSelfHandCard(card, handCards[i])
                            } else {
                                card.setVisible(false)
                            }
                        } else {
                            if (i === 0 || i > handCards.length) {
                                card.setVisible(false)
                            } else {
                                card.setVisible(true)
                                this.updateSelfHandCard(card, handCards[i - 1])
                            }
                        }
                    } else {
                        if (i === 0 || i > handCards.length) {
                            card.setVisible(false)
                        } else {
                            card.setVisible(true)
                            this.updateSelfHandCard(card, handCards[i-1])
                        }
                    }
                }
            } else {
                for (let i = 0; i < 14; ++i) {
                    let card = handNd.getChildByName('Card' + i)
                    if ((handCardCount % 3 ) === 2) {
                        if (i < handCardCount) {
                            card.setVisible(true)
                        } else {
                            card.setVisible(false)
                        }
                    } else {
                        if (i === 0 || i > handCardCount) {
                            card.setVisible(false)
                        } else {
                            card.setVisible(true)
                        }
                    }
                }
            }

            let showCards = player.showCards
            let isKaimen = false
            for (let i = 0; i < showCards.length; ++i) {
                if (showCards[i].pShowType === 3) {
                    if (showCards[i].pGangType !== 1) {
                        isKaimen = true
                    }
                } else {
                    isKaimen = true
                    break
                }
            }

            for (let i = 0; i < 4; ++i) {
                let groupNd = handNd.getChildByName('Group' + i)
                if (showCards[i]) {
                    groupNd.setVisible(true)
                    this.updateHandGroupCard(uiSeat, groupNd, showCards[i], isKaimen)
                } else {
                    groupNd.setVisible(false)
                }
            }
        },
        updateHandGroupCard: function (uiSeat, groupNd, groupInfo, isKaimen) {
            let showType = groupInfo.pShowType
            switch (showType) {
                case 1:// 吃
                    for (let i = 0; i < 8; ++i) {
                        let card = groupNd.getChildByName('Card' + i)
                        if ( i >= 3) {
                            card.setVisible(false)
                        } else {
                            let valueImg = this.getHandGroupCardImg(uiSeat, groupInfo.pChiCardColor, groupInfo.pBeginIndex + i)
                            card.getChildByName('CardValue').loadTexture(valueImg)
                            card.setVisible(true)
                        }
                    }
                    break
                case 2:// 碰
                    for (let i = 0; i < 8; ++i) {
                        let card = groupNd.getChildByName('Card' + i)
                        if ( i >= 3) {
                            card.setVisible(false)
                        } else {
                            let valueImg = this.getHandGroupCardImg(uiSeat, groupInfo.pPengCardColor, groupInfo.pPengCardNumber )
                            card.getChildByName('CardValue').loadTexture(valueImg)
                            card.setVisible(true)
                        }
                    }
                    break
                case 3:// 杠
                    let gangType = groupInfo.pGangType
                    if (gangType === 1) {
                        for (let i = 0; i < 8; ++i) {
                            let card = groupNd.getChildByName('Card' + i)
                            if ( i < 3 ) {
                                card.setVisible(false)
                            } else {
                                if (i === 3) {
                                    if (isKaimen || uiSeat === 0) {
                                        let valueImg = this.getHandGroupCardImg(uiSeat, groupInfo.pGangCardColor, groupInfo.pGangCardNumber )
                                        card.getChildByName('CardValue').loadTexture(valueImg)
                                        card.setVisible(true)
                                    } else {
                                        card.setVisible(false)
                                    }
                                } else if ( i === 4) {
                                    if (isKaimen || uiSeat === 0) {
                                        card.setVisible(false)
                                    } else {
                                        card.setVisible(true)
                                    }
                                } else {
                                    card.setVisible(true)
                                }
                            }
                        }
                    } else {
                        for (let i = 0; i < 8; ++i) {
                            let card = groupNd.getChildByName('Card' + i)
                            if ( i > 3 ) {
                                card.setVisible(false)
                            } else {
                                let valueImg = this.getHandGroupCardImg(uiSeat, groupInfo.pGangCardColor, groupInfo.pGangCardNumber )
                                card.getChildByName('CardValue').loadTexture(valueImg)
                                card.setVisible(true)
                            }
                        }
                    }
                    break
            }
        },

        getHandGroupCardImg: function (uiSeat, cardColor, cardNumber) {
            let imgPath = 'res/module/mahjong/card/value/'

            let colorStr = [
                'wan',
                'tong',
                'tiao',
                'feng'
            ]

            if (cardColor > 2) {
                cardNumber = cardColor - 2
                cardColor = 3
            }

            switch (uiSeat) {
                case 0:
                    imgPath += 'selfshow/'
                    imgPath += colorStr[cardColor]
                    imgPath += '_show_'
                    imgPath += cardNumber
                    imgPath +='.png'
                    break
                case 1:
                case 3:
                    imgPath += 'leftshow/'
                    imgPath += colorStr[cardColor]
                    imgPath += '_left_'
                    imgPath += cardNumber
                    imgPath +='.png'
                    break
                case 2:
                    imgPath += 'topshow/'
                    imgPath += colorStr[cardColor]
                    imgPath += '_top_'
                    imgPath += cardNumber
                    imgPath +='.png'
                    break
            }
            return imgPath
        },

        updateSelfHandCard: function (card, cardInfo) {

            let valueNd = card.getChildByName('CardValue')
            let valueImg = 'res/module/mahjong/card/value/selfhand/'
            card._cardInfo = cardInfo
            card.setPositionY(card._beginPos.y)
            let cardStr = [
                'wan',
                'tong',
                'tiao'
            ]
            if (cardInfo.nCardColor > 2) {
                valueImg += 'feng_hand_'
                valueImg += (cardInfo.nCardColor - 2)
                valueImg += '.png'
            } else {
                valueImg += cardStr[cardInfo.nCardColor]
                valueImg += '_hand_'
                valueImg += cardInfo.nCardNumber
                valueImg += '.png'
            }
            valueNd.loadTexture(valueImg)

            let pData = appInstance.dataManager().getPlayData()
            let tData = pData.tableData
            if (tData.pMustOutCard && tData.pMustOutCard.length) {
                if (appInstance.gameAgent().mjUtil().isCardInArray(tData.pMustOutCard, cardInfo)) {
                    card.setColor(cc.color(255,255,255))
                    card._isCanPut = true
                } else {
                    card.setColor(cc.color(100,100,100))
                    card._isCanPut = false
                }
            } else {
                card.setColor(cc.color(255,255,255))
                card._isCanPut = true
            }
        },

        updatePutCard: function (uiSeat, cardArray) {
            this._outCardNd[uiSeat].removeAllChildren()

            for (let i = 0; i < cardArray.length; ++i) {
                this.onePutCard(uiSeat, cardArray[i])
            }

        },

        onePutCard: function (uiSeat, cardInfo, isDele) {
            let outNd = this._outCardNd[uiSeat]
            let outCards = outNd.getChildren()
            let cardIndex = outNd.getChildrenCount()
            if (isDele) {
                outCards[cardIndex - 1].removeFromParent()
                return
            }

            let cardPath = 'res/module/mahjong/card/'
            let imgPath = cardPath + 'ground/out/'
            let scale = 1
            let isFlippedX = false
            let pos = cc.p(0,0)
            let zorder = cardIndex
            if (uiSeat === 0 || uiSeat === 2) {
                let outCardCfg = [
                    'self_out_1.png',
                    'self_out_2.png',
                    'self_out_3.png',
                    'self_out_4.png',
                    'self_out_5.png',
                ]

                let isLeft = (Math.floor(cardIndex / 5) % 2) === 0
                let isFirstLine = (Math.floor(cardIndex / 10) % 2) === 0
                let isBm = (Math.floor(cardIndex / 20) % 2) === 0
                let lineIndex = Math.floor(cardIndex % 10)

                if (uiSeat === 0) {
                    if (isLeft) {
                        imgPath += outCardCfg[lineIndex]
                        isFlippedX = true
                        zorder = lineIndex
                    } else {
                        imgPath += outCardCfg[10 - 1 -lineIndex]
                        zorder = 10 - lineIndex
                    }
                    if (isFirstLine) {
                        scale = 1.08
                        pos.x = lineIndex * 33.5 * scale
                    } else {
                        scale = 1.2
                        pos.x = lineIndex * 33.5 * scale
                        pos.x = pos.x - 18
                        pos.y -= 35
                        zorder += 100
                    }

                    if (isBm) {

                    } else {
                        pos.x -= 2.5
                        pos.y += 20
                        zorder += 500
                    }
                } else if (uiSeat === 2) {
                    if (isLeft) {
                        imgPath += outCardCfg[lineIndex]
                        zorder = lineIndex
                    } else {
                        isFlippedX = true
                        imgPath += outCardCfg[10 - 1 -lineIndex]
                        zorder = 10 - lineIndex
                    }
                    if (isFirstLine) {
                        scale = 1.12
                        pos.x = -1 * lineIndex * 33.5 * scale
                        zorder += 100
                    } else {
                        scale = 1
                        pos.x = -1 * lineIndex * 33.5 * scale
                        pos.x = pos.x - 18
                        pos.y += 35
                    }

                    if (isBm) {

                    } else {
                        pos.x += 1.5
                        pos.y += 14
                        zorder += 500
                    }
                }
            } else if (uiSeat === 1 || uiSeat === 3) {
                let isBm = (Math.floor(cardIndex / 20) % 2) === 0
                let lineIndex = Math.floor(cardIndex % 10)
                let isFirstLine = (Math.floor(cardIndex / 10) % 2) === 0
                if (isBm) {
                    imgPath += 'left_out_bm.png'
                } else {
                    imgPath += 'left_out_top.png'
                }
                if (uiSeat === 1) {
                    zorder = 50 - lineIndex
                    isFlippedX = true
                    pos.y = 16 * lineIndex
                    pos.x = -10 * lineIndex

                    if (isFirstLine) {
                    } else {
                        pos.x += 45
                        zorder -= 10
                    }
                    if (isBm) {
                    } else {
                        pos.y += 16.5
                    }
                } else if (uiSeat === 3) {
                    zorder = lineIndex
                    pos.y = -15 * lineIndex
                    pos.x = -8 * lineIndex

                    if (isFirstLine) {
                    } else {
                        pos.x -= 45
                        zorder -= 10
                    }
                    if (isBm) {
                    } else {
                        pos.y += 16.5
                        zorder += 10
                    }
                }

            }

            let card =  new cc.Sprite(imgPath)
            outNd.addChild(card)
            card.setAnchorPoint(cc.p(0.5,0.5))
            if (isFlippedX) {
                card.setFlippedX(true)
            }
            card.setScale(scale)
            card.setPosition(pos)
            card.setLocalZOrder(zorder)

            let cardImg = this.getCardImgOutCard(uiSeat, cardInfo)
            let cardValueSp = new cc.Sprite(cardImg)
            card.addChild(cardValueSp)
            let valuePos = cc.p(0,0)
            let valueScaleX = 1
            let valueScaleY = 1
            let valueSkewX = 0
            let valueSkewY = 0
            let lineIndex = Math.floor(cardIndex % 10)
            let tmp02Pos = [
                cc.p(26,35),
                cc.p(25,35),
                cc.p(24.5,35),
                cc.p(21,35),
                cc.p(20,35),
                cc.p(20,35),
                cc.p(21,35),
                cc.p(24.5,35),
                cc.p(25,35),
                cc.p(26,35),
            ]
            let tmp02SkewX = [
                25,
                20,
                15,
                8,
                5,
                -5,
                -8,
                -15,
                -20,
                -25
            ]
            switch (uiSeat) {
                case 0:
                case 2:
                    valuePos = tmp02Pos[lineIndex]
                    valueScaleX = 0.6
                    valueScaleY = 0.6
                    valueSkewX = tmp02SkewX[lineIndex]
                    if (uiSeat === 2) {
                        valueSkewX *= -1
                    }
                    break
                case 1:
                case 3:
                    let tmpSkewY = [
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0,
                        0
                    ]
                    valueSkewY = tmpSkewY[lineIndex]
                    valuePos = cc.p(30,30)
                    valueScaleX = 0.8
                    valueScaleY = 0.4
                    if (uiSeat === 3) {
                        cardValueSp.setRotationY(180)
                    }
                    break
                default:
                    break
            }
            cardValueSp.setPosition(valuePos)
            cardValueSp.setScaleX(valueScaleX)
            cardValueSp.setScaleY(valueScaleY)
            cardValueSp.setSkewX(valueSkewX)
            cardValueSp.setSkewY(valueSkewY)
        },

        getCardImgOutCard: function (uiSeat, cardInfo) {
            let imgPath = 'res/module/mahjong/card/value/'
            let CardColor = TableConfig.CardColor

            let color = cardInfo.nCardColor

            let colorStr = CardColor[color]
            let valueStr = cardInfo.nCardNumber
            if (color > 2) {
                valueStr = cardInfo.nCardColor - 2
                colorStr = 'feng'
            }

            switch (uiSeat) {
                case 0:
                case 2:
                    imgPath += 'selfshow/'
                    imgPath += colorStr
                    imgPath += '_show_'
                    imgPath += valueStr
                    imgPath += '.png'
                    break
                case 1:
                case 3:
                    //     cardValuePath += 'leftshow/feng_left_1.png'
                    imgPath += 'leftshow/'
                    imgPath += colorStr
                    imgPath += '_left_'
                    imgPath += valueStr
                    imgPath += '.png'
                    break
                default:
                    imgPath += 'selfshow/'
                    imgPath += colorStr
                    imgPath += '_show_'
                    imgPath += valueStr
                    imgPath += '.png'
                    break
            }

            return imgPath
        },

        initDeckCard: function (seatIndex, cardIndex, cardLen, parent, isTop) {
            let cardPath = 'res/module/mahjong/card/'
            let imgPath = ''
            let scale = 1
            let pos = cc.p(0,0)
            let isFlippedX = false
            let zorder = 1000 - cardIndex
            let cardLenMid = cardLen / 2
            if (seatIndex === 0 || seatIndex === 2) {
                let selfDeckCfg = [
                    'self_deck_2.png',
                    'self_deck_3.png',
                    'self_deck_4.png',
                    'self_deck_5.png',
                    'self_deck_6.png',
                    'self_deck_7.png',
                    'self_deck_8.png',
                    'self_deck_9.png',
                    'self_deck_10.png'
                ]
                let deckPath = selfDeckCfg[cardIndex]
                if (cardIndex >= cardLenMid) {
                    deckPath = selfDeckCfg[cardLen - 1 - cardIndex]
                } else {
                    isFlippedX = true
                    zorder = cardIndex
                }
                imgPath = cardPath + 'ground/deck/' + deckPath
                if (seatIndex === 2) {
                    scale = 0.6
                }
                pos.x = ( cardIndex - cardLenMid ) * 46 * scale
                if (isTop) {
                    pos.y = 20 * scale
                }
            } else if (seatIndex === 1 || seatIndex === 3) {
                imgPath = cardPath + 'ground/deck/'
                if (isTop) {
                    imgPath += 'left_deck_top.png'
                } else {
                    imgPath += 'left_deck_bm.png'
                }
                let scaleMid = cardLenMid
                if (cardLen % 2 === 0) {
                    scaleMid += 0.5
                }
                if (seatIndex === 1) {
                    isFlippedX = true
                    scale = 0.7 - (cardIndex - scaleMid) * 0.02
                    pos.y = ( cardIndex - cardLenMid ) * 16 * scale
                    pos.x = - 1 * ( cardIndex - cardLenMid ) * 12 * scale
                } else {
                    scale = 0.9 - (cardIndex - scaleMid) * 0.02
                    pos.y = ( cardIndex - cardLenMid ) * 16 * scale
                    pos.x = ( cardIndex - cardLenMid ) * 12.5 * scale
                }

                if (isTop) {
                    pos.y += (20 * scale)
                }
            }

            if (isTop) {
                zorder += 1000
            }

            let card =  new cc.Sprite(imgPath)
            if (parent) {
                parent.addChild(card)
            }
            if (isFlippedX) {
                card.setFlippedX(true)
            }
            card.setScale(scale)
            card.setPosition(pos)
            card.setLocalZOrder(zorder)

            let cardTag = 0
            if (seatIndex === 0) {
                cardTag = this._deckCardLen * 2 * 2
                // cardTag += (this._deckCardLen - cardIndex - 1) * 2
                cardTag += (cardIndex) * 2
            } else if (seatIndex === 3) {
                cardTag = this._deckCardLen * 2
                cardTag += (this._deckCardLen - cardIndex - 1) * 2
            } else if (seatIndex === 2) {
                cardTag += cardIndex * 2
            } else if (seatIndex === 1) {
                cardTag = this._deckCardLen * 3 * 2
                cardTag += cardIndex * 2
            }
            if (isTop) {
                cardTag += 1
            } else {
                cardTag += 2
            }

            card.setTag(cardTag)
            let cardName = 'card_' + seatIndex + '_' + cardTag
            card.setName(cardName)
            return card
        },

        clearView: function () {
            for (let i = 0; i < 4; ++i) {
                this._deckCardNd[i].removeAllChildren()
                this._outCardNd[i].removeAllChildren()
                this._handCardNd[i].setVisible(false)
                for (let j = 0; j < 4; ++j) {
                    this._handCardNd[i].getChildByName('Group' + j).setVisible(false)
                }
            }
        },

        clearTableGaming: function () {
            cc.log('=============clearTableGaming===============')
            for (let i = 0; i < 4; ++i) {
                this._outCardNd[i].removeAllChildren()
                this._handCardNd[i].setVisible(false)
                this._deckCardNd[i].setVisible(false)

                for (let j = 0; j < 4; ++j) {
                    this._handCardNd[i].getChildByName('Group' + j).setVisible(false)
                }
            }
        },

        onExit: function () {
            this._super()
        },

        showLiuJu: function () {
            cc.log('-----------deskCardLayer------------------showLiuJu----------------------------------------')
            this.fenZhangBg.setVisible(false)
            this.liuJuBg.setVisible(true)
        },

        showFenZhang: function () {
            cc.log('-----------deskCardLayer------------------showFenZhang----------------------------------------')
            this.liuJuBg.setVisible(false)
            this.fenZhangBg.setVisible(true)
        },

    })
    return Layer
})
