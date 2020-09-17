/**
 * Command
 * 命令组件
 */
load('public/components/Command', function () {
  let Command = cc.Class.extend({
    ctor: function () {
    },
    sendNotification: function (name, body) {
      appInstance.sendNotification(name, body)
    },
    /**
     * 收到notification
     * @param notification
     */
    execute (notification) {
    }
  })

  return Command
})