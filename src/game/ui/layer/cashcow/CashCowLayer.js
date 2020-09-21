load('game/ui/layer/cashcow/CashCowLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let CashCowMdt = include('game/ui/layer/cashcow/CashCowMdt')
    let cashCowLayer = BaseLayer.extend({
        _className: 'cashCowLayer',
        ctor: function () {
            this._super(ResConfig.View.CashCowLayer)
            this.registerMediator(new CashCowMdt(this))
            this.registerEventListener('rewardVideoCallback', this.onRewardVideoCallback)

        },
        RES_BINDING: function () {
            return {
                'pnl/btnPnl/closeBtn': {onClicked: this.onCloseClick},
                'pnl/btnPnl/shakeBtn': {onClicked: this.onShakeClick},
                'pnl/btnPnl/shakeCancleBtn': {},
                'pnl/btnPnl/recordBtn': {onClicked: this.onRecordClick},
                'pnl/btnPnl/cointreeNd': {},
                'pnl/poupPnl/propPnl': {onClicked: this.onHideShowPropPnlClick},
                'pnl/poupPnl/recordsPnl': {},
                'pnl/poupPnl/recordsPnl/dataListPnl': {},
                'pnl/poupPnl/recordsPnl/recordDataCell': {},
                'pnl/poupPnl/recordsPnl/recordCloseBtn': {onClicked: this.onHideRecordPnlClick}
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

            this._coinTreeAni = null

        },

        initView: function () {
            this.recordDataCell.setVisible(false)
            this.propPnl.setVisible(false)
            this.recordsPnl.setVisible(false)

            this._coinTreeAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.YaoJinShu)
            this.cointreeNd.addChild(this._coinTreeAni)
            this._coinTreeAni.setAnimation(0, 'animation', true)


            let cashCowNum = appInstance.dataManager().getUserData().cashCowNum
            let usedCashCowNum = appInstance.dataManager().getUserData().usedCashCowNum


            if (usedCashCowNum < cashCowNum) {

                this.shakeBtn.setVisible(true)
                this.shakeCancleBtn.setVisible(false)

            } else {

                this.shakeBtn.setVisible(false)
                this.shakeCancleBtn.setVisible(true)

            }


        },

        onRefreshView: function (data) {

            let cashCowNum = appInstance.dataManager().getUserData().cashCowNum
            let usedCashCowNum = appInstance.dataManager().getUserData().usedCashCowNum

            if (usedCashCowNum >= cashCowNum) {

                this.shakeBtn.setVisible(false)
                this.shakeCancleBtn.setVisible(true)

            }

            this.onShowPropPnl(data)

        },

        showView: function () {
            //  this.onShowRecordPnlClick()
        },


        onCloseClick: function () {
            appInstance.uiManager().removeUI(this)
        },

        onShakeClick: function () {
            this.shakeBtn.setTouchEnabled(false)
            if (cc.sys.OS_ANDROID === cc.sys.os) {
                appInstance.nativeApi().showRewardVideo()
            }
        },
        onRewardVideoCallback: function (msg) {
            if (msg == "0") {
                this._coinTreeAni.setAnimation(0, 'animation2', false)
                let deleAction = cc.DelayTime(10)
                let callBack = function () {
                    this._coinTreeAni.setAnimation(0, 'animation', true)
                    let msg = {}
                    appInstance.gameAgent().httpGame().cashCowReq(msg)
                    this.shakeBtn.setTouchEnabled(true)
                }.bind(this)
                //this.cointreeNd.runAction(cc.sequence(deleAction,cc.CallFunc(callBack)))
                this.cointreeNd.runAction(cc.sequence(cc.CallFunc(callBack)))
            } else {
                this.shakeBtn.setTouchEnabled(true)
            }
        },
        onShowPropPnl: function (data) {

            let coinsVal = 'x' + data.coin
            this.propPnl.getChildByName('coinsVal').setString(coinsVal)
            this.propPnl.setVisible(true)


        },

        onHideShowPropPnlClick: function () {

            this.propPnl.setVisible(false)

        },

        onRecordClick: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().cashCowRecordReq()

        },

        onShowRecordPnlClick: function (data) {


            this.dataListPnl.removeAllChildren()
            for (let i = 0; i < data.logList.length; i++) {
                this.onUpdateDataCell(data.logList, i)
            }

            this.recordsPnl.setVisible(true)

        },

        onUpdateDataCell: function (list, index) {
            let recordCell = this.recordDataCell.clone()
            recordCell.setVisible(true)
            this.dataListPnl.addChild(recordCell)

            if (Math.floor(index % 2)) {
                recordCell.getChildByName('bg').setVisible(false)
            } else {
                recordCell.getChildByName('bg').setVisible(true)
            }

            let record = list[index]
            recordCell.getChildByName('timeText').setString(this.onFormatDateTime(record.createTime))
            recordCell.getChildByName('coinsText').setString(record.propNum)
        },

        onHideRecordPnlClick: function () {

            this.recordsPnl.setVisible(false)

        },

        onFormatDateTime: function (timestamp) {

            let d = new Date(parseInt(timestamp));
            let month = (d.getMonth() + 1) < 10 ? (0 + "" + (d.getMonth() + 1)) : (d.getMonth() + 1);
            let day = d.getDate() < 10 ? (0 + "" + d.getDate()) : d.getDate();
            let hour = d.getHours() < 10 ? (0 + "" + d.getHours()) : d.getHours();
            let minute = d.getMinutes() < 10 ? (0 + "" + d.getMinutes()) : d.getMinutes();
            let second = d.getSeconds() < 10 ? (0 + "" + d.getSeconds()) : d.getSeconds();
            let dateString = d.getFullYear() + "-" + month + "-" + day + " " + hour + ": " + minute

            return dateString;

        }
    })
    return cashCowLayer
})
