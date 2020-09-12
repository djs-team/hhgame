package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/4/19
 */
public class RoomSearchs {

    private List<RoomMsgBean> room_msg;
    private List<RoomMsgBean> room_list;
    private List<MemberListBean> member_list;

    public List<RoomMsgBean> getRoom_msg() {
        return room_msg;
    }

    public void setRoom_msg(List<RoomMsgBean> room_msg) {
        this.room_msg = room_msg;
    }

    public List<RoomMsgBean> getRoom_list() {
        return room_list;
    }

    public void setRoom_list(List<RoomMsgBean> room_list) {
        this.room_list = room_list;
    }

    public List<MemberListBean> getMember_list() {
        return member_list;
    }

    public void setMember_list(List<MemberListBean> member_list) {
        this.member_list = member_list;
    }

    public static class RoomMsgBean {
        /**
         * room_id : 4
         * user_id : 10001
         * room_name : 交友交友
         * room_desc : 我来也
         * room_image : localhost.shopss.com/Public/Uploads/2019-03-26/5c99d07f4e1e1.png
         * room_welcomes :
         * room_type : 2
         * room_tags : 0
         * room_lock : 0
         * visitor_number : 500
         */
        private String room_id;
        private String user_id;
        private String room_name;
        private String room_desc;
        private String room_image;
        private String room_welcomes;
        private String room_type;
        private String room_tags;
        private String room_lock;
        private String visitor_number;
        private String pretty_room_id;

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getRoom_desc() {
            return room_desc;
        }

        public void setRoom_desc(String room_desc) {
            this.room_desc = room_desc;
        }

        public String getRoom_image() {
            return room_image;
        }

        public void setRoom_image(String room_image) {
            this.room_image = room_image;
        }

        public String getRoom_welcomes() {
            return room_welcomes;
        }

        public void setRoom_welcomes(String room_welcomes) {
            this.room_welcomes = room_welcomes;
        }

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }

        public String getRoom_tags() {
            return room_tags;
        }

        public void setRoom_tags(String room_tags) {
            this.room_tags = room_tags;
        }

        public String getRoom_lock() {
            return room_lock;
        }

        public void setRoom_lock(String room_lock) {
            this.room_lock = room_lock;
        }

        public String getVisitor_number() {
            return visitor_number;
        }

        public void setVisitor_number(String visitor_number) {
            this.visitor_number = visitor_number;
        }

        public String getPretty_room_id() {
            return pretty_room_id;
        }

        public void setPretty_room_id(String pretty_room_id) {
            this.pretty_room_id = pretty_room_id;
        }
    }


    public static class MemberListBean {

        /**
         * user_id : 10047
         * nickname : 来一个男嘉宾
         * avatar : http://face-test-01.oss-cn-beijing.aliyuncs.com/Avatar/register/1574044910978.jpg
         * sex : 1
         * lv_dengji : 1
         * is_vip : 0
         * pretty_id : 10047
         * pretty_avatar : http://face-test-01.oss-cn-beijing.aliyuncs.com//upload/boy.png
         * city :
         * city_two :
         * online : 0
         * state : 0
         * login_time : 2019-12-23 15:03:49
         * room_id :
         * online_str : 离线
         * age : 44
         */

        private String user_id;
        private String nickname;
        private String avatar;
        private String sex;
        private int lv_dengji;
        private int is_vip;
        private String pretty_id;
        private String pretty_avatar;
        private String city;
        private String city_two;
        private String online;
        private String state;
        private String login_time;
        private String room_id;
        private String online_str;
        private int age;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getLv_dengji() {
            return lv_dengji;
        }

        public void setLv_dengji(int lv_dengji) {
            this.lv_dengji = lv_dengji;
        }

        public int getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(int is_vip) {
            this.is_vip = is_vip;
        }

        public String getPretty_id() {
            return pretty_id;
        }

        public void setPretty_id(String pretty_id) {
            this.pretty_id = pretty_id;
        }

        public String getPretty_avatar() {
            return pretty_avatar;
        }

        public void setPretty_avatar(String pretty_avatar) {
            this.pretty_avatar = pretty_avatar;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity_two() {
            return city_two;
        }

        public void setCity_two(String city_two) {
            this.city_two = city_two;
        }

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getLogin_time() {
            return login_time;
        }

        public void setLogin_time(String login_time) {
            this.login_time = login_time;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getOnline_str() {
            return online_str;
        }

        public void setOnline_str(String online_str) {
            this.online_str = online_str;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
