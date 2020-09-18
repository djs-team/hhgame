package com.deepsea.mua.stub.entity.socket.receive;

public class NotifyAddFriendToClientParam {
    private int TargetId;

    private String TargetName;

    public int getTargetId() {
        return TargetId;
    }

    public void setTargetId(int targetId) {
        TargetId = targetId;
    }

    public String getTargetName() {
        return TargetName;
    }

    public void setTargetName(String targetName) {
        TargetName = targetName;
    }
}
