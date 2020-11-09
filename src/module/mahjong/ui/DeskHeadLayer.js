
/**
 * DeskHeadLayer
 */
load('module/mahjong/ui/DeskHeadLayer', function () {
    let GameResConfig = include('game/config/ResConfig')
    let PlayerPlay = GameResConfig.PlayerPlay
    let AniPlayer = GameResConfig.AniPlayer
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let DeskHeadLayerMdt = include('module/mahjong/ui/DeskHeadLayerMdt')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let DeskPersonlLayer = include('module/mahjong/ui/DeskPersonlLayer')
    let Layer = BaseLayer.extend({
        _className: 'DeskHeadLayer',
        RES_BINDING: function () {
            return {
                'bgPnl/headNd0': {  },
                'bgPnl/headNd1': {  },
                'bgPnl/headNd2': {  },
                'bgPnl/headNd3': {  },
            }
        },
        ctor: function () {
            this._super(ResConfig.View.DeskHeadLayer)
            this.registerMediator(new DeskHeadLayerMdt(this))
        },

        initData: function (pData) {
            this._uiSeatArray = pData.uiSeatArray

            this._playerNum = pData.pPlayerNum
            this._uiSeatConfig = TableConfig.UiSeatArray['4']
            this._aniNd = {}
            this._ani = {}

        },

        initView: function (pData) {
            this.initData(pData)
            for (let i = 0; i < 4; ++i) {
                this._aniNd[i] = this['headNd' + i].getChildByName('AniNd')
                this._aniNd[i].removeAllChildren()
                delete this._ani[i]
                this['headNd' + i].setVisible(false)
            }

            for (let i = 0; i < this._uiSeatArray.length; ++i) {
                this.updatePlayer(this._uiSeatArray[i])
            }
        },

        updateView: function (tData) {

        },

        hidePlayer: function () {
            for (let i = 0; i < 4; ++i) {
                this['headNd' + i].setVisible(false)
            }
        },

        updatePlayer: function (uiSeat) {
            let player = appInstance.dataManager().getPlayData().getPlayerByUiseat(uiSeat)
            let playerNd = this['headNd' + uiSeat]
            if (!player) {
                playerNd.setVisible(false)
                return
            }
            playerNd.setVisible(true)

            playerNd._sendMsg = {
                pid: player.pid,
                pSeatID: player.pSeatID,
            }

            let nameBg = playerNd.getChildByName('namePnl').getChildByName('nameBg')
            let nameTxt = nameBg.getChildByName('nameTxt')
            nameTxt.setString(global.cropStr(player.nickName, 5, '...'))

            let ani = appInstance.gameAgent().gameUtil().getAni(AniPlayer[player.pRole])
            ani.setScale(0.6)
            this._aniNd[uiSeat].addChild(ani)
            this._ani[uiSeat] = ani
            ani.setAnimation(0, PlayerPlay.stand, true)

            playerNd.getChildByName('namePnl').addClickEventListener(function (sender,et) {
                this.onShowPersonalData(sender)
            }.bind(this))
        },

        onShowPersonalData: function (sender) {
            let data = sender.getParent()._sendMsg
            let mySeatId = appInstance.dataManager().getPlayData().pMySeatID
            let pSeatId = data.pSeatID
            if(mySeatId === pSeatId)
                return

            this.showDeskPersonlLayer(data)




        },

        showDeskPersonlLayer: function (msg) {

                this.DeskPersonlLayer = appInstance.uiManager().createUI(DeskPersonlLayer,msg)
                this.DeskPersonlLayer.setLocalZOrder(1000)
                this.parent.addChild(this.DeskPersonlLayer)
        },

        clearView: function () {
            this.hidePlayer()
        },

        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        }
    })
    return Layer
})
