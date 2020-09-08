
/**
 *  SignMdt Mediator
 *
 */
load('game/ui/layer/sign/SignMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let mdt = Mediator.extend({
        mediatorName: 'SignMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.SIGN_IN_DATA,
                GameEvent.SIGN_IN_DATA_CHECK

            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.SIGN_IN_DATA:
                    this.initSignDatas(body)
                    break
                case GameEvent.SIGN_IN_DATA_CHECK:
                    this.onReceiveAwardsData(body)
                    break
                default:
                    break
            }
        },


        _treasureChestIndex: {



        },

        onRegister: function () {

            this.initView()
        },

        onRemove: function () {
        },

        initView: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().SIGNINDATASReq(msg)

        },

        initSignDatas: function (body) {

            let data = []
            data.flag = body.flag
            data.vipCode = body.vipCode
            data.currentSign = 0

            data.bonusConfigList = []
            data.checkinList = []

            switch (body.vipCode) {
                case 0 :
                    data.goVIPText = '成为VIP'
                    data.videoAcceptText = body.watchNum + '倍签到'
                    break
                case 1 :
                    data.goVIPText = '升级VIP'
                    data.vipLevel = '周VIP会员'
                    data.mulitAcceptText = body.vipNum + '倍签到'
                    data.videoAcceptText = body.watchNum + '倍签到'
                    break
                case 2 :
                    data.goVIPText = '升级VIP'
                    data.vipLevel = '月VIP会员'
                    data.mulitAcceptText = body.vipNum + '倍签到'
                    data.videoAcceptText = body.watchNum + '倍签到'
                    break
                case 3 :
                    data.goVIPText = '升级VIP'
                    data.vipLevel = '季VIP会员'
                    data.mulitAcceptText = body.vipNum + '倍签到'
                    data.videoAcceptText = body.watchNum + '倍签到'
                    break
                case 4 :
                    data.goVIPText = '续费VIP'
                    data.vipLevel = '年VIP会员'
                    data.mulitAcceptText = body.vipNum + '倍签到'
                    data.videoAcceptText = body.watchNum + '倍签到'
                    break
                default:
                    data.goVIPText = '成为VIP'
                    data.videoAcceptText = body.watchNum + '倍签到'
                    break
            }

            for(let i = 0; i < body.checkinList; i++){

                let checkIn = body.checkinList[i]
                let item = {}
                item.checkinId = checkIn.checkinId
                item.status = checkIn.status
                if(item.status == 1)
                    data.checkinId = item.checkinId
                else if(item.status == 2)
                    data.currentSign = item.checkinId

                GameUtil.getPropData(checkIn,item,GameUtil.CURRENCYTYPE_2,GameUtil.UNITLOCATION_BEFORE,'x','sign')

                data.checkinList.push(item)
            }



            for(let i = 0; i < body.bonusConfigList.length; i++){

                let bonusConfig = body.bonusConfigList[i]
                let item = {}
                item.id =bonusConfig.id
                item.status =bonusConfig.status
                item.bounsDesc =bonusConfig.bounsDesc
                item.signDaysText = '签到' + item.id + '天'

                this._treasureChestIndex[item.id] = i

                switch (bonusConfig.status) {
                    case 0:
                        if(i == 0)
                            item.res = 'res/code/sign/qd_9.png'
                        else if(i == 1)
                            item.res = 'res/code/sign/qd_6.png'
                        else
                            item.res = 'res/code/sign/qd_3.png'
                        item.loadingBarAwardsText = ''

                        break
                    case 1:
                        if(i == 0)
                            item.res = 'res/code/sign/qd_8.png'
                        else if(i == 1)
                            item.res = 'res/code/sign/qd_5.png'
                        else
                            item.res = 'res/code/sign/qd_2.png'
                        item.loadingBarAwardsText = '未领取'
                        break
                    case 2:
                        if(i == 0)
                            item.res = 'res/code/sign/qd_7.png'
                        else if(i == 1)
                            item.res = 'res/code/sign/qd_4.png'
                        else
                            item.res = 'res/code/sign/qd_1.png'
                        item.loadingBarAwardsText = '已领取'
                        break
                    default:
                        if(i == 0)
                            item.res = 'res/code/sign/qd_9.png'
                        else if(i == 1)
                            item.res = 'res/code/sign/qd_6.png'
                        else
                            item.res = 'res/code/sign/qd_3.png'
                        item.loadingBarAwardsText = ''
                        break

                }


                data.bonusConfigList.push(item)

            }


            this.view.onInitSignDatas(data)
        },

        onReceiveAwardsData: function (body) {

            let data = {}
            data.checkinId = body.checkinId
            data.type = body.type
            data.treasureChestData = {}


            if(data.type == 1){

                data.treasureChestData.loadingBarAwardsText = '已领取'
                let index = this._treasureChestIndex[data.checkinId]
                if(index == 0)
                    data.treasureChestData.res = 'res/code/sign/qd_7.png'
                else if(index == 1)
                    data.treasureChestData.res = 'res/code/sign/qd_4.png'
                else
                    data.treasureChestData.res = 'res/code/sign/qd_1.png'
            }


            this.view.onReceiveAwardsData(data)

            let propList = []
            GameUtil.getPropsData(body.rewardPropList,propList,'',GameUtil.DATATYPE_1,GameUtil.CURRENCYTYPE_1,GameUtil.UNITLOCATION_BEFORE,'x','sign')
            appInstance.gameAgent().addReceivePropsUI(propList)

        },

    })
    return mdt
})