package com.deepsea.mua.stub.entity.socket.receive;

public class GuestNumMsg {

    /**
     * MsgId : 75
     * ManNumber : 1
     * WomenNumber : 0
     */

    private int MsgId;
    private int LeftNumber;
    private int RightNumber;

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int MsgId) {
        this.MsgId = MsgId;
    }

    public int getLeftNumber() {
        return LeftNumber;
    }

    public void setLeftNumber(int leftNumber) {
        LeftNumber = leftNumber;
    }

    public int getRightNumber() {
        return RightNumber;
    }

    public void setRightNumber(int rightNumber) {
        RightNumber = rightNumber;
    }
}
