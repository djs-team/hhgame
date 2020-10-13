package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.WsUser;

public class GuardItem {
    private WsUser UserInfo;
    private String DeadlineTime;
    private int Days;
    private  int Intimacy;

    public int getDays() {
        return Days;
    }

    public void setDays(int days) {
        Days = days;
    }

    public WsUser getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(WsUser userInfo) {
        UserInfo = userInfo;
    }

    public String getDeadlineTime() {
        return DeadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        DeadlineTime = deadlineTime;
    }

    public int getIntimacy() {
        return Intimacy;
    }

    public void setIntimacy(int intimacy) {
        Intimacy = intimacy;
    }
}
