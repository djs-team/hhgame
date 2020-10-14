
load('game/ui/layer/coingame/CoinGameLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let LocalSave = include('game/public/LocalSave')
    let CoinGameMdt = include('game/ui/layer/coingame/CoinGameMdt')
    let coinGameLayer = BaseLayer.extend({
        _className: 'coinGameLayer',
        _goBtnData: [
            { name: '新手场', score: 500, cost: '2000-6万', minCost: 2000, maxCost: 60000, gameType: 'M1'},
            { name: '初级场', score: 2000, cost: '6万-24万', minCost: 60000, maxCost: 240000, gameType: 'M2'},
            { name: '高级场', score: 5000, cost: '24万-60万', minCost: 240000, maxCost: 600000, gameType: 'M3'},
            { name: '大师场', score: 20000, cost: '60万以上', minCost: 600000, gameType: 'M4'},
        ],
        _roomId: {
            '4': 'R1',
            '2': 'R2'
        },
        RES_BINDING: function () {
            return {
                'topPnl/closeBtn': { onClicked: this.onCloseClick },
                'topPnl/coinPnl': { },
                'topPnl/diamondsPnl': { },
                'bmPnl/startQuickBtn': { onClicked: this.onStartQuickBtnClick },
                'bmPnl/startQuickBtn/StartQuickName': {  },
                'leftPnl/PeopleBtn2': { onClicked: this.onPeopleClick },
                'leftPnl/PeopleBtn4': { onClicked: this.onPeopleClick },
                'MidPnl': { },
            }
        },
        ctor: function () {
            this._super(ResConfig.View.CoinGameLayer)
            this.registerMediator(new CoinGameMdt(this))
        },

        onEnter: function () {
            this._super()
            this.initData()
            this.initView()
        },

        initData: function (data) {
            this._peopleNum = global.localStorage.getIntKey(LocalSave.CoinGamePeopleNum)
            if (!this._peopleNum) {
                this._peopleNum = 2
            }
            this.onUpdatePropsData(data)
        },

        initView: function (data) {
            this.initData(data)
            for (let i = 1; i < 5; ++i) {
                this.initGoBtn(this.MidPnl.getChildByName('goBtn' + i), this._goBtnData[i - 1])
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
            let coinsCnt = this.coinPnl.getChildByName('coinsCnt').getString()

            if(coinsCnt < minCost){
                tipMsg = '您的金币不足，请充值'
                flag = false
            }else if(maxCost && coinsCnt > maxCost){
                tipMsg = '您的金币超出当前场次限制，请更换更高场次'
                flag = false
            }

            if(!flag)
                appInstance.gameAgent().Tips(tipMsg)

            return flag
        },

        initGoBtn: function (btnCell, btnData) {
            btnCell.getChildByName('Score').setString(btnData.score)
            btnCell.getChildByName('Cost').setString(btnData.cost)

            btnCell._sendMsg = {
                _gameType: btnData.gameType,
                _minCost: btnData.minCost,
                _maxCost: btnData.maxCost,

            }

            btnCell.addClickEventListener(function(sender, et) {
                this.goRoomClick(sender)
            }.bind(this))
        },

        onPeopleClick: function (sender) {
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

            let coinsCnt = this.coinPnl.getChildByName('coinsCnt')
            let diamondsCnt = this.diamondsPnl.getChildByName('diamondsCnt')

            if (data.hasOwnProperty('coin')) {
                coinsCnt.setString(data.coin)
            }

            if (data.hasOwnProperty('diamonds')) {
                diamondsCnt.setString(data.diamonds)
            }



        },

        onCreate: function () {
            this._super()
        },

        onExit: function () {
            this._super()
        },
        onCloseClick: function () {
            appInstance.uiManager().removeUI(this)
        }
    })
    return coinGameLayer
})
