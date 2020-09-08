
load('game/ui/layer/setting/SettingLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let settingLayer = BaseLayer.extend({
        _className: 'settingLayer',
        ctor: function () {
            this._super(ResConfig.View.SettingLayer)
        },
        RES_BINDING: function () {
            return {
                'pnl/closeBtn': { onClicked: this.onClose }
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
        onClose: function () {
            appInstance.uiManager().removeUI(this)
        }
    })
    return settingLayer
})
