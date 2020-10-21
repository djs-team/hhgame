
load('game/ui/layer/email/EmailLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let GameEvent = include('game/config/GameEvent')
    let EmailMdt = include('game/ui/layer/email/EmailMdt')
    let GameUtil = include('game/public/GameUtil')
    let EmailLayer = BaseLayer.extend({
        _className: 'EmailLayer',
        _startIndex: 0, //开始index
        _endIndex: 6,   //结束index
        _indexLength: 6, //每页长度
        _isCanRefreshData: true,
        _delMailId: [],
        _allMailId: [],
        _rewardMailId: 0,
        _rewardCount: 0,
        ctor: function () {
            this.registerMediator(new EmailMdt(this))
            this._super(ResConfig.View.EmailLayer)
        },
        RES_BINDING: function () {
            return {
                'pnl/closeBtn': { onClicked: this.onClose },

                'pnl/bgPnl': {},
                'pnl/bgPnl/rightBgPic': {},
                'pnl/bgPnl/emptyBgPic': {},

                'pnl/leftPnl': {},
                'pnl/leftPnl/leftList': {},
                'pnl/leftPnl/leftCellPnl': {},

                'pnl/rightPnl': {},
                'pnl/rightPnl/topPnl': {},
                'pnl/rightPnl/bottomPnl': {},
                'pnl/rightPnl/bottomPnl/rewardCell': {},
                'pnl/rightPnl/bottomPnl/rewardList': {},
                'pnl/rightPnl/bottomPnl/rewardBtn': {onClicked: this.onSubmitReward},
            }
        },
        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
            this._delMailId = []
            this._allMailId = []
            this.initData()
            this.initView()
        },
        onExit: function () {
            this._super()
        },
        onClose: function (sender) {
            GameUtil.delayBtn(sender);
            let msg = {
                'mailIdList' : this.removeRepeatArray(this._delMailId)
            }
            appInstance.gameAgent().httpGame().emailDelReq(msg)
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)
        },
        initView: function () {
            this.leftList.setScrollBarEnabled(false)
            this.rewardList.setScrollBarEnabled(false)
            this.rightBgPic.setVisible(false)
            this.emptyBgPic.setVisible(false)
            this.leftPnl.setVisible(false)
            this.rightPnl.setVisible(false)
            this.leftCellPnl.setVisible(false)
            this.rewardCell.setVisible(false)
        },
        initData: function () {
            this.leftList.addEventListener(this.selectedItemEvent, this)
        },
        selectedItemEvent: function (sender, type) {
            if (type !== ccui.ListView.EVENT_SELECTED_ITEM) {
                return
            }
            let curIndex = sender.getCurSelectedIndex()//当前手指点击的行，坐标以0开始
            let offIndex = 6 //一页展示的子节点cell
            let childLen = sender.getItems().length
            if (curIndex > (childLen - offIndex)) {
                this.refreshData()
            }
        },
        onEmailView: function (data) {
            let startIndex = this._startIndex;
            if (data.length<this._indexLength) {
                this._isCanRefreshData = false
                if (this.leftList.getChildrenCount() <= 0 && data.length<=0) {
                    this.leftPnl.setVisible(false)
                    this.rightPnl.setVisible(false)
                    this.emptyBgPic.setVisible(true)
                    this.rightBgPic.setVisible(false)
                    return
                }
                this.leftPnl.setVisible(true)
                this.rightPnl.setVisible(true)
                this.emptyBgPic.setVisible(false)
                this.rightBgPic.setVisible(true)
            } else {
                if (this._startIndex == 0) {
                    this.leftPnl.setVisible(true)
                    this.rightPnl.setVisible(true)
                    this.emptyBgPic.setVisible(false)
                    this.rightBgPic.setVisible(true)
                }
                this.refreshEmailParam(this._endIndex)
            }
            for (let i=0; i<data.length; i++) {
                let info = data[i]
                this._allMailId.push(info.mailId)
                let cell = this.leftCellPnl.clone();
                cell._data = info
                cell.setVisible(true)
                this.leftList.pushBackCustomItem(cell)
                if (startIndex==0 && i==0) {
                    this._delMailId.push(info.mailId)
                    cell.getChildByName('select').setVisible(true)
                    cell.getChildByName('common').setVisible(false)
                    this.updateEmailContentView(info)
                }
                cell.setName(info.mailId)
                cell.getChildByName('selectValue').setString(info.mailTitle)
                cell.addTouchEventListener(function (sender, et) {
                    this.updateLeftList()
                    this.onSubmitTouch(sender, et)

                }.bind(this))
            }
        },
        removeRepeatArray: function (arr) {
            return arr.filter(function (item, index, self) {
                return self.indexOf(item) === index;
            });
        },
        updateLeftList: function () {
            for (let i = 0; i < this._allMailId.length; i++) {
                this.setLeftCellStatus(this._allMailId[i])
            }
        },
        setLeftCellStatus: function (mailId) {
            let common = this.leftList.getChildByName(mailId).getChildByName('common')
            let select = this.leftList.getChildByName(mailId).getChildByName('select')
            common.setVisible(true)
            select.setVisible(false)
        },
        onSubmitTouch: function (sender, et) {
            let data = sender._data
            if (data.status == 0 || data.status == 2) {
                this._delMailId.push(data.mailId)
            }
            sender.getChildByName('common').setVisible(false)
            sender.getChildByName('select').setVisible(true)
            this.updateEmailContentView(data)
        },
        updateEmailContentView: function (data) {
            this._rewardMailId = data.mailId
            let rewardsList = this.rewardList
            rewardsList.removeAllChildren()
            this.topPnl.getChildByName('emailTitle').setString(data.mailTitle)
            this.topPnl.getChildByName('emailDate').setString(data.createTime)
            this.topPnl.getChildByName('emailContent').setString(data.mailInfo)
            let rewardsData = data.rewardsData
            this._rewardCount = rewardsData.length
            if (rewardsData.length<=0) {
                this.bottomPnl.setVisible(false)
                return
            }
            if (data.status==2) {
                this.rewardBtn.setVisible(false)
            } else {
                this.rewardBtn.setVisible(true)
            }
            for (let j=0; j<rewardsData.length; j++) {
                let information = rewardsData[j]
                let rewardsCell = this.rewardCell.clone()
                this.bottomPnl.setVisible(true)
                rewardsCell.setVisible(true)
                rewardsList.addChild(rewardsCell)
                rewardsCell.setName('rewards'+data.mailId+j)
                rewardsCell.getChildByName('rewardPic').loadTexture(information.res);
                rewardsCell.getChildByName('numValue').setString(information.num);
                if (data.status==2) {
                    rewardsCell.getChildByName('finishPic').setVisible(true);
                } else {
                    rewardsCell.getChildByName('finishPic').setVisible(false);
                }
            }
        },
        refreshEmailParam: function (startIndex) {
            this._startIndex = startIndex//邀请记录，开始行
            this._endIndex = startIndex + this._indexLength//邀请记录，结束行
        },
        refreshData: function () {
            if (!this._isCanRefreshData)
                return

            let msg = {
                'startIndex': this._startIndex,
                'endIndex': this._endIndex
            }

            appInstance.gameAgent().httpGame().emailInfoReq(msg)
        },
        onSubmitReward: function (sender) {
            GameUtil.delayBtn(sender);
            let msg = {
                'mailId' : this._rewardMailId
            }
            appInstance.gameAgent().httpGame().emailReceiveReq(msg)
        },
        onEmailReceive: function () {
            appInstance.gameAgent().Tips('------------------------------------领取成功！！！')
            for (let i=0; i<this._rewardCount; i++) {
                this.rewardList.getChildByName('rewards'+this._rewardMailId+i).getChildByName('finishPic').setVisible(true)
            }
            this.rewardBtn.setVisible(false)
        },
    })
    return EmailLayer
})
