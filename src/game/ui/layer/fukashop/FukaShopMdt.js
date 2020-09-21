
/**
 *  TaskMdt Mediator
 *
 */
load('game/ui/layer/fukashop/FukaShopMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let mdt = Mediator.extend({
        mediatorName: 'FukaShopMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.GETROLLIMGLIST,
                GameEvent.MENULIST,
                GameEvent.GOODSLIST,
                GameEvent.FUKA_BUGGOODS,
                GameEvent.FUKA_ROBLIST,
                GameEvent.FUKA_CARDLIST,
                GameEvent.FUKA_ROB,
                GameEvent.FUKA_ROBLOG,
                GameEvent.FUKA_MATERIAL_LIST,
                GameEvent.FUKA_MATERIA_LOG,
                GameEvent.FUKA_MATERIA_EXCHANGE,
                GameEvent.UPDATE_PROPSYNC,

            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.GETROLLIMGLIST:
                    this.initRollImgList(body)
                    break
                case GameEvent.MENULIST:
                    this.initMenuList(body)
                    break
                case GameEvent.GOODSLIST:
                    this.onUpdateMenuGoodsList(body)
                    break
                case GameEvent.FUKA_BUGGOODS:
                    this.view.buyGoodsResult(body)
                    break
                case GameEvent.FUKA_CARDLIST:
                    this.onForMatCardList(body)
                    break
                case GameEvent.FUKA_MATERIAL_LIST:
                    this.onForMatMaterialList(body)
                    break
                case GameEvent.FUKA_ROBLIST:
                    this.onForMatRobList(body)
                    break
                case GameEvent.FUKA_ROB:
                    this.view.onRobResult(body)
                    break
                case GameEvent.FUKA_ROBLOG:
                    this.onForMatRobLogData(body)
                    break
                case GameEvent.FUKA_MATERIA_LOG:
                    this.onFormatMaterialLog(body)
                    break
                case GameEvent.FUKA_MATERIA_EXCHANGE:
                    this.view.onExchangeOnlineResult(body)
                    break
                case GameEvent.UPDATE_PROPSYNC:
                    this.view.onUpdateUserData(body)
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
            appInstance.gameAgent().httpGame().ROLLIMGLISTReq(msg)
            appInstance.gameAgent().httpGame().MENULISTReq(msg)

        },

        initRollImgList: function (body) {

            let data = []
            for(let i = 0; i < body.outGoodsList.length; i++){
                let imgUrl = body.outGoodsList[i]
                data.push(imgUrl)
            }

            this.view.initRollImgList(data)
        },


        initMenuList: function (body) {

            let data = []
            for(let i = 0; i < body.goodsType.length; i++){

                let menuData = {}
                let menu = body.goodsType[i]

                menuData.goodsCode = menu.goodsCode
                menuData.name = menu.name
                menuData.res = this.getMenuImg(menuData.goodsCode)

                data.push(menuData)
            }

            this.view.initMenuList(data)
        },

        getMenuImg: function (code) {

            let res
            switch (code) {
                case 1:
                    res = 'res/fukashop/fk_15.png'
                    break
                case 2:
                    res = 'res/fukashop/fk_16.png'
                    break
                case 3:
                    res = 'res/fukashop/fk_17.png'
                    break
                case 4:
                    res = 'res/fukashop/fk_18.png'
                    break
                case 5:
                    res = 'res/fukashop/fk_19.png'
                    break
                default:
                    res = 'res/fukashop/fk_15.png'
                    break

            }

            return res

        },

        onForMatCardList: function (body) {

            let data = []
            this.onFormatGoodsData(data,body.goodsInfoList)
            this.view.onUpdatecardListFunction(data)

        },

        onForMatMaterialList: function (body) {

            let data = []
            this.onFormatGoodsData(data,body.goodsInfoList)
            this.view.onUpdateMaterialListFunction(data)

        },

        onUpdateMenuGoodsList: function (body) {

            let data = {}
            data.currGoodsCode = body.currGoodsCode
            data.currGoodsIntoList = []

            this.onFormatGoodsData(data.currGoodsIntoList,body.currGoodsIntoList)

            this.view.onUpdateMenuGoodsListFunction(data)


        },

        onFormatGoodsData: function (data,body) {
            for(let i = 0; i < body.length; i++){

                let info = body[i]
                let infoData = {}

                infoData.goodsId = info.id
                infoData.goodsName = info.goodsName
                infoData.goodsDesc = info.goodsDesc
                infoData.hallPictureUrl = info.hallPictureUrl
                infoData.fuKaNums = info.fuKaNums
                infoData.detailsPictureUrl = info.detailsPictureUrl
                infoData.upPictureUrls = info.upPictureUrl.split(';')

                data.push(infoData)

            }
        },

        onForMatRobList: function (body) {

            let data = []
            for(let i = 0; i < body.fukaDuoBaoList.length; i++){

                let baoData = {}
                let info = body.fukaDuoBaoList[i]

                baoData.duoBaoId = info.duoBaoId
                baoData.goodsid = info.goodsid
                baoData.allNum = info.fuKaNum
                baoData.currentNum = info.process
                baoData.playerConsumeFuka =  '我已夺宝' + info.playerConsumeFuka + '次'
                baoData.goodsNum = info.goodsNum
                baoData.goodsName = info.goodsName
                baoData.hallPictureUrl = info.hallPictureUrl
                baoData.nickName = info.nickName
                baoData.sdkPhotoUrl = info.sdkPhotoUrl
                baoData.duoBaoNum = info.duoBaoNum
                baoData.fuKaCnt = appInstance.dataManager().getUserData().fuka


                data.push(baoData)

            }

            this.view.onForMatRobList(data)

        },

        onForMatRobLogData: function (body) {

            let data = []
            for(let i = 0; i < body.duoBaoLogList.length; i++){
                let log = body.duoBaoLogList[i]
                let logData = {}

                logData.bettingNum = log.bettingNum
                logData.createTime = this.onFormatTimesOne(log.createTime)

                data.push(logData)
            }

            this.view.onForMatRobLogData(data)
        },

        onFormatMaterialLog: function (body) {



            let data = []

            for(let i = 0; i < body.playerGoodsLogList.length; i++){
                let log = body.playerGoodsLogList[i]
                let logData = {}

                logData.orderCode = log.orderCode
                logData.expressCode = log.expressCode
                logData.goodsName = log.goodsName
                logData.goodsUrl = log.goodsUrl
                logData.num = log.num
                logData.status = log.status
                logData.propValue = log.propValue

                if(logData.status == 0){
                    logData.statusText = '未处理'
                }else if(logData.status == 1){
                    logData.statusText = '处理中'
                }else if(logData.status == 2){
                    logData.statusText = '已完成'
                }else{
                    logData.statusText = '处理失败'
                }

                this.onFormatTimes(log.createTime,logData)

                data.push(logData)
            }
            this.view.onFormatMaterialLog(data)
        },




        onFormatTimesOne: function (timestamp) {

            let date = new Date(timestamp)
            let time = (this.padLeftZero(date.getMonth() + 1)) + '-' + (this.padLeftZero(date.getDate())) + '  ' + (this.padLeftZero(date.getHours())) + ":" + (this.padLeftZero(date.getMinutes()))
            return time
        },

        onFormatTimes: function (timestamp,data) {

            let date = new Date(timestamp)
            data.timeDays = (date.getFullYear()) + "-" + (this.padLeftZero(date.getMonth() + 1)) + "-" + (this.padLeftZero(date.getDate()))
            data.timeHours = (this.padLeftZero(date.getHours())) + ":" + (this.padLeftZero(date.getMinutes()))

        },


        padLeftZero: function (str) {

            return ('00' + str).substr(str.toString().length);
        }
    })
    return mdt
})