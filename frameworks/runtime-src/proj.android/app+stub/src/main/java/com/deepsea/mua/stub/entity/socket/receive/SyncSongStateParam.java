package com.deepsea.mua.stub.entity.socket.receive;

public class SyncSongStateParam {
    private int MsgId;
    private int IsPause ;
    private int ConsertUserId ;
    private String Code;

    public int getConsertUserId() {
        return ConsertUserId;
    }

    public void setConsertUserId(int consertUserId) {
        ConsertUserId = consertUserId;
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
