package com.deepsea.mua.stub.utils.eventbus;

import com.deepsea.mua.stub.entity.HeartBeatBean;

import java.util.List;

public class InviteOtherEvent {
    private int isRequest;
    public List<HeartBeatBean.InviteListBean> inviteListBeans;

    public int getIsRequest() {
        return isRequest;
    }

    public void setIsRequest(int isRequest) {
        this.isRequest = isRequest;
    }

    public InviteOtherEvent(int isRequest) {
        this.isRequest = isRequest;
    }

    public InviteOtherEvent() {
    }

    public InviteOtherEvent(List<HeartBeatBean.InviteListBean> inviteListBeans) {
        this.inviteListBeans = inviteListBeans;
    }
}
