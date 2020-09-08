
/**
 * DeskHeadLayer
 */
load('module/mahjong/ui/DeskResultLayer', function () {
    let GameResConfig = include('game/config/ResConfig')
    let PlayerPlay = GameResConfig.PlayerPlay
    let AniPlayer = GameResConfig.AniPlayer
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let DeskResultLayerMdt = include('module/mahjong/ui/DeskResultLayerMdt')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let Layer = BaseLayer.extend({
        _className: 'DeskResultLayer',
        _posConst: {
            '2': [
                cc.p(-200, 0),
                cc.p(200, 0)
            ],
            '4': [
                cc.p(-360, 0),
                cc.p(-120, 0),
                cc.p(120, 0),
                cc.p(360, 0)
            ]
        },
        RES_BINDING: function () {
            return {
                'topPnl/BackBtn': { onClicked: this.onBackHallBtnClick },
                'midPnl/midNd': {  },
                'midPnl/midNd/PlayerCell': {  },
                'bmPnl/BackHallBtn': { onClicked: this.onBackHallBtnClick },
                'bmPnl/InviteFriendBtn': { onClicked: this.onInviteFriendBtnClick },
                'bmPnl/NextGameBtn': { onClicked: this.onNextGameBtnClick },
                'bmPnl/nameTxt': {  },
                'bmPnl/ruleTxt': {  },
                'bmPnl/BaoCard': {  },
                'bmPnl/CardNd': {  },
                'bmPnl/CardNd/CardCell': {  },
            }
        },
        ctor: function () {
            this._super(ResConfig.View.DeskResultLayer)
            this.registerMediator(new DeskResultLayerMdt(this))
        },

        onBackHallBtnClick: function () {
            let HallScene = include('game/ui/scene/HallScene')
            appInstance.sceneManager().replaceScene(new HallScene())
        },

        onInviteFriendBtnClick: function () {

        },

        onNextGameBtnClick: function () {
            let goMsg = {}
            if (this._playerNum === 2) {
                goMsg.roomMode = 2
                goMsg.roomId = 'R2'
            } else if (this._playerNum === 4) {
                goMsg.roomMode = 1
                goMsg.roomId = 'R1'
            }

            goMsg.gameType = 'M5'
            goMsg.pExtend = 'gameResult'
            appInstance.gameAgent().tcpGame().enterTable(goMsg)
        },

        initData: function (pData) {
            this._pData = pData
            this._playerNum = pData.pPlayerNum || 2
            this._players = pData.players
            this._pos = this._posConst[this._playerNum]
            // this._selfInfo = pData.getSelfInfo()
            this._selfInfo = pData.players[0]
        },

        initView: function (pData) {

            pData = {"__instanceId":445,"players":{"0":{"__instanceId":447,"handCards":[{"nCardColor":2,"nCardNumber":9},{"nCardColor":2,"nCardNumber":9},{"nCardColor":2,"nCardNumber":8},{"nCardColor":2,"nCardNumber":9},{"nCardColor":2,"nCardNumber":8},{"nCardColor":0,"nCardNumber":3},{"nCardColor":0,"nCardNumber":5}],"putCards":[{"nCardColor":1,"nCardNumber":2},{"nCardColor":1,"nCardNumber":2},{"nCardColor":2,"nCardNumber":3}],"showCards":[{"pShowType":2,"pActionID":110,"pPengCardColor":2,"pPengCardNumber":1},{"pShowType":1,"pActionID":50,"pChiCardColor":2,"pBeginIndex":4}],"handCardCount":7,"pid":92562,"nickName":"麻将92562","coins":"0","robotModel":0,"age":0,"sex":1,"photo":"1","sdkPhotoUrl":"","exp":0,"vip":0,"pSeatID":0,"netIp":"","win":0,"lose":0,"role":4,"pIsTing":true,"pCoins":"40","pOffsetCoins":40,"pChiList":[{"pChiCardColor":2,"pBeginIndex":4,"pEndIndex":6}],"pPengList":[{"nCardColor":2,"nCardNumber":1}],"pGangList":[],"pIsQingYiSe":0,"pDoubleCount":3,"pDoubleList":[{"pDouble":3},{"pDouble":0},{"pDouble":95}],"pMaxDoubleID":0,"pJiangList":[],"pMaxWatchNum":5,"pAlreadyWatchNum":0,"pGameResultExtend":"{\"piao\":0}"},"1":{"__instanceId":446,"handCards":[{"nCardColor":0,"nCardNumber":1},{"nCardColor":0,"nCardNumber":1},{"nCardColor":0,"nCardNumber":6},{"nCardColor":0,"nCardNumber":6},{"nCardColor":0,"nCardNumber":6},{"nCardColor":1,"nCardNumber":3},{"nCardColor":2,"nCardNumber":4},{"nCardColor":2,"nCardNumber":8},{"nCardColor":2,"nCardNumber":8},{"nCardColor":2,"nCardNumber":9},{"nCardColor":2,"nCardNumber":9},{"nCardColor":2,"nCardNumber":9},{"nCardColor":0,"nCardNumber":3}],"putCards":[{"nCardColor":0,"nCardNumber":4}],"showCards":[],"handCardCount":13,"pid":92563,"nickName":"麻将92563","coins":"0","robotModel":0,"age":0,"sex":1,"photo":"1","sdkPhotoUrl":"","exp":0,"vip":0,"pSeatID":1,"netIp":"","win":0,"lose":0,"role":4,"pCoins":"-40","pOffsetCoins":-40,"pChiList":[],"pPengList":[],"pGangList":[],"pIsQingYiSe":0,"pDoubleCount":40,"pDoubleList":[{"pDouble":49},{"pDouble":37}],"pMaxDoubleID":0,"pJiangList":[],"pMaxWatchNum":5,"pAlreadyWatchNum":0,"pGameResultExtend":"{\"piao\":0}"}},"tableData":{"__instanceId":448,"pMustOutCard":[],"pTableID":"P_R2_M4_1599551231128","pMySeatID":1,"pChangePid":92563,"pChangeSeatID":1,"pEnterOrQuite":1,"pKeyPrivateTable":"","pSoundAppId":"-1","pIsOpenSoundApp":1,"pDongSeatID":1,"pChoiceRule":"99,37,96,1128","pTableStatus":100,"pRoomID":"R2","pMode":2,"pPlayMode":"zhaoyuan","pGameType":"M4","pBaseCoin":1,"pTaskFlag":1,"msgId":-2146369528,"msgName":"TableChangeProto","pPlayerNum":2,"nZhuangSeatID":0,"nDeckCardNum":34,"pCurSeatID":-1,"pTStatus":5,"pActions":[],"pPutSeatID":1,"lastPutCard":{"nCardColor":0,"nCardNumber":4},"pWinSeatID":0,"pHuType":[{"pHu":3},{"pHu":44}],"pBaoCard":{"nCardColor":0,"nCardNumber":7},"pBaseScore":1,"pIsLiuJu":0,"pBigWinSeatID":0,"pGameResultExtend":""},"pMySeatID":1,"pPlayerNum":2,"pDongSeatID":1,"uiSeatArray":[0,2]}
            cc.log('=========gameResult========pData====' + JSON.stringify(pData))
            this.initData(pData)

            this.PlayerCell.setVisible(false)
            for (let i = 0; i < this._playerNum; ++i) {
                this.initPlayerCell(i, this._players[i])
            }

            this.initSelfCard()


            let baoImg = appInstance.gameAgent().mjUtil().getCardValueImg(0, 'selfhand', this._pData.tableData.pBaoCard)
            this.BaoCard.getChildByName('CardValue').loadTexture(baoImg)
        },

        initSelfCard: function () {
            let handCards = this._selfInfo.handCards
            let pChiList = this._selfInfo.pChiList
            let pPengList = this._selfInfo.pPengList
            let pGangList = this._selfInfo.pGangList

            let cardLen = 45
            let offLen = 20

            let posX = 0
            for (let i = 0; i < handCards.length; ++i) {
                let card = this.CardCell.clone()
                this.CardNd.addChild(card)
                card.setPosition(cc.p(posX, 0))
                let cardImg = appInstance.gameAgent().mjUtil().getCardValueImg(0, 'selfhand', handCards[i])
                card.getChildByName('CardValue').loadTexture(cardImg)
                posX += cardLen
            }

            if ( pChiList.length) {
                posX += offLen
            }
            for (let i = 0; i < pChiList.length; ++i) {
                for (let cardNum = pChiList[i].pBeginIndex; cardNum <= pChiList[i].pEndIndex; ++cardNum) {
                    let card = this.CardCell.clone()
                    this.CardNd.addChild(card)
                    card.setPosition(cc.p(posX, 0))
                    let cardInfo = {
                        nCardColor: pChiList[i].pChiCardColor,
                        nCardNumber: cardNum
                    }
                    let cardImg = appInstance.gameAgent().mjUtil().getCardValueImg(0, 'selfhand', cardInfo)
                    card.getChildByName('CardValue').loadTexture(cardImg)
                    posX += cardLen
                }
            }

            if (pPengList.length) {
                posX += offLen
            }
            for (let index = 0; index < pPengList.length; ++index) {
                for (let i = 0; i < 3; ++i) {
                    let card = this.CardCell.clone()
                    this.CardNd.addChild(card)
                    card.setPosition(cc.p(posX, 0))
                    let cardImg = appInstance.gameAgent().mjUtil().getCardValueImg(0, 'selfhand', pPengList[index])
                    card.getChildByName('CardValue').loadTexture(cardImg)
                    posX += cardLen
                }
            }

            if (pGangList.length) {
                posX += offLen
            }
            for (let index = 0; index < pGangList.length; ++index) {
                for (let i = 0; i < 4; ++i) {
                    let card = this.CardCell.clone()
                    this.CardNd.addChild(card)
                    let cardImg = appInstance.gameAgent().mjUtil().getCardValueImg(0, 'selfhand', pGangList[index])
                    card.getChildByName('CardValue').loadTexture(cardImg)
                    if (i) {
                        card.setPosition(cc.p(posX + cardLen, 0))
                    } else {
                        card.setPosition(cc.p(posX, 0))
                        posX += cardLen
                    }

                }
            }
        },

        initPlayerCell: function (index, pinfo) {
            let cell = this.PlayerCell.clone()
            cell.setPosition(this._pos[index])
            cell.setVisible(true)
            this.midNd.addChild(cell)

            let head = cell.getChildByName('head')
            let name = cell.getChildByName('name')
            let bigWin = cell.getChildByName('bigWin')
            let aniNd = cell.getChildByName('aniNd')
            let winNum = cell.getChildByName('winNum')

            name.setString(pinfo.nickName)
            winNum.setString(pinfo.pOffsetCoins)
            bigWin.setVisible(this._pData.pWinSeatID === pinfo.pSeatID)
            let ani = appInstance.gameAgent().gameUtil().getAni(AniPlayer.FagePl)
            ani.setScale(0.25)
            aniNd.addChild(ani)

            if (pinfo.pOffsetCoins >= 0) {
                ani.setAnimation(0, PlayerPlay.win, true)
            } else {
                ani.setAnimation(0, PlayerPlay.lose, true)
            }
        },

        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        }
    })
    return Layer
})
