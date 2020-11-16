
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
                TableEvent.GamingProto,
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
                case TableEvent.GamingProto:
                    this.view.updatePersonalCoin(body);
                    break
                default:
                    break
            }
        },



        initView: function () {
            let pMinValue = appInstance.dataManager().getPlayData().tableData.pMinValue
            let tData = appInstance.dataManager().getPlayData().getPlayer(this.pSeatID)
            let isCanSend = false
            let userDataCoin = appInstance.dataManager().getUserData().coin
            if (userDataCoin>(pMinValue+100)) {
                isCanSend = true
            }
            let data = {
                pid: tData.pid,
                nickName: tData.nickName,
                pCoins: tData.coins,
                pRole: tData.pRole,
                pPhoto: tData.pPhoto,
                winCnt: tData.win,
                allCnt: tData.win + tData.lose,
                isCanSend: isCanSend,
                pSeatID: tData.pSeatID
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