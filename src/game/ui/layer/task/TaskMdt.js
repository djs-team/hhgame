
/**
 *  TaskMdt Mediator
 *
 */
load('game/ui/layer/task/TaskMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let mdt = Mediator.extend({
        mediatorName: 'TaskMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.TASK_DAILY,
                GameEvent.TASK_DAILY_RECEIVEREWARDS,
                GameEvent.TASK_CHALLENGE,
                GameEvent.TASK_CHALLENGE_RECEIVE,
                GameEvent.TASK_CHALLENGE_REFRESH,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.TASK_DAILY:

                    this.initDailyData(body)
                    break
                case GameEvent.TASK_DAILY_RECEIVEREWARDS:
                    this.receiveDailyRewards(body)
                    break
                case GameEvent.TASK_CHALLENGE:
                    this.initChallengeTasks(body)
                    break
                case GameEvent.TASK_CHALLENGE_RECEIVE:
                    this.onReceiveChallegeReward(body)
                    break
                case GameEvent.TASK_CHALLENGE_REFRESH:
                    this.refreshChallegeTasks(body)
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

        initView: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().TASKDAILYReq(msg)
            appInstance.gameAgent().httpGame().TASKCHALLENGEReq(msg)

        },

        receiveDailyRewards: function (msg) {


            let data = {}
            data.totalActivity = msg.totalActivity
            data.maxActivity = msg.maxActivity
            data.taskId = msg.taskId
            data.boxList = {}

            for(let i = 0; i < msg.boxList.length; i++){

                let boxCell = {}
                boxCell.activityId = msg.boxList[i].activityId
                boxCell.status = msg.boxList[i].status

                switch (msg.boxList[i].status) {
                    case 1:
                        boxCell.res = 'res/code/task/rwzx_7.png'
                        boxCell.activityText = '未领取'
                        boxCell.status = 1
                        break
                    case 2:
                        boxCell.res = 'res/code/task/rwzx_15.png'
                        boxCell.activityText = '已领取'
                        boxCell.status = 2
                        break
                    default:
                        break

                }
                data.boxList[i] = boxCell
            }

            this.view.receiveDailyRewards(data)
            this.onShowPropsUI(msg.rpList)
        },


        initDailyData: function (msg) {

            let data = {}
            data.activityRewards = {}
            data.taskList = {}
            data.allActivityProcess = 0
            data.totalActivity = msg.totalActivity
            for(let i = 0; i < msg.activityReward.length; i++){

                let msgActivityConfig = msg.activityReward[i]
                let activityConfig = {}
                activityConfig.activityId =msgActivityConfig.activityId
                activityConfig.activity =msgActivityConfig.activity
                activityConfig.status =msgActivityConfig.status
                activityConfig.desc =msgActivityConfig.desc


                switch (activityConfig.status) {
                    case 0:
                        activityConfig.res = 'res/code/task/rwzx_9.png'
                        activityConfig.activityText = activityConfig.activity
                        break
                    case 1:
                        activityConfig.res = 'res/code/task/rwzx_7.png'
                        activityConfig.activityText = '未领取'
                        break
                    case 2:
                        activityConfig.res = 'res/code/task/rwzx_15.png'
                        activityConfig.activityText = '已领取'
                        break
                    default:
                        activityConfig.res = 'res/code/task/rwzx_9.png'
                        activityConfig.activityText = activityConfig.activity
                        break

                }

                data.activityRewards[i] = activityConfig

                if(data.allActivityProcess < msgActivityConfig.activity){
                    data.allActivityProcess = msgActivityConfig.activity
                }


            }

            if(data.totalActivity >= data.allActivityProcess){
                data.totalActivity = data.allActivityProcess
            }



            for(let i = 0; i < msg.taskList.length; i++){

                let msgPlayerTaskDaily = msg.taskList[i]
                let playerTaskDaily = {}
                playerTaskDaily.taskId =msgPlayerTaskDaily.taskId
                playerTaskDaily.taskType =msgPlayerTaskDaily.taskType
                playerTaskDaily.taskName =msgPlayerTaskDaily.taskName
                playerTaskDaily.taskNum =msgPlayerTaskDaily.taskNum
                playerTaskDaily.propNum =msgPlayerTaskDaily.propNum
                playerTaskDaily.activity =msgPlayerTaskDaily.activity
                playerTaskDaily.reachNum =msgPlayerTaskDaily.reachNum
                playerTaskDaily.status =msgPlayerTaskDaily.status

                let propCode = msgPlayerTaskDaily.propCode
                switch (propCode) {
                    case 1://货币
                        playerTaskDaily.res = 'res/code/task/jinbi.png'
                        break
                    case 2:
                        playerTaskDaily.res = 'res/code/task/zuanshi.png'
                        break
                    case 3:
                        playerTaskDaily.res = 'res/code/task/gy_fk.png'
                        break
                    default:
                        playerTaskDaily.res = 'res/code/task/jinbi.png'
                        break

                }

                data.taskList[i] = playerTaskDaily

            }


            this.view.onUpdateDaily(data)
        },

        initChallengeTasks: function (body) {

            let data = this.initChallengeTasksData(body)
            this.view.initChallengeTasksData(data)

        },

        initChallengeTasksData: function (body) {

            let challengeTasks = {}
            for(let i = 0; i < body.taskList.length; i++){

                let data =  body.taskList[i]
                let challengeTask = {}

                challengeTask.stage = data.stage
                challengeTask.taskId = data.taskId
                challengeTask.taskName = data.taskName
                challengeTask.taskType = data.taskType
                challengeTask.taskNum = data.taskNum
                challengeTask.propNum = data.propNum
                challengeTask.reachNum = data.reachNum
                challengeTask.status = data.status

                switch (data.propCode) {
                    case 1://货币
                        challengeTask.propRes = 'res/code/task/jinbi.png'
                        break
                    case 2:
                        challengeTask.propRes = 'res/code/task/zuanshi.png'
                        break
                    case 3:
                        challengeTask.propRes = 'res/code/task/gy_fk.png'
                        break
                    default:
                        challengeTask.propRes = 'res/code/task/jinbi.png'
                        break

                }


                switch (data.stage) {
                    case 1:
                        challengeTask.stageRes = 'res/code/task/rwzx_8.png'
                        break
                    case 2:
                        challengeTask.stageRes = 'res/code/task/rwzx_6.png'
                        break
                    case 3:
                        challengeTask.stageRes = 'res/code/task/rwzx_5.png'
                        break
                    default:
                        challengeTask.stageRes = 'res/code/task/rwzx_8.png'
                        break

                }

                switch (data.status) {
                    case 0:
                        challengeTask.statusRes = 'res/button/xin_anniu_19.png'
                        challengeTask.statusText = '去完成'
                        break
                    case 1:
                        challengeTask.statusRes = 'res/button/xin_anniu_18.png'
                        challengeTask.statusText = '未领取'
                        break
                    case 2:
                        challengeTask.statusRes = 'res/code/task/yj_3.png'
                        break
                    default:
                        challengeTask.statusRes = 'res/button/xin_anniu_19.png'
                        challengeTask.statusText = '去完成'
                        break
                }



                challengeTasks[i] =  challengeTask

            }

            return challengeTasks

        },




        onReceiveChallegeReward: function (msg) {
            let data = {}
            data.stage = msg.stage

            data.status = 2
            data.statusRes = 'res/code/task/yj_3.png'

            this.view.onReceiveChallegeReward(data)
            this.onShowPropsUI([msg.rewardProp])
        },

        onShowPropsUI: function (dataList) {

            let propList = []
            GameUtil.getPropsData(dataList,propList,'',GameUtil.DATATYPE_1,GameUtil.CURRENCYTYPE_1,GameUtil.UNITLOCATION_BEFORE,'x')

            appInstance.gameAgent().addReceivePropsUI(propList)
        },


        refreshChallegeTasks: function (body) {
            let data = this.initChallengeTasksData(body)
            this.view.onRefreshChallegeTasks(data)
        },

    })
    return mdt
})