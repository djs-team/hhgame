package com.deepsea.mua.stub.entity;

public class InviteCodeBean {
    /**
     * code : 200
     * desc : 操作成功
     * data : {"url":"http://10.66.1.100:1103/Public/Download/index.html?referrercode=0"}
     */
    private String url;
    private String referrercode;

    public String getReferrercode() {
        return referrercode;
    }

    public void setReferrercode(String referrercode) {
        this.referrercode = referrercode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
