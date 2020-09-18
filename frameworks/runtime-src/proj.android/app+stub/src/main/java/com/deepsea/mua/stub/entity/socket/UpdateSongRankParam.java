package com.deepsea.mua.stub.entity.socket;

public class UpdateSongRankParam {
    private int Rank;
    private int MsgId;
    private String Code;
    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
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
