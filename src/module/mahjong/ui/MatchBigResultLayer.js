
/**
 * DeskHeadLayer
 */
load('module/mahjong/ui/MatchBigResultLayer', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let Layer = BaseLayer.extend({
        _className: 'MatchBigResultLayer',
        RES_BINDING: function () {
            return {
                'topPnl/BackBtn': { onClicked: this.onBackBtnClick },
                'midPnl/midNd': {  },
                'midPnl/midNd/PlayerCell': {  },
            }
        },

        onBackBtnClick: function () {

        },
        ctor: function () {
            this._super(ResConfig.View.MatchBigResultLayer)
        },

        initData: function (pData) {
            cc.log('=====================topLayer=========' + JSON.stringify(pData))
            this._selfInfo = pData.getSelfInfo()
            this._tData = pData.tableData
            this._actionCell = {}
            this._chiCell = []
        },

        initView: function (pData) {

        },


        clearView: function () {
            this.hideAction()
        },

        updateView: function (tData) {

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
