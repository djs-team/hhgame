
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

        initData: function (pData) {
            cc.log('=====================topLayer=========' + JSON.stringify(pData))
            this._selfInfo = pData.getSelfInfo()
            this._tData = pData.tableData
            this._actionCell = {}
            this._chiCell = []
        },

        initView: function (pData) {
            this.initData(pData)

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
