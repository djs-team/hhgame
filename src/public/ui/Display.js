/**
 *  显示适配相关的类 ( *此类 感谢 李大厨的奉献^_^*)
 *  FrameSize: 就是屏幕的实际分辨率，这是不变的
 *  getWinSize: 就是设计分辨率，通过cc.view.setDesignResolutionSize设置，相当于游戏设计的逻辑大小，可以这样理解，上面的FrameSize就是画框，这里的WinSize就是画布
 *  getVisibleSize：在设计分辨率（WinSize）下，在屏幕中被显示出来的画布的大小( <= WinSize)
 *  getVisibleOrigin：在设计分辨率下,在屏幕上显示后被截取的区域大小的一半（左右 或者 上下 各截取一半）， VisibleOrigin*2是显示后被截取的总大小 NoBorder模式下才有意义
 *  WinSize VisibleSize VisibleOrigin 与都设计的分辨率相关，满足如下关系
 *  WinSize.width = (VisibleOrigin.x * 2) + VisibleSize.width
 *  WinSize.height = (VisibleOrigin.y * 2) + VisibleSize.height
 *  cc.view.getScaleX: 屏幕正常显示的画布相对于设计分辨率的缩放比例
 *  cc.view.getScaleY: 屏幕正常显示的画布相对于设计分辨率的缩放比例
 *  getContentSize: 函数来获得节点原始的大小。只是逻辑尺寸，不是像素
 *  getContentSizeInPixels: 获得的是像素点大小
 *  像素点和逻辑点关系：逻辑点大小 = 像素大小/contentScaleFactor
 *  wiget.getCustomSize: 获取ccui.widget的自定义大小（在cocos studio中设置的大小）
 *
 * const userData = {
 *   layoutType: 'showAll',   // 适配类型 对应LayoutType
 *   screenAlign: 'top|left', // 对齐方式 对应AlignType
 *   offsetX: 99, // x轴坐标微调整 对应FrameSize的百分比值 为了解决屏幕对齐需要微调整的问题 比如：靠右居中 离屏幕边缘10px的距离
 *   offsetY: -5  // y轴坐标微调整 对应FrameSize的百分比值
 * }
 */
load('public/ui/Display', function () {
    let LayoutType = {
        // 不产生适配行为，但可识别位移操作
        None: 'none',
        //
        // 以宽度或高度缩放比例小的值进行等比例拉伸 （如果宽度和高度缩放比例大小不相等，会出现黑边）
        ShowAll: 'showAll',
        //
        // 以宽度或高度缩放比例大的值进行等比例拉伸 （如果宽度和高度缩放比例大小不相等，会出现出框 出框部分被裁剪）
        NoBorder: 'noBorder',
        //
        // 以宽度或高度各自的缩放比例进行非等比例拉伸 （如果宽度和高度缩放比例大小不相等，会出现图像变形）
        ExactFit: 'exactFit',
        //
        // 以宽度充满全屏的缩放比例进行等比例拉伸 （对于宽屏屏幕来说，纵向可能会有图像出屏）
        FullWidth: 'fullWidth',
        //
        // 以高度充满全屏的缩放比例进行等比例拉伸 （对于方屏屏幕来说，横向可能会有图像出屏）
        FullHeight: 'fullHeight',
        //
        // 非等比拉伸 以宽度充满全屏的缩放比例进行拉伸, 高度按ShowAll的方式拉伸（如果宽度和高度缩放比例大小不相等，会出现图像变形）
        OnlyFullWidth: 'onlyFullWidth',
        //
        // 非等比拉伸 以高度充满全屏的缩放比例进行拉伸, 宽度按ShowAll的方式拉伸 （如果宽度和高度缩放比例大小不相等，会出现图像变形）
        OnlyFullHeight: 'onlyFullHeight',
        //
        // 等比拉伸 以画布与节点资源图片宽高比 大的比例值为基础 宽度等比拉伸 （用于处理比画布大的图片，并让图片尽量多的显示在屏幕内）
        MaxArea: 'maxArea',
        //
        // 嵌入的子csd节点需要适配
        Embed: 'embed'
    }

    // 对齐类型
    let AlignType = {
        // 顶对齐
        Top: 'top',
        //
        // 底对齐
        Bottom: 'bottom',
        //
        // 中间对齐
        Mid: 'mid',
        //
        // 左对齐
        Left: 'left',
        //
        // 右对齐
        Right: 'right',
        //
        // x轴中间对齐
        MidW: 'midW',
        //
        // y轴中间对齐
        MidH: 'midH'
    }

    let Display = cc.Class.extend({
        _gDesignSize: { width: 1280, height: 720 },
        _gDesignScale: { scaleX: 1, scaleY: 1 },
        ctor: function () {
            this._gDesignScale.scaleX = this.getWinSize().width / this._gDesignSize.width
            this._gDesignScale.scaleY = this.getWinSize().height / this._gDesignSize.height
        },
        /**
         * 设置设计分辨率 默认为 1280*720
         */
        setDesignSize: function (w, h) {
            this._gDesignSize.width = w
            this._gDesignSize.height = h
        },
        getScreenRate: function () {
            let size = cc.view.getFrameSize()
            let w = size.width
            let h = size.height
            return w / h
        },
        gDesignScale: function () {
            return this._gDesignScale
        },
        getWinSize: function () {
            // return cc.director.getSafeAreaRect() // 针对全面屏
            return cc.view.getFrameSize() // 画布大小
        },
        // 遍历layer子层节点（子层的子层以下节点不在遍历范畴），根据在csd中各节点设置的layoutType值,一一进行自动doLayout操作
        // layer: 需要进行屏幕适配的layer
        autoLayout: function (layer) {
            if (layer) {
                let layoutType = this.getLayoutTypeByNode(layer)
                // layer强制要求不适配或其父节点已有适配行为时，忽略其子节点的适配请求
                if (layoutType !== LayoutType.None && !this.isAutoLayoutByParent(layer)) {
                    const children = layer.getChildren()
                    for (let i = 0; i < children.length; i++) {
                        this.doLayout(children[i])
                    }
                    return true
                }
            }
            return false
        },
        // 对单个节点按自身的layoutType进行ui适配, 只对node本身适配，不会对其子节点进行适配，
        // 其子节点应该遵循跟随父节点适配后,自动适应的原则来设计
        // node: 需要适配的节点
        doLayout: function (node) {
            if (node) {
                const layoutType = this.getLayoutTypeByNode(node)
                switch (layoutType) {
                    case LayoutType.None:
                        // this.doLayoutNone(node)
                        break
                    case LayoutType.ShowAll:
                        this.doLayoutShowAll(node)
                        break
                    case LayoutType.NoBorder:
                        this.doLayoutNoBorder(node)
                        break
                    case LayoutType.ExactFit:
                        this.doLayoutExactFit(node)
                        break
                    case LayoutType.FullWidth:
                        this.doLayoutFullWidth(node)
                        break
                    case LayoutType.FullHeight:
                        this.doLayoutFullHeight(node)
                        break
                    case LayoutType.OnlyFullWidth:
                        this.doLayoutOnlyFullWidth(node)
                        break
                    case LayoutType.OnlyFullHeight:
                        this.doLayoutOnlyFullHeight(node)
                        break
                    case LayoutType.MaxArea:
                        this.doLayoutMaxArea(node)
                        break
                    case LayoutType.Embed:
                        this.doLayoutEmbed(node)
                        break
                    default:
                        cc.log('doLayout error: layoutType<' + layoutType + '> is Invalid  by node<{node.getName()}>')
                }
            }
        },
        doLayoutNone: function (node) {
            this.autoPosition(node)
        },
        doLayoutShowAll: function (node) {
            let scale = Math.min(this._gDesignScale.scaleX, this._gDesignScale.scaleY)
            node.setScaleX(node.getScaleX() * scale)
            node.setScaleY(node.getScaleY() * scale)
            this.autoPosition(node)
        },
        doLayoutNoBorder: function (node) {
            let scale = Math.max(this._gDesignScale.scaleX, this._gDesignScale.scaleY)
            node.setScaleX(node.getScaleX() * scale)
            node.setScaleY(node.getScaleY() * scale)
            this.autoPosition(node)
        },
        doLayoutExactFit: function (node) {
            node.setScaleX(node.getScaleX() * this._gDesignScale.scaleX)
            node.setScaleY(node.getScaleY() * this._gDesignScale.scaleY)
            this.autoPosition(node)
        },
        doLayoutFullWidth: function (node) {
            let scale = this.getWinSize().width / node.getContentSize().width
            node.setScaleX(scale)
            node.setScaleY(scale)
            this.autoPosition(node)
        },
        doLayoutFullHeight: function (node) {
            let scale = this.getWinSize().height / node.getContentSize().height
            node.setScaleX(scale)
            node.setScaleY(scale)
            this.autoPosition(node)
        },
        doLayoutOnlyFullWidth: function (node) {
            let scaleX = this.getWinSize().width / node.getContentSize().width
            let scaleY = Math.min(this._gDesignScale.scaleX, this._gDesignScale.scaleY)
            node.setScaleX(scaleX)
            node.setScaleY(scaleY)
            this.autoPosition(node)
        },

        doLayoutOnlyFullHeight: function (node) {
            let scaleX = Math.min(this._gDesignScale.scaleX, this._gDesignScale.scaleY)
            let scaleY = this.getWinSize().height / node.getContentSize().height
            node.setScaleX(scaleX)
            node.setScaleY(scaleY)
            this.autoPosition(node)
        },

        doLayoutMaxArea: function (node) {
            let size = node.getContentSize()
            let scaleX = this.getWinSize().width / size.width
            let scaleY = this.getWinSize().height / size.height
            let scale = Math.max(scaleX, scaleY)
            node.setScaleX(scale)
            node.setScaleY(scale)
            this.autoPosition(node)
        },

        doLayoutEmbed: function (node) {
            const children = node.getChildren()
            for (let i = 0; i < children.length; i++) {
                this.doLayout(children[i])
            }
        },

        autoPosition: function (node) {
            let oldPos = node.getPosition()
            node.setPosition(oldPos.x * this._gDesignScale.scaleX, oldPos.y * this._gDesignScale.scaleY)
            this.doScreenAlignByNode(node)
            this.doPositionOffset(node)
            this.setAutoLayoutFlag(node)
        },

        doScreenAlignByNode: function (node) {
            let alignType = this.getScreenAlignTypeByNode(node)
            for (let i = 0; i < alignType.length && i < 2; i++) {
                switch (alignType[i]) {
                    case AlignType.Top:
                        this.doScreenAlignTop(node)
                        break
                    case AlignType.Bottom:
                        this.doScreenAlignBottom(node)
                        break
                    case AlignType.Mid:
                        this.doScreenAlignMid(node)
                        break
                    case AlignType.Left:
                        this.doScreenAlignLeft(node)
                        break
                    case AlignType.Right:
                        this.doScreenAlignRight(node)
                        break
                    case AlignType.MidW:
                        this.doScreenAlignMidW(node)
                        break
                    case AlignType.MidH:
                        this.doScreenAlignMidH(node)
                        break
                    default:
                        cc.log('doScreenAlign error: alignType<' + alignType[i] + '> is Invalid  by node<{node.getName()}>')
                }
            }
        },
        doScreenAlignTop: function (node) {
            // 画布的高度减去屏幕上方出屏的高度 得出的值对齐屏幕顶边
            let screenTopPos = cc.p(0, this.getWinSize().height - cc.view.getVisibleOrigin().y)
            if (node.getParent()) {
                screenTopPos = node.getParent().convertToWorldSpace(screenTopPos)
            }
            node.setPositionY(screenTopPos.y)
        },
        doScreenAlignBottom: function (node) {
            let screenBottomPos = cc.p(0, cc.view.getVisibleOrigin().y)
            if (node.getParent()) {
                screenBottomPos = node.getParent().convertToWorldSpace(screenBottomPos)
            }
            node.setPositionY(screenBottomPos.y)
        },
        doScreenAlignMidW: function (node) {
            let screenMid = cc.p(this.getWinSize().width / 2, this.getWinSize().height / 2)
            if (node.getParent()) {
                screenMid = node.getParent().convertToWorldSpace(screenMid)
            }
            node.setPositionX(screenMid.x)
        },
        doScreenAlignMidH: function (node) {
            let screenMid = cc.p(this.getWinSize().width / 2, this.getWinSize().height / 2)
            if (node.getParent()) {
                screenMid = node.getParent().convertToWorldSpace(screenMid)
            }
            node.setPositionY(screenMid.y)
        },
        doScreenAlignMid: function (node) {
            let screenMid = cc.p(this.getWinSize().width / 2, this.getWinSize().height / 2)
            if (node.getParent()) {
                screenMid = node.getParent().convertToWorldSpace(screenMid)
            }
            node.setPosition(screenMid)
        },

        doScreenAlignLeft: function (node) {
            let screenLeftPos = cc.p(cc.view.getVisibleOrigin().x, 0)
            if (node.getParent()) {
                screenLeftPos = node.getParent().convertToWorldSpace(screenLeftPos)
            }
            node.setPositionX(screenLeftPos.x)
        },
        doScreenAlignRight: function (node) {
            let screenRightPos = cc.p(this.getWinSize().width - cc.view.getVisibleOrigin().x, 0)
            if (node.getParent()) {
                screenRightPos = node.getParent().convertToWorldSpace(screenRightPos)
            }
            node.setPositionX(screenRightPos.x)
        },
        // 为了解决屏幕对齐需要微调整的问题 比如：靠右居中 离屏幕边缘10px的距离
        // offsetX、offsetY的值是相对于屏幕尺寸的百分比
        doPositionOffset: function (node, _offsetX, _offsetY) {
            let frameSize = cc.view.getFrameSize()
            let offsetPosition = this.getOffsetPositionByNode(node)
            let offsetX = (typeof _offsetX !== 'undefined') ? _offsetX / 100 : offsetPosition.offsetX
            let offsetY = (typeof _offsetY !== 'undefined') ? _offsetY / 100 : offsetPosition.offsetY
            let offset = cc.p(frameSize.width * offsetX, frameSize.height * offsetY)
            let newPos = cc.pAdd(node.getPosition(), offset)
            node.setPosition(newPos)
        },
        getUserDataByNode: function (node) {
            let userData = null
            if (node) {
                try {
                    let extensionData = node.getComponent('ComExtensionData')
                    if (extensionData) {
                        let property = extensionData.getCustomProperty()
                        if (property) {
                            userData = JSON.parse(property)
                        }
                    }
                } catch (e) {
                    cc.log(e)
                }
            }
            return userData
        },
        // 获取一个节点的layout类型
        // 读取的值是csd文件上节点的UserData属性对象的layoutType子属性，如果获取失败将返回默认值LayoutType.UnKnown
        // node: 需要识别layoutType的cocos studio中的ui节点
        getLayoutTypeByNode: function (node) {
            let layoutType = LayoutType.None
            if (node) {
                layoutType = LayoutType.ShowAll
                let userData = this.getUserDataByNode(node)
                if (userData && typeof userData.layoutType !== 'undefined' && userData.layoutType !== null) {
                    layoutType = userData.layoutType
                }
            }

            return layoutType
        },
        // 获取一个节点的对齐方式
        // ["top", "left"]
        getScreenAlignTypeByNode: function (node) {
            let alignType = []
            if (node) {
                let userData = this.getUserDataByNode(node)
                if (userData && typeof userData.screenAlign !== 'undefined' && userData.screenAlign !== null) {
                    alignType = userData.screenAlign.split('|', 2)

                    // 如果有Mid对齐方式 优先处理
                    if (alignType.length === 2 && alignType[1] === AlignType.Mid) {
                        let temp = alignType[0]
                        alignType[0] = alignType[1]
                        alignType[1] = temp
                    }
                }
            }

            return alignType
        },
        // 获取一个节点的对齐方式
        // ["top", "left"]
        getOffsetPositionByNode: function (node) {
            let offsetPosition = { offsetX: 0, offsetY: 0 }
            if (node) {
                let userData = this.getUserDataByNode(node)
                if (userData) {
                    if (typeof userData.offsetX !== 'undefined' && userData.offsetX !== null) {
                        offsetPosition.offsetX = userData.offsetX / 100
                    }
                    if (typeof userData.offsetY !== 'undefined' && userData.offsetY !== null) {
                        offsetPosition.offsetY = userData.offsetY / 100
                    }
                }
            }

            return offsetPosition
        },
        // 使用autoLayout操作后，给layer一个autoLayout的标记，
        // 避免layer在onEnter操作完成后，在layer中添加子layer1由于自己再执行autoLayout 造成layer1受到多次doLayout影响
        setAutoLayoutFlag: function (layer) {
            if (layer) {
                layer.__autoLayerFlag = true
            }
        },

        isAutoLayout: function (layer) {
            if (layer && layer.__autoLayerFlag) {
                return true
            }
            return false
        },

        // 测试layer的所有父级节点中，是否已有执行过autoLayout操作的节点
        isAutoLayoutByParent: function (layer) {
            let parent = layer
            while (parent) {
                if (this.isAutoLayout(parent)) {
                    return true
                }
                parent = parent.getParent()
            }
            return false
        },
        // 获取node在其父节点中的百分比坐标
        getPercentPosition: function (node) {
            let pos = cc.p(0, 0)
            if (node && node.getParent()) {
                let size = node.getParent().getContentSize()
                let p = node.getPosition()
                pos = cc.p(p.x / size.width, p.y / size.height)
            }
            return pos
        },
        // 获取node大小与设计大小的比值
        getPercentSizeByDesign: function (node) {
            let pSize = node.getCustomSize()
            let size = cc.size(pSize.width / this._gDesignSize.width, pSize.height / this._gDesignSize.height)
            return size
        }

    })
    return Display
})
