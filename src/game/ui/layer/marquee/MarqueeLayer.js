
/**
 * 系统提示tips
 */
load('game/ui/layer/marquee/MarqueeLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let MarqueeLayerMdt = include('game/ui/layer/marquee/MarqueeLayerMdt')
    let MarqueeLayer = BaseLayer.extend({
        _className: 'MarqueeLayer',
        RES_BINDING: function () {
            return {
                'pnl/SayPnl': {  },
                'pnl/SayPnl/SayTxt': { }
            }
        },
        ctor: function () {
            this._super(ResConfig.View.MarqueeLayer)
            this.registerMediator(new MarqueeLayerMdt(this))
        },

        runTips: function (tips, isTurn) {
            cc.log('--->>>' + tips)
            this.TipsText.setString(tips)

            this.TipsBg.setPosition(cc.p(global.View.size.halfWidth, 0))

            let callBack = cc.callFunc(function () {
                appInstance.uiManager().removeUI(this)
            }.bind(this))

            if (isTurn) {
                this.setRotation(270)
            }

            let delayAction = cc.delayTime(0.3)
            let fadeinAction = cc.fadeIn(0.3)
            let fadeoutAction = cc.fadeOut(0.3)
            this.TipsBg.runAction(cc.sequence(cc.spawn(fadeinAction,cc.moveTo(0.3, global.View.size.halfWidth, global.View.size.halfHeight)), delayAction, fadeoutAction, callBack))
        },
        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        }
    })
    return MarqueeLayer
})
