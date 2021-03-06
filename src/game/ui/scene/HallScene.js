
/**
 * HallScene
 */
load('game/ui/scene/HallScene', function () {
    let BaseScene = include('public/ui/BaseScene')
    let ResConfig = include('game/config/ResConfig')
    let HallMdt =  include('game/ui/scene/HallMdt')
    let HallScene = BaseScene.extend({
        _className: 'HallScene',
        RES_BINDING: function () {
            return {
                'topPnl/photoBtn': { onClicked: this.onPhotoClick  },
                'topPnl/guiZuBtn': { onClicked: this.onMemberClick },
                'topPnl/namePnl': { onClicked: this.onGonggaoClick  },
                'topPnl/coinPnl': { onClicked: this.onSettingClick },
                'topPnl/coinPnl/coinAddBtn': { onClicked: this.onSettingClick },
                'topPnl/diamondsPnl': { onClicked: this.onSettingClick },
                'topPnl/diamondsPnl/diamondsAddBtn': { onClicked: this.onSettingClick },
                'topPnl/fuKaPnl': { onClicked: this.onSettingClick },
                'topPnl/fuKaPnl/fuKaAddBtn': { onClicked: this.onSettingClick },
                'topPnl/emailBtn': { onClicked: this.onSettingClick },
                'topPnl/moreBtn': { onClicked: this.onMoreClick },


                'morePnl': { onClicked: this.onHideMorePnlClick },
                'morePnl/confBtn': { onClicked: this.onHideMorePnlClick },
                'morePnl/feedbackBtn': { onClicked: this.onHideMorePnlClick },
                'morePnl/customerServiceBtn': { onClicked: this.onHideMorePnlClick },
                'morePnl/authenticationBtn': { onClicked: this.onAuthenticationClick },


                'bmPnl/fuKaShopBtn': { onClicked: this.onFukaShopClick },
                'bmPnl/coinShopBtn': { onClicked: this.onSettingClick },
                'bmPnl/bmListPnl/signPnl/signBtn': { onClicked: this.onSignBtnClick },
                'bmPnl/bmListPnl/taskPnl': { onClicked: this.onTaskClick },
                'bmPnl/bmListPnl/rolesPnl/rolesBtn': { onClicked: this.onRoleClick },
                'bmPnl/startQuickPnl/startQuickBtn': { onClicked: this.onSettingClick },


                'leftPnl/invitationPnl/invitationBtn': { onClicked: this.onInvitationClick },
                'leftPnl/turnTablePnl/turnTableBtn': { onClicked: this.onTurnTableClick },
                'leftPnl/cashCowPnl/cashCowBtn': { onClicked: this.onCashCowClick },

                'rightPnl/coinGameNd': { },
                'rightPnl/coinGameNd/coinGameBtn': { onClicked: this.onCoinGameClick },
                'rightPnl/liveBroadcastBtn': { onClicked: this.onShareClick },
                'rightPnl/matchBtn': { onClicked: this.onShareClick },
                'rightPnl/changeAreaBtn': { onClicked: this.onShareClick },



                'popUpPnl/goShopPnl/canlBtn': { onClicked: this.onGoShopClick },
                'popUpPnl/goShopPnl/exchangeBtn': { onClicked: this.onGoShopClick },

                'popUpPnl/signPnl/acceptsPnl/closeBtn': { onClicked: this.onGoShopClick },
                'popUpPnl/signPnl/acceptsPnl/determineBtn': { onClicked: this.onGoShopClick },
                'popUpPnl/signPnl/rulePnl/closeBtn': { onClicked: this.onGoShopClick },



            }
        },

        onFukaShopClick: function() {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.FukaShopLayer)
        },

        onPhotoClick: function () {
          //  this.personalDataPnl.setVisible(true)
            appInstance.gameAgent().addPopUI(ResConfig.Ui.PersonalLayer)
        },

        onUpdateNameClick: function () {
            this.updateNamePnl.setVisible(true)
        },

        onHideUpdateNamePnlClick: function () {
            this.updateNamePnl.setVisible(false)
        },

        onEmailClick: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.EmailLayer)
        },
        onInvitationClick: function () {

            appInstance.gameAgent().addPopUI(ResConfig.Ui.InvitationLayer)
        },

        onHideMorePnlClick: function () {
            this.morePnl.setVisible(false)
        },
        onTurnTableClick: function () {
            appInstance.gameAgent().addUI(ResConfig.Ui.TurnTableLayer)
        },

        onTaskClick: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.TaskLayer)
        },
        onCashCowClick: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().cashCowNumReq(msg)

        },

        onGoCashCowLayer : function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.CashCowLayer)
        },

        onSignBtnClick: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.SignLayer)
        },

        onMemberClick: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.MemberLayer)
        },

        onGoShopClick: function () {
            let shopClass = include('game/ui/layer/shop/ShopLayer')
            let shopUI = appInstance.uiManager().createPopUI(shopClass)
            appInstance.sceneManager().getCurScene().addChild(shopUI)
        },
        onCoinGameClick: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.CoinGameLayer)
        },

        onRoleClick: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.RoleLayer)
        },

        onLiveBroadcastClick: function () {
            //跳转直播界面

            appInstance.gameAgent().Tips('想看什么呀？')
           // appInstance.gameAgent().tcpGame().enterTable()
        },
        onMatchClick: function () {
            let matchClass = include('game/ui/layer/match/MatchLayer')
            let matchUI = appInstance.uiManager().createPopUI(matchClass)
            appInstance.sceneManager().getCurScene().addChild(matchUI)
        },
        onGonggaoClick: function () {

        },
        onSettingClick: function () {
            let settingClass = include('game/ui/layer/setting/SettingLayer')
            let settingUI = appInstance.uiManager().createPopUI(settingClass)
            appInstance.sceneManager().getCurScene().addChild(settingUI)
        },
        onShareClick: function () {

        },

        onMoreClick: function () {
            let isAuthentication = appInstance.dataManager().getUserData().isAuthentication
            if(isAuthentication != 0){
                this.authenticationBtn.setVisible(true)
            }else{
                this.authenticationBtn.setVisible(false)
            }
            this.morePnl.setVisible(true);
        },

        onChangeAreaClick: function () {
           // 跳转到选择城市界面
        },

        onStartQuickClick: function () {
            //跳转到匹配界面
        },

        onConfBtnClick: function () {
            let confClass = include('game/ui/layer/config/ConfLayer')
            let confUI = appInstance.uiManager().createPopUI(confClass)
            appInstance.sceneManager().getCurScene().addChild(confUI)
            this.onHideMorePnlClick()
        },
        onFeedbackClick: function () {
            let feedbackClass = include('game/ui/layer/feedback/FeedbackLayer')
            let feedbackUI = appInstance.uiManager().createPopUI(feedbackClass)
            appInstance.sceneManager().getCurScene().addChild(feedbackUI)
            this.onHideMorePnlClick()
        },

        onAuthenticationClick: function () {

            this.onHideMorePnlClick()
            appInstance.gameAgent().addPopUI(ResConfig.Ui.Authentication)

        },

        onCustomerServiceClick: function () {
          //一个弹框
            this.onHideMorePnlClick()
        },

        ctor: function () {
            this._super(ResConfig.View.HallScene)
            this.registerMediator(new HallMdt(this))
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

            this.onInitUserData();

        },

        initView: function () {
            let jinbichangAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.DatingJinbichang)
            jinbichangAni.setAnimation(0, 'animation', true)
            this.coinGameNd.addChild(jinbichangAni)
        },

        showView: function () {

        },

        onInitUserData: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().userDataReq(msg)

        },

       onUpdateUserData: function (data) {
          //  cc.log('-----------------onUpdateUserData :' + JSON.stringify(data))
           let nameNd =  this.namePnl.getChildByName('name')
           let coinsCnt =  this.coinPnl.getChildByName('coinsCnt')
           let diamondsCnt =  this.diamondsPnl.getChildByName('diamondsCnt')
           let fuKaCnt =  this.fuKaPnl.getChildByName('fuKaCnt')

           if(data.hasOwnProperty('pname')){
               nameNd.setString(data.pname)
           }

           if(data.hasOwnProperty('coin')){
               coinsCnt.setString(data.coin)
           }

           if(data.hasOwnProperty('diamonds')){
               diamondsCnt.setString(data.diamonds)
           }

           if(data.hasOwnProperty('fuKa')){
               fuKaCnt.setString(data.fuKa)
           }

       }



    })

    return HallScene
})
