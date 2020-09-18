package com.deepsea.mua.stub.utils.eventbus;

public class ShowMineDialog {
    private int type;  //0 头像  1  实名认证

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ShowMineDialog() {
    }

    public ShowMineDialog(int type) {
        this.type = type;
    }
}
