package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class SongRankListParam {
    private List<SongRankData> SongRankDatas;
    private int Page;
    private int AllPage;

    public List<SongRankData> getSongRankDatas() {
        return SongRankDatas;
    }

    public void setSongRankDatas(List<SongRankData> songRankDatas) {
        SongRankDatas = songRankDatas;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getAllPage() {
        return AllPage;
    }

    public void setAllPage(int allPage) {
        AllPage = allPage;
    }
}
