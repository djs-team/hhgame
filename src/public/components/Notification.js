/**
 * Notification.js
 * 中间件组件
 */
load('public/components/Notification', function () {
  let Notification = cc.Class.extend({
    name: null,
    body: null,
    ctor: function (name, body) {
      this.name = name
      this.body = body
    },
    getName: function () {
      return this.name
    },
    getBody: function () {
      return this.body
    }
  })
  return Notification

})
