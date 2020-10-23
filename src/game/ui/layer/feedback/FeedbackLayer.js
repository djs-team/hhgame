
load('game/ui/layer/feedback/FeedBackLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let GameEvent = include('game/config/GameEvent')
    let FeedBackLayerMdt = include('game/ui/layer/feedback/FeedBackLayerMdt')
    let GameUtil = include('game/public/GameUtil')
    let feedbackLayer = BaseLayer.extend({
        _className: 'FeedBackLayer',
        _subMitType : 0,
        ctor: function () {
            this._super(ResConfig.View.FeedbackLayer)
            this.registerMediator(new FeedBackLayerMdt(this))
        },
        RES_BINDING: function () {
            return {
                'pnl/closeBtn': { onClicked: this.oncloseBtnClick },
                'pnl/FeedBackBtn': { onClicked: this.onFeedBackBtnClick },
                'pnl/MyMsgBtn': { onClicked: this.onMyMsgBtnClick },

                'pnl/FeedBackPnl': { },
                'pnl/FeedBackPnl/EditMidTxt': { },
                'pnl/FeedBackPnl/SendMsgMidBtn': { onClicked: this.onSendMsgBtnClick },

                'pnl/MyMsgPnl': { },
                'pnl/MyMsgPnl/MyMsgDetailPnl': { },
                'pnl/MyMsgPnl/MyMsgDetailPnl/MyMsgList': { },
                'pnl/MyMsgPnl/MyMsgDetailPnl/FeedBackList': { },
                'pnl/MyMsgPnl/MyMsgDetailPnl/FeedBackList/FeedBackBm': { },
                'pnl/MyMsgPnl/MyMsgDetailPnl/FeedBackList/FeedBackBm/SendMsgBtn': { onClicked: this.onSendMsgBtnClick },
                'pnl/MyMsgPnl/MyMsgDetailPnl/FeedBackList/FeedBackBm/EditTxt': { },
                'pnl/MyMsgPnl/MyMsgDetailPnl/CellPnl': { },
                'pnl/MyMsgPnl/MyMsgDetailPnl/CellPnl/TileCell': { },
                'pnl/MyMsgPnl/MyMsgDetailPnl/CellPnl/TxtCell': { },
                'pnl/MyMsgPnl/MyMsgListPnl': { },
                'pnl/MyMsgPnl/MyMsgListPnl/MyMsgListView': { },
                'pnl/MyMsgPnl/MyMsgListPnl/MyMsgCell': { },

            }
        },
        onEnter: function () {
            this._super()
            this.initView()
        },

        initData: function () {

            this._leftIndex = 0
        },

        initView: function () {
            this.MyMsgListView.setScrollBarEnabled(false)
            this.MyMsgPnl.setVisible(false)
            this.MyMsgCell.setVisible(false)
            this.initData()
            this.updateView()
        },

        updateView: function () {
            if (this._leftIndex === 0) {
                this.updateFeedBack()
            } else if (this._leftIndex === 1) {
                this.updateMyMsg()
            }
        },

        updateMyMsg: function () {
            this.FeedBackBtn.setTouchEnabled(true)
            this.FeedBackBtn.setBright(true)
            this.MyMsgBtn.setTouchEnabled(false)
            this.MyMsgBtn.setBright(false)


            this.FeedBackPnl.setVisible(false)
            this.MyMsgPnl.setVisible(true)
            this.MyMsgListPnl.setVisible(true)
            this.MyMsgDetailPnl.setVisible(false)

            appInstance.gameAgent().httpGame().FEEDBACKGETLISTReq()
        },

        updateMyMsgList: function (data) {

            this.MyMsgListView.removeAllChildren()
            for(let i = 0; i < data.length; i++){
                this.onInitMyMsgListCell(data[i])
            }
        },

        onInitMyMsgListCell: function (data) {
            let cell = this.MyMsgCell.clone()
            cell.setVisible(true)
            this.MyMsgListView.pushBackCustomItem(cell)

            cell._sendMsg = {
                feedbackId: data.feedbackId
            }
            cell.getChildByName('createTime').setString(data.createTime)

            if(data.status == 1){
                cell.getChildByName('status').setString('已处理')
                cell.getChildByName('status').setTextColor(cc.color(175,89,12))
            }

            cell.addClickEventListener(function (sender,dt) {
                GameUtil.delayBtn(sender);
                this.onClickMyMsgCell(sender)
            }.bind(this))

        },

        onClickMyMsgCell: function (sender) {
            let data = sender._sendMsg
            let msg = {
                feedbackId: data.feedbackId
            }
            appInstance.gameAgent().httpGame().FEEDBACKGETINFOReq(msg)
        },

        updateFeedBack: function () {
            this.FeedBackBtn.setTouchEnabled(false)
            this.FeedBackBtn.setBright(false)
            this.MyMsgBtn.setTouchEnabled(true)
            this.MyMsgBtn.setBright(true)

            this.MyMsgPnl.setVisible(false)
            this.FeedBackPnl.setVisible(true)
            this.EditMidTxt.setString('')

        },

        updateMyMsgDetail: function (data) {
            this._subMitFeedId = data.feedbackId
            this.MyMsgDetailPnl.setVisible(true)
            this.MyMsgListPnl.setVisible(false)

            let listChilds = this.FeedBackList.getChildren()

            for (let i = 0; i < listChilds.length; ++i) {
                if (listChilds[i] === this.FeedBackBm) {
                    continue
                }
                this.FeedBackList.removeChild(listChilds[i])
            }

            for (let i = 0; i < data.feedbackList.length; ++i) {
                this.pushFeedBackCell(data.feedbackList[i])
            }

        },

        onDealSubMitResult: function (data) {

            if(this._subMitType == 0){
                appInstance.gameAgent().Tips('提交成功！')
                this.EditMidTxt.setString('')
            }else{
                this.EditTxt.setString('')
                this.pushFeedBackCell(data)
            }

        },

        pushFeedBackCell: function (cellInfo) {

            let childCount = this.FeedBackList.getChildrenCount()

            let TileCell = this.TileCell.clone()
            this.FeedBackList.insertCustomItem(TileCell, childCount - 1)

            TileCell.getChildByName('tile').setString(cellInfo.tile)
            TileCell.getChildByName('time').setString(cellInfo.time)

            let TxtCell = this.TxtCell.clone()
            let txt = cellInfo.txt
            let forMatLength = 35

            let line = Math.ceil( txt.length / forMatLength)
            let forMatTxt = this.onForMatTxt(txt,forMatLength)

            let size = TxtCell.getContentSize()
            size.height = 50 * line
            TxtCell.setContentSize(size)
            TxtCell.getChildByName('bg1').setContentSize(size)
            TxtCell.getChildByName('bg1').setPositionY(size.height/2)
            size.width = size.width - 30
            TxtCell.getChildByName('info').setContentSize(size)

            TxtCell.getChildByName('info').setString(forMatTxt)
            TxtCell.getChildByName('info').setPositionY(size.height/2)

            this.FeedBackList.insertCustomItem(TxtCell, childCount)

            this.FeedBackList.refreshView()
            this.FeedBackList.jumpToBottom()
        },

        onForMatTxt: function (txt,forMatLength) {

            let forMatTxt = ''
            let cnt = 0
            for(let i = 0; i < txt.length; i++){
                if(i !== 0 && i%forMatLength === 0){
                    forMatTxt = forMatTxt + '\n'
                }
                forMatTxt = forMatTxt + txt[i]
            }
            return forMatTxt
        },



        onFeedBackBtnClick: function () {
            this._leftIndex = 0
            this.updateFeedBack(true)
        },

        onMyMsgBtnClick: function () {
            this._leftIndex = 1
            this.updateMyMsg(true)
        },

        onSendMsgBtnClick: function (sender) {
            let txtInfo = ''
            if(sender == this.SendMsgMidBtn){
                this._subMitType = 0
                txtInfo = this.EditMidTxt.getString()
            }else{
                this._subMitType = 1
                txtInfo = this.EditTxt.getString()
            }

            if (!txtInfo || txtInfo === '') {
                appInstance.gameAgent().Tips('提交问题不能为空')
                return
            }

            if(txtInfo.length > 150){
                appInstance.gameAgent().Tips('请不要超过150个字')
                return
            }


            let msg = {
                type: this._subMitType,
                text: txtInfo,
                feedbackId: this._subMitFeedId || 0
            }

            appInstance.gameAgent().httpGame().FEEDBACKSUBMITReq(msg)

        },

        oncloseBtnClick: function () {
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)
        },
        onExit: function () {
            this._super()
        }
    })
    return feedbackLayer
})
