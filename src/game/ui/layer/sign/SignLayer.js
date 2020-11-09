load('game/ui/layer/sign/SignLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let SignMdt = include('game/ui/layer/sign/SignMdt')
    let GameEvent = include('game/config/GameEvent')
    let LocalSave = include('game/public/LocalSave')
    let GameUtil = include('game/public/GameUtil')
    let SignLayer = BaseLayer.extend({
        _className: 'signLayer',
        ctor: function () {
            appInstance.gameAgent().hideLoading()
            this._super(ResConfig.View.SignLayer)
            this.registerMediator(new SignMdt(this))
            this.registerEventListener('rewardVideoCallback', this.onRewardVideoCallback)
        },
        RES_BINDING: function () {
            return {

                'pnl/btnPnl/becomeBtn': {onClicked: this.onGoVIPBtnClicked},
                'pnl/btnPnl/upgradeBtn': {onClicked: this.onGoVIPBtnClicked},
                'pnl/btnPnl/renewBtn': {onClicked: this.onGoVIPBtnClicked},

                'pnl/btnPnl/mulitAcceptBtn': {onClicked: this.onVideoAcceptClicked},
                'pnl/btnPnl/ordinaryAcceptPnl': {onClicked: this.onOrdinaryAcceptClicked},
                'pnl/btnPnl/explainBtn': {onClicked: this.onExplainClick},
                'pnl/btnPnl/cancleBtn': {onClicked: this.onCloseClick},

                'pnl/loadingBarPnl/loadingBar': {},
                'pnl/loadingBarPnl/loadingBar/treasureChestCell': {},

                'pnl/signDataList': {},
                'pnl/signDataListCell': {},
                'pnl/signDataCell': {},

                'roleBlock': {},
                'poupPnl': {},
                'poupPnl/rulePnl': {},
                'poupPnl/rulePnl/ruleCloseBtn': {onClicked: this.onHideRulePnlClick},


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

            this._checkInName = 'checkInName_'
            this._treasureChestName = 'treasureChestName_'
            this._listPnlName = 'listPnlName_'
            this._maxSignNum = 30

        },

        initView: function () {

            this.signDataList.setScrollBarEnabled(false)

            this.becomeBtn.setVisible(false)
            this.upgradeBtn.setVisible(false)
            this.renewBtn.setVisible(false)
            this.treasureChestCell.setVisible(false)
            this.signDataCell.setVisible(false)
            this.signDataListCell.setVisible(false)
            this.poupPnl.setVisible(false)
            this.roleBlock.setVisible(false)
            this.rulePnl.setVisible(false)

            this.ordinaryAcceptPnl.getChildByName('singleAcceptText').setVisible(false)


            let signDate = global.localStorage.getStringForKey(LocalSave.SignDate)
            let dateStr = global.getCurDayStr()
            if (!signDate || signDate !== dateStr) {
                appInstance.gameAgent().gameUtil().autoPlaySound(ResConfig.Sound.qiandao)
                global.localStorage.setStringForKey(LocalSave.SignDate, dateStr)
            }
        },

        onExplainClick: function (sender) {
            GameUtil.delayBtn(sender);
            this.poupPnl.setVisible(true)
            this.roleBlock.setVisible(true)
            this.rulePnl.setVisible(true)
        },

        onHideRulePnlClick: function () {
            this.poupPnl.setVisible(false)
            this.roleBlock.setVisible(false)
            this.rulePnl.setVisible(false)
        },

        showView: function () {

        },

        onCloseClick: function () {
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)
        },

        onInitSignDatas: function (data) {

            let btnNameArray = {
                becomeBtn: this.becomeBtn,
                upgradeBtn: this.upgradeBtn,
                renewBtn: this.renewBtn

            }
            btnNameArray[data.showBtnName].setVisible(true)


            if (data.vipCode != 0) {
                this.ordinaryAcceptPnl.getChildByName('vipLevelText').setVisible(true)
                this.ordinaryAcceptPnl.getChildByName('mulitAcceptText').setVisible(true)
                this.ordinaryAcceptPnl.getChildByName('singleAcceptText').setVisible(false)

                this.ordinaryAcceptPnl.getChildByName('vipLevelText').setString(data.vipLevel)
                this.ordinaryAcceptPnl.getChildByName('mulitAcceptText').setString(data.mulitAcceptText)
            } else {
                this.ordinaryAcceptPnl.getChildByName('vipLevelText').setVisible(false)
                this.ordinaryAcceptPnl.getChildByName('mulitAcceptText').setVisible(false)
                this.ordinaryAcceptPnl.getChildByName('singleAcceptText').setVisible(true)
            }

            this.mulitAcceptBtn.getChildByName('videoAcceptText').setString(data.videoAcceptText)

            this.onUpdataReceiveFlag(data.flag)
            if (this._flag == 1)
                this._checkinId = data.checkinId
            else
                this._checkinId = data.checkinId - 1
            this.currentSign = data.currentSign

            this.onUpdateProCessBar(data.currentSign, this._maxSignNum)
            this.onInitCheckinListDatas(data.checkinList)
            this.onInitBonusConfigListDatas(data.bonusConfigList)

        },

        onVideoAcceptClicked: function (sender) {
            GameUtil.delayBtn(sender);
            if (this.isCanReceiveRewards()) {
                appInstance.gameAgent().Tips('已领取，请勿重复领取！')
                return
            }

            appInstance.nativeApi().showRewardVideo()

        },
        onRewardVideoCallback: function (msg) {
            if (msg == "0") {
                this.onAcceptSignAwards(0)
            } else {
                this.shakeBtn.setTouchEnabled(true)
            }
        },

        onOrdinaryAcceptClicked: function (sender) {
            GameUtil.delayBtn(sender);
            this.onAcceptSignAwards(1)

        },

        onAcceptSignAwards: function (isWatch) {

            if (this.isCanReceiveRewards()) {
                appInstance.gameAgent().Tips('已领取，请勿重复领取！')
                return
            }

            let msg = {}
            msg.checkinId = this._checkinId
            msg.watch = isWatch
            msg.type = 0

            this.onAcceptAwards(msg)

        },

        onAcceptTreasureChestAwards: function (_checkinId) {


            let msg = {}
            msg.checkinId = _checkinId
            msg.type = 1
            msg.watch = 1
            this.onAcceptAwards(msg)

        },

        onAcceptAwards: function (msg) {


            msg = msg || {}
            appInstance.gameAgent().httpGame().CHECKINReq(msg)


        },

        //初始化签到列表
        onInitCheckinListDatas: function (data) {

            for (let i = 0; i < 5; i++) {

                let listPnl = this.signDataListCell.clone()
                listPnl.setName(this._listPnlName + i)
                listPnl.setVisible(true)



                this.signDataList.pushBackCustomItem(listPnl)

                for (let j = 0; j < 6; j++) {
                    this.onInitCheckInCellData(data,6*i+j,listPnl,i)
                }
            }
        },


        onInitCheckInCellData: function (data, index, listPnl, i) {

            let cellData = data[index]
            let cell = this.signDataCell.clone()
            listPnl.addChild(cell)
            
            cell.setPositionY(0)

            cell.setPositionX( 55 + index%6*90 - i * 13)

            cell.setVisible(true)
            cell.setName(this._checkInName + cellData.checkinId)
            cell.getChildByName('awardsPg').loadTexture(cellData.res)
            cell.getChildByName('awardsValueText').setString(cellData.num)
            if (cellData.status != 2) {
                cell.getChildByName('acceptedPg').setVisible(false)
                cell.getChildByName('blockPg').setVisible(false)
            }

        },


        //初始化宝箱数据
        onInitBonusConfigListDatas: function (data) {


            for (let i = 0; i < data.length; i++) {

                this.onInitBonusConfigData(data[i])

            }

        },


        onInitBonusConfigData: function (data) {

            let cell = this.treasureChestCell.clone()
            this.loadingBar.addChild(cell)

            cell._data = {
                'status': data.status,
                'id': data.id,
            }

            cell.setVisible(true)
            cell.getChildByName('treasureChestExplainPg').setVisible(false)
            cell.setName(this._treasureChestName + data.id)
            cell.getChildByName('signDaysText').setString(data.signDaysText)
            cell.getChildByName('loadingBarAwardsPg').loadTexture(data.res)
            cell.getChildByName('loadingBarAwardsText').setString(data.loadingBarAwardsText)
            cell.getChildByName('treasureChestExplainPg').getChildByName('treasureChestExplainText').setString(data.bounsDesc)
            if(data.showAniNd){
                let boLiAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.DatingQianDao)
                boLiAni.setAnimation(0, 'animation1', true)
                cell.getChildByName('boxAniNd').addChild(boLiAni)
                cell.getChildByName('boxAniNd').setVisible(true)
            }else{
                cell.getChildByName('boxAniNd').setVisible(false)
            }


            // let barPositionX = this.loadingBar.getPositionX() + (data.id / this._maxSignNum * 465.00)
            let barPositionX = this.loadingBar.getPositionX() - 10 + (data.id / this._maxSignNum * 450.00)
            let barPositionY = this.loadingBar.getPositionY()
            cell.setPositionX(barPositionX)
            cell.setPositionY(barPositionY)

            cell.addTouchEventListener(function (sender, et) {

                this.onTreasureChestTouch(sender, et)

            }.bind(this))

        },

        onUpdateProCessBar: function (currentProcess, allProcess) {

            this.loadingBar.setPercent(Math.round(currentProcess / allProcess * 100))

        },

        onTreasureChestTouch: function (sender, et) {

            let data = sender._data

            switch (et) {
                case 0:
                    break
                case 1:
                    sender.getChildByName('treasureChestExplainPg').setVisible(true)
                    break
                case 2:
                    switch (data.status) {
                        case 0:
                            appInstance.gameAgent().Tips('尚未满足要求，请继续完成任务')
                            break
                        case 1:
                            this.onAcceptTreasureChestAwards(data.id)
                            break
                        default:
                            break
                    }
                    sender.getChildByName('treasureChestExplainPg').setVisible(false)
                    break
                case 3:
                    sender.getChildByName('treasureChestExplainPg').setVisible(false)
                    break
            }

        },

        onReceiveAwardsData: function (data) {


            this.onUpdataReceiveFlag(0)

            this.currentSign += 1
            this.onUpdateProCessBar(this.currentSign, this._maxSignNum)
            if(data.type == 0)
                this.onUpdateSignData(data)


            if(data.hasOwnProperty('canUpdateBox') && data.canUpdateBox)
                this.onUpdateTreasureChestData(data)


        },

        onUpdateTreasureChestData: function (data) {

            let cell = this.loadingBar.getChildByName(this._treasureChestName + data.checkinId)
            cell.getChildByName('loadingBarAwardsPg').loadTexture(data.treasureChestData.res)
            cell.getChildByName('loadingBarAwardsText').setString(data.treasureChestData.loadingBarAwardsText)

            if(data.treasureChestData.showAniNd){
                cell._data.status = 1
                let boLiAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.DatingQianDao)
                boLiAni.setAnimation(0, 'animation1', true)
                cell.getChildByName('boxAniNd').addChild(boLiAni)
                cell.getChildByName('boxAniNd').setVisible(true)
            }else{
                cell.getChildByName('boxAniNd').setVisible(false)
            }

        },

        onUpdateSignData: function (data) {

            let cell = this.signDataList.getChildByName(this._listPnlName + (parseInt(data.checkinId / 6))).getChildByName(this._checkInName + data.checkinId)
            cell.getChildByName('acceptedPg').setVisible(true)
            cell.getChildByName('blockPg').setVisible(true)

        },


        isCanReceiveRewards: function () {

            if (this._flag == 0)
                return true
            else
                return false

        },

        onUpdataReceiveFlag: function (flag) {
            this._flag = flag
        },

        onGoVIPBtnClicked: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.MemberLayer)
            this.onCloseClick()

        },
    })
    return SignLayer
})
