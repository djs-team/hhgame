package com.deepsea.mua.stub.entity;

public class IsCreateRoomVo {
    private int is_show;//入口是否显示1:显示   2:不显示
    private int type;//打开的类型1:主持我的房间   2:申请主持页

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
