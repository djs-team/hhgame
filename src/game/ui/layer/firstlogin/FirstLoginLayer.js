
load('game/ui/layer/firstlogin/FirstLoginLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let GameConfig = include('game/config/GameConfig')
    let propsRes = GameConfig.propsRes
    let Layer = BaseLayer.extend({
        _className: 'FirstLoginLayer',
        RES_BINDING: function () {
            return {
                'BgPnl/Goods0': {},
                'BgPnl/Goods1': {},
                'BgPnl/SureBtn': { onClicked: this.onSureBtnClick }
            }
        },
        ctor: function (reward) {
            this._super(ResConfig.View.FirstLoginLayer)
            this.initData(reward)
            this.initView()
        },

        initData: function (reward) {
            this._reward = reward
        },

        initView: function () {
            //先只弄两个
            if (this._reward.length < 2) {
                appInstance.uiManager().removeUI(this)
                return
            }
            for (let i = 0; i < 2; ++i) {
                this['Goods' + i].getChildByName('GoodsIcon').loadTexture(propsRes[this._reward[i].propType].propCode[this._reward[i].propCode].currency)
                this['Goods' + i].getChildByName('GoodsNum').setString('X' +this._reward[i].propNum)
            }

        },

        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        },
        onSureBtnClick: function () {
            appInstance.uiManager().removeUI(this)
        }
    })
    return Layer
})
