package com.deepsea.mua.stub.entity.socket.receive;

public class SyncSongUpdateWindowParam {
    private int MsgId;
    private int IsPause ;
    private int Volume ;
    private String Code;

    public int getVolume() {
        return Volume;
    }

    public void setVolume(int volume) {
        Volume = volume;
    }

    public int isPause() {
        return IsPause;
    }

    public void setPause(int pause) {
        IsPause = pause;
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
