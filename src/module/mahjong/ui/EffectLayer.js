
/**
 * EffectLayer
 */
load('module/mahjong/ui/EffectLayer', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let EffectLayerMdt = include('module/mahjong/ui/EffectLayerMdt')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let Layer = BaseLayer.extend({
        _className: 'EffectLayer',
        RES_BINDING: function () {
            return {
            }
        },
        ctor: function () {
            this._super(ResConfig.View.EffectLayer)
            this.registerMediator(new EffectLayerMdt(this))
        },


        initData: function (pData) {

        },

        initView: function (pData) {
            this.initData(pData)

        },

        clearView: function () {
            this.hideAction()
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
