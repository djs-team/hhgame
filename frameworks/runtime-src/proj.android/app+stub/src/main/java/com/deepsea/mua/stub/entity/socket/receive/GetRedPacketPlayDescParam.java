package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class GetRedPacketPlayDescParam {
    private int MsgId;
    private String Code;
    private String PlayingDesc;

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

    public String getPlayingDesc() {
        return PlayingDesc;
    }

    public void setPlayingDesc(String playingDesc) {
        PlayingDesc = playingDesc;
    }
}
