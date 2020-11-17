
/**
 * DeskHeadLayer
 */
load('module/mahjong/ui/DeskTopLayer', function () {
    let HallResConfig = include('game/config/ResConfig')
    let GameUtil = include('game/public/GameUtil')
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let DeskTopLayerMdt = include('module/mahjong/ui/DeskTopLayerMdt')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let LocalSave = include('game/public/LocalSave')
    let Sound = ResConfig.Sound
    let SELECT = TableConfig.Select
    let Layer = BaseLayer.extend({
        _className: 'DeskTopLayer',
        _lineExpressionNum: 5,
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
        _tableLevel: {
            'M1': '新手场 底分500',
            'M2': '初级场 底分2000',
            'M3': '高级场 底分5000',
            'M4': '大师场 底分20000',
        },
        RES_BINDING: function () {
            return {
                'pnl/ActionNd': {  },
                'pnl/ActionNd/ActionCell': { onClicked: this.onActionCellClick },
                'pnl/ChiNd': {  },
                'pnl/ChiNd/ChiCell': { onClicked: this.onChiCellClick  },

                'HostingPnl': { onClicked: this.onHostingClick },

                'BmPnl/HeadNd': {},
                'BmPnl/HeadNd/NameBg': {},
                'BmPnl/HeadNd/NameBg/NameTxt': {},
                'BmPnl/sayBtn': { onClicked: this.onSayViewClick },
                'BmPnl/sayContentPnl': { onClicked: this.onCloseSayContentClicked },
                'BmPnl/sayContentPnl/expressionBtn': { onClicked: this.onSayViewClick },
                'BmPnl/sayContentPnl/languageBtn': { onClicked: this.onSayViewClick },
                'BmPnl/sayContentPnl/contentList': {},
                'BmPnl/sayContentPnl/expressionTransCell': {},
                'BmPnl/sayContentPnl/expressionCell': {},
                'BmPnl/sayContentPnl/sayCell': {},
                'BmPnl/sayPnl': {},
                'BmPnl/expressionPnl': {},
                'BmPnl/magicPnl': {},
                'TopPnl/SetBtn': { onClicked: this.onSetBtnClick },
                'TopPnl/BaoNd': {},
                'TopPnl/BaoNd/BaoCard': {},
                'TopPnl/BaoNd/NoBao': {},
                'TopPnl/BaoNd/RemainingCard/RemainingCardsTxt': {},
                'TopPnl/TableLevelNd': {},
                'TopPnl/TableLevelNd/TableLevelTxt': {},
            }
        },
        ctor: function () {
            this._super(ResConfig.View.DeskTopLayer)
            this.registerMediator(new DeskTopLayerMdt(this))
        },

        onSetBtnClick: function () {
            appInstance.gameAgent().addPopUI(HallResConfig.Ui.SetLayer, {isDesk: true})
        },

        onCloseChuang: function () {
            this.sayContentPnl.setVisible(false)
        },

        onHostingClick: function () {
            let msg = {}
            msg.pHosting = 0
            appInstance.gameAgent().tcpGame().TableHostingProto(msg)
        },

        UpdateTableHosting: function (pHosting) {
            if (pHosting === 2) {
                this.HostingPnl.setVisible(false)
            } else if (pHosting === 1) {
                this.HostingPnl.setVisible(true)
            } else {
                // 不应该，也不会走进这里面来
                this.HostingPnl.setVisible(false)
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

        updateRemainingCard: function(num) {
            this.BaoNd.setVisible(true)
            num = num || 0
            this.RemainingCardsTxt.setString(num)
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
            cc.log('=====================topLayer=========' + JSON.stringify(pData))
            this._selfInfo = pData.getSelfInfo()
            this._tData = pData.tableData
            this._actionCell = {}
            this._chiCell = []
        },

        initView: function (pData) {
            this.contentList.setScrollBarEnabled(false)
            this.initData(pData)
            this.ActionCell.setVisible(false)
            this.ChiCell.setVisible(false)

            this.HostingPnl.setVisible(false)

            this.sayBtn.setVisible(true)
            this.sayContentPnl.setVisible(false)
            this.sayPnl.setVisible(false)
            this.sayPnl.getChildByName('sayPnl0').setVisible(false)
            this.sayPnl.getChildByName('sayPnl1').setVisible(false)
            this.sayPnl.getChildByName('sayPnl2').setVisible(false)
            this.sayPnl.getChildByName('sayPnl3').setVisible(false)
            this.expressionPnl.setVisible(false)
            this.expressionPnl.getChildByName('expressionImg0').setVisible(false)
            this.expressionPnl.getChildByName('expressionImg1').setVisible(false)
            this.expressionPnl.getChildByName('expressionImg2').setVisible(false)
            this.expressionPnl.getChildByName('expressionImg3').setVisible(false)
            this.magicPnl.setVisible(false)
            this.magicPnl.getChildByName('magicNode0').setVisible(false)
            this.magicPnl.getChildByName('magicNode1').setVisible(false)
            this.magicPnl.getChildByName('magicNode2').setVisible(false)
            this.magicPnl.getChildByName('magicNode3').setVisible(false)

            this.HeadNd.setVisible(false)

            this.NameTxt.setString(this._selfInfo.nickName)
            this.TableLevelTxt.setString(this._tableLevel[this._tData.pGameType])

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

            // this.onDealPower()
        },

        // onDealPower: function () {
        //     //获取当前连接网络  android=>0--没网 1--wifi 2--4G  ios=>WIFI   WWAN   NONE
        //     let state = appInstance.nativeApi().getNetWorkStates()
        //     //获取网络信号  0-4
        //     let dbm = appInstance.nativeApi().getSignalStrength()
        //     if (cc.sys.OS_ANDROID === cc.sys.os) {
        //         switch (state) {
        //             case 0:
        //                 console.log('====================android没网')
        //                 break
        //             case 1:
        //                 console.log('====================androidWIFI')
        //                 switch (dbm) {
        //                     case 0:
        //                         console.log('===================android0格')
        //                         break
        //                     case 1:
        //                         console.log('===================android1格')
        //                         break
        //                     case 2:
        //                         console.log('===================android2格')
        //                         break
        //                     case 3:
        //                         console.log('===================android3格')
        //                         break
        //                     case 4:
        //                         console.log('===================android4格')
        //                         break
        //                 }
        //                 break
        //             case 2:
        //                 console.log('====================android4G')
        //                 switch (dbm) {
        //                     case 0:
        //                         console.log('===================android0格')
        //                         break
        //                     case 1:
        //                         console.log('===================android1格')
        //                         break
        //                     case 2:
        //                         console.log('===================android2格')
        //                         break
        //                     case 3:
        //                         console.log('===================android3格')
        //                         break
        //                     case 4:
        //                         console.log('===================android4格')
        //                         break
        //                 }
        //                 break
        //         }
        //     } else if (cc.sys.OS_IOS === cc.sys.os) {
        //         switch (state) {
        //             case 'NONE':
        //                 console.log('====================ios没网')
        //                 break
        //             case 'WIFI':
        //                 console.log('====================iosWIFI')
        //                 switch (dbm) {
        //                     case 0:
        //                         console.log('===================ios0格')
        //                         break
        //                     case 1:
        //                         console.log('===================ios1格')
        //                         break
        //                     case 2:
        //                         console.log('===================ios2格')
        //                         break
        //                     case 3:
        //                         console.log('===================ios3格')
        //                         break
        //                     case 4:
        //                         console.log('===================ios4格')
        //                         break
        //                 }
        //                 break
        //             case 'WWAN':
        //                 console.log('====================ios4G')
        //                 switch (dbm) {
        //                     case 0:
        //                         console.log('===================ios0格')
        //                         break
        //                     case 1:
        //                         console.log('===================ios1格')
        //                         break
        //                     case 2:
        //                         console.log('===================ios2格')
        //                         break
        //                     case 3:
        //                         console.log('===================ios3格')
        //                         break
        //                     case 4:
        //                         console.log('===================ios4格')
        //                         break
        //                 }
        //                 break
        //         }
        //     }
        //
        //     //获取电量 0-100
        //     let batteryLevel = appInstance.nativeApi().getBatteryLevel()
        //     console.log('======================电量'+batteryLevel);
        // },

        onCloseSayContentClicked: function () {
            this.sayContentPnl.setVisible(false)
        },

        onSayViewClick: function (sender) {
            this.sayContentPnl.setVisible(true)
            this.contentList.setVisible(true)
            this.sayCell.setVisible(false)
            this.expressionCell.setVisible(false)
            this.expressionTransCell.setVisible(false)
            this.contentList.removeAllChildren()
            if (sender == this.languageBtn) {
                this.languageBtn.setTouchEnabled(false)
                this.languageBtn.setBright(false)
                this.expressionBtn.setTouchEnabled(true)
                this.expressionBtn.setBright(true)
                let fangyan = global.localStorage.getStringForKey(LocalSave.LocalLanguage)
                let fangyanSay = '';
                if (fangyan == 'dongbei') {
                    fangyanSay = TableConfig.experssion['say']['dongSay']
                } else {
                    fangyanSay = TableConfig.experssion['say']['puSay']
                }
                let fangyanSayLength = Object.keys(fangyanSay).length;
                for (let i=0; i<fangyanSayLength; i++) {
                    this.onFangYanSayView(fangyanSay[i])
                }
            } else {
                this.languageBtn.setTouchEnabled(true)
                this.languageBtn.setBright(true)
                this.expressionBtn.setTouchEnabled(false)
                this.expressionBtn.setBright(false)
                let expression = TableConfig.experssion['express']
                this.onExpressionSayView(expression);
            }
        },

        onExpressionSayView: function (data) {
            let dataLength = Object.keys(data).length;
            let ceil = Math.ceil(dataLength/this._lineExpressionNum)
            let quYu = dataLength%this._lineExpressionNum
            for (let i = 0; i < ceil; i++) {
                let listPnl = this.expressionTransCell.clone()
                listPnl.setVisible(true)
                this.contentList.pushBackCustomItem(listPnl)
                let lineExpressionLength = 0;
                if (i == ceil-1) {
                    lineExpressionLength = quYu
                } else {
                    lineExpressionLength = this._lineExpressionNum
                }
                for (let j = 0; j < lineExpressionLength; j++) {
                    this.onExpressionDetailView(data, this._lineExpressionNum*i+j, listPnl, j);
                }
            }
        },

        onExpressionDetailView: function (data, index, listPnl, i) {
            let cellData = data[index]
            let cell = this.expressionCell.clone()
            listPnl.addChild(cell)
            cell.setPositionY(0)
            cell.setPositionX(18 + 48*i)
            cell.setVisible(true)
            cell._sendMsg = {
                num: cellData.id,
                type: TableConfig.ExpressionType[1],
            }
            cell.setName('num'+cellData.id)
            cell.getChildByName('expressionImg').loadTexture(cellData.res)
            cell.addClickEventListener(function (sender,dt) {
                GameUtil.delayBtn(sender)
                this.gameSendNews(sender)
            }.bind(this))
        },

        onFangYanSayView: function (data) {
            let cell = this.sayCell.clone()
            cell.setVisible(true)
            this.contentList.pushBackCustomItem(cell)
            cell._sendMsg = {
                num: data.id,
                type: TableConfig.ExpressionType[0],
            }
            cell.setName('num'+data.id)
            cell.getChildByName('sayText').setString(data.text)
            cell.addClickEventListener(function (sender,dt) {
                GameUtil.delayBtn(sender)
                this.gameSendNews(sender)
            }.bind(this))
        },

        gameSendNews: function (sender) {
            let data = sender._sendMsg
            let pData = appInstance.dataManager().getPlayData().tableData.pTableID
            let msg = {
                'type': data.type,
                'num': data.num,
                'toSeatID': 0,
                'tableId':pData
            }
            appInstance.gameAgent().tcpGame().ToSendNewsProto(msg)
        },

        toExpressionView: function (data) {
            let fangyan = global.localStorage.getStringForKey(LocalSave.LocalLanguage)
            let type = data.type
            let num = data.num
            let fromSeatID = data.fromSeatID
            let uiSeatID = appInstance.dataManager().getPlayData().seatId2UI(fromSeatID)
            if (type == TableConfig.ExpressionType[0]) {
                //播放语音
                appInstance.gameAgent().mjUtil().playKuaiJieYuSound(Sound.kuaijieyu[num])
                this.updateSayShow(uiSeatID);
                //展示语言
                if (fangyan == 'dongbei') {
                    this.sayPnl.getChildByName('sayPnl'+uiSeatID).getChildByName('sayText').setString(TableConfig.experssion['say']['dongSay'][num-1]['text']);
                } else {
                    this.sayPnl.getChildByName('sayPnl'+uiSeatID).getChildByName('sayText').setString(TableConfig.experssion['say']['puSay'][num-1]['text']);
                }
                this.runAction(cc.sequence(cc.DelayTime(2), cc.CallFunc(function() {
                    this.updateSayHide(uiSeatID);
                }.bind(this))))
            } else if (type == TableConfig.ExpressionType[1]) {
                this.updateExpressionShow(uiSeatID);
                this.expressionPnl.getChildByName('expressionImg'+uiSeatID).loadTexture(TableConfig.experssion['express'][num-1]['res'])
                this.runAction(cc.sequence(cc.DelayTime(2), cc.CallFunc(function() {
                    this.updateExpressionHide(uiSeatID);
                }.bind(this))))
            } else if (type == TableConfig.ExpressionType[2]) {
                let toSeatID = data.toSeatID
                let toUiSeatID = appInstance.dataManager().getPlayData().seatId2UI(toSeatID)
                this.updateMagicShow(toUiSeatID)
                let magicAnimation = appInstance.gameAgent().gameUtil().getAni(ResConfig.Magic[num-1])
                magicAnimation.setAnimation(0, ResConfig.MagicAnimation[num-1], true)
                this.magicPnl.getChildByName('magicNode'+toUiSeatID).removeAllChildren()
                this.magicPnl.getChildByName('magicNode'+toUiSeatID).addChild(magicAnimation)
                this.runAction(cc.sequence(cc.DelayTime(2), cc.CallFunc(function() {
                    this.updateMagicHide(toUiSeatID);
                }.bind(this))))
            }
            this.sayPnl.getChildByName('sayPnl'+uiSeatID)
        },

        updateSayShow: function (uiSeatID) {
            this.sayPnl.setVisible(true)
            this.sayPnl.getChildByName('sayPnl'+uiSeatID).setVisible(true)
        },

        updateSayHide: function (uiSeatID) {
            this.sayPnl.setVisible(false)
            this.sayPnl.getChildByName('sayPnl'+uiSeatID).setVisible(false)
        },

        updateExpressionShow: function (uiSeatID) {
            this.expressionPnl.setVisible(true)
            this.expressionPnl.getChildByName('expressionImg'+uiSeatID).setVisible(true)
        },

        updateExpressionHide: function (uiSeatID) {
            this.expressionPnl.setVisible(false)
            this.expressionPnl.getChildByName('expressionImg'+uiSeatID).setVisible(false)
        },

        updateMagicShow: function (uiSeatID) {
            this.magicPnl.setVisible(true)
            this.magicPnl.getChildByName('magicNode'+uiSeatID).setVisible(true)
        },

        updateMagicHide: function (uiSeatID) {
            this.magicPnl.setVisible(false)
            this.magicPnl.getChildByName('magicNode'+uiSeatID).setVisible(false)
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
            this.hideSelectChi()
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
