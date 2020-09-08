
load('game/ui/layer/personal/PersonalLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let PersonalMdt = include('game/ui/layer/personal/PersonalMdt')
    let layer = BaseLayer.extend({
        _className: 'PersonalLayer',
        ctor: function () {
            this._super(ResConfig.View.PersonalLayer)

            this.registerMediator(new PersonalMdt(this))
        },
        RES_BINDING: function () {
            return {

                'personalDataPnl/dataBtn': {  },
                'personalDataPnl/personalDataCloseBtn': { onClicked: this.onPersonalDataCloseClick },
                'personalDataPnl/changePicBtn': {  },
                'personalDataPnl/cancellationBtn': {  },
                'personalDataPnl/namePnl': {  },
                'personalDataPnl/namePnl/updateNameBtn': { onClicked: this.onUpdateNameClick },
                'personalDataPnl/idPnl': {  },
                'personalDataPnl/currencyPnl': {  },
                'personalDataPnl/rolePnl': {  },

                'updateNamePnl': {  },
                'updateNamePnl/closeUpdateNameBtn': { onClicked: this.onCloseUpdateNameClick },
                'updateNamePnl/confirmUpdateNameBtn': { onClicked: this.onConfirmUpdateNameClick },
                'updateNamePnl/updataPnl': {  },


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

        onInitUserData: function (data) {

            if(data.hasOwnProperty('pname')){

                this.namePnl.getChildByName('nameText').setString(data.pname)

            }
            if(data.hasOwnProperty('pid')){

                this.idPnl.getChildByName('idVale').setString(data.pid)

            }
            if(data.hasOwnProperty('coin')){

                this.currencyPnl.getChildByName('coinPnl').getChildByName('coinVaule').setString(data.coin)

            }
            if(data.hasOwnProperty('diamonds')){

                this.currencyPnl.getChildByName('diamondPnl').getChildByName('diamondVaule').setString(data.diamonds)

            }

            if(data.hasOwnProperty('fuKa')){

                this.currencyPnl.getChildByName('fuKaPnl').getChildByName('fuKaVaule').setString(data.fuKa)

            }

        },

        onUpdateNameClick: function () {

            let _nameUpdate = appInstance.dataManager().getUserData().nameUpdate
            if(_nameUpdate != 0){
                cc.log('can not updateName!!')
                return
            }

            this.updateNamePnl.setVisible(true)

        },

        onCloseUpdateNameClick: function () {

            this.updateNamePnl.setVisible(false)

        },

        onPersonalDataCloseClick: function () {

            appInstance.uiManager().removeUI(this)

        },

        onCheckedNameLength:  function(str) {

            let strLength = 0;
            for (let i = 0; i < str.length; i++) {
                if (str.charCodeAt(i) > 255) //如果是汉字，则字符串长度加2
                    strLength += 2;
                else
                    strLength++;
            }

            return strLength;

        },

        onConfirmUpdateNameClick: function () {

            let nameNd = this.updataPnl.getChildByName('updateText').getString()
            if(nameNd == null || nameNd.length == 0){
                cc.log("onConfirmUpdateNameClick nameNd is null!")
                return;
            }
            let nameLength = this.onCheckedNameLength(nameNd);
            if(nameLength > 12){
                cc.log("onConfirmUpdateNameClick nameNd is too long!")
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

        }





    })
    return layer
})
