
load('game/ui/layer/arena/ArenaLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let ArenaMdt = include('game/ui/layer/arena/ArenaMdt')
    let matchLayer = BaseLayer.extend({
        _className: 'matchLayer',
        _arenaTypeBtnData: [
            { name: '金币赛', type: 1 },
            { name: '大奖赛', type: 2 },
            { name: '会员赛', type: 3 }
        ],
        _arenaBtnCellName: 'arenaBtn_',
        ctor: function () {
            this._super(ResConfig.View.ArenaLayer)
            this.registerMediator(new ArenaMdt(this))
        },
        RES_BINDING: function () {
            return {
                'pnl/closeBtn': { onClicked: this.onClose },
                'pnl/leftPnl': { },
                'pnl/leftCell': { },
                'pnl/areanListView': { },
                'pnl/areanCell': { },
                'pupupPnl': { },
                'pupupPnl/leftBtnPnl/rewardsBtn': { onClicked: this.matchDetailBtnClick },
                'pupupPnl/leftBtnPnl/formatBtn': { onClicked: this.matchDetailBtnClick },
                'pupupPnl/rewardsPnl/rewardsListView': { },
                'pupupPnl/rewardsPnl/rewardsCell': { },
                'pupupPnl/formatPnl': { },
            }
        },
        onCreate: function () {
            this._super()
            this.initData()
            this.initView()
        },
        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        },
        initView: function () {

            this.pupupPnl.setVisible(false)

        },
        initData: function () {

            this.initArenaTypeBtn()

        },
        onClose: function () {
            appInstance.uiManager().removeUI(this)
        },

        matchDetailBtnClick: function (sender) {

        },

        initArenaTypeBtn: function () {

            for(let i = 0; i < this._arenaTypeBtnData.length; i++){

                let data = this._arenaTypeBtnData[i]
                let cell = this.leftCell.clone()
                this.leftPnl.addChild(cell)

                cell.setName(this._arenaBtnCellName + i)
                cell.getChildByName('select').setVisible(false)
                cell.getChildByName('name').setString(data.name)
                cell._sendData = {
                    matchType : data.type,
                    cellName : cell.getName()
                }

                cell.addClickEventListener(function (sender,et) {
                    this.onAreanBtnClick(sender)
                }.bind(this))

                if(i == 0)
                    this.onAreanBtnClick(cell)
            }
        },

        onAreanBtnClick: function (sender) {

            let data = sender._sendData

            //判断当前点中的是哪个按钮，切换状态
            this.onUpdateArenaBtnStatus(data.cellName)

            //真正执行请求服务器数据的方法
            this.onGetMatchMessages(data.matchType)
        },

        onUpdateArenaBtnStatus: function (cellName) {

            for(let i = 0; i < this.leftPnl.getChildren().length; i++){
                let cell = this.leftPnl.getChildren()[i]
                cell.setPositionX(0)
                cell.setPositionY(0 - 110 * i)
                if(cell.getName() == cellName){
                    cell.getChildByName('common').setVisible(false)
                    cell.getChildByName('select').setVisible(true)
                    cell.setEnabled(false)
                }else{
                    cell.getChildByName('common').setVisible(true)
                    cell.getChildByName('select').setVisible(false)
                    cell.setEnabled(true)
                }

            }

        },

        onGetMatchMessages: function (matchType) {

            let msg = {
                matchType: matchType
            }

            appInstance.gameAgent().tcpGame().GetArenaMessageProto(msg)
        },


    })
    return matchLayer
})
