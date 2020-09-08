
/**
 * UI Manager
 **/
load('public/manager/UiManager', function () {
    let uiManager = cc.Class.extend({
        ctor: function () {
            this._uiCntr = []
            this.ClassObjectHelper = include('public/manager/ClassObject')
        },
        /**
         * 用于创建UI
         * args 使用arguments 实现参数的解析。默认规定第一个参数一定要是想要创建的UI类对象
         * 其他作为它所需要的参数来用
         */
        createUI: function () {
            if (arguments.length > this.ClassObjectHelper.MAX_ARG_NUMBERS) {
                cc.error('[UIManager] createUI %s  arguments length can not more than %d !!!! ', arguments[0], this.ClassObjectHelper.MAX_ARG_NUMBERS)
                return
            }
            let args = Array.prototype.slice.call(arguments)
            let layerObj = this.ClassObjectHelper.createObjByArgs(args)
            this._addUI(layerObj)
            return layerObj
        },
        /**
         * 用于创建并播放弹出UI 的动画
         * @returns {*}
         * @constructor
         */
        createPopUI: function () {
            if (arguments.length > this.ClassObjectHelper.MAX_ARG_NUMBERS) {
                cc.error('[UIManager] createPopUI %s  arguments length can not more than %d !!!! ', arguments[0], this.ClassObjectHelper.MAX_ARG_NUMBERS)
                return
            }
            let args = Array.prototype.slice.call(arguments)
            let layerObj = this.ClassObjectHelper.createObjByArgs(args)
            layerObj.setCanPopAction(true)
            this._addUI(layerObj)
            return layerObj
        },
        /**
         * 用于创建UI,显示传入class 和 args
         * args 使用arguments 实现参数的解析。默认规定第一个参数一定要是想要创建的UI类对象
         * 其他作为它所需要的参数来用
         */
        createUIByArgsArray: function (ObjProto, args, pop) {
            if (!Array.isArray(args)) {
                cc.error('[UIManager] createUI createUIByArgsArray Api by array, %s in not array  !!!! ' + args)
                return
            }

            if (args.length - 1 > this.ClassObjectHelper.MAX_ARG_NUMBERS) {
                cc.error('[UIManager] createUI %s  arguments length can not more than %d !!!! ', ObjProto, this.ClassObjectHelper.MAX_ARG_NUMBERS)
                return
            }
            let argsTmp = [].concat(ObjProto).concat(args)
            let layerObj = this.ClassObjectHelper.createObjByArgs(argsTmp)
            pop && layerObj.setCanPopAction(true)
            this._addUI(layerObj)
            return layerObj
        },

        /**
         * 用于填充UIManager中的uiCntr
         */
        _addUI: function (uiObj) {
            let len = this._uiCntr.length ? this._uiCntr.length - 1 : 0
            let uiCtr = this._uiCntr[len]
            if (!uiCtr) {
                return
            }
            if (uiObj.setRelationUIMgr) {
                uiObj.setRelationUIMgr(true)
            }
            uiCtr.push(uiObj)
        },
        /**
         * 根据名字获得当前场景中是否已经存在已创建的UI
         * @param name
         */
        getUIByName: function (name) {
            let len = this._uiCntr.length ? this._uiCntr.length - 1 : 0
            let uiCtr = this._uiCntr[len]
            if (uiCtr) {
                for (let i = uiCtr.length - 1; i >= 0; i--) {
                    if (uiCtr[i]._className === name) {
                        return uiCtr[i]
                    }
                }
            }
            return null
        },
        /**
         * 根据UI 对象删除
         * @param obj
         */
        removeUI: function (obj, onlyCleanRelation) {
            let len = this._uiCntr.length ? this._uiCntr.length - 1 : 0
            let uiCtr = this._uiCntr[len]
            if (!uiCtr || !obj) {
                return
            }
            for (let i = uiCtr.length - 1; i >= 0; i--) {
                if (uiCtr[i] && uiCtr[i] === obj) {
                    if (uiCtr[i].setRelationUIMgr) {
                        uiCtr[i].setRelationUIMgr(false)
                    }
                    if (!onlyCleanRelation) {
                        uiCtr[i].removeFromParent(true)
                    }
                    uiCtr[i] = null
                    uiCtr.splice(i, 1)
                    break
                }
            }
        },
        /**
         * 根据UI 的类名来删除
         * @param obj
         */
        removeUIByName: function (name) {
            let len = this._uiCntr.length ? this._uiCntr.length - 1 : 0
            let uiCtr = this._uiCntr[len]
            if (!uiCtr) {
                return
            }
            for (let i = uiCtr.length - 1; i >= 0; i--) {
                if (uiCtr[i] && uiCtr[i]._className === name) {
                    uiCtr[i].removeFromParent(true)
                    uiCtr[i] = null
                    uiCtr.splice(i, 1)
                    continue
                }
            }
        },

        /**
         * 每次新创建一个场景对象实例会对uiManager 的栈数据进行加1更新
         */
        push: function () {
            this._uiCntr.push([])
        },
        /**
         *每次新创建一个场景对象实例销毁时会对uiManager 的栈数据进行减1更新
         */
        pop: function () {
            let uis = this._uiCntr.pop()
            if (uis) {
                uis = null
            }
        },
        /**
         * 每帧去执行
         */
        onUpdate: function (dt) {
            let len = this._uiCntr.length ? this._uiCntr.length - 1 : 0
            let uiCtr = this._uiCntr[len]
            if (!uiCtr) {
                return
            }
            for (let i = 0; i < uiCtr.length; i++) {
                if (uiCtr[i] && uiCtr[i].onUpdate) {
                    uiCtr[i].onUpdate(dt)
                }
            }
        },
        /**
         *   游戏通用系统提示消息
         */
        popSystemMessage: function (msg) {
            if (msg.callback) {
                global.alertMsg(msg.text, msg.callback)
            } else {
                global.showMsg(msg)
            }
        }
    })
    return uiManager
})
