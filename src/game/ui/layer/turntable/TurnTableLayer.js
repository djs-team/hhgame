load('game/ui/layer/turntable/TurnTableLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let TurnTableMdt = include('game/ui/layer/turntable/TurnTableMdt')
    let tabState = 0;//0-转盘点击需要看视频 1可以领取三倍奖励
    let turnTableLayer = BaseLayer.extend({
        _className: 'turnTableLayer',
        _requestDeley: 5,
        _requestCache: 0,
        _isHaveInitUserData: false,
        _lightTime: 0,
        _lightInterval: 0.5,
        _canTurnTableStatus: 0,//0代表可以免费1代表不可以免费可看视频2不可以转盘
        ctor: function () {
            this._super(ResConfig.View.TurnTableLayer)

            this.registerMediator(new TurnTableMdt(this))
            this.registerEventListener('rewardVideoCallback', this.onRewardVideoCallback)

        },
        RES_BINDING: function () {
            return {

                'topPnl/returnBtn': {onClicked: this.onCloseClick},
                'topPnl/explainBtn': {},
                'topPnl/recordBtn': {onClicked: this.onRecordClick},

                'bmPnl/awardsPnl/awardsUserDataNd': {},
                'bmPnl/awardsPnl/awardsUserDataNd/awardsUserDataPnl': {},
                'bmPnl/awardsPnl/userDataCell': {},
                'bmPnl/zhuanPnl': {},
                'bmPnl/zhuanPnl/pointPnl': {onClicked: this.onTurnPointClick},
                'bmPnl/zhuanPnl/pointPnl/TurnPointImg': {},
                'bmPnl/zhuanPnl/pointPnl/turnPointStartImg': {},
                'bmPnl/zhuanPnl/pointPnl/turnPointAssImg': {},
                'bmPnl/zhuanPnl/turnTablePic': {},
                'bmPnl/zhuanPnl/turnTablePic/goodsNd': {},
                'bmPnl/zhuanPnl/turnTablePic/pointNd0': {},
                'bmPnl/zhuanPnl/turnTablePic/pointNd1': {},
                'popUpPnl/timesEndPnl': {},
                'popUpPnl/explainPnl': {},
                'popUpPnl/recordsPnl': {},

                'popUpPnl/awardsPnl': {},
                'popUpPnl/awardsPnl/pgPnl': {},
                'popUpPnl/awardsPnl/propAwardsCloseBtn': {onClicked: this.onSingleClaimClick},
                'popUpPnl/awardsPnl/multipleClaimBtn': {onClicked: this.onMultipleClaimClick},
                'popUpPnl/awardsPnl/multSingleClaimBtn': {onClicked: this.onSingleClaimClick},
                'popUpPnl/awardsPnl/singleClaimBtn': {onClicked: this.onSingleClaimClick},
                'popUpPnl/explainPnl/explainDataPnl/closeBtn': {onClicked: this.onGoShopClick},

                'popUpPnl/recordsPnl/recordLogListView': {},
                'popUpPnl/recordsPnl/recordDataCell': {},
                'popUpPnl/recordsPnl/recordCloseBtn': {onClicked: this.onHideRecordPnlClick},

                'popUpPnl/acceptedPnl': {onClicked: this.onHideAcceptPnlClick},
                'popUpPnl/acceptedPnl/awardsVal': {},
                'popUpPnl/acceptedPnl/acceptedBg/acceptedTypePg': {},

            }
        },
        onGoShopClick: function () {

        },
        onEnter: function () {
            this._super()
            this.initData()
            this.initView()

        },
        onExit: function () {
            this._super()
        },

        initData: function () {
            this._lightBg = 0
            this._rewardUserData = []
        },

        onUpdate: function (dt) {
            if (this._isHaveInitUserData) {
                this._requestCache += dt
                if (this._requestCache > this._requestDeley) {
                    this._requestCache = 0
                    // appInstance.gameAgent().httpGame().REFRESHAWARDSDATAReq()
                }
            }

            this._lightTime += dt
            if (this._lightTime > this._lightInterval) {
                this._lightTime = 0
                this.playLight()
            }
        },


        playRewadInfo: function () {

            this.awardsUserDataPnl.stopAllActions()
            this.awardsUserDataPnl.setPosition(cc.p(0, -50))

            let moveto = cc.moveTo(5, cc.p(0, 30 * this._rewardUserData.length + 300))
            let callBack = function () {
                appInstance.gameAgent().httpGame().REFRESHAWARDSDATAReq()
                this.awardsUserDataPnl.setPosition(cc.p(0, -50))
            }.bind(this)

            this.awardsUserDataPnl.runAction(cc.sequence(moveto, cc.callFunc(callBack)))
        },

        initView: function () {

            this.timesEndPnl.setVisible(false)
            this.explainPnl.setVisible(false)
            this.recordsPnl.setVisible(false)
            this.userDataCell.setVisible(false)
            this.awardsPnl.setVisible(false)
            this.acceptedPnl.setVisible(false)
            this.recordDataCell.setVisible(false)
            this.pointNd1.setVisible(false)

            // this.pointerBtn.setRotation(36 * 3)

        },

        initZhuanView: function (zhuanData) {

            for (let i = 1; i < 11; ++i) {
                let cell = this._goodsArray['goods_' + i]
                cell.getChildByName('name').setString(zhuanData[i - 1].name)
                cell.loadTexture(zhuanData[i - 1].res)
            }
        },

        updateUserDataView: function (userData) {
            this.awardsUserDataPnl.removeAllChildren()
            this._rewardUserData = userData
            for (let i = 0; i < userData.length; ++i) {
                this.updateUserCell(userData[i], i)
            }

            this.playRewadInfo()
            this._isHaveInitUserData = true
        },

        updateUserCell: function (userData, index) {
            let userCell = this.userDataCell.clone()
            userCell.setVisible(true)
            this.awardsUserDataPnl.addChild(userCell)
            userCell.setPosition(cc.p(0, 0 - 30 * index))

            let nameNd = userCell.getChildByName('userName')
            let gooodsNd = userCell.getChildByName('goodsName')
            nameNd.setString(userData.pName)
            gooodsNd.setString(userData.luckPrizeStr)
        },


        onCloseClick: function () {
            // appInstance.gameAgent().tcpGame().enterTable()
            appInstance.uiManager().removeUI(this)
        },

        onDataInit: function (data) {


            this._canTurnTableStatus = data.code
            this._goodsArray = {}
            for (let i = 1; i < 11; ++i) {
                this._goodsArray['goods_' + i] = this.goodsNd.getChildByName('goods' + i)
            }

            this.onUpdateTurnPointImg()
            this.initZhuanView(data.configList)
            this.updateUserDataView(data.luckyPrize)
        },

        onUpdateTurnPointImg: function () {

            if (this._canTurnTableStatus == 2) {
                this.TurnPointImg.setVisible(false)
                this.turnPointStartImg.setVisible(false)
                this.turnPointAssImg.setVisible(true)
            } else {
                this.TurnPointImg.setVisible(true)
                this.turnPointStartImg.setVisible(true)
                this.turnPointAssImg.setVisible(false)
                if (this._canTurnTableStatus == 0) {
                    this.turnPointStartImg.getChildByName('turnFreeText').setVisible(true)
                    this.turnPointStartImg.getChildByName('videoImg').setVisible(false)
                } else {
                    this.turnPointStartImg.getChildByName('turnFreeText').setVisible(false)
                    this.turnPointStartImg.getChildByName('videoImg').setVisible(true)
                }

            }

        },

        onTurnPointClick: function () {
            tabState = 0;
            this.pointPnl.setTouchEnabled(false)
            if (!this.onCanTurnPointFunction())
                return
            this.onTurnPointFunction()

        },

        onCanTurnPointFunction: function () {

            let flag = true
            if (this._canTurnTableStatus !== 0) {
                flag = false
                if (this._canTurnTableStatus === 1) {
                    //观看视频
                    appInstance.gameAgent().Tips('转盘需要看视频哦~')
                    if (cc.sys.OS_ANDROID === cc.sys.os) {
                        appInstance.nativeApi().showRewardVideo()
                    }

                } else {
                    appInstance.gameAgent().Tips('次数已用尽，比赛场也很好玩哦')
                    this.pointPnl.setTouchEnabled(true)
                }
            }

            return flag
        },

        onTurnPointFunction: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().TURNPOINTReq(msg)

            this.TurnPointImg.stopAllActions()

            let time = 5
            let rotateAngle = 360 * 5
            let action = cc.RotateBy(time, rotateAngle)
            let beginEaseAction = cc.EaseCubicActionIn(action)

            this.TurnPointImg.runAction(beginEaseAction)
            appInstance.audioManager().playEffect(ResConfig.Sound.turnTableBegin)
        },


        playTurnTable: function (data) {
            appInstance.audioManager().playEffect(ResConfig.Sound.turnTableInd)

            this.TurnPointImg.stopAllActions()
            this.TurnPointImg.setRotation(0)
            let endtime = 3
            let rotateAngle = 360 * 5 + 36 * (data.turntableId - 1)
            let endAction = cc.RotateBy(endtime, rotateAngle)
            let endEaseAction = cc.EaseCubicActionOut(endAction)
            this.TurnPointImg.runAction(endEaseAction)

            let endCallFunc = function () {
                this._lightInterval = 0.5
                this.onShowTurnPointRewards(data)
                appInstance.audioManager().playEffect(ResConfig.Sound.turnTableEnd)
            }.bind(this)

            let delayTime = [
                1, 1, 1
            ]

            let callLightInterval = [
                0.3,
                0.1,
                0.3
            ]

            let tmpIndex = 0
            let lightCallFunc = function () {
                if (tmpIndex > 2) {
                    tmpIndex = 0
                }
                this._lightInterval = callLightInterval[tmpIndex]
                tmpIndex += 1
            }.bind(this)

            this.runAction(cc.Sequence(cc.CallFunc(lightCallFunc), cc.DelayTime(delayTime[0]),
                cc.CallFunc(lightCallFunc), cc.DelayTime(delayTime[1]),
                cc.CallFunc(lightCallFunc), cc.DelayTime(delayTime[2]),
                /*          cc.CallFunc(lightCallFunc),cc.DelayTime(delayTime[3]),
                          cc.CallFunc(lightCallFunc),cc.DelayTime(delayTime[4]),*/
                cc.CallFunc(endCallFunc)
            ))
        },

        playLight: function () {

            this['pointNd' + this._lightBg].setVisible(false)
            this['pointNd' + (1 - this._lightBg)].setVisible(true)
            this._lightBg = 1 - this._lightBg
        },

        onTurnPointResult: function (data) {

            //指针转动动画
            this.playTurnTable(data)
        },

        onShowTurnPointRewards: function (data) {
            //初始化奖励信息
            this.pgPnl.getChildByName('awardsPg').getChildByName('awardsTypePg').loadTexture(data.res)
            this.pgPnl.getChildByName('awardsPg').getChildByName('awardsVal').setString('x' + data.propNum)


            if (data.multiple <= 1) {

                this.multipleClaimBtn.setVisible(false)
                this.multSingleClaimBtn.setVisible(false)
                this.singleClaimBtn.setVisible(true)
            } else {
                let multipleClaimText = '一倍领取'
                switch (data.multiple) {
                    case 2:
                        multipleClaimText = '二倍领取'
                        break
                    case 3:
                        multipleClaimText = '三倍领取'
                        break
                    case 4:
                        multipleClaimText = '四倍领取'
                        break
                    case 5:
                        multipleClaimText = '五倍领取'
                        break
                    case 6:
                        multipleClaimText = '六倍领取'
                        break
                    case 7:
                        multipleClaimText = '七倍领取'
                        break
                    case 8:
                        multipleClaimText = '八倍领取'
                        break
                    case 9:
                        multipleClaimText = '九倍领取'
                        break
                    case 10:
                        multipleClaimText = '十倍领取'
                        break
                    default:
                        break

                }
                this.multipleClaimBtn.getChildByName('multipleClaimText').setString(multipleClaimText)
                this.multipleClaimBtn.setVisible(true)
                this.multSingleClaimBtn.setVisible(true)
                this.singleClaimBtn.setVisible(false)

            }

            this.awardsPnl.setVisible(true)
            this.pointPnl.setTouchEnabled(true)
            this.TurnPointImg.setRotation(0)
        },

        onUpdateRewardsPnl: function (data) {


        },

        onSingleClaimClick: function () {

            let msg = {}
            msg.turntableId = appInstance.dataManager().getGameData().turntableId
            msg.type = 0
            appInstance.gameAgent().httpGame().ACCCPTAWARDSReq(msg)

        },

        onMultipleClaimClick: function () {

            if (cc.sys.OS_ANDROID === cc.sys.os) {
                appInstance.nativeApi().showRewardVideo()
            }

        },
        onRewardVideoCallback: function (msg) {
            if (msg == "0") {
                if (tabState == 0) {
                    tabState = 1;
                    this.onTurnPointFunction()
                } else {
                    tabState = 0;
                    let msg = {}
                    msg.turntableId = appInstance.dataManager().getGameData().turntableId
                    msg.type = 1
                    appInstance.gameAgent().httpGame().ACCCPTAWARDSReq(msg)
                }
            }
        },


        onReceiveAwardsResult: function (data) {

            this.acceptedTypePg.loadTexture(data.res)
            this.awardsVal.setString('x' + data.propNum)

            this._canTurnTableStatus = data.code
            this.onUpdateTurnPointImg()

            this.awardsPnl.setVisible(false)
            this.acceptedPnl.setVisible(true)

        },

        onHideAcceptPnlClick: function () {

            this.acceptedPnl.setVisible(false)

        },

        onRecordClick: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().TURNTABLELOGReq(msg)

        },

        onHideRecordPnlClick: function () {
            this.recordsPnl.setVisible(false)
        },

        onShowRecordPnlClick: function (data) {


            this.recordLogListView.removeAllChildren()
            for (let i = 0; i < data.length; i++) {
                this.onUpdateDataCell(data, i)
            }

            this.recordsPnl.setVisible(true)

        },

        onUpdateDataCell: function (list, index) {
            let recordCell = this.recordDataCell.clone()
            recordCell.setVisible(true)
            this.recordLogListView.pushBackCustomItem(recordCell)

            if (Math.floor(index % 2)) {
                recordCell.getChildByName('bg').setVisible(true)
            } else {
                recordCell.getChildByName('bg').setVisible(false)
            }

            let record = list[index]
            let awardsText = record.propName + record.propNum + record.propUnit
            recordCell.getChildByName('timeText').setString(this.onFormatDateTime(record.time))
            recordCell.getChildByName('awardsText').setString(awardsText)
        },

        onFormatDateTime: function (timestamp) {

            let d = new Date(parseInt(timestamp));
            let month = (d.getMonth() + 1) < 10 ? (0 + "" + (d.getMonth() + 1)) : (d.getMonth() + 1);
            let day = d.getDate() < 10 ? (0 + "" + d.getDate()) : d.getDate();
            let hour = d.getHours() < 10 ? (0 + "" + d.getHours()) : d.getHours();
            let minute = d.getMinutes() < 10 ? (0 + "" + d.getMinutes()) : d.getMinutes();
            let second = d.getSeconds() < 10 ? (0 + "" + d.getSeconds()) : d.getSeconds();
            let dateString = d.getFullYear() + "-" + month + "-" + day + " " + hour + ": " + minute

            return dateString;

        }


    })
    return turnTableLayer
})
