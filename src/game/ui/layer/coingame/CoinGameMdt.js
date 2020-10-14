
/**
 *  Turntable Mediator
 *
 */
load('game/ui/layer/coingame/CoinGameMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let mdt = Mediator.extend({
        mediatorName: 'CoinGameMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.UPDATE_PROPSYNC
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.UPDATE_PROPSYNC:
                    this.view.onUpdatePropsData(body)
                    break
                default:
                    break

            }
        },

        onRegister: function () {

            this.initView()
        },

        onRemove: function () {
        },

        /**
         * 初始化商城灯信息
         */
        initView: function () {

            let data = {
                'coin' : appInstance.dataManager().getUserData().coin,
                'diamonds' : appInstance.dataManager().getUserData().diamonds,
            }

            this.view.initView(data)


        },

        /**
         * 获取的商城列表信息
         * @param body
         */
        initCoinShopData: function (body) {
            let data = {}
            data.goodsJinData = []
            data.goodsZuanData = []
            this.onFormatTimeMsg(data,body)
            for(let i = 0; i < body.coinStoreList.length; i++){
                let goods = body.coinStoreList[i]
                let goodsJinData = {}
                let goodsZuanData = {}
                if (goods.storeType==1) {
                    goodsJinData.id = goods.id
                    goodsJinData.number = goods.number+'金币'
                    goodsJinData.price = goods.price
                    switch (goods.id) {
                        case 1:
                            goodsJinData.res = 'res/coinshop/coin_1.png';
                            break;
                        case 2:
                            goodsJinData.res = 'res/coinshop/coin_2.png';
                            break;
                        case 3:
                            goodsJinData.res = 'res/coinshop/coin_3.png';
                            break;
                        case 4:
                            goodsJinData.res = 'res/coinshop/coin_4.png';
                            break;
                        case 5:
                            goodsJinData.res = 'res/coinshop/coin_5.png';
                            break;
                        case 6:
                            goodsJinData.res = 'res/coinshop/coin_5.png';
                            break;
                    }
                    data.goodsJinData.push(goodsJinData)
                } else {
                    goodsZuanData.id = goods.id
                    goodsZuanData.number = goods.number+'钻石'
                    goodsZuanData.price = '￥'+goods.price
                    switch (goods.id) {
                        case 7:
                            goodsZuanData.res = 'res/coinshop/diamond_1.png';
                            break;
                        case 8:
                            goodsZuanData.res = 'res/coinshop/diamond_2.png';
                            break;
                        case 9:
                            goodsZuanData.res = 'res/coinshop/diamond_3.png';
                            break;
                        case 10:
                            goodsZuanData.res = 'res/coinshop/diamond_4.png';
                            break;
                        case 11:
                            goodsZuanData.res = 'res/coinshop/diamond_5.png';
                            break;
                        case 12:
                            goodsZuanData.res = 'res/coinshop/diamond_6.png';
                            break;
                    }
                    data.goodsZuanData.push(goodsZuanData)
                }
            }
            this.view.initCoinShopData(data)

        },

        /**
         * 购买返回数据
         * @param body
         */
        onBuyCoinsResult: function (body) {

            let data = {
                propType : GameConfig.propType_currency,
                propCode : GameConfig.propType_currency_coin,
                propNum : body.coins
            }
            this.onFormatPropMsg(data)

        },

        /**
         * 处理获取信息
         * @param data
         */
        onFormatPropMsg: function (data) {


            let propData = {}
            GameUtil.getPropData(data,propData,GameUtil.CURRENCYTYPE_1,GameUtil.UNITLOCATION_BEFORE,'x')

            let propList = []
            propList.push(propData)
            appInstance.gameAgent().addReceivePropsUI(propList)

        },

        /**
         * 视频次数数据处理
         * @param data
         * @param body
         */
        onFormatTimeMsg: function (data,body) {
            data.timesText = '今日剩余' + body.usedWatchVideoNum + '/' + body.maxWatchVideoNum
            if(body.usedWatchVideoNum < body.maxWatchVideoNum){
                data.canWatchVideo = true
            }else{
                data.canWatchVideo = false
            }
        },

        /**
         * 视频回调数据处理
         * @param body
         */
        watchVideoResult: function (body) {
            let propData = {
                propType : GameConfig.propType_currency,
                propCode : GameConfig.propType_currency_diamonds,
                propNum : body.diamonds
            }

            this.onFormatPropMsg(propData)

            let timeData = {}
            this.onFormatTimeMsg(timeData,body)
            this.view.onUpdateTime(timeData)
        },

        /**
         * 地址回调
         */
        onUpdateAddressResult: function () {

            appInstance.gameAgent().Tips('修改成功！')
            this.view.onCloseUpdateAddressClicked()

        }

    })
    return mdt
})