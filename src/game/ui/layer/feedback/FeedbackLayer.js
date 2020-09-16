
load('game/ui/layer/feedback/FeedbackLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let feedbackLayer = BaseLayer.extend({
        _className: 'feedbackLayer',
        ctor: function () {
            this._super(ResConfig.View.FeedbackLayer)
        },
        RES_BINDING: function () {
            return {
                'pnl/closeBtn': { onClicked: this.oncloseBtnClick },
                'pnl/FeedBackBtn': { onClicked: this.onFeedBackBtnClick },
                'pnl/MyMsgBtn': { onClicked: this.onMyMsgBtnClick },

                'pnl/MyMsgList': { },
                'pnl/FeedBackList': { },
                'pnl/FeedBackList/FeedBackBm': { },
                'pnl/FeedBackList/FeedBackBm/SendMsgBtn': { onClicked: this.onSendMsgBtnClick },
                'pnl/FeedBackList/FeedBackBm/EditTxt': { },

                'pnl/FeedBackMid': { },
                'pnl/FeedBackMid/EditMidTxt': { },
                'pnl/FeedBackMid/SendMsgMidBtn': { onClicked: this.onSendMsgBtnClick },
                'pnl/CellPnl': { },
                'pnl/CellPnl/TileCell': { },
                'pnl/CellPnl/TxtCell': { },
            }
        },
        onEnter: function () {
            this._super()
            this.initView()
        },

        initData: function () {
            this._leftIndex = 0

            this._historyInfo = []

            let sendMsg = {}
            sendMsg.tile = '提交问题'
            sendMsg.time = '2020-07-08   12:00:58'
            sendMsg.txt = '第一个提交的问题凑字数凑字数凑字数凑字数凑字数提交的问题凑字数凑字数凑字数凑字数凑字数提交的问题凑字数凑字数凑字数凑字数凑字数提交的问题凑字数凑字数凑字数凑字数凑字数'
            this._historyInfo.push(sendMsg)

            sendMsg = {}
            sendMsg.tile = '客服回复'
            sendMsg.time = '2020-07-08   12:00:58'
            sendMsg.txt = '第一个回复的问题'
            this._historyInfo.push(sendMsg)

            sendMsg = {}
            sendMsg.tile = '提交问题'
            sendMsg.time = '2020-07-08   12:00:58'
            sendMsg.txt = '第二个提交的问题'
            this._historyInfo.push(sendMsg)

            sendMsg = {}
            sendMsg.tile = '客服回复'
            sendMsg.time = '2020-07-08   12:00:58'
            sendMsg.txt = '第二个回复的问题'
            this._historyInfo.push(sendMsg)
        },

        initView: function () {
            this.MyMsgList.setVisible(false)
            this.FeedBackList.setVisible(false)
            this.FeedBackMid.setVisible(false)
            this.FeedBackMid.setVisible(false)

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

        updateMyMsg: function (onlyVisible) {
            this.FeedBackBtn.setTouchEnabled(true)
            this.FeedBackBtn.setBright(true)
            this.MyMsgBtn.setTouchEnabled(false)
            this.MyMsgBtn.setBright(false)

            this.MyMsgList.setVisible(true)
            this.FeedBackList.setVisible(false)
            this.FeedBackMid.setVisible(false)

            if (onlyVisible) {
                return
            }
            // 在这初始化我的消息
        },

        updateFeedBack: function (onlyVisible) {
            this.FeedBackBtn.setTouchEnabled(false)
            this.FeedBackBtn.setBright(false)
            this.MyMsgBtn.setTouchEnabled(true)
            this.MyMsgBtn.setBright(true)

            this.MyMsgList.setVisible(false)

            if (!this._historyInfo.length) {
                this.FeedBackMid.setVisible(true)
                this.FeedBackList.setVisible(false)
                return
            }

            this.FeedBackMid.setVisible(false)
            this.FeedBackList.setVisible(true)

            if (onlyVisible) {
                return
            }

            for (let i = 0; i < this._historyInfo.length; ++i) {
                this.pushFeedBackCell(this._historyInfo[i])
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

            let line = Math.ceil( txt.length / 35)

            let size = TxtCell.getContentSize()
            size.height = 50 * line
            TxtCell.setContentSize(size)
            TxtCell.getChildByName('bg1').setContentSize(size)
            TxtCell.getChildByName('bg1').setPositionY(size.height/2)
            size.width = size.width - 30
            TxtCell.getChildByName('info').setContentSize(size)

            TxtCell.getChildByName('info').setString(txt)
            TxtCell.getChildByName('info').setPositionY(size.height/2)

            this.FeedBackList.insertCustomItem(TxtCell, childCount)

            this.FeedBackList.refreshView()
            this.FeedBackList.jumpToBottom()
        },



        onFeedBackBtnClick: function () {
            this._leftIndex = 0
            this.updateFeedBack(true)
        },

        onMyMsgBtnClick: function () {
            this._leftIndex = 1
            this.updateMyMsg(true)
        },

        onSendMsgBtnClick: function () {
            let txtInfo = this.EditMidTxt.getString() || this.EditTxt.getString()
            if (!txtInfo || txtInfo === '') {
                appInstance.gameAgent().Tips('提交问题不能为空')
                return
            }
            let sendMsg = {}
            sendMsg.tile = '提交问题'
            sendMsg.time = '2020-07-08   12:00:58'
            sendMsg.txt = txtInfo
            this._historyInfo.push(sendMsg)

            this.EditMidTxt.setString('')
            this.EditTxt.setString('')
            this.pushFeedBackCell(sendMsg)
        },

        oncloseBtnClick: function () {
            appInstance.uiManager().removeUI(this)
        },
        onExit: function () {
            this._super()
        }
    })
    return feedbackLayer
})
