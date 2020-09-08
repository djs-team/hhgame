
/**
 * 用于视图的工具, 纯函数
 * @type {{}}
 */
global.View = {}

global.View.size = {
    width: 1280,
    height: 720,
    halfWidth: 640,
    halfHeight: 360
}

global.View.runPopupAnimation = function (node) {
    let originScale = node.getScale()
    node.setScale(0.01)

    // TODO 动画播放时 蒙灰层有从小变大的效果 不美观
    if (node.rootNode) {
        let blockNode = node.rootNode.getChildByName('block')
        if (blockNode) {
            node.rootNode.getChildByName('block').setScale(blockNode.getScaleX() * 2)
        }
    }

    let scaleTo = new cc.EaseBackOut(cc.scaleTo(0.25, originScale), 0.3)
    node.runAction(scaleTo)
}

global.View.createTextField = function (pSize, maxLength, strPlaceHolder, fontSize, fontColor4) {
    strPlaceHolder = strPlaceHolder || ''
    fontSize = fontSize || 32
    fontColor4 = fontColor4 || cc.color(204, 204, 204, 255)
    let TextField = global.View.editBoxProduction(cc.size(pSize.width - 10, pSize.height - 10), '', maxLength, strPlaceHolder)
    TextField.setAnchorPoint(0.5, 0.5)
    TextField.setPosition(pSize.width / 2, pSize.height / 2)
    TextField.setFontColor(fontColor4)
    TextField.setPlaceholderFontColor(fontColor4)
    TextField.setFontSize(fontSize)
    TextField.setPlaceholderFontSize(fontSize)
    return TextField
}

/**
 * 生成一个editbox
 * @param size 大小
 * @param scale9FilePath 图片地图
 * @param maxLength 最大长度
 * @param placeHolder 站位提示文本
 * @returns {cc.EditBox}
 */
global.View.editBoxProduction = function (size, scale9FilePath, maxLength, placeHolder) {
    let editBox = new cc.EditBox(size, new cc.Scale9Sprite(scale9FilePath))
    if (maxLength > 0) {
        editBox.setMaxLength(maxLength)
    }
    editBox.setPlaceHolder(placeHolder)
    editBox.setReturnType(cc.KEYBOARD_RETURNTYPE_DONE)
    return editBox
}

/*
 * 系统等待转圈
 * delayTime: 延迟显示的时间 block的时间默认800ms
 */
global.View.block = function (delayTime) {
    let block = include('Game/Hall/BlockLayer')
    block.showInstance(delayTime)
}

global.View.unblock = function () {
    let block = include('Game/Hall/BlockLayer')
    block.cleanupInstance()
}
