/**
 * DeskHeadLayer
 */
load('module/mahjong/ui/ShareLayer', function () {
    let GameResConfig = include('game/config/ResConfig')
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let GameUtil = include('game/public/GameUtil')

    let Layer = BaseLayer.extend({
        _className: 'ShareLayer',
        RES_BINDING: function () {
            return {

                'bg_share': {},
                'shareTopPnl': {},
                'shareLeftPnl': {},
                'shareRightPnl': {},
                'shareBmPnl': {},
                'shareBmPnl/share_img_qr': {},
                'shareClickLayout': {},
                'shareClickLayout/share_qr2': {},
                'shareClickLayout/share_channel_wx': {onClicked: this.onWxShareClick},
                'shareClickLayout/share_channel_friend': {onClicked: this.onWxCircleShareClick},
                'shareClickLayout/BackBtn': {onClicked: this.onCloseFunction},

            }
        },

        ctor: function (msg) {
            this._super(ResConfig.View.ShareLayer)
            this._msg = msg

            this.registerEventListener('inviteCodeCallback', this.onInviteCodeCallback)

        },

        onWxShareClick: function (sender) {
            GameUtil.delayBtn(sender);
            let fileName = "result_share.jpg";
            appInstance.nativeApi().shareImage('WEIXIN', fileName)
        },

        onWxCircleShareClick: function (sender) {
            GameUtil.delayBtn(sender);
            let fileName = "result_share.jpg";
            appInstance.nativeApi().shareImage('WEIXIN_CIRCLE', fileName)
        },
        onInviteCodeCallback: function (msg) {
            this.loadCodePg(this.share_qr2, msg)
            this.loadCodePg(this.share_img_qr, msg)
            this.beforeShareImg()
            appInstance.gameAgent().saveCanvas()
            this.afterShareImg()
        },
        loadCodePg: function (parent, img) {
            let size = parent.getContentSize()
            let sp = new cc.Sprite(img);
            sp.setContentSize(size)
            sp.setPosition(cc.p(size.width / 2, size.height / 2))
            parent.addChild(sp);
        },
        beforeShareImg: function () {
            this.bg_share.setVisible(true)
            this.shareTopPnl.setVisible(true)
            this.shareLeftPnl.setVisible(true)
            this.shareRightPnl.setVisible(true)
            this.shareBmPnl.setVisible(true)
            this.shareClickLayout.setVisible(false)
        },
        //屏幕截取后  重置界面
        afterShareImg: function () {
            this.bg_share.setVisible(false)
            this.shareTopPnl.setVisible(false)
            this.shareLeftPnl.setVisible(false)
            this.shareRightPnl.setVisible(false)
            this.shareBmPnl.setVisible(false)
            this.shareClickLayout.setVisible(true)        },

        onCloseFunction: function () {
            appInstance.uiManager().removeUI(this)
        },


        initData: function () {
            let myPid = appInstance.dataManager().getUserData().pid;
            this.inviteUrl = 'https://share.hehefun.cn/index.html?installPid=' + myPid;
        },

        initView: function () {
            cc.log('---------------------22222222222222222222222')
            this.bg_share.setVisible(false)
            this.shareTopPnl.setVisible(false)
            this.shareLeftPnl.setVisible(false)
            this.shareRightPnl.setVisible(false)
            this.shareBmPnl.setVisible(false)
            this.shareClickLayout.setVisible(true)
            this.initData()

            cc.log('---------------------11111111111111111111111111')
            appInstance.nativeApi().getInvitationCode(this.inviteUrl)

            // 调用前  将自己想要分享的图片 弄全屏 在截取完后 再重置回原来的状态

        },

        onEnter: function () {
            this._super()
            this.initView()
        },
        onExit: function () {
            this._super()
        }
    })
    return Layer
})
