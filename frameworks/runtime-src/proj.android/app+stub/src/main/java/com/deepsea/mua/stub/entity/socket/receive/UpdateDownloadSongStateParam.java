package com.deepsea.mua.stub.entity.socket.receive;

public class UpdateDownloadSongStateParam {
    private int MsgId;
    private int State ;
    private String Code;

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

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
}
