
/**
 * DeskHeadLayer
 */
load('module/mahjong/ui/MatchJinjiLayer', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let Layer = BaseLayer.extend({
        _className: 'MatchJinjiLayer',
        RES_BINDING: function () {
            return {
                'pnl/BmTxt': {  },
                'pnl/TopTxt': {  },
                'pnl/Slider': {  },
            }
        },
        ctor: function () {
            this._super(ResConfig.View.MatchJinjiLayer)
        },
        
        updateView: function (msg) {
            msg = {}
            msg.bm1 = 5
            msg.bm2 = 65
            msg.bm3 = 17

            msg.bm5 = 3

            let topStr = cc.formatStr('第%s轮(%s进%s)', msg.bm1.toString(), msg.bm2.toString(),msg.bm3.toString())
            this.TopTxt.setString(topStr)

            let bmStr = cc.formatStr('等待%s桌对局结束，确定晋级名单', msg.bm5.toString())
            this.BmTxt.setString(bmStr)

            this.Slider.setPercent(10)
        },

        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        }
    })
    return Layer
})
