package com.deepsea.mua.stub.entity.socket.receive;

public class LeaveRoomUserToOtherParam {
    private int UserId;
    private int MsgId;
    private String Code;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
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
