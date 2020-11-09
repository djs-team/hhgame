
/**
 * 文件操作的工具函数
 */
global.gameFile = {}

// 创建目录
global.gameFile.createDir = function (dirPath) {
    if (!jsb.fileUtils.isDirectoryExist(dirPath)) {
        jsb.fileUtils.createDirectory(dirPath)
    }
}

// 删除目录
global.gameFile.removeDir = function (dirPath) {
    if (jsb.fileUtils.isDirectoryExist(dirPath)) {
        jsb.fileUtils.removeDirectory(dirPath)
    }
}

// 保存文件
global.gameFile.saveFile = function (data, filePath) {
    return jsb.fileUtils.writeDataToFile(new Uint8Array(data), filePath)
}

// 读取文件
global.gameFile.readFile = function (filePath) {
    return cc.loader.getRes(filePath)
}

/**
 * 删除热更目录
 * @param isM: true 删除子模块热更文件
 */
global.gameFile.delUpdateModule = function () {
    let path = jsb.fileUtils.getWritablePath()
    global.gameFile.removeDir(path + 'update/')
}
/**
 * 下载网络文件
 * @param url 下载的地址
 * @param okFunc 成功回调
 * @param errFunc 失败回调
 */
global.gameFile.downloadFile = function (url, okFunc, errFunc) {
    try {
        let xhr = cc.loader.getXMLHttpRequest()
        xhr.onreadystatechange = function () { // 下载文件
            if (xhr.readyState === 4 && xhr.status === 200) {
                try {
                    xhr.responseType = 'arraybuffer'
                    if (okFunc) {
                        okFunc(xhr.response)
                    }
                } catch (e) {
                }
            } else {
                if (errFunc) {
                    errFunc()
                }
            }
        }
        xhr.onerror = function () {
            if (errFunc) {
                errFunc()
            }
        }
        xhr.open('GET', url, true)
        xhr.send()
    } catch (e) {

    }
}
