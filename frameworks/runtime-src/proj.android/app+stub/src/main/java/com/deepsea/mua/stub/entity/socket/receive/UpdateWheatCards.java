package com.deepsea.mua.stub.entity.socket.receive;

public class UpdateWheatCards {
    private int MsgId;
    private String Code;
    private  int Numbers;

    public int getNumbers() {
        return Numbers;
    }

    public void setNumbers(int numbers) {
        Numbers = numbers;
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
