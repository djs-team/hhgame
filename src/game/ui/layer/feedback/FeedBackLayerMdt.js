
/**
 *  TaskMdt Mediator
 *
 */
load('game/ui/layer/feedback/FeedBackLayerMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let mdt = Mediator.extend({
        mediatorName: 'FeedBackLayerMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.HALL_FEEDBACK_LIST,
                GameEvent.HALL_FEEDBACK_INFO,
                GameEvent.HALL_FEEDBACK_SUBMIT

            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.HALL_FEEDBACK_LIST:
                    this.onFormatFeedBackList(body)
                    break
                case GameEvent.HALL_FEEDBACK_INFO:
                    this.onFormatFeedBackInfo(body)
                    break
                case GameEvent.HALL_FEEDBACK_SUBMIT:
                    this.onForMatSubMitResult(body)
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


        },

        onFormatFeedBackList: function (body) {
            let data = []
            for(let i = 0; i < body.feedbackList.length; i++){

                let feedBack = body.feedbackList[i]
                let info = {}

                info.feedbackId = feedBack.feedbackId
                info.createTime = feedBack.createTime
                info.status = feedBack.status

                data.push(info)
            }

            this.view.updateMyMsgList(data)
        },

        onFormatFeedBackInfo: function (body) {
            let data = {}
            data.feedbackId = body.feedbackId
            data.feedbackList = []
            for(let i = 0; i < body.feedbackList.length; i++){

                let feedBack = body.feedbackList[i]
                let info = {}

                this.onForMatPlayerFeedbackInfo(feedBack,info)

                data.feedbackList.push(info)
            }

            this.view.updateMyMsgDetail(data)
        },


        onForMatPlayerFeedbackInfo: function (data,info) {
            info.tile = data.type == 0 ? '提交问题' : '客服回复'
            info.time = data.time
            info.txt = data.text
        },

        onForMatSubMitResult: function (body) {

            let data = {}
            cc.log('-------------onForMatSubMitResult---------body :' + JSON.stringify(body))
            this.onForMatPlayerFeedbackInfo(body.playerFeedback,data)
            this.view.onDealSubMitResult(data)

        },


        onFormatTimes: function (timeStr) {

            let date = new Date(timeStr)
            let time = (this.padLeftZero(date.getMonth() + 1)) + '-' + (this.padLeftZero(date.getDate())) + '  ' + (this.padLeftZero(date.getHours())) + ":" + (this.padLeftZero(date.getMinutes()))
            return time
        },

        padLeftZero: function (str) {

            return ('00' + str).substr(str.toString().length);
        }
    })
    return mdt
})