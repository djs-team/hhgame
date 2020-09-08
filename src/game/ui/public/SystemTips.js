
/**
 * 系统提示tips
 */
load('game/ui/public/SystemTips', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let SystemTips = BaseLayer.extend({
        _className: 'SystemTips',
        RES_BINDING: function () {
            return {
                'pnl/TipsBg': {  },
                'pnl/TipsBg/TipsText': { }
            }
        },
        ctor: function () {
            this._super(ResConfig.View.SystemTipsLayer)
        },

        runTips: function (tips) {
            this.TipsText.setString(tips)

            this.TipsBg.setPosition(cc.p(global.View.size.halfWidth, 0))

            let callBack = cc.callFunc(function () {
                appInstance.uiManager().removeUI(this)
            }.bind(this))

            let delayAction = cc.delayTime(0.3)
            let fadeinAction = cc.fadeIn(0.3)
            let fadeoutAction = cc.fadeOut(0.3)
            this.TipsBg.runAction(cc.sequence(cc.spawn(fadeinAction,cc.moveTo(0.3, global.View.size.halfWidth, global.View.size.halfHeight)), delayAction, fadeoutAction, callBack))
        },
        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        }
    })
    return SystemTips
})
