
/**
 * Notifier
 * 发射器组件
 */
load('public/components/Notifier', function () {
  let Notification = include('public/components/Notification')
  let Notifier = cc.Class.extend({
    observerCtrl: null,
    ctor: function (observerCtrl) {
      this.observerCtrl = observerCtrl
    },
    /**
     * 会根据优先级来派发
     * @param name
     * @param body
     */
    sendNotification: function (name, body) {
      if (this.observerCtrl) {
        let notification = new Notification(name, body)
        this.observerCtrl.notifyObservers(notification)
      }
    }
  })
  return Notifier
})
