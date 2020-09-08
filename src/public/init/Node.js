/**
 * 对于Node 做一些扩展
 * @private
 */

/**
 * 实现自己以及自己子节点可以继承设置的shader 的接口
 */
cc.Node.prototype.setGLProgramStateEX = function (glProgramState, isRecursive) {
    if (isRecursive) {
        let recursiveSetGlProgramState = function (node) {
            if (node && node.getChildren().length > 0) {
                for (let i = 0; i < node.getChildren().length; i++) {
                    let child = node.getChildren()[i]
                    if (child && child.setGLProgramState) {
                        child.setGLProgramState(glProgramState)
                        recursiveSetGlProgramState(child)
                    }
                }
            }
        }
        recursiveSetGlProgramState(this)
    } else {
        this.setGLProgramState(glProgramState)
    }
}

/**
 * 根据传入的资源进行初始化
 */
cc.Node.prototype.initByRes = function (res) {
    if (res && appInstance.resManager()) {
        if (this.LOAD_RES_CONFIG) {
            appInstance.resManager().addResConfig(this.LOAD_RES_CONFIG.value)
        }
        this.addChild(this.loadRes(res))
        if (this.RES_BINDING) {
            this.registerResBinding(this.RES_BINDING())
        }
    }
}

/**
 * 用于加载ui资源
 */
cc.Node.prototype.loadRes = function (resFile, path) {
    this.rootNode = null
    if (resFile.indexOf('.csb') > 0) {
        this.rootNode = ccs.CSLoader.loadCSB(resFile)
    } else {
        this.rootNode = ccs.load(resFile, path).node
    }
    return this.rootNode
}

/**
 * 根据path 获取对应的节点
 * @param path ['a/b']
 */
cc.Node.prototype.getChildByPath = function (path) {
    if (!global.getChildByPath) {
        throw Error('utils has not find getChildByPath function!')
    }
    return global.getChildByPath(this.rootNode, path)
}

/**
 * 用于根据具体的配置文件来绑定控件的响应事件
 * @param bindingCfg
 * bindingCfg={
 * ['a/b'] = {
 *  name:''，
 *  events:{
 *    {event: 'click',func:'onClick'}
 *  }
 * }
 * }
 *
 */
cc.Node.prototype.registerResBinding = function (bindingCfg) {
    for (let key in bindingCfg) {
        let value = bindingCfg[key]
        let node = this.getChildByPath(key)
        if (!node) {
            throw Error(cc.formatStr('[%s] node [%s] is not find!', this._className, key))
            return
        }

        if (value.name) {
            this[value.name] = node
        } else {
            let pathArray = key.split('/')
            let name = pathArray[pathArray.length - 1]
            this[name] = node
        }

        if(value.clickEffectRes){         // 增加点击音效的配置
            node.clickEffectRes = value.clickEffectRes
        }

        if (value.onClicked && typeof value.onClicked === 'function') {
            if (value.likeButton) { // 加入这个字段的imgae 会跟button 一样点击的时候播放缩放动画
                node.likeButton = value.likeButton
            }

            if (value.effectClick) { // 加入这个字段的imgae 会跟button 一样点击的时候播放缩放动画
                node.effectClick = value.effectClick
            }

            node.onClicked(value.onClicked, this)
        }

        if (value.onTouched && typeof value.onTouched === 'function') {
            node.onTouched(value.onTouched, this)
        }

        if (value.onSlider && typeof value.onSlider === 'function') {
            node.onSlider(value.onSlider, this)
        }

        if (value.onChecked && typeof value.onChecked === 'function') {
            node.onChecked(value.onChecked, this)
        }

        if (value.onListener && typeof value.onListener === 'function') {
            node.onListener(value.onListener, this)
        }
    }
}


/**
 *
 * 对于widget事件注册做一个简单的封装方便后面做统一的扩展以及处理
 * @private
 */

ccui.Widget.prototype.onTouched = function (callBack, obj) {
    this.addTouchEventListener(function (sender, eventType) {
        if (eventType === ccui.Widget.TOUCH_BEGAN) {
            if (sender.getDescription() === 'Button') {
                if (appInstance && appInstance.audioManager().getButtonClickEffectSwitch()) {
                    let effect  = sender.clickEffectRes ||  appInstance.audioManager().getButtonClickEffect()
                    appInstance.audioManager().playEffect(effect)
                }
            }
            callBack.call(obj, sender, eventType)
        }
    }, obj)
}

ccui.Widget.prototype.onClicked = function (callBack, obj) {
    this.addTouchEventListener(function (sender, eventType) {
        if (eventType === ccui.Widget.TOUCH_BEGAN) {
            if (sender.getDescription() === 'Button') {
                if (appInstance && appInstance.audioManager().getButtonClickEffectSwitch()) {
                    let effect  = sender.clickEffectRes || appInstance.audioManager().getButtonClickEffect()
                    appInstance.audioManager().playEffect(effect)
                }
            }
        } else if (eventType === ccui.Widget.TOUCH_ENDED) {

            if (sender.onClicking) {
                return
            }

            if (sender.effectClick) {
                sender.onClicking = true
                setTimeout(function () {
                    if (cc.sys.isObjectValid(sender)) {
                        sender.onClicking = false
                    }
                }, 200)
            }

            callBack.call(obj, sender, eventType)
        }
    }, obj)
}

ccui.Widget.prototype.onListener = function (callBack, obj) {
    this.addEventListener(function (sender, eventType) {
        callBack.call(obj, sender, eventType)
    })
}

/**
 * 关于Image 点击事件的封装 实现类似于按钮的自动缩放
 */
ccui.ImageView.prototype.onClicked = function (callBack, obj) {
    if (this.likeButton) {
        this.__orgScaleX = this.getScaleX()
        this.__orgScaleY = this.getScaleY()
    }
    this.addTouchEventListener(function (sender, eventType) {
        if (eventType === ccui.Widget.TOUCH_BEGAN) {
            if (sender.likeButton) {
                if (appInstance && appInstance.audioManager().getButtonClickEffectSwitch()) {
                    let effect  = sender.clickEffectRes || appInstance.audioManager().getButtonClickEffect()
                    appInstance.audioManager().playEffect(effect)
                }
                sender.runAction(cc.scaleTo(0.05, sender.__orgScaleX + 0.1, sender.__orgScaleY + 0.1))
            }
        } else if (eventType === ccui.Widget.TOUCH_ENDED) {
            if (sender.likeButton) {
                sender.runAction(cc.scaleTo(0.05, sender.__orgScaleX, sender.__orgScaleY))
            }

            if (sender.onClicking) {
                return
            }

            if (sender.effectClick) {
                sender.onClicking = true
                setTimeout(function () {
                    if (cc.sys.isObjectValid(sender)) {
                        sender.onClicking = false
                    }
                }, 200)
            }

            callBack.call(obj, sender, eventType)
        } else if (eventType === ccui.Widget.TOUCH_CANCELED) {
            if (sender.likeButton) {
                sender.runAction(cc.scaleTo(0.05, sender.__orgScaleX, sender.__orgScaleY))
            }
        }
    }, obj)
}

/**
 *  关于Slider 事件的封装
 */
ccui.Slider.prototype.onSlider = function (callBack, obj) {
    this.addEventListener(function (sender, eventType) {
        callBack.call(obj, sender, eventType)
    })
}

/**
 *  关于CheckBox 事件的封装
 */
ccui.CheckBox.prototype.onChecked = function (callBack, obj) {
    this.addEventListener(function (sender, eventType) {
        if (appInstance && appInstance.audioManager().getButtonClickEffectSwitch()) {
            let effect  = sender.clickEffectRes || appInstance.audioManager().getButtonClickEffect()
            appInstance.audioManager().playEffect(effect)
        }
        callBack.call(obj, sender, eventType)
    })
}

