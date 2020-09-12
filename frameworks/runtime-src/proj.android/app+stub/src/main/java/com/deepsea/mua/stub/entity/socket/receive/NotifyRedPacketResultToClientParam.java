package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.UserRedPacket;

import java.util.List;

public class NotifyRedPacketResultToClientParam {
    private int MsgId;
    private String Code;
    private List<UserRedPacket> UserRedPackets;

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

    public List<UserRedPacket> getUserRedPackets() {
        return UserRedPackets;
    }

    public void setUserRedPackets(List<UserRedPacket> userRedPackets) {
        UserRedPackets = userRedPackets;
    }
}
