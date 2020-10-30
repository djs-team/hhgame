/**
 *  Native 跨平台接口封装
 */

load('public/suport/NativeApi', function () {
    let AppConfig = include('game/public/AppConfig')

    let NativeApi = cc.Class.extend({
        getImei: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    return 'TODO'
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    return jsb.reflection.callStaticMethod('AppController', 'getImei')
                }
            } catch (e) {
                console.log('getImei error: ')
            }
        },
        // 获取当前连接网络
        getNetWorkStates: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    return jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'getPhoneInfo', '(Ljava/lang/String;)Ljava/lang/String;', "netState")

                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    return jsb.reflection.callStaticMethod('AppController', 'getNetWorkStates')
                }
            } catch (e) {
                console.log('getNetWorkStates error: ')
            }
        },
        // 获取网络信号
        getSignalStrength: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    return jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'getPhoneInfo', '(Ljava/lang/String;)Ljava/lang/String;', "netInfo")
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    return jsb.reflection.callStaticMethod('AppController', 'getSignalStrength')
                }
            } catch (e) {
                console.log('getSignalStrength error: ')
            }
        },
        // 获取电量
        getBatteryLevel: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    return jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'getPhoneInfo', '(Ljava/lang/String;)Ljava/lang/String;', "batter")
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    return jsb.reflection.callStaticMethod('AppController', 'getBatteryLevel')
                }
            } catch (e) {
                console.log('getBatteryLevel error: ')
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
        thirdPay: function (type, message) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    console.log('thirdPay-start')
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'thirdPay', '(Ljava/lang/String;Ljava/lang/String;)V', type, message)
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'appPurchaseWithPayType:payParam:userID:orderNo:paySuccessMethod:', type, message, "", "", "ThirdPayCallback")
                }
            } catch (e) {
                console.log('虽然我挂掉了,但是我还是坚持打印了了log: ' + String(e))
            }
        },
        // 苹果支付
        applyPay: function (message, orderNo) {
            try {
                if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'appPurchaseWithPayType:payParam:userID:orderNo:paySuccessMethod:', "ios", message, "", orderNo, "ApplePayCallback")
                    // jsb.reflection.callStaticMethod('AppController', 'appPurchaseWithPayType:payParam:userID:paySuccessMethod:', type, message, "", "ThirdPayCallback")
                }
            } catch (e) {
                console.log('虽然我挂掉了,但是我还是坚持打印了了log: ' + String(e))
            }
        },
        wxLogin: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'login', '(Ljava/lang/String;)V', "wx")
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'weChatLoginWithMethod:', "THIRD_LOGIN_RESULT")
                }
            } catch (e) {
                this.HelloOC('wxLogin throw: ' + JSON.stringify(e))
            }
        },
        oneClickLogin: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'login', '(Ljava/lang/String;)V', "jpush")
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'JPushLoginWithMethod:showPhoneAlert:', "THIRD_LOGIN_RESULT", AppConfig.loginShowPhoneAlert)
                }
            } catch (e) {
                this.HelloOC('oneCLickLogin throw: ' + JSON.stringify(e))
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
        },
        //跳转相亲视频
        jumpToBlindDate: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    let userInfo = {};
                    userInfo.username = appInstance.dataManager().getUserData().account
                    userInfo.nickname = appInstance.dataManager().getUserData().pname
                    userInfo.avatar = appInstance.dataManager().getUserData().sdkphotourl
                    userInfo.platform = appInstance.dataManager().getUserData().platform
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'jumpToBlindDate', '(Ljava/lang/String;)V', JSON.stringify(userInfo))
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    let userInfo = {};
                    userInfo.applePayType = AppConfig.applePayType
                    userInfo.token = appInstance.dataManager().getUserData().key
                    userInfo.platform = appInstance.dataManager().getUserData().platform
                    userInfo.nickname = appInstance.dataManager().getUserData().pname
                    userInfo.username = appInstance.dataManager().getUserData().account
                    userInfo.avatar = appInstance.dataManager().getUserData().sdkphotourl

                    jsb.reflection.callStaticMethod('AppController', 'enterLiveBroadcastWithToken:', JSON.stringify(userInfo))
                }
            } catch (e) {
                NativeApi.HelloOC('UploadFile throw: ' + JSON.stringify(e))
            }
        },
        //分享图片------  WEIXIN, 微信  WEIXIN_CIRCLE 微信朋友圈
        shareImage: function (platform, fileName) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'shareImage', '(Ljava/lang/String;Ljava/lang/String;)V', platform, fileName)
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    let writePath = jsb.fileUtils.getWritablePath()
                    let sharePath = writePath + fileName
                    jsb.reflection.callStaticMethod('AppController', 'WXShareIOSforImage:platform:', sharePath, platform)
                }

                appInstance.gameAgent().httpGame().sharedCompleteTaskReq();
            } catch (e) {
                NativeApi.HelloOC('shareImage throw: ' + JSON.stringify(e))
            }
        },
        //分享文章------  WEIXIN, 微信  WEIXIN_CIRCLE 微信朋友圈
        shareArticle: function (platform, title, description, url, thumbUrl) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'shareArticle', '(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V', platform, title, description, url, thumbUrl)
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    let writePath = jsb.fileUtils.getWritablePath()
                    let sharePath = writePath + thumbUrl
                    jsb.reflection.callStaticMethod('AppController', 'WXShareIOSforUrl:Title:Desc:image:platform:', url, title, description, sharePath, platform)
                }

                appInstance.gameAgent().httpGame().sharedCompleteTaskReq();
            } catch (e) {
                NativeApi.HelloOC('shareArticle throw: ' + JSON.stringify(e))
            }
        },
        //激励视频
        showRewardVideo: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    let uid = appInstance.dataManager().getUserData().pid
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'showRewardVideo', '(Ljava/lang/String;)V', uid)
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    let uid = appInstance.dataManager().getUserData().pid
                    jsb.reflection.callStaticMethod('AppController', 'openBUAdRewardWithUserId:method:', uid, "rewardVideoCallback")
                }
            } catch (e) {
                NativeApi.HelloOC('showRewardVideo throw: ' + JSON.stringify(e))
            }
        },
        //复制
        copy: function (msg) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'copyStr', '(Ljava/lang/String;)Ljava/lang/String;', msg)
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'copyToPasteboard:', msg)
                }
            } catch (e) {
                NativeApi.HelloOC('copy throw: ' + JSON.stringify(e))
            }
        },
        //获取二维码
        getInvitationCode: function (msg) {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    let uid = appInstance.dataManager().getUserData().pid
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'getInvitationCode', '(Ljava/lang/String;Ljava/lang/String;)V', msg, uid)
                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    jsb.reflection.callStaticMethod('AppController', 'createQRCodeImageWithString:method:', msg, "inviteCodeCallback")
                }
            } catch (e) {
                NativeApi.HelloOC('getInvitationCode throw: ' + JSON.stringify(e))
            }
        },
        //获取安装参数
        getInstallParam: function () {
            try {
                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'getInstallParam', '()V')
                } else if (cc.sys.OS_IOS === cc.sys.os) {

                }
            } catch (e) {
                NativeApi.HelloOC('getInvitationCode throw: ' + JSON.stringify(e))
            }
        },
        //上传图片
        uploadPictureParam: function (msg) {
            try {

                if (cc.sys.OS_ANDROID === cc.sys.os) {
                    let param = JSON.stringify(msg.info)

                    jsb.reflection.callStaticMethod('org.cocos2dx.javascript.AppActivity', 'getPictureFromPhoneAlbum', '(Ljava/lang/String;)V', param)

                } else if (cc.sys.OS_IOS === cc.sys.os) {
                    let param = JSON.stringify(msg)

                    jsb.reflection.callStaticMethod('AppController', 'selectedOnePhotoWithPutParam:method:', param, "PERSONALLAYER_CHANGE_PICTURE")
                }
            } catch (e) {
                NativeApi.HelloOC('uploadPictureParam throw: ' + JSON.stringify(e))
            }
        },
    })
    return NativeApi
})
