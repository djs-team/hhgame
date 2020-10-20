
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

    initData: function () {

    },

    formatUrl: function (host, port) {
        return 'ws://' + host + ':' + port
    },

    initTcp: function (params) {
        if (!params || !params.host || !params.port) {
            console.log('error >>> initTcp need host and port')
            return
        }
        if (this._socket) {

        }
        this.initData()
        let url = this.formatUrl(params.host, params.port)
        cc.log('========url=====' + url)
        this._socket = new WebSocket(url)
        this._socket.binaryType = 'arraybuffer'
        this._socket.onopen = this.onSocketConnect.bind(this);

        this._socket.onmessage = this.onSocketReceived.bind(this);

        this._socket.onerror = this.onSocketError.bind(this);

        this._socket.onclose = this.onSocketClose.bind(this);
    },

    send: function (arrayBuffer) {
        this._socket.send(arrayBuffer)
    },

    disconnect: function () {
        if (this._socket) {
            if (this._socket.disconnect) {
                this._socket.disconnect()
            }
            if (this._socket.close) {
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
    },

    onSocketClose: function (event) {
        cc.log('================onSocketClose================' + JSON.stringify(event))
    },
    getReadyState: function () {
        if (this._socket && cc.sys.isObjectValid(this._socket)) {
            return this._socket.readyState
        }
        return WebSocket.CLOSED
    }
})

window.TcpClient = new TcpClient()
