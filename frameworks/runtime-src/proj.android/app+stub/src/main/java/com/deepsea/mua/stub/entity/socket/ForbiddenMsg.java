package com.deepsea.mua.stub.entity.socket;

/**
 * Created by JUN on 2019/4/22
 */
public class ForbiddenMsg {
    private int MsgId;
    private boolean IsDisableMsg;

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int MsgId) {
        this.MsgId = MsgId;
    }

    public boolean isIsDisableMsg() {
        return IsDisableMsg;
    }

    public void setIsDisableMsg(boolean IsDisableMsg) {
        this.IsDisableMsg = IsDisableMsg;
    }
}
