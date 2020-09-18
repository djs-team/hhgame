package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/5/9
 */
public class GuardListBean {
    private List<GuardRoomBean> guard_roomlist;
    private List<GuardMemberBean> guard_memberlist;

    public List<GuardRoomBean> getGuard_roomlist() {
        return guard_roomlist;
    }

    public void setGuard_roomlist(List<GuardRoomBean> guard_roomlist) {
        this.guard_roomlist = guard_roomlist;
    }

    public List<GuardMemberBean> getGuard_memberlist() {
        return guard_memberlist;
    }

    public void setGuard_memberlist(List<GuardMemberBean> guard_memberlist) {
        this.guard_memberlist = guard_memberlist;
    }

    public static class GuardRoomBean {
        /**
         * room_id : 32
         * guard_level : 4
         * creat_time : 2019-04-18 15:46:48
         * long_day : 27
         * image : http://192.168.1.210/zhibo/Public/Uploads/gift/4.png
         * room_name : 哈哈哈哈
         * room_image : http://192.168.1.210/zhibohttp://qzapp.qlogo.cn/qzapp/101470077/C3415AED5C0449AF57FEE2925539AD9A/30
         * countdown_day : 25
         * gold_marks : 2
         */

        private String room_id;
        private String guard_level;
        private String creat_time;
        private String long_day;
        private String image;
        private String room_name;
        private String room_image;
        private int countdown_day;
        private String gold_marks;
        private String room_type;

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getGuard_level() {
            return guard_level;
        }

        public void setGuard_level(String guard_level) {
            this.guard_level = guard_level;
        }

        public String getCreat_time() {
            return creat_time;
        }

        public void setCreat_time(String creat_time) {
            this.creat_time = creat_time;
        }

        public String getLong_day() {
            return long_day;
        }

        public void setLong_day(String long_day) {
            this.long_day = long_day;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getRoom_image() {
            return room_image;
        }

        public void setRoom_image(String room_image) {
            this.room_image = room_image;
        }

        public int getCountdown_day() {
            return countdown_day;
        }

        public void setCountdown_day(int countdown_day) {
            this.countdown_day = countdown_day;
        }

        public String getGold_marks() {
            return gold_marks;
        }

        public void setGold_marks(String gold_marks) {
            this.gold_marks = gold_marks;
        }

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }
    }

    public static class GuardMemberBean {
        /**
         * user_id : 10060
         * guard_level : 1
         * creat_time : 2019-04-19 15:54:50
         * long_day : 7
         * image : http://192.168.1.210/zhibo/Public/Uploads/gift/1.png
         * nickname : 丽丽
         * avatar : http://192.168.1.210/zhibohttp://thirdwx.qlogo.cn/mmopen/vi_32/IN2icsuhGqEcXKJHRh0Fg5mNx6g3ugOb8goDlEFJICgWB38K1UczavCAhG64SSK
         * countdown_day : 6
         * gold_marks : 1
         */
        private String user_id;
        private String guard_level;
        private String creat_time;
        private String long_day;
        private String image;
        private String nickname;
        private String avatar;
        private int countdown_day;
        private String gold_marks;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getGuard_level() {
            return guard_level;
        }

        public void setGuard_level(String guard_level) {
            this.guard_level = guard_level;
        }

        public String getCreat_time() {
            return creat_time;
        }

        public void setCreat_time(String creat_time) {
            this.creat_time = creat_time;
        }

        public String getLong_day() {
            return long_day;
        }

        public void setLong_day(String long_day) {
            this.long_day = long_day;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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

        public int getCountdown_day() {
            return countdown_day;
        }

        public void setCountdown_day(int countdown_day) {
            this.countdown_day = countdown_day;
        }

        public String getGold_marks() {
            return gold_marks;
        }

        public void setGold_marks(String gold_marks) {
            this.gold_marks = gold_marks;
        }
    }
}
