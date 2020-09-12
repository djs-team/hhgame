package com.deepsea.mua.stub.entity.socket.receive;

public class SyncDemandSongParam {
    private int MsgId;
    private String Code;
    private String DemandSongUserName;

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

    public String getDemandSongUserName() {
        return DemandSongUserName;
    }

    public void setDemandSongUserName(String demandSongUserName) {
        DemandSongUserName = demandSongUserName;
    }
}
