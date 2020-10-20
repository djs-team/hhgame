
load('game/ui/layer/arena/ArenaLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let ArenaMdt = include('game/ui/layer/arena/ArenaMdt')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let matchLayer = BaseLayer.extend({
        _className: 'matchLayer',
        _arenaTypeBtnData: [

            { name: '会员赛', type: 3 },
            { name: '大奖赛', type: 2 },
            { name: '金币赛', type: 1 },
        ],
        _arenaBtnCellName: 'arenaBtn_',
        _arenaType: 1,

        ctor: function () {
            this._super(ResConfig.View.ArenaLayer)
            this.registerMediator(new ArenaMdt(this))
        },
        RES_BINDING: function () {
            return {
                'pnl/closeBtn': { onClicked: this.onCloseBtnClick },
                'pnl/leftPnl': { },
                'pnl/leftCell': { },
                'pnl/areanListView': { },
                'pnl/areanCell': { },
                'pupupPnl': { },
                'pupupPnl/popupCloseBtn': { onClicked: this.onHideBtnClick },
                'pupupPnl/leftBtnPnl/rewardsBtn': { onClicked: this.onMatchDetailBtnClick },
                'pupupPnl/leftBtnPnl/formatBtn': { onClicked: this.onMatchDetailBtnClick },
                'pupupPnl/rewardsPnl': { },
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
            this.areanCell.setVisible(false)
            this.rewardsCell.setVisible(false)
            this.leftCell.setVisible(false)

        },
        initData: function () {

            this.initArenaTypeBtn()

        },
        onCloseBtnClick: function (sender) {
            GameUtil.delayBtn(sender);
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)
        },

        onHideBtnClick: function (sender) {
            GameUtil.delayBtn(sender);
            this.pupupPnl.setVisible(false)
        },

        onMatchDetailBtnClick: function (sender) {
            GameUtil.delayBtn(sender);
            if(sender == this.rewardsBtn)
                this._currentPopupBtn = 'rewards'
            else
                this._currentPopupBtn = 'format'

            this.onShowPopupBtnClickResult()
        },

        onShowPopupBtnClickResult: function () {
            if(this._currentPopupBtn == 'rewards'){
                this.rewardsBtn.setEnabled(false)
                this.rewardsBtn.setBright(false)
                this.formatBtn.setEnabled(true)
                this.formatBtn.setBright(true)

                this.rewardsPnl.setVisible(true)
                this.formatPnl.setVisible(false)
            }else{
                this.rewardsBtn.setEnabled(true)
                this.rewardsBtn.setBright(true)
                this.formatBtn.setEnabled(false)
                this.formatBtn.setBright(false)

                this.rewardsPnl.setVisible(false)
                this.formatPnl.setVisible(true)
            }
        },

        initArenaTypeBtn: function () {

            for(let i = 0; i < this._arenaTypeBtnData.length; i++){

                let data = this._arenaTypeBtnData[i]
                let cell = this.leftCell.clone()
                this.leftPnl.addChild(cell)

                cell.setPositionX(0)
                cell.setPositionY(215 - 110 * i)
                cell.setName(this._arenaBtnCellName + i)
                cell.getChildByName('select').setVisible(false)
                cell.getChildByName('name').setString(data.name)
                cell._sendData = {
                    matchType : data.type,
                    cellName : cell.getName()
                }

                cell.addClickEventListener(function (sender,et) {
                    GameUtil.delayBtn(sender);
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

        onInitMatchList: function (data) {

            this.areanListView.removeAllChildren()
            this._arenaType = data.matchType

            for(let i = 0; i < data.matchList.length; i++){

                let arenaData = data.matchList[i]
                let cell = this.areanCell.clone()
                cell.setVisible(true)
                this.areanListView.pushBackCustomItem(cell)

                cell.getChildByName('recordImg').loadTexture(arenaData.propRes)
                cell.getChildByName('name').setString(arenaData.matchtitle)
                cell.getChildByName('areaName').setString(arenaData.areaName)
                cell.getChildByName('peopleCnt').setString(arenaData.matchplayersnum)


                if(arenaData.state == 0){
                    cell.getChildByName('statusExplain').setString(arenaData.time)
                    cell.getChildByName('signBtn').setBright(false)
                    cell.getChildByName('signBtn').setEnabled(false)
                }else{
                    cell.getChildByName('statusExplain').setString('坐满即开')
                    cell.getChildByName('signBtn').setBright(true)
                    cell.getChildByName('signBtn').setEnabled(true)
                }
                cell.getChildByName('signBtn').getChildByName('signPropImg').loadTexture(arenaData.consumptionRes)
                cell.getChildByName('signBtn').getChildByName('signPropCnt').setString(arenaData.matchfee)

                cell._sendData = {
                    pExtend: 'match_arena',//请求参加比赛时，传给服务器
                    roomMode: arenaData.matchMode,//请求参加比赛时，传给服务器
                    roomId: arenaData.matchId,//请求参加比赛时，传给服务器
                    startTime: arenaData.time,
                    format: arenaData.format,
                    consumptionType: arenaData.consumptionType,//比赛场入场费消耗类型 1金币2钻石
                    matchfee: arenaData.matchfee,//报名费
                    rankingList: arenaData.rankingList,
                }

                cell.getChildByName('explainBtn').addClickEventListener(function (sender,dt) {
                    GameUtil.delayBtn(sender);
                    this.onShowPopupClick(sender)
                }.bind(this))

                cell.getChildByName('signBtn').addClickEventListener(function (sender,dt) {
                    GameUtil.delayBtn(sender);
                    this.onSignMatchClick(sender)
                }.bind(this))
            }
        },

        onShowPopupClick: function (sender) {

            let data = sender.getParent()._sendData
            this.onFormatRewardsData(data.rankingList)
            this.onFormatSaiZhiData(data.startTime,data.format)
            this._currentPopupBtn = 'rewards'
            this.onShowPopupBtnClickResult()

            this.pupupPnl.setVisible(true)
        },

        onSignMatchClick: function (sender) {

            let data = sender.getParent()._sendData

            if(!this.onCanSignMatch(data))
                return

            this.onSignMatchFunction(data.roomMode,data.roomId,data.pExtend)
        },

        onCanSignMatch: function (data) {

            if(this._arenaType == 3 ){
                let vipCode = appInstance.dataManager().getUserData().vipCode
                if(vipCode <= 0){
                    appInstance.gameAgent().Tips('该功能只对会员开放')
                    return false
                }
            }

            let consumptionType = data.consumptionType
            let matchfee = data.matchfee
            let myPropCnt
            let propName

            if(consumptionType == 1){
                myPropCnt = appInstance.dataManager().getUserData().coin
                propName = '金币'
            }else{
                myPropCnt = appInstance.dataManager().getUserData().diamonds
                propName = '钻石'
            }


            if(matchfee > myPropCnt ){
                let dialogMsg = {
                    ViewType: 1,
                    TileName : '提 示',
                    LeftBtnName: '取 消',
                    RightBtnName : '确认',
                    RightBtnClick : function () {
                        appInstance.gameAgent().addPopUI(ResConfig.Ui.CoinShopLayer)
                        appInstance.uiManager().removeUI(this)
                    }.bind(this),

                    SayText : '您的' + propName + '不足,是否购买'
                }
                appInstance.gameAgent().addDialogUI(dialogMsg)
                return false

            }

            return true
        },


        onSignMatchFunction: function (roomMode,roomId,pExtend) {

            let msg = {
                roomMode: roomMode,
                roomId: roomId,
                pExtend: pExtend,
                gameType: ''
            }

            appInstance.gameAgent().tcpGame().enterTable(msg)

        },

        onFormatRewardsData: function (data) {

            this.rewardsListView.removeAllChildren()

            for(let i = 0; i < data.length; i++){

                let info = data[i]
                let cell = this.rewardsCell.clone()
                cell.setVisible(true)
                this.rewardsListView.pushBackCustomItem(cell)

                if(info.isImg){
                    cell.getChildByName('rankingImg').setVisible(true)
                    cell.getChildByName('rankingName').setVisible(false)
                    cell.getChildByName('bg1').setVisible(true)
                    cell.getChildByName('bg2').setVisible(false)
                    cell.getChildByName('rankingImg').loadTexture(info.rankingImg)
                }else{
                    cell.getChildByName('rankingImg').setVisible(false)
                    cell.getChildByName('rankingName').setVisible(true)
                    cell.getChildByName('bg1').setVisible(false)
                    cell.getChildByName('bg2').setVisible(true)
                    cell.getChildByName('rankingName').setString(info.ranking)
                }

                cell.getChildByName('rewards').setString(info.propContext)

            }

        },

        onFormatSaiZhiData: function (startTime,format) {
            this.formatPnl.getChildByName('startTime').setString(startTime)
            this.formatPnl.getChildByName('formatText').setString(format)
        },

    })
    return matchLayer
})
