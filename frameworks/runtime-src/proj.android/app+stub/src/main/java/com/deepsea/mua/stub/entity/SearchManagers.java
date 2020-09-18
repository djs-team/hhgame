package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/4/8
 */
public class SearchManagers {

    private List<RoomManagers.ListBean> member_list;

    public List<RoomManagers.ListBean> getMember_list() {
        return member_list;
    }

    public void setMember_list(List<RoomManagers.ListBean> member_list) {
        this.member_list = member_list;
    }
}
