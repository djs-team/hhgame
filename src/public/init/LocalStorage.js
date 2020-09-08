/**
 * 用于本地存储的接口
 * @type {{}}
 */
global.localStorage = {}

global.localStorage.getBoolForKey = function (key) {
    let value = sys.localStorage.getItem(key)
    if (value === 'true') {
        return true
    } else if (value === 'false') {
        return false
    }
    return value
}

global.localStorage.setBoolForKey = function (key, value) {
    if (typeof (value) === 'boolean') {
        sys.localStorage.setItem(key, value)
    } else {
        cc.error('setBoolForKey TypeError: value type is not boolean!')
    }
}

global.localStorage.getNumberForKey = function (key) {
    let value = sys.localStorage.getItem(key)
    if (value != null) {
        let temp = parseFloat(value)
        if (isNaN(temp)) return 0
        else return temp
    }
    return value
}

global.localStorage.setNumberForKey = function (key, value) {
    if (typeof (value) === 'number') {
        sys.localStorage.setItem(key, value)
    } else {
        cc.error('setBoolForKey TypeError: value type is not number!')
    }
}

global.localStorage.getIntKey = function (key) {
    let value = sys.localStorage.getItem(key)
    if (value != null) {
        let temp = parseInt(value)
        if (isNaN(temp)) return 0
        else return temp
    }
    return value
}
global.localStorage.setIntKey = function (key, value) {
    if (typeof (value) === 'number') {
        sys.localStorage.setItem(key, value)
    } else {
        cc.error('setBoolForKey TypeError: value type is not number!')
    }
}

global.localStorage.getStringForKey = function (key) {
    let value = sys.localStorage.getItem(key)
    return value
}

global.localStorage.setStringForKey = function (key, value) {
    if (typeof (value) === 'string') {
        sys.localStorage.setItem(key, value)
    } else {
        cc.error('setBoolForKey TypeError: value type is not string!')
    }
}

global.localStorage.getDataForKey = function (key) {
    let value = sys.localStorage.getItem(key)
    if (typeof (value) === 'string') {
        return JSON.parse(value)
    }
    return value
}

global.localStorage.setDataForKey = function (key, value) {
    if (typeof (value) === 'object') {
        sys.localStorage.setItem(key, JSON.stringify(value))
    } else {
        cc.error('setBoolForKey TypeError: value type is not object!')
    }
}

global.localStorage.removeItem = function (key) {
    cc.sys.localStorage.removeItem(key)
}
