
/**
 * ObserverContainer
 * 观察者组件容器
 */
load('public/components/MediatorController', function () {
  let Observer = include('public/components/Observer')
  let MediatorController = cc.Class.extend({
    mediatorMultiMap: {}, //可以存储多个同类型实例的map
    observerCtrl: null,
    ctor: function (observerCtrl) {
      this.observerCtrl = observerCtrl
    },
    /**
     * 注册mediator
     * @param mediator
     * @param isMulti (note: 开启这个参数也就预示着 同一类型的mediator可以存在多个，同时观察者数量也会增加N倍，根据需求酌情使用，
     * 不推荐特大体量的对象开启)
     */
    registerMediator: function (mediator, isMulti) {
      let name = mediator.getMediatorName()
      if (isMulti) {
        let instanceId = mediator.__instanceId
        if (!this.mediatorMultiMap[name]) {
          this.mediatorMultiMap[name] = {}
        }
        if (this.mediatorMultiMap[name][instanceId]) {
          return
        }
        this.mediatorMultiMap[name][instanceId] = mediator
      } else {
        if (this.mediatorMultiMap[name]) {
          return
        }
        this.mediatorMultiMap[name] = mediator
      }
      let list = mediator.getNotificationList()
      if (list.length > 0) {
        let observer = new Observer(mediator.handleNotification, mediator)
        for (let i = 0; i < list.length; i++) {
          this.observerCtrl.registerObserver(list[i], observer)
        }
      }
      mediator.onRegister()
    },
    /**
     * 检索是否存在观察者
     * @param mediatorName
     */
    retrieveMediator: function (mediatorName, instanceId) {
      let mediator = null
      if (!instanceId) {
        mediator = this.mediatorMultiMap[mediatorName]
      } else {
        mediator = this.mediatorMultiMap[mediatorName][instanceId]
      }
      return mediator
    },
    /**
     * 删除mediator
     * @param mediatorName
     */
    removeMediator: function (mediatorName, instanceId) {
      let mediator = this.retrieveMediator(mediatorName, instanceId)
      if (!mediator) {
        return null
      }
      let list = mediator.getNotificationList()
      let i = list.length
      while (i--) {
        this.observerCtrl.removeObserver(list[i], mediator)
      }
      if (instanceId) {
        cc.log('removeMediator>>>>>>>>>>>>>>>>>' + instanceId)
        delete this.mediatorMultiMap[mediatorName][instanceId]
      } else {
        delete this.mediatorMultiMap[mediatorName]
      }
      mediator.onRemove()
      return mediator
    }

  })

  return MediatorController
})