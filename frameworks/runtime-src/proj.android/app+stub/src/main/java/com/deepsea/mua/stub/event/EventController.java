package com.deepsea.mua.stub.event;

/**
 * Created by tong on 2016/10/20.
 */
public class EventController {

    //开启青少年模式
    public static final int EVENT_OPEN_YOUNGER = 100;
    //关闭青少年模式
    public static final int EVENT_CLOSE_YOUNGER = 101;
    //修改青少年模式密码
    public static final int EVENT_MODIFY_YOUNGER = 102;
    //解锁青少年模式
    public static final int EVENT_UNLOCK_YOUNGER = 103;
    //开启家长模式
    public static final int EVENT_OPEN_PARENT = 104;
    //关闭家长模式
    public static final int EVENT_CLOSE_PARENT = 105;
    //修改家长模式密码
    public static final int EVENT_MODIFY_PARENT = 106;

    //@Inject
    EventCenter mEventCenter;

    private EventController() {
        if (mEventCenter == null) {
            mEventCenter = new EventCenter();
        }
    }


    private static EventController sEventController;

    public static EventController getEventController() {
        if (sEventController == null) {
            synchronized (EventController.class) {
                if (sEventController == null) {
                    sEventController = new EventController();
                }
            }
        }

        return sEventController;
    }

    public void addObserver(IEventObserver observer) {
        mEventCenter.addObserver(observer);
    }

    public void removeObserver(IEventObserver observer) {
        mEventCenter.removeObserver(observer);
    }

    public void openYounger() {
        EventMessage message = new EventMessage();
        message.what = EVENT_OPEN_YOUNGER;
        mEventCenter.notifyObservers(message);
    }

    public void closeYounger() {
        EventMessage message = new EventMessage();
        message.what = EVENT_CLOSE_YOUNGER;
        mEventCenter.notifyObservers(message);
    }

    public void modifyYounger() {
        EventMessage message = new EventMessage();
        message.what = EVENT_MODIFY_YOUNGER;
        mEventCenter.notifyObservers(message);
    }

    public void unlockYounger() {
        EventMessage message = new EventMessage();
        message.what = EVENT_UNLOCK_YOUNGER;
        mEventCenter.notifyObservers(message);
    }

    public void openParent() {
        EventMessage message = new EventMessage();
        message.what = EVENT_OPEN_PARENT;
        mEventCenter.notifyObservers(message);
    }

    public void closeParent() {
        EventMessage message = new EventMessage();
        message.what = EVENT_CLOSE_PARENT;
        mEventCenter.notifyObservers(message);
    }

    public void modifyParent() {
        EventMessage message = new EventMessage();
        message.what = EVENT_MODIFY_PARENT;
        mEventCenter.notifyObservers(message);
    }

    public void release() {
        if (mEventCenter != null) {
            mEventCenter.release();
        }
    }
}
