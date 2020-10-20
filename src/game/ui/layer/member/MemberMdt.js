/**
 *  CashCowMdt Mediator
 *
 */
load('game/ui/layer/member/MemberMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let mdt = Mediator.extend({
        mediatorName: 'MemberMdt',
        ctor: function (view) {
            this._super(this.mediatorName, view)
        },
        getNotificationList: function () {
            return [
                GameEvent.VIP_INFO_GET,
                GameEvent.VIP_INFO_RECEIVE,
                GameEvent.PLAYER_BUY_VIP_ORDER,
                GameEvent.PLAYER_BUY_VIP_ORDER_APPLE_CHECK,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.VIP_INFO_GET:
                    this.initViewData(body)
                    break
                case GameEvent.VIP_INFO_RECEIVE:
                    this.onReceiveRewards(body)
                    break
                case GameEvent.PLAYER_BUY_VIP_ORDER:
                    appInstance.gameAgent().Tips('------------------------------------订单调用成功！！！')
                    cc.log('=============' + JSON.stringify(body))
                    let payType = body.payType;
                    if (payType == 1) {
                        cc.log('=============' + "调用支付宝")
                        appInstance.nativeApi().thirdPay("alipay", body.zhifubaoSign)
                    } else if (payType == 2) {
                        let wxSign=JSON.stringify(body.wxinfo)
                        cc.log('=============' + "调用微信"+wxSign)
                        appInstance.nativeApi().thirdPay("wx", wxSign)
                    } else if (payType == 3) {
                        // 苹果支付
                        let vipCode = body.vipCode;
                        var iosFlag = "";
                        if (parseInt(vipCode) < 5) {
                          // VIP
                          iosFlag = "com.hehegames.hhyuejumajiang.v." + vipCode;
                        } else {
                          iosFlag = "com.hehegames.hhyuejumajiang.s." + vipCode;
                        }

                        appInstance.nativeApi().applyPay(iosFlag, body.orderId);
                    }
                    break
                case GameEvent.PLAYER_BUY_VIP_ORDER_APPLE_CHECK:
                    // 苹果支付检验成功
                    appInstance.gameAgent().Tips('------------------------------------苹果支付订单校验成功！！！')
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
            appInstance.gameAgent().httpGame().GETVIPINFOReq(msg)

        },

        initViewData: function (body) {

            let data = {}
            data.privilegeList = []
            data.memberList = []
            data.rechargeList = []

            data.playerVipCode = body.playerVipCode
            data.canVipDaily = body.canVipDaily
            data.vipCodeList = body.vipCodeList


            if (body.playerVipCode > 0) {
                this.formatDate(data, body.surplusTime)

            }


            for (let i = 0; i < body.vipList.length; i++) {

                let _vip = body.vipList[i]
                let privilegeData = {}
                let memberData = {}
                let rechargeData = {}
                data.privilegeList.push(privilegeData)
                data.memberList.push(memberData)
                data.rechargeList.push(rechargeData)


                privilegeData.vipCode = _vip.vipCode
                rechargeData.vipCode = _vip.vipCode
                memberData.vipCode = _vip.vipCode
                privilegeData.roleCode = _vip.roleCode
                rechargeData.roleCode = _vip.roleCode
                memberData.roleCode = _vip.roleCode
                privilegeData.equityMulti = '5、最多领取' + _vip.equityMulti + '倍签到奖励'

                GameUtil.getPropsData(_vip.firstChargeGift, privilegeData, 'firstChargeGift', GameUtil.DATATYPE_2, GameUtil.CURRENCYTYPE_1, GameUtil.UNITLOCATION_BEFORE, 'x')

                GameUtil.getPropsData(_vip.dailyGift, privilegeData, 'dailyGift', GameUtil.DATATYPE_2, GameUtil.CURRENCYTYPE_1, GameUtil.UNITLOCATION_BEFORE, 'x')


                let level = ''
                switch (_vip.vipCode) {

                    case GameConfig.VIP_LEVEL_1:
                        rechargeData.rechageBg = 'res/code/member/vip_1.png'
                        rechargeData.memberLevelPg = 'res/code/member/vip_1_1.png'
                        privilegeData.memberLevelPg = 'res/code/member/vip_1_1.png'
                        break
                    case GameConfig.VIP_LEVEL_2:
                        level = '月'
                        rechargeData.rechageBg = 'res/code/member/vip_2.png'
                        rechargeData.memberLevelPg = 'res/code/member/vip_2_1.png'
                        privilegeData.memberLevelPg = 'res/code/member/vip_2_1.png'
                        break
                    case GameConfig.VIP_LEVEL_3:
                        level = '季'
                        rechargeData.rechageBg = 'res/code/member/vip_3.png'
                        rechargeData.memberLevelPg = 'res/code/member/vip_3_1.png'
                        privilegeData.memberLevelPg = 'res/code/member/vip_3_1.png'
                        break
                    case GameConfig.VIP_LEVEL_4:
                        rechargeData.rechageBg = 'res/code/member/vip_4.png'
                        rechargeData.memberLevelPg = 'res/code/member/vip_4_1.png'
                        privilegeData.memberLevelPg = 'res/code/member/vip_4_1.png'
                        level = '年'
                        break
                    default:
                        break
                }
                level = this.onGetMemberLevel(_vip.vipCode)
                memberData.memberName = level + 'VIP专属角色'
                rechargeData.time = '时限：' + _vip.time + '天'
                rechargeData.money = _vip.money + 'RMB'


            }
            this.view.initViewData(data)


        },

        formatDate: function (data, timeStamp) {

            let days = parseInt(timeStamp / 1000 / 60 / 60 / 24)
            let hours = Math.round(timeStamp / 1000 / 60 / 60 % 24)
            data.dueReminderText = '尊敬的' + this.onGetMemberLevel(data.playerVipCode) + 'VIP会员，您的VIP有效时限距离到期还有：' + days + '天' + hours + '时'

        },

        onReceiveRewards: function (body) {

            let propList = []
            GameUtil.getPropsData(body.dailyGift, propList, '', GameUtil.DATATYPE_1, GameUtil.CURRENCYTYPE_1, GameUtil.UNITLOCATION_BEFORE, 'x', 'member')
            appInstance.gameAgent().addReceivePropsUI(propList)

            this.view.onReceiveRewards()
        },

        onGetMemberLevel: function (vipLevel) {
            let vipName = ''
            switch (vipLevel) {
                case GameConfig.VIP_LEVEL_1:
                    vipName = '周'
                    break
                case GameConfig.VIP_LEVEL_2:
                    vipName = '月'
                    break
                case GameConfig.VIP_LEVEL_3:
                    vipName = '季'
                    break
                case GameConfig.VIP_LEVEL_4:
                    vipName = '年'
                    break
                default:
                    break
            }

            return vipName
        }

    })
    return mdt
})
