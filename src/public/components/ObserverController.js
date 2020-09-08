
/**
 * ObserverContainer
 * 观察者组件gu
 */
load('public/components/ObserverController', function () {
  let ObserverController = cc.Class.extend({
    observerMap: {},
    ctor: function () {
    },
    registerObserver: function (notificationName, observer) {
      let observers = this.observerMap[notificationName]
      if (observers) {
        observers.push(observer)
      } else {
        this.observerMap[notificationName] = [observer]
      }
    },
    removeObserver: function (notificationName, notifyContext) {
      let observers = this.observerMap[notificationName]
      if (observers) {
        let i = observers.length
        while (i--) {
          let observer = observers[i]
          if (observer.compareNotifyContext(notifyContext)) {
            observers.splice(i, 1)
            break
          }
        }
      }
      if (observers.length === 0) {
        delete this.observerMap[notificationName]
      }
    },
    notifyObservers: function (notification) {
      let notificationName = notification.getName()
      let observersRef = this.observerMap[notificationName]
      if (observersRef) {
        let observers = observersRef.slice(0)
        let len = observers.length
        for (let i = 0; i < len; i++) {
          let observer = observers[i]
          observer.notifyObserver(notification)
        }
      }
    }

  })
  return ObserverController
})