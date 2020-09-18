package com.deepsea.mua.stub.entity;

import com.hyphenate.chat.EMMessage;

public class FriendHXInfo {
private String userId;
private EMMessage message;
private int unreadCount;

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public FriendHXInfo() {
    }

    public FriendHXInfo(String userId, EMMessage message,int unreadCount) {
        this.userId = userId;
        this.message = message;
        this.unreadCount=unreadCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public EMMessage getMessage() {
        return message;
    }

    public void setMessage(EMMessage message) {
        this.message = message;
    }
}
