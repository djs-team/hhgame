/**
 * EventManager Manager
 **/
load('public/manager/EventManager', function () {
    let EventManager = cc.Class.extend({
        listenerMap: null,
        ctor: function () {
            this.listenerMap = {}
        },
        /**
         *
         * @returns {null}
         */
        getEventListeners: function () {
            return this.listenerMap
        },
        /**
         * 派发事件
         * @param eventName
         * @param userData
         */
        dispatchEvent: function (eventName, userData) {
            if (cc.sys.OS_OSX === cc.sys.os) {
                cc.log('\n dispatchEvent EventManager.js eventName:userData ' + eventName + ' ' + JSON.stringify(userData) + '\n')
            }
            cc.eventManager.dispatchCustomEvent(eventName, userData)
        },
        /**
         * 注册自己定义的全局事件
         * @param eventName
         * @param cb
         * @param target
         */
        registerGlobelListener: function (eventName, cb, target) {
            this.registerEventListener(eventName, cb, target, 1)
        },
        /**
         * 注册自定义事件监听
         * @param eventName
         * @param cb
         * @param target
         */
        registerEventListener: function (eventName, cb, target, priority) {
            try {
                let nodeOrPriority = target
                if (typeof priority === 'number' && priority !== 0) {
                    nodeOrPriority = priority
                }
                let listener = cc.eventManager.addListener(cc.EventListener.create({
                    event: cc.EventListener.CUSTOM,
                    eventName: eventName,
                    callback: function (et) {
                        if (cb && typeof cb === 'function') {
                            if (target) {
                                cb.call(target, et.getUserData())
                            } else {
                                cb(et.getUserData())
                            }
                        }
                    }
                }), nodeOrPriority)
                this._add(eventName, listener, target)
            } catch (e) {
                throw new Error(cc.formatStr('registerEventListener Error EventName is [%s]', eventName))
            }

        },
        /**
         * 注册keyboard 的监听(键盘事件不会存储到容器中，生命周期跟着target 的生命周期走)
         * @param cb
         * @param target
         */
        registerKeyboardListener: function (cb, target) {
            cc.eventManager.addListener(cc.EventListener.create({
                event: cc.EventListener.KEYBOARD,
                onKeyReleased: function (key, event) {
                    cb.call(target, key, event)
                }
            }), target)
        },
        /**
         *  往容器中添加listener
         */
        _add: function (eventName, listener, target) {
            if (!this.listenerMap[target.__instanceId]) {
                this.listenerMap[target.__instanceId] = {}
            }
            this.listenerMap[target.__instanceId][eventName] = listener
        },
        /**
         *  往容器中添加listener
         */
        _del: function (target) {
            if (this.listenerMap[target.__instanceId]) {
                delete this.listenerMap[target.__instanceId]
            }
        },
        /**
         * 注销掉监听的事件
         */
        unRegisterEventListener: function (eventName, target) {
            let listeners = this.listenerMap[target.__instanceId]
            if (listeners) {
                let listener = listeners[eventName]
                if (listener) {
                    cc.eventManager.removeListener(listener)
                    delete listeners[eventName]
                    listener = null
                }
            }
        },
        /**
         *  暂停node 上绑定的事件
         * @param node
         */
        pauseEventListenersForTarget: function (node) {
            cc.eventManager.pauseTarget(node)
        },
        /**
         *  恢复 node 上绑定的事件
         * @param node,
         */
        resumeEventListenersForTarget: function (node) {
            cc.eventManager.resumeTarget(node)
        }

    })

    return EventManager
})
