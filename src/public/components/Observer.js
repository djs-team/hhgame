
/**
 * Observer
 * 观察者组件
 */
load('public/components/Observer', function () {
  let Observer = cc.Class.extend({
    _notifyMethod: null,
    _context: null,
    ctor: function (notifyMethod, context) {
      this.setNotifyMethod(notifyMethod)
      this.setNotifyContext(context)
    },
    setNotifyMethod: function (notifyMethod) {
      this._notifyMethod = notifyMethod
    },
    setNotifyContext: function (context) {
      this._context = context
    },
    getNotifyMethod: function () {
      return this._notifyMethod
    },
    getNotifyContext: function () {
      return this._context
    },
    notifyObserver: function (notification) {
      this.getNotifyMethod().call(this.getNotifyContext(), notification)
    },
    compareNotifyContext: function (object) {
      return object === this._context
    }
  })
  return Observer
})
