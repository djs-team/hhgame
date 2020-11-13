
/**
 *  Turntable Mediator
 *
 */
load('game/ui/layer/set/SetLayerMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event
    let mdt = Mediator.extend({
        mediatorName: 'SetLayerMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                TableEvent.GameResultProto,
                TableEvent.MatchResultProto,
            ]
        },
        handleNotification: function (notification) {

            let name = notification.getName()
            switch (name) {
                case TableEvent.GameResultProto:
                case TableEvent.MatchResultProto:
                    this.view.onCloseLayer()
                    break
            }
        },


        onRegister: function () {

            this.initView()
        },

        onRemove: function () {
        },

        initView: function () {


        }

    })
    return mdt
})