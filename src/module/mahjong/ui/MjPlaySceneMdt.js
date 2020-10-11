
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
                TableEvent.InitCardProto,
                TableEvent.MatchJinjiGaming,
                TableEvent.MatchResultProto,
                TableEvent.MatchResultBigProto,
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
                    this.GameResultProto(body)
                    break
                case TableEvent.TableChangeProto:
                    this.TableChangeProto()
                    break
                case TableEvent.InitCardProto:
                    this.view.clearGameResult()
                    break
                case TableEvent.MatchJinjiGaming:
                    this.view.showMatchJinjiLayer(body)
                    break
                case TableEvent.MatchResultProto:
                    this.view.showMatchResultLayer(body)
                    break
                case TableEvent.MatchResultBigProto:
                    this.view.showMatchBigResultLayer(body)
                    break
            }
        },

        TableChangeProto: function () {
            this.view.clearGameResult()
        },


        GameResultProto: function (msg) {
            this.view.showGameResultLayer(msg)
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