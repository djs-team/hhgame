
/**
 * 牌桌背景层
 */
load('module/mahjong/ui/DeskBgLayer', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let Layer = BaseLayer.extend({
        _className: 'DeskBgLayer',
        RES_BINDING: function () {
            return {
                // 'pnl/TipsBg': {  },
                // 'pnl/TipsBg/TipsText': { }
            }
        },
        ctor: function () {
            this._super(ResConfig.View.DeskBgLayer)
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
    return Layer
})
