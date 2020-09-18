package com.deepsea.mua.stub.utils.eventbus;

public class BrushEvent {
    private int msgType;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public BrushEvent(int msgType) {
        this.msgType = msgType;
    }
}
