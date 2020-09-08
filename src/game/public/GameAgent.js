
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
        ctor: function () {
            this._httpGame = new HttpGame()
            this._tcpGame = new TcpGame()
            this._gameUtil = GameUtil
            this._mjUtil = new MjUtil()
            this.initGame()
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

        addPopUI: function (resUi) {
            let layer = include(resUi)
            let UI = appInstance.uiManager().createPopUI(layer)
            appInstance.sceneManager().getCurScene().addChild(UI)
        },

        addUI: function (resUi) {
            let layer = include(resUi)
            let UI = appInstance.uiManager().createUI(layer)
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

        addReceivePropsUI: function (data) {
            if (!data) {
                cc.log('error happen when addReceivePropsUI, there need some data for view')
            }
            let layer = include(ResConfig.Ui.ReceivePropsLayer)
            let UI = appInstance.uiManager().createPopUI(layer, data)
            appInstance.sceneManager().getCurScene().addChild(UI)
        },

        Tips: function (text) {
            let SystemTips = include(ResConfig.Ui.SystemTips)
            let TipsUi = appInstance.uiManager().createUI(SystemTips)
            appInstance.sceneManager().getCurScene().addChild(TipsUi)
            TipsUi.runTips(text)
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
                //给服务器三秒的反应时间，三秒还不返回给我 ，那就是服务器挂了
                if (this._heartBeatTimes > GameConfig.HeartBeatInterval + 3) {
                    cc.log(' >>>>>>>>>>心跳异常')
                    this._heartBeatTimes = 0
                    this._loginOk = false
                    let LoginScene = include('game/ui/scene/LoginScene')
                    appInstance.sceneManager().replaceScene(new LoginScene())
                }
            }
        },

        initGame: function () {

        }
    })
    return GameAgent
})