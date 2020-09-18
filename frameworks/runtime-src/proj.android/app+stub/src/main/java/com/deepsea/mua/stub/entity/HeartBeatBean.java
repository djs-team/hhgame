package com.deepsea.mua.stub.entity;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class HeartBeatBean {


    /**
     * isRequest : 0
     * list : [{"id":"36","inviterId":"1069232","inviteeId":"1069195","roomId":"102576","state":"0","createTime":"2019-11-05 14:59:58","free":"1","hongId":"1069232","guestId":"","guestName":"","guestAvatar":"","sex":"","city":"","stature":"","age":"","hongName":"红娘","hongAvatar":"http://image.xiaokongping.comAvatar/register/1572488374314.jpg","upCost":false}]
     */

    private int isRequest;
    private List<InviteListBean> list;

    public int getIsRequest() {
        return isRequest;
    }

    public void setIsRequest(int isRequest) {
        this.isRequest = isRequest;
    }

    public List<InviteListBean> getList() {
        return list;
    }

    public void setList(List<InviteListBean> list) {
        this.list = list;
    }

    public static class InviteListBean implements Serializable {

        /**
         * id : 26970
         * inviterId : 10045
         * inviteeId : 10115
         * roomId : 118
         * state : 0
         * createTime : 2020-06-03 17:33:29
         * free : 2
         * micro_level : 0
         * room_mode : 3
         * micro_cost : 10
         * hongId : 10045
         * guestId :
         * guestName :
         * guestAvatar :
         * sex :
         * city :
         * stature :
         * age :
         * hongName : 我是女嘉宾
         * hongAvatar : http://face-test-01.oss-cn-beijing.aliyuncs.com/Avatar/100103/1573906210934.jpg
         * upCost : 10
         */

        private String id;
        private String inviterId;
        private String inviteeId;
        private String roomId;
        private String state;
        private String createTime;
        private String free;
        private String micro_level;
        private String room_mode;
        private String micro_cost;
        private String hongId;
        private String guestId;
        private String guestName;
        private String guestAvatar;
        private String sex;
        private String city;
        private String stature;
        private String age;
        private String hongName;
        private String hongAvatar;
        private String upCost;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInviterId() {
            return inviterId;
        }

        public void setInviterId(String inviterId) {
            this.inviterId = inviterId;
        }

        public String getInviteeId() {
            return inviteeId;
        }

        public void setInviteeId(String inviteeId) {
            this.inviteeId = inviteeId;
        }

        public String getRoomId() {
            return roomId;
        }

        public void setRoomId(String roomId) {
            this.roomId = roomId;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getMicro_level() {
            return micro_level;
        }

        public void setMicro_level(String micro_level) {
            this.micro_level = micro_level;
        }

        public String getRoom_mode() {
            return room_mode;
        }

        public void setRoom_mode(String room_mode) {
            this.room_mode = room_mode;
        }

        public String getMicro_cost() {
            return micro_cost;
        }

        public void setMicro_cost(String micro_cost) {
            this.micro_cost = micro_cost;
        }

        public String getHongId() {
            return hongId;
        }

        public void setHongId(String hongId) {
            this.hongId = hongId;
        }

        public String getGuestId() {
            return guestId;
        }

        public void setGuestId(String guestId) {
            this.guestId = guestId;
        }

        public String getGuestName() {
            return guestName;
        }

        public void setGuestName(String guestName) {
            this.guestName = guestName;
        }

        public String getGuestAvatar() {
            return guestAvatar;
        }

        public void setGuestAvatar(String guestAvatar) {
            this.guestAvatar = guestAvatar;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStature() {
            return stature;
        }

        public void setStature(String stature) {
            this.stature = stature;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getHongName() {
            return hongName;
        }

        public void setHongName(String hongName) {
            this.hongName = hongName;
        }

        public String getHongAvatar() {
            return hongAvatar;
        }

        public void setHongAvatar(String hongAvatar) {
            this.hongAvatar = hongAvatar;
        }

        public String getUpCost() {
            return upCost;
        }

        public void setUpCost(String upCost) {
            this.upCost = upCost;
        }
    }

}
