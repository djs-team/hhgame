package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class DisableMsgParam {
    private int MsgId;
    private String Code;
    private List<DisableMsgData> DisableMsgs;

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

    public List<DisableMsgData> getDisableMsgs() {
        return DisableMsgs;
    }

    public void setDisableMsgs(List<DisableMsgData> disableMsgs) {
        DisableMsgs = disableMsgs;
    }
}
