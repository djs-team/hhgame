
/**
 *  DeskHeadLayerMdt Mediator
 *
 */
load('module/mahjong/ui/DeskResultLayerMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event

    let DeskMdt = Mediator.extend({
        mediatorName: 'DeskResultLayerMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                TableEvent.InitCardProto,
                GameEvent.TABLE_RESULT_RECEIVE,
                GameEvent.UPDATE_PROPSYNC,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case TableEvent.InitCardProto:
                    break
                case GameEvent.TABLE_RESULT_RECEIVE:
                    this.view.updateNextGame(body)
                    break
                case GameEvent.UPDATE_PROPSYNC:
                    this.view.changeCoin();
                    break
            }
        },

        UpdateView: function () {

        },

        initView: function () {
            let pData = appInstance.dataManager().getPlayData()
            this.view.initView(pData)
        },

        onRegister: function () {
            this.initView()

        },

        onRemove: function () {
        }

    })

    return DeskMdt
})