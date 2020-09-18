package com.deepsea.mua.stub.utils.eventbus;

public class SyncSongProgressEvent {
    public int currentPos;
    public int max;

    public SyncSongProgressEvent() {
    }

    public SyncSongProgressEvent(int currentPos,int max) {
        this.currentPos = currentPos;
        this.max=max;
    }

}
