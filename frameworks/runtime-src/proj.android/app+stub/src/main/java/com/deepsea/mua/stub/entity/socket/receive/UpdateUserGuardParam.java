package com.deepsea.mua.stub.entity.socket.receive;

public class UpdateUserGuardParam {
    private int GuardState;// 0不是主持守护，1是普通守护，2是榜一守护
    private String GuardHeadImage;

    public int getGuardState() {
        return GuardState;
    }

    public void setGuardState(int guardState) {
        GuardState = guardState;
    }

    public String getGuardHeadImage() {
        return GuardHeadImage;
    }

    public void setGuardHeadImage(String guardHeadImage) {
        GuardHeadImage = guardHeadImage;
    }
}
