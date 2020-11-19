load('game/ui/layer/coinshop/CoinShopLayer', function () {
    let AppConfig = include('game/public/AppConfig')
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let CoinShopMdt = include('game/ui/layer/coinshop/CoinShopMdt')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let coinSender = {};

    let CoinShopLayer = BaseLayer.extend({
        _className: 'CoinShopLayer',
        _dataMsg: {},
        _dataJinMsg: {},
        _dataZuanMsg: {},

        ctor: function () {
            appInstance.gameAgent().hideLoading()
            this._super(ResConfig.View.CoinShopLayer)
            this.registerMediator(new CoinShopMdt(this))
            this.registerEventListener('rewardVideoCallback', this.onRewardVideoCallback)
            this.registerEventListener('ThirdPayCallback', this.onThirdPayCallBack)
            this.registerEventListener('ApplePayCallback', this.onApplePayCallBack)

        },
        RES_BINDING: function () {
            return {

                'topPnl/closeBtn': {onClicked: this.onColseClicked},

                'topPnl/coinPnl': {},
                'topPnl/diamondsPnl': {},
                'topPnl/fuKaPnl': {},

                'btmPnl/midPnl/videoBtn': {onClicked: this.onVideoClicked},
                'btmPnl/midPnl/timesText': {},

                'btmPnl/leftPnl/jinBtn': {onClicked: this.onJinBiClicked},
                'btmPnl/leftPnl/zuanBtn': {onClicked: this.onZuanShiClicked},

                'btmPnl/rightPnl/goodMidNd': {},
                'btmPnl/rightPnl/goodCell': {},
                'btmPnl/rightPnl/addressBtn': {onClicked: this.onAddressClicked},

                'popUpPnl/addressPnl': {},
                'popUpPnl/addressPnl/confirmBtn': {onClicked: this.onConfirmClicked},
                'popUpPnl/addressPnl/updateAddressCloseBtn': {onClicked: this.onCloseUpdateAddressClicked},
                'PayType': {onClicked: this.onClosePayTypeClicked },
                'PayType/bgAli/ivAli': {onClicked: this.onAliPayClick},
                'PayType/bgWx/ivWx': {onClicked: this.onWxClick},
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

        onJinBiClicked: function () {
            this.jinBtn.getChildByName('Image_33').setVisible(false)
            this.jinBtn.getChildByName('Image_34').setVisible(true)
            this.zuanBtn.getChildByName('Image_36').setVisible(true)
            this.zuanBtn.getChildByName('Image_37').setVisible(false)
            this.onInitGoodsView(this._dataJinMsg, 1)
        },

        onZuanShiClicked: function () {
            this.jinBtn.getChildByName('Image_33').setVisible(true)
            this.jinBtn.getChildByName('Image_34').setVisible(false)
            this.zuanBtn.getChildByName('Image_36').setVisible(false)
            this.zuanBtn.getChildByName('Image_37').setVisible(true)
            this.onInitGoodsView(this._dataZuanMsg, 2)
        },

        /**
         * 初始化数据
         */
        initData: function () {

            this.videoBtn.setBright(false)
            this.videoBtn.setTouchEnabled(false)
        },

        /**
         * 初始化页面
         */
        initView: function () {
            this.addressPnl.setVisible(false)
            this.goodCell.setVisible(false)
            this.goodCell.getChildByName('coinsPg').setVisible(false)
            this.jinBtn.getChildByName('Image_33').setVisible(false)
            this.jinBtn.getChildByName('Image_34').setVisible(true)
            this.zuanBtn.getChildByName('Image_36').setVisible(true)
            this.zuanBtn.getChildByName('Image_37').setVisible(false)
            this.PayType.setVisible(false)

        },

        /**
         * 点击收货信息触发玩家地址信息
         */
        onAddressClicked: function (sender) {
            GameUtil.delayBtn(sender);
            this.addressPnl.setVisible(true)
            appInstance.gameAgent().httpGame().GETADDRESSReq()
        },

        /**
         * 触发关闭按钮
         */
        onColseClicked: function () {
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)
        },

        /**
         * 初始化商品页信息-头部金币、钻石、福卡信息
         * @param data
         */
        onUpdatePropsData: function (data) {

            let coinsCnt = this.coinPnl.getChildByName('coinsCnt')
            let diamondsCnt = this.diamondsPnl.getChildByName('diamondsCnt')
            let fuKaCnt = this.fuKaPnl.getChildByName('fuKaCnt')

            if (data.hasOwnProperty('coin')) {
                coinsCnt.setString(GameUtil.getStringRule(data.coin))
                this._dataMsg.coinsCnt = data.coin
            }

            if (data.hasOwnProperty('diamonds')) {
                diamondsCnt.setString(GameUtil.getStringRule(data.diamonds))
                this._dataMsg.diamondsCnt = data.diamonds

            }

            if (data.hasOwnProperty('fuKa')) {
                fuKaCnt.setString(GameUtil.getStringRule(data.fuKa))
                this._dataMsg.fuKaCnt = data.fuKa

            }


        },

        /**
         * 更新列表数据
         * @param data
         */
        initCoinShopData: function (data) {
            this._dataJinMsg = data.goodsJinData
            this._dataZuanMsg = data.goodsZuanData
            this.onUpdateTime(data)
            this.onInitGoodsView(this._dataJinMsg, 1)

        },

        /**
         * 视频次数数据更新
         * @param data
         */
        onUpdateTime: function (data) {

            this.timesText.setString(data.timesText)
            if (data.canWatchVideo) {

                this.videoBtn.getChildByName('canWatchPg').setVisible(true)
                this.videoBtn.getChildByName('noCanWatchPg').setVisible(false)

                this.videoBtn.setBright(true)
                this.videoBtn.setTouchEnabled(true)

            } else {
                this.videoBtn.getChildByName('canWatchPg').setVisible(false)
                this.videoBtn.getChildByName('noCanWatchPg').setVisible(true)

                this.videoBtn.setBright(false)
                this.videoBtn.setTouchEnabled(false)
            }

        },

        /**
         * 更新商品列表信息
         * @param data
         */
        onInitGoodsView: function (data, type) {
            var goodMidNd = this.goodMidNd;
            for (let i = 0; i < data.length; i++) {

                let goodsData = data[i]
                let goodsCell = this.goodCell.clone()
                goodsCell.setVisible(true)

                goodMidNd.addChild(goodsCell)

                let cellPositionX = (i) % 3 * 174
                let cellPositionY
                if (i < 3) {
                    cellPositionY = 18
                } else {
                    cellPositionY = -180
                }

                goodsCell.setPositionX(cellPositionX)
                goodsCell.setPositionY(cellPositionY)

                goodsCell._sendMsg = {
                    'goodsid': goodsData.id,
                    'price': goodsData.price
                }

                goodsCell.getChildByName('coinsPg').loadTexture(goodsData.res)
                goodsCell.getChildByName('coinsPg').setVisible(true)

                goodsCell.getChildByName('coinsVueText').setString(goodsData.number)
                goodsCell.getChildByName('pricePg').getChildByName('priceText').setString(goodsData.price)

                if (type == 1) {
                    goodsCell.getChildByName('pricePg').getChildByName('Image_79').setVisible(true)
                    goodsCell.addClickEventListener(function (sender, et) {
                        appInstance.audioManager().playEffect(ResConfig.Sound.btnEffect)
                        this.onBuyCoinsFunction(sender)

                    }.bind(this))
                } else {
                    goodsCell.getChildByName('pricePg').getChildByName('priceText').setPosition(49.4, 14.94)
                    goodsCell.getChildByName('pricePg').getChildByName('Image_79').setVisible(false)
                    goodsCell.addClickEventListener(function (sender, et) {
                        appInstance.audioManager().playEffect(ResConfig.Sound.btnEffect)
                        this.onBuyZuanFunction(sender)

                    }.bind(this))
                }
            }

        },

        /**
         * 钻石购买
         * @param s
         */
        onBuyZuanFunction: function (s) {
            coinSender = s;
            if (cc.sys.OS_IOS === cc.sys.os) {
                if (AppConfig.applePayType == "Apple") {
                   let _sendData = coinSender._sendData
                   let msg = {
                       vipCode: _sendData.vipCode,
                       payType: 3
                   }
                   appInstance.gameAgent().httpGame().ZuanPaysOrderReq(msg)
                } else {
                    this.PayType.setVisible(true)
                }
            } else {
                this.PayType.setVisible(true)
            }

        },
        /**
         * 支付宝支付
         */
        onAliPayClick: function (sender) {
            GameUtil.delayBtn(sender);
            this.PayType.setVisible(false)
            //下单
            let _sendData = coinSender._sendMsg
            let msg = {
                goodsid: _sendData.goodsid,
                payType: 1
            }
            appInstance.gameAgent().httpGame().ZuanPaysOrderReq(msg)

        },
        /**
         * 微信支付
         */
        onWxClick: function (sender) {
            GameUtil.delayBtn(sender);
            this.PayType.setVisible(false)
            //下单
            let _sendData = coinSender._sendMsg
            let msg = {
                goodsid: _sendData.goodsid,
                payType: 2
            }
            appInstance.gameAgent().httpGame().ZuanPaysOrderReq(msg)

        },
        //第三方支付结果回调
        onThirdPayCallBack: function (msg) {
            if (msg == 0) {
                appInstance.gameAgent().Tips('------------------------------------充值成功！！！')
            } else {
                appInstance.gameAgent().Tips('------------------------------------充值失败！！！')
            }
        },
        // 苹果支付成功订单校验
        onApplePayCallBack: function (msg) {
           msg = JSON.parse(msg)
           appInstance.gameAgent().httpGame().VIPPaysOrderAppleCheckReq(msg)
        },
        /**
         * 触发购买事件
         * @param sender
         */
        onBuyCoinsFunction: function (sender) {

            let dialogMsg = {
                ViewType: 1,
                TileName: '提 示',
            }

            let msg
            let btnNameArray = {}
            if (this._dataMsg.diamondsCnt < sender._sendMsg.price) {

                msg = '您的钻石数量不足，请先领取钻石'
                btnNameArray.MidBtnName = '确 定'
            } else {
                msg = '您是否确定兑换'
                btnNameArray.LeftBtnName = '取 消'
                btnNameArray.RightBtnName = '确 定'
            }

            dialogMsg.SayText = msg

            if (btnNameArray.hasOwnProperty('LeftBtnName')) {
                dialogMsg.LeftBtnName = btnNameArray.LeftBtnName
            }

            if (btnNameArray.hasOwnProperty('MidBtnName')) {
                dialogMsg.MidBtnName = btnNameArray.MidBtnName
            }

            if (btnNameArray.hasOwnProperty('RightBtnName')) {
                dialogMsg.RightBtnName = btnNameArray.RightBtnName
                dialogMsg.RightBtnClick = function () {

                    let _sendMsg = sender._sendMsg
                    let msg = {}
                    msg.goodsid = _sendMsg.goodsid
                    appInstance.gameAgent().httpGame().COINSSHOPBUYReq(msg)
                }
            }

            appInstance.gameAgent().addDialogUI(dialogMsg)


        },

        /**
         * 观看视频按钮
         */
        onVideoClicked: function () {
            this.videoBtn.setTouchEnabled(false)
            if(cc.sys.os === cc.sys.OS_WINDOWS)
                appInstance.gameAgent().httpGame().VIDEOFORDIAMONDSReq()
            else
                appInstance.nativeApi().showRewardVideo()


        },
        onRewardVideoCallback: function (msg) {
            appInstance.gameAgent().Tips('金币商城接收到视频回调，msg ：' + msg)
            if (msg == "0") {
                appInstance.gameAgent().httpGame().VIDEOFORDIAMONDSReq()
            } else {
                this.shakeBtn.setTouchEnabled(true)
            }
        },

        /**
         * 获取地址信息渲染
         * @param data
         */
        onUpdateAddressData: function (data) {

            if (data.hasOwnProperty('phone'))
                this.addressPnl.getChildByName('phponeTextFiled').setString(data.phone)

            if (data.hasOwnProperty('name'))
                this.addressPnl.getChildByName('nameTextFiled').setString(data.name)

            if (data.hasOwnProperty('address'))
                this.addressPnl.getChildByName('addressTextFiled').setString(data.address)

        },

        /**
         * 地址确认
         */
        onConfirmClicked: function (sender) {
            GameUtil.delayBtn(sender);
            let phone = this.addressPnl.getChildByName('phponeTextFiled').getString()
            let name = this.addressPnl.getChildByName('nameTextFiled').getString()
            let address = this.addressPnl.getChildByName('addressTextFiled').getString()

            if (!(/^1[3|4|5|7|8][0-9]\d{8,11}$/.test(phone))) {
                appInstance.gameAgent().Tips('手机号格式异常，请检查后重新输入')
                return
            }

            if (name.length >= 50) {

                appInstance.gameAgent().Tips('名字超长，请检查后重新输入')
                return

            }

            if (name.length >= 50) {

                appInstance.gameAgent().Tips('地址超长，请检查后重新输入')
                return

            }

            let msg = {}
            msg.phone = phone
            msg.name = name
            msg.address = address
            appInstance.gameAgent().httpGame().UPDATEADDRESSReq(msg)

        },

        /**
         * 关闭地址弹框
         */
        onCloseUpdateAddressClicked: function () {
            this.addressPnl.setVisible(false)

        },


        onClosePayTypeClicked: function () {
            this.PayType.setVisible(false)
        }

    })
    return CoinShopLayer
})
