package com.deepsea.mua.stub.entity.socket.receive;

public class NotifyRedPacketProgressParam {
    private int MsgId;
    private String Code;
    private float Progress;
    private boolean IsVisible;

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public float getProgress() {
        return Progress;
    }

    public void setProgress(float progress) {
        Progress = progress;
    }

    public boolean isVisible() {
        return IsVisible;
    }

    public void setVisible(boolean visible) {
        IsVisible = visible;
    }
}
