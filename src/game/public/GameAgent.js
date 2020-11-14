
/*

let dialogData = {}
dialogData.ViewType = 1
dialogData.TileName = '测试窗口'
dialogData.CloseBtn = function () {
    cc.log('==========close click=====')
}
dialogData.SayText = '测试窗口应该要说些什么呢？'
dialogData.LeftBtnName = '左侧按钮'
dialogData.RightBtnName = '右侧按钮'
dialogData.LeftBtnClick = function () {
    cc.log('====================LeftBtnClick--======')
}
appInstance.gameAgent().addDialogUI(dialogData)
*/
/**
 * 游戏内实例的接口
 */
load('game/public/GameAgent', function () {
    let GameConfig = include('game/config/GameConfig')
    let GameEvent = include('game/config/GameEvent')
    let HttpGame = include('game/public/HttpGame')
    let TcpGame = include('game/public/TcpGame')
    let GameUtil = include('game/public/GameUtil')
    let MjUtil = include('module/mahjong/common/MjUtil')
    let ResConfig = include('game/config/ResConfig')
    let GameAgent  = cc.Class.extend({
        _httpGame: null,
        _tcpGame: null,
        _gameUtil: null,
        _mjUtil: null,
        _heartBeatTimes: 0,
        _sendHearBeat: 0,
        _loginOk: false,
        _reConectTimes: 0,
        ctor: function () {
            this._httpGame = new HttpGame()
            this._tcpGame = new TcpGame()
            this._gameUtil = GameUtil
            this._mjUtil = new MjUtil()
            this.initGame()
        },

        initGame: function () {
            let LoadingLayer = include('game/ui/public/LoadingLayer')
            this._loadingLayer = appInstance.uiManager().createUI(LoadingLayer)
            this._loadingLayer.retain()

        },

        showLoading: function (info) {
            let loadingLayer = appInstance.gameAgent().getLoading()
            let parent = loadingLayer.getParent()
            let curScene = appInstance.sceneManager().getCurScene()

            if (!parent) {
                curScene.addChild(loadingLayer)
            } else if (parent !== curScene) {
                loadingLayer.removeFromParent()
                curScene.addChild(loadingLayer)
            } else {
                loadingLayer.setVisible(true)
            }
            loadingLayer.setLocalZOrder(10000)
            loadingLayer.setVisible(true)
            loadingLayer.updateView(info)
        },

        hideLoading: function () {
            if (this._loadingLayer && cc.sys.isObjectValid(this._loadingLayer)) {
                this._loadingLayer.setVisible(false)
            }
        },

        getLoading: function () {
            return this._loadingLayer
        },

        onTcpClose: function () {
            appInstance.gameAgent().setLoginOk(false)

            let curSceneName = appInstance.sceneManager().getCurSceneName()
            if (curSceneName === 'HallScene' || curSceneName === 'MjPlayScene') {
                // appInstance.gameAgent().Tips('网络连接中断')
                // appInstance.sendNotification(GameEvent.DIALOG_HIDE_ALL)
                // let dialogMsg = {
                //     ViewType: 1,
                //     SayText: '网络异常正在重新连接，请稍等 '
                // }
                // appInstance.gameAgent().addDialogUI(dialogMsg)
                appInstance.gameAgent().showLoading()

                appInstance.gameNet().setReconnect(true)
            }
        },

        delayCall: function (delayTime, callFun, info, obj) {
            appInstance.sceneManager().getCurScene().runAction(cc.sequence((cc.DelayTime(delayTime), cc.callFunc(callFun.apply(obj, [info])))))
        },

        onReconnectError: function () {
            appInstance.gameAgent().goLoginScene()
        },

        httpGame: function () {
            return this._httpGame
        },

        tcpGame: function () {
            return this._tcpGame
        },

        gameUtil: function () {
            return this._gameUtil
        },

        mjUtil: function () {
            return this._mjUtil
        },

        saveCanvas: function () {
            let size = cc.director.getWinSize();
            let fileName = "result_share.jpg";
            let fullPath = jsb.fileUtils.getWritablePath() + fileName; //拿到可写路径，将图片保存在本地，可以在ios端或者java端读取该文件
            if (jsb.fileUtils.isFileExist(fullPath)) {
                jsb.fileUtils.removeFile(fullPath);
            }
            let texture = new cc.RenderTexture(Math.floor(size.width), Math.floor(size.height));
            texture.setPosition(cc.p(size.width / 2, size.height / 2));
            texture.begin();
            cc.director.getRunningScene().visit(); //这里可以设置要截图的节点，设置后只会截取指定节点和其子节点
            texture.end();
            texture.saveToFile(fileName, cc.IMAGE_FORMAT_JPG);
        },

        addPopUI: function (resUi, data) {
            let layer = include(resUi)
            let UI = appInstance.uiManager().createPopUI(layer, data)
            appInstance.sceneManager().getCurScene().addChild(UI)
        },

        addUI: function (resUi, data) {
            let layer = include(resUi)
            let UI = appInstance.uiManager().createUI(layer, data)
            appInstance.sceneManager().getCurScene().addChild(UI)
        },

        addDialogUI: function (data) {
            if (!data) {
                cc.log('error happen when addDialogUI, there need some data for view')
            }
            let layer = include(ResConfig.Ui.DialogLayer)
            let UI = appInstance.uiManager().createPopUI(layer, data)
            appInstance.sceneManager().getCurScene().addChild(UI)
        },

        addDialogUINoPop: function (data) {
            if (!data) {
                cc.log('error happen when addDialogUI, there need some data for view')
            }
            let layer = include(ResConfig.Ui.DialogLayer)
            let UI = appInstance.uiManager().createUI(layer, data)
            appInstance.sceneManager().getCurScene().addChild(UI)
        },


        addReceivePropsUI: function (data) {
            if (!data) {
                cc.log('error happen when addReceivePropsUI, there need some data for view')
            }
            let layer = include(ResConfig.Ui.ReceivePropsLayer)
            let UI = appInstance.uiManager().createPopUI(layer, data)
            appInstance.sceneManager().getCurScene().addChild(UI)
        },

        Tips: function (text, isTurn) {
            let SystemTips = include(ResConfig.Ui.SystemTips)
            let TipsUi = appInstance.uiManager().createUI(SystemTips)
            appInstance.sceneManager().getCurScene().addChild(TipsUi)
            TipsUi.runTips(text, isTurn)
        },

        goLoginScene: function (data) {
            let LoginScene = include('game/ui/scene/LoginScene')
            appInstance.sceneManager().replaceScene(new LoginScene(data))
            appInstance.gameAgent().setLoginOk(false)
            TcpClient.disconnect()
        },

        reSetHeartBeatTimes: function () {
            this._heartBeatTimes = 0
        },

        setLoginOk: function (tag) {
            this._loginOk = tag

        },

        onUpdate: function (dt) {
            // 这里是心跳
            if (this._loginOk) {

                this._heartBeatTimes += dt
                this._sendHearBeat += dt
                if (this._sendHearBeat > GameConfig.HeartBeatInterval) {
                    this._sendHearBeat = 0
                    this._tcpGame.heartBeat()
                }
                //给5秒延迟，五秒后进行tcp重新链接
                if (this._heartBeatTimes > GameConfig.HeartBeatInterval + 5) {
                    // appInstance.gameAgent().Tips('网络连接中断，尝试重新链接')
                    // this._heartBeatTimes = 0
                    // this._loginOk = false
                    //
                    // let dialogMsg = {
                    //     ViewType: 1,
                    //     SayText: '网络异常,正在重新连接，请稍等 '
                    // }
                    // appInstance.gameAgent().addDialogUI(dialogMsg)
                    // appInstance.gameNet().setReconnect(true)
                }
            }
        }
    })
    return GameAgent
})