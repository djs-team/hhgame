
/**
 *  CashCowMdt Mediator
 *
 */
load('game/ui/layer/email/EmailMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let mdt = Mediator.extend({
        mediatorName: 'EmailMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.HALL_EMAIL_INFO,
                GameEvent.HALL_EMAIL_DEL,
                GameEvent.HALL_EMAIL_RECEIVE,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.HALL_EMAIL_INFO:
                    this.onEmailDealData(body)
                    break
                case GameEvent.HALL_EMAIL_DEL:
                    this.view.onEmailDel(body)
                    break
                case GameEvent.HALL_EMAIL_RECEIVE:
                    this.view.onEmailReceive(body)
                    break
            }
        },


        onRegister: function () {

            this.initView()
        },

        onRemove: function () {
        },

        initView: function () {
            let msg = {
                'startIndex' : 0,
                'endIndex' : 6
            }
            appInstance.gameAgent().httpGame().emailInfoReq(msg)
        },
        onEmailDealData: function (msg) {
            let data = []
            if (msg.mailList.length>0) {
                for (let i=0; i<msg.mailList.length; i++) {
                    let emailData = msg.mailList[i]
                    let emailInformation = {}
                    emailInformation.mailId = emailData.mailId
                    emailInformation.mailTitle = emailData.mailTitle
                    emailInformation.mailInfo = emailData.mailInfo
                    emailInformation.status = emailData.status
                    emailInformation.createTime = this.onFormatDateTime(emailData.createTime)
                    emailInformation.rewardsData = []
                    let rewardInfo = emailData.rewardList
                    if (rewardInfo.length>0) {
                        for (let j=0; j<rewardInfo.length; j++) {
                            let rewardData = rewardInfo[j]
                            let rewardPropData = {}
                            GameUtil.getPropData(rewardData, rewardPropData, GameUtil.CURRENCYTYPE_1, GameUtil.UNITLOCATION_BEFORE, 'x')
                            emailInformation.rewardsData.push(rewardPropData)
                        }
                    }
                    data.push(emailInformation)
                }
            }
            this.view.onEmailView(data);
        },
        onFormatDateTime: function (timestamp) {
            let d = new Date(parseInt(timestamp));
            let month = (d.getMonth() + 1) < 10 ? (0 + "" + (d.getMonth() + 1)) : (d.getMonth() + 1);
            let day = d.getDate() < 10 ? (0 + "" + d.getDate()) : d.getDate();
            let hour = d.getHours() < 10 ? (0 + "" + d.getHours()) : d.getHours();
            let minute = d.getMinutes() < 10 ? (0 + "" + d.getMinutes()) : d.getMinutes();
            let second = d.getSeconds() < 10 ? (0 + "" + d.getSeconds()) : d.getSeconds();
            let dateString = d.getFullYear() + "-" + month + "-" + day + " " + hour + ":" + minute
            return dateString;
        },
        onEmailDel: function () {

        }
    })
    return mdt
})