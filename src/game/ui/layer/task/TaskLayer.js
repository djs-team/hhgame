
load('game/ui/layer/task/TaskLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let TaskMdt = include('game/ui/layer/task/TaskMdt')
    let GameConfig = include('game/config/GameConfig')
    let GameEvent = include('game/config/GameEvent')
    let taskLayer = BaseLayer.extend({
        _className: 'taskLayer',
        ctor: function () {
            this._super(ResConfig.View.TaskLayer)
            this.registerMediator(new TaskMdt(this))
        },
        RES_BINDING: function () {
            return {

               'pnl/cancleBtn': { onClicked: this.onCancleBtnClick },

                'pnl/leftPnl/everyTaskBtn': { onClicked: this.onTaskBtnClick},
                'pnl/leftPnl/challengeTaskBtn': { onClicked: this.onTaskBtnClick},

                'pnl/rightPnl/everyTasksPnl': { },
                'pnl/rightPnl/everyTasksPnl/topPnl': { },
                'pnl/rightPnl/everyTasksPnl/topPnl/activityPg/activityValue': { },
                'pnl/rightPnl/everyTasksPnl/topPnl/progressBarPnl/activityLoadingBar': { },
                'pnl/rightPnl/everyTasksPnl/topPnl/progressBarPnl/activityLoadingBar/activityAwardsCell': { },
                'pnl/rightPnl/everyTasksPnl/bmPnl/taskList': { },
                'pnl/rightPnl/everyTasksPnl/bmPnl/taskCell': { },

                'pnl/rightPnl/challengeTasksPnl': { },
                'pnl/rightPnl/challengeTasksPnl/challengeTaskCell': { },
                'pnl/rightPnl/challengeTasksPnl/changeTaskBtn': { onClicked: this.onChangeTaskBtnClick },

                'pnl/popUpPnl/rewardOnePnl': { onClicked: this.onHideRewardOnePnl},
                'pnl/popUpPnl/rewardTwoPnl': { onClicked: this.onHideRewardTwoPnl},
                'pnl/popUpPnl/rewardThreePnl': { onClicked: this.onHideRewardThreePnl},


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
        initView: function () {

            this.activityAwardsCell.setVisible(false)
            this.taskCell.setVisible(false)
            this.challengeTasksPnl.setVisible(false)
            this.rewardOnePnl.setVisible(false)
            this.rewardTwoPnl.setVisible(false)
            this.rewardThreePnl.setVisible(false)
            this.challengeTasksPnl.setVisible(false)
            this.challengeTaskCell.setVisible(false)

            this.challengeTaskCell.getChildByName('completePg').setVisible(false)
            this.challengeTaskCell.getChildByName('challegeBtnPg').setVisible(false)
            this.challengeTaskCell.getChildByName('challengeTaskBolck').setVisible(false)

            this.onEveryTaskBtnClick()


        },


        onUpdateDaily: function (data) {


            this.activityValue.setString(data.totalActivity)
            this.onUpdateActivityTask(data.totalActivity,data.allActivityProcess,data.activityRewards)
            this.onUpdateDailyTask(data.taskList)

        },


        onUpdateActivityTask: function (totalActivity,allActivityProcess,activityRewards) {


            //修改进度条数据
            let procent = Math.round(totalActivity/allActivityProcess*100)
            this.activityLoadingBar.setPercent(procent)


            let lengtn = Object.keys(activityRewards).length
            for(let i = 0; i < lengtn; i++){
                this.initActivityCell(allActivityProcess,activityRewards[i])
            }

        },

        initActivityCell: function (allActivityProcess,activityReward) {

            let barPositionX = this.activityLoadingBar.getPositionX()
            let barPositionY = this.activityLoadingBar.getPositionY()
            let cell = this.activityAwardsCell.clone()

            cell.setName('activityTask'+activityReward.activityId)
            cell.status = activityReward.status
            cell.activityId = activityReward.activityId

            cell.setPositionX(barPositionX + (activityReward.activity / allActivityProcess * 477))
            cell.setPositionY(barPositionY - 7)
            cell.getChildByName('activityPg').loadTexture(activityReward.res)
            cell.getChildByName('activityText').setString(activityReward.activityText)
            cell.getChildByName('activityDescPg').getChildByName('activityDescribeText').setString(activityReward.desc)



            cell.addTouchEventListener(function(sender, et) {
                this.onActivityCellTouch(sender, et)
            }.bind(this))

            cell.getChildByName('activityDescPg').setVisible(false)
            cell.setVisible(true)
            this.activityLoadingBar.addChild(cell)

        },


        onActivityCellTouch: function (sender, et) {

            switch (et) {
                case 0:
                    break
                case 1:
                    sender.getChildByName('activityDescPg').setVisible(true)
                    break
                case 2:
                    switch (sender.status) {
                        case 0:
                            appInstance.gameAgent().Tips('尚未满足要求，请继续完成任务')
                            break
                        case 1:
                            this.onReceiveDailyTask(sender.activityId,1)
                            break
                        default:
                            break
                    }
                    sender.getChildByName('activityDescPg').setVisible(false)
                    break
                case 3:
                    sender.getChildByName('activityDescPg').setVisible(false)
                    break
            }



            sender.getChildByName('activityDescPg').getChildByName('activityDescribeText').setVisible(true)



        },


        onUpdateDailyTask: function (taskList) {


            let lengtn = Object.keys(taskList).length
            for(let i = 0; i < lengtn; i++){
                this.initDailyTaskCell(taskList[i])
            }

        },

        initDailyTaskCell: function (dailyTask) {


            let cell = this.taskCell.clone()
            cell._data = {
                taskId: dailyTask.taskId,
                status: dailyTask.status,
                taskType: dailyTask.taskType,
            }

            cell.setName('dailyTask'+dailyTask.taskId)
            cell.getChildByName('everyDayTaskNameText').setString(dailyTask.taskName)
            cell.getChildByName('awardsVal').setString('x' + dailyTask.propNum)
            cell.getChildByName('activityNumText').setString('活跃度+' + dailyTask.activity)
            cell.getChildByName('acceptedBg').getChildByName('acceptedTypePg').loadTexture(dailyTask.res)
            cell.getChildByName('everyDayProgressBarPnl').getChildByName('everyDayProgressBar').setPercent(Math.round(dailyTask.reachNum/dailyTask.taskNum*100))

            if (dailyTask.reachNum>dailyTask.taskNum) {
                cell.getChildByName('everyDayProgressBarPnl').getChildByName('progressValueText').setString(dailyTask.taskNum + '/' + dailyTask.taskNum)
            } else {
                cell.getChildByName('everyDayProgressBarPnl').getChildByName('progressValueText').setString(dailyTask.reachNum + '/' + dailyTask.taskNum)
            }

            switch (dailyTask.status) {
                case 0:
                    cell.getChildByName('everyDayDealBtn').setVisible(true)
                    cell.getChildByName('everyAcceptBtn').setVisible(false)
                    cell.getChildByName('everyAcceptedPg').setVisible(false)

                    cell.getChildByName('everyDayDealBtn').addClickEventListener(function(sender, et) {
                        this.onDailyTaskCellClick(sender)
                    }.bind(this))
                    break
                case 1:
                    cell.getChildByName('everyDayDealBtn').setVisible(false)
                    cell.getChildByName('everyAcceptBtn').setVisible(true)
                    cell.getChildByName('everyAcceptedPg').setVisible(false)

                    cell.getChildByName('everyAcceptBtn').addClickEventListener(function(sender, et) {
                        this.onDailyTaskCellClick(sender)
                    }.bind(this))
                    break
                case 2:
                    cell.getChildByName('everyDayDealBtn').setVisible(false)
                    cell.getChildByName('everyAcceptBtn').setVisible(false)
                    cell.getChildByName('everyAcceptedPg').setVisible(true)
                    break
                default:
                    cell.getChildByName('everyDayDealBtn').setVisible(true)
                    cell.getChildByName('everyAcceptBtn').setVisible(false)
                    cell.getChildByName('everyAcceptedPg').setVisible(false)

                    cell.getChildByName('everyDayDealBtn').addClickEventListener(function(sender, et) {
                        this.onDailyTaskCellClick(sender)
                    }.bind(this))
                    break
            }




            cell.getChildByName
            if(dailyTask.status != 2){
                cell.getChildByName('taskCellPnl').setVisible(false)
            }

            cell.setVisible(true)
            this.taskList.pushBackCustomItem(cell)

        },

        onDailyTaskCellClick: function (sender) {
            let data = sender.getParent()._data

            let status = data.status
            switch (status) {
                case 0:
                    this.onJumpOtherLayer(data.taskType)
                    break
                case 1:
                    //调用http方法，领取货币
                    this.onReceiveDailyTask(data.taskId,0)

                    break
                default:
                    break

            }

        },

        onCancleBtnClick: function () {
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)

        },

        onEveryTaskBtnClick: function () {

            this.everyTaskBtn.setTouchEnabled(false)
            this.everyTaskBtn.setBright(false)
            this.challengeTaskBtn.setTouchEnabled(true)
            this.challengeTaskBtn.setBright(true)

            this.everyTasksPnl.setVisible(true)
            this.challengeTasksPnl.setVisible(false)


        },

        onChallengeTaskBtnClick: function () {

            this.everyTaskBtn.setTouchEnabled(true)
            this.everyTaskBtn.setBright(true)
            this.challengeTaskBtn.setTouchEnabled(false)
            this.challengeTaskBtn.setBright(false)

            this.everyTasksPnl.setVisible(false)
            this.challengeTasksPnl.setVisible(true)


        },

        onTaskBtnClick: function(sender) {

            if(sender ===  this.challengeTaskBtn){

                this.onChallengeTaskBtnClick()
            }else{

                this.onEveryTaskBtnClick()
            }

        },

        onReceiveDailyTask: function (code,type) {

            let msg = {}
            msg.code = code
            msg.type = type
            appInstance.gameAgent().httpGame().RECEIVEDAILYREWARDSReq(msg)

        },

        receiveDailyRewards: function (data) {

            this.updataActivityTasksByIds(data.totalActivity,data.maxActivity,data.boxList)
            if(data.taskId > 0){
                this.updateDailyTaskByTaskId(data.taskId)
            }
            this.receiveRewards(data.rewards)
        },

        updateDailyTaskByTaskId: function (taskId) {

            this.taskList.getChildByName('dailyTask'+taskId).getChildByName('taskCellPnl').setVisible(true)
            this.taskList.getChildByName('dailyTask'+taskId).getChildByName('everyDayProgressBarPnl').getChildByName('everyDayProgressBar').setPercent(100)
            this.taskList.getChildByName('dailyTask'+taskId).getChildByName('everyDayDealBtn').setVisible(false)
            this.taskList.getChildByName('dailyTask'+taskId).getChildByName('everyAcceptBtn').setVisible(false)
            this.taskList.getChildByName('dailyTask'+taskId).getChildByName('everyAcceptedPg').setVisible(true)

        },

        updataActivityTasksByIds: function (totalActivity,maxActivity,boxList) {

            this.activityValue.setString(totalActivity)
            let procent = Math.round(totalActivity/maxActivity*100)


            this.activityLoadingBar.setPercent(procent)

            let lengtn = Object.keys(boxList).length
            for(let i = 0; i < lengtn; i++){
                this.updateActivityCell(boxList[i])
            }

        },


        updateActivityCell: function (activityConfig) {

            let cell = this.activityLoadingBar.getChildByName('activityTask'+activityConfig.activityId)
            cell.getChildByName('activityPg').loadTexture(activityConfig.res)
            cell.getChildByName('activityText').setString(activityConfig.activityText)
            cell.status = 1

        },

        receiveRewards: function (data) {

            let lengtn = Object.keys(data).length
            let _pnl
            switch (lengtn) {
                case 1:
                    _pnl = this.rewardOnePnl
                    break
                case 2:
                    _pnl = this.rewardTwoPnl
                    break
                case 3:
                    _pnl = this.rewardThreePnl
                    break
                default:
                    break

            }

            for(let i = 1; i <= lengtn; i++){

                let cell = data[i-1]
                _pnl.getChildByName('acceptedBg' + i).getChildByName('acceptedTypePg').loadTexture(cell.res)
                _pnl.getChildByName('acceptedBg' + i).getChildByName('awardsVal').setString('x' + cell.propNum)
            }

            _pnl.setVisible(true)
        },
        onHideRewardOnePnl: function () {

            this.rewardOnePnl.setVisible(false)

        },

        onHideRewardTwoPnl: function () {

            this.rewardTwoPnl.setVisible(false)

        },


        onHideRewardThreePnl: function () {

            this.rewardThreePnl.setVisible(false)

        },

        initChallengeTasksData: function (data) {


            let lengtn = Object.keys(data).length
            for(let i = 0; i < lengtn; i++){

                this.initChallengeTaskData(data[i])

            }
        },

        initChallengeTaskData: function (data) {

            let cell = this.challengeTaskCell.clone()
            cell._data = {
                'stage' : data.stage,
                'taskId' : data.taskId,
                'status' : data.status,
                'taskType' : data.taskType,
                'taskNum' : data.taskNum,
            }

            cell.setName('challegeTask'+data.stage)
            cell.getChildByName('statePg').loadTexture(data.stageRes)
            cell.getChildByName('acceptedPg').getChildByName('acceptedTypePg').loadTexture(data.propRes)
            cell.getChildByName('acceptedPg').getChildByName('awardsVal').setString('x'+data.propNum)
            cell.getChildByName('taskName').setString(data.taskName)

            cell.getChildByName('challegeTaskProgressBarPnl').getChildByName('challegeTaskProgressBar').setPercent(Math.round(data.reachNum/data.taskNum*100))
            if (data.reachNum>data.taskNum) {
                cell.getChildByName('challegeTaskProgressBarPnl').getChildByName('progressValueText').setString(data.taskNum + '/' + data.taskNum)
            } else {
                cell.getChildByName('challegeTaskProgressBarPnl').getChildByName('progressValueText').setString(data.reachNum + '/' + data.taskNum)
            }


            if(data.status == 2){
                cell.getChildByName('completePg').setVisible(true)
                cell.getChildByName('challengeTaskBolck').setVisible(true)
                cell.getChildByName('challegeBtnPg').setVisible(false)

            }else{

                cell.getChildByName('completePg').setVisible(false)
                cell.getChildByName('challengeTaskBolck').setVisible(false)

                cell.getChildByName('challegeBtnPg').setVisible(true)
                cell.getChildByName('challegeBtnPg').loadTexture(data.statusRes)
                cell.getChildByName('challegeBtnPg').getChildByName('challegeBtnText').setString(data.statusText)
                cell.getChildByName('challegeBtnPg').addClickEventListener(function (sender,et) {

                    this.onClickChallegeTaskBtn(sender)
                }.bind(this))

            }
            cell.getChildByName('statePg').loadTexture(data.stageRes)
            cell.getChildByName('statePg').loadTexture(data.stageRes)
            cell.setPosition(cell.getPositionX() + (data.stage - 1) * 250,cell.getPositionY())
            cell.setVisible(true)
            this.challengeTasksPnl.addChild(cell)

        },

        onClickChallegeTaskBtn: function (sender) {

            let data = sender.getParent()._data
            switch (data.status) {
                case 0:
                   this.onJumpOtherLayer(data.taskType)
                    break
                case 1:
                    //调用http方法，领取货币
                    this.challengeTaskCell.getChildByName('challegeBtnPg').setTouchEnabled(false)
                    this.challengeTaskCell.getChildByName('challegeBtnPg').setBright(false)
                    this.onReceiveChallengTaskRewards(data.taskId,data.stage)

                    break
                default:
                    break

            }

        },

        onJumpOtherLayer: function (taskType) {

            appInstance.gameAgent().addPopUI(ResConfig.Ui[GameConfig.jumping[taskType]])
            this.onCancleBtnClick()

        },

        onReceiveChallengTaskRewards: function (code,stage) {

            let msg = {}
            msg.code = code
            msg.stage = stage
            appInstance.gameAgent().httpGame().RECEIVECHALLENGEWARDSReq(msg)

        },

        onReceiveChallegeReward: function (data) {
            this.challengeTaskCell.getChildByName('challegeBtnPg').setTouchEnabled(true)
            this.challengeTaskCell.getChildByName('challegeBtnPg').setBright(true)
            let cell = this.challengeTasksPnl.getChildByName('challegeTask' + data.stage)

            cell.getChildByName('completePg').setVisible(true)
            cell.getChildByName('challengeTaskBolck').setVisible(true)
            cell.getChildByName('challegeBtnPg').setVisible(false)
            cell.getChildByName('challegeTaskProgressBarPnl').getChildByName('challegeTaskProgressBar').setPercent(100)
            cell.getChildByName('challegeTaskProgressBarPnl').getChildByName('progressValueText').setString(cell._data.taskNum + '/' + cell._data.taskNum)

            this.receiveRewards(data.rewards)


        },

        onChangeTaskBtnClick: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().REFRESHCHALLENGETASKSReq(msg)

        },

        onRefreshChallegeTasks : function (data) {

            let lengtn = Object.keys(data).length
            for(let i = 0; i < lengtn; i++){

                let cell = data[i]

                this.challengeTasksPnl.removeChildByName('challegeTask'+cell.stage,true)
                this.initChallengeTaskData(cell)

            }

        },





    })
    return taskLayer
})
