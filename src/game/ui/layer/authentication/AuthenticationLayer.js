
load('game/ui/layer/authentication/AuthenticationLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let AuthenticationMdt = include('game/ui/layer/authentication/AuthenticationMdt')
    let authenticationLayer = BaseLayer.extend({
        _className: 'authenticationLayer',
        ctor: function () {
            this._super(ResConfig.View.AuthenticationLayer)
            this.registerMediator(new AuthenticationMdt(this))
        },
        RES_BINDING: function () {
            return {
                'pnl/closeBtn': { onClicked: this.onCloseClick },
                'pnl/determineBtn': { onClicked: this.onDetermineClick },
                'pnl/dataPnl/namePnl/dataPic/nameText': {  },
                'pnl/dataPnl/cardIdPnl/dataPic/cardIdText': {  },
            }
        },
        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        },


        onCloseClick: function () {

            appInstance.uiManager().removeUI(this)
        },
        onDetermineClick: function () {

            cc.log('======onDetermineClick====')
            let msg = {}
            msg.name = this.nameText.getString()
            msg.decumentCard = this.cardIdText.getString()
            appInstance.gameAgent().httpGame().authenticationReq(msg)

        },
    })
    return authenticationLayer
})
