package com.deepsea.mua.stub.entity.socket.receive;

public class LeaveRoomParam {
    private int MsgId;
    private String Code;
    private  int LeaveRoomCode;
    private String LeaveRoomMsg;

    public int getLeaveRoomCode() {
        return LeaveRoomCode;
    }

    public void setLeaveRoomCode(int leaveRoomCode) {
        LeaveRoomCode = leaveRoomCode;
    }

    public String getLeaveRoomMsg() {
        return LeaveRoomMsg;
    }

    public void setLeaveRoomMsg(String leaveRoomMsg) {
        LeaveRoomMsg = leaveRoomMsg;
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
