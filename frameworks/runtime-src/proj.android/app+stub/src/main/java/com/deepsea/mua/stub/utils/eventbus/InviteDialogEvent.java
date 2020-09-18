package com.deepsea.mua.stub.utils.eventbus;

import com.deepsea.mua.stub.entity.HeartBeatBean;

import java.util.List;

public class InviteDialogEvent {
    private int isRequest;
    public List<HeartBeatBean.InviteListBean> inviteListBeans;

    public int getIsRequest() {
        return isRequest;
    }

    public void setIsRequest(int isRequest) {
        this.isRequest = isRequest;
    }

    public InviteDialogEvent(int isRequest) {
        this.isRequest = isRequest;
    }

    public InviteDialogEvent() {
    }

    public InviteDialogEvent(List<HeartBeatBean.InviteListBean> inviteListBeans) {
        this.inviteListBeans = inviteListBeans;
    }
}
