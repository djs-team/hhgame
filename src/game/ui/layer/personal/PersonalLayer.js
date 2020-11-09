load('game/ui/layer/personal/PersonalLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let PersonalMdt = include('game/ui/layer/personal/PersonalMdt')
    let GameUtil = include('game/public/GameUtil')
    let GameEvent = include('game/config/GameEvent')
    let AniPlayer = ResConfig.AniPlayer
    let PlayerPlay = ResConfig.PlayerPlay
    let layer = BaseLayer.extend({
        _className: 'PersonalLayer',
        ctor: function () {
            this._super(ResConfig.View.PersonalLayer)

            this.registerMediator(new PersonalMdt(this))

            this.registerEventListener('PERSONALLAYER_CHANGE_PICTURE', this.onChangePicSuccess)
        },
        RES_BINDING: function () {
            return {

                'personalDataPnl/dataBtn': {},
                'personalDataPnl/photoPic': {},
                'personalDataPnl/personalDataCloseBtn': {onClicked: this.onPersonalDataCloseClick},
                'personalDataPnl/changePicBtn': {onClicked: this.onChangePicBtnClick},
                'personalDataPnl/cancellationBtn': {onClicked: this.onExitBtnClick},
                'personalDataPnl/namePnl': {},
                'personalDataPnl/namePnl/updateNameBtn': {onClicked: this.onUpdateNameClick},
                'personalDataPnl/idPnl': {},
                'personalDataPnl/currencyPnl': {},
                'personalDataPnl/rolePnl': {},
                'personalDataPnl/rolePnl/aniNd': {},

                'updateNamePnl': {},
                'updateNamePnl/closeUpdateNameBtn': {onClicked: this.onCloseUpdateNameClick},
                'updateNamePnl/confirmUpdateNameBtn': {onClicked: this.onConfirmUpdateNameClick},
                'updateNamePnl/updataPnl': {},


            }
        },
        onGoShopClick: function () {

        },
        onCreate: function () {
            this._super()
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

        initView: function () {

        },

        showView: function () {

        },


        loadUrlImage: function (url, cell) {
            let size = cell.getContentSize();
            cc.loader.loadImg(url, null, function (err, img) {

                var logo = new cc.Sprite(img);
                logo.setContentSize(size)
                logo.setPosition(cc.p(size.width / 2, size.height / 2))
                // logo.setRadius(20)
                // logo.setScale9Enabled(true)
                cell.addChild(logo);
            });
        },
        onInitUserData: function (data) {

            if (data.hasOwnProperty('_nameUpdate')) {

                if (data._nameUpdate != 0)
                    this.updateNameBtn.setVisible(false)
                else
                    this.updateNameBtn.setVisible(true)

            }

            if (data.hasOwnProperty('pname')) {

                this.namePnl.getChildByName('nameText').setString(data.pname)

            }

            if (data.hasOwnProperty('pid')) {

                this.idPnl.getChildByName('idVale').setString(data.pid)

            }
            if (data.hasOwnProperty('coin')) {

                this.currencyPnl.getChildByName('coinPnl').getChildByName('coinVaule').setString(GameUtil.getStringRule(data.coin))

            }
            if (data.hasOwnProperty('diamonds')) {

                this.currencyPnl.getChildByName('diamondPnl').getChildByName('diamondVaule').setString(GameUtil.getStringRule(data.diamonds))

            }

            if (data.hasOwnProperty('fuKa')) {

                this.currencyPnl.getChildByName('fuKaPnl').getChildByName('fuKaVaule').setString(GameUtil.getStringRule(data.fuKa))
            }
            if (data.hasOwnProperty('photo')) {

                this.loadUrlImage(data.photo, this.photoPic)
            }

            if (data.hasOwnProperty('pRole')) {

                let pRole = data.pRole
                this.aniNd.removeAllChildren()
                let ani = appInstance.gameAgent().gameUtil().getAni(AniPlayer[pRole])
                this.aniNd.addChild(ani)
                ani.setPosition(cc.p(0, 0))
                ani.setScale(0.4)
                ani.setAnimation(0, PlayerPlay.stand, true)
            }

        },

        onChangePicBtnClick: function () {
            let msg = {}
            appInstance.gameAgent().httpGame().getUpDatePictureTokenReq(msg)
        },
        selectHeadCallback: function (url) {

        },

        onChangePicSuccess: function (msg) {
            //更新头像
            let msgPhoto = {}
            msgPhoto.photoUrl = msg;
            appInstance.gameAgent().httpGame().updateUserPhotoReq(msgPhoto)
        },

        onUpdateNameClick: function () {
            this.updateNamePnl.setVisible(true)
        },

        onCloseUpdateNameClick: function () {
            this.updateNamePnl.setVisible(false)

        },

        onPersonalDataCloseClick: function () {
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)

        },

        onCheckedNameLength: function (str) {

            let strLength = 0;
            for (let i = 0; i < str.length; i++) {
                if (str.charCodeAt(i) > 255) //如果是汉字，则字符串长度加2
                    strLength += 2;
                else
                    strLength++;
            }

            return strLength;

        },

        onConfirmUpdateNameClick: function (sender) {
            GameUtil.delayBtn(sender);
            let nameNd = this.updataPnl.getChildByName('updateText').getString()
            if (nameNd == null || nameNd.length == 0) {
                appInstance.gameAgent().Tips('名称不可为空！')
                return;
            }
            let nameLength = this.onCheckedNameLength(nameNd)
            if (nameLength > 12) {
                appInstance.gameAgent().Tips('您的名称太长，最长不可超过6位汉字！')
                return;
            }

            let msg = {}
            msg.nickname = nameNd
            msg.type = 1
            appInstance.gameAgent().httpGame().updateUserNameReq(msg)

        },

        updateUserName: function (data) {

            this.onInitUserData(data)
            this.onCloseUpdateNameClick()

        },

        updateUserPicture: function (data) {

            this.onInitUserData(data)

        },

        onExitBtnClick: function (sender) {
            GameUtil.delayBtn(sender);
            appInstance.gameAgent().goLoginScene()
        },


    })
    return layer
})
