
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
    let TableEvent = TableConfig.Event
    let HuType = TableConfig.HuType
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
                'topPnl/CloseBtn': { onClicked: this.onCloseBtnClick },
                'midPnl/midNd': {  },
                'midPnl/midNd/PlayerCell': {  },
                'bmPnl': {  },
                'bmPnl/BackHallBtn': { onClicked: this.onBackHallBtnClick },
                'bmPnl/InviteFriendBtn': { onClicked: this.onInviteFriendBtnClick },
                'bmPnl/NextGameBtn': { onClicked: this.onNextGameBtnClick },
                'bmPnl/BaoCard': {  },
                'bmPnl/CardCell': {  },
                'bmPnl/InfoCell': {  },
            }
        },

        ctor: function (msg) {
            this._super(ResConfig.View.DeskResultLayer)
            this._msg = msg
            this.registerMediator(new DeskResultLayerMdt(this))
            cc.log('=======DeskResultLayer==========' + JSON.stringify(msg))
        },

        onCloseBtnClick: function () {
            if (this._isMatch) {
                appInstance.uiManager().removeUI(this)
            } else {
                this.onBackHallBtnClick()
            }
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
            this._isMatch = pData.isMatch
            this._playerNum = pData.pPlayerNum || 2
            this._players = pData.players
            this._pos = this._posConst[this._playerNum]
            this._selfInfo = pData.getSelfInfo()

            this._playerInfoCell = {}
            this._playerCell = {}
        },

        initView: function (pData) {
            cc.log('==============GameResult============' + JSON.stringify(pData))
            this.initData(pData)

            this.PlayerCell.setVisible(false)
            this.InfoCell.setVisible(false)

            for (let i = 0; i < this._playerNum; ++i) {
                this.initPlayerCell(i, this._players[i])
            }

            let baoImg = appInstance.gameAgent().mjUtil().getCardValueImg(0, 'selfhand', this._pData.tableData.pBaoCard)
            this.BaoCard.getChildByName('CardValue').loadTexture(baoImg)

            let initInfo = {}
            initInfo._index = 0
            this.onInfoBtnClick(initInfo)

            if (this._isMatch) {
                this.BackHallBtn.setVisible(false)
                this.InviteFriendBtn.setVisible(false)
                this.NextGameBtn.setVisible(false)
                cc.log('===========msg============' + JSON.stringify(this._msg))
                if (this._msg) {
                    cc.log('=============this._msg.pIsOver==========' + this._msg.pIsOver)
                    if (this._msg.pIsOver === 1) {
                        appInstance.sendNotification(TableEvent.clearTableView)
                    } else {
                        cc.log('===========================清理其他数据===============')
                        appInstance.sendNotification(TableEvent.clearTableGaming)
                    }
                }
            } else {
                appInstance.sendNotification(TableEvent.clearTableView)
            }
        },

        onInfoBtnClick: function (sender) {
            let index = sender._index
            for (let i = 0; i < this._playerNum; ++i) {
                this._playerCell[i].getChildByName('InfoBtn').setVisible(true)
                this._playerInfoCell[i].setVisible(false)
            }
            this._playerCell[index].getChildByName('InfoBtn').setVisible(false)
            this._playerInfoCell[index].setVisible(true)
        },

        initPlayerInfo: function (index, pinfo) {
            let infoCell = this.InfoCell.clone()
            this._playerInfoCell[index] = infoCell
            this.bmPnl.addChild(infoCell)
            infoCell.getChildByName('nameTxt').setString(pinfo.nickName)

            let ruleTxt = infoCell.getChildByName('ruleTxt')
            let CardNd = infoCell.getChildByName('CardNd')

            let handCards = pinfo.handCards
            let pChiList = pinfo.pChiList
            let pPengList = pinfo.pPengList
            let pGangList = pinfo.pGangList

            let cardLen = 45
            let offLen = 20

            let posX = 0
            for (let i = 0; i < handCards.length; ++i) {
                let card = this.CardCell.clone()
                CardNd.addChild(card)
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
                    CardNd.addChild(card)
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
                    CardNd.addChild(card)
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
                    CardNd.addChild(card)
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

            let HuList = pinfo.pDoubleList
            let huTxt = ''
            for (let i = 0; i < HuList.length; ++i) {
                let txtTmp = HuType[HuList[i].pDouble] || '胡类型:' + HuList[i].pDouble
                huTxt += txtTmp
                huTxt += '   '
            }
            ruleTxt.setString(huTxt)
        },

        initPlayerCell: function (index, pinfo) {
            this.initPlayerInfo(index, pinfo)

            let cell = this.PlayerCell.clone()
            this._playerCell[index] = cell
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
            let ani = appInstance.gameAgent().gameUtil().getAni(AniPlayer[pinfo.pRole])
            ani.setScale(0.25)
            aniNd.addChild(ani)

            if (pinfo.pOffsetCoins >= 0) {
                ani.setAnimation(0, PlayerPlay.win, true)
            } else {
                ani.setAnimation(0, PlayerPlay.lose, true)
            }

            let InfoBtn = cell.getChildByName('InfoBtn')
            InfoBtn._index = index
            InfoBtn.addClickEventListener(function(sender, et) {
                this.onInfoBtnClick(sender)
            }.bind(this))
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
