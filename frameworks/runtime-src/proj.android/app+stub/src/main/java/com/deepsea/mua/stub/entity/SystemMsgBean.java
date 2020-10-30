package com.deepsea.mua.stub.entity;

public class SystemMsgBean {

    /**
     * system_id : 1
     * fromsend : admin
     * pushmsg : 欢迎新用户
     * pushtime : 2019-10-25 13:54:20
     */

    private String id;
    private String time;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
