
load('public/http/HttpAgent', function () {
    let HttpType = include('public/http/HttpType')
    let HttpAgent  = cc.Class.extend({
         _url: 'http://10.66.66.111:8080/mj-hall-server/msgReceive/httpDecoder.do?',
        _cb: null,
        _commonData: {
            ver: 1, //版本号
            ext: 1, //扩展数据
            pid: 0, //玩家ID
            serviceNum: 0,　//服务器ID
            key: '', //验证码
            gid: 5 //
        },
        _requestQueue: [],
        ctor: function () {
        },

        /**
         * 每帧调用一次的更新函数
         * @param dt
         */
        onUpdate: function (dt) {
            if (this._requestQueue.length) {
                let msg = this._requestQueue.shift()
                appInstance.httpFactory().createHttpRequest(msg.method, msg.url, msg.msg, msg.cb)
            }
        },

        setUrl: function (url) {
            this._url = url
        },

        setCallBack: function (cb) {
            this._cb = cb
        },

        defaultCb: function (data) {
            if (result.code === HttpType.ResultCode.OK) {
                cc.log('===httpAgent:: defaultCb==' + JSON.stringify(data))
            } else {
                cc.log('===httpAgent:: defaultCb==error')
            }
        },

        setCommonData: function (tb) {
            for (let key in tb) {
                this._commonData[key] = tb[key]
            }
        },

        getCommonData: function () {
            return this._commonData
        },

        sendPost: function (msg) {
            let cb = this._cb || this.defaultCb
            for (let key in this._commonData) {
                msg[key] = this._commonData[key]
            }
            let queue = {
                method: 'POST',
                cb: cb,
                url: this._url,
                msg: 'httpMsg=' + JSON.stringify(msg)
            }

            this._requestQueue.push(queue)
            this.onUpdate()
        },

        sendGet: function (msg) {
            let cb = this._cb || this.defaultCb
            for (let key in this._commonData) {
                msg[key] = this._commonData[key]
            }
            let queue = {
                method: 'GET',
                cb: cb,
                url: this._url,
                msg: 'httpMsg=' + JSON.stringify(msg)
            }
            this._requestQueue.push(queue)
            this.onUpdate()
        }

    })
    return HttpAgent
})