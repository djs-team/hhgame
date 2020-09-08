
load('game/ui/layer/coingame/CoinGameLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let LocalSave = include('game/public/LocalSave')
    let coinGameLayer = BaseLayer.extend({
        _className: 'coinGameLayer',
        _goBtnData: [
            { name: '新手场', score: 100, cost: '100-1000', gameType: 'M1'},
            { name: '初级场', score: 1000, cost: '1000-1万', gameType: 'M2'},
            { name: '高级场', score: 10000, cost: '1万-10万', gameType: 'M3'},
            { name: '大师场', score: 100000, cost: '10万-1000万', gameType: 'M4'},
        ],
        _roomId: {
            '4': 'R1',
            '2': 'R2'
        },
        RES_BINDING: function () {
            return {
                'topPnl/closeBtn': { onClicked: this.onCloseClick },
                'bmPnl/startQuickBtn': { onClicked: this.onStartQuickBtnClick },
                'bmPnl/startQuickBtn/StartQuickName': {  },
                'leftPnl/PeopleBtn2': { onClicked: this.onPeopleClick },
                'leftPnl/PeopleBtn4': { onClicked: this.onPeopleClick },
                'MidPnl': { },
            }
        },
        ctor: function () {
            this._super(ResConfig.View.CoinGameLayer)
        },

        onEnter: function () {
            this._super()
            this.initData()
            this.initView()
        },

        initData: function () {
            this._peopleNum = global.localStorage.getIntKey(LocalSave.CoinGamePeopleNum)
            if (!this._peopleNum) {
                this._peopleNum = 2
            }
        },

        initView: function () {
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
            let goMsg = {}
            if (this._peopleNum === 2) {
                goMsg.roomMode = 2
                goMsg.roomId = 'R2'
            } else if (this._peopleNum === 4) {
                goMsg.roomMode = 1
                goMsg.roomId = 'R1'
            }
            goMsg.gameType = sender._gameType
            goMsg.pExtend = 'gameHall'
            appInstance.gameAgent().tcpGame().enterTable(goMsg)
            global.localStorage.setIntKey(LocalSave.CoinGamePeopleNum, this._peopleNum)
        },

        initGoBtn: function (btnCell, btnData) {
            btnCell.getChildByName('Score').setString(btnData.score)
            btnCell.getChildByName('Cost').setString(btnData.cost)
            btnCell._gameType = btnData.gameType
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
