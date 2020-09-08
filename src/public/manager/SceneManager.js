/**
 * Scene Manager
 **/
load('public/manager/SceneManager', function () {
    let SceneManager = cc.Class.extend({
        childCache: [],
        changeSceneing: false,
        _sceneStack: [],
        ctor: function () {
            this._sceneStack = []
        },
        /**
         * 用于获得当前的场景
         */
        getCurScene: function () {
            if (this._sceneStack.length > 0) {
                return this._sceneStack[this._sceneStack.length - 1]
            }
        },
        /**
         * 用于获得当前的场景的名称
         */
        getCurSceneName: function () {
            let name = ''
            let curScene = this.getCurScene()
            if (curScene) {
                name = curScene._className
            }
            return name
        },
        /**
         * 往栈内压入一个场景对象
         */
        pushScene: function (sceneObj) {
            if (sceneObj) {
                this._sceneStack.push(sceneObj)
                appInstance.uiManager().push()
                cc.director.pushScene(sceneObj)
            }
        },
        /**
         * 替换场景
         * @param sceneObj
         */
        replaceScene: function (sceneObj) {
            if (sceneObj) {
                if (this._sceneStack.length > 0) {
                    this._sceneStack[this._sceneStack.length - 1] = sceneObj
                } else {
                    this._sceneStack[0] = sceneObj
                }
                appInstance.uiManager().pop()
                appInstance.uiManager().push()
                cc.director.replaceScene(sceneObj)
            }
        },
        /**
         * 从栈顶删除一个场景
         */
        popScene: function () {
            if (this._sceneStack.length > 1) {
                cc.director.popScene()
                this._sceneStack.pop()
                appInstance.uiManager().pop()
            }
        },
        /**
         * 游戏每帧更新的结构
         * @param dt
         */
        onUpdate: function (dt) {
            let curScene = this.getCurScene()
            if (curScene && curScene.onUpdate) {
                curScene.onUpdate(dt)
            }
        }
    })

    return SceneManager
})
