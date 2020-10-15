
/**
 *  Turntable Mediator
 *
 */
load('game/ui/layer/turntable/TurnTableMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let mdt = Mediator.extend({
        mediatorName: 'TurnTableMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.TURNTABLE_INIT,
                GameEvent.TURNTABLE_POINT,
                GameEvent.TURNTABLE_RECEIVE,
                GameEvent.TURNTABLE_LUCKY_PRIZE,
                GameEvent.TURNTABLE_PLAYERLOG
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.TURNTABLE_INIT:
                    this.turnTableInit(body)
                    break
                case GameEvent.TURNTABLE_POINT:
                    this.onTurnPointResult(body)
                    break
                case  GameEvent.TURNTABLE_RECEIVE:
                    this.onReceiveAwardsResult(body)
                    break
                case  GameEvent.TURNTABLE_LUCKY_PRIZE:
                    this.view.updateUserDataView(body.luckyPrize)
                    break
                case  GameEvent.TURNTABLE_PLAYERLOG:
                    this.view.onShowRecordPnlClick(body.turntableLogList)
                    break
                default:
                    break
            }
        },

        turnTableInit: function (body) {

            let tableData = {}
            let configList = {}

            for (let i = 0; i < body.configList.length; ++i) {
          //      this._goodsArray['goods_' + i] = this.goodsNd.getChildByName('goods' + i)
                let config = {}
                let configData = body.configList[i]
                config.name = configData.describe
                config.turntableId = configData.turntableId

                GameUtil.getPropData(configData,config,GameUtil.CURRENCYTYPE_2,GameUtil.UNITLOCATION_NULL,'',GameConfig.FUNCTION_NAME_TURNTABLE)
                configList[i] = config
            }

            tableData.code = body.code
            tableData.configList = configList
            tableData.luckyPrize = body.luckyPrize

            this.view.onDataInit(tableData)
        },

        onTurnPointResult: function (body) {

            let _resultData = {}

            _resultData.multiple = body.multiple
            _resultData.turntableId = body.turntableId

            GameUtil.getPropData(body,_resultData,GameUtil.CURRENCYTYPE_1,GameUtil.UNITLOCATION_BEFORE,'x')

            this.view.onTurnPointResult(_resultData)
        },

        onReceiveAwardsResult: function (body) {

            let _resultData = {}

            _resultData.code = body.code
            GameUtil.getPropData(body,_resultData,GameUtil.CURRENCYTYPE_1,GameUtil.UNITLOCATION_BEFORE,'x')

            this.view.onReceiveAwardsResult(_resultData)
        },

        onRegister: function () {

            this.initView()
        },

        onRemove: function () {
        },

        initView: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().TURNTABLEReq(msg)
            
        },

        onRefreshView: function () {

        },

        onRefreshAwardsData: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().REFRESHAWARDSDATAReq(msg)

        }

    })
    return mdt
})