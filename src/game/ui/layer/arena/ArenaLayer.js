
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
                'commonPgPnl': { },
                'pnl': { },
                'pnl/closeBtn': { onClicked: this.onCloseBtnClick },
                'pnl/leftPnl': { },
                'pnl/leftCell': { },
                'pnl/areanListView': { },
                'pnl/areanCell': { },
                'pnl/recordPnl': { onClicked: this.onAwardBtnClick },
                'pupupPnl': { },
                'pupupPnl/popupCloseBtn': { onClicked: this.onHideBtnClick },
                'pupupPnl/leftBtnPnl/rewardsBtn': { onClicked: this.onMatchDetailBtnClick },
                'pupupPnl/leftBtnPnl/formatBtn': { onClicked: this.onMatchDetailBtnClick },
                'pupupPnl/rewardsPnl': { },
                'pupupPnl/rewardsPnl/rewardsListView': { },
                'pupupPnl/rewardsPnl/rewardsCell': { },
                'pupupPnl/formatPnl': { },
                'myAwardPnl': { },
                'myAwardPnl/awardCell': { },
                'myAwardPnl/awardList': { },
                'myAwardPnl/awardCloseBtn': { onClicked: this.onAwardCloseBtnClick },
                'exchangePnl': {},
                'exchangePnl/goodsUrl': {},
                'exchangePnl/phonePnl': {},
                'exchangePnl/phonePnl/phoneTextField': {},
                'exchangePnl/rechargePhoneBtn': {},
                'exchangePnl/phoneCloseBtn': { onClicked: this.onPhoneCloseBtnClick },
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
        onAwardBtnClick: function (sender) {
            GameUtil.delayBtn(sender);
            this.commonPgPnl.setVisible(false)
            this.pnl.setVisible(false)
            this.pupupPnl.setVisible(false)
            this.myAwardPnl.setVisible(true)
            this.awardCell.setVisible(false)
            let msg = {'type':6}
            appInstance.gameAgent().httpGame().FUKAMATERIALOGReq(msg)
        },
        onAwardCloseBtnClick: function () {
            this.commonPgPnl.setVisible(true)
            this.pnl.setVisible(true)
            this.pupupPnl.setVisible(false)
            this.myAwardPnl.setVisible(false)
        },
        onPhoneCloseBtnClick: function () {
            this.exchangePnl.setVisible(false)
        },
        initView: function () {
            this.awardList.setScrollBarEnabled(false)
            this.commonPgPnl.setVisible(true)
            this.pnl.setVisible(true)
            this.areanListView.setScrollBarEnabled(false)
            this.rewardsListView.setScrollBarEnabled(false)
            this.pupupPnl.setVisible(false)
            this.myAwardPnl.setVisible(false)
            this.areanCell.setVisible(false)
            this.rewardsCell.setVisible(false)
            this.leftCell.setVisible(false)
            this.exchangePnl.setVisible(false)

        },
        onUpdateAwardList: function (msg) {
            this.awardList.removeAllChildren()
            let goodInfo = msg.playerGoodsLogList
            let length = goodInfo.length
            for (let i=0; i<length; i++) {
                this.onUpdateAwardCellView(goodInfo[i], i);
            }

        },
        onUpdateAwardCellView: function (data, i) {
            let cell = this.awardCell.clone();
            cell.setVisible(true)
            this.awardList.pushBackCustomItem(cell)
            if (i%2==0) {
                cell.getChildByName('bgPic_1').setVisible(true)
                cell.getChildByName('bgPic_2').setVisible(false)
            } else {
                cell.getChildByName('bgPic_1').setVisible(false)
                cell.getChildByName('bgPic_2').setVisible(true)
            }
            cell._sendMsg = {
                orderCode : data.orderCode,
                propValue : data.propValue,
                goodsUrl : data. goodsUrl,
            }
            let size = {
                height : 56.00,
                width : 56.00
            }
            this.onLoadUrlImg(data.goodsUrl,size,cell.getChildByName('awardContentPnl').getChildByName('awardImg'))
            cell.setName(data.orderCode)
            cell.getChildByName('awardContentPnl').getChildByName('awardImg').getChildByName('awardText').setString(data.goodsName);
            cell.getChildByName('timeText').setString(this.onFormatDateTime(data.createTime));
            switch (data.status) {
                case 0:
                    cell.getChildByName('statusPnl').getChildByName('Text_56').setVisible(false)
                    cell.getChildByName('statusPnl').getChildByName('Button_13').setVisible(true)
                    cell.getChildByName('statusPnl').getChildByName('yunDanPnl').setVisible(false)
                    cell.getChildByName('statusPnl').getChildByName('Button_13').addClickEventListener(function (sender,dt) {
                        GameUtil.delayBtn(sender)
                        this.phoneExchange(cell._sendMsg)
                    }.bind(this))
                    break
                case 1:
                    cell.getChildByName('statusPnl').getChildByName('Text_56').setVisible(true)
                    cell.getChildByName('statusPnl').getChildByName('Text_56').setString('受理中')
                    cell.getChildByName('statusPnl').getChildByName('Button_13').setVisible(false)
                    cell.getChildByName('statusPnl').getChildByName('yunDanPnl').setVisible(false)
                    break
                case 2:
                    cell.getChildByName('statusPnl').getChildByName('Text_56').setVisible(true)
                    cell.getChildByName('statusPnl').getChildByName('Text_56').setString('已完成')
                    cell.getChildByName('statusPnl').getChildByName('Button_13').setVisible(false)
                    cell.getChildByName('statusPnl').getChildByName('yunDanPnl').setVisible(true)
                    if (data.expressCode != '') {
                        let expressCode = '';
                        if (data.expressCode.length>8) {
                            for (let j=0; j<8; j++) {
                                expressCode = expressCode+data.expressCode[j]
                            }
                            expressCode = expressCode+'...'
                        } else {
                            expressCode = data.expressCode
                        }

                        cell.getChildByName('statusPnl').getChildByName('yunDanPnl').getChildByName('Text_58_0').setString(expressCode)
                        cell.getChildByName('statusPnl').getChildByName('yunDanPnl').getChildByName('Button_13_0').addClickEventListener(function (sender,dt) {
                            appInstance.nativeApi().copy(data.expressCode)
                            appInstance.gameAgent().Tips('复制成功')
                        }.bind(this))
                    }
                    break
                default:
                    break
            }
        },

        phoneExchange: function (data) {
            this.exchangePnl.setVisible(true)
            let size = {
                height : 223.00,
                width : 130.00
            }
            this.onLoadUrlImg(data.goodsUrl,size,this.exchangePnl.getChildByName('goodsUrl'))
            this.rechargePhoneBtn.addClickEventListener(function (sender,et) {
                GameUtil.delayBtn(sender);
                this.onRechargePhoneBtnClick(data.orderCode)
            }.bind(this))
        },

        onRechargePhoneBtnClick: function (orderCode) {

            let pnoneNum = this.phoneTextField.getString()
            if(!(/^1[3|4|5|7|8][0-9]\d{8,11}$/.test(pnoneNum))){
                appInstance.gameAgent().Tips('手机号格式异常，请检查后重新输入')
                return
            }

            let msg = {
                orderCode : orderCode,
                mobile : pnoneNum
            }
            appInstance.gameAgent().httpGame().FUKAMATERIAEXCHANGEReq(msg)
        },

        onExchangeOnlineResult: function (data) {
            let status = data.status
            let orderCode = data.orderCode
            let isRefresh = true
            switch (status) {
                case 0:
                    isRefresh = false
                    appInstance.gameAgent().Tips('兑换成功')
                    this.onChangeRewardsCellData(orderCode)
                    break
                case 106:
                    appInstance.gameAgent().Tips('订单不存在')
                    break
                case 108:
                    appInstance.gameAgent().Tips('订单已处理')
                    break
                case 67:
                    isRefresh = false
                    appInstance.gameAgent().Tips('手机号格式异常，请检查后重新输入')
                    break
                case 107:
                    isRefresh = false
                    appInstance.gameAgent().Tips('该商品不可兑换，请联系客服')
                    break
                default:
                    isRefresh = false
                    appInstance.gameAgent().Tips('充值失败，请重试')
                    break
            }

            if(isRefresh){
                this.onPhoneCloseBtnClick()
                this.phoneTextField.setString('请输入充值手机号')
            }
        },

        onChangeRewardsCellData: function (orderCode) {
            let cell = this.awardList.getChildByName(orderCode)
            if (cell) {
                cell.getChildByName('statusPnl').getChildByName('Text_56').setVisible(true)
                cell.getChildByName('statusPnl').getChildByName('Button_13').setVisible(false)
                cell.getChildByName('statusPnl').getChildByName('yunDanPnl').setVisible(false)
                this.phoneTextField.setString('请输入充值手机号')
            }
            this.onPhoneCloseBtnClick()
        },

        onLoadUrlImg: function (url,size,cell) {
            cc.loader.loadImg(url, { isCrossOrigin: false },function(err,texture){
                if (!err && texture) {
                    let sp = new cc.Sprite(texture)
                    sp.setContentSize(size)
                    sp.setPosition(cc.p(size.width/2,size.height/2))
                    //sp.setRotationX(-90)
                    cell.addChild(sp)
                }
            })
        },
        initData: function () {

            this.initArenaTypeBtn()

        },
        onCloseBtnClick: function () {
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)
        },

        onHideBtnClick: function () {
            this.pupupPnl.setVisible(false)
        },

        onMatchDetailBtnClick: function (sender) {
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
                    let dialogMsg = {
                        ViewType: 1,
                        TileName : '提 示',
                        LeftBtnName: '我知道了',
                        RightBtnName : '成为会员',
                        RightBtnClick : function () {
                            appInstance.gameAgent().addPopUI(ResConfig.Ui.MemberLayer)
                            appInstance.uiManager().removeUI(this)
                        }.bind(this),

                        SayText : '本场比赛只针对VIP玩家开放'
                    }
                    appInstance.gameAgent().addDialogUI(dialogMsg)
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
            let forMatLength = 34
            format = GameUtil.onForMatTxt(format,forMatLength)
            this.formatPnl.getChildByName('startTime').setString(startTime)
            this.formatPnl.getChildByName('formatText').setString(format)
        },

        onFormatDateTime: function (timestamp) {

            let d = new Date(parseInt(timestamp));
            let month = (d.getMonth() + 1) < 10 ? (0 + "" + (d.getMonth() + 1)) : (d.getMonth() + 1);
            let day = d.getDate() < 10 ? (0 + "" + d.getDate()) : d.getDate();
            let hour = d.getHours() < 10 ? (0 + "" + d.getHours()) : d.getHours();
            let minute = d.getMinutes() < 10 ? (0 + "" + d.getMinutes()) : d.getMinutes();
            let second = d.getSeconds() < 10 ? (0 + "" + d.getSeconds()) : d.getSeconds();
            let dateString = d.getFullYear() + "-" + month + "-" + day + "\n" + hour + ":" + minute

            return dateString;

        }

    })
    return matchLayer
})
