
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
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let TableEvent = TableConfig.Event
    let HuType = TableConfig.HuType
    let Layer = BaseLayer.extend({
        _className: 'DeskResultLayer',
        _myCoin: 0,   //我的金币
        _minCoin: GameConfig.goBtnData[0]['minCost'],
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
        _watchMaxNum: 0,  //最大观看次数
        _usedWatchNum: 0,  //当前观看次数
        _isWatch: false,   //是否处于看视频自动开启下一场阶段
        _isCompleteHttp: false,   //http处理状况
        _isCompleteTcp: false,   //tcp处理状况
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
                'bmPnl/HuCard': {  },
                'bmPnl/CardCell': {  },
                'bmPnl/InfoCell': {  },
            }
        },

        ctor: function (msg) {
            this._super(ResConfig.View.DeskResultLayer)
            this._msg = msg
            this.registerMediator(new DeskResultLayerMdt(this))
            this.registerEventListener('rewardVideoCallback', this.onRewardVideoCallback)
        },

        onCloseBtnClick: function () {
            if (this._isMatch) {
                if (this._msg && this._msg.pIsOver === 0) {
                    appInstance.gameAgent().tcpGame().matchReady()
                }
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
            let InvitationLayer = include('game/ui/layer/invitation/InvitationLayer')
            this.addChild(appInstance.uiManager().createUI(InvitationLayer))
        },

        /**
         * 点击下一局按钮
         */
        onNextGameBtnClick: function (sender) {
            GameUtil.delayBtn(sender);
            //判断是否观看视频
            if (this._myCoin<this._minCoin && this._usedWatchNum<this._watchMaxNum) {
                if(cc.sys.os === cc.sys.OS_WINDOWS){
                    this._isWatch = true
                    appInstance.gameAgent().httpGame().AcceptAwardReq()
                } else {
                    appInstance.nativeApi().showRewardVideo()
                }
            } else if (this._myCoin<this._minCoin) {
                let dialogMsg = {
                    ViewType: 1,
                    TileName : '提 示',
                    LeftBtnName: '取 消',
                    RightBtnName : '去兑换',
                    RightBtnClick : function () {
                        appInstance.gameAgent().addPopUI(GameResConfig.Ui.CoinShopLayer)
                    }.bind(this),

                    SayText : '您的金币不足，是否去商城兑换'
                }
                appInstance.gameAgent().addDialogUI(dialogMsg)
            } else {
                this.toGamePlay()
            }
        },

        /**
         * 发起下一局游戏请求
         */
        toGamePlay: function () {
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

        /**
         * 更新用户当前金币
         */
        changeCoin: function () {
            this._myCoin = appInstance.dataManager().getUserData().coin
            this.updateNextBtnView()
            if (this._isWatch) {
                this._isCompleteTcp = true
                if (this._isCompleteHttp) {
                    this.updateGo();
                }
            }
        },

        /**
         * 视频回调
         * @param msg
         */
        onRewardVideoCallback: function (msg) {
            if (msg == "0") {
                this._isWatch = true
                appInstance.gameAgent().httpGame().AcceptAwardReq()
            }
        },

        /**
         * 看完视频发起下一场请求
         */
        updateNextGame: function () {
            this._isCompleteHttp = true
            this._usedWatchNum = this._usedWatchNum+1
            this.updateNextBtnView()
            if (this._isCompleteTcp) {
                this.updateGo();
            }
        },

        updateGo: function () {
            if (this._myCoin>=this._minCoin) {
                this.toGamePlay()
                this._isCompleteTcp = false
                this._isCompleteHttp = false
                this._isWatch = false
            } else {
                appInstance.gameAgent().Tips('-----------------------金币不足')
                this._isCompleteTcp = false
                this._isCompleteHttp = false
                this._isWatch = false
            }
        },

        initData: function (pData) {
            this._pData = pData
            this._isMatch = pData.isMatch()
            this._playerNum = pData.pPlayerNum || 2
            this._players = pData.players
            this._pos = this._posConst[this._playerNum]
            this._selfInfo = pData.getSelfInfo()

            this._playerInfoCell = {}
            this._playerCell = {}

            //更新我的金币
            this._myCoin = appInstance.dataManager().getUserData().coin
        },

        initView: function (pData) {
            this.initData(pData)
            this.PlayerCell.setVisible(false)
            this.InfoCell.setVisible(false)

            let initInfo = {}
            initInfo._index = 0

            for (let i = 0; i < this._playerNum; ++i) {
                this.initPlayerCell(i, this._players[i])
                if (this._players[i].pid === this._selfInfo.pid) {
                    initInfo._index = i
                    //初始化最大观看次数和已经观看次数
                    this._watchMaxNum = this._players[i].pMaxWatchNum
                    this._usedWatchNum = this._players[i].pAlreadyWatchNum
                }
            }
            this.onInfoBtnClick(initInfo)


            let baoCardInfo = this._pData.tableData.pBaoCard
            if (baoCardInfo.nCardNumber || baoCardInfo.nCardColor) {
                let baoImg = appInstance.gameAgent().mjUtil().getCardValueImg(0, 'selfhand', baoCardInfo)
                this.BaoCard.getChildByName('CardValue').loadTexture(baoImg)
                this.BaoCard.setVisible(true)
            } else {
                this.BaoCard.setVisible(false)
            }


            let huCardInfo = this._pData.tableData.pHuCard
            if (huCardInfo.nCardNumber || huCardInfo.nCardColor) {
                let HuImg = appInstance.gameAgent().mjUtil().getCardValueImg(0, 'selfhand', huCardInfo)
                this.HuCard.getChildByName('CardValue').loadTexture(HuImg)
                this.HuCard.setVisible(true)
            } else {
                this.HuCard.setVisible(false)
            }





            if (this._isMatch) {
                this.BackHallBtn.setVisible(false)
                this.InviteFriendBtn.setVisible(false)
                this.NextGameBtn.setVisible(false)
                if (this._msg) {
                    if (this._msg.pIsOver === 1) {
                        appInstance.sendNotification(TableEvent.clearTableView)
                    } else {
                        appInstance.sendNotification(TableEvent.clearTableGaming)
                    }
                }
            } else {
                appInstance.sendNotification(TableEvent.clearTableView)
            }

            this.updateNextBtnView()

        },

        /**
         * 更新下一局按钮样式
         */
        updateNextBtnView: function () {
            if (this._myCoin<this._minCoin) {
                this.NextGameBtn.getChildByName('Image_3').setVisible(true)
                this.NextGameBtn.getChildByName('Text_3').setVisible(true)
                this.NextGameBtn.getChildByName('Text_3').setString('剩余:'+(parseInt(this._watchMaxNum)-parseInt(this._usedWatchNum))+'/'+this._watchMaxNum)
                this.NextGameBtn.getChildByName('Text_1').setPosition(110.46, 54.86)
            } else {
                this.NextGameBtn.getChildByName('Image_3').setVisible(false)
                this.NextGameBtn.getChildByName('Text_3').setVisible(false)
                this.NextGameBtn.getChildByName('Text_1').setPosition(100, 50)
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

            let handCards = appInstance.gameAgent().mjUtil().sortCard(pinfo.handCards)
            handCards.reverse()
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
                let cardInfo = {
                    nCardColor: pGangList[index].pGangCardColor,
                    nCardNumber: pGangList[index].pGangCardNumber,
                }
                for (let i = 0; i < 4; ++i) {
                    let card = this.CardCell.clone()
                    CardNd.addChild(card)
                    let cardImg = appInstance.gameAgent().mjUtil().getCardValueImg(0, 'selfhand', cardInfo)
                    card.getChildByName('CardValue').loadTexture(cardImg)
                    if (!i) {
                        card.setPosition(cc.p(posX + cardLen, -10))
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

            GameUtil.loadUrlImg(pinfo.pPhoto,head)

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
