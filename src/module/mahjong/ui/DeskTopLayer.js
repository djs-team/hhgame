
/**
 * DeskHeadLayer
 */
load('module/mahjong/ui/DeskTopLayer', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let DeskTopLayerMdt = include('module/mahjong/ui/DeskTopLayerMdt')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let SELECT = TableConfig.Select
    let Layer = BaseLayer.extend({
        _className: 'DeskTopLayer',
        _maxChi: 6,
        _chiPos:[
            [0],
            [1],
            [
                cc.p(-110,0),
                cc.p(110,0)
            ],
            [
                cc.p(-220,0),
                cc.p(0,0),
                cc.p(220,0)
            ],
            [
                cc.p(-330,0),
                cc.p(-110,0),
                cc.p(110,0),
                cc.p(330,0)
            ],
        ],
        RES_BINDING: function () {
            return {
                'pnl/ActionNd': {  },
                'pnl/ActionNd/ActionCell': { onClicked: this.onActionCellClick },
                'pnl/ChiNd': {  },
                'pnl/ChiNd/ChiCell': { onClicked: this.onChiCellClick  },
                'HostingPnl': { onClicked: this.onHostingClick }
            }
        },
        ctor: function () {
            this._super(ResConfig.View.DeskTopLayer)
            this.registerMediator(new DeskTopLayerMdt(this))
        },

        onHostingClick: function () {
            let msg = {}
            msg.pHosting = 0
            appInstance.gameAgent().tcpGame().TableHostingProto(msg)
        },

        TableHostingProto: function (pHosting) {
            if (pHosting === 2) {
                this.HostingPnl.setVisible(false)
            } else if (pHosting === 1) {
                this.HostingPnl.setVisible(true)
            }
        },

        onChiCellClick: function (sender) {
            let chiInfo = sender._chiInfo
            let sendMsg = chiInfo.sendMsg
            let lastPutCard = chiInfo.lastPutCard
            let pCards = chiInfo.pCards

            appInstance.gameAgent().mjUtil().removeCard(pCards, lastPutCard)
            sendMsg.pCards = pCards
            delete sendMsg.lastPutCard

            this.hideSelectChi()
            appInstance.gameAgent().tcpGame().PlayerSelectProto(sendMsg)
        },

        onActionCellClick: function(sender) {
            let pActionInfo = sender.pActionInfo
            let pAction = pActionInfo.pAction
            let lastPutCard = pActionInfo.lastPutCard
            if (pAction.pActionID === 120 || pAction.pActionID === 50) {//吃 吃听
                let sendMsg = {}
                sendMsg.nSeatID = appInstance.dataManager().getPlayData().pMySeatID
                sendMsg.pActionID = pAction.pActionID
                sendMsg.pCards = pAction.pCards || []
                sendMsg.pNoCards = pAction.pNoCards || []
                if ( pAction.pCards.length === 2) {
                    appInstance.gameAgent().tcpGame().PlayerSelectProto(sendMsg)
                } else {
                    sendMsg.lastPutCard = lastPutCard
                    this.updateSelectChi(sendMsg)
                }
            } else {
                let sendMsg = {}
                sendMsg.nSeatID = appInstance.dataManager().getPlayData().pMySeatID
                sendMsg.pActionID = pAction.pActionID
                sendMsg.pCards = pAction.pCards || []
                sendMsg.pNoCards = pAction.pNoCards || []
                appInstance.gameAgent().tcpGame().PlayerSelectProto(sendMsg)
            }
            this.hideAction()
        },

        initData: function (pData) {
            this._actionCell = {}
            this._chiCell = []
        },

        initView: function (pData) {
            this.initData(pData)
            this.ActionCell.setVisible(false)
            this.ChiCell.setVisible(false)

            this.HostingPnl.setVisible(false)


            for (let i = 0; i < this._maxChi; ++i) {
                let chiCell = this.ChiCell.clone()
                this.ChiNd.addChild(chiCell)
                this._chiCell.push(chiCell)
            }

            for (let key in SELECT) {
                let ActionInfo = SELECT[key]
                if (ActionInfo.isBtn) {
                    let btn = this.ActionCell.clone()
                    btn.getChildByName('ActionImg').loadTexture(ActionInfo.img)
                    this._actionCell[key] = btn
                    btn._ActionInfo = ActionInfo
                    this.ActionNd.addChild(btn)
                }
            }
        },

        hideSelectChi: function () {
            for (let i = 0; i < this._maxChi; ++i) {
                this._chiCell[i].setVisible(false)
                this._chiCell[i]._chiInfo = null
            }
        },

        updateSelectChi: function (sendMsg) {
            let pCards = sendMsg.pCards
            let lastPutCard = sendMsg.lastPutCard
            let cellCard = []
            for (let i = 0; i < pCards.length; i+=2) {
                let tmpCard = pCards.slice(i, i + 2)
                tmpCard.push(lastPutCard)
                appInstance.gameAgent().mjUtil().sortCard(tmpCard)
                cellCard.push(tmpCard)
            }

            for (let i = 0; i < this._maxChi; ++i) {
                if (i < cellCard.length) {
                    this._chiCell[i].setVisible(true)
                    this._chiCell[i].setPosition(this._chiPos[cellCard.length][i])
                    this._chiCell[i]._chiInfo = {
                        pCards: cellCard[i],
                        lastPutCard: lastPutCard,
                        sendMsg: sendMsg
                    }
                    this.updateSelectChiCell(this._chiCell[i], cellCard[i])
                } else {
                    this._chiCell[i].setVisible(false)
                }
            }
        },

        updateSelectChiCell: function (chiCell, cards) {
            for (let i = 0; i < 3; ++i) {
                let card = chiCell.getChildByName('Card' + i)
                let cardValue = card.getChildByName('CardValue')
                let valueImgPath = appInstance.gameAgent().mjUtil().getCardValueImg(0, 'selfhand', cards[i])
                cardValue.loadTexture(valueImgPath)
            }
        },

        updateAction: function (actionInfo) {
            this.hideAction()
            if (!actionInfo) {
                return
            }
            let actions = actionInfo.pActions
            if (!actions) {
                return
            }
            if (!actions.length) {
                return
            }
            let pass = {
                "pActionID":130
            }
            actions.unshift(pass)
            for (let i = 0; i < actions.length; ++i) {
                let action = this._actionCell[actions[i].pActionID]
                action.pActionInfo = {
                    lastPutCard: actionInfo.lastPutCard,
                    pAction: actions[i]
                }
                action.setVisible(true)
                action.setPositionX(-200 * i)
            }
        },

        hideAction: function () {
            for (let key in this._actionCell) {
                this._actionCell[key].setVisible(false)
            }
        },

        clearView: function () {
            this.hideAction()
        },

        updateView: function (tData) {

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
