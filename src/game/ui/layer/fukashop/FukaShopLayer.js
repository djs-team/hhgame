
load('game/ui/layer/fukashop/FukaShopLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let FukaShopMdt = include('game/ui/layer/fukashop/FukaShopMdt')
    let Layer = BaseLayer.extend({
        _className: 'FukaShopLayer',
        _menuItemName: 'menuItem_',//菜单cell名称前缀
        _selectStr: '',//查询框内容
        _menuStartIndex: 0,//菜单栏(主页)开始位
        _menuIndexLength: 9,//菜单栏（主页）一页显示几条
        _canRefreshMenuList : true,
        _cardListStartIndex : 0,//卡券兑换开始位
        _cardListIndexLength : 12,//卡券兑换请求长度
        _materiaListStartIndex : 0,//实物兑换开始位
        _materiaListIndexLength : 12,//实物兑换开始位
        _robListStartIndex : 0,//福卡夺宝开始位
        _robListIndexLength : 6,//福卡夺宝开始位
        _giftLogStartIndex : 0,//获得商品记录开始位
        _giftLogIndexLength : 4,//获得商品记录开始位
        _robLogStartIndex : 0,//福卡夺宝记录开始位
        _robLogIndexLength : 15,//福卡夺宝记录开始位
        _currentLayer : 'goodsList',
        RES_BINDING: function () {
            return {
                'goodsListPnl' : {},
                'goodsListPnl/leftPnl/searchBtn' : { onClicked : this.onsearchBtnClick},
                'goodsListPnl/leftPnl/searchTextFiled' : {},
                'goodsListPnl/leftPnl/imgPageView' : { },
                'goodsListPnl/leftPnl/imgPageCell' : { },

                'goodsListPnl/midPnl/cardExchangeBtn' : { onClicked: this.onCardExchangeBtnClick},
                'goodsListPnl/midPnl/seizeBtn' : { onClicked: this.onSeizeBtnClick},
                'goodsListPnl/midPnl/objectExchangeBtn' : { onClicked: this.onObjectExchangeBtnClick },


                'goodsListPnl/bmPnl/menuListView' : {  },
                'goodsListPnl/bmPnl/menuCell' : {  },
                'goodsListPnl/bmPnl/goodsListView' : {  },
                'goodsListPnl/bmPnl/goodsPnl' : {  },
                'goodsListPnl/bmPnl/goodsCell' : {  },

                'exchangePnl' : {},
                'exchangePnl/exangeListView' : {},
                'exchangePnl/exchangeListPnl' : {},
                'exchangePnl/exangeGoodsCell' : {},

                'robPnl' : {},
                'robPnl/robListView' : {},
                'robPnl/robListPnl' : {},
                'robPnl/robGoodsCell' : {},

                'goodsDetailsPnl' : {},
                'goodsDetailsPnl/bottomWhiltPnl' : {},
                'goodsDetailsPnl/detailTopPnl/goodDetailPageView' : {},
                'goodsDetailsPnl/detailTopPnl/goodsDetailCell' : {},

                'goodsDetailsPnl/detailMidPnl/goodsNameText' : {},
                'goodsDetailsPnl/detailMidPnl/introductionText' : {},
                'goodsDetailsPnl/detailMidPnl/detailPriceText' : {},
                'goodsDetailsPnl/detailMidPnl/goodsExchangeMidBtn' : {},

                'goodsDetailsPnl/detailBottomPnl' : {},
                'goodsDetailsPnl/detailBottomPnl/detailImg' : {},
                'goodsDetailsPnl/detailBottomPnl/goodsExchangeBottomBtn' : {},


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
                'rewardsLogPnl/phoneExchangePnl/phoneMidPnl/rechargePhoneBtn' : { },

                'commonPnl' : {},
                'commonPnl/fuKaPnl' : {},
                'commonPnl/giftLogBtnPnl' : { onClicked : this.onGetGiftLogClick},
                'commonPnl/robLogBtnPnl' : { onClicked: this.onGetRobLogClick},


                'popupPnl' : { onClicked: this.onClosePopupPnlClick},
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
            this._allElementArray = [
                'goodsListPnl',
                'goodsPnl',
                'goodsDetailsPnl',
                'detailBottomPnl',
                'exchangePnl',
                'robPnl',
                'robListPnl',
                'robGoodsCell',
                'robLogPnl',
                'rewardsLogPnl',
                'logPnl',
                'rewardsLogCell',
                'phoneExchangePnl',
                'popupPnl'
            ]
        },

        initView: function () {

            this.goodsPnl.setVisible(false)
            this.goodsCell.setVisible(false)
            this.goodsListPnl.setVisible(true)

            this.goodsDetailsPnl.setVisible(false)
            this.detailBottomPnl.setVisible(false)
            this.bottomWhiltPnl.setVisible(false)

            this.exchangePnl.setVisible(false)
            this.exangeGoodsCell.setVisible(false)


            this.robPnl.setVisible(false)
            this.robListPnl.setVisible(false)
            this.robGoodsCell.setVisible(false)


            this.robLogPnl.setVisible(false)
            this.robLogCell.setVisible(false)


            this.rewardsLogPnl.setVisible(false)
            this.logPnl.setVisible(false)
            this.rewardsLogCell.setVisible(false)

            this.phoneExchangePnl.setVisible(false)

            this.popupPnl.setVisible(false)

            this.imgPageCell.setVisible(false)
            this.menuCell.setVisible(false)


            this.goodsListView.addEventListener(this.selectedItemEvent,this)

        },

        selectedItemEvent: function (sender, type) {
            if (type !== ccui.ListView.EVENT_SELECTED_ITEM) {
                return
            }
            let curIndex = sender.getCurSelectedIndex()//当前手指点击的行，坐标以0开始
            let offIndex = 2 //一页展示的子节点cell
            let childLen = sender.getItems().length
            if ( curIndex > (childLen - offIndex)) {
                cc.log('-----------------------到底了-------------------')
            }
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
            let elementNameArray = [
                'exchangePnl'
            ]

            this._currentLayer = 'cardExchange'
            this.onSetShowElementFunction(elementNameArray)
            this.onRefreshCardListParams()

            this.onGetCardListData(this._cardListStartIndex)

        },

        onGetCardListData: function (startIndex) {
            let msg = {
                startIndex : startIndex,
                endIndex : startIndex + this._cardListIndexLength
            }
            appInstance.gameAgent().httpGame().FUKACARDLISTReq(msg)
        },

        onRefreshCardListParams: function (){
            this._cardListStartIndex = 0
        },

        onObjectExchangeBtnClick: function () {
            let elementNameArray = [
                'exchangePnl'
            ]

            this._currentLayer = 'objectExchange'
            this.onSetShowElementFunction(elementNameArray)
            this.onRefreshMateriaListParams()

            this.onGetMateriaListData(this._materiaListStartIndex)
        },

        onGetMateriaListData: function (startIndex) {
            let msg = {
                startIndex : startIndex,
                endIndex : startIndex + this._materiaListIndexLength
            }
            appInstance.gameAgent().httpGame().FUKAMATERIALLISTReq(msg)
        },

        onRefreshMateriaListParams: function (){
            this._materiaListStartIndex = 0
        },

        onSeizeBtnClick: function () {

            let elementNameArray = [
                'robPnl'
            ]

            this.onSetShowElementFunction(elementNameArray)
            this.onRefreshRobListParams()

            this.onGetRobListData(this._robListStartIndex)

        },

        onGetRobListData: function (startIndex) {
            let msg = {
                startIndex : startIndex,
                endIndex : startIndex + this._robListIndexLength
            }
            appInstance.gameAgent().httpGame().FUKAROBLISTReq(msg)
        },

        onRefreshRobListParams: function () {
            this._robListStartIndex = 0
        },



        onGoogsExchangeBtnClick: function (goodsId,price) {

            let msg = {
                titleName : '提 示',
                contextType : 1
            }

            let isHaveAdress = appInstance.dataManager().getUserData().isHaveAdress
            if(isHaveAdress != 1){
                msg.wordsText = '您还未完整填写收货地址，为保证\n' +
                    '货物顺利到达，请完善收货地址'
                msg.midBtnFunction = this.onClosePopupPnlClick
                this.onShowPopubUI(msg)
                return
            }

            let fuka = appInstance.dataManager().getUserData().fuKa
            if(fuka >= price){

                msg.wordsText = '您确定兑换吗'
                msg.leftBtnFunction = this.onClosePopupPnlClick
                msg.rightBtnFunction = function () {
                    let sendMsg = {
                        goodsid : goodsId
                    }
                    appInstance.gameAgent().httpGame().FUKABUYGOODSReq(sendMsg)
                }

            }else{

                msg.wordsText = '您的福卡不足'
                msg.midBtnFunction = this.onClosePopupPnlClick

            }

            this.onShowPopubUI(msg)

        },

        onRechargePhoneBtnClick: function (orderCode) {

            let pnoneNum = this.phoneNumTextField.getString()
            if(!(/^1[3|4|5|7|8][0-9]\d{8,11}$/.test(pnoneNum))){
                appInstance.gameAgent().Tips('手机号格式异常，请检查后重新输入')
                return
            }

            let msg = {
                orderCode : orderCode,
                mobile : pnoneNum
            }
            appInstance.gameAgent().httpGame().FUKAMATERIAEXCHANGEReq(msg)
        },

        initRollImgList: function (data) {

            for(let i = 0; i < data.length; i++){

                let cell = this.imgPageCell.clone()
                cell.setVisible(true)
                this.imgPageView.pushBackCustomItem(cell)

                cell.getChildByName('slideImg').loadTexture(data[i])

            }
        },

        initMenuList: function (data) {

            for(let i = 0; i < data.length; i++){

                this.onInitMenuItemCell(data,i)

            }
            this.onGetGoodsList(this._currentMenuItemCode,1)
        },

        onInitMenuItemCell: function (data,index) {

            let item = data[index]
            let cell = this.menuCell.clone()
            cell.setVisible(true)
            this.menuListView.pushBackCustomItem(cell)

            let status = 0
            if(index == 0){
                this._currentMenuItemCode = item.goodsCode
                status = 1
            }


            cell._sendMsg = {
                goodsCode: item.goodsCode,
                status: status,//0可操作 1不可操作
            }
            cell.setPositionX(0)
            cell.setPositionY(index * 10)
            cell.setName(this._menuItemName + item.goodsCode)
            cell.getChildByName('maskImg').setVisible(false)
            cell.getChildByName('selectedImg').setVisible(false)
            cell.setName(this._menuItemName + item.goodsCode)
            cell.getChildByName('menuImg').loadTexture(item.res)
            cell.getChildByName('menuName').setString(item.name)
            cell.addClickEventListener(function (sender,dt) {
                this.onMenuItemClick(sender)
            }.bind(this))
        },



        onMenuItemClick: function (sender) {

            this._currentLayer = 'goodsList'
            let data = sender._sendMsg
            if(data.status != 0)
                return
            this.onGetGoodsList(data.goodsCode,1)

        },

        onGetGoodsList: function (goodsCode,type) {
            let msg = {
                goodsCode : goodsCode,
                selectStr : this._selectStr,
                type : type,
                startIndex : this._menuStartIndex,
                indexLength : this._menuIndexLength,
            }

            appInstance.gameAgent().httpGame().GOODSLISTReq(msg)
        },

        onUpdateMenuGoodsListFunction: function (data) {

            let currGoodsIntoList = data.currGoodsIntoList
            let listLength = currGoodsIntoList.length
            this.onUpdateMenuCell(data.currGoodsCode,listLength)
            this.onUpdateMenuGoodsList(currGoodsIntoList)

        },

        onUpdateMenuCell: function (goodsCode,listLength) {

            let staticParams = {}
            let cellStatusParams = {}

            if(listLength < this._currentMenuItemCode)
                staticParams._canRefreshMenuList = false
            else
                staticParams._canRefreshMenuList = true

            if(this._currentMenuItemCode == goodsCode){
                staticParams._menuStartIndex = this._menuStartIndex + listLength

                if(this._menuStartIndex == 0){
                    cellStatusParams.selectedCode = goodsCode
                }

            }else{//不同功能菜单按钮切换

                cellStatusParams.selectedCode = goodsCode
                cellStatusParams.unSelectedCode = this._currentMenuItemCode

                staticParams._currentMenuItemCode = goodsCode
                staticParams._menuStartIndex = 0

            }

            this.onUpdateParams(staticParams)
            this.onUpdateMenuExhibition(cellStatusParams)

        },

        onUpdateMenuExhibition: function (params) {

            params = params || {}
            let flag = true
            for(let key in params){

                if(key == 'unSelectedCode')
                    flag = false
                else
                    flag = true

                this.onUpdateCellData(params[key],flag)

            }

        },

        onUpdateCellData: function (code,flag) {

            let cell = this.menuListView.getChildByName(this._menuItemName + code)
            cell.getChildByName('maskImg').setVisible(flag)
            cell.getChildByName('selectedImg').setVisible(flag)

            if(flag)
                cell._sendMsg.status = 1
            else
                cell._sendMsg.status = 0

        },


        onUpdateParams: function (params) {

            params = params || {}
            for(let key in params){
                this[key] = params[key]
            }

        },

        onUpdateMenuGoodsList: function (data) {

            this.onForMatGoodsList(data,'_menuStartIndex','goodsListView','goodsPnl',3,20,'goodsCell',220,'goodsPriceText','goodImg')

        },

        onForMatGoodsList: function (data,startIndex,listViewName,listPnlName,rowLength,rowInterval,cellName,cellInterval,fuKaNumName,imgName) {
            if(this[startIndex] == 0)
                this[listViewName].removeAllChildren()

            let rowNum = Math.ceil(data.length / rowLength)
            for(let i = 0; i < rowNum; i++){

                let goodsPnl = this[listPnlName].clone()
                goodsPnl.setVisible(true)
                goodsPnl.setPositionY(0)
                goodsPnl.setPositionX((this[listViewName].getChildrenCount() + i) * 20)
                this[listViewName].pushBackCustomItem(goodsPnl)

                for(let j = 0; j < rowLength; j++){
                    this.onInitMenuCellData(data,goodsPnl,rowLength * i+j,rowLength,cellName,cellInterval,fuKaNumName,imgName)
                }


            }
        },

        onInitMenuCellData: function (data,goodsPnl,index,rowLength,cellName,cellInterval,fuKaNumName,imgName) {

            let goodsData = data[index]
            if(!goodsData)
                return
            let cell = this[cellName].clone()
            cell.setVisible(true)
            cell.setPositionX(0)
            cell.setPositionY((index % rowLength) * cellInterval)
            goodsPnl.addChild(cell)

            cell.getChildByName(fuKaNumName).setString(goodsData.fuKaNums+'福卡')
            cell.getChildByName(imgName).loadTexture(goodsData.hallPictureUrl)
            cell._sendMsg = {
                goodsName : goodsData.goodsName,
                goodsDesc : goodsData.goodsDesc,
                fuKaNums : goodsData.fuKaNums,
                detailsPictureUrl : goodsData.detailsPictureUrl,
                upPictureUrls : goodsData.upPictureUrls,
                goodsId : goodsData.goodsId
            }

            cell.addClickEventListener(function (sender,dt) {
                this.onShowGoodsDetailMsg(sender)
            }.bind(this))

        },


        onShowGoodsDetailMsg: function (sender) {


            let elementNameArray = [
                'goodsDetailsPnl'
            ]
            this.onSetShowElementFunction(elementNameArray)

            let data = sender._sendMsg
            for(let i = 0; i < data.upPictureUrls.length; i++){

                let cell = this.goodsDetailCell.clone()
                cell.setVisible(false)
                this.goodDetailPageView.pushBackCustomItem(cell)

                cell.getChildByName('goodsDetailImg').loadTexture(data.upPictureUrls[i])

            }

            this.goodsNameText.setString(data.goodsName)
            this.introductionText.setString(data.goodsDesc)
            this.detailPriceText.setString(data.fuKaNums+'福卡')

            let btn
            if(data.detailsPictureUrl){
                this.goodsExchangeMidBtn.setVisible(false)
                this.bottomWhiltPnl.setVisible(false)
                this.detailBottomPnl.setVisible(true)
                btn = this.goodsExchangeBottomBtn
                this.detailImg.loadTexture(data.detailsPictureUrl)

            }else{
                this.goodsExchangeMidBtn.setVisible(true)
                this.bottomWhiltPnl.setVisible(true)
                this.detailBottomPnl.setVisible(false)
                btn = this.goodsExchangeMidBtn
            }

            btn.addClickEventListener(function (sender,dt) {
                this.onGoogsExchangeBtnClick(data.goodsId,data.fuKaNums)
            }.bind(this))

        },

        onSetShowElementFunction: function (elementNameArray) {

            for(let i = 0; i < this._allElementArray.length; i++){
                if(elementNameArray.indexOf(this._allElementArray[i]) != -1)
                    this[this._allElementArray[i]].setVisible(true)
                else
                    this[this._allElementArray[i]].setVisible(false)

            }

        },

        onShowPopubUI: function (msg) {

            if(msg.titleName){
                this.titlePnl.setVisible(true)
                this.titlePnl.getChildByName('titleContextText').setVisible(msg.titleName)
            }else{
                this.titlePnl.setVisible(false)
            }

            switch (msg.contextType) {
                case 1:
                    this.contextPnl.getChildByName('wordsPnl').setVisible(true)
                    this.contextPnl.getChildByName('imgPnl').setVisible(false)
                    this.contextPnl.getChildByName('robPnl').setVisible(false)
                    this.contextPnl.getChildByName('wordsPnl').getChildByName('wordsText').setString(msg.wordsText)

                    break
                case 2:
                    this.contextPnl.getChildByName('wordsPnl').setVisible(false)
                    this.contextPnl.getChildByName('imgPnl').setVisible(true)
                    this.contextPnl.getChildByName('robPnl').setVisible(false)
                    this.contextPnl.getChildByName('imgPnl').getChildByName('imgNameText').setString(msg.imgNameText)
                    this.contextPnl.getChildByName('imgPnl').getChildByName('photoImg').loadTexture(msg.photoImg)
                    break
                case 3:
                    this.contextPnl.getChildByName('wordsPnl').setVisible(false)
                    this.contextPnl.getChildByName('imgPnl').setVisible(false)
                    this.contextPnl.getChildByName('robPnl').setVisible(true)

                    this.contextPnl.getChildByName('robPnl').getChildByName('fuaVauleText').setString(msg.fuaVauleText)
                    this.contextPnl.getChildByName('robPnl').getChildByName('prizePercentText').setString(msg.currentNum + '/' + msg.allNum)
                    this.contextPnl.getChildByName('robPnl').getChildByName('prizeLoadingBar').setParent(msg.currentNum / msg.allNum * 100)
                    break
                default:
                    break
            }

            if(msg.leftBtnFunction){
                this.btnsPnl.getChildByName('leftBtn').setVisible(true)
                this.btnsPnl.getChildByName('leftBtn').addClickEventListener(function (sender,dt) {
                    msg.leftBtnFunction()
                }.bind(this))
            }else{
                this.btnsPnl.getChildByName('leftBtn').setVisible(false)
            }

            if(msg.midBtnFunction){
                this.btnsPnl.getChildByName('midBtn').setVisible(true)
                this.btnsPnl.getChildByName('midBtn').addClickEventListener(function (sender,dt) {
                    msg.midBtnFunction()
                }.bind(this))
            }else{
                this.btnsPnl.getChildByName('midBtn').setVisible(false)
            }

            if(msg.rightBtnFunction){
                this.btnsPnl.getChildByName('rightBtn').setVisible(true)
                this.btnsPnl.getChildByName('rightBtn').addClickEventListener(function (sender,dt) {
                    msg.rightBtnFunction()
                    sender.setEnabled(false)
                    sender.setBright(false)
                }.bind(this))
            }else{
                this.btnsPnl.getChildByName('rightBtn').setVisible(false)
            }


            this.popupPnl.setVisible(true)

        },

        onClosePopupPnlClick: function () {

            this.popupPnl.setVisible(false)

        },

        onUpdatecardListFunction: function (data) {

            this.onForMatGoodsList(data,'_cardListStartIndex','exangeListView','exchangeListPnl',3,20,'exangeGoodsCell',220,'goodsPriceText','goodImg')

        },

        onUpdateMaterialListFunction: function (data) {

            this.onForMatGoodsList(data,'_materiaListStartIndex','exangeListView','exchangeListPnl',3,20,'exangeGoodsCell',220,'goodsPriceText','goodImg')

        },

        onForMatRobList: function (data) {
            if(this._robListStartIndex == 0)
                this.robListView.removeAllChildren()

            let rowLength = 2
            let rowNum = Math.ceil(data.length / rowLength)
            for(let i = 0; i < rowNum; i++){

                let goodsPnl = this.robListPnl.clone()
                goodsPnl.setVisible(true)

                goodsPnl.setPositionY(0)
                goodsPnl.setPositionX((this.robListView.getChildrenCount() + i) * 20)
                this.robListView.pushBackCustomItem(goodsPnl)

                for(let j = 0; j < rowLength; j++){
                    this.onInitRobGoodsCell(data,goodsPnl,rowLength * i+j,rowLength)
                }


            }
        },

        onInitRobGoodsCell: function (data,goodsPnl,index,rowLength) {

            let goodsData = data[index]
            let cell = this.robGoodsCell.clone()
            cell.setVisible(true)
            cell.setPositionX(0)
            cell.setPositionY((index % rowLength) * cellInterval)
            goodsPnl.addChild(cell)

            cell.getChildByName('robGoodsNameText').setString(goodsData.goodsName)
            cell.getChildByName('goodImg').loadTexture(goodsData.hallPictureUrl)
            cell.getChildByName('robLoadingBar').setParent(goodsData.currentNum / goodsData.allNum * 100)
            cell.getChildByName('robPercent').setString(goodsData.currentNum + '/' + goodsData.allNum)
            cell.getChildByName('numText').setString(goodsData.playerConsumeFuka)
            cell._sendMsg = {
                currentNum : goodsData.currentNum,
                allNum : goodsData.allNum,
                fuKaCnt : goodsData.fuKaCnt,
                nickName : goodsData.nickName,
                sdkPhotoUrl : goodsData.sdkPhotoUrl,
                goodsId : goodsData.goodsid,
                roundNum : goodsData.playerConsumeFuka
            }

            cell.getChildByName('luckyPnl').addClickEventListener(function (sender,dt) {
                this.onShowLuckBoyMsg(cell._sendMsg)
            }.bind(this))

            cell.getChildByName('robBtn').addClickEventListener(function (sender,dt) {
                this.onRobGoodsFunction(cell._sendMsg)
            }.bind(this))
        },

        onShowLuckBoyMsg: function (data) {
            let msg = {
                titleName : '提 示',
                contextType : 2,
                imgNameText : data.nickName,
                photoImg : data.sdkPhotoUrl,
                midBtnFunction : this.onClosePopupPnlClick
            }

            this.onShowPopubUI(msg)
        },

        onRobGoodsFunction: function (data) {
            let msg = {
                titleName : '福卡夺宝',
                contextType : 3,
                fuaVauleText : data.fuKaCnt,
                currentNum : data.currentNum,
                allNum : data.allNum,
                leftBtn : this.onClosePopupPnlClick,
                rightBtn : function () {

                    let robFuKaNum = this.contextPnl.getChildByName('robPnl').getChildByName('fuKaFiled').getString()
                    let fuKaCnt = this.contextPnl.getChildByName('robPnl').getChildByName('fuaVauleText').getString()
                    let diffCntArray = this.contextPnl.getChildByName('robPnl').getChildByName('prizePercentText').getString().split('/')
                    let diffCnt = diffCntArray[1] - diffCntArray[0]
                    if(robFuKaNum < fuKaCnt){
                        appInstance.gameAgent().Tips('您的福卡不足')
                        return
                    }

                    if(robFuKaNum > diffCnt){
                        appInstance.gameAgent().Tips('您的福卡数量超过剩余值，请核实后再次夺宝')
                        return
                    }

                    let msg = {
                        duoBaoId : data.goodsId,
                        duoBaoNum : data.roundNum,
                        fukaNum : data.robFuKaNum,
                    }

                    appInstance.gameAgent().httpGame().FUKAROBReq(msg)

                }
            }

            this.onShowPopubUI(msg)

        },

        onRobResult: function (data) {

            let status = data.status
            let msg = {
                titleName : '提示',
                contextType : 1,
                midBtnFunction : function () {
                    this.onClosePopupPnlClick()
                    this.onSeizeBtnClick()
                }
            }
            switch (status) {
                case 0:
                    msg.titleName = '兑换成功'
                    msg.wordsText = '我们将在2-3个工作日内向您发货'
                    break
                case 100:
                    msg.wordsText = '当论夺宝已结束，请参加下一轮夺宝'
                    break
                case 7:
                    msg.wordsText = '您的福卡不足，请充值后再参与夺宝'
                    break
                case 101:
                    msg.wordsText = '已超上限，请核实后再参与夺宝'
                    break
                default:
                    msg.wordsText = '夺宝失败，请刷新后再操作'
                    break

            }

             this.onShowPopubUI(msg)
        },

        buyGoodsResult: function (data) {

            let status = data.status
            let msg = {
                titleName : '提示',
                contextType : 1,
                midBtnFunction : function () {
                    this.onClosePopupPnlClick()
                    if(this.this._currentLayer == 'goodsList')
                        this.this.onGetGoodsList(this._currentMenuItemCode,1)
                    else if(this.this._currentLayer == 'cardExchange')
                        this.onCardExchangeBtnClick()
                    else if(this.this._currentLayer == 'objectExchange')
                        this.onObjectExchangeBtnClick()
                }
            }
            switch (status) {
                case 0:
                    msg.titleName = '兑换成功'
                    msg.wordsText = '我们将在2-3个工作日内向您发货'
                    break
                case 93:
                    msg.wordsText = '未知商品，请您刷新后重试'
                    break
                case 1:
                    msg.wordsText = '购买失败，请您刷新后重试'
                    break
                case 99:
                    msg.wordsText = '您还未完整填写收货地址，为保证\n' +
                        '货物顺利到达，请完善收货地址'
                    break
                case 73:
                    msg.wordsText = '福卡不足，请您充值后重试'
                    break
                default:
                    msg.wordsText = '购买失败，请您刷新后重试'
                    break

            }

            this.onShowPopubUI(msg)
        },

        onGetRobLogClick: function () {
            let elementNameArray = [
                'robLogPnl'
            ]

            this.onSetShowElementFunction(elementNameArray)

            this.onGetRobLogData()

        },

        onGetRobLogData: function () {

            appInstance.gameAgent().httpGame().FUKAROBLOGReq()
        },


        onForMatRobLogData: function (robList) {

            this.robLogListView.removeAllChildren()
            for(let i = 0; i < robList.length; i++){
                let data = robList[i]
                let cell = this.robLogCell.clone()
                cell.setVisible(true)
                cell.getChildByName('numText').setString(data.bettingNum)
                cell.getChildByName('timeText').setString(data.createTime)

                robLogListView.pushBackCustomItem(cell)

            }

        },

        onGetGiftLogClick: function () {

            let elementNameArray = [
                'rewardsLogPnl',
                'logPnl'
            ]

            this.onSetShowElementFunction(elementNameArray)
            this.onGetGiftLogData()
        },

        onGetGiftLogData: function () {
            appInstance.gameAgent().httpGame().FUKAMATERIALOGReq()
        },

        onFormatMaterialLog: function (logList) {

            this.rewardsLogListView.removeAllChildren()
            this._giftLogList = logList

            for(let i = 0; i < logList.length; i++){

                let data = logList[i]
                let cell = this.rewardsLogCell.clone()
                cell.setVisible(true)
                this.rewardsLogListView.pushBackCustomItem(cell)

                cell.setName(data.orderCode)
                cell.getChildByName('rewardsImg').loadTexture(data.goodsUrl)
                cell.getChildByName('goodsNameText').setString(data.goodsName)
                cell.getChildByName('orderNumberValueText').setString(data.expressCode)
                cell.getChildByName('timeDaysText').setString(data.timeDays)
                cell.getChildByName('timeHoursText').setString(data.timeHours)
                cell.getChildByName('statusText').setString(data.statusText)
                cell._sendMsg = {
                    orderCode : data.orderCode,
                    num : data.num,
                    propValue : data.propValue,
                }


                if(data.status == 0){
                    cell.getChildByName('exchangeBtn').setVisible(true)
                    cell.getChildByName('copyBtn').setVisible(false)
                    cell.getChildByName('exchangeBtn').addClickEventListener(function (sender,dt) {
                        this.onExchangeOnline(cell._sendMsg)
                    }.bind(this))

                }else{
                    cell.getChildByName('exchangeBtn').setVisible(false)
                    if(data.status == 2 && data.propValue && data.propValue.substr(0,2) != '3,3')
                        cell.getChildByName('copyBtn').setVisible(true)
                    else
                        cell.getChildByName('copyBtn').setVisible(false)

                }

            }

        },

        onExchangeOnline: function (data) {

            let elementNameArray = [
                'rewardsLogPnl',
                'phoneExchangePnl'
            ]
            this.onSetShowElementFunction(elementNameArray)
            this.rechargePhoneBtn.addClickEventListener(function (sender,et) {
                this.onRechargePhoneBtnClick(data.orderCode)
            }.bind(this))
        },

        onExchangeOnlineResult: function (data) {

            let status = data.status
            let orderCode = data.orderCode
            let isRefresh = true
            switch (status) {
                case 0:
                    isRefresh = false
                    appInstance.gameAgent().Tips('兑换成功')
                    this.onChangeRewardsCellData(orderCode)
                    break
                case 106:
                    appInstance.gameAgent().Tips('订单不存在')
                    break
                case 108:
                    appInstance.gameAgent().Tips('订单已处理')
                    break
                case 67:
                    isRefresh = false
                    appInstance.gameAgent().Tips('手机号格式异常，请检查后重新输入')
                    break
                case 107:
                    isRefresh = false
                    appInstance.gameAgent().Tips('该商品不可兑换，请联系客服')
                    break
                default:
                    isRefresh = false
                    appInstance.gameAgent().Tips('充值失败，请重试')
                    break
            }

            if(isRefresh){
                this.onGetGiftLogClick()
            }

        },

        onChangeRewardsCellData: function (orderCode) {

            let cell =  this.rewardsLogListView.getChildByName(orderCode)
            if(cell){
                cell.getChildByName('statusText').setString('处理中')
                cell.getChildByName('exchangeBtn').setVisible(false)
                cell.getChildByName('copyBtn').setVisible(false)
            }

            let elementNameArray = [
                'rewardsLogPnl',
                'logPnl'
            ]
            this.onSetShowElementFunction(elementNameArray)

        }

    })
    return Layer
})
