
load('game/ui/layer/fukashop/FukaShopLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let FukaShopMdt = include('game/ui/layer/fukashop/FukaShopMdt')
    let GameEvent = include('game/config/GameEvent')
    let Layer = BaseLayer.extend({
        _className: 'FukaShopLayer',
        _menuItemName: 'menuItem_',//菜单cell名称前缀
        _menuStartIndex: 0,//菜单栏(主页)开始位
        _menuIndexLength: 9,//菜单栏（主页）一页显示几条
        _canRefreshMenuList : true,
        _canRefreshCardExchangeList : true,
        _canRefreshObjectExchangeList : true,
        _canRefreshRobList : true,
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
        _currentLayer : 'goodsListPnl',
        _searchText : '',//搜索框查询字符串

        RES_BINDING: function () {
            return {
                'goodsListPnl' : {},
                'goodsListPnl/leftPnl/searchBtn' : { onClicked : this.onsearchBtnClick},
                'goodsListPnl/leftPnl/searchTextFiled' : {},
                'goodsListPnl/leftPnl/imgPageView' : { },
                'goodsListPnl/leftPnl/imgPageCell' : { },

                'goodsListPnl/midPnl/cardExchangeBtn' : { },
                'goodsListPnl/midPnl/seizeBtn' : {},
                'goodsListPnl/midPnl/objectExchangeBtn' : {},


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
                'goodsDetailsPnl/detailBottomPnl/detailImgPnl' : {},
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
                'commonPnl/giftLogBtnPnl' : {},
                'commonPnl/robLogBtnPnl' : { },
                'commonPnl/returnBtn' : { onClicked: this.onReturnBtnClick},


               // 'popupPnl' : { onClicked: this.onClosePopupPnlClick},
                'popupPnl' : { },
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

            this.giftLogBtnPnl._sendMsg = {
                actionType : 'click'
            }
            this.giftLogBtnPnl.addClickEventListener(function (sender,dt) {
                this.onGetGiftLogClick(sender)
            }.bind(this))

            this.robLogBtnPnl._sendMsg = {
                actionType : 'click'
            }
            this.robLogBtnPnl.addClickEventListener(function (sender,dt) {
                this.onGetRobLogClick(sender)
            }.bind(this))

            this.cardExchangeBtn._sendMsg = {
                actionType : 'click'
            }
            this.cardExchangeBtn.addClickEventListener(function (sender,dt) {
                this.onCardExchangeBtnClick(sender)
            }.bind(this))

            this.seizeBtn._sendMsg = {
                actionType : 'click'
            }
            this.seizeBtn.addClickEventListener(function (sender,dt) {
                this.onSeizeBtnClick(sender)
            }.bind(this))

            this.objectExchangeBtn._sendMsg = {
                actionType : 'click'
            }
            this.objectExchangeBtn.addClickEventListener(function (sender,dt) {
                this.onObjectExchangeBtnClick(sender)
            }.bind(this))

            this.jumpArrayStep = [

            ]

            this.searchTextFiled.addEventListener(this.onTextFieldListener,this)

            this.fuKaPnl.getChildByName('fuKaCnt').setString(appInstance.dataManager().getUserData().fuKa)
            this.fuKaPnl.getChildByName('fuKaCnt').setTextColor(cc.color(255,255,255))
            this.giftLogBtnPnl.getChildByName('giftLogPnlNameText').setTextColor(cc.color(255,255,255))
            this.robLogBtnPnl.getChildByName('robLogPnlNameText').setTextColor(cc.color(255,255,255))
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
            this.exangeListView.addEventListener(this.selectedItemEvent,this)
            this.robListView.addEventListener(this.selectedItemEvent,this)

        },

        onTextFieldListener: function(sender,dt){

            cc.log('-------------------------onTextFieldListener--------------dt ：' + dt )
            cc.log('-------------------------onTextFieldListener--------------dt ：' + ccui.TextField.EVENT_ATTACH_WITH_IME )
            cc.log('-------------------------onTextFieldListener--------------dt ：' + ccui.TextField.EVENT_DETACH_WITH_IME )
            cc.log('-------------------------onTextFieldListener--------------dt ：' + ccui.TextField.EVENT_INSERT_TEXT )
            cc.log('-------------------------onTextFieldListener--------------dt ：' + ccui.TextField.EVENT_DELETE_BACKWARD )

        },

        selectedItemEvent: function (sender, type) {
            if (type !== ccui.ListView.EVENT_SELECTED_ITEM) {
                return
            }

            let curIndex = sender.getCurSelectedIndex()//当前手指点击的行，坐标以0开始
            let offIndex //一页展示的子节点cell
            let _functionName
            let childLen = sender.getItems().length

            if(sender == this.goodsListView){
                offIndex = 3
                _functionName = function () {
                    if(!this._canRefreshMenuList){
                        appInstance.gameAgent().Tips('---------------到底了----------------')
                        return
                    }
                    this.onGetGoodsList(this._currentMenuItemCode,1)
                }.bind(this)
            }else if(sender == this.exangeListView){
                offIndex = 5
                if(this._currentLayer == 'cardExchange'){
                    _functionName = function () {
                        if(!this._canRefreshCardExchangeList ){
                            appInstance.gameAgent().Tips('---------------到底了----------------')
                            return
                        }
                        this.onGetCardListData(this._cardListStartIndex)
                    }.bind(this)
                }else{
                    _functionName = function () {
                        if(!this._canRefreshObjectExchangeList ){
                            appInstance.gameAgent().Tips('---------------到底了----------------')
                            return
                        }
                        this.onGetMateriaListData(this._materiaListStartIndex)
                    }.bind(this)
                }
            }else{
                offIndex = 3
                _functionName = function () {
                    if(!this._canRefreshRobList  ){
                        appInstance.gameAgent().Tips('---------------到底了----------------')
                        return
                    }
                    this.onGetRobListData(this._robListStartIndex)
                }.bind(this)

            }


            if ( curIndex > (childLen - offIndex)) {

                _functionName()
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

            this._searchText = this.searchTextFiled.getString()
            this._menuStartIndex = 0
            let searchType = 0
            if(this._searchText == ''){
                cc.log('------------------ onsearchBtnClick : ' + this._searchText)
                searchType = 1
            }


            this.onGetGoodsList(this._currentMenuItemCode,searchType)

        },

        onCardExchangeBtnClick: function (sender) {
            let elementNameArray = [
                'cardExchange',
                'exchangePnl'
            ]

            let flag = false
            if(sender)
                flag = true

            this.onSetShowElementFunction(elementNameArray,flag)
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

        onObjectExchangeBtnClick: function (sender) {
            let elementNameArray = [
                'objectExchange',
                'exchangePnl'
            ]

            let flag = false
            if(sender)
                flag = true

            this.onSetShowElementFunction(elementNameArray,flag)
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

        onSeizeBtnClick: function (sender) {

            let elementNameArray = [
                'robPnl'
            ]

            let flag = false
            if(sender)
                flag = true

            this.onSetShowElementFunction(elementNameArray,sender)
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
                msg.midBtnFunction = function () {
                    this.onClosePopupPnlClick()
                }.bind(this)

                this.onShowPopubUI(msg)
                return
            }

            let fuka =  this.fuKaPnl.getChildByName('fuKaCnt').getString()
            if(fuka >= price){

                msg.wordsText = '您确定兑换吗'
                msg.leftBtnFunction = function () {
                    this.onClosePopupPnlClick()
                }.bind(this)

                msg.rightBtnFunction = function () {
                    let sendMsg = {
                        goodsid : goodsId
                    }
                    appInstance.gameAgent().httpGame().FUKABUYGOODSReq(sendMsg)
                    return true
                }

            }else{

                msg.wordsText = '您的福卡不足'
                msg.midBtnFunction = function () {
                    this.onClosePopupPnlClick()
                }.bind(this)


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
            this.imgPageView.removeAllChildren()
            for(let i = 0; i < data.length; i++){
                let cell = this.imgPageCell.clone()
                cell.setVisible(true)
                this.imgPageView.pushBackCustomItem(cell)
               // let size = cell.getContentSize()
                let size = {
                    height : 227.00,
                    width : 629.00
                }
                //let url = 'http://p3.itc.cn/q_70/images03/20200911/b7c565cbc87848538cace549fb609e7b.jpeg'
               let  url = data[i].outerPictureUrl
                this.onLoadUrlImg(url,size,cell)
            }

        },

        onLoadUrlImg: function (url,size,cell) {
            cc.loader.loadImg(url, { isCrossOrigin: false },function(err,texture){
                if (!err && texture) {
                    let sp = new cc.Sprite(texture)
                    sp.setContentSize(size)
                    sp.setPosition(cc.p(size.width/2,size.height/2))
                    //sp.setRotationX(-90)
                    cell.addChild(sp)
                }
            })
        },

        initMenuList: function (data) {

            this.menuListView.removeAllChildren()
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

            this._menuStartIndex = 0
            let data = sender._sendMsg
            if(data.status != 0)
                return

            this.onGetGoodsList(data.goodsCode,1)

        },

        onGetGoodsList: function (goodsCode,type) {
            let msg = {
                goodsCode : goodsCode,
                selectStr : this._searchText,
                type : type,
                startIndex : this._menuStartIndex,
                indexLength : this._menuIndexLength,
            }

            appInstance.gameAgent().httpGame().GOODSLISTReq(msg)
        },

        onUpdateMenuGoodsListFunction: function (data) {

            this.onUpdateMenuCell(data.currGoodsCode)
            this.onUpdateMenuGoodsList(data.currGoodsIntoList)

        },

        onUpdateMenuCell: function (goodsCode) {

            let staticParams = {}
            let cellStatusParams = {}

            if(this._currentMenuItemCode == goodsCode){

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

            this.onForMatGoodsList(data,'_menuStartIndex','goodsListView','goodsPnl','_menuIndexLength','_canRefreshMenuList',3,20,'goodsCell',220,'goodsPriceText','goodsImgPnl',200,168)

        },

        onForMatGoodsList: function (data,startIndex,listViewName,listPnlName,_dataLength,canRefreshName,rowLength,rowInterval,cellName,cellInterval,fuKaNumName,imgName,sizeHight,sizeWidth) {
            if(this[startIndex] == 0)
                this[listViewName].removeAllChildren()

            let dataLength = data.length
            this[startIndex] = this[startIndex] + dataLength
            if(dataLength < this[_dataLength])
                this[canRefreshName] = false
            else
                this[canRefreshName] = true

            let rowNum = Math.ceil(data.length / rowLength)
            for(let i = 0; i < rowNum; i++){

                let goodsPnl = this[listPnlName].clone()
                this[listViewName].pushBackCustomItem(goodsPnl)
                goodsPnl.setVisible(true)
                goodsPnl.setPositionY(0)
                goodsPnl.setPositionX((this[listViewName].getChildrenCount() + i) * 20)


                for(let j = 0; j < rowLength; j++){
                    this.onInitMenuCellData(data,goodsPnl,rowLength * i+j,rowLength,cellName,cellInterval,fuKaNumName,imgName,sizeHight,sizeWidth)
                }


            }
        },

        onInitMenuCellData: function (data,goodsPnl,index,rowLength,cellName,cellInterval,fuKaNumName,imgName,sizeHight,sizeWidth) {

            let goodsData = data[index]
            if(!goodsData)
                return
            let cell = this[cellName].clone()
            cell.setVisible(true)
            cell.setPositionX(0)
            cell.setPositionY((index % rowLength) * cellInterval)
            goodsPnl.addChild(cell)

            cell.getChildByName(fuKaNumName).setString(goodsData.fuKaNums+'福卡')
            cell._sendMsg = {
                goodsName : goodsData.goodsName,
                goodsDesc : goodsData.goodsDesc,
                fuKaNums : goodsData.fuKaNums,
                detailsPictureUrl : goodsData.detailsPictureUrl,
                upPictureUrls : goodsData.upPictureUrls,
                goodsId : goodsData.goodsId
            }

            let size = {
                height : sizeHight,
                width : sizeWidth
            }
            //this.onLoadUrlImg('http://47.105.94.107:80/public/uploads/goods_image/5f688a4bbdfe8.jpg',size,cell.getChildByName(imgName))
           // this.onLoadUrlImg('https://bg-test-mj.heheshow.cn/public/uploads/goods_image/5f688a4bbdfe8.jpg',size,cell.getChildByName(imgName))
            this.onLoadUrlImg(goodsData.hallPictureUrl,size,cell.getChildByName(imgName))
            //this.onLoadUrlImg('http://p3.itc.cn/q_70/images03/20200911/b7c565cbc87848538cace549fb609e7b.jpeg',size,cell.getChildByName(imgName))
            cell.addClickEventListener(function (sender,dt) {
                this.onShowGoodsDetailMsg(sender)
            }.bind(this))

        },


        onShowGoodsDetailMsg: function (sender) {


            let elementNameArray = [
                'goodsDetailsPnl'
            ]

            let flag = false
            if(sender)
                flag = true

            this.onSetShowElementFunction(elementNameArray,flag)

            let data = sender._sendMsg
            for(let i = 0; i < data.upPictureUrls.length; i++){

                let cell = this.goodsDetailCell.clone()
                cell.setVisible(true)
                this.goodDetailPageView.pushBackCustomItem(cell)
                let size = {
                    height : 556,
                    width : 416
                }

                this.onLoadUrlImg(data.upPictureUrls[i],size,cell)

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

                let size = {
                    height : 395.00,
                    width : 709.00
                }
                let  url = data.detailsPictureUrl
                this.onLoadUrlImg(url,size,this.detailImgPnl)

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

        onSetShowElementFunction: function (elementNameArray,flag) {

            if(flag && this.jumpArrayStep.indexOf(this._currentLayer) == -1)
                this.jumpArrayStep.push(this._currentLayer)

            this._currentLayer = elementNameArray[0]
            if(this._currentLayer == 'goodsListPnl'){
                this.fuKaPnl.getChildByName('fuKaCnt').setTextColor(cc.color(255,255,255))
                this.giftLogBtnPnl.getChildByName('giftLogPnlNameText').setTextColor(cc.color(255,255,255))
                this.robLogBtnPnl.getChildByName('robLogPnlNameText').setTextColor(cc.color(255,255,255))
            }else{
                this.fuKaPnl.getChildByName('fuKaCnt').setTextColor(cc.color(197,195,194))
                this.giftLogBtnPnl.getChildByName('giftLogPnlNameText').setTextColor(cc.color(224,82,65))
                this.robLogBtnPnl.getChildByName('robLogPnlNameText').setTextColor(cc.color(224,82,65))
            }

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

                    this.contextPnl.getChildByName('robPnl').getChildByName('fuKaFiled').setString('')
                    this.contextPnl.getChildByName('robPnl').getChildByName('fuaVauleText').setString(msg.fuaVauleText)

                    this.contextPnl.getChildByName('robPnl').getChildByName('prizePercentText').setString(msg.currentNum + '/' + msg.allNum)
                    this.contextPnl.getChildByName('robPnl').getChildByName('prizeLoadingBar').setPercent(msg.currentNum / msg.allNum * 100)
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
                this.btnsPnl.getChildByName('rightBtn').setEnabled(true)
                this.btnsPnl.getChildByName('rightBtn').setBright(true)
                this.btnsPnl.getChildByName('rightBtn').addClickEventListener(function (sender,dt) {
                    let flag = msg.rightBtnFunction()
                    if(flag){
                        sender.setEnabled(false)
                        sender.setBright(false)
                    }
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

            this.onForMatGoodsList(data,'_cardListStartIndex','exangeListView','exchangeListPnl','_cardListIndexLength','_canRefreshCardExchangeList',3,20,'exangeGoodsCell',220,'goodsPriceText','goodsImgPnl',200,168)

        },

        onUpdateMaterialListFunction: function (data) {

            this.onForMatGoodsList(data,'_materiaListStartIndex','exangeListView','exchangeListPnl','_materiaListIndexLength','_canRefreshObjectExchangeList',3,20,'exangeGoodsCell',220,'goodsPriceText','goodsImgPnl',200,168)

        },

        onForMatRobList: function (data) {
            if(this._robListStartIndex == 0)
                this.robListView.removeAllChildren()


            if(data.length < this._robListIndexLength  )
                this._canRefreshRobList  = false
            else
                this._canRefreshRobList  = true

            let rowLength = 2
            let rowNum = Math.ceil(data.length / rowLength)
            for(let i = 0; i < rowNum; i++){

                let goodsPnl = this.robListPnl.clone()
                goodsPnl.setVisible(true)
                this.robListView.pushBackCustomItem(goodsPnl)
                goodsPnl.setPositionY(0)
                goodsPnl.setPositionX((this.robListView.getChildrenCount() + i) * 20)


                for(let j = 0; j < rowLength; j++){
                    this.onInitRobGoodsCell(data,goodsPnl,rowLength * i+j,rowLength)
                }


            }
        },

        onInitRobGoodsCell: function (data,goodsPnl,index,rowLength) {

            let goodsData = data[index]
            let cell = this.robGoodsCell.clone()
            goodsPnl.addChild(cell)

            cell.setVisible(true)
            cell.setPositionX(0)
            cell.setPositionY((index % rowLength) * 300)

            cell.getChildByName('robGoodsNameText').setString(goodsData.goodsName)
            cell.getChildByName('goodImg').loadTexture(goodsData.hallPictureUrl)
            cell.getChildByName('robLoadingBar').setPercent(goodsData.currentNum / goodsData.allNum * 100)
            cell.getChildByName('robPercent').setString(goodsData.currentNum + '/' + goodsData.allNum)
            cell.getChildByName('numText').setString(goodsData.playerConsumeFuka)
            cell._sendMsg = {
                currentNum : goodsData.currentNum,
                allNum : goodsData.allNum,
                fuKaCnt : goodsData.fuKaCnt,
                nickName : goodsData.nickName,
                sdkPhotoUrl : goodsData.sdkPhotoUrl,
                goodsId : goodsData.goodsid,
                roundNum : goodsData.duoBaoNum,
                duoBaoId : goodsData.duoBaoId,
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
                midBtnFunction : function () {
                    this.onClosePopupPnlClick()
                }.bind(this)
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
                leftBtnFunction : function () {
                    this.onClosePopupPnlClick()
                }.bind(this),

                rightBtnFunction : function () {

                    let robFuKaNum = this.contextPnl.getChildByName('robPnl').getChildByName('fuKaFiled').getString()
                    let fuKaCnt = this.contextPnl.getChildByName('robPnl').getChildByName('fuaVauleText').getString()
                    let diffCntArray = this.contextPnl.getChildByName('robPnl').getChildByName('prizePercentText').getString().split('/')
                    let diffCnt = diffCntArray[1] - diffCntArray[0]
                    if(robFuKaNum < fuKaCnt){
                        appInstance.gameAgent().Tips('您的福卡不足')
                        return false
                    }

                    if(!this.onTest(robFuKaNum)){
                        appInstance.gameAgent().Tips('您输入的福卡数量有误，请检查后重新输入')
                        return false
                    }

                    if(robFuKaNum > diffCnt){
                        appInstance.gameAgent().Tips('您的福卡数量超过剩余值，请核实后再次夺宝')
                        return false
                    }

                    let msg = {
                        duoBaoId : data.duoBaoId,
                        duoBaoNum : data.roundNum,
                        fukaNum : robFuKaNum,
                    }

                    appInstance.gameAgent().httpGame().FUKAROBReq(msg)

                    return true

                }.bind(this)
            }

            this.onShowPopubUI(msg)

        },

        onTest: function (str) {
            if (str == '')
                return false
            if (!(/(^[1-9]\d*$)/.test(str)))
                return false
            return true
        },

        onRobResult: function (data) {

            let status = data.status
            let msg = {
                titleName : '提示',
                contextType : 1,
                midBtnFunction : function () {
                    this.onClosePopupPnlClick()
                    this.onSeizeBtnClick()
                }.bind(this)
            }
            switch (status) {
                case 0:
                    msg.titleName = '兑换成功'
                    msg.wordsText = '恭喜您参与成功，敬请期待夺宝结果'
                    break
                case 100:
                    msg.wordsText = '当论夺宝已结束，请参加下一轮夺宝'
                    break
                case 73:
                    msg.wordsText = '您的福卡不足，请充值后再参与夺宝'
                    break
                case 101:
                    msg.wordsText = '已超上限，请核实后再参与夺宝'
                    break
                case 110:
                    msg.wordsText = '参与福卡数量不可为0，请重新参与夺宝'
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
                    if(this.this._currentLayer == 'goodsListPnl'){
                       this.onShowGoodsListLayer()
                    }else if(this.this._currentLayer == 'cardExchange'){
                        this.onCardExchangeBtnClick()
                    }else if(this.this._currentLayer == 'objectExchange'){
                        this.onObjectExchangeBtnClick()
                    }

                }.bind(this)
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

        onGetRobLogClick: function (sender) {
            let elementNameArray = [
                'robLogPnl'
            ]

            let flag = false
            if(sender)
                flag = true

            this.onSetShowElementFunction(elementNameArray,flag)

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
                cell.setPositionX(0)
                cell.setPositionY(80 * i)
                cell.getChildByName('numText').setString(data.bettingNum)
                cell.getChildByName('timeText').setString(data.createTime)

                this.robLogListView.pushBackCustomItem(cell)

            }

        },

        onGetGiftLogClick: function (sender) {

            let flag = false
            if(sender)
                flag = true

            let elementNameArray = [
                'logPnl',
                'rewardsLogPnl'

            ]

            this.onSetShowElementFunction(elementNameArray,flag)
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
                'phoneExchangePnl',
                'rewardsLogPnl'

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
                'logPnl',
                'rewardsLogPnl'

            ]
            this.onSetShowElementFunction(elementNameArray)

        },

        onUpdateUserData: function (data) {

            if(data.hasOwnProperty('fuKa')){
                this.fuKaPnl.getChildByName('fuKaCnt').setString(data.fuKa)
            }

        },

        onReturnBtnClick: function () {

            if(this.jumpArrayStep.length == 0){
                appInstance.sendNotification(GameEvent.HALL_RED_GET)
                appInstance.uiManager().removeUI(this)
            }else{
                let lastStepName = this.jumpArrayStep.pop()
                switch (lastStepName) {
                    case 'logPnl':
                        this.onGetGiftLogClick()
                        break
                    case 'phoneExchangePnl':
                        this.onReturnBtnClick()
                        break
                    case 'robLogPnl':
                        this.onReturnBtnClick()
                        break
                    case 'goodsDetailsPnl':
                        this.onReturnBtnClick()
                        break
                    case 'robPnl':
                        this.onSeizeBtnClick()
                        break
                    case 'objectExchange':
                        this.onObjectExchangeBtnClick()
                        break
                    case 'cardExchange':
                        this.onCardExchangeBtnClick()
                        break
                    case 'goodsListPnl':
                        this.onShowGoodsListLayer()
                        break
                    default:
                        appInstance.uiManager().removeUI(this)
                        break

                }
            }

        },

        onShowGoodsListLayer: function () {

            let elementNameArray = [
                'goodsListPnl',
                'goodsPnl'

            ]

            this.onRefreshGoodsListParams()
            this.onSetShowElementFunction(elementNameArray)
            appInstance.gameAgent().httpGame().ROLLIMGLISTReq()
            appInstance.gameAgent().httpGame().MENULISTReq()

        },

        onRefreshGoodsListParams: function (){
            this._menuStartIndex = 0
            this.searchTextFiled.setString('')
            this._searchText = ''
        },

    })
    return Layer
})
