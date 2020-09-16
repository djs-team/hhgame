
load('game/ui/layer/fukashop/FukaShopLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let FukaShopMdt = include('game/ui/layer/fukashop/FukaShopMdt')
    let Layer = BaseLayer.extend({
        _className: 'FukaShopLayer',
        RES_BINDING: function () {
            return {
                'goodsListPnl' : {},
                'goodsListPnl/leftPnl/searchBtn' : { onClicked : this.onsearchBtnClick},
                'goodsListPnl/leftPnl/searchTextFiled' : {},
                'goodsListPnl/leftPnl/ImgPageView' : { },
                'goodsListPnl/leftPnl/imgPageCell' : { },

                'goodsListPnl/midPnl/cardExchangeBtn' : { onClicked: this.onCardExchangeBtnClick},
                'goodsListPnl/midPnl/seizeBtn' : { onClicked: this.onSeizeBtnClick},
                'goodsListPnl/midPnl/objectExchangeBtn' : { onClicked: this.onObjectExchangeBtnClick },


                'goodsListPnl/bmPnl/menuListView' : {  },
                'goodsListPnl/bmPnl/menuCell' : {  },
                'goodsListPnl/bmPnl/goodsListView' : {  },
                'goodsListPnl/bmPnl/goodsCell' : {  },

                'exchangePnl' : {},
                'exchangePnl/exangeListView' : {},
                'exchangePnl/exangeGoodsCell' : {},

                'robPnl' : {},
                'robPnl/robListView' : {},
                'robPnl/robListPnl' : {},
                'robPnl/robGoodsCell' : {},

                'goodsDetailsPnl' : {},
                'goodsDetailsPnl/bottomWhiltPnl' : {},
                'goodsDetailsPnl/detailTopPnl/goodDetailPageView' : {},
                'goodsDetailsPnl/detailTopPnl/goodsDetailCell' : {},

                'goodsDetailsPnl/detailMidPnl/introductionText' : {},
                'goodsDetailsPnl/detailMidPnl/detailPriceText' : {},
                'goodsDetailsPnl/detailMidPnl/goodsExchangeMidBtn' : { onClicked: this.onGoogsExchangeBtnClick},

                'goodsDetailsPnl/detailBottomPnl/detailImg' : {},
                'goodsDetailsPnl/detailBottomPnl/goodsExchangeBottomBtn' : { onClicked: this.onGoogsExchangeBtnClick},


                'robLogPnl' : {},
                'robLogPnl/robLogListView' : {},
                'robLogPnl/robLogCell' : {},

                'rewardsLogPnl' : {},
                'rewardsLogPnl/logPnl' : {},
                'rewardsLogPnl/logPnl/rewardsLogListView' : {},
                'rewardsLogPnl/logPnl/rewardsLogCell' : {},

                'rewardsLogPnl/phoneExchangePnl' : {},
                'rewardsLogPnl/phoneExchangePnl/phoneTopPnl/phoneImg' : {},
                'rewardsLogPnl/phoneExchangePnl/phoneTopPnl/phoneNumTextField' : {},
                'rewardsLogPnl/phoneExchangePnl/phoneMidPnl/rechargePhoneBtn' : { onClicked: this.onRechargePhoneBtnClick},

                'commonPnl' : {},
                'commonPnl/fuKaPnl' : {},
                'commonPnl/giftLogPnl' : {},
                'commonPnl/robLogPnl' : {},


                'popupPnl' : {},
                'popupPnl/titlePnl' : {},
                'popupPnl/contextPnl' : {},
                'popupPnl/btnsPnl' : {},


            }
        },
        ctor: function () {
            this._super(ResConfig.View.FukaShopLayer)
            this.registerMediator(new FukaShopMdt(this))
            this.initData()
            this.initView()
        },

        initData: function () {

        },

        initView: function () {

        },

        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        },
        onCloseClick: function () {
            appInstance.uiManager().removeUI(this)
        },

        onsearchBtnClick: function () {

        },

        onCardExchangeBtnClick: function () {

        },

        onSeizeBtnClick: function () {

        },

        onObjectExchangeBtnClick: function () {

        },

        onGoogsExchangeBtnClick: function () {

        },

        onRechargePhoneBtnClick: function () {

        },


    })
    return Layer
})
