package com.deepsea.mua.stub.event;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tong on 2016/10/20.
 */
public class EventCenter {
    private ConcurrentHashMap<IEventObserver, Integer> observers;

//    @Inject
    public EventCenter() {
        observers = new ConcurrentHashMap<>();
    }

    public void addObserver(IEventObserver observer) {
        this.observers.put(observer, 0);
    }

    public void removeObserver(IEventObserver observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers(EventMessage message) {
        Iterator<IEventObserver> it = observers.keySet().iterator();
        while (it.hasNext()) {
            IEventObserver observer = it.next();
            observer.onEventReceived(message);
        }
    }

    public void release() {
        observers.clear();
    }
}
