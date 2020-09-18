package com.deepsea.mua.stub.utils.eventbus;

public class ShowRankStepOne {
    private int type;//1-头像 2实名认证
    private int currentPos;

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ShowRankStepOne() {
    }

    public ShowRankStepOne(int currentPos) {
        this.currentPos = currentPos;
    }


}
