package com.deepsea.mua.stub.entity;

import java.util.List;

public class FriendInfoListBean {
    /**
     * system_time : {"pushtime":"2019-10-24 14:01:15"}
     * list : [{"user_id":"1069198","nickname":"滨州","avatar":"http://image.xiaokongping.com/Avatar/register/1571908352498.jpg","sex":"2","city":null,"online":"1","age":18}]
     */

    private SystemTimeBean system_time;
    List<FriendInfoBean> list;

    public List<FriendInfoBean> getList() {
        return list;
    }

    public void setList(List<FriendInfoBean> list) {
        this.list = list;
    }

    public SystemTimeBean getSystem_time() {
        return system_time;
    }

    public void setSystem_time(SystemTimeBean system_time) {
        this.system_time = system_time;
    }


    public static class SystemTimeBean {
        /**
         * pushtime : 2019-10-24 14:01:15
         */

        private String pushtime;

        public String getPushtime() {
            return pushtime;
        }

        public void setPushtime(String pushtime) {
            this.pushtime = pushtime;
        }
    }


//    List<FriendInfoBean> list;
//    private String system_time;
//
//    public String getSystem_time() {
//        return system_time;
//    }
//
//    public void setSystem_time(String system_time) {
//        this.system_time = system_time;
//    }
//    public List<FriendInfoBean> getList() {
//        return list;
//    }
//
//    public void setList(List<FriendInfoBean> list) {
//        this.list = list;
//    }


}
