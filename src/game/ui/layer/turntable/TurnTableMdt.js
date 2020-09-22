
/**
 *  Turntable Mediator
 *
 */
load('game/ui/layer/turntable/TurnTableMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
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
                    cc.log('=========TurnTableMdt==========deal something===========' + JSON.stringify(body))
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
                config.name = body.configList[i].describe
                let propType = body.configList[i].propType
                let propCode = body.configList[i].propCode
                let propNum = body.configList[i].propNum
                switch (propType) {
                    case 1://货币
                        if(propCode == 1){
                            if(propNum <= 3999){
                                config.res = 'res/code/goods/zphd_10.png'
                            }else if(propNum >= 4000 && propNum <= 6999){
                                config.res = 'res/code/goods/scjb_16.png'
                            }else{
                                config.res = 'res/code/goods/scjb_17.png'
                            }
                        }else if(propCode == 2){
                            if(propNum <= 19){
                                config.res = 'res/code/goods/zphd_6.png'
                            }else if(propNum >= 20 && propNum <= 29){
                                config.res = 'res/code/goods/zphd_7.png'
                            }else{
                                config.res = 'res/code/goods/scjb_8.png'
                            }
                        }else{
                            config.res = 'res/code/goods/gy_fk.png'
                        }
                        break
                    case 2://角色
                        if(propCode == 1){//货币
                            config.res = 'res/code/goods/zphd_1.png'
                        }else if(propCode == 2){
                            config.res = 'res/code/goods/zphd_2.png'
                        }else{
                            config.res = 'res/code/goods/zphd_2.png'
                        }
                        break
                    case 3://实物
                        if(propCode == 1){
                            config.res = 'res/code/goods/zphd_12.png'
                        }else if(propCode == 2){
                            config.res = 'res/code/goods/zphd_13.png'
                        }else{
                            config.res = 'res/code/goods/zphd_14.png'
                        }
                        break
                    default:
                        break

                }
                configList[i] = config
            }

            tableData.configList = configList
            tableData.luckyPrize = body.luckyPrize

            this.view.onDataInit(tableData)
        },

        onTurnPointResult: function (body) {

            let _resultData = {}

            _resultData.multiple = body.multiple
            _resultData.propNum = body.propNum

            let propType = body.propType
            let propCode = body.propCode
            switch (propType) {
                case 1://货币
                    if(propCode == 1){
                        _resultData.res = 'res/code/goods/zphd_10.png'
                    }else if(propCode == 2){
                        _resultData.res = 'res/code/goods/zphd_6.png'
                    }else{
                        _resultData.res = 'res/code/goods/gy_fk.png'
                    }
                    break
                case 2://角色
                    if(propCode == 1){//货币
                        _resultData.res = 'res/code/goods/zphd_1.png'
                    }else if(propCode == 2){
                        _resultData.res = 'res/code/goods/zphd_2.png'
                    }else{
                        _resultData.res = 'res/code/goods/zphd_2.png'
                    }
                    break
                case 3://实物
                    if(propCode == 1){
                        _resultData.res = 'res/code/goods/zphd_12.png'
                    }else if(propCode == 2){
                        _resultData.res = 'res/code/goods/zphd_13.png'
                    }else{
                        _resultData.res = 'res/code/goods/zphd_14.png'
                    }
                    break
                default:
                    break

            }


            this.view.onTurnPointResult(_resultData)
        },

        onReceiveAwardsResult: function (body) {

            let _resultData = {}

            _resultData.code = body.code
            _resultData.propNum = body.propNum

            let propType = body.propType
            let propCode = body.propCode
            switch (propType) {
                case 1://货币
                    if(propCode == 1){
                        _resultData.res = 'res/code/goods/zphd_10.png'
                    }else if(propCode == 2){
                        _resultData.res = 'res/code/goods/zphd_6.png'
                    }else{
                        _resultData.res = 'res/code/goods/gy_fk.png'
                    }
                    break
                case 2://角色
                    if(propCode == 1){//货币
                        _resultData.res = 'res/code/goods/zphd_1.png'
                    }else if(propCode == 2){
                        _resultData.res = 'res/code/goods/zphd_2.png'
                    }else{
                        _resultData.res = 'res/code/goods/zphd_2.png'
                    }
                    break
                case 3://实物
                    if(propCode == 1){
                        _resultData.res = 'res/code/goods/zphd_12.png'
                    }else if(propCode == 2){
                        _resultData.res = 'res/code/goods/zphd_13.png'
                    }else{
                        _resultData.res = 'res/code/goods/zphd_14.png'
                    }
                    break
                default:
                    break

            }


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