
/**
 *  CashCowMdt Mediator
 *
 */
load('game/ui/layer/cashcow/CashCowMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')

    let mdt = Mediator.extend({
        mediatorName: 'CashCowMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.GET_CASHCOWNUM,
                GameEvent.UPDATE_CASHCOWNUM,
                GameEvent.GET_CASHCOWRECORD
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.GET_CASHCOWNUM:
                    this.view.onRefreshView()
                    break
                case GameEvent.UPDATE_CASHCOWNUM:
                    this.onRefreshView(body)
                    break
                case GameEvent.GET_CASHCOWRECORD:
                    this.view.onShowRecordPnlClick(body)
                    break
            }
        },


        onRegister: function () {
            this.initView()
        },

        onRemove: function () {
        },

        initView: function () {
            let pRole = appInstance.dataManager().getUserData().pRole
            this.view.initView(pRole)
            appInstance.gameAgent().httpGame().cashCowNumReq()
        },

        onRefreshView: function (body) {

            if(body.status == 0){
                let data = {}
                body.propType = 1
                body.propNum = body.coin

                GameUtil.getPropData(body,data,GameUtil.CURRENCYTYPE_1,GameUtil.UNITLOCATION_BEFORE,'x')
                appInstance.gameAgent().addReceivePropsUI(data)
            }

            this.view.onRefreshView()
        }


    })
    return mdt
})