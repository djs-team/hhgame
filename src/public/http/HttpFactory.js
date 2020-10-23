/**
 * HttpFactory
 * 用于创建管理销毁Http请求
 */
load('public/http/HttpFactory', function () {
    let HttpType = include('public/http/HttpType')

    let HttpRequest = cc.Class.extend({
        _method: null,
        _timeOut: 3000,
        _params: null,
        _parseType: HttpType.ParseType.Text,
        _callBack: null,
        _xhr: null,
        _url: null,
        _lifeCycleAgent: null, // 生命周期代理
        ctor: function (method, url, params, callBack, parseType, timeOut, requestHeaderOptions, lifeCycleAgent) {
            this._method = method
            this._url = url
            this._params = params || null
            if (method === 'GET' && params) {
                this._url = global.buildURL(url, params)
                this._params = null
            }

            this._requestHeaderOptions = requestHeaderOptions || {}
            this._parseType = parseType || HttpType.ParseType.Text
            this._timeOut = timeOut || 3000
            this._lifeCycleAgent = lifeCycleAgent || null
            this._callBack = callBack
        },
        execute: function (ret) {
            this._callBack(ret)
            appInstance.httpFactory().removeByID(this.__instanceId)
        },
        create: function () {
            this._xhr = cc.loader.getXMLHttpRequest()
            this._xhr.timeout = this._timeOut
            this._xhr.onerror = function (event) {
                this.execute({ code: HttpType.ResultCode.OnError, data: event })
            }.bind(this)

            this._xhr.ontimeout = function (event) {
                this.execute({ code: HttpType.ResultCode.TimeOut, data: event })
            }.bind(this)

            this._xhr.onreadystatechange = function () {
                if (this._xhr.readyState === 4 && this._xhr.status === 200) {
                    let data
                    switch (this._parseType) {
                        case HttpType.ParseType.XXTea:
                            // TODO: 底层绑定
                            let xxteaData = jsb.fileUtils.getXXSecretData(this._xhr.responseText)
                            data = JSON.parse(xxteaData)
                            break
                        case HttpType.ParseType.Byte:
                            data = this._xhr.response
                            break
                        default: // to be parse TextFormat.
                            if (this._xhr.responseText) {
                                data = JSON.parse(this._xhr.responseText)
                            }
                            break
                    }
                    this.execute({ code: HttpType.ResultCode.OK, data: data })
                } else {
                    this.execute({ code: HttpType.ResultCode.OnError, data: this._xhr.statusText || null })
                }
            }.bind(this)

            for (let k in this._requestHeaderOptions) {
                if (this._requestHeaderOptions.hasOwnProperty(k)) { this._xhr.setRequestHeader(k, this._requestHeaderOptions[k]) }
            }

            this._xhr.open(this._method, this._url)
            this._xhr.send(this._params)
        },
        abort: function () {
            if (this._xhr) {
                this._xhr.abort()
            }
        }

    })

    let Factory = cc.Class.extend({
        _map: {},
        ctor: function () {
        },
        /**
         * 用于发起一个Http 请求
         * @param method (GET/POST)
         * @param url
         * @param params
         * @param parseType
         * @param callBack
         * @param timeOut ( 默认3s 的超时时间)
         */
        createHttpRequest: function (method, url, params, callBack, parseType, timeOut, requestHeaderOptions) {
            let hq = new HttpRequest(method, url, params, callBack, parseType, timeOut, requestHeaderOptions)
            hq.create()
            this._map[hq.__instanceId] = hq
            return hq.__instanceId
        },
        /**
         * 根据id 删除容器中的http 对象
         * @param id
         */
        removeByID: function (id) {
            delete this._map[id]
        },
        /**
         * 根据id 终止已经发出的http 请求
         * @param id
         */
        abortByID: function (id) {
            if (this._map[id]) {
                this._map[id].abort()
                this.removeByID(id)
            }
        },
        /**
         * 终止所有的Http 请求
         */
        abortAll: function () {
            for (let id in this._map) {
                if (this._map[id]) {
                    this._map[id].abort()
                    this.removeByID(id)
                }
            }
        }
    })
    return Factory
})
