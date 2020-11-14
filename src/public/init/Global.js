let global = {}

global.bind = function (fn, thisArg) {
    return function wrap () {
        let args = new Array(arguments.length)
        for (let i = 0; i < args.length; i++) {
            args[i] = arguments[i]
        }
        return fn.apply(thisArg, args)
    }
}

// global is a library of generic helper functions non-specific to axios
global.toString = Object.prototype.toString

/**
 * Determine if a value is an Array
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is an Array, otherwise false
 */
global.isArray = function (val) {
    return this.toString.call(val) === '[object Array]'
}

/**
 * Determine if a value is an ArrayBuffer
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is an ArrayBuffer, otherwise false
 */
global.isArrayBuffer = function (val) {
    return this.toString.call(val) === '[object ArrayBuffer]'
}

/**
 * Determine if a value is a view on an ArrayBuffer
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a view on an ArrayBuffer, otherwise false
 */
global.isArrayBufferView = function (val) {
    let result
    if ((typeof ArrayBuffer !== 'undefined') && (ArrayBuffer.isView)) {
        result = ArrayBuffer.isView(val)
    } else {
        result = (val) && (val.buffer) && (val.buffer instanceof ArrayBuffer)
    }
    return result
}

/**
 * Determine if a value is a String
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a String, otherwise false
 */
global.isString = function (val) {
    return typeof val === 'string'
}

/**
 * Determine if a value is a Number
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a Number, otherwise false
 */
global.isNumber = function (val) {
    return typeof val === 'number'
}

/**
 * Determine if a value is undefined
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if the value is undefined, otherwise false
 */
global.isUndefined = function (val) {
    return typeof val === 'undefined'
}

/**
 * Determine if a value is an Object
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is an Object, otherwise false
 */
global.isObject = function (val) {
    return val !== null && typeof val === 'object'
}

/**
 * Determine if a value is a Date
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a Date, otherwise false
 */
global.isDate = function (val) {
    return this.toString.call(val) === '[object Date]'
}

/**
 * Determine if a value is a File
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a File, otherwise false
 */
global.isFile = function (val) {
    return this.toString.call(val) === '[object File]'
}

/**
 * Determine if a value is a Blob
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a Blob, otherwise false
 */
global.isBlob = function (val) {
    return this.toString.call(val) === '[object Blob]'
}

/**
 * Determine if a value is a Function
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a Function, otherwise false
 */
global.isFunction = function (val) {
    return this.toString.call(val) === '[object Function]'
}

// 是否是空对象 a = {}
global.isEmptyObject = function (obj) {
    for (let key in obj) {
        return false
    }
    return true
}

/**
 * Determine if a value is a Stream
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a Stream, otherwise false
 */
global.isStream = function (val) {
    return this.isObject(val) && this.isFunction(val.pipe)
}

/**
 * Determine if a value is a URLSearchParams object
 *
 * @param {Object} val The value to test
 * @returns {boolean} True if value is a URLSearchParams object, otherwise false
 */
global.isURLSearchParams = function (val) {
    return typeof URLSearchParams !== 'undefined' && val instanceof URLSearchParams
}

/**
 * Trim excess whitespace off the beginning and end of a string
 *
 * @param {String} str The String to trim
 * @returns {String} The String freed of excess whitespace
 */
global.trim = function (str) {
    return str.replace(/^\s*/, '').replace(/\s*$/, '')
}

/**
 * Determine if we're running in a standard browser environment
 *
 * This allows axios to run in a web worker, and react-native.
 * Both environments support XMLHttpRequest, but not fully standard globals.
 *
 * web workers:
 *  typeof window -> undefined
 *  typeof document -> undefined
 *
 * react-native:
 *  navigator.product -> 'ReactNative'
 */
global.isStandardBrowserEnv = function () {
    if (typeof navigator !== 'undefined' && navigator.product === 'ReactNative') {
        return false
    }
    return (
        typeof window !== 'undefined' &&
        typeof document !== 'undefined'
    )
}

global.cropStr = function (str, num, endstr) {
    if (str.length <= num ) {
        return str
    }
    num = num || 6
    endstr = endstr || ''

    return str.substring(0,num) + endstr
}

/**
 *
 * @param {version1} str like 1.0.1
 * @param {version2} str like 1.0.1
 * @returns {number} -1 小于  0 等于  1 大于
 */
global.checkVersion = function (version1, version2) {
    let arr1 = version1.split('.')
    let arr2 = version2.split('.')
    if (arr1[0] > arr2[0]) {
        return 1
    } else if (arr1[0] === arr2[0]) {
        if (arr1[1] > arr2[1]) {
            return 1
        } else if (arr1[1] === arr2[1]) {
            if (arr1[2] > arr2[2]) {
                return 1
            } else if(arr1[2] === arr2[2]) {
                return 0
            } else {
                return -1
            }
        } else {
            return -1
        }
    } else {
        return -1
    }
}

/**
 *
 * @param {version1} str like 1.0.1
 * @param {version2} str like 1.0.1
 * @returns {boolean}
 */
global.isNeedForce = function (serverVersion, localVersion) {
    let sArry = serverVersion.split('.')
    let lArry = localVersion.split('.')
    if (sArry[0] > lArry[0]) {
        return true
    } else if (sArry[0] === lArry[0]) {
        if (sArry[1] > lArry[1]) {
            return true
        } else if (sArry[1] === lArry[1]) {
            return false
        } else {
            return false
        }
    } else {
        return false
    }
}

global.getCurDayStr = function () {
    let now = new Date()
    return now.getFullYear().toString() + (now.getMonth() + 1).toString() + now.getDate().toString()
}

/**
 * Iterate over an Array or an Object invoking a function for each item.
 *
 * If `obj` is an Array callback will be called passing
 * the value, index, and complete array for each item.
 *
 * If 'obj' is an Object callback will be called passing
 * the value, key, and complete object for each property.
 *
 * @param {Object|Array} obj The object to iterate
 * @param {Function} fn The callback to invoke for each item
 */
global.forEach = function (obj, fn) {
    // Don't bother if no value provided
    if (obj === null || typeof obj === 'undefined') {
        return
    }

    // Force an array if not already something iterable
    if (typeof obj !== 'object' && !this.isArray(obj)) {
        /* eslint no-param-reassign:0 */
        obj = [obj]
    }

    if (this.isArray(obj)) {
        // Iterate over array values
        for (let i = 0, l = obj.length; i < l; i++) {
            // eslint-disable-next-line no-useless-call
            fn.call(null, obj[i], i, obj)
        }
    } else {
        // Iterate over object keys
        for (let key in obj) {
            if (Object.prototype.hasOwnProperty.call(obj, key)) {
                // eslint-disable-next-line no-useless-call
                fn.call(null, obj[key], key, obj)
            }
        }
    }
}

/**
 * Accepts varargs expecting each argument to be an object, then
 * immutably merges the properties of each object and returns result.
 *
 * When multiple objects contain the same key the later object in
 * the arguments list will take precedence.
 *
 * Example:
 *
 * ```js
 * var result = merge({foo: 123}, {foo: 456});
 * console.log(result.foo); // outputs 456
 * ```
 *
 * @param {Object} obj1 Object to merge
 * @returns {Object} Result of all merge properties
 */
global.merge = function (/* obj1, obj2, obj3, ... */) {
    var result = {}

    function assignValue (val, key) {
        if (typeof result[key] === 'object' && typeof val === 'object') {
            result[key] = global.merge(result[key], val)
        } else {
            result[key] = val
        }
    }

    for (var i = 0, l = arguments.length; i < l; i++) {
        global.forEach(arguments[i], assignValue)
    }
    return result
}

global.mergeData = function (source, tmpTb) {
    for (let k in tmpTb) {
        source[k] = tmpTb[k]
    }
}

/**
 * Extends object a by mutably adding to it the properties of object b.
 *
 * @param {Object} a The object to be extended
 * @param {Object} b The object to copy properties from
 * @param {Object} thisArg The object to bind function to
 * @return {Object} The resulting value of object a
 */
global.extend = function (a, b, thisArg) {
    this.forEach(b, function assignValue (val, key) {
        if (thisArg && typeof val === 'function') {
            a[key] = global.bind(val, thisArg)
        } else {
            a[key] = val
        }
    })
    return a
}

global.encode = function (val) {
    return encodeURIComponent(val)
        .replace(/%40/gi, '@')
        .replace(/%3A/gi, ':')
        .replace(/%24/g, '$')
        .replace(/%2C/gi, ',')
        .replace(/%20/g, '+')
        .replace(/%5B/gi, '[')
        .replace(/%5D/gi, ']')
}

/**
 * Build a URL by appending params to the end
 *
 * @param {string} url The base of the url (e.g., http://www.google.com)
 * @param {object} [params] The params to be appended
 * @returns {string} The formatted url
 */
global.buildURL = function (url, params, paramsSerializer) {
    /* eslint no-param-reassign:0 */
    if (!params) {
        return url
    }

    let serializedParams
    if (paramsSerializer) {
        serializedParams = paramsSerializer(params)
    } else if (global.isURLSearchParams(params)) {
        serializedParams = params.toString()
    } else {
        let parts = []
        global.forEach(params, function serialize (val, key) {
            if (val === null || typeof val === 'undefined') {
                return
            }
            if (global.isArray(val)) {
                key = key + '[]'
            }
            if (!global.isArray(val)) {
                val = [val]
            }
            global.forEach(val, function parseValue (v) {
                if (global.isDate(v)) {
                    v = v.toISOString()
                } else if (global.isObject(v)) {
                    v = JSON.stringify(v)
                }
                parts.push(global.encode(key) + '=' + global.encode(v))
            })
        })
        serializedParams = parts.join('&')
    }
    if (serializedParams) {
        url += (url.indexOf('?') === -1 ? '?' : '&') + serializedParams
    }
    return url
}

global.HttpGet = function (url, cbSucc, cbFail, options) {
    let flag = false
    let xhr = cc.loader.getXMLHttpRequest()
    xhr.open('GET', url)

    if (cc.sys.isNative) { xhr.setRequestHeader('Accept-Encoding', 'gzip,deflate') }

    let isRaw = false
    if (options && options.responseType) {
        isRaw = true
        xhr.responseType = options.responseType
        delete options.responseType
    }

    for (let k in options) {
        if (options.hasOwnProperty(k)) { xhr.setRequestHeader(k, options[k]) }
    }

    xhr.onreadystatechange = function () { // 功能位置, 像是牌友群中的功能
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                try {
                    if (isRaw) { cbSucc(new Uint8Array(xhr.response)) } else { cbSucc(xhr.responseText) }
                } catch (e) {
                    if (typeof e === 'string') {
                        cc.log(e)
                    } else {
                        let arr = e.stack.match(/(src\/\w+.js:[0-9]*)|(<.\w+@)/g)
                        if (arr) {
                            cc.log(arr[0] + '\n' + arr[1] + '\nmessage=' + e.message)
                        } else {
                            cc.log('' + e.stack + e.message)
                        }
                    }
                }
            } else {
                if (!flag) {
                    flag = true
                    cbFail(xhr.statusText, xhr.responseText)
                }
            }
        }
    }
    xhr.onerror = function () {
        if (!flag) {
            flag = true
            cbFail(xhr.status, null)
        }
    }
    xhr.send()
}

/**
 * Return the time span between Two Date in days.
 */
global.getSpanInDay = function (toDate, fromDate) {
    let time = new Date(toDate - fromDate)
    let diffDays = time / 1000 / 60 / 60 / 24
    return diffDays
}

/**
 * 开始时间
 */
global.Tools = { titleList: {} }
global.debugStartTime = function (title) {
    global.Tools.titleList[title] = new Date().getTime()
    cc.log('---->开始加载 【' + title + '】.....')
}

/**
 * 结束时间
 */
global.debugEndTime = function (title) {
    let time = new Date().getTime()
    time = time - global.Tools.titleList[title]
    cc.log('---->加载 【' + title + '】结束，操作耗时:' + (time) + 'ms')
}

/**
 * 统计当前 textureCache的信息,找到可以优化的点, 减少drawCall
 * cc.spriteFrameCache.removeUnusedSpriteFrames()
 * cc.textureCache.removeUnusedTextures()
 */
global.showTextureCache = function () {
    if (cc.sys.isMobile) {
        return
    }
    // '/cc_fps_images' rc=4   id=15 999 x 54 @ 16 bpp => 105
    // 纹理的名字，       引用数，id，  大小      像素的bit值，内存使用情况(KB)
    // arr1= arr1.length=12
    // arr1= arr1="/cc_fps_images",rc=4,id=15,999,x,54,@,16,bpp,=>,105,
    console.log('showTextureCache 开始')
    let newArr = []
    let texrues = cc.textureCache.getCachedTextureInfo()
    let a = texrues.split('KB\n') // [ab, c, de]
    for (let i = 0; i < a.length - 1; i++) {
        let arr1 = a[i].split(' ')
        let obj = {}
        obj.size = arr1[10]
        obj.sizeM = '' + (arr1[10] / 1024).toFixed(3) + 'MB'
        obj.rc = arr1[1]
        obj.id = arr1[2]
        obj.wx = '' + arr1[3] + 'X' + arr1[5]
        obj.bpp = arr1[7] + 'bpp'
        obj.url = arr1[0]
        let w = obj.url.indexOf('/res/')
        if (w !== -1) {
            obj.url = obj.url.substr(w)
        }
        let w1 = obj.url.indexOf('/_cache/')
        if (w1 !== -1) {
            obj.url = obj.url.substr(w1)
        }

        newArr.push(obj)
    }
    newArr.sort(function (a, b) {
        return b.size - a.size
    })
    newArr.push(a[a.length - 1])
    for (let i = 0; i < newArr.length; i++) {
        console.log('' + JSON.stringify(newArr[i]))
    }
    console.log('showTextureCache 结束 ' + a[a.length - 1])
}

// 深度拷备
global.deepClone = function (sObj) {
    if (typeof (sObj) !== 'object') {
        return sObj
    }

    let s = {}
    if (sObj.constructor === Array) {
        s = []
    }

    for (let i in sObj) {
        s[i] = global.deepClone(sObj[i])
    }
    return s
}

// 把一个对象去除所有key 转换成数组的格式
global.removeKeyToArray = function (sObj) {
    if (typeof (sObj) !== 'object') {
        return [sObj]
    }

    if (sObj.constructor === Array) {
        let temp = []
        for (let i = 0; i < sObj.length; i++) {
            if (typeof (sObj) !== 'object') {
                temp.push(sObj[i])
            } else {
                temp.push(global.removeKeyToArray(sObj[i]))
            }
        }
    }

    let arr = []
    for (let i in sObj) {
        if (typeof (sObj[i]) !== 'object') {
            arr.push(sObj[i])
        } else {
            arr.push(global.removeKeyToArray(sObj[i]))
        }
    }
    return arr
}

// 把orgData的数据覆盖到tarData中，有相同属性则只修改值，tarData中没有orgData中对应属性则新生成一个同名同值属性
global.overlapData = function (tarData, orgData) {
    if (orgData) {
        if (!tarData) tarData = {}
        for (let k in orgData) {
            tarData[k] = orgData[k]
        }
    }
}

/*
 * 把orgData的数据覆盖到tarData中，只覆盖tarData和orgData共同拥有的属性，
 */
global.overlapModelData = function (tarData, orgData) {
    if (tarData && orgData) {
        for (let key in orgData) {
            if (key in tarData) {
                tarData[key] = orgData[key]
            }
        }
    }
}

/**
 *  根据path 获取对应的节点
 * @param path ['a/b']
 * @param nodeCache 会用来缓存节点提高查找效率 但是用完记得自己释放
 */
global.getChildByPath = function (rootNode, path, nodeCache) {
    /**
     * 根据数组去寻找节点
     * @param root
     * @param array
     * @returns {*}
     */
    let getChildByArray = function (root, array) {
        if (!root || !array) {
            return null
        }
        let node = root
        let cacheKey = ''
        for (let i = 0; i < array.length; i++) {
            let name = array[i]
            node = node.getChildByName(name)
            cacheKey = cacheKey + '/' + name
            if (!node) {
                cc.error(cc.formatStr('ERROR: node %s not find! ', cacheKey))
                return null
            }
            if (nodeCache) {
                nodeCache[cacheKey] = node
            }
        }
        return node
    }
    /**
     * 依赖缓存查找节点
     * @param path
     */
    let findNodeFromCache = function (root, subPath) {
        let pathArray = path.split('/')
        let nodeName = pathArray.pop()
        if (pathArray.length > 0) {
            let cacheKey = pathArray.join('/')
            let node = null
            if (nodeCache[cacheKey]) {
                node = nodeCache[cacheKey].getChildByName(nodeName)
                if (!node) {
                    cc.error(cc.formatStr('ERROR: node %s not find! ', path))
                    return null
                }
                nodeCache[path] = node
                return node
            } else {
                pathArray.push(nodeName)
                return getChildByArray(root, pathArray)
            }
        } else {
            let node = rootNode.getChildByName(nodeName)
            if (!node) {
                cc.error(cc.formatStr('ERROR: node %s not find! ', path))
            }
            return node
        }
    }

    if (!rootNode) { return null }

    if (nodeCache) {
        return findNodeFromCache(rootNode, path)
    } else {
        let pathArray = path.split('/')
        return getChildByArray(rootNode, pathArray)
    }
}

/*
 * 弹出式系统弹窗
 * msg 消息字符串
 * onConfirm 确认回调
 * onCancel 取消回调，注意：如需显示取消按钮必须传入取消回调
 * zorder 显示层级 不传参默认用 9999
 */
global.alertMsg = function (msg, onConfirm, onCancel, zorder, layerFile) {
    zorder = zorder || 9999
    if (!layerFile) {
        layerFile = 'Game/Hall/AlertLayer'
    }
    let AlertLayer = include(layerFile)
    let alertLayer = appInstance.uiManager().createUI(AlertLayer)
    appInstance.sceneManager().getCurScene().addChild(alertLayer, zorder)
    alertLayer.alertMsg(msg, onConfirm, onCancel)
    return alertLayer
}
/*
 * 弹幕消息（显示在屏幕中间，一定时间后自己消失）
 * msg 消息字符串
 * delayTime 显示时长
 * zorder 显示层级(gm.ZorderType) 不传参默认用gm.ZorderType.LayerTop
 */
global.showMsg = function (msg, delayTime, zorder, layerFile) {
    zorder = zorder || 9999
    if (!layerFile) {
        layerFile = 'Game/Hall/ShowMsgLayer'
    }
    let ShowMsgLayer = include(layerFile)
    let showMsgLayer = appInstance.uiManager().createUI(ShowMsgLayer)
    appInstance.sceneManager().getCurScene().addChild(showMsgLayer, zorder)
    showMsgLayer.showMsg(msg, delayTime, zorder)
}

/*
 * 浮动式弹幕消息（显示在屏幕下方，滚动上屏幕中间后自己消失）
 * msg 消息字符串
 * delayTime 显示时长
 * zorder 显示层级
 */
global.showFloatMsg = function (msg, delayTime, zorder, layerFile) {
    zorder = zorder || 9999
    if (!layerFile) {
        layerFile = 'Game/Hall/ShowMsgLayer'
    }
    let ShowMsgLayer = include(layerFile)
    let showMsgLayer = appInstance.uiManager().createUI(ShowMsgLayer)
    appInstance.sceneManager().getCurScene().addChild(showMsgLayer, zorder)
    showMsgLayer.showFloatMsg(msg, delayTime, zorder)
}

/*
 * 字符串缩写(超出showNum字符部分用...表示)
 * showNum: 显示的最大字符数
 */
global.strShrink = function (str, showNum) {
    let num = showNum || 1
    let temp = str
    if (str && str.length > num) {
        temp = str.substring(0, num) + '...'
    }
    return temp
}

/**
 *  根据路径按照顺序命名规则创建动画
 * @param path
 * @param count
 * @param rect
 * @param delayUnits
 * @returns {*}
 */
global.createAnimation = function (path, count, rect, delayUnits) {
    let frames = []
    let delay = delayUnits || 0.25
    for (let tempIndex = 0; tempIndex < count; tempIndex++) {
        let fileName = path + tempIndex + '.png'
        let curRect = rect
        if (rect instanceof Array) {
            curRect = rect[tempIndex]
        }
        let frame = new cc.SpriteFrame(fileName, curRect)
        frames.push(frame)
    }
    let animation = new cc.Animation(frames, delay)
    return new cc.Animate(animation)
}

global.getRad = function (d) {
    return d * Math.PI / 180.0
}
global.EARTH_RADIUS = 6378137.0


/**
 * 比较版本号
 * @param ver1
 * @param ver2
 * @returns {number}
 */
global.compareVersion = function (ver1, ver2) {
    if (ver1 === ver2) {
        return 0
    }
    let array1 = ver1.split('.')
    let array2 = ver2.split('.')
    let arrayLength = array1.length > array2.length ? array2.length : array1.length
    let value1 = 0
    let value2 = 0
    for (let i = 0; i < arrayLength; i++) {
        value1 = parseInt(array1[i], 10)
        if (!value1) {
            value1 = 0
        }
        value2 = parseInt(array2[i], 10)
        if (!value2) {
            value2 = 0
        }
        if (value1 === value2) {
            if (i === arrayLength - 1) {
                if (array1.length === array2.length) {
                    return 0
                } else {
                    return array1.length < array2.length ? -1 : 1
                }
            } else {
                continue
            }
        } else if (value1 < value2) {
            return -1
        } else {
            return 1
        }
    }
}

/**
 * 比较版本号(安卓比较versionCode而不是versionName)
 * @param ver1
 * @param ver2
 * @returns {number}
 */
global.compareVersionCode = function (ver1, ver2) {
    if (cc.sys.OS_ANDROID === cc.sys.os) { // 目前只支持安卓
        return Number(ver1) - Number(ver2)
    } else {
        return global.compareVersion(ver1, ver2)
    }
}

global.touchSecurity = function (target) {
    target.touchIndex = 0
    let judgeGesture = function (rects, pos) {
        if (cc.rectContainsPoint(rects[target.touchIndex], pos)) {
            cc.log('touch is  in rect !!!!')
            target.touchIndex++
            if (target.touchIndex === rects.length) {
                let str = base64decode('dGhpcyBpcyBsZXdhbiBnYW1lISEh')
                global.alertMsg(str)
                cc.eventManager.removeListener(target.listener)
                target.listener = null
                target.touchIndex = 0
            }
        } else {
            cc.eventManager.removeListener(target.listener)
            target.listener = null
        }
    }
    let winSize = cc.director.getWinSize()
    let rects = [
        cc.rect(0, winSize.height - 100, 100, 100),
        cc.rect(winSize.width - 100, 0, 100, 100),
        cc.rect(0, 0, 100, 100),
        cc.rect(winSize.width - 100, winSize.height - 100, 100, 100)]
    target.listener = cc.EventListener.create({
        event: cc.EventListener.TOUCH_ONE_BY_ONE,
        swallowTouches: false,
        status: null,
        onTouchBegan: function (touch, event) {
            let pos = touch.getLocation()
            judgeGesture(rects, pos)
            return true
        }
    })
    cc.eventManager.addListener(target.listener, target)
}

