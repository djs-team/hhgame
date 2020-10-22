
/**
 *  DeskPersonlLayerMdt Mediator
 *
 */
load('module/mahjong/ui/DeskPersonlLayerMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event

    let DeskMdt = Mediator.extend({
        mediatorName: 'DeskPersonlLayerMdt',
        ctor: function (view,msg) {
            this._super(this.mediatorName,view)
            this.pid = msg.pid
            this.pSeatID = msg.pSeatID
        },
        getNotificationList: function () {
            return [
                TableEvent.GameResultProto,
                TableEvent.MatchResultProto,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case TableEvent.GameResultProto:
                case TableEvent.MatchResultProto:
                    this.view.onCloseBtnClick()
                    break
                default:
                    break
            }
        },



        initView: function () {
            let tData = appInstance.dataManager().getPlayData().getPlayer(this.pSeatID)
            let data = {
                pid: tData.pid,
                nickName: tData.nickName,
                pCoins: tData.coins,
                pRole: tData.pRole,
                pPhoto: tData.pPhoto,
                winCnt: tData.win,
                allCnt: tData.win + tData.lose,
                isCanSend: true
            }

            this.view.initView(data)

        },

        onRegister: function () {

            this.initView()

        },

        onRemove: function () {
        }

    })

    return DeskMdt
})