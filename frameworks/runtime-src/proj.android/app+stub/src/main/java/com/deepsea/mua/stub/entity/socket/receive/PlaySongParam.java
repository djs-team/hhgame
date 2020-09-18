package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.receive.SongInfo;
public class PlaySongParam {
    private int MsgId;
    private SongInfo SongInfo;
    private SongInfo NextSongInfo;
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

    public SongInfo getNextSongInfo() {
        return NextSongInfo;
    }

    public void setNextSongInfo(com.deepsea.mua.stub.entity.socket.receive.SongInfo nextSongInfo) {
        NextSongInfo = nextSongInfo;
    }
}
