package com.deepsea.mua.stub.entity;

public class CheckBindWx {
    private int is_band;//1:已绑定   2:未绑定
    private String wx_id;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIs_band() {
        return is_band;
    }

    public void setIs_band(int is_band) {
        this.is_band = is_band;
    }

    public String getWx_id() {
        return wx_id;
    }

    public void setWx_id(String wx_id) {
        this.wx_id = wx_id;
    }
}
