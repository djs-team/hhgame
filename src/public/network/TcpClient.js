
/**
 * 客户端TCP管理
 */

let TcpClient = cc.Class.extend({
    _headLen: 18,
    _socket: null,
    ctor: function () {

    },

    initGameNet: function (gameNet) {
        this._gameNet = gameNet
    },

    formatUrl: function (host, port) {
        return 'ws://' + host + ':' + port
    },

    connectTcp: function (host, port) {
        if (host && port) {
            this._host = host
            this._port = port
        }

        if (!this._host || !this._port) {
            return
        }

        this.disconnect()

        let url = this.formatUrl(this._host, this._port)

        this._socket = new WebSocket(url)
        this._socket.binaryType = 'arraybuffer'
        this._socket.onopen = this.onSocketConnect.bind(this);

        this._socket.onmessage = this.onSocketReceived.bind(this);

        this._socket.onerror = this.onSocketError.bind(this);

        this._socket.onclose = this.onSocketClose.bind(this);
    },

    send: function (arrayBuffer) {
        if (this.getSocketState() !== WebSocket.CLOSED) {
            this._socket.send(arrayBuffer)
        }
    },

    disconnect: function () {
        if (this._socket) {
            if (this._socket.disconnect) {
                this._socket.disconnect()
            }
            if (cc.sys.isObjectValid(this._socket) && this._socket.close) {
                this._socket.close()
            }
            this._socket = null
        }
    },

    onSocketConnect: function (event) {
        cc.log('================onSocketConnect================' + JSON.stringify(event))
        if (event.type === 'open') {
            appInstance.gameAgent().tcpGame().tcpLogin()
        }
    },

    onSocketReceived: function (event) {
        this._gameNet.tcpReceved(event.data)
    },

    onSocketError: function (event) {
        cc.log('================onSocketError================' + JSON.stringify(event))
        appInstance.eventManager().dispatchEvent('TCP_CLOSE')
    },

    onSocketClose: function (event) {
        cc.log('================onSocketClose================' + JSON.stringify(event))
        appInstance.eventManager().dispatchEvent('TCP_CLOSE')
    },
    getSocketState: function () {
        if (this._socket && cc.sys.isObjectValid(this._socket)) {
            return this._socket.readyState
        }
        return WebSocket.CLOSED
    }
})

window.TcpClient = new TcpClient()
