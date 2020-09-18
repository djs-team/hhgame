package com.deepsea.mua.stub.entity.socket.receive;

public class UpdateSongLyricParam {
    private int MsgId;
    private String Lyric ;
    private String Code;

    public String getLyric() {
        return Lyric;
    }

    public void setLyric(String lyric) {
        Lyric = lyric;
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
