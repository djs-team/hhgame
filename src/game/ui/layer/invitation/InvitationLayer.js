
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
                'pnl/returnBtn': {},
                'pnl/bgPnl': {},

                'pnl/invitationPnl/invitNowPnl': {},
                'pnl/invitationPnl/inviteNowBtn': {onClicked : this.onInviteNowClick},

                'pnl/invitationPnl/sfpPnl': {},
                'pnl/invitationPnl/sfpPnl/sftWxBtn': {onClicked : this.onWxShareClick},
                'pnl/invitationPnl/sfpPnl/sftWxCircleBtn': {onClicked : this.onWxCircleShareClick},

                'pnl/invitationTasksPnl/myTaskList': {},
                'pnl/invitationTasksPnl/myTaskCell': {},

                'pnl/myInvitationsPnl': {},
                'pnl/myInvitationsPnl/shareTaskTitle': {},
                'pnl/myInvitationsPnl/shareTaskList': {},
                'pnl/myInvitationsPnl/shareTaskCell': {},

                'pnl/leftPnl/invitationBtn': {},
                'pnl/leftPnl/invitationTasksBtn': {},
                'pnl/leftPnl/myInvitationsBtn': {},


            }
        },
        _agentFlag: 0,
        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
            this.initView()
        },
        onExit: function () {
            this._super()

        },
        onClose: function () {
            appInstance.uiManager().removeUI(this)
        },

        /**
         * 初始化样式
         */
        initView: function () {
            this.onDefaultView()

            this.myTaskList.addEventListener(this.selectedItemEvent,this)

        },

        selectedItemEvent: function (sender, type) {
            if (type !== ccui.ListView.EVENT_SELECTED_ITEM) {
                return
            }
            let curIndex = sender.getCurSelectedIndex()
            let offIndex = 3 //一页展示的子节点cell
            let childLen = sender.getItems().length
            if ( curIndex > (childLen - offIndex)) {
                cc.log('=======滑动到底了===========')
            }
        },

        listenerText1: function (sender,dt) {

            cc.log('-------------1111111-------------sender : ' + JSON.stringify(sender) + ' dt : ' + dt)

            cc.log('' )
            cc.log('' )
            cc.log('' )
            cc.log('' )
            cc.log('' )


        },

        listenerText: function (sender,dt) {

            cc.log('--------------------------sender : ' + JSON.stringify(sender) + ' dt : ' + dt)

        },
        /**
         * 默认样式
         */
        onDefaultView: function() {
            this._agentFlag = appInstance.dataManager().getUserData().agentFlag;
            this.myTaskCell.setVisible(false)
            this.hintReturnBtn.setVisible(false)
            this.inviteFriendsPnl.setVisible(true)
            this.myInvitePnl.setVisible(false)
            this.shareInvitePnl.setVisible(false)
            this.hintPnl.setVisible(false)
            this.leftInviteFriendsPnl.getChildByName('common').setVisible(false)
            this.leftInviteFriendsPnl.getChildByName('leftBtn').setVisible(true)
            this.leftMyInvitePnl.getChildByName('common').setVisible(true)
            this.leftMyInvitePnl.getChildByName('leftBtn').setVisible(false)
            this.leftShareInvitePnl.getChildByName('common').setVisible(true)
            this.leftShareInvitePnl.getChildByName('leftBtn').setVisible(false)
            this.returnBtn.setVisible(true)
            this.agentDataText.setVisible(false)
            this.rewardPnl.setVisible(false)
        },

        /**
         * 邀请好友
         */
        onInviteFriendsClick: function () {
            this.onDefaultView()
            let msg = {}
            appInstance.gameAgent().httpGame().myInviteReq(msg)
        },

        /**
         * 我的邀请
         */
        onMyInviteClick: function () {
            this.inviteFriendsPnl.setVisible(false)
            this.myInvitePnl.setVisible(true)
            this.shareInvitePnl.setVisible(false)
            this.hintPnl.setVisible(false)
            this.leftInviteFriendsPnl.getChildByName('common').setVisible(true)
            this.leftInviteFriendsPnl.getChildByName('leftBtn').setVisible(false)
            this.leftMyInvitePnl.getChildByName('common').setVisible(false)
            this.leftMyInvitePnl.getChildByName('leftBtn').setVisible(true)
            this.leftShareInvitePnl.getChildByName('common').setVisible(true)
            this.leftShareInvitePnl.getChildByName('leftBtn').setVisible(false)
            this.returnBtn.setVisible(true)

        },

        /**
         * 分享详情
         */
        onShareInviteClick: function () {
            this.inviteFriendsPnl.setVisible(false)
            this.myInvitePnl.setVisible(false)
            this.shareInvitePnl.setVisible(true)
            this.hintPnl.setVisible(false)
            this.leftInviteFriendsPnl.getChildByName('common').setVisible(true)
            this.leftInviteFriendsPnl.getChildByName('leftBtn').setVisible(false)
            this.leftMyInvitePnl.getChildByName('common').setVisible(true)
            this.leftMyInvitePnl.getChildByName('leftBtn').setVisible(false)
            this.leftShareInvitePnl.getChildByName('common').setVisible(false)
            this.leftShareInvitePnl.getChildByName('leftBtn').setVisible(true)
            this.returnBtn.setVisible(true)
            //代理样式
            if (this._agentFlag !== 0) {
                this.noDataText.setVisible(false)
                this.agentDataText.setVisible(true)
                this.shareTaskList.setVisible(false)
                this.shareTaskCell.setVisible(false)
            }
        },

        /**
         * 立即邀请
         */
        onInviteNowClick: function () {

        },

        /**
         * 关闭立即邀请
         */
        onHintClick: function () {
            this.returnBtn.setVisible(true)
            this.hintReturnBtn.setVisible(false)
            this.onDefaultView()
        },

        /**
         * 同步更新我的邀请
         * @param data
         */
        onUpMyInviteData: function(data) {
            for (let i = 0; i < Object.keys(data).length; i++) {
                this.initMyInvite(data[i])
            }
        },

        /**
         * 我的邀请渲染
         * @param myInviteData
         */
        initMyInvite: function (myInviteData) {

            let cell = this.myTaskCell.clone()
            cell._data = {
                taskId: myInviteData.taskId,
                status: myInviteData.status,
            }
            cell.setName('myInviteTask'+myInviteData.taskId)
            cell.getChildByName('myInviteNameText').setString(myInviteData.taskName)
            cell.getChildByName('moneyText').setString('x' + myInviteData.propNum)
            cell.getChildByName('progressBarPnl').getChildByName('progressBar').setPercent(Math.round(myInviteData.reachNum/myInviteData.taskNum*100))
            cell.getChildByName('progressBarPnl').getChildByName('progressBarText').setString(myInviteData.reachNum + '/' + myInviteData.taskNum)
            cell.getChildByName('moneyByPic').getChildByName('moneyPic').loadTexture(myInviteData.propRes)
            switch (myInviteData.status) {
                case 0:
                    cell.getChildByName('myInviteFinishedBtn').setVisible(false)
                    cell.getChildByName('myInviteUnfinishedBtn').setVisible(true)
                    cell.getChildByName('myInviteProcessingBtn').setVisible(false)

                    cell.getChildByName('myInviteUnfinishedBtn').addClickEventListener(function(sender, et) {
                        this.onMyInviteCellClick(sender)
                    }.bind(this))
                    break
                case 1:
                    cell.getChildByName('myInviteFinishedBtn').setVisible(false)
                    cell.getChildByName('myInviteUnfinishedBtn').setVisible(false)
                    cell.getChildByName('myInviteProcessingBtn').setVisible(true)
                    cell.getChildByName('myInviteProcessingBtn').addClickEventListener(function(sender, et) {
                        this.onMyInviteCellClick(sender)
                    }.bind(this))
                    break
                case 2:
                    cell.getChildByName('myInviteFinishedBtn').setVisible(true)
                    cell.getChildByName('myInviteUnfinishedBtn').setVisible(false)
                    cell.getChildByName('myInviteProcessingBtn').setVisible(false)
                    break
                default:
                    cell.getChildByName('myInviteFinishedBtn').setVisible(false)
                    cell.getChildByName('myInviteUnfinishedBtn').setVisible(true)
                    cell.getChildByName('myInviteProcessingBtn').setVisible(false)
                    cell.getChildByName('myInviteUnfinishedBtn').addClickEventListener(function(sender, et) {
                        this.onMyInviteCellClick(sender)
                    }.bind(this))
                    break
            }
            cell.setVisible(true)
            this.myTaskList.pushBackCustomItem(cell)
        },

        /**
         * 我的邀请-任务按钮
         * @param sender
         */
        onMyInviteCellClick: function (sender) {
            let data = sender.getParent()._data
            let status = data.status
            switch (status) {
                case 0:
                    this.onJumpOtherLayer()
                    break
                case 1:
                    //调用http方法，领取货币
                    this.onReceiveMyInvite(data.taskId)
                    break
                default:
                    break
            }
        },

        /**
         * 去完成
         */
        onJumpOtherLayer: function () {
            this.onDefaultView();
        },

        /**
         * 领取
         * @param taskId
         */
        onReceiveMyInvite: function (taskId) {
            let msg = {}
            msg.taskId = taskId
            appInstance.gameAgent().httpGame().receiveMyInviteReq(msg)
        },

        /**
         * 领取任务-更新样式
         * @param data
         */
        onReceiveMyInviteReward: function (data) {
            let cell = this.myTaskList.getChildByName('myInviteTask' + data.taskId)
            cell.getChildByName('myInviteFinishedBtn').setVisible(true)
            cell.getChildByName('myInviteUnfinishedBtn').setVisible(false)
            cell.getChildByName('myInviteProcessingBtn').setVisible(false)
            cell.getChildByName('progressBarPnl').getChildByName('progressBar').setPercent(100)
            this.receiveRewards(data)
        },

        /**
         * 领取任务-开启奖励提示
         * @param data
         */
        receiveRewards: function (data) {
            let _pnl = this.rewardPnl
            _pnl.getChildByName('acceptedBg').getChildByName('acceptedTypePg').loadTexture(data.propRes)
            _pnl.getChildByName('acceptedBg').getChildByName('awardsVal').setString('x' + data.propNum)
            _pnl.setVisible(true)
        },

        /**
         * 关闭奖励提示
         */
        onPopUpPnlClick: function () {
            this.rewardPnl.setVisible(false)
        },

        /**
         * 分享详情-请求数据渲染
         * @param data
         */
        onUpShareInviteData: function (data) {
            let length = Object.keys(data).length;
            if (length>0) {
                this.noDataText.setVisible(false)
                this.agentDataText.setVisible(false)
                for (let i = 0; i < length; i++) {
                    this.initShareInvite(data[i])
                }
            } else {
                this.noDataText.setVisible(true)
                this.agentDataText.setVisible(false)
                this.shareTaskList.setVisible(false)
                this.shareTaskCell.setVisible(false)
            }
        },

        /**
         * 渲染分享详情
         * @param data
         */
        initShareInvite: function (data) {
            let cell = this.shareTaskCell.clone();
            let pic = 'res/code/invite/yqyl_5.png';
            cell.getChildByName('numberPic').loadTexture(pic)
            cell.getChildByName('numberPic').getChildByName('numberText').setString(data.id)
            cell.getChildByName('idPic').loadTexture(pic)
            cell.getChildByName('idPic').getChildByName('idText').setString(data.invitePid)
            cell.getChildByName('nicknamePic').loadTexture(pic)
            cell.getChildByName('nicknamePic').getChildByName('nicknameText').setString(data.pName)
            cell.getChildByName('timePic').loadTexture(pic)
            cell.getChildByName('timePic').getChildByName('timeText').setString(this.onFormatDateTime(data.createTime))
            cell.setVisible(true)
            this.shareTaskList.addChild(cell)
        },

        /**
         * 毫秒换成日期
         * @param timestamp
         * @returns {string}
         */
        onFormatDateTime: function (timestamp) {
            let d=new Date(parseInt(timestamp));
            let month=(d.getMonth()+1)<10?(0+""+(d.getMonth()+1)):(d.getMonth()+1);
            let day=d.getDate()<10?(0+""+d.getDate()):d.getDate();
            let hour=d.getHours()<10?(0+""+d.getHours()):d.getHours();
            let minute=d.getMinutes()<10?(0+""+d.getMinutes()):d.getMinutes();
            let dateString=d.getFullYear()+ "-" + month +"-"+day+" "+hour+": "+minute
            return dateString;
        },
    })
    return invitationLayer
})
