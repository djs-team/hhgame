package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class GetGuardItemListParam {
    private int UserCount;
    private int AllPage;
    private List<GuardItem> GuardItems;
    private boolean IsGuard;
    private String GuardHead;
    private String GuardName;

    public boolean isGuard() {
        return IsGuard;
    }

    public void setGuard(boolean guard) {
        IsGuard = guard;
    }

    public String getGuardHead() {
        return GuardHead;
    }

    public void setGuardHead(String guardHead) {
        GuardHead = guardHead;
    }

    public String getGuardName() {
        return GuardName;
    }

    public void setGuardName(String guardName) {
        GuardName = guardName;
    }

    public int getUserCount() {
        return UserCount;
    }

    public void setUserCount(int userCount) {
        UserCount = userCount;
    }

    public int getAllPage() {
        return AllPage;
    }

    public void setAllPage(int allPage) {
        AllPage = allPage;
    }

    public List<GuardItem> getGuardItems() {
        return GuardItems;
    }

    public void setGuardItems(List<GuardItem> guardItems) {
        GuardItems = guardItems;
    }
}
