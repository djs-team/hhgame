package com.deepsea.mua.stub.entity.socket.send;

import com.deepsea.mua.stub.entity.InviteOnmicroData;

import java.util.List;

public class GiveBlueRoseParam {
    private List<Integer> UserIds;
    private int MsgId;
    private int BlueRose;

    public List<Integer> getUserIds() {
        return UserIds;
    }

    public void setUserIds(List<Integer> userIds) {
        UserIds = userIds;
    }

    public int getBlueRose() {
        return BlueRose;
    }

    public void setBlueRose(int blueRose) {
        BlueRose = blueRose;
    }

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public GiveBlueRoseParam(List<Integer> userIds) {
        UserIds = userIds;
    }

    public GiveBlueRoseParam() {
    }
}
