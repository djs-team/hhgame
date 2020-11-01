
/**
 * 网络管理层
 */
load('public/network/GameNet', function () {
    let EventType = include('public/config/EventType')
    let MsgQueue = include('public/network/MsgQueue')
    let ParserSe = include('public/network/ParserSe')

    let GameNet = cc.Class.extend({
        isReconnect: false,
        reConnectTimes: 0,
        reConnectInterval: 10,
        reConnectNum: 0,
        msgQueue: null,
        reqPingPong: [],
        reqStart: null,
        port: null,
        host: null,
        ctor: function (eventMgr) {
            this._eventMgr = eventMgr
            this._msgQueue = new MsgQueue()
            this._protoReObj = {}
            this.reqStart = Date.now()
            this.registWebScoketSysEvent()

            TcpClient.initGameNet(this)
        },

        tcpReceved: function (arrayBuffer) {
            this._msgQueue.pushBuffer(arrayBuffer)
        },

        /**
         * 获得socket 的状态
         * @returns {*}
         */
        getSocketState: function () {
            return TcpClient.getSocketState()
        },
        /**
         * 获取注册的handler
         * @param key
         */
        getHandler: function (key) {
            if (this._msgQueue) {
                return this._msgQueue.getHandler(key)
            }
            return null
        },
        /**
         * 设置handler 的delay
         * @param key
         * @param delay
         */
        setHanlderDelay: function (key, delay) {
            let handler = this.getHandler(key)
            if (handler) {
                handler.setDelay(delay)
            }
        },
        /**
         * 将服务器返回的消息放入队列
         * @param route
         * @param msg
         */
        pushMsgToQueue: function (route, msg) {
            let msgData = { cmd: route, msg: msg }
            if (this._msgQueue) {
                this._msgQueue.pushMsg(msgData)
            }
            global.View.unblock()
        },
        /**
         * 用于注册网络协议的处理函数
         */
        registerMsgHandler: function (key, handlerObj) {
            if (this._msgQueue) {
                this._msgQueue.registerMsgHandler(key, handlerObj)
            }
            this._protoReObj[key] = handlerObj
        },
        /**
         * 用于根据key值注销协议处理函数
         * @param key
         */
        unRegisterMsgHandler: function (key) {
            if (this._msgQueue) {
                this._msgQueue.removeHandlerByKey(key)
            }
            this._protoReObj[key] = null
        },

        getProtoReObj: function (msgId) {
            return this._protoReObj[msgId]
        },

        setReconnect: function (tag) {
            this.isReconnect = tag || false
        },

        onUpdate: function (dt) {
            if (this._msgQueue) {
                this._msgQueue.onUpdate(dt)
            }

            if (this.isReconnect) {
                this.reConnectTimes += dt
                if (this.reConnectTimes > this.reConnectInterval) {
                    this.reConnectTimes = 0
                    this.reConnectNum += 1
                    if (this.reConnectNum < 5) {
                        this.connect()
                    } else {
                        appInstance.eventManager().dispatchEvent('RECONNECT_OVER_TIMES')
                    }
                }
            }
        },
        /**
         * 客户端用于来取每条通信直接的平均时间
         * @constructor
         */
        ComputePingPong: function () {
            this.reqPingPong.push(Date.now() - this.reqStart)
            if (this.reqPingPong.length > 5) {
                this.reqPingPong.splice(0, 1)
            }
            let delay = 0
            for (let i = 0; i < this.reqPingPong.length; i++) {
                delay += this.reqPingPong[i]
            }
            delay = delay / this.reqPingPong.length
            this._eventMgr.dispatchEvent(EventType.NET_EVENT.NET_EVENT_PINGPONG, delay)
        },
        /**
         * websocket 的connect 的接口
         * @param host
         * @param port
         */
        connect: function (host, port) {
            this.host = host ? host : this.host
            this.port = port ? port : this.port
            if (!this.host || !this.port) {
                cc.log('>>> --- gameNet connect not enough in fo:-- host:' + this.host + '   port:' + this.port)
                return
            }
            TcpClient.connectTcp(host, port)
        },

        /**
         *  断开链接
         * @param isRestart
         */
        disConnect: function (isRestart) {
            TcpClient.disconnect(isRestart)
        },

        /**
         * 用于往服务器消息
         * @param packet
         */
        send: function (packet) {
            packet.LockPacket()
            let PacketParser = new ParserSe(packet)
            TcpClient.send(PacketParser.getArrayBuffer())
        },
        /**
         * 用于往服务器发送request 请求
         * @param type
         * @param msg
         * @param callBack
         */
        request: function (type, msg, callBack) {
            this.reqStart = Date.now()
            let cb = function (route, body) {
                this.ComputePingPong()
                if (callBack) {
                    callBack(body)
                } else {
                    this.pushMsgToQueue(route, body)
                }
                global.View.unblock()
            }.bind(this)
            TcpClient.request(type, msg, cb)
        },
        /**
         * 用于内部注册net 层事件的回调
         * @private
         */
        _registerNetEvent: function (evt, callBack) {
            // if (callBack) {
            //     tcpClient.off(evt)
            //     tcpClient.on(evt, (data) => {
            //         if (cc.sys.OS_WINDOWS === cc.sys.os) {
            //             cc.log(evt + '@' + JSON.stringify(data))
            //         }
            //         callBack(evt.toString(), data)
            //     })
            // }
        },

        /**
         * 获得 host 以及端口的数据
         * @returns {string}
         */
        getHostPort: function () {
            return 'host:' + this.host + ', port=' + this.port
        },

        /**
         * 注册网络系统级事件响应
         */
        registWebScoketSysEvent: function () {
            this.registConnect()
            this.registOnKick()
            this.registDisconnect()
        },

        /**
         * 服务器连接
         */
        registConnect: function () {
            this._registerNetEvent(EventType.POMELO_EVENT.POMELO_EVENT_ROUNDCONNECT, function () {
                global.View.unblock()
                global.alertMsg('当前网络不可用, 请重新连接。', function () { setTimeout(function () { appInstance.connectorApi().connectServer() }, 200) })
            })
        },
        /**
         * 服务器踢人
         */
        registOnKick: function () {
            this._registerNetEvent(EventType.POMELO_EVENT.POMELO_EVENT_ONKICK, function () {
                global.View.unblock()
                global.alertMsg('您有非常规操作, 或此账户多设备登录，请检查再尝试。', function () {
                    setTimeout(function () {
                        tcpClient.isOnKick = false
                        appInstance.connectorApi().connectServer()
                    }, 200)
                })
            })
        },

        /**
         * scoket error
         * 1 客户端连接失败
         * 2 请求客户端连接无法完成与远程服务器的握手
         * 3 协议将根本无法使用，当前的readystate为state:：connecting
         * 4 当需要从外部轮询数组中删除套接字描述符时。In也是包含要删除的fd成员的结构libwebsocket pollargs
         */
        registScoketError: function () {
            this._registerNetEvent(EventType.POMELO_EVENT.POMELO_EVENT_IOERROR, function () {
            })
        },

        /**
         * scoket close 网络连接断开
         */
        registDisconnect: function () {
            this._registerNetEvent(EventType.POMELO_EVENT.POMELO_EVENT_DISCONNECT, function () {
                tcpClient.View.block()
            })
        },
        /**
         * 心跳超时, 触发断线重连
         */
        registHeartBeatTimeOut: function () {
            this._registerNetEvent(EventType.POMELO_EVENT.POMELO_EVENT_HEARTBEATTIMEOUT, function () {
            })
        }
    })

    return GameNet
})
