
load('game/public/HttpGame', function () {
    let HttpEvent = include('game/config/HttpEvent')
    let AppConfig = include('game/public/AppConfig')
    let ResConfig = include('game/config/ResConfig')
    let GameEvent = include('game/config/GameEvent')
    let LocalSave = include('game/public/LocalSave')

    let Ui = ResConfig.Ui

    let HttpGame  = cc.Class.extend({
        _requestBackCall: {},
        ctor: function () {
            appInstance.httpAgent().setUrl(AppConfig.httpUrl)
            appInstance.httpAgent().setCallBack(this.requestBack)
        },

        httpLogin: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_LOGIN]) {
                this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_LOGIN] = this.httpLoginBack
            }
            let sendMsg = {
                platform: 1,
                account:'',
                device: 'devicestr',
                phoneModel: cc.sys.os,
                unionId: '',
            }
            if (cc.sys.OS_WINDOWS === cc.sys.os) {
                let imeiStr = global.localStorage.getStringForKey(LocalSave.LocalImei)
                if (!imeiStr) {
                    imeiStr = 'windows imei random' + Math.floor(Math.random() * 1000000)
                    cc.log('=====imei====' + imeiStr)
                    global.localStorage.setStringForKey(LocalSave.LocalImei, imeiStr)
                }
                sendMsg.imei = imeiStr
            } else {
                sendMsg.imei = appInstance.nativeApi().getImei()
            }

            this.checkSendMsg(sendMsg, msg)
            sendMsg.msgID = HttpEvent.MJ_HALL_MESSAGE_LOGIN
            appInstance.httpAgent().sendPost(sendMsg)
        },

        httpLoginBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('=========httpLoginBack============error================')
                return
            }
            let saveKey = [
                'platform',
                'account',
                'key',
                'pid',
                'timetamp',
                'lastChannel',
                'fistLogin'
            ]

            appInstance.dataManager().getUserData().saveMsg(msg, saveKey)

            let msgCommon = {}
            msgCommon.pid = msg.pid
            msgCommon.key = msg.key
            appInstance.httpAgent().setCommonData(msgCommon)

            appInstance.msgTool().setHeadPid(msg.pid)

            if (msg.lastChannel) {
                let sendData = {}
                sendData.channel = msg.lastChannel
                appInstance.gameAgent().httpGame().chooseCity(sendData)
            } else {
                appInstance.gameAgent().addPopUI(Ui.ChooseCityLayer)
            }
        },


        chooseCity: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_CHOOSE_CITY]) {
                this._requestBackCall[HttpEvent.MJ_HALL_CHOOSE_CITY] = this.chooseCityBack
            }
            let sendMsg = {
                channel: 0
            }
            this.checkSendMsg(sendMsg, msg)

            msg.msgID = HttpEvent.MJ_HALL_CHOOSE_CITY
            appInstance.httpAgent().sendPost(msg)
        },

        chooseCityBack: function (msg) {
            cc.log('   选择城市:::chooseCityBack>>>\n' + JSON.stringify(msg))
            if (msg.status !== 0) {
                return
            }
            let saveKey = [
                'hostip',
                'hostport',
                'firstLogin',
                'vipFlag',
                'channel',

            ]
            appInstance.dataManager().getUserData().saveMsg(msg, saveKey)

            let msgCommon = {}
            msgCommon.serviceNum = msg.serviceNum
            appInstance.httpAgent().setCommonData(msgCommon)
            appInstance.msgTool().setHeadServerId(msg.serviceNum)

            appInstance.gameNet().connect(msg.hostip, msg.hostport)

            appInstance.gameAgent().httpGame().checkHallRed()
        },


        checkHallRed: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_CHECK_HALL_FLAG]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_CHECK_HALL_FLAG] = this.checkHallRedBack
            }

            msg.msgID = HttpEvent.MJ_HALL_PLAYER_CHECK_HALL_FLAG
            appInstance.httpAgent().sendPost(msg)
        },


        checkHallRedBack: function (msg) {
            cc.log('   检测大厅红点:::checkHallRed>>>\n' + JSON.stringify(msg))



        },

        checkSendMsg: function (sendMsg, msg) {
            for (let key in sendMsg) {
                sendMsg[key] = msg[key] ? msg[key] : sendMsg[key]
            }
        },

        getRequestBackCall: function () {
            return this._requestBackCall
        },

        requestBack: function (msg) {
            if (msg.code !== 0) {
                cc.log('------------->>>httpGame error happen')
                return
            }
            let data = msg.data
            let msgID = appInstance.msgTool().GET_HTTP_RETID(data.id)
            if (appInstance.gameAgent().httpGame().getRequestBackCall()[msgID]) {
                appInstance.gameAgent().httpGame().getRequestBackCall()[msgID](data)
            } else {
                cc.log('there is not callback for this msg:::' + msgID)
            }
        },

        userDataReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_USERDATA]) {
                this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_USERDATA] = this.userDataBack
            }

            msg.msgID = HttpEvent.MJ_HALL_MESSAGE_USERDATA
            appInstance.httpAgent().sendPost(msg)
        },

        userDataBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame error happen')
                return
            }
            msg.pRole = msg.roleCode
            let saveKey = [
                'pname',
                'coin',
                'diamonds',
                'fuKa',
                'pRole',
                'nameUpdate',
                'photo',
                'sdkphotourl',
                'nameUpdate',
                'isAuthentication',
                'agentFlag'
            ]
            appInstance.dataManager().getUserData().saveMsg(msg, saveKey)
            appInstance.sendNotification(GameEvent.USERDATA, msg)

        },

        updateUserNameReq: function (msg) {

            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_UPDATEUSERNAME]) {
                this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_UPDATEUSERNAME] = this.updateUserNameBack
            }

            msg.msgID = HttpEvent.MJ_HALL_MESSAGE_UPDATEUSERNAME
            appInstance.httpAgent().sendPost(msg)

        },

        updateUserNameBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame updateUserNameBack error happen')
                return
            }

            let saveKey = [
                'pname',
                'nameUpdate'
            ]
            appInstance.dataManager().getUserData().saveMsg(msg, saveKey)
            appInstance.sendNotification(GameEvent.UPDATE_USERNAME, msg)

        },

        authenticationReq: function (msg) {

            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_REAL_NAME]) {
                this._requestBackCall[HttpEvent.MJ_HALL_REAL_NAME] = this.authenticationBack
            }

            msg.msgID = HttpEvent.MJ_HALL_REAL_NAME
            appInstance.httpAgent().sendPost(msg)

        },

        authenticationBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame authenticationBack error happen')
                return
            }

            let saveKey = [
                'isAuthentication'
            ]
            let data = {'isAuthentication':0}
            appInstance.dataManager().getUserData().saveMsg(data, saveKey)
            appInstance.sendNotification(GameEvent.AUTHENICATION, msg)

        },

        cashCowNumReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_CASH_COW_NUM]) {
                this._requestBackCall[HttpEvent.MJ_HALL_CASH_COW_NUM] = this.cashCowNumBack
            }

            msg.msgID = HttpEvent.MJ_HALL_CASH_COW_NUM
            appInstance.httpAgent().sendPost(msg)

        },

        cashCowNumBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame cashCowNumBack error happen')
                return
            }

            cc.log('------------->>>httpGame cashCowNumBack  data : ' + JSON.stringify(msg))
            let saveKey = [
                'cashCowNum',
                'usedCashCowNum'
            ]

            appInstance.dataManager().getUserData().saveMsg(msg, saveKey)
            appInstance.sendNotification(GameEvent.GET_CASHCOWNUM, msg)

        },

        cashCowReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_CASH_COW]) {
                this._requestBackCall[HttpEvent.MJ_HALL_CASH_COW] = this.cashCowBack
            }

            msg.msgID = HttpEvent.MJ_HALL_CASH_COW
            appInstance.httpAgent().sendPost(msg)

        },

        cashCowBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame cashCowBack error happen')
                return
            }

            console.log('----------------- cashCowBack data : ' + JSON.stringify(msg));
            let saveKey = [

                'usedCashCowNum'
            ]

            let _usedCashCowNum = appInstance.dataManager().getUserData().usedCashCowNum + 1
            let saveData = {

                'usedCashCowNum' : _usedCashCowNum

            }

            appInstance.dataManager().getUserData().saveMsg(saveData, saveKey)
            appInstance.sendNotification(GameEvent.UPDATE_CASHCOWNUM, msg)

        },

        cashCowRecordReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_CASH_COW_LOG]) {
                this._requestBackCall[HttpEvent.MJ_HALL_CASH_COW_LOG] = this.cashCowRecordBack
            }

            msg.msgID = HttpEvent.MJ_HALL_CASH_COW_LOG
            appInstance.httpAgent().sendPost(msg)

        },

        cashCowRecordBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame cashCowRecordBack error happen')
                return
            }

            console.log('-------------------cashCowRecordBack data : ' + JSON.stringify(msg))
            appInstance.sendNotification(GameEvent.GET_CASHCOWRECORD, msg)

        },

        TURNTABLEReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_TURNTABLE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_TURNTABLE] = this.TURNTABLEBack
            }

            msg.msgID = HttpEvent.MJ_HALL_TURNTABLE
            appInstance.httpAgent().sendPost(msg)

        },

        TURNTABLEBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame TURNTABLEBack error happen')
                return
            }


            console.log('-------------------TURNTABLEBack data : ' + JSON.stringify(msg))
            appInstance.sendNotification(GameEvent.TURNTABLE_INIT, msg)

        },

        TURNPOINTReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_TURNPOINT]) {
                this._requestBackCall[HttpEvent.MJ_HALL_TURNPOINT] = this.TURNPOINTBack
            }

            msg.msgID = HttpEvent.MJ_HALL_TURNPOINT
            appInstance.httpAgent().sendPost(msg)

        },

        TURNPOINTBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame TURNPOINTBack error happen')
                return
            }

            console.log('-------------------TURNPOINTBack data : ' + JSON.stringify(msg))

            let saveKey = [

                'turntableId'
            ]

            let saveData = {

                'turntableId' : msg.turntableId

            }

            appInstance.dataManager().getGameData().saveMsg(saveData,saveKey)
            appInstance.sendNotification(GameEvent.TURNTABLE_POINT, msg)

        },

        ACCCPTAWARDSReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TURNTABLE_RECEIVE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TURNTABLE_RECEIVE] = this.ACCCPTAWARDSBack
            }

            msg.msgID = HttpEvent.MJ_HALL_PLAYER_TURNTABLE_RECEIVE
            appInstance.httpAgent().sendPost(msg)

        },

        ACCCPTAWARDSBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame ACCCPTAWARDSBack error happen')
                return
            }

            console.log('-------------------ACCCPTAWARDSBack data : ' + JSON.stringify(msg))
            appInstance.sendNotification(GameEvent.TURNTABLE_RECEIVE, msg)

        },

        REFRESHAWARDSDATAReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_LUCKY_PRIZE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_LUCKY_PRIZE] = this.REFRESHAWARDSDATABack
            }

            msg.msgID = HttpEvent.MJ_HALL_LUCKY_PRIZE
            appInstance.httpAgent().sendPost(msg)

        },

        REFRESHAWARDSDATABack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame REFRESHAWARDSDATABack error happen')
                return
            }

            console.log('-------------------REFRESHAWARDSDATABack data : ' + JSON.stringify(msg))
            appInstance.sendNotification(GameEvent.TURNTABLE_LUCKY_PRIZE, msg)

        },

        TURNTABLELOGReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TURNTABLE_LOG]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TURNTABLE_LOG] = this.TURNTABLELOGBack
            }

            msg.msgID = HttpEvent.MJ_HALL_PLAYER_TURNTABLE_LOG
            appInstance.httpAgent().sendPost(msg)

        },

        TURNTABLELOGBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame TURNTABLELOGBack error happen')
                return
            }

            console.log('-------------------TURNTABLELOGBack data : ' + JSON.stringify(msg))
            appInstance.sendNotification(GameEvent.TURNTABLE_PLAYERLOG, msg)

        },


        TASKDAILYReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_DAILY]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_DAILY] = this.TASKDAILYBack
            }

            msg.msgID = HttpEvent.MJ_HALL_PLAYER_TASK_DAILY
            appInstance.httpAgent().sendPost(msg)

        },

        TASKDAILYBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame TASKDAILYBack error happen')
                return
            }

            appInstance.sendNotification(GameEvent.TASK_DAILY, msg)

        },

        RECEIVEDAILYREWARDSReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_DAILY_RECEIVE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_DAILY_RECEIVE] = this.RECEIVEDAILYREWARDSBack
            }

            msg.msgID = HttpEvent.MJ_HALL_PLAYER_TASK_DAILY_RECEIVE
            appInstance.httpAgent().sendPost(msg)

        },

        RECEIVEDAILYREWARDSBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame TASKDAILYBack error happen')
                return
            }

            cc.log('------------------------------ RECEIVEDAILYREWARDSBack msg : ' + JSON.stringify(msg))
            appInstance.sendNotification(GameEvent.TASK_DAILY_RECEIVEREWARDS, msg)

        },


        TASKCHALLENGEReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_CHALLENGE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_CHALLENGE] = this.TASKCHALLENGEBack
            }

            msg.msgID = HttpEvent.MJ_HALL_PLAYER_TASK_CHALLENGE
            appInstance.httpAgent().sendPost(msg)

        },

        TASKCHALLENGEBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame TASKCHALLENGEBack error happen')
                return
            }


            appInstance.sendNotification(GameEvent.TASK_CHALLENGE, msg)

        },

        RECEIVECHALLENGEWARDSReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_CHALLENGE_RECEIVE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_CHALLENGE_RECEIVE] = this.RECEIVECHALLENGEWARDSBack
            }

            msg.msgID = HttpEvent.MJ_HALL_PLAYER_TASK_CHALLENGE_RECEIVE
            appInstance.httpAgent().sendPost(msg)

        },

        RECEIVECHALLENGEWARDSBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame RECEIVECHALLENGEWARDSBack error happen')
                return
            }



            appInstance.sendNotification(GameEvent.TASK_CHALLENGE_RECEIVE, msg)

        },

        REFRESHCHALLENGETASKSReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_CHALLENGE_REFRESH]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_CHALLENGE_REFRESH] = this.REFRESHCHALLENGETASKBack
            }

            msg.msgID = HttpEvent.MJ_HALL_PLAYER_TASK_CHALLENGE_REFRESH
            appInstance.httpAgent().sendPost(msg)

        },

        REFRESHCHALLENGETASKBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame REFRESHCHALLENGETASKBack error happen')
                return
            }


            appInstance.sendNotification(GameEvent.TASK_CHALLENGE_REFRESH, msg)

        },

        SIGNINDATASReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_CHECK_IN_INFO]) {
                this._requestBackCall[HttpEvent.MJ_HALL_CHECK_IN_INFO] = this.SIGNINDATASBack
            }

            msg.msgID = HttpEvent.MJ_HALL_CHECK_IN_INFO
            appInstance.httpAgent().sendPost(msg)

        },

        SIGNINDATASBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame SIGNINDATASBack error happen')
                return
            }

            appInstance.sendNotification(GameEvent.SIGN_IN_DATA, msg)

        },

        CHECKINReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_CHECK_IN]) {
                this._requestBackCall[HttpEvent.MJ_HALL_CHECK_IN] = this.CHECKINBack
            }

            msg.msgID = HttpEvent.MJ_HALL_CHECK_IN
            appInstance.httpAgent().sendPost(msg)

        },

        CHECKINBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame CHECKINBack error happen')
                return
            }

            appInstance.sendNotification(GameEvent.SIGN_IN_DATA_CHECK, msg)

        },

        /**
         * 我的邀请-任务数据请求
         * @param msg
         */
        myInviteReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_INVITE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_INVITE] = this.myInviteBack
            }

            msg.msgID = HttpEvent.MJ_HALL_PLAYER_TASK_INVITE
            appInstance.httpAgent().sendPost(msg)

        },

        /**
         * 我的邀请-任务数据返回请求
         * @param msg
         */
        myInviteBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame myInviteBack error happen')
                return
            }

            appInstance.sendNotification(GameEvent.MY_INVITE, msg)

        },

        /**
         * 我的邀请-做任务请求
         * @param msg
         */
        receiveMyInviteReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_INVITE_RECEIVE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_TASK_INVITE_RECEIVE] = this.receiveMyInviteBack
            }

            msg.msgID = HttpEvent.MJ_HALL_PLAYER_TASK_INVITE_RECEIVE
            appInstance.httpAgent().sendPost(msg)

        },

        /**
         * 我的邀请-做任务返回请求
         * @param msg
         */
        receiveMyInviteBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame TASKDAILYBack error happen')
                return
            }
            cc.log('------------------------------ receiveMyInviteBack msg : ' + JSON.stringify(msg))
            appInstance.sendNotification(GameEvent.MY_INVITE_RECEIVE, msg)

        },

        /**
         * 分享详情-数据请求
         * @param msg
         */
        shareInviteReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_INVITE_LOG]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_INVITE_LOG] = this.shareInviteBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_INVITE_LOG
            appInstance.httpAgent().sendPost(msg)

        },

        /**
         * 分享详情-数据返回请求
         * @param msg
         */
        shareInviteBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame shareInviteBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.SHARE_INVITE, msg)

        },



        GETVIPINFOReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_VIP_INFO]) {
                this._requestBackCall[HttpEvent.MJ_HALL_VIP_INFO] = this.GETVIPINFOBack
            }
            msg.msgID = HttpEvent.MJ_HALL_VIP_INFO
            appInstance.httpAgent().sendPost(msg)

        },


        GETVIPINFOBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame GETVIPINFOBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.VIP_INFO_GET, msg)

        },


        RECEIVEVIPDAILYReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_VIP_DAILY_GIFT_RECEIVE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_VIP_DAILY_GIFT_RECEIVE] = this.RECEIVEVIPDAILYBack
            }
            msg.msgID = HttpEvent.MJ_HALL_VIP_DAILY_GIFT_RECEIVE
            appInstance.httpAgent().sendPost(msg)

        },


        RECEIVEVIPDAILYBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame RECEIVEVIPDAILYBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.VIP_INFO_RECEIVE, msg)

        },

        GETROLESReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_ROLE_GET]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_ROLE_GET] = this.GETROLESBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_ROLE_GET
            appInstance.httpAgent().sendPost(msg)

        },


        GETROLESBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame GETROLESBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.ROLES_GET, msg)

        },

        BUYROLEReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_ROLE_BUY]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_ROLE_BUY] = this.BUYROLEBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_ROLE_BUY
            appInstance.httpAgent().sendPost(msg)

        },


        BUYROLEBack: function (msg) {
            if (msg.status !== 0) {
                appInstance.gameAgent().Tips('购买失败！')
                return
            }
            appInstance.gameAgent().Tips('购买成功！')
            appInstance.sendNotification(GameEvent.ROLES_BUY, msg)

        },

        ROLESELECTEDReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_ROLE_SELECT]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_ROLE_SELECT] = this.ROLESELECTEDBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_ROLE_SELECT
            appInstance.httpAgent().sendPost(msg)

        },


        ROLESELECTEDBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame ROLESELECTEDBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.ROLES_SELECT, msg)

        },

        COINSSHOPDATASReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_COIN_STORE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_COIN_STORE] = this.COINSSHOPDATASBack
            }
            msg.msgID = HttpEvent.MJ_HALL_COIN_STORE
            appInstance.httpAgent().sendPost(msg)
        },


        COINSSHOPDATASBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame COINSSHOPDATASBack error happen')
                return
            }

            appInstance.sendNotification(GameEvent.COINSHOP_GET, msg)
        },

        COINSSHOPBUYReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_COIN_BUY]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_COIN_BUY] = this.COINSSHOPBUYBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_COIN_BUY
            appInstance.httpAgent().sendPost(msg)

        },


        COINSSHOPBUYBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame COINSSHOPBUYBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.COINSHOP_BUY, msg)

        },

        UPDATEADDRESSReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_UPDATE_DELIVERY_ADDRESS]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_UPDATE_DELIVERY_ADDRESS] = this.UPDATEADDRESSBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_UPDATE_DELIVERY_ADDRESS
            appInstance.httpAgent().sendPost(msg)

        },


        UPDATEADDRESSBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame UPDATEADDRESSBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.ADRESS_UPDATE, msg)

        },

        GETADDRESSReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_GET_DELIVERY_ADDRESS]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_GET_DELIVERY_ADDRESS] = this.GETADDRESSBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_GET_DELIVERY_ADDRESS
            appInstance.httpAgent().sendPost(msg)

        },


        GETADDRESSBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame GETADDRESSBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.ADRESS_GET, msg)

        },

        VIDEOFORDIAMONDSReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_WATCH_VIDEO_GET_DIAMONDS]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_WATCH_VIDEO_GET_DIAMONDS] = this.VIDEOFORDIAMONDSBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_WATCH_VIDEO_GET_DIAMONDS
            appInstance.httpAgent().sendPost(msg)

        },


        VIDEOFORDIAMONDSBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame VIDEOFORDIAMONDSBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.VIDEO_WATCH_DIAMONDS, msg)

        },

        VIPPaysOrderReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_BUY_VIP_ORDER]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_BUY_VIP_ORDER] = this.VIPPaysOrderBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_BUY_VIP_ORDER
            appInstance.httpAgent().sendPost(msg)

        },


        VIPPaysOrderBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame VIPPaysOrderBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.PLAYER_BUY_VIP_ORDER, msg)

        },
    })
    return HttpGame
})