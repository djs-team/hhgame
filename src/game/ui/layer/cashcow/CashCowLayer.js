load('game/ui/layer/cashcow/CashCowLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let CashCowMdt = include('game/ui/layer/cashcow/CashCowMdt')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let AniPlayer = ResConfig.AniPlayer
    let PlayerPlay = ResConfig.PlayerPlay
    let cashCowLayer = BaseLayer.extend({
        _className: 'cashCowLayer',
        ctor: function () {
            appInstance.gameAgent().hideLoading()
            this._super(ResConfig.View.CashCowLayer)
            this.registerMediator(new CashCowMdt(this))
            this.registerEventListener('rewardVideoCallback', this.onRewardVideoCallback)

        },
        RES_BINDING: function () {
            return {

                'popupBlock': {},

                'pnl/pgPnl/aniNd': {},

                'pnl/btnPnl/closeBtn': {onClicked: this.onCloseClick},
                'pnl/btnPnl/shakeBtn': {onClicked: this.onShakeClick},
                'pnl/btnPnl/recordBtn': {onClicked: this.onRecordClick},
                'pnl/btnPnl/explainBtn': {onClicked: this.onExplainClick},
                'pnl/btnPnl/cointreeNd': {},

                'pnl/recordsPnl': {},
                'pnl/recordsPnl/dataListPnl': {},
                'pnl/recordsPnl/recordDataCell': {},
                'pnl/recordsPnl/recordCloseBtn': {onClicked: this.onHideRecordPnlClick},
                'pnl/rulePnl': {},
                'pnl/rulePnl/ruleCloseBtn': {onClicked: this.onHideRulePnlClick},
            }
        },

        onCreate: function () {
            this._super()
        },

        onEnter: function () {
            this._super()
        },

        onExit: function () {
            this._super()
        },

        onExplainClick: function () {

            this.popupBlock.setVisible(true)
            this.rulePnl.setVisible(true)
        },

        initData: function (data) {

            this._coinTreeAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.YaoJinShu)
            this.cointreeNd.addChild(this._coinTreeAni)
            this._coinTreeAni.setAnimation(0, 'animation', true)

            this.aniNd.removeAllChildren()
            let ani = appInstance.gameAgent().gameUtil().getAni(AniPlayer[data])
            this.aniNd.addChild(ani)
            ani.setPosition(cc.p(0,0))
            ani.setScale(0.4)
            ani.setAnimation(0, PlayerPlay.stand, true)
        },

        initView: function (data) {

            this.initData(data)
            this.dataListPnl.setScrollBarEnabled(false)
            this.recordDataCell.setVisible(false)
            this.recordsPnl.setVisible(false)
            this.rulePnl.setVisible(false)
            this.popupBlock.setVisible(false)

            let cashCowNum = appInstance.dataManager().getUserData().cashCowNum
            let usedCashCowNum = appInstance.dataManager().getUserData().usedCashCowNum

            let flag = false
            if (usedCashCowNum < cashCowNum)
                flag = true

            this.shakeBtn.setBright(flag)
            this.shakeBtn.setTouchEnabled(flag)

        },

        onRefreshView: function () {

            let cashCowNum = appInstance.dataManager().getUserData().cashCowNum
            let usedCashCowNum = appInstance.dataManager().getUserData().usedCashCowNum

            let flag = false
            if (usedCashCowNum < cashCowNum)
                flag = true

            this.shakeBtn.setBright(flag)
            this.shakeBtn.setTouchEnabled(flag)


        },


        onCloseClick: function () {
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)
        },

        onShakeClick: function () {
            this.shakeBtn.setTouchEnabled(false)
            if(cc.sys.os !== cc.sys.OS_WINDOWS)
                appInstance.nativeApi().showRewardVideo()
            else
                appInstance.gameAgent().httpGame().cashCowReq()

        },
        onRewardVideoCallback: function (msg) {
            if (msg == "0") {
                this._coinTreeAni.setAnimation(0, 'animation2', false)
                let deleAction = cc.DelayTime(10)
                let callBack = function () {
                    this._coinTreeAni.setAnimation(0, 'animation', true)
                    let msg = {}
                    appInstance.gameAgent().httpGame().cashCowReq(msg)
                    //this.shakeBtn.setTouchEnabled(true)
                }.bind(this)
                //this.cointreeNd.runAction(cc.sequence(deleAction,cc.CallFunc(callBack)))
                this.cointreeNd.runAction(cc.sequence(cc.CallFunc(callBack)))
            } else {
                this.shakeBtn.setTouchEnabled(true)
            }
        },


        onRecordClick: function (sender) {
            GameUtil.delayBtn(sender);
            appInstance.gameAgent().httpGame().cashCowRecordReq()

        },

        onShowRecordPnlClick: function (data) {


            this.dataListPnl.removeAllChildren()
            for (let i = 0; i < data.logList.length; i++) {
                this.onUpdateDataCell(data.logList, i)
            }

            this.popupBlock.setVisible(true)
            this.recordsPnl.setVisible(true)

        },

        onUpdateDataCell: function (list, index) {
            let recordCell = this.recordDataCell.clone()
            recordCell.setVisible(true)
            this.dataListPnl.addChild(recordCell)

            if (Math.floor(index % 2)) {
                recordCell.getChildByName('bg').setVisible(true)
            } else {
                recordCell.getChildByName('bg').setVisible(false)
            }

            let record = list[index]
            recordCell.getChildByName('timeText').setString(this.onFormatDateTime(record.createTime))
            recordCell.getChildByName('coinsText').setString(record.propNum)
        },

        onHideRecordPnlClick: function () {
            this.popupBlock.setVisible(false)
            this.recordsPnl.setVisible(false)

        },

        onHideRulePnlClick: function () {
            this.popupBlock.setVisible(false)
            this.rulePnl.setVisible(false)
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

        }
    })
    return cashCowLayer
})
