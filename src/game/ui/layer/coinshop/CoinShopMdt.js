
/**
 *  Turntable Mediator
 *
 */
load('game/ui/layer/coinshop/CoinShopMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let mdt = Mediator.extend({
        mediatorName: 'CoinShopMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.UPDATE_PROPSYNC,
                GameEvent.COINSHOP_GET,
                GameEvent.COINSHOP_BUY,
                GameEvent.ADRESS_GET,
                GameEvent.ADRESS_UPDATE,
                GameEvent.VIDEO_WATCH_DIAMONDS,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.UPDATE_PROPSYNC:
                    this.view.onUpdatePropsData(body)
                    break
                case GameEvent.COINSHOP_GET:
                    this.initCoinShopData(body)
                    break
                case GameEvent.COINSHOP_BUY:
                    this.onBuyCoinsResult(body)
                    break
                case GameEvent.ADRESS_GET:
                    this.view.onUpdateAddressData(body)
                    break
                case GameEvent.ADRESS_UPDATE:
                    this.onUpdateAddressResult()
                    break
                case GameEvent.VIDEO_WATCH_DIAMONDS:
                    this.watchVideoResult(body)
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

        initView: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().ROLESELECTEDBack(msg)
            let data = {
                'coin' : appInstance.dataManager().getUserData().coin,
                'diamonds' : appInstance.dataManager().getUserData().diamonds,
                'fuKa' : appInstance.dataManager().getUserData().fuKa,
            }

            this.view.onUpdatePropsData(data)


        },

        initCoinShopData: function (body) {

            let data = {}

            this.onFormatTimeMsg(data,body)
            for(let i = 0; i < body.coinStoreList.length; i++){

                let goods = body.coinStoreList[i]
                let goodsData = {}
                goodsData.id = goods.id
                goodsData.coin = goods.coin+'金币'
                goodsData.diamonds = goods.diamonds
                goodsData.describe = goods.describe
                data.goodsData.push(goodsData)
            }

            this.view.initCoinShopData(data)

        },

        onBuyCoinsResult: function (body) {

            let data = {
                propType : GameConfig.propType_role,
                propCode : GameConfig.propType_currency_coin,
                propNum : body.coins
            }
            this.onFormatPropMsg(data)

        },

        onFormatPropMsg: function (data) {

            let propData = {}
            GameUtil.getPropData(data,propData,GameUtil.CURRENCYTYPE_1,GameUtil.UNITLOCATION_BEFORE,'x')

            let propList = []
            propList.push(propData)
            appInstance.gameAgent().addReceivePropsUI(propList)

        },

        onFormatTimeMsg: function (data,body) {
            data.goodsData = []
            data.timesText = '今日剩余' + body.usedWatchVideoNum + '/' + body.maxWatchVideoNum
            if(body.usedWatchVideoNum < body.maxWatchVideoNum){
                data.canWatchVideo = true
            }else{
                data.canWatchVideo = false
            }
        },

        watchVideoResult: function (body) {
            let propData = {
                propType : GameConfig.propType_role,
                propCode : GameConfig.propType_currency_diamonds,
                propNum : body.diamonds
            }

            this.onFormatPropMsg(propData)

            let timeData = {}
            this.onFormatTimeMsg(timeData,body)
            this.view.onUpdateTime(timeData)
        },

        onUpdateAddressResult: function () {

            this.view.onCloseUpdateAddressClicked()

        }

    })
    return mdt
})