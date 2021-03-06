
/**
 * LoginScene
 */
load('game/ui/scene/LoginScene', function () {
    let BaseScene = include('public/ui/BaseScene')
    let ResConfig = include('game/config/ResConfig')
    let LoginMdt =  include('game/ui/scene/LoginMdt')
    let HallScene = include('game/ui/scene/HallScene')
    let LoginScene = BaseScene.extend({
        _className: 'LoginScene',
        RES_BINDING: function () {
            return {
                'pnl/phoneLogin': { onClicked: this.onphoneLoginClick },
                'pnl/wxLogin': { onClicked: this.onwxLoginClick },
                'pnl/agreeBtn': { onClicked: this.onagreeBtnClick },
            }
        },

        ctor: function () {
            this._super(ResConfig.View.LoginScene)
            this.registerMediator(new LoginMdt(this))

            this.registerEventListener('GET_PHOTO_FROM_ALBUM', this.onPhotoSuccess)
            this.registerEventListener('GET_PHOTO_UPLOADPIC', this.onUpLoadPhotoSuccess)
            this.registerEventListener('PIC_SIZE_WARNING', this.onSizeWarning)

            this.uploadUrl = [
                'http://download.jxlwgame.com/weixin/uploadimg',
                'http://download.jxlwgame.com/weixin/uploadimg1'
            ]
        },

        initView: function () {
            // this.goTest()
        },

        onphoneLoginClick: function () {

            let msg = {}
            msg.platform = 1 // 登陆平台1游客2手机3微信4闲聊5账号登录
            appInstance.gameAgent().httpGame().httpLogin(msg)
        },

        onwxLoginClick: function () {
            // let LocalSave = include('game/public/LocalSave')
            // global.localStorage.setStringForKey(LocalSave.LocalImei, '')
            // let msg = {}
            // msg.platform = 1 // 登陆平台1游客2手机3微信4闲聊5账号登录
            // appInstance.gameAgent().httpGame().httpLogin(msg)

            appInstance.sceneManager().replaceScene(new HallScene())
            // this.goTest()
        },

        doPhoto: function () {
            appInstance.nativeApi().getPictureFromPhoneAlbum('www.baidu.com', 'abc')
        },

        goTest: function () {
            let MjPlayScene = include('module/mahjong/ui/MjPlayScene')
            appInstance.sceneManager().replaceScene(new MjPlayScene())
        },

        onagreeBtnClick: function () {

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

        },

        showView: function () {

        },

        onLoginClick2: function () {
            cc.log('====================onLoginClick2============')

            let proto = include('game/msghandler/PutCardProto')
            let protoObj = new proto()
            let reData = protoObj.getReData()
            cc.log('=============' + JSON.stringify(reData))

            // appInstance.gameNet().connect()

            // appInstance.gameAgent().httpGame().httpLogin()
        },

        onLoginClick3: function () {
            let Packet = include('public/network/Packet')
            let LoginProto = include('game/msghandler/LoginProto')
            let PacketProto = new Packet(new LoginProto())
            let tb = {
                'gid': 5,
                'key': '这是个啥啊',
                'channel': 10047
            }
            PacketProto.setValue(tb)
            appInstance.gameNet().send(PacketProto)
        }
    })

    return LoginScene
})
