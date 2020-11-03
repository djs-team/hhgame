/**
 * DeskHeadLayer
 */
load('module/mahjong/ui/ShareLayer', function () {
    let GameResConfig = include('game/config/ResConfig')
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')

    let Layer = BaseLayer.extend({
        _className: 'ShareLayer',
        RES_BINDING: function () {
            return {

                'bg_share': {},
                'shareTopPnl': {},
                'shareTopPnl/share_img_sex': {},
                'shareTopPnl/share_tv_name': {},
                'shareTopPnl/share_img_photo': {},
                'shareTopPnl/cityNamePg/cityName': {},

                'shareLeftPnl': {},

                'shareBmPnl/share_img_qr': {},

            }
        },

        ctor: function (msg) {
            this._super(ResConfig.View.ShareLayer)
            this._msg = msg

            this.registerEventListener('inviteCodeCallback', this.onInviteCodeCallback)
        },



        onInviteFriendBtnClick: function () {




        },
        onInviteCodeCallback: function (msg) {
            this.loadCodePg(this.share_img_qr, msg)
        },
        loadCodePg: function (parent, img) {
            let size = parent.getContentSize()
            let sp = new cc.Sprite(img);
            sp.setContentSize(size)
            sp.setPosition(cc.p(size.width / 2, size.height / 2))
            parent.addChild(sp);
        },
        // 分享接口
        shareImg: function () {

        },

        //屏幕截取后  重置界面
        afterShareImg: function () {
            this.onCloseFunction()
        },

        onCloseFunction: function () {
            appInstance.uiManager().removeUI(this)
        },



        initData: function () {
            let myPid = appInstance.dataManager().getUserData().pid;
            this.inviteUrl = 'https://share.hehefun.cn/index.html?installPid=' + myPid;

            let pname = appInstance.dataManager().getUserData().pname;
            this.share_tv_name.setString(pname)
            //可以做图像的加载
        },

        initView: function () {
            this.initData()

            appInstance.nativeApi().getInvitationCode(this.inviteUrl)

            // 调用前  将自己想要分享的图片 弄全屏 在截取完后 再重置回原来的状态
            appInstance.gameAgent().saveCanvas()//截取的话可能会有个问题，加载头像会有时间延迟，而且，应该不是一个线程串行执行的，可能会出现截屏的时候，头像尚未加载出来
            this.shareImg()
            this.afterShareImg()
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
