/**
 * Layer 的基类
 */
load('public/ui/BaseLayer', function () {
    let BaseLayer = cc.Layer.extend({
        _className: 'BaseLayer',
        _canPlayPopAction: false,
        _isRelationUIMgr: false,
        _hasAutoLayout: false, // 标记适配，避免重复适配
        _mediatorName: null,
        _mediatorInstanceId: null,
        ctor: function (res) {
            this._super()
            this.initByRes(res)
            this.onCreate()
        },
        onCreate: function () {
        },
        registerMediator: function (mediator, isMulti) {
            this._mediatorName = mediator.mediatorName
            if (isMulti) {
                this._mediatorInstanceId = mediator.__instanceId
            }
            appInstance.mediatorCtrl().registerMediator(mediator, isMulti)
        },
        sendNotification: function (name, body) {
            appInstance.sendNotification(name, body)
        },
        setCanPopAction: function (flag) {
            this._canPlayPopAction = flag
        },
        setRelationUIMgr: function (relation) {
            this._isRelationUIMgr = relation
        },
        onEnter: function () {
            cc.log('<<<' + this._className + '>>>')
            this._super()
            if (!this._hasAutoLayout) {
                appInstance.display().autoLayout(this.rootNode)
                this._hasAutoLayout = true
            }
            if (this._canPlayPopAction) {
                global.View.runPopupAnimation(this)
            }
        },
        onExit: function () {
            this._super()
            if (this._mediatorName) {
                appInstance.mediatorCtrl().removeMediator(this._mediatorName, this._mediatorInstanceId)
            }
            if (this._isRelationUIMgr) {
                appInstance.uiManager().removeUI(this, true)
            }
        },
        getRootNode: function () {
            return this.rootNode
        },
        /**
         * 游戏每帧更新的结构
         * @param dt
         */
        onUpdate: function (dt) {
        },
        /**
         * 注册键盘事件
         * @param cb
         */
        registerKeyboardListener: function (cb) {
            appInstance.eventManager().registerKeyboardListener(cb, this)
        },
        /**
         * 注册事件
         */
        registerEventListener: function (eventName, cb) {
            appInstance.eventManager().registerEventListener(eventName, cb, this)
        },
        /**
         * 派发事件
         * @param eventName
         * @param userData
         */
        dispatchEvent: function (eventName, userData) {
            appInstance.eventManager().dispatchEvent(eventName, userData)
        },
        /**
         * 删除事件的监听
         * （如果没有特殊需求不需要主动的释放, 挂到scene 上的基类会随着自己的生命周期销毁）
         */
        unRegisterEventListener: function (eventName) {
            appInstance.eventManager().unRegisterEventListener(eventName, this)
        }

    })
    return BaseLayer
})
