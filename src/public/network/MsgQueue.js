
/**
 * 用于管理msg 的队列
 */
load('public/network/MsgQueue', function () {
    let ParserRe = include('public/network/ParserRe')
    let MsgQueue = cc.Class.extend({
        ctor: function () {
            this._bufferQueue = [] // 消息队列用来缓存服务器过来的字节
            this._msgQueue = [] // 消息队列用来缓存消息
            this._msgHandles = {} // 注册的消息监听队列
            this._curMsgHandlerDelay = 0 // 用于处理有需要延迟处理
        },

        /**
         * 将buffer 压入队列中
         */
        pushBuffer: function (buffer) {
            this._bufferQueue.push(buffer)
        },
        /**
         * 将msg 压入队列中
         */
        pushMsg: function (msg) {
            this._msgQueue.push(msg)
        },
        /**
         * 获取注册的handler
         * @param key
         */
        getHandler: function (key) {
            return this._msgHandles[key]
        },
        /**
         * 将buffer队列中的添加到msgQueue队列中
         * @param buffer
         */
        buffer2Msg: function (buffer) {
            let RePacket = new ParserRe(buffer)
            let msgData = RePacket.getMsgData()
            this._msgQueue.push(msgData)
        },
        /**
         * 用于每帧去处理相关的 msg 数据
         */
        onUpdate: function (dt) {
            if (this._curMsgHandlerDelay <= 0) {
                if (this._msgQueue.length > 0) {
                    let msg = this._msgQueue.shift()
                    if (msg.msgId) {
                        let handler = this._msgHandles[msg.msgId]
                        if (handler && handler.handleMsg) {
                            handler.handleMsg(msg)
                            this._curMsgHandlerDelay = handler._delay || 0
                        }
                    }
                } else if (this._bufferQueue.length) {
                    this.buffer2Msg(this._bufferQueue.shift())
                }
            } else {
                this._curMsgHandlerDelay = this._curMsgHandlerDelay - dt
            }
        },
        /**
         *  用于注册网络消息监听]
         */
        registerMsgHandles: function (handlerList) {
            if (handlerList) {
                for (let key in handlerList) {
                    let handler = handlerList[key]
                    this.registerMsgHandler(key, handler)
                }
            }
        },
        /**
         *  用于注册协议数据的监听
         */
        registerMsgHandler: function (key, handler) {
            this._msgHandles[key] = handler
        },
        /**
         * 用于卸载监听msg
         * @param handlerListKey 注册的消息监听队列的唯一标识
         */
        removeHandlerByKey: function (key) {
            if (this._msgHandles[key]) {
                this._msgHandles[key] = null
                delete this._msgHandles[key]
            }
        },
        removeAllHandler: function () {
            for (let key in this._msgHandles) {
                this.removeHandlerByKey(key)
            }
        },
        /*
         * 消息管理器资源释放
         */
        exit: function () {
            this._msgQueue = []
            this.removeAllHandler()
        }
    })

    return MsgQueue
})
