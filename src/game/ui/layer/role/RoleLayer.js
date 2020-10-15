
load('game/ui/layer/role/RoleLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let RoleMdt = include('game/ui/layer/role/RoleMdt')
    let GameConfig = include('game/config/GameConfig')
    let GameUtil = include('game/public/GameUtil')
    let GameEvent = include('game/config/GameEvent')
    let AniPlayer = ResConfig.AniPlayer
    let PlayerPlay = ResConfig.PlayerPlay
    let RoleLayer = BaseLayer.extend({
        _className: 'RoleLayer',
        _rolesChoiceBtn_all : 'allRolesBtn',
        _rolesChoiceBtn_my : 'myRolesBtn',

        ctor: function () {
            this._super(ResConfig.View.RoleLayer)
            this.registerMediator(new RoleMdt(this))
        },
        RES_BINDING: function () {
            return {

                'pnl/btnPnl/closeBtn': {onClicked : this.onClseClick},
                'pnl/btnPnl/allRolesBtn': {onClicked : this.onRolesChiceClick},
                'pnl/btnPnl/myRolesBtn': {onClicked : this.onRolesChiceClick},
                'pnl/btnPnl/useBtn': {},
                'pnl/btnPnl/renewBtn': {},
                'pnl/btnPnl/renewMidBtn': {},
                'pnl/btnPnl/useMidBtn': {},
                'pnl/btnPnl/toGetBtn': {},
                'pnl/btnPnl/toBuyBtn': {},

                'pnl/dataPnl/diamondsPnl': {},
                'pnl/dataPnl/diamondsPnl/diamondsAddBtn': {onClicked : this.onCoinShopClick},


                'pnl/dataPnl/coinPnl': {},
                'pnl/dataPnl/coinPnl/coinAddBtn': {onClicked : this.onCoinShopClick},

                'pnl/dataPnl/roleNd': {},
                'pnl/dataPnl/roleName': {},
                'pnl/dataPnl/roleVipName': {},
                'pnl/dataPnl/timeText': {},

                'pnl/roleImageListView': {},
                'pnl/roleImageCell': {},
                'pnl/listPnl': {},






            }
        },
        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()

            this.initData()
            this.initView()
        },
        onExit: function () {
            this._super()

        },
        onClseClick: function () {
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)
        },

        initData: function () {
            this._roleArrayName = this._rolesChoiceBtn_all
        },

        initView: function () {

            this.useBtn.setVisible(false)
            this.renewBtn.setVisible(false)
            this.renewMidBtn.setVisible(false)
            this.useMidBtn.setVisible(false)
            this.toGetBtn.setVisible(false)
            this.toBuyBtn.setVisible(false)
            this.listPnl.setVisible(false)

            this.roleVipName.setVisible(false)
            this.timeText.setVisible(false)
            this.roleImageCell.setVisible(false)



        },

        onCoinShopClick: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.CoinShopLayer)
            this.onClseClick()
        },

        onUpdatePropsData: function (data) {

            let coinsCnt =  this.coinPnl.getChildByName('coinsCnt')
            let diamondsCnt =  this.diamondsPnl.getChildByName('diamondsCnt')

            if(data.hasOwnProperty('coin')){
                coinsCnt.setString(GameUtil.getStringRule(data.coin))
            }

            if(data.hasOwnProperty('diamonds')){
                diamondsCnt.setString(GameUtil.getStringRule(data.diamonds))
            }
        },

        onRolesChiceClick: function (sender) {

            let choiceBtnName
            if(sender == this.allRolesBtn)
                choiceBtnName = this._rolesChoiceBtn_all
            else
                choiceBtnName = this._rolesChoiceBtn_my

            this.onUpdateRolesChoiceType(choiceBtnName)
            this.onUpdateRoleImageListView(choiceBtnName)

        },

        onUpdateRoleImageListView: function (choiceBtnName,showRoleCode) {

            let _rolesData
            if(choiceBtnName == this._rolesChoiceBtn_all){
                _rolesData = this._data.allRoles
            }else{
                _rolesData = this._data.myRoles
            }

            this.updateLeftList(_rolesData,showRoleCode)

        },

        updateLeftList: function (dataList,showRoleCode) {

            this.roleImageListView.removeAllChildren()

            let listNum = Math.ceil(dataList.length / 2)
            for(let i = 0; i < listNum; i++){
                let listPnl = this.listPnl.clone()
                listPnl.setVisible(true)
                this.roleImageListView.pushBackCustomItem(listPnl)
                for (let cellIndex = 0; cellIndex < 2; ++cellIndex) {
                    let dataIndex = 2* i + cellIndex
                    let cellData = dataList[dataIndex]
                    if (cellData) {
                        this.initRoleCell(listPnl, cellIndex, dataList[dataIndex],cellData.roleCode == showRoleCode)
                    }
                }

            }
        },

        onReceiveRoleSelectedResule: function (data) {

            let roleCode = data.roleCode
            let parmList = [
                'allRoles',
                'myRoles'
            ]
            this.onUpdateRoleStatus(parmList,roleCode,2)
            this.onUpdateRoleImageListView(this._roleArrayName,roleCode)

        },

        onUpdateRoleStatus: function(parmList,roleCode,status){

            let step1 = false
            let step2 = false
            for(let key in parmList){
                for(let i = 0; i < this._data[parmList[key]].length; i++){

                    let role = this._data[parmList[key]][i]

                    if(!role){
                        continue
                    }

                    if(role.status == 2){
                        role.status = 1
                        step1 = true
                    }


                    if(role.roleCode == roleCode){
                        if(role.status == 1){
                            role.status = status
                            step2 = true
                        }
                    }

                    if(step1 && step2)
                        break

                }
            }



        },


        initRoleCell: function (listPnl, cellIndex,cellData,isSelected) {

            let cell = this.roleImageCell.clone()
            listPnl.addChild(cell)

            cell.setVisible(true)
            cell.setPositionY(0)
            cell.setPositionX(130 * cellIndex)

            let status = cellData.status
            switch (status) {//0:未拥有1.已拥有2.已出战
                case 0:
                    cell.getChildByName('brightPg').setVisible(false)
                    cell.getChildByName('roleBrightPg').setVisible(false)
                    cell.getChildByName('usedPg').setVisible(false)
                    cell.getChildByName('ashPg').setVisible(true)
                    cell.getChildByName('roleAshPg').loadTexture(cellData.ashImg)

                    break
                case 1:
                case 2:
                    cell.getChildByName('ashPg').setVisible(false)
                    cell.getChildByName('roleAshPg').setVisible(false)
                    if(status == 1)
                        cell.getChildByName('usedPg').setVisible(false)
                    cell.getChildByName('brightPg').setVisible(true)
                    cell.getChildByName('roleBrightPg').loadTexture(cellData.brightImg)

                    break
                default:
                    break
            }

            if(isSelected){

                this.onUpdateRoleDetailData(cellData)

            }

            cell.addClickEventListener(function(sender, et) {
                this.onRoleClick(cellData)
            }.bind(this))

        },

        onRoleClick: function (sender) {

            this.onUpdateRoleDetailData(sender)

        },

        onUpdateRoleDetailData: function (cellData) {

            let getType = cellData.get_type//获得的大类型1.登录免费赠送2.货币购买3.vip赠送'
            let status= cellData.status//0:未拥有1.已拥有2.已出战

            this.roleName.setString(cellData.roleName)
            this.roleNd.removeAllChildren()
            let ani = appInstance.gameAgent().gameUtil().getAni(AniPlayer[cellData.roleCode])
            this.roleNd.addChild(ani)
            ani.setPosition(cc.p(0,0))
            ani.setScale(0.35)
            ani.setAnimation(0, PlayerPlay.stand, true)

            switch (getType) {
                case 1:

                    this.roleVipName.setVisible(false)
                    this.useBtn.setVisible(false)
                    this.renewBtn.setVisible(false)
                    this.renewMidBtn.setVisible(false)
                    this.toGetBtn.setVisible(false)
                    this.toBuyBtn.setVisible(false)

                    if(status == 1){
                        this.useMidBtn.setVisible(true)
                        this.useMidBtn.addClickEventListener(function (sender,et) {
                            this.onUsedClicked(cellData)
                        }.bind(this))
                    }else{
                        this.useMidBtn.setVisible(false)
                    }


                    this.timeText.setVisible(true)
                    this.timeText.setString(cellData.dueReminderText)

                    break
                case 2:
                case 3:

                    if(status == 0){
                        this.onChangeRoleDetailForNoBuy(cellData)
                    }else{
                        this.onChangeRoleDetailForBuy(cellData)
                    }
                    break
                default:
                    break

            }

        },

        //角色出战按钮点击
        onUsedClicked: function(cellData){

            let msg = {}
            msg.roleCode = cellData.roleCode
            appInstance.gameAgent().httpGame().ROLESELECTEDReq(msg)

        },

        onChangeRoleDetailForNoBuy: function (cellData) {

            let getType = cellData.get_type//获得的大类型1.登录免费赠送2.货币购买3.vip赠送'


            this.useBtn.setVisible(false)
            this.renewBtn.setVisible(false)
            this.renewMidBtn.setVisible(false)
            this.useMidBtn.setVisible(false)

            let buyBtn
            if(getType == 2){
                this.roleVipName.setVisible(false)
                this.toBuyBtn.setVisible(true)
                this.toGetBtn.setVisible(false)
                this.roleVipName.setVisible(false)
                this.timeText.setVisible(false)
                buyBtn = this.toBuyBtn
            }else{

                this.toBuyBtn.setVisible(false)
                this.toGetBtn.setVisible(true)
                this.roleVipName.setVisible(true)
                this.roleVipName.setString(cellData.levelName)
                this.timeText.setVisible(true)
                this.timeText.setString(cellData.dueReminderText)
                buyBtn = this.toGetBtn
            }


            buyBtn.addClickEventListener(function (sender,et) {
                this.onBuyRoleClicked(cellData)
            }.bind(this))

        },


        onChangeRoleDetailForBuy: function (cellData) {

            let status= cellData.status//0:未拥有1.已拥有2.已出战

            this.toBuyBtn.setVisible(false)
            this.toGetBtn.setVisible(false)
            this.useMidBtn.setVisible(false)

            this.roleVipName.setVisible(false)
            this.timeText.setVisible(true)
            this.timeText.setString(cellData.dueReminderText)


            let buyBtn
            let useBtn

            switch (status) {
                case 1:

                    this.renewMidBtn.setVisible(false)
                    this.useBtn.setVisible(true)
                    this.renewBtn.setVisible(true)
                    useBtn = this.useBtn
                    buyBtn = this.renewBtn
                    break
                case 2:

                    this.renewMidBtn.setVisible(true)

                    this.useBtn.setVisible(false)
                    this.renewBtn.setVisible(false)
                    useBtn = this.useBtn
                    buyBtn = this.renewMidBtn

                    break
                default:
                    break

            }

            useBtn.addClickEventListener(function (sender,et) {
                this.onUsedClicked(cellData)
            }.bind(this))
            buyBtn.addClickEventListener(function (sender,et) {
                this.onBuyRoleClicked(cellData)
            }.bind(this))

        },

        //购买角色按钮点击
        onBuyRoleClicked: function(cellData){
            cc.log('-------------------------------onBuyRoleClicked------------------------------')

            let getType = cellData.get_type


            switch (getType) {
                case 2:

                    let num = cellData.num
                    let propName
                    let propNum
                    let dialogMsg = {
                        ViewType: 1,
                        LeftBtnName: '取 消',
                    }



                    if(cellData.code == GameConfig.propType_currency_coin){

                        propName = '金币'
                        propNum = this.coinPnl.getChildByName('coinsCnt').getString()

                    }else if(cellData.code == GameConfig.propType_currency_diamonds){

                        propName = '钻石'
                        propNum = this.diamondsPnl.getChildByName('diamondsCnt').getString()

                    }else{
                        break
                    }

                    if(propNum < num) {

                        dialogMsg.TileName = '提 示'
                        dialogMsg.RightBtnName = '去领取'
                        dialogMsg.RightBtnClick = function () {
                            appInstance.gameAgent().addPopUI(ResConfig.Ui.MemberLayer)
                            appInstance.uiManager().removeUI(this)
                        }.bind(this)

                        dialogMsg.SayText = '您的' + propName + '不足'


                    }else {

                        dialogMsg.TileName = '购 买'
                        dialogMsg.RightBtnName = '购 买'
                        dialogMsg.RightBtnClick = function () {
                            let msg = {}
                            msg.getId = cellData.getId
                            appInstance.gameAgent().httpGame().BUYROLEReq(msg)
                        }

                        dialogMsg.SayText = '您确定花费' + num + propName + '购买此角色吗'

                    }

                    appInstance.gameAgent().addDialogUI(dialogMsg)


                    break
                case 3://跳转到会员充值界面
                    appInstance.gameAgent().addPopUI(ResConfig.Ui.MemberLayer)
                    break
                default:
                    break

            }



        },

        onAddDialogUI: function (dialogMsg){

            appInstance.gameAgent().addDialogUI(dialogMsg)
        },


        onUpdateRolesChoiceType: function (choiceBtnName) {

            let choiceBtn
            let otherBtn
            switch (choiceBtnName) {
                case this._rolesChoiceBtn_all:
                    choiceBtn = this.allRolesBtn
                    otherBtn = this.myRolesBtn
                    this._roleArrayName = this._rolesChoiceBtn_all
                    break
                case this._rolesChoiceBtn_my:
                    choiceBtn = this.myRolesBtn
                    otherBtn = this.allRolesBtn
                    this._roleArrayName = this._rolesChoiceBtn_my
                    break
                default:
                    choiceBtn = this.allRolesBtn
                    otherBtn = this.myRolesBtn
                    break
            }

            choiceBtn.setBright(false)
            choiceBtn.setTouchEnabled(false)
            otherBtn.setBright(true)
            otherBtn.setTouchEnabled(true)

        },

        onInitRolesData: function (data) {

            this._data = {}
            this._data.allRoles = data.allRoles
            this._data.myRoles = data.myRoles
            let showRoleCode = data.showRoleCode

            cc.log('----------------------------------this._roleArrayName : ' + this._roleArrayName)
            this.onUpdateRolesChoiceType(this._roleArrayName)
            this.onUpdateRoleImageListView(this._roleArrayName,showRoleCode)
        },


    })
    return RoleLayer
})
