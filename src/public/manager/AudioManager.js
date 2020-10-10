
load('public/manager/AudioManager', function () {
    let audioManager = cc.Class.extend({
        _bgMusic: null, // 背景音乐
        _bgMusicID: null, // 背景音乐的ID
        _musicVolume: null, // 背景音乐的音量
        _effectVolume: null, // 音效的音量
        _isMute: false, // 是否静音
        _effectArray: [], // 用于存储音效
        _MAXEFFECTNUMS: 20,
        _buttonClickEffectSwitch: true, //按钮点击音效的开关
        _buttonClickEffect:'', // 默认的按钮点击的全局音效
        ctor: function () {
            this._bgMusic = null
            this._bgMusicID = jsb.AudioEngine.INVALID_AUDIO_ID
            this._musicVolume = global.localStorage.getNumberForKey('MusicVolume') !== null ? global.localStorage.getNumberForKey('MusicVolume') : 0.5
            this._effectVolume = global.localStorage.getNumberForKey('EffectVolume') !== null ? global.localStorage.getNumberForKey('EffectVolume') : 0.5
            this._isMute = this.getSaveIsMute()
            let clickEffect = global.localStorage.getStringForKey('BtClickEffect')|| 'res/sound/audio_button_click.mp3'
            this.setButtonClickEffect(clickEffect)
            let isOpenClickEffect =  global.localStorage.getBoolForKey('IsOpenBtClickEffect')!== null ?  global.localStorage.getBoolForKey('IsOpenBtClickEffect'): true
            this.setButtonClickEffectSwitch(isOpenClickEffect)
        },
        getButtonClickEffectSwitch:function(){
            return this._buttonClickEffectSwitch
        },
        setButtonClickEffectSwitch: function(isOpen){
            this._buttonClickEffectSwitch = isOpen
            global.localStorage.setBoolForKey('IsOpenBtClickEffect',isOpen)
        },
        setButtonClickEffect:function(effectName){
            this._buttonClickEffect = effectName
            global.localStorage.setStringForKey('BtClickEffect',effectName)
        },
        getButtonClickEffect:function(){
            return this._buttonClickEffect
        },
        saveMusicVolume: function () {
            global.localStorage.setNumberForKey('MusicVolume', this._musicVolume)
        },
        saveEffectVolume: function () {
            global.localStorage.setNumberForKey('EffectVolume', this._effectVolume)
        },
        /**
         * 存储当前的音量
         */
        flush: function () {
            this.saveMusicVolume()
            this.saveEffectVolume()
        },
        getMusicVolume: function () {
            return this._musicVolume
        },
        getEffectVolume: function () {
            return this._effectVolume
        },
        // /**
        //  * 设置静音的状态
        //  * @param isMute
        //  */
        // setMute: function (isMute) {
        //   this._isMute = isMute
        // },
        getSaveIsMute: function () {
            return false
        },
        /**
         *  播放背景音乐
         */
        playMusic: function (path, isLoop) {
            if (this._isMute) {
                return
            }
            if (this._bgMusic && this._bgMusic === path) {
                return
            }
            this._bgMusic = path
            if (this._bgMusicID !== jsb.AudioEngine.INVALID_AUDIO_ID) {
                jsb.AudioEngine.stop(this._bgMusicID)
                this._bgMusicID = jsb.AudioEngine.INVALID_AUDIO_ID
            }

            this._bgMusicID = jsb.AudioEngine.play2d(path, isLoop, this._musicVolume)
        },
        /**
         *  播放音效
         */
        playEffect: function (path) {
            if (this._isMute) {
                return
            }
            if (!this._effectVolume) {
                return
            }
            if (this._effectArray.length >= this._MAXEFFECTNUMS) {
                let effectId = this._effectArray.shift()
                jsb.AudioEngine.stop(effectId)
            }
            let effectId = jsb.AudioEngine.play2d(path, false, this._effectVolume)
            if (effectId !== jsb.AudioEngine.INVALID_AUDIO_ID) {
                this._effectArray.push(effectId)
            }
            return effectId
        },
        /**
         *  停止播放音乐
         */
        stopMusic: function () {
            if (this._bgMusicID !== jsb.AudioEngine.INVALID_AUDIO_ID) {
                jsb.AudioEngine.stop(this._bgMusicID)
                this._bgMusicID = jsb.AudioEngine.INVALID_AUDIO_ID
                this._bgMusic = null
            }
        },

        /**
         *  暂停播放背景音乐
         */
        pauseMusic: function () {
            if (this._bgMusicID !== jsb.AudioEngine.INVALID_AUDIO_ID) {
                this.pauseByID(this._bgMusicID)
            }
        },

        resumeMusic: function () {
            if (this._bgMusicID !== jsb.AudioEngine.INVALID_AUDIO_ID) {
                this.resumeByID(this._bgMusicID)
            }
        },

        /**
         * 暂停所有音效
         */
        pauseAll: function () {
            jsb.AudioEngine.pauseAll()
        },
        /**
         * 根据ID 暂停
         */
        pauseByID: function (audioID) {
            jsb.AudioEngine.pause(audioID)
        },
        /**
         * 恢复所有音效
         */
        resumeAll: function () {
            if (this._isMute) return
            jsb.AudioEngine.resumeAll()
        },
        /**
         * 根据ID 恢复
         */
        resumeByID: function (audioID) {
            if (this._isMute) return
            jsb.AudioEngine.resume(audioID)
        },
        /**
         * 音量大小
         * @param val [0-1]
         */
        setMusicVolume: function (val) {
            this._musicVolume = val
            if (this._bgMusicID || this._bgMusicID === 0) {
                jsb.AudioEngine.setVolume(this._bgMusicID, this._musicVolume)
            }
        },
        /**
         * 设置音效音量的大小
         */
        setEffectVolume: function (val) {
            this._effectVolume = val
            for (let i = 0; i < this._effectArray.length; i++) {
                let audioID = this._effectArray[i]
                if (jsb.AudioEngine.getState(audioID) === jsb.AudioEngine.AudioState.PLAYING) {
                    jsb.AudioEngine.setVolume(audioID, this._effectVolume)
                }
            }
        },
        /**
         * 停止所有音效
         */
        stopAllEffect: function () {
            for (let i = 0; i < this._effectArray.length; i++) {
                let audioID = this._effectArray[i]
                jsb.AudioEngine.stop(audioID)
            }
            this._effectArray = []
        },
        /**
         * 停止指定音效
         */
        stopEffect: function (audioID) {
            let idx = this._effectArray.indexOf(audioID)
            if (idx >= 0) {
                jsb.AudioEngine.stop(audioID)
                this._effectArray.splice(idx, 1)
            }
        },
        /**
         *  清理环境
         */
        clearAudioEnv: function () {
            this._bgMusic = null
            this._bgMusicID = jsb.AudioEngine.INVALID_AUDIO_ID
            jsb.AudioEngine.stopAll()
            this.stopAllEffect()
            jsb.AudioEngine.uncacheAll()
        },
        uncacheAudio: function (path) {
            jsb.AudioEngine.uncache(path)
        }

    })
    return audioManager
})
