
/**
 * Mediator.js
 * 中间件组件
 */
load('public/components/Mediator', function () {
  let Mediator = cc.Class.extend({
    mediatorName: '',
    view: null,
    notifier: null,
    ctor: function (name, view) {
      this.mediatorName = name
      this.view = view
    },
    getMediatorName: function () {
      return this.mediatorName
    },
    getView: function () {
      return this.view
    },
    sendNotification: function (name, body) {
      appInstance.sendNotification(name, body)
    },
    getNotificationList: function () {
      return []
    },
    handleNotification: function (notification) {
    },
    onRegister: function () {
    },
    onRemove: function () {
    }

  })
  return Mediator
})
