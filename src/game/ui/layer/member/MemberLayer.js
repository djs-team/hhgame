
load('game/ui/layer/member/MemberLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let GameConfig = include('game/config/GameConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let MemberMdt = include('game/ui/layer/member/MemberMdt')
    let MemberLayer = BaseLayer.extend({
        _className: 'MemberLayer',
        ctor: function () {
            this._super(ResConfig.View.MemberLayer)
            this.registerMediator(new MemberMdt(this))
        },
        RES_BINDING: function () {
            return {

                'pnl/closeBtn': { onClicked: this.onCloseClick},

                'pnl/privilegePnl': { },
                'pnl/privilegePnl/privilegePgPnl/interestsBg/vipSignText': { },
                'pnl/privilegePnl/dataPnl/vipLevelPg': { },
                'pnl/privilegePnl/dataPnl/characterPg': { },
                'pnl/privilegePnl/dataPnl/dueReminderText': { },
                'pnl/privilegePnl/dataPnl/firstPnl/firstCoinsBg': { },
                'pnl/privilegePnl/dataPnl/firstPnl/firstDiamondsBg': { },
                'pnl/privilegePnl/dataPnl/firstPnl/firstBlockPnl': { },
                'pnl/privilegePnl/dataPnl/firstPnl/acceptedPg': { },
                'pnl/privilegePnl/dataPnl/everyDayPnl/everyDayCoinsBg': { },
                'pnl/privilegePnl/dataPnl/everyDayPnl/everyDayDiamondsBg': { },
                'pnl/privilegePnl/dataPnl/everyDayPnl/everyDayBlockPnl': { },
                'pnl/privilegePnl/dataPnl/everyDayPnl/acceptedPg': { },


                'pnl/privilegePnl/btPnl/acceptBtn': { onClicked: this.onAcceptCliecked},
                'pnl/privilegePnl/btPnl/renewBtn': { onClicked: this.onShowRechargeClicked},
                'pnl/privilegePnl/btPnl/renew1Btn': { onClicked: this.onShowRechargeClicked},
                'pnl/privilegePnl/btPnl/upgradeBtn': { onClicked: this.onShowRechargeClicked},
                'pnl/privilegePnl/btPnl/turnNextBtn': { onClicked: this.onTurnNextCliecked},
                'pnl/privilegePnl/btPnl/turnPreviousBtn': { onClicked: this.onTurnPreviousCliecked},
                'pnl/privilegePnl/btPnl/levelLowerBtn': { },
                'pnl/privilegePnl/btPnl/toBeVIPBtn': { onClicked: this.onShowRechargeClicked},

                'pnl/rechargePnl': { },
                'pnl/rechargePnl/rechargeListView': { },
                'pnl/rechargePnl/rechargeCell': { },
                'pnl/rechargePnl/memberListView': { },
                'pnl/rechargePnl/memberCell': { },




            }
        },


        onCreate: function () {
            this._super()
        },

        onEnter: function () {
            this._super()
            this.initData()
            this.initView()
            this.showView()
        },

        onExit: function () {
            this._super()
        },


        initData: function () {

            this.rechargePnl.setVisible(false)
            this.turnNextBtn.setVisible(false)
            this.turnPreviousBtn.setVisible(false)

        },

        initView: function () {



        },




        showView: function () {
          //  this.onShowRecordPnlClick()
        },

        onCloseClick: function () {
            appInstance.uiManager().removeUI(this)
        },

        onAcceptCliecked: function () {

            let msg = {}
            msg.vipCode = this._PublicData.vipCode
            appInstance.gameAgent().httpGame().RECEIVEVIPDAILYReq(msg)

        },

        onShowRechargeClicked: function () {
            this.privilegePnl.setVisible(false)
            this.rechargePnl.setVisible(true)
            this.turnNextBtn.setVisible(false)
            this.turnPreviousBtn.setVisible(false)

            this.onInitRechargeDatas()
            this.onInitMemberDatas()

        },

        onInitRechargeDatas: function () {

            for(let i = 0; i < this.rechargeList.length; i++){
                let cell = this.rechargeCell.clone()
                let rechargeData = rechargeList[i]
                this.rechargeListView.addchild(cell)
                cell.setVisible(true)
                cell.getChildByName('rechageBg').loadTexture(rechargeData.rechageBg)
                cell.getChildByName('memberLevelPg').loadTexture(rechargeData.memberLevelPg)
                cell.getChildByName('pricePg').getChildByName('timeLimitText').setString(rechargeData.time)
                cell.getChildByName('pricePg').getChildByName('priceText').setString(rechargeData.money)
                cell._sendData = {

                    'vipCode' : rechargeData.vipCode

                }

                cell.setPositionX(210 * i)

                cell.addClickEventListener(function (sender,et) {

                    this.onRechargeClicked(sender)

                }.bind(this))

            }

        },

        onRechargeClicked : function (sender) {

            let _sendData = sender.getParent()._sendData
            //调用充值接口
            appInstance.gameAgent().Tips('-----------------------------调用充值接口-----------------vipCode ：' + _sendData.vipCode)

        },

        onInitMemberDatas: function () {

            for(let i = 0; i < this.memberList.length; i++){
                let cell = this.memberCell.clone()
                let memberData = memberList[i]
                this.memberListView.addchild(cell)
                cell.setVisible(true)
                cell.getChildByName('memberImage').loadTexture(memberData.memberRes)
                cell.getChildByName('memberNameText').setString(memberData.memberName)

                cell.setPositionX(172 * i)

            }



        },

        onTurnNextCliecked: function () {

            let nextCode = this._PublicData.turnCodeData.turnNextCode
            this.changeTurnBtn(nextCode)
        },

        onTurnPreviousCliecked: function () {
            let nextCode = this._PublicData.turnCodeData.turnPreviousCode
            this.changeTurnBtn(nextCode)
        },



        initViewData: function (data) {

            this._PublicData = {
                'playerVipCode' : data.playerVipCode,
                'canVipDaily' : data.canVipDaily,
                'vipCodeList' : data.vipCodeList,
                'dueReminderText' : data.dueReminderText,
            }


            this.initTurnCodeData(this._PublicData.playerVipCode)
            this.initPrivilegeData(data.privilegeList)
            this.initMemberData(data.memberList)
            this.initRechargeData(data.rechargeList)

        },

        initTurnCodeData: function (turnCurrentCode) {

            switch (turnCurrentCode) {

                case 0:
                case 1:
                    this._PublicData.turnCodeData = {
                        'turnNextCode' : 2,
                        'turnPreviousCode' : 1,
                        'turnCurrentCode' : 1,
                    }
                    break
                case 2:
                case 3:
                    this._PublicData.turnCodeData = {
                        'turnNextCode' : turnCurrentCode + 1,
                        'turnPreviousCode' : turnCurrentCode - 1,
                        'turnCurrentCode' : turnCurrentCode,
                    }
                    break
                case 4:
                    this._PublicData.turnCodeData = {
                        'turnNextCode' : 4,
                        'turnPreviousCode' : turnCurrentCode - 1,
                        'turnCurrentCode' : turnCurrentCode,
                    }
                    break
                default:
                    this._PublicData.turnCodeData = {
                        'turnNextCode' : 2,
                        'turnPreviousCode' : 1,
                        'turnCurrentCode' : 1,
                    }
                    break

            }

        },

        initPrivilegeData: function (privilegeList) {



            this._PrivilegeListData = []
            for(let i = 1; i <= privilegeList.length; i++){

                let privilege = privilegeList[i-1]
                let privilegeData = {}
                privilegeData.vipCode = privilege.vipCode
                privilegeData.equityMulti = privilege.equityMulti
                privilegeData.firstChargeGift = privilege.firstChargeGift
                privilegeData.dailyGift = privilege.dailyGift
                privilegeData.memberRes = privilege.res
                privilegeData.memberLevelPg = privilege.memberLevelPg

                this._PrivilegeListData[i] = privilegeData

            }



            this.onChangePrivilegeData(this._PublicData.playerVipCode)


        },

        onChangePrivilegeData: function (memberLevel) {



            this.changeTurnBtn(memberLevel)


            if(memberLevel == GameConfig.VIP_LEVEL_0)
                memberLevel = GameConfig.VIP_LEVEL_1

            let privilege = this._PrivilegeListData[memberLevel]
            this._PublicData.vipCode = privilege.vipCode

            this.vipLevelPg.loadTexture(privilege.memberLevelPg)
            this.characterPg.loadTexture(privilege.memberRes)
            this.vipSignText.setString(privilege.equityMulti)

            for(let i = 0; i < privilege.firstChargeGift; i++){
                let _data = privilege.firstChargeGift[i]
                let _nodeProject
                if(_data.propCode == GameConfig.propType_currency_coin){
                    _nodeProject = this.firstCoinsBg
                }else{
                    _nodeProject = this.firstDiamondsBg
                }

                this._nodeProject.getChildByName('acceptedTypePg').loadTexture(_data.res)
                this._nodeProject.getChildByName('awardsVal').setString(_data.num)

            }

            for(let i = 0; i < privilege.dailyGift; i++){
                let _data = privilege.dailyGift[i]
                let _nodeProject
                if(_data.propCode == GameConfig.propType_currency_coin){
                    _nodeProject = this.everyDayCoinsBg
                }else{
                    _nodeProject = this.everyDayDiamondsBg
                }

                this._nodeProject.getChildByName('acceptedTypePg').loadTexture(_data.res)
                this._nodeProject.getChildByName('awardsVal').setString(_data.num)

            }

            if(this._PublicData.vipCodeList.indexOf(privilege.vipCode) != -1){
                this.firstPnl.getChildByName('firstBlockPnl').setVisible(true)
                this.firstPnl.getChildByName('acceptedPg').setVisible(true)
            }else{
                this.firstPnl.getChildByName('firstBlockPnl').setVisible(false)
                this.firstPnl.getChildByName('acceptedPg').setVisible(false)
            }


            if(this._PublicData.playerVipCode != GameConfig.VIP_LEVEL_0){

                this.dueReminderText.setString(this._PublicData.dueReminderText)

                let isHaveAcceptRewards = false
                if(this._PublicData.playerVipCode < privilege.vipCode){
                    this.acceptBtn.setVisible(false)
                    this.levelLowerBtn.setVisible(true)
                }else if(this._PublicData.playerVipCode == privilege.vipCode){
                    this.levelLowerBtn.setVisible(true)
                    this.acceptBtn.setVisible(true)
                    if(this._PublicData.canVipDaily == 1){//已领取每日奖励
                        isHaveAcceptRewards = true
                        this.acceptBtn.setTouchEnabled(false)
                        this.acceptBtn.setBright(false)
                    }
                }else{
                    isHaveAcceptRewards = true
                    this.levelLowerBtn.setVisible(true)
                    this.acceptBtn.setVisible(true)
                    this.acceptBtn.setTouchEnabled(false)
                    this.acceptBtn.setBright(false)
                }

                if(isHaveAcceptRewards){
                    this.everyDayPnl.getChildByName('everyDayBlockPnl').setVisible(true)
                    this.everyDayPnl.getChildByName('acceptedPg').setVisible(true)
                }else{
                    this.everyDayPnl.getChildByName('everyDayBlockPnl').setVisible(false)
                    this.everyDayPnl.getChildByName('acceptedPg').setVisible(false)
                }

                this.toBeVIPBtn.setVisible(false)

                if(privilege.vipCode == GameConfig.VIP_LEVEL_4){
                    this.renewBtn.setVisible(false)
                    this.renew1Btn.setVisible(true)
                    this.upgradeBtn.setVisible(false)
                }else{
                    this.renewBtn.setVisible(true)
                    this.renew1Btn.setVisible(false)
                    this.upgradeBtn.setVisible(true)

                }


            }else{
                this.everyDayPnl.getChildByName('everyDayBlockPnl').setVisible(false)
                this.everyDayPnl.getChildByName('acceptedPg').setVisible(false)
                this.firstPnl.getChildByName('firstBlockPnl').setVisible(false)
                this.firstPnl.getChildByName('acceptedPg').setVisible(false)

                this.toBeVIPBtn.setVisible(true)
                this.renewBtn.setVisible(false)
                this.renew1Btn.setVisible(false)
                this.upgradeBtn.setVisible(false)
            }

        },

        initRechargeData: function (rechargeList) {

            this._RechargeListData = []
            for(let i = 0; i < rechargeList.length; i++){

                let recharge = rechargeList[i]
                let rechargeData = {}
                rechargeData.vipCode = recharge.vipCode
                rechargeData.rechageBg = recharge.rechageBg
                rechargeData.memberLevelPg = recharge.memberLevelPg
                rechargeData.time = recharge.time
                rechargeData.money = recharge.money

                this._RechargeListData[i] = rechargeData

            }

        },

        initMemberData: function (memberList) {

            this._MemberListData = []
            for(let i = 0; i < memberList.length; i++){

                let mmeber = memberList[i]
                let memberData = {}
                memberData.memberName = mmeber.memberName
                memberData.memberRes = mmeber.res

                this._MemberListData[i] = memberData

            }

        },

        changeTurnBtn: function (nextCode) {


            switch (nextCode) {
                case 0:
                case 1:
                    this.turnNextBtn.setVisible(true)
                    this.turnPreviousBtn.setVisible(false)

                    break
                case 2:
                case 3:
                    this.turnNextBtn.setVisible(true)
                    this.turnPreviousBtn.setVisible(true)

                    break
                case 4:
                    this.turnNextBtn.setVisible(false)
                    this.turnPreviousBtn.setVisible(true)

                    break
                default:
                    this.turnNextBtn.setVisible(true)
                    this.turnPreviousBtn.setVisible(false)

                    break
            }

            this.initTurnCodeData(nextCode)


        },



        onReceiveRewards: function () {

            this.everyDayPnl.getChildByName('everyDayBlockPnl').setVisible(true)
            this.everyDayPnl.getChildByName('acceptedPg').setVisible(true)

        },



    })
    return MemberLayer
})
