
/**
 *  TaskMdt Mediator
 *
 */
load('game/ui/layer/invitation/InvitationMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
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
        _seq : 1,
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
            this._agentFlag = appInstance.dataManager().getUserData().agentFlag//0不是代理 1是1级代理 2是2级代理
            appInstance.gameAgent().httpGame().myInviteReq()
            if(this._agentFlag == 0){
                let msg = {
                    'startIndex' : 0,
                    'endIndex' : 6
                }

                appInstance.gameAgent().httpGame().shareInviteReq(msg)
            }


        },

        /**
         * 领取后的数据处理
         * @param msg
         */
        receiveMyInvite: function (msg) {


            let propData = {}
            msg.propType = 1
            GameUtil.getPropData(msg,propData,GameUtil.CURRENCYTYPE_1,GameUtil.UNITLOCATION_BEFORE,'x')

            let propList = []
            propList.push(propData)
            appInstance.gameAgent().addReceivePropsUI(propList)

            let data = {}
            data.taskId = msg.taskId
            data.status = 2
            this.view.updateTaskByTaskId(data)


        },

        /**
         * 我的邀请数据处理
         * @param msg
         */
        myInviteData: function (msg) {
            let data = []
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

                data.push(myInviteTaskData)
            }
            this.view.onUpMyInviteData(data)
        },

        /**
         * 分享详情数据处理
         * @param msg
         */
        shareInviteData: function (msg) {

            let data = []

            for(let i = 0; i < msg.inviteLogList.length; i++){
                let log = msg.inviteLogList[i]
                let logData = {
                    seq : this._seq,
                    invitePid : log.invitePid,
                    pName : log.pName,
                }

                this._seq++
                this.onFormatTimes(log.createTime,logData)
                data.push(logData)
            }

            this.view.updateMyInvitationsData(data)
        },

        onFormatTimes: function (timestamp,data) {

            let date = new Date(timestamp)
            data.timeDays = (date.getFullYear()) + "-" + (this.padLeftZero(date.getMonth() + 1)) + "-" + (this.padLeftZero(date.getDate()))
            data.timeHours = (this.padLeftZero(date.getHours())) + "-" + (this.padLeftZero(date.getMinutes()))

        },

        padLeftZero: function (str) {

            return ('00' + str).substr(str.toString().length);
        }


    })
    return mdt
})