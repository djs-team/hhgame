
/**
 *  TaskMdt Mediator
 *
 */
load('game/ui/layer/invitation/InvitationMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let mdt = Mediator.extend({
        mediatorName: 'InvitationMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.MY_INVITE,
                GameEvent.MY_INVITE_RECEIVE,
                GameEvent.SHARE_INVITE,
            ]
        },
        _page: 7,
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.MY_INVITE:
                    this.myInviteData(body)
                    break
                case GameEvent.MY_INVITE_RECEIVE:
                    this.receiveMyInvite(body)
                    break
                case GameEvent.SHARE_INVITE:
                    this.shareInviteData(body)
                    break
                default:
                    break
            }
        },
        onRegister: function () {
            this.initView()
        },

        onRemove: function () {

        },

        /**
         * 初始化我的邀请喝分享详情的数据
         */
        initView: function () {
            let msg = {}
            appInstance.gameAgent().httpGame().myInviteReq(msg)
            // if (this.view._agentFlag == 0) {
                appInstance.gameAgent().httpGame().shareInviteReq(msg)
            // }
        },

        /**
         * 领取后的数据处理
         * @param msg
         */
        receiveMyInvite: function (msg) {
            let data = {}
            data.taskId = msg.taskId
            data.rewards = {}
            data.status = 2
            data.propNum = msg.propNum
            switch (msg.propCode) {
                case 1:
                    data.propRes = 'res/code/task/jinbi.png'
                    break
                case 2:
                    data.propRes = 'res/code/task/zuanshi.png'
                    break
                case 3:
                    data.propRes = 'res/code/task/gy_fk.png'
                    break
                default:
                    data.propRes = 'res/code/task/jinbi.png'
                    break
            }
            this.view.onReceiveMyInviteReward(data)
        },

        /**
         * 我的邀请数据处理
         * @param msg
         */
        myInviteData: function (msg) {
            let data = {}
            for (let i = 0; i < msg.taskList.length; i++) {
                let myInviteTask = msg.taskList[i]
                let myInviteTaskData = {}
                myInviteTaskData.taskId = myInviteTask.taskId       //任务id
                myInviteTaskData.taskName = myInviteTask.taskName   //任务名称
                myInviteTaskData.taskNum = myInviteTask.taskNum     //条件数量
                myInviteTaskData.propCode = myInviteTask.propCode   //道具编码：1金币、2钻石、3福卡
                myInviteTaskData.propNum = myInviteTask.propNum     //奖励道具数量
                myInviteTaskData.reachNum = myInviteTask.reachNum   //已经达到条件数
                myInviteTaskData.status = myInviteTask.status       //状态：0未完成、1未领取、2已领取

                //道具编码信息
                switch (myInviteTask.propCode) {
                    case 1:
                        myInviteTaskData.propRes = 'res/code/task/jinbi.png'
                        break
                    case 2:
                        myInviteTaskData.propRes = 'res/code/task/zuanshi.png'
                        break
                    case 3:
                        myInviteTaskData.propRes = 'res/code/task/gy_fk.png'
                        break
                    default:
                        myInviteTaskData.propRes = 'res/code/task/jinbi.png'
                        break
                }

                //状态信息
                switch (myInviteTask.status) {
                    case 0:
                        myInviteTaskData.statusRes = 'res/code/task/dqxz_3.png'
                        myInviteTaskData.statusText = '去完成'
                        break
                    case 1:
                        myInviteTaskData.statusRes = 'res/code/task/yqyl_7.png'
                        myInviteTaskData.statusText = '未领取'
                        break
                    case 2:
                        myInviteTaskData.statusRes = 'res/code/task/yj_3.png'
                        break
                    default:
                        myInviteTaskData.statusRes = 'res/code/task/dqxz_3.png'
                        myInviteTaskData.statusText = '去完成'
                        break
                }
                data[i] = myInviteTaskData
            }
            this.view.onUpMyInviteData(data)
        },

        /**
         * 分享详情数据处理
         * @param msg
         */
        shareInviteData: function (msg) {
            let inviteData = msg.inviteLogList
            let data = {}
            if (inviteData.length>0) {
                for (let i = 0; i < inviteData.length; i++) {
                    let info = {
                        'id': i+1,
                        'invitePid': inviteData[i].invitePid,
                        'pName': inviteData[i].pName,
                        'createTime': inviteData[i].createTime
                    }
                    data[i] = info
                }
            }
            this.view.onUpShareInviteData(data)
        },


    })
    return mdt
})