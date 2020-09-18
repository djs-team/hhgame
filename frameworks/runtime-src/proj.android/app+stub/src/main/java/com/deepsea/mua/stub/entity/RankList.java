package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/8/27
 */
public class RankList {
    private List<TotalRank> rich_list;
    private List<TotalRank> like_list;

    public List<TotalRank> getRich_list() {
        return rich_list;
    }

    public void setRich_list(List<TotalRank> rich_list) {
        this.rich_list = rich_list;
    }

    public List<TotalRank> getLike_list() {
        return like_list;
    }

    public void setLike_list(List<TotalRank> like_list) {
        this.like_list = like_list;
    }
}
