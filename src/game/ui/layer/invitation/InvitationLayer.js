
load('game/ui/layer/invitation/InvitationLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let InvitationMdt = include('game/ui/layer/invitation/InvitationMdt')
    let invitationLayer = BaseLayer.extend({
        _className: 'invitationLayer',
        ctor: function () {
            this._super(ResConfig.View.InvitationLayer)
            this.registerMediator(new InvitationMdt(this))
        },
        RES_BINDING: function () {
            return {
                'pnl/returnBtn': {onClicked : this.returnClike},
                'pnl/bgPnl': {},

                'pnl/invitationPnl': {},
                'pnl/invitationPnl/invitNowPnl': {},
                'pnl/invitationPnl/inviteNowBtn': {onClicked : this.onInviteNowClick},
                'pnl/invitationPnl/sfpPnl': {},
                'pnl/invitationPnl/sfpPnl/sftWxBtn': {onClicked : this.onWxShareClick},
                'pnl/invitationPnl/sfpPnl/sftWxCircleBtn': {onClicked : this.onWxCircleShareClick},
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
        },
        onExit: function () {
            this._super()

        },
        onClose: function () {
            appInstance.uiManager().removeUI(this)
        },

        initData: function () {
            this._agentFlag = appInstance.dataManager().getUserData().agentFlag//0不是代理 1是1级代理 2是2级代理
            this._returnType = 1//1是关闭当前界面，2是返回邀请好友界面
            this._inviChoiceType = 1//1代表邀请好友，2代表我的邀请，3代表分享详情
            this._startIndex = 0//邀请记录，开始行
            this._endIndex = 5//邀请记录，结束行
            this._indexLength = 5//邀请记录，记录长度
            this._isCanRefreshMyInvitationsData = true

            this.myTaskList.setVisible(false)
            this.myTaskCell.setVisible(false)

            this.invitNowPnl.setVisible(false)
            this.sfpPnl.setVisible(false)

            this.invitationTasksPnl.setVisible(false)
            this.myInvitationsPnl.setVisible(false)
            this.dataText.setVisible(false)

           // this.myTaskList.addEventListener(this.selectedItemEvent,this)
        },

        onInviteNowClick : function () {

            this.leftPnl.setVisible(false)
            this.myTaskList.setVisible(false)
            this.myTaskCell.setVisible(false)
            this.invitNowPnl.setVisible(false)
            this.invitationTasksPnl.setVisible(false)
            this.myInvitationsPnl.setVisible(false)
            this.dataText.setVisible(false)

            this.sfpPnl.setVisible(true)
        },


        /**
         * 初始化样式
         */
        initView: function () {

            this.updateInviChoiceBtn()

        },

        returnClike: function () {

            if(this._returnType == 1){
                appInstance.uiManager().removeUI(this)
            }else{
                this.leftPnl.setVisible(true)
                this.updateInviChoiceBtn()
            }


        },

        selectedItemEvent: function (sender, type) {
            if (type !== ccui.ListView.EVENT_SELECTED_ITEM) {
                return
            }
            let curIndex = sender.getCurSelectedIndex()
            cc.log('-----------------------curIndex---------------')
            let offIndex = 3 //一页展示的子节点cell
            let childLen = sender.getItems().length
            if ( curIndex > (childLen - offIndex)) {
                cc.log('=======滑动到底了===========')
            }
        },


        inviChoiceClick: function (sender) {

            if(sender === this.invitationBtn)
                this._inviChoiceType = 1
            else if(sender === this.invitationTasksBtn)
                this._inviChoiceType = 2
            else if(sender === this.myInvitationsBtn)
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

            if(this._inviChoiceType == 3)
                selectEdBtn = this.myInvitationsBtn
            else if(this._inviChoiceType == 2)
                selectEdBtn = this.invitationTasksBtn
            else
                selectEdBtn = this.invitationBtn

            for(let key in btnArray){
                if(key === selectEdBtn){
                    key.setVisible(true)
                    key.setTouchEnabled(true)
                    key.setBright(true)
                }else{
                    key.setVisible(false)
                    key.setTouchEnabled(false)
                    key.setBright(false)
                }

            }

            this.updateDataView()

        },

        

        updateDataView: function () {


            if(this._inviChoiceType == 3)
                this.showMyInvitationsData()
            else if(this._inviChoiceType == 2)
                this.showInvitationTasksData()
            else
                this.showInvitationsData()

        },


        showInvitationTasksData: function () {

            appInstance.gameAgent().httpGame().myInviteReq()

        },

        showInvitationsData: function () {

            this.invitNowPnl.setVisible(true)
            this.myTaskList.setVisible(false)
            this.myTaskCell.setVisible(false)
            this.sfpPnl.setVisible(false)
            this.invitationTasksPnl.setVisible(false)
            this.myInvitationsPnl.setVisible(false)

        },

        showMyInvitationsData: function () {

            this.invitNowPnl.setVisible(false)
            this.myTaskList.setVisible(false)
            this.myTaskCell.setVisible(false)
            this.sfpPnl.setVisible(false)
            this.invitationTasksPnl.setVisible(false)
            this.myInvitationsPnl.setVisible(true)

            if(this._agentFlag == 0){
                this.dataText.setVisible(true)
                return
            }


            this.refreshMyInvitationsParam(0)

        },

        refreshMyInvitationsData: function () {

            if(!this._isCanRefreshMyInvitationsData)
                return

            let msg = {
                'startIndex' : this._startIndex,
                'endIndex' : this._endIndex
            }

            appInstance.gameAgent().httpGame().shareInviteReq(msg)

        },

        refreshMyInvitationsParam: function (startIndex) {
            this._startIndex = startIndex//邀请记录，开始行
            this._endIndex = startIndex + this._indexLength//邀请记录，结束行
        },


        updateMyInvitationsData: function (data) {

            if(data.length < this._indexLength){
                this._isCanRefreshMyInvitationsData = false
                if(this.myTaskList.getChildrenCount() <= 0){

                    this.myTaskList.setVisible(false)
                    this.dataText.setVisible(true)
                    this.dataText.setString('当前无数据分享')
                    return

                }
            }else{
                if( this._startIndex == 0){
                    this.myTaskList.removeAllChildren()
                    this.myTaskList.setVisible(true)
                }

                this.refreshMyInvitationsParam(this._endIndex)
            }


            for(let i = 0; i < data.length; i++){

                let log = data[i]
                let cell = this.shareTaskCell.clone()
                cell.setVisible(true)
                this.myTaskList.pushBackCustomItem(cell)

                cell.getChildByName('numberText').setString(log.seq)
                cell.getChildByName('timeDaysText').setString(log.timeDays)
                cell.getChildByName('timeHoursText').setString(log.timeHours)
                cell.getChildByName('idText').setString(log.invitePid)
                cell.getChildByName('nicknameText').setString(log.pName)

            }

        },

        onUpMyInviteData: function (data) {

            for(let i = 0; i < data.length; i++){
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
            cell.getChildByName('acceptedBg').getChildByName('moneyPic').loadTexture(dailyTask.propRes)
            cell.getChildByName('progressBarPnl').getChildByName('progressBar').setPercent(Math.round(dailyTask.reachNum/dailyTask.taskNum*100))
            cell.getChildByName('progressBarPnl').getChildByName('progressBarText').setString(dailyTask.reachNum + '/' + dailyTask.taskNum)


            switch (dailyTask.status) {
                case 0:
                    cell.getChildByName('myInviteUnfinishedBtn').setVisible(true)
                    cell.getChildByName('myInviteProcessingBtn').setVisible(false)
                    cell.getChildByName('myInviteFinishedBtn').setVisible(false)

                    cell.getChildByName('myInviteUnfinishedBtn').addClickEventListener(function(sender, et) {
                        this.onTaskCellClick(sender)
                    }.bind(this))
                    break
                case 1:
                    cell.getChildByName('myInviteUnfinishedBtn').setVisible(false)
                    cell.getChildByName('myInviteProcessingBtn').setVisible(true)
                    cell.getChildByName('myInviteFinishedBtn').setVisible(false)

                    cell.getChildByName('myInviteProcessingBtn').addClickEventListener(function(sender, et) {
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

                    cell.getChildByName('myInviteUnfinishedBtn').addClickEventListener(function(sender, et) {
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
                        taskId : data.taskId
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
            this.myTaskList.getChildByName(data.taskId)._data.status = data.status

        },



    })
    return invitationLayer
})
