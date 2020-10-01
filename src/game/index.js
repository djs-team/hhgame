
/**
 * app 启动后首先加载的入口文件
 */
load('game/index', function () {
    let GameAgent = include('game/public/GameAgent')
    let publicIndex = include('game/public/index')
    let dataIndex = include('game/data/index')
    let msgHandlerIndex = include('game/msghandler/index')
    let mjDataIndex = include('module/mahjong/data/index')

    let index = {}
    /**
     * 添加搜索路径
     */
    index.addSearchPath = function () {
        let writePath = jsb.fileUtils.getWritablePath() + '/update/'
        jsb.fileUtils.addSearchPath(writePath + 'res')
        jsb.fileUtils.addSearchPath('res')
    }

    /**
     * 初始化游戏子模块的入口
     */
    index.initModuleGame = function () {
        mjDataIndex.run()
    }

    index.initGame = function () {
        appInstance.setGameAgent(new GameAgent())
    }

    index.run = function () {
        appInstance.audioManager().setButtonClickEffect('res/sound/btnClick.mp3')

        this.addSearchPath()
        this.initGame()

        this.initModuleGame()

        publicIndex.run()
        dataIndex.run()
        msgHandlerIndex.register()


        let UpdateScene = include('game/ui/scene/UpdateScene')
        appInstance.sceneManager().pushScene(new UpdateScene())
    }

    index.restart = function () {
        // appInstance.connectorApi()._lastIpAndPort = false
        // global.localStorage.removeItem('lastIpAndPort')

        msgHandlerIndex.unRegister()
        dataIndex.restart()
        publicIndex.restart()

    }
    return index
})
