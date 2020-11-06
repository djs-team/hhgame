
load('game/ui/layer/coingame/CoinGameLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let LocalSave = include('game/public/LocalSave')
    let CoinGameMdt = include('game/ui/layer/coingame/CoinGameMdt')
    let GameUtil = include('game/public/GameUtil')
    let GameEvent = include('game/config/GameEvent')
    let GameConfig = include('game/config/GameConfig')
    let coinGameLayer = BaseLayer.extend({
        _className: 'coinGameLayer',
        _roomId: {
            '4': 'R1',
            '2': 'R2'
        },
        RES_BINDING: function () {
            return {
                'topPnl/closeBtn': { onClicked: this.onCloseClick },
                'topPnl/coinPnl': { onClicked: this.onCoinShopClick},
                'topPnl/coinPnl/coinAddBtn': {onClicked: this.onCoinShopClick},
                'topPnl/diamondsPnl': { onClicked: this.onCoinShopClick},
                'topPnl/diamondsPnl/diamondsAddBtn': {onClicked: this.onCoinShopClick},
                'bmPnl/startQuickBtn': { onClicked: this.onStartQuickBtnClick },
                'bmPnl/startQuickBtn/StartQuickName': {  },
                'leftPnl/PeopleBtn2': { onClicked: this.onPeopleClick },
                'leftPnl/PeopleBtn4': { onClicked: this.onPeopleClick },
                'MidPnl': { },
            }
        },
        ctor: function () {
            appInstance.gameAgent().hideLoading()
            this._super(ResConfig.View.CoinGameLayer)
            this.registerMediator(new CoinGameMdt(this))
        },

        onCoinShopClick: function (sender) {
            GameUtil.delayBtn(sender);
            appInstance.gameAgent().addPopUI(ResConfig.Ui.CoinShopLayer)
        },

        onEnter: function () {
            this._super()
        },

        initData: function (data) {
            this._goBtnArray = []
            this._peopleNum = global.localStorage.getIntKey(LocalSave.CoinGamePeopleNum)
            if (!this._peopleNum) {
                this._peopleNum = 2
            }
            this.onUpdatePropsData(data)
        },

        initView: function (data) {
            this.initData(data)
            for (let i = 1; i < 5; ++i) {
                this.initGoBtn(this.MidPnl.getChildByName('goBtn' + i), GameConfig.goBtnData[i - 1])
            }
            this.updatePeopleBtn()
            if (this._peopleNum === 2) {
                this.StartQuickName.setString('二人')
            } else if (this._peopleNum === 4) {
                this.StartQuickName.setString('四人')
            }
        },

        goRoomClick: function (sender) {

            let data = sender._sendMsg

            if(!this.onCanEnterTable(data))
                return

            let goMsg = {}
            if (this._peopleNum === 2) {
                goMsg.roomMode = 2
                goMsg.roomId = 'R2'
            } else if (this._peopleNum === 4) {
                goMsg.roomMode = 1
                goMsg.roomId = 'R1'
            }
            goMsg.gameType = data._gameType
            goMsg.pExtend = 'gameHall'
            appInstance.gameAgent().tcpGame().enterTable(goMsg)
            global.localStorage.setIntKey(LocalSave.CoinGamePeopleNum, this._peopleNum)
        },

        onCanEnterTable: function (data) {

            let flag = true
            let tipMsg = ''
            let minCost = data._minCost
            let maxCost = data._maxCost

            if( this._coinCnt < minCost){
                // tipMsg = '您的金币不足，请充值'
                // flag = false
                let dialogMsg = {
                    ViewType: 1,
                    TileName : '提 示',
                    LeftBtnName: '取 消',
                    RightBtnName : '去兑换',
                    RightBtnClick : function () {
                        appInstance.gameAgent().addPopUI(ResConfig.Ui.CoinShopLayer)
                        appInstance.uiManager().removeUI(this)
                    }.bind(this),

                    SayText : '您的金币不足，是否去商城兑换'
                }
                appInstance.gameAgent().addDialogUI(dialogMsg)
                return
            }else if(maxCost &&  this._coinCnt > maxCost){
                tipMsg = '您的金币超出当前场次限制，请更换更高场次'
                flag = false
            }

            if(!flag)
                appInstance.gameAgent().Tips(tipMsg)

            return flag
        },

        initGoBtn: function (btnCell, btnData) {
            if(this._goBtnArray.indexOf(this.startQuickBtn) === -1){
                this._goBtnArray.push(this.startQuickBtn)
            }
            this._goBtnArray.push(btnCell)
            btnCell.getChildByName('Score').setString(btnData.score)
            btnCell.getChildByName('Cost').setString(btnData.cost)

            btnCell._sendMsg = {
                _gameType: btnData.gameType,
                _minCost: btnData.minCost,
                _maxCost: btnData.maxCost,

            }

            btnCell.addClickEventListener(function(sender, et) {
                GameUtil.delayBtns(this._goBtnArray)
                this.goRoomClick(sender)
            }.bind(this))
        },

        onPeopleClick: function (sender) {
            GameUtil.delayBtn(sender);
            if (sender === this.PeopleBtn2) {
                this._peopleNum = 2
            } else if ( sender === this.PeopleBtn4) {
                this._peopleNum = 4
            }
            this.updatePeopleBtn()
        },

        updatePeopleBtn: function () {
            if (this._peopleNum === 2) {
                this.PeopleBtn2.setTouchEnabled(false)
                this.PeopleBtn2.setBright(false)
                this.PeopleBtn4.setTouchEnabled(true)
                this.PeopleBtn4.setBright(true)
            } else if (this._peopleNum === 4) {
                this.PeopleBtn2.setTouchEnabled(true)
                this.PeopleBtn2.setBright(true)
                this.PeopleBtn4.setTouchEnabled(false)
                this.PeopleBtn4.setBright(false)
            } else {
                this._peopleNum = 2
                this.updatePeopleBtn()
            }
        },

        onStartQuickBtnClick: function () {
            GameUtil.delayBtns(this._goBtnArray)
            let goMsg = {}
            if (this._peopleNum === 2) {
                goMsg.roomMode = 2
                goMsg.roomId = 'R2'
            } else if (this._peopleNum === 4) {
                goMsg.roomMode = 1
                goMsg.roomId = 'R1'
            }
            goMsg.gameType = 'M5'
            goMsg.pExtend = 'gameHall'
            appInstance.gameAgent().tcpGame().enterTable(goMsg)
            global.localStorage.setIntKey(LocalSave.CoinGamePeopleNum, this._peopleNum)
        },

        onUpdatePropsData: function (data) {
            if( !data) return

            let coinsCnt = this.coinPnl.getChildByName('coinsCnt')
            let diamondsCnt = this.diamondsPnl.getChildByName('diamondsCnt')

            if (data.hasOwnProperty('coin')) {
                this._coinCnt = data.coin
                coinsCnt.setString(GameUtil.getStringRule(data.coin))
            }

            if (data.hasOwnProperty('diamonds')) {
                diamondsCnt.setString(GameUtil.getStringRule(data.diamonds))
            }



        },

        onCreate: function () {
            this._super()
        },

        onExit: function () {
            this._super()
        },
        onCloseClick: function () {
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)
        }
    })
    return coinGameLayer
})
