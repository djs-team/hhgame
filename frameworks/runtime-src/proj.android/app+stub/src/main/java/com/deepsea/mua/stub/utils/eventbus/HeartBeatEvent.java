package com.deepsea.mua.stub.utils.eventbus;

public class HeartBeatEvent {
    private int isRequest;

    public int getIsRequest() {
        return isRequest;
    }

    public void setIsRequest(int isRequest) {
        this.isRequest = isRequest;
    }

    public HeartBeatEvent(int isRequest) {
        this.isRequest = isRequest;
    }

    public HeartBeatEvent() {
    }
}
