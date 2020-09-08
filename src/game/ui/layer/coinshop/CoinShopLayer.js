
load('game/ui/layer/coinshop/CoinShopLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let shopLayer = BaseLayer.extend({
        _className: 'shopLayer',
        ctor: function () {
            this._super(ResConfig.View.ShopLayer)
        },
        RES_BINDING: function () {
            return {
                'pnl/dataPnl/timesText': {  },
                'pnl/dataPnl/coinPnl/coinsCnt': {  },
                'pnl/dataPnl/diamondsPnl/diamondsCnt': {  },
                'pnl/dataPnl/fuKaPnl/fuKaCnt': {  },
                'pnl/dataPnl/goodsTopPnl': {  },
                'pnl/dataPnl/goodsBottomPnl': {  },
                'pnl/dataPnl/goodCell': {  },


                'pnl/btnPnl/closeBtn': {onClicked : this.onColseClicked  },
                'pnl/btnPnl/addressBtn': {onClicked : this.onAddressClicked  },
                'pnl/btnPnl/videoBtn': {onClicked : this.onVideoClicked  },


                'pnl/popUpPnl/addressPnl': {  },
                'pnl/popUpPnl/addressPnl/confirmBtn': {onClicked : this.onConfirmClicked  },
                'pnl/popUpPnl/addressPnl/updateAddressCloseBtn': {onClicked : this.onCloseUpdateAddressClicked  },
            }
        },
        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
            this.initView()
        },
        onExit: function () {
            this._super()
        },
        onClose: function () {
            appInstance.uiManager().removeUI(this)
        },

        initData: function () {

            this._dataMsg = {}

        },

        initView: function () {

            this.addressPnl.setVisible(false)
            this.goodCell.setVisible(false)
            this.goodCell.getChildByName('coinsPg1').setVisible(false)
            this.goodCell.getChildByName('coinsPg2').setVisible(false)
            this.goodCell.getChildByName('coinsPg3').setVisible(false)
            this.goodCell.getChildByName('coinsPg4').setVisible(false)
            this.goodCell.getChildByName('coinsPg5').setVisible(false)

        },

        onAddressClicked: function () {
            this.addressPnl.setVisible(true)
            appInstance.gameAgent().httpGame().GETADDRESSReq()
        },

        onColseClicked: function () {

            appInstance.uiManager().remove(this)

        },

        onUpdatePropsData: function (data) {

            let coinsCnt =  this.coinPnl.getChildByName('coinsCnt')
            let diamondsCnt =  this.diamondsPnl.getChildByName('diamondsCnt')
            let fuKaCnt =  this.fuKaPnl.getChildByName('fuKaCnt')

            if(data.hasOwnProperty('coin')){
                coinsCnt.setString(data.coin)
                this._dataMsg.coinsCnt = data.coin
            }

            if(data.hasOwnProperty('diamonds')){
                diamondsCnt.setString(data.diamonds)
                this._dataMsg.diamondsCnt = data.diamonds

            }

            if(data.hasOwnProperty('fuKa')){
                fuKaCnt.setString(data.fuKa)
                this._dataMsg.fuKaCnt = data.fuKa

            }


        },

        initCoinShopData: function (data) {

            this.onUpdateTime(data)
            this.onInitGoodsView(data)

        },

        onUpdateTime: function (data) {

            this.timesText.setString(data.timesText)
            if(data.canWatchVideo){

                this.videoBtn.getChildByName('canWatchPg').setVisible(true)
                this.videoBtn.getChildByName('noCanWatchPg').setVisible(false)

                this.videoBtn.setBright(true)
                this.videoBtn.setTouchEnabled(true)

            }else{
                this.videoBtn.getChildByName('canWatchPg').setVisible(false)
                this.videoBtn.getChildByName('noCanWatchPg').setVisible(true)

                this.videoBtn.setBright(false)
                this.videoBtn.setTouchEnabled(false)
            }

        },

        onInitGoodsView: function (data) {

            for(let i = 1; i <= data.goodsData.length; i++){

                let goodsData = data.goodsData[i]
                let goodsCell = this.goodCell.clone()



                let pnl
                if(i < 4){
                    pnl = this.goodsTopPnl
                }else{
                    pnl = this.goodsBottomPnl
                }

                pnl.addChild(goodsCell)
                let positionX = pnl.getPositionX()
                let positionY = pnl.getPositionY()

                goodsCell.setPositionX(positionX + (i - 1)%3 * 174)
                goodsCell.setPositionX(positionY)

                let childPgName = 'coinsPg'
                if(i > 5)
                    childPgName = 'coinsPg' + 5
                else
                    childPgName = 'coinsPg' + 1

                this.goodCell.getChildByName(childPgName).setVisible(true)


                goodsCell._sendMsg = {
                    'goodsid' : goodsData.id,
                    'diamonds' : goodsData.diamonds
                }

                goodsCell.getChildByName('coinsVueText').setString(goodsData.coin)
                goodsCell.getChildByName('pricePg').getChildByName('priceText').setString(goodsData.diamonds)

                goodsCell.addClickEventListener(function (sender,et) {

                    this.onBuyCoinsFunction(sender)

                }.bind(this))

            }

        },

        onBuyCoinsFunction: function (sender) {

            let dialogMsg = {
                ViewType: 1,
                TileName : '提 示',
            }

            let msg
            let btnNameArray = {}
            if(this._dataMsg.diamondsCnt < sender.diamonds){

                msg = '您的钻石数量不足，请先领取钻石'
                btnNameArray.MidBtnName = '确 定'
            }else{
                msg = '您是否确定兑换'
                btnNameArray.LeftBtnName = '取 消'
                btnNameArray.RightBtnName = '确 定'
            }

            dialogMsg.SayText = msg

            if(btnNameArray.hasOwnProperty('LeftBtnName')){
                dialogMsg.LeftBtnName = btnNameArray.LeftBtnName
            }

            if(btnNameArray.hasOwnProperty('MidBtnName')){
                dialogMsg.MidBtnName = btnNameArray.MidBtnName
            }

            if(btnNameArray.hasOwnProperty('RightBtnName')){
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

        onVideoClicked: function () {

            appInstance.gameAgent().httpGame().VIDEOFORDIAMONDSReq()

        },

        onUpdateAddressData: function (data) {

            if(data.hasOwnProperty('phone'))
                this.addressPnl.getChildByName('phponeTextFiled').setString(data.phone)

            if(data.hasOwnProperty('name'))
                this.addressPnl.getChildByName('nameTextFiled').setString(data.name)

            if(data.hasOwnProperty('address'))
                this.addressPnl.getChildByName('addressTextFiled').setString(data.address)

        },

        onConfirmClicked: function () {

            let phone = this.addressPnl.getChildByName('phponeTextFiled').getString()
            let name = this.addressPnl.getChildByName('nameTextFiled').getString()
            let address = this.addressPnl.getChildByName('addressTextFiled').getString()

            if(!(/^1[3|4|5|7|8][0-9]\d{8,11}$/.test(phone))){
                appInstance.gameAgent().Tips('手机号格式异常，请检查后重新输入')
                return
            }

            if(name.length >= 50){

                appInstance.gameAgent().Tips('名字超长，请检查后重新输入')
                return

            }

            if(name.length >= 50){

                appInstance.gameAgent().Tips('地址超长，请检查后重新输入')
                return

            }

            let msg = {}
            msg.phone = phone
            msg.name = name
            msg.address = address
            appInstance.gameAgent().httpGame().UPDATEADDRESSReq(msg)

        },

        onCloseUpdateAddressClicked: function () {

            this.addressPnl.setVisible(false)

        }

    })
    return shopLayer
})
