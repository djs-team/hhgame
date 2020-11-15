
/**
 * 跑马灯
 */
load('game/ui/layer/marquee/MarqueeLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let MarqueeLayerMdt = include('game/ui/layer/marquee/MarqueeLayerMdt')
    let MarqueeLayer = BaseLayer.extend({
        _className: 'MarqueeLayer',
        _TxtLen: 26,
        _checkTime: 0,
        _DelayTime: 5,
        RES_BINDING: function () {
            return {
                'BgPnl': {  },
                'BgPnl/SayPnl': {  },
                'BgPnl/SayPnl/SayText': { }
            }
        },
        ctor: function () {
            this._super(ResConfig.View.MarqueeLayer)
            this.registerMediator(new MarqueeLayerMdt(this))
            this.initData()
        },

        httpMsgBack: function(msg) {
            let broadList = msg.broadList
            if (!broadList || !broadList.length){
                return
            }
            this._marqueeArry = []
            broadList.sort(function(a,b) {
                return a.grade > b.grade
            })
            for (let i = 0; i < broadList.length; ++i){
                let item = broadList[i]
                item._alreadyTimes = 0
                this._marqueeArry.push(item)
            }

            this.onUpdate(20)
        },

        tcpMsgBack: function(msg) {
            this._marqueeArry = []
            msg._alreadyTimes = 0
            this._marqueeArry.push(msg)
            this.onUpdate(20)
        },

        initData: function () {
            this._pnlSize = this.SayPnl.getContentSize()
            this._pnlLen = this._pnlSize.width

            this._isCanPlay = true
            this._isPlayIng = false


            this._marqueeArry = []
            this._curMarQueeIndex = 0;
        },

        /**
         * 游戏每帧更新的结构
         * @param dt
         */
        onUpdate: function (dt) {
            this._checkTime += dt
            if (this._checkTime > this._DelayTime) {
                this._checkTime = 0
                this.playMsg()
            }
        },

        playMsg: function () {

            if (!this._isCanPlay) {
                return
            }

            if (this._isPlayIng) {
                return
            }

            if (!this._marqueeArry.length) {
                return
            }
            let info = this._marqueeArry[this._curMarQueeIndex]

            if (!info) {
                if (this._curMarQueeIndex >= this._marqueeArry.length) {
                    this._curMarQueeIndex = 0
                }
                return
            }

            if (info.grade === 10000) {
                this._isCanPlay = false
                this.SayText.stopAllActions()
                appInstance.uiManager().removeUI(this)
            }

            let nowDate = Date.parse(new Date())

            if (nowDate < info.beginTime) {
                this._curMarQueeIndex += 1
                this.playMsg()
                return
            }
            if (nowDate > info.endTime) {
                this._marqueeArry.splice(this._curMarQueeIndex, 1)
                this.playMsg()
                return
            }
            if (info._alreadyTimes >= info.showCount) {
                this._marqueeArry.splice(this._curMarQueeIndex, 1)
                this.playMsg()
                return
            }

            this._DelayTime = info.delayTime

            info._alreadyTimes += 1

            this.ShowMsg(info.messageText)
        },

        ShowMsg: function (txt) {
            this.SayPnl.setVisible(true)
            this.SayText.stopAllActions()
            this.SayText.setString(txt)
            this.SayText.ignoreContentAdaptWithSize(true)
            let txtSize = this.SayText.getContentSize()
            let txtLen = txtSize.width
            let moveLen = this._pnlLen + txtLen
            this.SayText.setPositionX(-1 * txtLen)
            let moveTime = moveLen / 900 * 5
            this._isPlayIng = true
            this.SayText.runAction(cc.sequence(cc.moveTo( moveTime, cc.p( moveLen,30)), cc.CallFunc(function(){
                this._curMarQueeIndex += 1
                this.SayPnl.setVisible(false)
                this._isPlayIng = false
            }.bind(this))))
        },
        onEnter: function () {
            this._super()
            this.SayPnl.setVisible(false)
            appInstance.gameAgent().httpGame().marqueeSe()
        },
        onExit: function () {
            this._super()
        }
    })
    return MarqueeLayer
})
