
load('game/ui/layer/set/SetLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let GameEvent = include('game/config/GameEvent')
    let LocalSave = include('game/public/LocalSave')
    let GameUtil = include('game/public/GameUtil')
    let layer = BaseLayer.extend({
        _className: 'SetLayer',
        ctor: function (data) {
            this._super(ResConfig.View.SetLayer)
            this._data = data
        },
        RES_BINDING: function () {
            return {
                'pnl/CloseBtn': { onClicked: this.onCloseClick },
                'pnl/ExitBtn': { onClicked: this.onExitBtnClick },
                'pnl/Node1/MusicSlider': { },
                'pnl/Node1/MusicBtn': { onClicked: this.onMusicBtnClick },
                'pnl/Node2/EffectSlider': { },
                'pnl/Node2/EffectBtn': { onClicked: this.onEffectBtnClick },
                'pnl/Node3/EmojBtn': { onClicked: this.onEmojBtnClick },
                'pnl/Node3/LaguageBtn': { onClicked: this.onLaguageBtnClick },
            }
        },

        initView: function () {
            let musicNum = appInstance.audioManager().getMusicVolume()
            let effectNum = appInstance.audioManager().getEffectVolume()
            this.MusicSlider.setPercent(musicNum * 100)
            this.EffectSlider.setPercent(effectNum* 100)

            if (musicNum === 0) {
                this.MusicBtn.setSelected(true)
            } else {
                this.MusicBtn.setSelected(false)
            }

            if (effectNum === 0) {
                this.EffectBtn.setSelected(true)
            } else {
                this.EffectBtn.setSelected(false)
            }

            this.MusicSlider.addEventListener(this.MusicSliderTouch, this)
            this.EffectSlider.addEventListener(this.EffectSliderTouch, this)

            if (this._data ) {
                if (this._data.isDesk) {
                    this.ExitBtn.setVisible(false)
                }
            }

            let localLanguage = appInstance.gameAgent().gameUtil().getLocalLanguage()
            if (localLanguage === 'putong') {
                this.LaguageBtn.setSelected(false)
            } else {
                this.LaguageBtn.setSelected(true)
            }
        },

        EffectSliderTouch: function (sender, event) {
            appInstance.audioManager().setEffectVolume(sender.getPercent() / 100)
            if (sender.getPercent() === 0) {
                this.EffectBtn.setSelected(true)
            } else {
                this.EffectBtn.setSelected(false)
            }
        },
        MusicSliderTouch: function (sender, event) {
            appInstance.audioManager().setMusicVolume(sender.getPercent() / 100)
            if (sender.getPercent() === 0) {
                this.MusicBtn.setSelected(true)
            } else {
                this.MusicBtn.setSelected(false)
            }
        },

        onEffectBtnClick: function (sender) {
            // GameUtil.delayBtn(sender);
            if (sender.isSelected()) {
                this.EffectSlider.setPercent(100)
                this.EffectBtn.setSelected(true)
            } else {
                this.EffectSlider.setPercent(0)
                this.EffectBtn.setSelected(false)
            }
        },

        onMusicBtnClick: function (sender) {
            // GameUtil.delayBtn(sender);
            if (sender.isSelected()) {
                this.MusicSlider.setPercent(100)
                this.MusicBtn.setSelected(true)
            } else {
                this.MusicSlider.setPercent(0)
                this.MusicBtn.setSelected(false)
            }

        },

        onEmojBtnClick: function (sender) {
            // GameUtil.delayBtn(sender);
            if (sender.isSelected()) {

            } else {

            }
        },

        onLaguageBtnClick: function (sender) {
            // GameUtil.delayBtn(sender);
            if (sender.isSelected()) {
                global.localStorage.setStringForKey(LocalSave.LocalLanguage, 'putong')
            } else {
                global.localStorage.setStringForKey(LocalSave.LocalLanguage, 'dongbei')
            }
        },
        onExitBtnClick: function (sender) {
            GameUtil.delayBtn(sender);
            appInstance.gameAgent().goLoginScene()
        },
        onEnter: function () {
            this._super()
            this.initView()
        },
        onExit: function () {
            this._super()
        },
        onCloseClick: function (sender) {
            GameUtil.delayBtn(sender);
            appInstance.audioManager().flush()
            appInstance.sendNotification(GameEvent.HALL_RED_GET)
            appInstance.uiManager().removeUI(this)
        }
    })
    return layer
})
