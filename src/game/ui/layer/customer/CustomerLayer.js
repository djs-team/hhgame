load('game/ui/layer/customer/CustomerLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let GameEvent = include('game/config/GameEvent')
    let CustomerLayer = BaseLayer.extend({
        _className: 'CustomerLayer',
        ctor: function () {
            this._super(ResConfig.View.CustomerLayer)
        },
        RES_BINDING: function () {
            return {
                'customerPnl/closeBtn': { onClicked: this.onClose },
                'customerPnl/gzhPnl/gzhContent': { },
                'customerPnl/gzhPnl/gzhBtn': { onClicked: this.onGzhBtnClick },
                'customerPnl/zxkfPnl/zxkfContent': { },
                'customerPnl/zxkfPnl/zxkfBtn': { onClicked: this.onZxkfBtnClick },
            }
        },
        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
        },
        onGzhBtnClick: function () {
            let string = this.gzhContent.getString()
            appInstance.nativeApi().copy(string)
            appInstance.gameAgent().Tips('复制成功')
        },
        onZxkfBtnClick: function () {
            let string = this.zxkfContent.getString()
            appInstance.nativeApi().copy(string)
            appInstance.gameAgent().Tips('复制成功')
        },
        onExit: function () {
            this._super()
        },
        onClose: function () {
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)
        }
    })
    return CustomerLayer
})
