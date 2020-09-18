package com.deepsea.mua.stub.entity;

public class MicroUserBean {
    private String uid;
    private String uName;
    private String uHeadUrl;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getuHeadUrl() {
        return uHeadUrl;
    }

    public void setuHeadUrl(String uHeadUrl) {
        this.uHeadUrl = uHeadUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public MicroUserBean() {

    }

    public MicroUserBean(String uid, String uName, String uHeadUrl, int type) {
        this.uid = uid;
        this.uName = uName;
        this.uHeadUrl = uHeadUrl;
        this.type = type;
    }
}
