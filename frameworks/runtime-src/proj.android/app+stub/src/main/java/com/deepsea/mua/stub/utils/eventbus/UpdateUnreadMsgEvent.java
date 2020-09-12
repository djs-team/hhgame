package com.deepsea.mua.stub.utils.eventbus;

public class UpdateUnreadMsgEvent {
    private boolean hasUnread;

    public boolean isHasUnread() {
        return hasUnread;
    }

    public void setHasUnread(boolean hasUnread) {
        this.hasUnread = hasUnread;
    }

    public UpdateUnreadMsgEvent() {
    }

    public UpdateUnreadMsgEvent(boolean hasUnread) {
        this.hasUnread = hasUnread;
    }
}
