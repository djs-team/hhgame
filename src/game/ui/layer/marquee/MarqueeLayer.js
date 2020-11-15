
/**
 * 系统提示tips
 */
load('game/ui/layer/marquee/MarqueeLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let MarqueeLayerMdt = include('game/ui/layer/marquee/MarqueeLayerMdt')
    let MarqueeLayer = BaseLayer.extend({
        _className: 'MarqueeLayer',
        _TxtLen: 26,
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

        initData: function () {
            this._pnlSize = this.SayPnl.getContentSize()
            this._pnlLen = this._pnlSize.width
        },

        ShowMsg: function () {
            this.SayPnl.setVisible(true)
            this.SayText.stopAllActions()
            this.SayText.setString('这是一个跑马灯真的啊')
            this.SayText.ignoreContentAdaptWithSize(true)
            let txtSize = this.SayText.getContentSize()
            let txtLen = txtSize.width
            let moveLen = this._pnlLen + txtLen
            this.SayText.setPositionX(-1 * txtLen)
            let moveTime = moveLen / 900 * 5

            this.SayText.runAction(cc.sequence(cc.moveTo( moveTime, cc.p( moveLen,30)), cc.CallFunc(function(){
                cc.log('======action end')
                this.SayPnl.setVisible(false)
            }.bind(this))))
        },
        onEnter: function () {
            this._super()
            this.ShowMsg()
        },
        onExit: function () {
            this._super()
        }
    })
    return MarqueeLayer
})
