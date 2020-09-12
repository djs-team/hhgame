package com.deepsea.mua.stub.event;

/**
 * Created by tong on 2016/10/20.
 */
public interface IEventObserver {
    void onEventReceived(EventMessage message);
}
