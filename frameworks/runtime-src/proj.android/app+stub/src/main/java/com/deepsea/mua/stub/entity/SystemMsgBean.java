package com.deepsea.mua.stub.entity;

public class SystemMsgBean {

    /**
     * system_id : 1
     * fromsend : admin
     * pushmsg : 欢迎新用户
     * pushtime : 2019-10-25 13:54:20
     */

    private String system_id;
    private String fromsend;
    private String pushmsg;
    private String pushtime;

    public String getSystem_id() {
        return system_id;
    }

    public void setSystem_id(String system_id) {
        this.system_id = system_id;
    }

    public String getFromsend() {
        return fromsend;
    }

    public void setFromsend(String fromsend) {
        this.fromsend = fromsend;
    }

    public String getPushmsg() {
        return pushmsg;
    }

    public void setPushmsg(String pushmsg) {
        this.pushmsg = pushmsg;
    }

    public String getPushtime() {
        return pushtime;
    }

    public void setPushtime(String pushtime) {
        this.pushtime = pushtime;
    }
}
