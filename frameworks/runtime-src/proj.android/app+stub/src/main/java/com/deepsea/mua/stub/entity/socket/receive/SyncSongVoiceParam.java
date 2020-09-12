package com.deepsea.mua.stub.entity.socket.receive;

public class SyncSongVoiceParam {
    private int MsgId;
    private int Volume ;
    private String Code;

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public int getVolume() {
        return Volume;
    }

    public void setVolume(int volume) {
        Volume = volume;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
