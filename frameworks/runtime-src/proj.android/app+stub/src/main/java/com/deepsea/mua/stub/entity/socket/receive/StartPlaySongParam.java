package com.deepsea.mua.stub.entity.socket.receive;

public class StartPlaySongParam {
    private int MsgId;
    private SongInfo SongInfo;
    private String Code;

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public SongInfo getSongInfo() {
        return SongInfo;
    }

    public void setSongInfo(SongInfo songInfo) {
        SongInfo = songInfo;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

}
