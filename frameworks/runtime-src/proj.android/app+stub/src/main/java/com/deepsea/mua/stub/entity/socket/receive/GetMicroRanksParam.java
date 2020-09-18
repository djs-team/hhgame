package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class GetMicroRanksParam {
    private int MsgId;
    private String Code;
    private int AllPage;
    private int AllRolse;
    private List<MicroRankData> MicroRankDatas;

    public int getAllRolse() {
        return AllRolse;
    }

    public void setAllRolse(int allRolse) {
        AllRolse = allRolse;
    }

    public int getAllPage() {
        return AllPage;
    }

    public void setAllPage(int allPage) {
        AllPage = allPage;
    }

    public List<MicroRankData> getMicroRankDatas() {
        return MicroRankDatas;
    }

    public void setMicroRankDatas(List<MicroRankData> microRankDatas) {
        MicroRankDatas = microRankDatas;
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
