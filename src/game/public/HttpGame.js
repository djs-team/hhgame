load('game/public/HttpGame', function () {
    let HttpEvent = include('game/config/HttpEvent')
    let AppConfig = include('game/public/AppConfig')
    let ResConfig = include('game/config/ResConfig')
    let GameEvent = include('game/config/GameEvent')
    let LocalSave = include('game/public/LocalSave')

    let Ui = ResConfig.Ui
    let nickName = "";
    let photo = "";

    let HttpGame = cc.Class.extend({
        _requestBackCall: {},
        ctor: function () {
            appInstance.httpAgent().setUrl(AppConfig.httpUrl)
            appInstance.httpAgent().setCallBack(this.requestBack)
        },

        /**
         * 登录请求
         * @param msg
         */
        httpLogin: function (msg) {
            if (msg.platform == 3) {
                nickName = msg.nickName;
                photo = msg.photo;
            }
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_LOGIN]) {
                this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_LOGIN] = this.httpLoginBack
            }
            let sendMsg = {
                platform: 1,
                account: '',
                device: 'devicestr',
                phoneModel: cc.sys.os,
                imei: '',
                unionId: '',
                invitationPid: ""
            }
            let myParam = cc.sys.localStorage.getItem("installParam");
            msg.invitationPid = myParam;
            cc.log('=========httpLoginRequest===============' + JSON.stringify(msg))
            this.checkSendMsg(sendMsg, msg)
            sendMsg.msgID = HttpEvent.MJ_HALL_MESSAGE_LOGIN
            appInstance.httpAgent().sendPost(sendMsg)

        },

        /**
         * 登录回调
         * @param msg
         */
        httpLoginBack: function (msg) {
            cc.log('=========httpLoginBack================' + JSON.stringify(msg))
            if (msg.status !== 0) {
                return
            }
            let saveKey = [
                'platform',
                'account',
                'key',   //登陆成功后，服务端存的session KEY
                'pid',
                'sex',
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

            //判断是否要跳到选择城市layer
            if (msg.lastChannel) {
                let sendData = {}
                sendData.channel = msg.lastChannel
                appInstance.gameAgent().httpGame().chooseCity(sendData)
            } else {
                appInstance.gameAgent().addPopUI(Ui.ChooseCityLayer)
            }

            if (msg.platform == 3) {
                // 更新昵称
                let msgNickName = {}
                msgNickName.nickname = nickName;
                msgNickName.type = 0;
                cc.log("=============updateUserNameReq" + JSON.stringify(msg))
                appInstance.gameAgent().httpGame().updateUserNameReq(msgNickName)
                //更新头像
                let msgPhoto = {}
                msgPhoto.photoUrl = photo;
                cc.log("=============updatePhoto" + JSON.stringify(msgPhoto))
                appInstance.gameAgent().httpGame().updateUserPhotoReq(msgPhoto)
            }
        },

        /**
         * 城市请求
         * @param msg
         */
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

        /**
         * 回调城市
         * @param msg
         */
        chooseCityBack: function (msg) {
            cc.log('   选择城市:::chooseCityBack>>>\n' + JSON.stringify(msg))
            if (msg.status !== 0) {
                return
            }
            let saveKey = [
                'hostip',  //客户端需要连接哪个网关的IP
                'hostport',  //端口号
                'firstLogin',  //是否每天第一次登陆0是1不是
                'vipFlag',  //vip弹窗标记0：正常不弹窗1：弹vip界面 2：弹出vip不足三天提示
                'channel', //城市编号
            ]
            appInstance.dataManager().getUserData().saveMsg(msg, saveKey)

            let msgCommon = {}
            msgCommon.serviceNum = msg.serviceNum
            appInstance.httpAgent().setCommonData(msgCommon)
            appInstance.msgTool().setHeadServerId(msg.serviceNum)

            appInstance.gameNet().connect(msg.hostip, msg.hostport)

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
            appInstance.sendNotification(GameEvent.HALL_RED_BACK, msg)
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
            cc.log('---http msg back------>>> ' + JSON.stringify(msg))
            if (msg.code !== 0) {
                cc.log('----requestBack--------->>>httpGame error happen===' + JSON.stringify(msg))
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

        /**
         * 用户数据请求
         * @param msg
         */
        userDataReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_USERDATA]) {
                this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_USERDATA] = this.userDataBack
            }

            msg.msgID = HttpEvent.MJ_HALL_MESSAGE_USERDATA
            appInstance.httpAgent().sendPost(msg)
        },

        /**
         * 用户数据返回
         * @param msg
         */
        userDataBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------userDataBack------->>>httpGame error happen')
                return
            }
            msg.pRole = msg.roleCode
            let saveKey = [
                'pid',
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
                'agentFlag',
                'isHaveAdress',//0未填写，1填写
                'vipCode',//0 不是会员 1周会员 2 月 3 季 4年
            ]
            appInstance.dataManager().getUserData().saveMsg(msg, saveKey)
            appInstance.sendNotification(GameEvent.USERDATA, msg)

        },

        updateUserNameReq: function (msg) {
            cc.log("========开始请求updateUserName")
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_UPDATEUSERNAME]) {
                this._requestBackCall[HttpEvent.MJ_HALL_MESSAGE_UPDATEUSERNAME] = this.updateUserNameBack
            }

            msg.msgID = HttpEvent.MJ_HALL_MESSAGE_UPDATEUSERNAME
            appInstance.httpAgent().sendPost(msg)

        },

        updateUserNameBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('-----updateUserNameBack-------->>>httpGame updateUserNameBack error happen')
                return
            }
            cc.log('------------->>>httpGame updateUserNameBack ' + JSON.stringify(msg))

            let saveKey = [
                'pname',
                'nameUpdate'
            ]
            appInstance.dataManager().getUserData().saveMsg(msg, saveKey)
            appInstance.sendNotification(GameEvent.UPDATE_USERNAME, msg)

        },
        updateUserPhotoReq: function (msg) {
            cc.log("========开始请求updateUserPhotoReq")
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_PHOTO_CHANGE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_PHOTO_CHANGE] = this.updateUserPhotoBack
            }

            msg.msgID = HttpEvent.MJ_HALL_PLAYER_PHOTO_CHANGE
            appInstance.httpAgent().sendPost(msg)

        },

        updateUserPhotoBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame updateUserPhotoBack error happen')
                return
            }
            cc.log('------------->>>httpGame updateUserPhotoBack ' + JSON.stringify(msg))

            // let saveKey = [
            //     'pphoto',
            //     'photoUpdate'
            // ]
            // appInstance.dataManager().getUserData().saveMsg(msg, saveKey)
            appInstance.sendNotification(GameEvent.UPDATE_USERPHOTO, msg)

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
                cc.log('------authenticationBack------->>>httpGame authenticationBack error happen')
                return
            }

            let saveKey = [
                'isAuthentication'
            ]
            let data = {'isAuthentication': 0}
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
                cc.log('-----cashCowNumBack-------->>>httpGame cashCowNumBack error happen')
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
            console.log('----------------- cashCowReq data : ' + JSON.stringify(msg));
            appInstance.httpAgent().sendPost(msg)

        },

        cashCowBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------cashCowBack------->>>httpGame cashCowBack error happen')
                return
            }

            console.log('----------------- cashCowBack data : ' + JSON.stringify(msg));
            let saveKey = [

                'usedCashCowNum'
            ]

            let _usedCashCowNum = appInstance.dataManager().getUserData().usedCashCowNum + 1
            let saveData = {

                'usedCashCowNum': _usedCashCowNum

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
                cc.log('------cashCowRecordBack------->>>httpGame cashCowRecordBack error happen')
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
                cc.log('----TURNTABLEBack--------->>>httpGame TURNTABLEBack error happen')
                return
            }


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


            let saveKey = [

                'turntableId'
            ]

            let saveData = {

                'turntableId': msg.turntableId

            }

            appInstance.dataManager().getGameData().saveMsg(saveData, saveKey)
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
                let errorStr = ''
                if(msg.status === 76)
                    errorStr = '任务未完成'
                else if(msg.status === 77)
                    errorStr = '任务已领取'
                errorStr = errorStr + '，错误码：' + msg.status + '，请刷新后重试，或联系客服'
                appInstance.gameAgent().Tips(errorStr)
                return
            }

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

            let saveKey = [

                'pRole',
            ]

            let saveData = {

                'pRole': msg.roleCode
            }
            appInstance.dataManager().getUserData().saveMsg(saveData, saveKey)
            appInstance.sendNotification(GameEvent.ROLES_SELECT, msg)
            let ruleMsg = {}
            appInstance.sendNotification(GameEvent.URLE_USED_UPDATE, ruleMsg)

        },

        /**
         * 发起商城请求
         * @param msg
         * @constructor
         */
        COINSSHOPDATASReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_COIN_STORE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_COIN_STORE] = this.COINSSHOPDATASBack
            }
            msg.msgID = HttpEvent.MJ_HALL_COIN_STORE
            appInstance.httpAgent().sendPost(msg)
        },

        /**
         * 接收商城请求
         * @param msg
         * @constructor
         */
        COINSSHOPDATASBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame COINSSHOPDATASBack error happen')
                return
            }

            appInstance.sendNotification(GameEvent.COINSHOP_GET, msg)
        },

        /**
         * 钻石下单购买请求
         * @param msg
         * @constructor
         */
        ZuanPaysOrderReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_BUY_DIAMONDS_ORDER]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_BUY_DIAMONDS_ORDER] = this.ZuanPaysOrderBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_BUY_DIAMONDS_ORDER
            appInstance.httpAgent().sendPost(msg)

        },

        /**
         * 接收钻石下单购买
         * @param msg
         * @constructor
         */
        ZuanPaysOrderBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame VIPPaysOrderBack error happen' + JSON.stringify(msg))
                return
            }
            appInstance.sendNotification(GameEvent.DIAMONDS_BUY, msg)

        },

        /**
         * 购买请求
         * @param msg
         * @constructor
         */
        COINSSHOPBUYReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_COIN_BUY]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_COIN_BUY] = this.COINSSHOPBUYBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_COIN_BUY
            appInstance.httpAgent().sendPost(msg)

        },

        /**
         * 购买回调
         * @param msg
         * @constructor
         */
        COINSSHOPBUYBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame COINSSHOPBUYBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.COINSHOP_BUY, msg)

        },

        /**
         * 地址请求
         * @param msg
         * @constructor
         */
        UPDATEADDRESSReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_UPDATE_DELIVERY_ADDRESS]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_UPDATE_DELIVERY_ADDRESS] = this.UPDATEADDRESSBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_UPDATE_DELIVERY_ADDRESS
            appInstance.httpAgent().sendPost(msg)

        },

        /**
         * 地址回调
         * @param msg
         * @constructor
         */
        UPDATEADDRESSBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame UPDATEADDRESSBack error happen')
                return
            }
            let saveKey = [
                'isHaveAdress'//0未填写，1填写
            ]
            let saveData = {

                'isHaveAdress': 1

            }
            appInstance.dataManager().getUserData().saveMsg(saveData, saveKey)
            appInstance.sendNotification(GameEvent.ADRESS_UPDATE, msg)

        },

        /**
         * 请求获取玩家信息
         * @param msg
         * @constructor
         */
        GETADDRESSReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_GET_DELIVERY_ADDRESS]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_GET_DELIVERY_ADDRESS] = this.GETADDRESSBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_GET_DELIVERY_ADDRESS
            appInstance.httpAgent().sendPost(msg)

        },

        /**
         * 回收玩家信息
         * @param msg
         * @constructor
         */
        GETADDRESSBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame GETADDRESSBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.ADRESS_GET, msg)

        },

        /**
         * 观看视频请求
         * @param msg
         * @constructor
         */
        VIDEOFORDIAMONDSReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_WATCH_VIDEO_GET_DIAMONDS]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_WATCH_VIDEO_GET_DIAMONDS] = this.VIDEOFORDIAMONDSBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_WATCH_VIDEO_GET_DIAMONDS
            appInstance.httpAgent().sendPost(msg)

        },

        /**
         * 视频请求回调
         * @param msg
         * @constructor
         */
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
                cc.log('------------->>>httpGame VIPPaysOrderBack error happen' + JSON.stringify(msg))
                return
            }
            appInstance.sendNotification(GameEvent.PLAYER_BUY_VIP_ORDER, msg)

        },

        // 苹果支付成功订单校验
        VIPPaysOrderAppleCheckReq: function (msg) {
            msg = msg || {}
            cc.log('-------------VIPPaysOrderAppleCheckReq---Request-----：' + JSON.stringify(msg))
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_BUY_VIP_ORDER_APPLE_CHECK]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_BUY_VIP_ORDER_APPLE_CHECK] = this.VIPPaysOrderAppleCheckBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_BUY_VIP_ORDER_APPLE_CHECK
            appInstance.httpAgent().sendPost(msg)
        },

        VIPPaysOrderAppleCheckBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame VIPPaysOrderAppleCheckBack error happen' + JSON.stringify(msg))
                return
            }
            appInstance.sendNotification(GameEvent.PLAYER_BUY_VIP_ORDER_APPLE_CHECK, msg)

        },

        ROLLIMGLISTReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_STORE_OUT_GOODS]) {
                this._requestBackCall[HttpEvent.MJ_HALL_STORE_OUT_GOODS] = this.ROLLIMGLISTBack
            }
            msg.msgID = HttpEvent.MJ_HALL_STORE_OUT_GOODS
            appInstance.httpAgent().sendPost(msg)

        },


        ROLLIMGLISTBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame ROLLIMGLISTBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.GETROLLIMGLIST, msg)

        },

        MENULISTReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_STORE_GOODS_TYPE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_STORE_GOODS_TYPE] = this.MENULISTBack
            }
            msg.msgID = HttpEvent.MJ_HALL_STORE_GOODS_TYPE
            appInstance.httpAgent().sendPost(msg)

        },


        MENULISTBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame MENULISTBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.MENULIST, msg)

        },

        GOODSLISTReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.PLAYER_SHOPPING]) {
                this._requestBackCall[HttpEvent.PLAYER_SHOPPING] = this.GOODSLISTBack
            }
            msg.msgID = HttpEvent.PLAYER_SHOPPING
            appInstance.httpAgent().sendPost(msg)

        },


        GOODSLISTBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame GOODSLISTBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.GOODSLIST, msg)

        },

        FUKABUYGOODSReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_GOODS_BUY]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_GOODS_BUY] = this.FUKABUYGOODSBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_GOODS_BUY
            appInstance.httpAgent().sendPost(msg)

        },


        FUKABUYGOODSBack: function (msg) {

            appInstance.sendNotification(GameEvent.FUKA_BUGGOODS, msg)

        },

        FUKAROBLISTReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_FUKA_DUOBAO]) {
                this._requestBackCall[HttpEvent.MJ_HALL_FUKA_DUOBAO] = this.FUKAROBLISTBack
            }
            msg.msgID = HttpEvent.MJ_HALL_FUKA_DUOBAO
            appInstance.httpAgent().sendPost(msg)

        },


        FUKAROBLISTBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame FUKA_ROBLISTBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.FUKA_ROBLIST, msg)

        },


        FUKACARDLISTReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_CARD_GOODS]) {
                this._requestBackCall[HttpEvent.MJ_HALL_CARD_GOODS] = this.FUKACARDLISTBack
            }
            msg.msgID = HttpEvent.MJ_HALL_CARD_GOODS
            appInstance.httpAgent().sendPost(msg)

        },


        FUKACARDLISTBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame FUKA_ROBLISTBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.FUKA_CARDLIST, msg)

        },

        FUKAROBReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_DUO_BAO]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_DUO_BAO] = this.FUKAROBBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_DUO_BAO
            appInstance.httpAgent().sendPost(msg)

        },


        FUKAROBBack: function (msg) {

            appInstance.sendNotification(GameEvent.FUKA_ROB, msg)

        },

        FUKAROBLOGReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_DUO_BAO_LOG]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_DUO_BAO_LOG] = this.FUKAROBLOGBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_DUO_BAO_LOG
            appInstance.httpAgent().sendPost(msg)

        },


        FUKAROBLOGBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame FUKAROBLOGBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.FUKA_ROBLOG, msg)

        },

        FUKAMATERIALLISTReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_ENTITY_GOODS]) {
                this._requestBackCall[HttpEvent.MJ_HALL_ENTITY_GOODS] = this.FUKAMATERIALLISTBack
            }
            msg.msgID = HttpEvent.MJ_HALL_ENTITY_GOODS
            appInstance.httpAgent().sendPost(msg)

        },


        FUKAMATERIALLISTBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame FUKAMATERIALLISTBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.FUKA_MATERIAL_LIST, msg)

        },

        FUKAMATERIALOGReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_GOODS_LOG]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_GOODS_LOG] = this.FUKAMATERIALOGReqBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_GOODS_LOG
            appInstance.httpAgent().sendPost(msg)

        },


        FUKAMATERIALOGReqBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame FUKAMATERIALOGReqBack error happen')
                return
            }
            appInstance.sendNotification(GameEvent.FUKA_MATERIA_LOG, msg)

        },

        FUKAMATERIAEXCHANGEReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_ONLINE_EXCHANGE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_ONLINE_EXCHANGE] = this.FUKAMATERIAEXCHANGEBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_ONLINE_EXCHANGE
            appInstance.httpAgent().sendPost(msg)

        },


        FUKAMATERIAEXCHANGEBack: function (msg) {

            appInstance.sendNotification(GameEvent.FUKA_MATERIA_EXCHANGE, msg)

        },


        FEEDBACKGETLISTReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_FEEDBACK]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_FEEDBACK] = this.FEEDBACKGETLISTBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_FEEDBACK
            appInstance.httpAgent().sendPost(msg)

        },


        FEEDBACKGETLISTBack: function (msg) {
            if (msg.status !== 0) {
                appInstance.gameAgent().Tips('未知异常，请重试！')
                return
            }
            appInstance.sendNotification(GameEvent.HALL_FEEDBACK_LIST, msg)

        },


        FEEDBACKSUBMITReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_FEEDBACK_SUBMIT]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_FEEDBACK_SUBMIT] = this.FEEDBACKSUBMITBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_FEEDBACK_SUBMIT
            appInstance.httpAgent().sendPost(msg)

        },


        FEEDBACKSUBMITBack: function (msg) {
            if (msg.status !== 0) {
                appInstance.gameAgent().Tips('未知异常，请重试！')
                return
            }
            appInstance.sendNotification(GameEvent.HALL_FEEDBACK_SUBMIT, msg)

        },


        FEEDBACKGETINFOReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_FEEDBACK_INFO]) {
                this._requestBackCall[HttpEvent.MJ_HALL_PLAYER_FEEDBACK_INFO] = this.FEEDBACKGETINFOBack
            }
            msg.msgID = HttpEvent.MJ_HALL_PLAYER_FEEDBACK_INFO
            appInstance.httpAgent().sendPost(msg)

        },


        FEEDBACKGETINFOBack: function (msg) {
            if (msg.status !== 0) {
                appInstance.gameAgent().Tips('未知异常，请重试！')
                return
            }
            appInstance.sendNotification(GameEvent.HALL_FEEDBACK_INFO, msg)

        },

        /**
         * 邮件信息请求
         * @param msg
         */
        emailInfoReq: function (msg) {
            console.log('---msg1111'+JSON.stringify(msg));
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_MAIL_LIST]) {
                this._requestBackCall[HttpEvent.MJ_HALL_MAIL_LIST] = this.emailInfoBack
            }

            msg.msgID = HttpEvent.MJ_HALL_MAIL_LIST
            appInstance.httpAgent().sendPost(msg)

        },

        /**
         * 邮件信息返回
         * @param msg
         */
        emailInfoBack: function (msg) {
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame SIGNINDATASBack error happen')
                return
            }

            appInstance.sendNotification(GameEvent.HALL_EMAIL_INFO, msg)

        },

        /**
         * 删除邮件请求
         * @param msg
         */
        emailDelReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_MAIL_DEL]) {
                this._requestBackCall[HttpEvent.MJ_HALL_MAIL_DEL] = this.emailDelBack
            }

            msg.msgID = HttpEvent.MJ_HALL_MAIL_DEL
            appInstance.httpAgent().sendPost(msg)

        },

        /**
         * 删除邮件返回
         * @param msg
         */
        emailDelBack: function (msg) {

            if (msg.status !== 0) {
                cc.log('------------->>>httpGame SIGNINDATASBack error happen')
                return
            }

            appInstance.sendNotification(GameEvent.HALL_EMAIL_DEL, msg)

        },

        /**
         * 邮件领取奖励请求
         * @param msg
         */
        emailReceiveReq: function (msg) {
            msg = msg || {}
            if (!this._requestBackCall[HttpEvent.MJ_HALL_MAIL_RECEIVE_DELETE]) {
                this._requestBackCall[HttpEvent.MJ_HALL_MAIL_RECEIVE_DELETE] = this.emailReceiveBack
            }

            msg.msgID = HttpEvent.MJ_HALL_MAIL_RECEIVE_DELETE
            appInstance.httpAgent().sendPost(msg)

        },

        /**
         * 邮件领取奖励返回
         * @param msg
         */
        emailReceiveBack: function (msg) {
            console.log('--------emailReceiveBack'+JSON.stringify(msg));
            if (msg.status !== 0) {
                cc.log('------------->>>httpGame SIGNINDATASBack error happen')
                return
            }

            appInstance.sendNotification(GameEvent.HALL_EMAIL_RECEIVE, msg)

        },
    })
    return HttpGame
})
