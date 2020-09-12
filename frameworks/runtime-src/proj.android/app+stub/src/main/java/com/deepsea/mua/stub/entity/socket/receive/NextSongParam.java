package com.deepsea.mua.stub.entity.socket.receive;

public class NextSongParam {
    private int MsgId;
    private int State;
    private String Code;
    private  int ConsertUserId;
    private String ConsertUserName;

    public int getConsertUserId() {
        return ConsertUserId;
    }

    public void setConsertUserId(int consertUserId) {
        ConsertUserId = consertUserId;
    }

    public String getConsertUserName() {
        return ConsertUserName;
    }

    public void setConsertUserName(String consertUserName) {
        ConsertUserName = consertUserName;
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

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }
}
