
/**
 * CommandController
 * 命令组件管理器
 */
load('public/components/CommandController', function () {
  let Observer = include('public/components/Observer')
  let CommandController = cc.Class.extend({
    commandMap: {},
    observerCtrl: null,
    ctor: function (observerCtrl) {
      this.observerCtrl = observerCtrl
    },
    /**
     * 注册Command
     * @param notificationName
     * @param commandClass
     */
    registerCommand: function (notificationName, commandClass) {
      if (!this.commandMap[notificationName]) {
        this.observerCtrl.registerObserver(notificationName, new Observer(this.executeCommand, this))
      }
      this.commandMap[notificationName] = commandClass
    },
    /**
     * 执行命令
     * @param notification
     */
    executeCommand: function (notification) {
      let commandClassRef = this.commandMap[notification.getName()]
      if (commandClassRef) {
        let command = new commandClassRef()
        command.execute(notification)
      }
    },
    /**
     * 是否有命令
     * @param notificationName
     */
    hasCommand: function (notificationName) {
      return this.commandMap[notificationName] != null
    },
    /**
     * 删除commond
     * @param notificationName
     */
    removeCommand: function (notificationName) {
      if (this.hasCommand(notificationName)) {
        this.observerCtrl.removeObserver(notificationName, this)
        delete this.commandMap[notificationName]
      }
    }


  })

  return CommandController
})