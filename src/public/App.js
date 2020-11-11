/**
 *  整个游戏有且只有一个唯一的App 实例用来全局的管理游戏
 */
load('public/App', function () {
    let ResManager = include('public/manager/ResManager')
    let EventManager = include('public/manager/EventManager')
    let SceneManager = include('public/manager/SceneManager')
    let UiManager = include('public/manager/UiManager')
    let DataManager = include('public/manager/DataManager')
    let GameNet = include('public/network/GameNet')
    let AudioManager = include('public/manager/AudioManager')
    let HeadFileCache = include('public/ui/HeadFileCache')
    let ModuleManager = include('public/manager/ModuleManager')
    let Display = include('public/ui/Display')
    let GameManager = include('public/manager/GameManager')
    let NativeApi = include('public/suport/NativeApi')
    let HttpFactory = include('public/http/HttpFactory')
    let ObserverController = include('public/components/ObserverController')
    let Notifier = include('public/components/Notifier')
    let MediatorController = include('public/components/MediatorController')
    let CommandController = include('public/components/CommandController')
    let MsgTool = include('public/network/MsgTool')
    let HttpAgent = include('public/http/HttpAgent')
    let ProtoArrays = include('public/network/ProtoArrays')

    let UPDATE_DELAY_TIME = 1 / 60 // 60帧
    let App = cc.Class.extend({
        _resManager: null, // 资源管理对象
        _eventManager: null, // 事件管理对象
        _sceneManager: null, // scene层管理对象
        _uiManager: null, // UI层事件管理对象
        _dataManager: null,
        _gameNet: null, // 网络连接层管理对象
        _headFileCache: null, // 头像的缓存类
        _moduleManager: null,
        _gameManager: null,
        _nativeApi: null,
        _httpFactory: null,
        _httpAgent: null,
        _audioManager: null,
        _display: null, // 显示适配相关
        _msgTool: null,
        _protoArrays: null, // 通用proto arrays

        _timerIdArray: {},

        _observerController: null,
        _notifier: null,
        _mediatorCtrl: null,
        _commandCtrl: null,
        _gameAgent: null,

        ctor: function () {
            this._resManager = new ResManager()
            this._eventManager = new EventManager()
            this._sceneManager = new SceneManager()
            this._uiManager = new UiManager()
            this._dataManager = new DataManager()
            this._msgTool = new MsgTool()
            this._gameNet = new GameNet(this._eventManager)
            this._audioManager = new AudioManager()
            this._headFileCache = new HeadFileCache()
            this._moduleManager = new ModuleManager()
            this._display = new Display()
            this._gameManager = new GameManager()
            this._nativeApi = new NativeApi()
            this._httpFactory = new HttpFactory()
            this._httpAgent = new HttpAgent()
            this._protoArrays = new ProtoArrays(this._msgTool)
            this._observerController = new ObserverController()

            this._notifier = new Notifier(this._observerController)
            this._mediatorCtrl = new MediatorController(this._observerController)
            this._commandCtrl = new CommandController(this._observerController)


            cc.director._scheduler.schedule(this.onUpdate, this, UPDATE_DELAY_TIME, cc.REPEAT_FOREVER, 0, false, 'appUpdate')
        },
        onExit: function () {
        },
        /**
         * 作为app启动的入口
         */
        run: function () {
            cc.log('=====================public app run==============')
        },

        unAllRegisterEventListener: function () {
            // this.eventManager().unRegisterEventListener(EventType.USER_EVENT.USER_EVENT_ONGLONOTIFY, this)
        },

        restartGame: function (delUpdate) {
            appInstance.gameNet().disConnect()
            cc.director.getScheduler().unscheduleAllWithMinPriority(cc.Scheduler.PRIORITY_NON_SYSTEM)
            this.httpFactory().abortAll()
            let GameIndex = include('game/index')
            if (GameIndex && GameIndex.restart) {
                GameIndex.restart()
            }
            this.unAllRegisterEventListener()
            this.resManager().reset()
            this.audioManager().clearAudioEnv()
            this.moduleManager().clearModuleEnv()
            if (delUpdate) {
                global.gameFile.delUpdateModule(true)
            }
            this.clearTimer()

            this.eventManager().dispatchEvent('restartGame')
        },

        clearTimer: function () {
            if (this._timerIdArray) {
                for (let key in this._timerIdArray) {
                    clearTimeout(this._timerIdArray[key])
                }
            }
        },

        /**
         * 每帧调用一次的更新函数
         * @param dt
         */
        onUpdate: function (dt) {
            this._sceneManager.onUpdate(dt)
            this._uiManager.onUpdate(dt)
            this._gameNet.onUpdate(dt)
            this._httpAgent.onUpdate(dt)

            if (this._gameAgent && this._gameAgent.onUpdate) {
                this._gameAgent.onUpdate(dt)
            }
        },

        updateSubModule: function (et) {
            // cc.log('[updateSubModule]:' + JSON.stringify(et))
            if (this._moduleManager && et.type) {
                if (et.type === 'urlContent') {
                    this._moduleManager.updateSubModuleInfos(et.msg)
                } else if (et.type === 'nromal') {
                    this.__marqueeMsg = et.msg
                }
            }
        },

        setGameAgent: function (agent) {
            this._gameAgent = agent
        },

        gameAgent: function () {
            return this._gameAgent
        },

        protoArrays: function () {
            return this._protoArrays
        },

        httpAgent: function () {
            return this._httpAgent
        },

        sendNotification: function (name, body) {
            if (this._notifier) {
                this._notifier.sendNotification(name, body)
            }
        },

        mediatorCtrl: function () {
            return this._mediatorCtrl
        },

        commandCtrl: function () {
            return this._commandCtrl
        },

        display: function () {
            return this._display
        },

        uiManager: function () {
            return this._uiManager
        },

        audioManager: function () {
            return this._audioManager
        },

        resManager: function () {
            return this._resManager
        },

        eventManager: function () {
            return this._eventManager
        },

        sceneManager: function () {
            return this._sceneManager
        },

        dataManager: function () {
            return this._dataManager
        },

        gameNet: function () {
            return this._gameNet
        },

        msgTool: function () {
            return this._msgTool
        },

        headFileCache: function () {
            return this._headFileCache
        },

        moduleManager: function () {
            return this._moduleManager
        },

        gameManager: function () {
            return this._gameManager
        },

        nativeApi: function () {
            return this._nativeApi
        },

        httpFactory: function () {
            return this._httpFactory
        }
    })
    return App
})
