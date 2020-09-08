
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
            this.initView()
        },
        onExit: function () {
            this._super()

        },
        onClose: function () {
            appInstance.uiManager().removeUI(this)
        },

        initData: function () {
            this._agentFlag = appInstance.dataManager().getUserData().agentFlag
            this._returnType = 1
            this._inviChoiceType = 1//1代表邀请好友，2代表我的邀请，3代表分享详情
        },

        /**
         * 初始化样式
         */
        initView: function () {

            this.myTaskList.addEventListener(this.selectedItemEvent,this)

            this.inviChoiceClick()

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
                this.updateMyInvitationsData()
            else if(this._inviChoiceType == 2)
                this.updateInvitationTasksData()
            else
                this.updateInvitationsData()

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



    })
    return invitationLayer
})
