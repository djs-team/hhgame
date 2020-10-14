
/**
 *  CashCowMdt Mediator
 *
 */
load('game/ui/layer/cashcow/CashCowMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let mdt = Mediator.extend({
        mediatorName: 'CashCowMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.UPDATE_CASHCOWNUM,
                GameEvent.GET_CASHCOWRECORD
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.UPDATE_CASHCOWNUM:
                    this.view.onRefreshView(body)
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

        }

    })
    return mdt
})