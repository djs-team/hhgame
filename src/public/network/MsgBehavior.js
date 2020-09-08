/**
 *  用于定义处理协议的行为基类
 */
load('public/network/MsgBehavior', function () {
    let MsgBehavior = cc.Class.extend({
        _delay: 0,
        ctor: function () {
        },
        handleMsg: function (msg) {
            return msg
        },
        setDelay: function (delay) {
            this._delay = delay
        }
    })
    return MsgBehavior
})
