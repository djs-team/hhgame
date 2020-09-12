package com.deepsea.mua.stub.entity;

import java.util.List;

public class InviteOnMicroBean {
    private List<InviteOnmicroData> TargetUserIds;
    private int MsgId;
    private int Free;

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public int getFree() {
        return Free;
    }

    public void setFree(int free) {
        Free = free;
    }

    public List<InviteOnmicroData> getTargetUserIds() {
        return TargetUserIds;
    }

    public void setTargetUserIds(List<InviteOnmicroData> targetUserIds) {
        TargetUserIds = targetUserIds;
    }

    public InviteOnMicroBean(List<InviteOnmicroData> targetUserIds) {
        TargetUserIds = targetUserIds;
    }

    public InviteOnMicroBean() {
    }
}
