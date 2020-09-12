package com.deepsea.mua.stub.entity.socket;

import com.deepsea.mua.stub.entity.RankBean;

import java.util.List;

/**
 * Created by JUN on 2019/5/22
 */
public class RoomRank {

    private List<RankBean> UserRankDatas;
    private int MsgId;
    private int Success;
    private int AllPage;

    public int getAllPage() {
        return AllPage;
    }

    public void setAllPage(int allPage) {
        AllPage = allPage;
    }

    public List<RankBean> getRankDatas() {
        return UserRankDatas;
    }

    public void setRankDatas(List<RankBean> rankDatas) {
        UserRankDatas = rankDatas;
    }

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public int getSuccess() {
        return Success;
    }

    public void setSuccess(int success) {
        Success = success;
    }
}
