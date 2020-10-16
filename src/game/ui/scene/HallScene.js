/**
 * HallScene
 */
load('game/ui/scene/HallScene', function () {
    let BaseScene = include('public/ui/BaseScene')
    let ResConfig = include('game/config/ResConfig')
    let HallMdt = include('game/ui/scene/HallMdt')
    let LocalSave = include('game/public/LocalSave')
    let GameUtil = include('game/public/GameUtil')
    let AniPlayer = ResConfig.AniPlayer
    let PlayerPlay = ResConfig.PlayerPlay
    let HallScene = BaseScene.extend({
        _className: 'HallScene',
        RES_BINDING: function () {
            return {
                'topPnl/photoBtn': {onClicked: this.onPhotoClick},
                'topPnl/guiZuBtn': {onClicked: this.onMemberClick},
                'topPnl/namePnl': {onClicked: this.onGonggaoClick},
                'topPnl/coinPnl': {},
                'topPnl/coinPnl/coinAddBtn': {onClicked: this.onCoinShopClick},
                'topPnl/diamondsPnl': {},
                'topPnl/diamondsPnl/diamondsAddBtn': {onClicked: this.onCoinShopClick},
                'topPnl/fuKaPnl': {},
                'topPnl/fuKaPnl/fuKaAddBtn': {onClicked: this.onCoinShopClick},
                'topPnl/emailBtn': {onClicked: this.onEmailBtnClick},
                'topPnl/moreBtn': {onClicked: this.onMoreClick},
                'topPnl/morePnl': {onClicked: this.onHideMorePnlClick},
                'topPnl/morePnl/setBtn': {onClicked: this.onSetBtnClick},
                'topPnl/morePnl/feedbackBtn': {onClicked: this.onFeedbackBtnClick},
                'topPnl/morePnl/customerServiceBtn': {onClicked: this.onHideMorePnlClick},
                'topPnl/morePnl/authenticationBtn': {onClicked: this.onAuthenticationClick},


                'bmPnl/fuKaShopBtn': {onClicked: this.onFukaShopClick},
                'bmPnl/coinShopBtn': {onClicked: this.onCoinShopClick},
                'bmPnl/bmListPnl/signPnl': {onClicked: this.onSignBtnClick},
                'bmPnl/bmListPnl/taskPnl': {onClicked: this.onTaskClick},
                'bmPnl/bmListPnl/rolesPnl': {onClicked: this.onRoleClick},
                'bmPnl/startQuickPnl/startQuickBtn': {onClicked: this.onStartQuickBtnClick},


                'leftPnl/invitationPnl': {},
                'leftPnl/invitationPnl/invitationBtn': {onClicked: this.onInvitationClick},
                'leftPnl/turnTablePnl/turnTableNd': {},
                'leftPnl/turnTablePnl/turnTableNd/turnTableBtn': {onClicked: this.onTurnTableClick},
                'leftPnl/cashCowPnl/cashCowNd': {},
                'leftPnl/cashCowPnl/cashCowNd/cashCowBtn': {onClicked: this.onCashCowClick},

                'rightPnl/aniNd': {},
                'rightPnl/coinGameNd': {},
                'rightPnl/coinGameNd/coinGameBtn': {onClicked: this.onCoinGameClick},
                'rightPnl/liveBroadcastNd': {},
                'rightPnl/liveBroadcastNd/liveBroadcastBtn': {onClicked: this.onLiveBroadcastClick},
                'rightPnl/matchNd': {},
                'rightPnl/matchNd/matchBtn': {onClicked: this.onGoAreanClick},
                'rightPnl/changeAreaNd': {},
                'rightPnl/changeAreaNd/changeAreaBtn': {onClicked: this.goChooseCity},


                'popUpPnl/goShopPnl/canlBtn': {onClicked: this.onGoShopClick},
                'popUpPnl/goShopPnl/exchangeBtn': {onClicked: this.onGoShopClick},

                'popUpPnl/signPnl/acceptsPnl/closeBtn': {onClicked: this.onGoShopClick},
                'popUpPnl/signPnl/acceptsPnl/determineBtn': {onClicked: this.onGoShopClick},
                'popUpPnl/signPnl/rulePnl/closeBtn': {onClicked: this.onGoShopClick},


            }
        },

        onEmailBtnClick: function () {
            this.emailBtn.getChildByName('redImg').setVisible(false)
            appInstance.gameAgent().Tips('敬请期待！！！')
        },

        onFukaShopClick: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.FukaShopLayer)
        },

        onGoAreanClick: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.ArenaLayer)
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

            this.invitationPnl.getChildByName('redImg').setVisible(false)
            appInstance.gameAgent().addUI(ResConfig.Ui.InvitationLayer)
        },

        onHideMorePnlClick: function () {
            this.morePnl.setVisible(false)
        },
        onTurnTableClick: function () {
            appInstance.gameAgent().addUI(ResConfig.Ui.TurnTableLayer)
        },

        onFeedbackBtnClick: function () {
            this.morePnl.setVisible(false)
            appInstance.gameAgent().addPopUI(ResConfig.Ui.FeedBackLayer)
        },

        onTaskClick: function () {
            this.taskPnl.getChildByName('redImg').setVisible(false)
            appInstance.gameAgent().addPopUI(ResConfig.Ui.TaskLayer)
        },
        onCashCowClick: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().cashCowNumReq(msg)

        },

        onGoCashCowLayer: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.CashCowLayer)
        },

        onSignBtnClick: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.SignLayer)
        },

        onMemberClick: function () {
            this.guiZuBtn.getChildByName('redImg').setVisible(false)
            appInstance.gameAgent().addPopUI(ResConfig.Ui.MemberLayer)
        },

        onCoinShopClick: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.CoinShopLayer)
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
            appInstance.nativeApi().jumpToBlindDate()

        },
        onMatchClick: function () {
            let matchClass = include('game/ui/layer/match/MatchLayer')
            let matchUI = appInstance.uiManager().createPopUI(matchClass)
            appInstance.sceneManager().getCurScene().addChild(matchUI)
        },
        onGonggaoClick: function () {

        },

        onSetBtnClick: function () {
            this.onHideMorePnlClick()
            appInstance.gameAgent().addPopUI(ResConfig.Ui.SetLayer)
        },

        goChooseCity: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.ChooseCityLayer)
        },

        onMoreClick: function () {
            let isAuthentication = appInstance.dataManager().getUserData().isAuthentication
            if (isAuthentication != 0) {
                this.authenticationBtn.setVisible(true)
            } else {
                this.authenticationBtn.setVisible(false)
            }
            this.morePnl.setVisible(true);
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
            if (appInstance.dataManager().getUserData().MatchJinjiGaming) {
                this.showMatchJinjiLayer(appInstance.dataManager().getUserData().MatchJinjiGaming)
                appInstance.dataManager().getUserData().MatchJinjiGaming = null
            }
        },

        onExit: function () {
            this._super()
        },

        initData: function (selfInfo) {
            this._selfInfo = selfInfo
            this._pRole = selfInfo.pRole

            this._peopleNum = global.localStorage.getIntKey(LocalSave.CoinGamePeopleNum)
            if (!this._peopleNum) {
                this._peopleNum = 2
            }

            this.onInitUserData();

        },



        initView: function (selfInfo) {
            this.initData(selfInfo)

            let jinbichangAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.DatingJinbichang)
            jinbichangAni.setAnimation(0, 'animation', true)
            this.coinGameNd.addChild(jinbichangAni)

            let xiangqinAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.DatingXiangQin)
            xiangqinAni.setAnimation(0, 'animation', true)
            this.liveBroadcastNd.addChild(xiangqinAni)

            let saishichangAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.DatingSaiShiChang)
            saishichangAni.setAnimation(0, 'animation', true)
            this.matchNd.addChild(saishichangAni)

            let gengduowanfaAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.DatingGengDuoWanFa)
            gengduowanfaAni.setAnimation(0, 'animation', true)
            this.changeAreaNd.addChild(gengduowanfaAni)

            let yaojinshuAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.DatingYaoJinShu)
            yaojinshuAni.setAnimation(0, 'animation', true)
            this.cashCowNd.addChild(yaojinshuAni)

            let zhuanpanAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.DatingZhuanPan)
            zhuanpanAni.setAnimation(0, 'animation', true)
            this.turnTableNd.addChild(zhuanpanAni)

            this.updatePlayerAni()
            this.morePnl.setVisible(false)

        },

        updatePlayerAni: function (pRole) {

            pRole = pRole || this._pRole

            if (pRole !=0 && !pRole) {
                return
            }

            this.aniNd.removeAllChildren()
            let ani = appInstance.gameAgent().gameUtil().getAni(AniPlayer[pRole])
            this.aniNd.addChild(ani)
            ani.setPosition(cc.p(0,0))
            ani.setScale(0.6)
            ani.setAnimation(0, PlayerPlay.stand, true)
        },
        

        onInitUserData: function () {
            let msg = {}
            appInstance.gameAgent().httpGame().userDataReq(msg)

        },

        loadUrlImage: function (url, cell) {
            let size = cell.getContentSize();
            cell.retain()
            cc.loader.loadImg(url, null, function (err, img) {
                if ( !err && img) {
                    var logo = new cc.Sprite(img);
                    logo.setContentSize(size)
                    logo.setPosition(cc.p(size.width / 2, size.height / 2))
                    cell.addChild(logo);
                    cell.release()
                }
            }.bind(this));
        },
        showMatchJinjiLayer: function (msg) {
            if (!this.MatchJinjiLayer) {
                let MatchJinjiLayer = include('module/mahjong/ui/MatchJinjiLayer')
                this.MatchJinjiLayer = appInstance.uiManager().createUI(MatchJinjiLayer)
                this.addChild(this.MatchJinjiLayer)
            }
            this.MatchJinjiLayer.updateView(msg)
        },
        onUpdateUserData: function (data) {
            let nameNd = this.namePnl.getChildByName('name')
            let coinsCnt = this.coinPnl.getChildByName('coinsCnt')
            let diamondsCnt = this.diamondsPnl.getChildByName('diamondsCnt')
            let fuKaCnt = this.fuKaPnl.getChildByName('fuKaCnt')
            let photo = this.photoBtn;

            if (data.hasOwnProperty('pname')) {
                nameNd.setString(data.pname)
            }

            if (data.hasOwnProperty('coin')) {
                coinsCnt.setString(GameUtil.getStringRule(data.coin))
            }

            if (data.hasOwnProperty('diamonds')) {
                diamondsCnt.setString(GameUtil.getStringRule(data.diamonds))
            }

            if (data.hasOwnProperty('fuKa')) {
                fuKaCnt.setString(GameUtil.getStringRule(data.fuKa))
            }
            if (data.hasOwnProperty('sdkphotourl')) {
                this.loadUrlImage(data.sdkphotourl, photo)
            }

            this.updatePlayerAni(data.pRole)

        },

        onStartQuickBtnClick: function () {
            let goMsg = {}
            if (this._peopleNum === 2) {
                goMsg.roomMode = 2
                goMsg.roomId = 'R2'
            } else if (this._peopleNum === 4) {
                goMsg.roomMode = 1
                goMsg.roomId = 'R1'
            }
            goMsg.gameType = 'M5'
            goMsg.pExtend = 'gameHall'
            appInstance.gameAgent().tcpGame().enterTable(goMsg)
            global.localStorage.setIntKey(LocalSave.CoinGamePeopleNum, this._peopleNum)
        },

        onUpdateHallRedStatus: function (data) {

            if(data.hasOwnProperty('mailFlag'))
                this.emailBtn.getChildByName('redImg').setVisible(true)
            else
                this.emailBtn.getChildByName('redImg').setVisible(false)

            if(data.hasOwnProperty('vipDailyFlag'))
                this.guiZuBtn.getChildByName('redImg').setVisible(true)
            else
                this.guiZuBtn.getChildByName('redImg').setVisible(false)

            if(data.hasOwnProperty('invitationFlag'))
                this.invitationPnl.getChildByName('redImg').setVisible(true)
            else
                this.invitationPnl.getChildByName('redImg').setVisible(false)

            if(data.hasOwnProperty('taskFlag'))
                this.taskPnl.getChildByName('redImg').setVisible(true)
            else
                this.taskPnl.getChildByName('redImg').setVisible(false)


        },


    })

    return HallScene
})
