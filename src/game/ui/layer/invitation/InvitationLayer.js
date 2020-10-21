load('game/ui/layer/invitation/InvitationLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let inviteUrl = ""
    let InvitationMdt = include('game/ui/layer/invitation/InvitationMdt')
    let invitationLayer = BaseLayer.extend({
        _className: 'invitationLayer',
        _returnType: 1,//1是关闭当前界面，2是返回邀请好友界面
        _inviChoiceType: 1,//1代表邀请好友，2代表我的邀请，3代表分享详情
        _startIndex: 0,//邀请记录，开始行
        _endIndex: 6,//邀请记录，结束行
        _indexLength: 6,//邀请记录，记录长度
        _isCanRefreshMyInvitationsData: true,

        ctor: function () {
            this._super(ResConfig.View.InvitationLayer)
            this.registerMediator(new InvitationMdt(this))
            this.registerEventListener('inviteCodeCallback', this.onInviteCodeCallback)
        },
        RES_BINDING: function () {
            return {
                'returnBtn': {onClicked: this.returnClike},
                'pnl/bgPnl': {},

                'screenLayout': {},
                'screenLayout/qrCodePg3': {},
                'pnl/invitationPnl': {},
                'pnl/invitationPnl/invitNowPnl': {},
                'pnl/invitationPnl/invitNowPnl/qrCodePg1': {},
                'pnl/invitationPnl/invitNowPnl/inviteNowBtn': {onClicked: this.onInviteNowClick},
                'pnl/invitationPnl/sfpPnl': {},
                'pnl/invitationPnl/sfpPnl/qrCodePg2': {},
                'pnl/invitationPnl/sfpPnl/sftWxBtn': {onClicked: this.onWxShareClick},
                'pnl/invitationPnl/sfpPnl/sftWxCircleBtn': {onClicked: this.onWxCircleShareClick},
                'pnl/invitationTasksPnl': {},
                'pnl/invitationTasksPnl/myTaskList': {},
                'pnl/invitationTasksPnl/myTaskCell': {},

                'pnl/myInvitationsPnl': {},
                'pnl/myInvitationsPnl/dataText': {},
                'pnl/myInvitationsPnl/shareTaskList': {},
                'pnl/myInvitationsPnl/shareTaskCell': {},

                'pnl/leftPnl': {},
                'pnl/leftPnl/invitationBtn': {onClicked: this.inviChoiceClick},
                'pnl/leftPnl/invitationTasksBtn': {onClicked: this.inviChoiceClick},
                'pnl/leftPnl/myInvitationsBtn': {onClicked: this.inviChoiceClick},

            }
        },


        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
            this.initData()
            this.initView()
            appInstance.nativeApi().getInvitationCode(inviteUrl)
        },
        onExit: function () {
            this._super()

        },
        onClose: function () {
            appInstance.uiManager().removeUI(this)
        },

        initData: function () {


            this.myTaskList.setVisible(false)
            this.myTaskCell.setVisible(false)

            this.invitNowPnl.setVisible(false)
            this.sfpPnl.setVisible(false)

            this.invitationTasksPnl.setVisible(false)
            this.myInvitationsPnl.setVisible(false)

            this.shareTaskCell.setVisible(false)
            this.screenLayout.setVisible(false)

            this.shareTaskList.addEventListener(this.selectedItemEvent, this)
            let myPid = appInstance.dataManager().getUserData().pid;
            inviteUrl = 'https://share.hehefun.cn/index.html?installPid=' + myPid;
        },

        onWxShareClick: function (sender) {
            GameUtil.delayBtn(sender);
            let fileName = "result_share.jpg";
            appInstance.nativeApi().shareArticle('WEIXIN', "老铁，三缺一，就差你了", "邀请你来打麻将，还有话费和实物等你来赢哦", inviteUrl, fileName)
        },

        onWxCircleShareClick: function (sender) {
            GameUtil.delayBtn(sender);
            let fileName = "result_share.jpg";
            appInstance.nativeApi().shareImage('WEIXIN_CIRCLE', fileName)
        },
        onInviteCodeCallback: function (msg) {
            this.loadCodePg(this.qrCodePg1, msg)
            this.loadCodePg(this.qrCodePg2, msg)
            this.loadCodePg(this.qrCodePg3, msg)
            // imgUrl = msg;//sd卡二维码路径
            // this.loadUrlImage(msg, this.invitNowPnl.getChildByName('qrCodePg'));
            // this.loadUrlImage(msg, this.sfpPnl.getChildByName('qrCodePg'));
        },

        loadCodePg: function (parent, img) {
            let size = parent.getContentSize()
            let sp = new cc.Sprite(img);
            sp.setContentSize(size)
            sp.setPosition(cc.p(size.width / 2, size.height / 2))
            parent.addChild(sp);
        },

        loadUrlImage: function (url, cell) {
            let size = cell.getContentSize();
            cc.loader.loadImg(url, null, function (err, img) {
                var logo = new cc.Sprite(img);
                logo.setContentSize(size)
                logo.setPosition(cc.p(size.width / 2, size.height / 2))
                cell.addChild(logo);
            });
        },

        onInviteNowClick: function (sender) {
            GameUtil.delayBtn(sender);
            this._returnType = 2
            this.leftPnl.setVisible(false)
            this.myTaskList.setVisible(false)
            this.myTaskCell.setVisible(false)
            this.invitNowPnl.setVisible(false)
            this.invitationTasksPnl.setVisible(false)
            this.myInvitationsPnl.setVisible(false)
            this.dataText.setVisible(false)


            this.beforShareImg()
            // 调用前  将自己想要分享的图片 弄全屏 在截取完后 再重置回原来的状态
            appInstance.gameAgent().saveCanvas()
            this.shareImg()
            this.afterShareImg()
            //调用完后 重置状态
        },
        // 分享接口
        shareImg: function () {

        },
        //分享前处理界面
        beforShareImg: function () {
            this.screenLayout.setVisible(true)

        },
        //屏幕截取后  重置界面
        afterShareImg: function () {
            this.screenLayout.setVisible(false)
            this.sfpPnl.setVisible(true)
        },


        /**
         * 初始化样式
         */
        initView: function () {
            this.myTaskList.setScrollBarEnabled(false)
            this.shareTaskList.setScrollBarEnabled(false)
            this.updateInviChoiceBtn()
        },

        returnClike: function (sender) {
            GameUtil.delayBtn(sender);
            if (this._returnType == 1) {
                appInstance.sendNotification(GameEvent.HALL_RED_GET)
                appInstance.uiManager().removeUI(this)
            } else {
                this._returnType = 1
                this.leftPnl.setVisible(true)
                this.updateInviChoiceBtn()
            }


        },

        selectedItemEvent: function (sender, type) {
            if (type !== ccui.ListView.EVENT_SELECTED_ITEM) {
                return
            }
            let curIndex = sender.getCurSelectedIndex()//当前手指点击的行，坐标以0开始
            let offIndex = 6 //一页展示的子节点cell
            let childLen = sender.getItems().length
            if (curIndex > (childLen - offIndex)) {
                this.refreshMyInvitationsData()
            }
        },


        inviChoiceClick: function (sender) {
            GameUtil.delayBtn(sender);
            if (sender === this.invitationBtn)
                this._inviChoiceType = 1
            else if (sender === this.invitationTasksBtn)
                this._inviChoiceType = 2
            else if (sender === this.myInvitationsBtn)
                this._inviChoiceType = 3

            this.updateInviChoiceBtn()
        },

        updateInviChoiceBtn: function () {

            let selectEdBtn

            let btnArray = [
                this.invitationBtn,
                this.invitationTasksBtn,
                this.myInvitationsBtn
            ]

            if (this._inviChoiceType == 3)
                selectEdBtn = this.myInvitationsBtn
            else if (this._inviChoiceType == 2)
                selectEdBtn = this.invitationTasksBtn
            else
                selectEdBtn = this.invitationBtn

            for (let key in btnArray) {
                let _btn = btnArray[key]
                if (_btn === selectEdBtn) {
                    _btn.setTouchEnabled(false)
                    _btn.setBright(false)
                } else {
                    _btn.setTouchEnabled(true)
                    _btn.setBright(true)
                }

            }

            this.updateDataView()

        },


        updateDataView: function () {


            if (this._inviChoiceType == 3)
                this.showMyInvitationsData()
            else if (this._inviChoiceType == 2)
                this.showInvitationTasksData()
            else
                this.showInvitationsData()

        },


        showInvitationTasksData: function () {

            this.myTaskList.setVisible(true)
            this.myTaskCell.setVisible(false)
            this.invitNowPnl.setVisible(false)
            this.sfpPnl.setVisible(false)
            this.invitationTasksPnl.setVisible(true)
            this.myInvitationsPnl.setVisible(false)
            this.dataText.setVisible(false)


        },

        showInvitationsData: function () {

            this.invitNowPnl.setVisible(true)
            this.myTaskList.setVisible(false)
            this.myTaskCell.setVisible(false)
            this.sfpPnl.setVisible(false)
            this.invitationTasksPnl.setVisible(false)
            this.myInvitationsPnl.setVisible(false)
            // appInstance.nativeApi().getInvitationCode(inviteUrl)
        },

        showMyInvitationsData: function () {

            this.invitNowPnl.setVisible(false)
            this.myTaskList.setVisible(false)
            this.myTaskCell.setVisible(false)
            this.sfpPnl.setVisible(false)
            this.invitationTasksPnl.setVisible(false)
            this.myInvitationsPnl.setVisible(true)
            if (this.shareTaskList.getChildrenCount() <= 0)
                this.dataText.setVisible(true)


            /*if(this._agentFlag == 0){
                this.dataText.setVisible(true)
                return
            }


            this.refreshMyInvitationsParam(0)*/

        },

        refreshMyInvitationsData: function () {

            if (!this._isCanRefreshMyInvitationsData)
                return

            let msg = {
                'startIndex': this._startIndex,
                'endIndex': this._endIndex
            }

            appInstance.gameAgent().httpGame().shareInviteReq(msg)

        },

        refreshMyInvitationsParam: function (startIndex) {
            this._startIndex = startIndex//邀请记录，开始行
            this._endIndex = startIndex + this._indexLength//邀请记录，结束行
        },


        updateMyInvitationsData: function (data) {
            if (data.length < this._indexLength) {
                this._isCanRefreshMyInvitationsData = false
                if (this.shareTaskList.getChildrenCount() <= 0) {
                    if (data.length > 0) {
                        this.dataText.setVisible(false)
                    } else {
                        this.dataText.setVisible(true)
                        this.shareTaskList.setVisible(false)
                        this.dataText.setString('当前无数据分享')
                        return
                    }

                }
                this.dataText.setVisible(false)
            } else {
                if (this._startIndex == 0) {

                    this.dataText.setVisible(false)
                    this.shareTaskList.setVisible(true)
                }

                this.refreshMyInvitationsParam(this._endIndex)
            }

            for (let i = 0; i < data.length; i++) {

                let log = data[i]
                let cell = this.shareTaskCell.clone()
                cell.setVisible(true)
                this.shareTaskList.pushBackCustomItem(cell)

                cell.getChildByName('numberText').setString(log.seq)
                cell.getChildByName('timeDaysText').setString(log.timeDays)
                cell.getChildByName('timeHoursText').setString(log.timeHours)
                cell.getChildByName('idText').setString(log.invitePid)
                cell.getChildByName('nicknameText').setString(log.pName)

            }

        },

        onUpMyInviteData: function (data) {

            for (let i = 0; i < data.length; i++) {
                this.initTaskCell(data[i])
            }

        },

        initTaskCell: function (dailyTask) {


            let cell = this.myTaskCell.clone()
            cell._data = {
                taskId: dailyTask.taskId,
                status: dailyTask.status
            }

            cell.setName(dailyTask.taskId)
            cell.getChildByName('myInviteNameText').setString(dailyTask.taskName)
            cell.getChildByName('moneyText').setString('x' + dailyTask.propNum)
            cell.getChildByName('moneyByPic').getChildByName('moneyPic').loadTexture(dailyTask.propRes)
            cell.getChildByName('progressBarPnl').getChildByName('progressBar').setPercent(Math.round(dailyTask.reachNum / dailyTask.taskNum * 100))
            cell.getChildByName('progressBarPnl').getChildByName('progressBarText').setString(dailyTask.reachNum + '/' + dailyTask.taskNum)


            switch (dailyTask.status) {
                case 0:
                    cell.getChildByName('myInviteUnfinishedBtn').setVisible(true)
                    cell.getChildByName('myInviteProcessingBtn').setVisible(false)
                    cell.getChildByName('myInviteFinishedBtn').setVisible(false)

                    cell.getChildByName('myInviteUnfinishedBtn').addClickEventListener(function (sender, et) {
                        GameUtil.delayBtn(sender);
                        this.onTaskCellClick(sender)
                    }.bind(this))
                    break
                case 1:
                    cell.getChildByName('myInviteUnfinishedBtn').setVisible(false)
                    cell.getChildByName('myInviteProcessingBtn').setVisible(true)
                    cell.getChildByName('myInviteFinishedBtn').setVisible(false)

                    cell.getChildByName('myInviteProcessingBtn').addClickEventListener(function (sender, et) {
                        GameUtil.delayBtn(sender);
                        this.onTaskCellClick(sender)
                    }.bind(this))
                    break
                case 2:
                    cell.getChildByName('myInviteUnfinishedBtn').setVisible(false)
                    cell.getChildByName('myInviteProcessingBtn').setVisible(false)
                    cell.getChildByName('myInviteFinishedBtn').setVisible(true)
                    break
                default:
                    cell.getChildByName('myInviteUnfinishedBtn').setVisible(true)
                    cell.getChildByName('myInviteProcessingBtn').setVisible(false)
                    cell.getChildByName('myInviteFinishedBtn').setVisible(false)

                    cell.getChildByName('myInviteUnfinishedBtn').addClickEventListener(function (sender, et) {
                        GameUtil.delayBtn(sender);
                        this.onTaskCellClick(sender)
                    }.bind(this))
                    break
            }


            cell.setVisible(true)
            this.myTaskList.pushBackCustomItem(cell)

        },

        onTaskCellClick: function (sender) {
            let data = sender.getParent()._data

            let status = data.status
            switch (status) {
                case 0:
                    this._inviChoiceType = 1
                    this.updateInviChoiceBtn()
                    break
                case 1:
                    //调用http方法，领取货币
                    let msg = {
                        taskId: data.taskId
                    }
                    appInstance.gameAgent().httpGame().receiveMyInviteReq(msg)

                    break
                default:
                    break

            }

        },


        updateTaskByTaskId: function (data) {

            this.myTaskList.getChildByName(data.taskId).getChildByName('myInviteProcessingBtn').setVisible(false)
            this.myTaskList.getChildByName(data.taskId).getChildByName('myInviteFinishedBtn').setVisible(false)
            this.myTaskList.getChildByName(data.taskId).getChildByName('myInviteFinishedBtn').setVisible(true)
            this.myTaskList.getChildByName(data.taskId)._data.status = data.status

        },


    })
    return invitationLayer
})
