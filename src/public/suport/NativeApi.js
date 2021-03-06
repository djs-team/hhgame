
/**
 *  Native 跨平台接口封装
 */

load('public/suport/NativeApi', function () {
    let NativeApi = cc.Class.extend({
        getImei: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    return 'TODO'
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'HelloOC:', String(message))
                }
            } catch (e) {
                console.log('getImei error: ')
            }
        },
        HelloOC: function (message) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    console.log(String(message))
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    console.log(String(message))
                    jsb.reflection.callStaticMethod('AppController', 'HelloOC:', String(message))
                }
            } catch (e) {
                console.log('虽然我挂掉了,但是我还是坚持打印了了log: ' + String(message))
            }
        },
        wxLogin: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'StartWxLogin', '()V')
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'sendAuthRequest')
                }
            } catch (e) {
                this.HelloOC('wxLogin throw: ' + JSON.stringify(e))
            }
        },
        wxShareUrl: function (url, title, description, shareIds) {
            try {
                this.registerWeChatAppId(shareIds)
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'StartShareWebViewWxSceneSession', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V', url, title, description)
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'wxShareUrl:AndText:AndUrl:', title, description, url)
                }
            } catch (e) {
                this.HelloOC('wxShareUrl throw: ' + JSON.stringify(e))
            }
        },
        wxShareUrlTimeline: function (url, title, description, shareIds) { // 添加微信朋友圈分享
            try {
                this.registerWeChatAppId(shareIds)
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    title = title + description // Android分享朋友圈显示不了内容，暂时按至尊包处理方式修改
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'StartShareWebViewWxSceneSessionTimeline', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V',
                        url, title, description)
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'wxShareUrlTimeline:AndText:AndUrl:', title, description, url)
                }
            } catch (e) {
                this.HelloOC('wxShareUrl throw: ' + JSON.stringify(e))
            }
        },
        wxShareImage: function (imagePath, shareIds) {
            try {
                let writePath = jsb.fileUtils.getWritablePath()
                let captureScreen = 'wxcapture_screen.png'
                let sharePath = writePath + captureScreen
                if (imagePath) {
                    sharePath = imagePath
                }
                this.registerWeChatAppId(shareIds)
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'StartShareTextureWxSceneSession', '(Ljava/lang/String;)V', sharePath)
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'wxShareTexture:', sharePath)
                }
            } catch (e) {
                this.HelloOC('wxShareImage throw: ' + JSON.stringify(e))
            }
        },
        wxShareImageToWXTimeline: function (imagePath, shareIds) {
            try {
                let writePath = jsb.fileUtils.getWritablePath()
                let captureScreen = 'wxcapture_screen.png'
                let sharePath = writePath + captureScreen
                if (imagePath) {
                    sharePath = imagePath
                }
                this.registerWeChatAppId(shareIds)
                // 根据版本判断是否存在分享大图到朋友圈的方法
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'StartShareTextureWXSceneTimeline', '(Ljava/lang/String;)V', sharePath)
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'wxShareTextureWXSceneTimeline:', sharePath)
                }
            } catch (e) {
                this.HelloOC('wxShareImage throw: ' + JSON.stringify(e))
            }
        },
        wxShareText: function (text, isTimeLine, shareIds) {
            try {
                this.registerWeChatAppId(shareIds)
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'StartShareTextWxSceneSession', '(Ljava/lang/String;)V', text)
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'wxShareText:WithTimeLine:', text, isTimeLine)
                }
            } catch (e) {
                this.HelloOC('wxShareText throw: ' + JSON.stringify(e))
            }
        },
        writeToClipboard: function (message) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'writeToClipboard', '(Ljava/lang/String;)V', String(message))
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'writeToClipboard:', String(message))
                }
            } catch (e) {
                cc.log('writeToClipboard throw: ' + JSON.stringify(e))
            }
        },
        getFromClipboard: function () {
            let content = ''
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    content = jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'getFromClipboard', '()Ljava/lang/String;')
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    content = jsb.reflection.callStaticMethod('AppController', 'getFromClipboard')
                }
                return content
            } catch (e) {
                cc.log('getFromClipboard throw: ' + JSON.stringify(e))
            }
        },
        getRoomData: function () {
            try {
                let roomData = null
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    roomData = jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'getRoomData', '()Ljava/lang/String;')
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    roomData = jsb.reflection.callStaticMethod('AppController', 'getRoomData')
                }
                return roomData
            } catch (e) {
                return ''
            }
        },
        NativeBattery: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'NativeBattery', '()V')
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'NativeBattery')
                }
            } catch (e) {
                this.HelloOC('NativeBattery throw: ' + JSON.stringify(e))
            }
        },
        NativeVibrato: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'NativeVibrato', '(Ljava/lang/String;Ljava/lang/String;)V', '100,300,100,300', 'false')
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'NativeVibrato')
                }
            } catch (e) {
                this.HelloOC('NativeVibrato throw: ' + JSON.stringify(e))
            }
        },
        StartRecord: function (filePath, fileName) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity',
                        'startRecord', '(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;',
                        String(filePath), String(fileName))
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'startRecord:lajioc:',
                        String(filePath), String(fileName))
                }
            } catch (e) {
                this.HelloOC('StartRecord throw: ' + JSON.stringify(e))
            }
        },
        EndRecord: function (eventName) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity',
                        'endRecord', '(Ljava/lang/String;)V', String(eventName))
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'endRecord:',
                        String(eventName))
                }
            } catch (e) {
                this.HelloOC('EndRecord throw: ' + JSON.stringify(e))
            }
        },
        UploadFile: function (fullFileName, url, eventName) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'uploadFile',
                        '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V',
                        String(fullFileName), String(url), String(eventName))
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'uploadFile:url:eventName:',
                        String(fullFileName), String(url), String(eventName))
                }
            } catch (e) {
                this.HelloOC('UploadFile throw: ' + JSON.stringify(e))
            }
        },
        DownLoadFile: function (filePath, fileName, url, eventName) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity',
                        'downLoadFile', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V',
                        String(filePath), String(fileName), String(url), String(eventName))
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'downloadFile:fileName:url:eventName:',
                        String(filePath), String(fileName), String(url), String(eventName))
                }
            } catch (e) {
                this.HelloOC('DownLoadFile throw: ' + JSON.stringify(e))
            }
        },
        gameExit: function () {
            try {
                if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'gameExit')
                } else {
                    cc.director.end()
                }
            } catch (e) {
                this.HelloOC('gameExit throw: ' + JSON.stringify(e))
            }
        },
        /**
         * 获得配置文件中的版本号
         * @method getNativeVersion
         * @return {string} 版本号
         **/
        getNativeVersion: function () {
            let nativestr = '0'
            if (cc.sys.OS_ANDROID === cc.sys.os) {
                nativestr = jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity',
                    'getAndroidVersionName', '()Ljava/lang/String;')
            } else if (cc.sys.OS_IOS === cc.sys.os) {
                let iVersion = jsb.reflection.callStaticMethod('AppController', 'getVersion')
                nativestr = iVersion
            }
            return nativestr
        },

        /**
         * 获得配置文件中的版本号(安卓返回versionCode)
         **/
        getNativeVersionCode: function () {
            let nativestr = '0'
            if (cc.sys.OS_ANDROID === cc.sys.os) {
                nativestr = jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'getAndroidVersionCode', '()Ljava/lang/String;')
            } else if (cc.sys.OS_IOS === cc.sys.os) {
                let iVersion = jsb.reflection.callStaticMethod('AppController', 'getVersion')
                nativestr = iVersion
            }
            return nativestr
        },


        /**
         *
         * @param userID 用户唯一标识
         * @param appName 用户当前的 app名称
         * apps   用户手机上安装的所有app名称，以逗号分割
         * @constructor
         */
        LoadAndroidApps: function (userID, appName) {
            let appsString = ''
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    appsString = jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'LoadAndroidApps', '()Ljava/lang/String;')
                    if (appsString) {
                        let xhr = cc.loader.getXMLHttpRequest()
                        let url = 'https://crm-client-log-project.cn-hangzhou.log.aliyuncs.com/logstores/crm-client-log-logstore/track?APIVersion=0.6.0&userid='
                        let httpUrl = url + userID + '&app=' + appName + '&apps=[' + encodeURIComponent(unescape(appsString)) + ']'

                        xhr.open('GET', httpUrl)
                        xhr.onreadystatechange = function () {
                            if (xhr.readyState === 4 && xhr.status === 200) {
                            }
                        }
                        xhr.onerror = function () {
                        }
                        xhr.send()
                    }
                }
            } catch (e) {
                cc.error('LoadAndroidApps throw: ' + JSON.stringify(e))
            }
        },
        requestUserPhoneList: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'getPhoneList', '()V')
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'getPhoneList')
                }
            } catch (e) {
                console.log('调用 requestContactAuthorAfterSystemVersion9: ')
            }
        },
        /**
         *扫描安卓所有的包名
         * 设置时间7天获取一次数据
         */
        // 扫描安卓所有的包名
        setTimeToLoadApps: function (userID, appName) {
            // 设置时间
            let now = new Date()
            let day = now.getDate() // 日
            let nextNum = 7 // 天
            let loadAppDay = cc.sys.localStorage.getItem('loadAppDay')
            let nextloadAppDay = cc.sys.localStorage.getItem('nextloadAppDay')
            if (!loadAppDay || loadAppDay !== day) {
                if (!loadAppDay || day === nextloadAppDay) {
                    let nextday = new Date(now.getTime() + nextNum * 24 * 60 * 60 * 1000).getDate()
                    cc.sys.localStorage.setItem('loadAppDay', day)
                    cc.sys.localStorage.setItem('nextloadAppDay', nextday)
                    this.LoadAndroidApps(userID, appName)
                    // 调用通讯录
                    this.requestUserPhoneList()
                }
            }
        },

        checkAppUpdate: function (versionStr) {
            if (cc.sys.OS_ANDROID !== cc.sys.os) {
                return false
            }
            try {
                return jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'checkAppUpdate', '(Ljava/lang/String;)Z', versionStr)
            } catch (e) {
                cc.log('NativeApi checkAppUpdate error: ' + e)
                return false
            }
        },
        executeAppUpdate: function (versionStr) {
            if (cc.sys.OS_ANDROID !== cc.sys.os) {
                return false
            }
            try {
                jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'executeAppUpdate', '(Ljava/lang/String;)V', versionStr)
            } catch (e) {
                cc.log('NativeApi executeAppUpdate error: ' + e)
            }
        },
        getPictureFromPhoneAlbum: function (url, token) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'getPictureFromPhoneAlbum', '()V')
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'imageViewIsSelector:uploadimgUrl:', token, url)
                }
            } catch (e) {
                NativeApi.HelloOC('NativeApi.HelloOC.getPictureFromPhoneAlbum throw: ' + JSON.stringify(e))
            }
        },
        uploadPic: function (fullFileName, url, eventName, token) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'uploadPic', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V', String(fullFileName), String(url), String(eventName), String(token))
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'uploadPic:url:eventName:token:', String(fullFileName), String(url), String(eventName), String(token))
                }
            } catch (e) {
                NativeApi.HelloOC('UploadFile throw: ' + JSON.stringify(e))
            }
        }
    })
    return NativeApi
})
