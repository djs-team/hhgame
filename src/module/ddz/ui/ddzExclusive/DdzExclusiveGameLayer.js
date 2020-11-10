load('module/ddz/ui/ddzExclusive/DdzExclusiveGameLayer', function () {
    let AppConfig = include('game/public/AppConfig')
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let DdzExclusiveGameMdt = include('module/ddz/ui/ddzExclusive/DdzExclusiveGameMdt')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let AniPlayer = ResConfig.AniPlayer
    let PlayerPlay = ResConfig.PlayerPlay

    let DdzExclusiveGameLayer = BaseLayer.extend({
        _className: 'DdzExclusiveGameLayer',
        _dataMsg: {},
        _dataJinMsg: {},
        _dataZuanMsg: {},

        ctor: function () {
            appInstance.gameAgent().hideLoading()
            this._super(ResConfig.View.DdzExclusiveGameLayer)
            this.registerMediator(new DdzExclusiveGameMdt(this))


        },
        RES_BINDING: function () {
            return {

                'topPnl/closeBtn': {onClicked: this.onClose},
                'leftPnl/aniNd': {},
                // 'topPnl/diamondsPnl': {},
                // 'topPnl/fuKaPnl': {},
                //
                // 'btmPnl/midPnl/videoBtn': {onClicked: this.onVideoClicked},
                // 'btmPnl/midPnl/timesText': {},
                //
                // 'btmPnl/leftPnl/jinBtn': {onClicked: this.onJinBiClicked},
                // 'btmPnl/leftPnl/zuanBtn': {onClicked: this.onZuanShiClicked},
                //
                // 'btmPnl/rightPnl/goodMidNd': {},
                // 'btmPnl/rightPnl/goodCell': {},
                // 'btmPnl/rightPnl/addressBtn': {onClicked: this.onAddressClicked},
                //
                // 'popUpPnl/addressPnl': {},
                // 'popUpPnl/addressPnl/confirmBtn': {onClicked: this.onConfirmClicked},
                // 'popUpPnl/addressPnl/updateAddressCloseBtn': {onClicked: this.onCloseUpdateAddressClicked},
                // 'PayType': {onClicked: this.onClosePayTypeClicked },
                // 'PayType/bgAli/ivAli': {onClicked: this.onAliPayClick},
                // 'PayType/bgWx/ivWx': {onClicked: this.onWxClick},
            }
        },
        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
            this.initData()
            this.initView()
        },
        onExit: function () {
            this._super()
        },
        onClose: function () {
            appInstance.uiManager().removeUI(this)
        },

        /**
         * 初始化数据
         */
        initData: function () {


        },

        /**
         * 初始化页面
         */
        initView: function () {
            this.updatePlayerAni("1")

        },
        updatePlayerAni: function (pRole) {

            pRole = pRole || this._pRole
            if (this._selectRole == pRole || (pRole != 0 && !pRole)) {
                return
            }

            this.aniNd.removeAllChildren()
            let ani = appInstance.gameAgent().gameUtil().getAni(AniPlayer[pRole])
            this.aniNd.addChild(ani)
            ani.setPosition(cc.p(0, 0))
            ani.setScale(0.6)
            ani.setAnimation(0, PlayerPlay.stand, true)
            this._selectRole = pRole
        },

    })
    return DdzExclusiveGameLayer
})
