
/**
 * 共用弹出提示窗
 */
load('game/ui/public/DialogLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let DialogLayerMdt = include('game/ui/public/DialogLayerMdt')
    let DialogLayer = BaseLayer.extend({
        _viewNum: 1,
        _className: 'SystemTips',
        RES_BINDING: function () {
            return {
                'dialogPnl_1': {  },
            }
        },
        ctor: function (data) {
            this._super(ResConfig.View.DialogLayer)
            this.registerMediator(new DialogLayerMdt(this))
            this.initData(data)
            this.initView()
        },

        initData: function (data) {
            this._hidename = data.HideName || ''
            this._viewtype = data.ViewType
            this._tilename = data.TileName
            this._closebtn = data.CloseBtn
            this._saytext = data.SayText
            this._leftbtnname = data.LeftBtnName
            this._midbtnname = data.MidBtnName
            this._rightbtnname = data.RightBtnName
            this._leftbtnclick = data.LeftBtnClick
            this._midbtnclick = data.MidBtnClick
            this._rightbtnclick = data.RightBtnClick
        },

        hideView: function () {
            appInstance.uiManager().removeUI(this)
        },

        hideByName: function (msg) {
            if (msg.HideName === this._hidename) {
                appInstance.uiManager().removeUI(this)
            }
        },

        initView: function () {
            for (let i = 1; i < this._viewNum; ++i) {
                this['dialogPnl_' + i].setVisible(false)
            }
            let pnl = this['dialogPnl_' + this._viewtype]
            pnl.setVisible(true)
            let tileBg = pnl.getChildByName('TileBg')
            let sayText = pnl.getChildByName('SayText')
            let closeBtn = pnl.getChildByName('CloseBtn')
            let leftBtn = pnl.getChildByName('LeftBtn')
            let midBtn = pnl.getChildByName('MidBtn')
            let rightBtn = pnl.getChildByName('RightBtn')

            if (this._tilename) {
                tileBg.setVisible(true)
                tileBg.getChildByName('name').setString(this._tilename)
            } else {
                tileBg.setVisible(false)
            }

            sayText.setString(this._saytext)

            if (this._closebtn) {
                closeBtn.setVisible(true)
                closeBtn.addClickEventListener(function(sender, et) {
                    if (typeof this._closebtn === 'function') {
                        this._closebtn()
                    }
                    appInstance.uiManager().removeUI(this)
                }.bind(this))

            } else {
                closeBtn.setVisible(false)
            }

            if (this._leftbtnname) {
                leftBtn.setVisible(true)
                leftBtn.getChildByName('name').setString(this._leftbtnname)
                leftBtn.addClickEventListener(function(sender, et) {
                    if (this._leftbtnclick) {
                        this._leftbtnclick()
                    }
                    appInstance.uiManager().removeUI(this)
                }.bind(this))

            } else {
                leftBtn.setVisible(false)
            }

            if (this._midbtnname) {
                midBtn.setVisible(true)
                midBtn.getChildByName('name').setString(this._midbtnname)
                midBtn.addClickEventListener(function(sender, et) {
                    if (this._midbtnclick) {
                        this._midbtnclick()
                    }
                    appInstance.uiManager().removeUI(this)
                }.bind(this))

            } else {
                midBtn.setVisible(false)
            }

            if (this._rightbtnname) {
                rightBtn.setVisible(true)
                rightBtn.getChildByName('name').setString(this._rightbtnname)
                rightBtn.addClickEventListener(function(sender, et) {
                    if (this._rightbtnclick) {
                        this._rightbtnclick()
                    }
                    appInstance.uiManager().removeUI(this)
                }.bind(this))

            } else {
                rightBtn.setVisible(false)
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
        }
    })
    return DialogLayer
})
