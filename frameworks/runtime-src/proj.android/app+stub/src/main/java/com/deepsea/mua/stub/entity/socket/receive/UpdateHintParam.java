package com.deepsea.mua.stub.entity.socket.receive;

public class UpdateHintParam {
    private int MsgId;
    private String Code;
    private String Msg;

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
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
