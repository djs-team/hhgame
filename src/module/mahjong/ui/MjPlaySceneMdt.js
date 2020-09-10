
/**
 *  MjPlaySceneMdt Mediator
 *
 */
load('module/mahjong/ui/MjPlaySceneMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event

    let MDT = Mediator.extend({
        mediatorName: 'MjPlaySceneMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                TableEvent.clearTableView,
                TableEvent.GameResultProto,
                TableEvent.TableChangeProto,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case TableEvent.clearTableView:
                    this.view.clearTableView()
                    break
                case TableEvent.GameResultProto:
                    this.GameResultProto()
                    break
                case TableEvent.TableChangeProto:
                    this.TableChangeProto()
                    break
            }
        },

        TableChangeProto: function () {
            this.view.TableChangeProto()
        },


        GameResultProto: function () {
            this.view.clearTableView()
            this.view.showGameResultLayer()
        },

        initView: function () {

        },

        onRegister: function () {
            this.initView()
        },

        onRemove: function () {
        }

    })

    return MDT
})