/**
 * LoginScene
 */
load('game/ui/scene/LoginScene', function () {
    let BaseScene = include('public/ui/BaseScene')
    let ResConfig = include('game/config/ResConfig')
    let LoginMdt = include('game/ui/scene/LoginMdt')
    let LocalSave = include('game/public/LocalSave')
    let GameUtil = include('game/public/GameUtil')
    let LoginScene = BaseScene.extend({
        _className: 'LoginScene',
        _canLogin: true,
        RES_BINDING: function () {
            return {
                'pnl/phoneLogin': {onClicked: this.onphoneLoginClick},
                'pnl/wxLogin': {onClicked: this.onwxLoginClick},
                'pnl/agreeBtn': {onClicked: this.onagreeBtnClick},
            }
        },

        ctor: function (data) {
            this._super(ResConfig.View.LoginScene)
            this.registerMediator(new LoginMdt(this))

            this._viewData = data

            this.registerEventListener('GET_PHOTO_FROM_ALBUM', this.onPhotoSuccess)
            this.registerEventListener('GET_PHOTO_UPLOADPIC', this.onUpLoadPhotoSuccess)
            this.registerEventListener('PIC_SIZE_WARNING', this.onSizeWarning)
            this.registerEventListener('THIRD_LOGIN_RESULT', this.onThirdLogin)
            this.registerEventListener('installParam', this.onInstallParam)

            this.uploadUrl = [
                'http://download.jxlwgame.com/weixin/uploadimg',
                'http://download.jxlwgame.com/weixin/uploadimg1'
            ]
        },

        initView: function () {
            if (this._viewData) {
                if (this._viewData.sayTxt) {
                    appInstance.gameAgent().Tips(this._viewData.sayTxt)
                }
            }
            // this.goTest()
        },

        /**
         * 一键登录
         */
        onphoneLoginClick: function () {
            GameUtil.delayBtn(this.phoneLogin);
            if(!this.onCheckeCanLogin())
                return

            //判断当前系统
            if (cc.sys.OS_WINDOWS === cc.sys.os) {
                this.debugLogin()
            } else {
                appInstance.nativeApi().oneClickLogin()
            }
        },

        onCheckeCanLogin: function () {

            let flag = true
            if(!this._canLogin){
                appInstance.gameAgent().Tips('请同意《用户协议》！')
                flag = false
            }
            return flag
        },

        debugLogin: function () {
            let msg = {}
            let imeiStr = global.localStorage.getStringForKey(LocalSave.LocalImei)
            if (!imeiStr) {
                imeiStr = 'windows imei random' + Math.floor(Math.random() * 1000000)
                global.localStorage.setStringForKey(LocalSave.LocalImei, imeiStr)
            }
            msg.imei = imeiStr
            appInstance.gameAgent().httpGame().httpLogin(msg)
        },

        onwxLoginClick: function () {
            GameUtil.delayBtn(this.wxLogin);
            if(!this.onCheckeCanLogin())
                return
            
            if (cc.sys.OS_WINDOWS === cc.sys.os) {
                this.debugLogin()
            } else {
                appInstance.nativeApi().wxLogin()
            }

        },

        doPhoto: function () {
            appInstance.nativeApi().getPictureFromPhoneAlbum('www.baidu.com', 'abc')
        },

        goTest: function () {
            let MjPlayScene = include('module/mahjong/ui/MjPlayScene')
            appInstance.sceneManager().replaceScene(new MjPlayScene())
            // let TurnTableLayer = include('game/ui/layer/turntable/TurnTableLayer')
            // let TurnTableLayerUI = appInstance.uiManager().createPopUI(TurnTableLayer)
            // appInstance.sceneManager().getCurScene().addChild(TurnTableLayerUI)
        },

        onagreeBtnClick: function () {

            if(this._canLogin)
                this._canLogin = false
            else
                this._canLogin = true
        },


        goChooseCity: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.ChooseCityLayer)
        },


        onBtnSendPhoto: function () {
            let pInfo = appInstance.dataManager().getUserData()
            if (pInfo && pInfo.accessToken) {
                appInstance.nativeApi().getPictureFromPhoneAlbum(this.uploadUrl[type], pInfo.accessToken)
            }
        },

        onPhotoSuccess: function (msg) {
            this.photoPath = msg.photoPath
            let pInfo = appInstance.dataManager().getUserData()
            cc.log('=========1111msg.photoPath' + msg.photoPath)
            cc.log('========1111this.uploadUrl[this.sIndex]' + this.uploadUrl[0])
            cc.log('=======111pInfo.accessToken' + pInfo.accessToken)
            appInstance.nativeApi().uploadPic(msg.photoPath, this.uploadUrl[0], 'GET_PHOTO_UPLOADPIC', 'abc')
        },

        onUpLoadPhotoSuccess: function (msg) {
            // utils.View.unblock()
            cc.log('-- getPictureFromPhone -- GET_PHOTO_UPLOADPIC -- d  = ' + JSON.stringify(msg))
            if (cc.sys.OS_ANDROID === cc.sys.os) {
                msg = JSON.parse(msg)
            }
            if (cc.sys.OS_IOS === cc.sys.os) {
                msg = JSON.parse(JSON.stringify(msg))
            }
            if (msg && parseInt(msg.errno) === 0) {
                // utils.showMsg('图片上传成功！')
                this.refreshPhoto(this.sIndex, msg.payimg, this.photoPath)
            } else {
                // utils.showMsg('图片上传失败，请重试！')
            }
        },
        onThirdLogin: function (msg) {
            cc.log('======onThirdLogin=======' + JSON.stringify(msg))
            if (cc.sys.OS_ANDROID === cc.sys.os) {
                msg.imei = appInstance.nativeApi().getImei()
                appInstance.gameAgent().httpGame().httpLogin(msg)
            }
            if (cc.sys.OS_IOS === cc.sys.os) {
                msg = JSON.parse(msg)
                msg.imei = appInstance.nativeApi().getImei()
                appInstance.gameAgent().httpGame().httpLogin(msg)
            }
        },
        onInstallParam: function (msg) {
            cc.log('======onInstallParam=======' + JSON.stringify(msg))
            if (cc.sys.OS_ANDROID === cc.sys.os) {
                cc.sys.localStorage.setItem("installParam", msg);
                let myParam = cc.sys.localStorage.getItem("installParam");
                cc.log('======onInstallParam=======' + JSON.stringify(myParam))


            }
            if (cc.sys.OS_IOS === cc.sys.os) {

            }
        },

        onSizeWarning: function (msg) {
            appInstance.gameAgent().Tips('图片较大！请上传1M以下图片，谢谢')
        },

        onEnter: function () {
            this._super()
            this.initData()
            this.initView()
            this.showView()
        },

        onExit: function () {
            this._super()
        },

        initData: function () {
            appInstance.nativeApi().getInstallParam()

            appInstance.audioManager().playMusic(ResConfig.Sound.bg1, true)
        },

        showView: function () {

        }
    })

    return LoginScene
})
