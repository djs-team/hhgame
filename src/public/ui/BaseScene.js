
/**
 * Scene 的基类
 */
load('public/ui/BaseScene', function () {
    let BaseScene = cc.Scene.extend({
        _className: 'BaseScene',
        _preloadConfigs: [],
        _preloadCallBack: null,
        _gameExitAlert: null,
        _hasAutoLayout: false, // 标记适配，避免重复适配
        _mediatorName: null,
        _mediatorInstanceId:null,
        ctor: function (res) {
            this._super()
            this.initByRes(res)
            global.touchSecurity(this)
            this.registerKeyboardListener(function (keyCode, event) { this.onKeyBoard(keyCode, event) }.bind(this))
            this.onCreate()
        },
        onCreate:function(){
        },
        registerMediator: function (mediator,isMulti) {
            this._mediatorName = mediator.mediatorName
            if (isMulti) {
                this._mediatorInstanceId = mediator.__instanceId
            }
            appInstance.mediatorCtrl().registerMediator(mediator,isMulti)
        },
        sendNotification:function(name,body){
            appInstance.sendNotification(name,body)
        },
        onKeyBoard: function (keyCode, event) {
            if (keyCode === cc.KEY['r']) {
                if (cc.sys.OS_WINDOWS === cc.sys.os || cc.sys.OS_OSX === cc.sys.os) {
                    appInstance.restartGame()
                }
            } else if (keyCode === cc.KEY.back) {
                if (cc.director.isValid() && !this._gameExitAlert) {
                    this._gameExitAlert = global.alertMsg('确定要退出游戏吗？', function () {
                        appInstance.nativeApi().gameExit()
                    }, function () {
                        this._gameExitAlert = null
                    }.bind(this))
                }
            } else if (keyCode === cc.KEY['d']) {
                if (cc.sys.OS_WINDOWS === cc.sys.os || cc.sys.OS_OSX === cc.sys.os) {
                    appInstance.gameNet().disConnect()
                }
            } else if (keyCode === cc.KEY['p']) {
                if (cc.sys.OS_WINDOWS === cc.sys.os || cc.sys.OS_OSX === cc.sys.os) {
                    appInstance.uiManager().popSystemMessage('主动清理内存测试')
                    cc.director.purgeCachedData()
                }
            }
        },
        onEnter: function () {
            this._super()
            if (!this._hasAutoLayout) {
                appInstance.display().autoLayout(this.rootNode)
                this._hasAutoLayout = true
            }
        },

        onExit: function () {
            this._super()
            appInstance.eventManager()._del(this)
            if (this._mediatorName) {
                appInstance.mediatorCtrl().removeMediator(this._mediatorName, this._mediatorInstanceId)
            }
        }
        ,
        /**
         * 游戏每帧更新的结构
         * @param dt
         */
        onUpdate: function (dt) {
        }
        ,
        /**
         * 注册键盘事件
         * @param cb
         */
        registerKeyboardListener: function (cb) {
            appInstance.eventManager().registerKeyboardListener(cb, this)
        }
        ,
        /**
         * 注册事件
         */
        registerEventListener: function (eventName, cb) {
            appInstance.eventManager().registerEventListener(eventName, cb, this)
        }
        ,
        /**
         * 派发事件
         * @param eventName
         * @param userData
         */
        dispatchEvent: function (eventName, userData) {
            appInstance.eventManager().dispatchEvent(eventName, userData)
        }
        ,
        /**
         * 删除事件的监听
         * （如果没有特殊需求不需要主动的释放, 挂到scene 上的基类会随着自己的生命周期销毁）
         */
        unRegisterEventListener: function (eventName) {
            appInstance.eventManager().unRegisterEventListener(eventName, this)
        }

    })
    return BaseScene
})
