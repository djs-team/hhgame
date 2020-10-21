
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

            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case TableEvent.UpdateView:
                    this.UpdateView()
                    break
                default:
                    break
            }
        },

        UpdateView: function () {

        },

        initView: function () {
            let tData = appInstance.dataManager().getPlayData().getPlayer(this.pSeatID)
            cc.log('--------DeskPersonlLayerMdt--------tData : ' + JSON.stringify(tData))
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

            cc.log('--------DeskPersonlLayerMdt--------data : ' + JSON.stringify(data))
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