package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/5/10
 */
public class GuardTypeBean {
    /**
     * guard_info : {"name":"哈哈哈哈","image":"http://192.168.1.210/zhibo/Public/Uploads/gift/4.png","room_id":"32","user_id":"","countdown_day":"5"}
     * guard_list : [{"guard_id":"4","long_time":"7","coin":"300","image":"http://192.168.1.210/zhibo/Public/Uploads/gift/4.png","nickname_logo":"2","is_texiao":"2","is_room_out":"2","is_gift":"2","type":"0","gold_marks":"1"},{"guard_id":"5","long_time":"30","coin":"8300","image":"http://192.168.1.210/zhibo/Public/Uploads/gift/4.png","nickname_logo":"2","is_texiao":"2","is_room_out":"2","is_gift":"2","type":"0","gold_marks":"2"},{"guard_id":"6","long_time":"90","coin":"88300","image":"http://192.168.1.210/zhibo/Public/Uploads/gift/5.png","nickname_logo":"2","is_texiao":"2","is_room_out":"2","is_gift":"2","type":"0","gold_marks":"3"}]
     */

    private GuardInfoBean guard_info;
    private List<GuardListBean> guard_list;

    public GuardInfoBean getGuard_info() {
        return guard_info;
    }

    public void setGuard_info(GuardInfoBean guard_info) {
        this.guard_info = guard_info;
    }

    public List<GuardListBean> getGuard_list() {
        return guard_list;
    }

    public void setGuard_list(List<GuardListBean> guard_list) {
        this.guard_list = guard_list;
    }

    public static class GuardInfoBean {
        /**
         * name : 哈哈哈哈
         * image : http://192.168.1.210/zhibo/Public/Uploads/gift/4.png
         * room_id : 32
         * user_id :
         * countdown_day : 5
         */

        private String name;
        private String image;
        private String room_id;
        private String user_id;
        private int countdown_day;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

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

        public int getCountdown_day() {
            return countdown_day;
        }

        public void setCountdown_day(int countdown_day) {
            this.countdown_day = countdown_day;
        }
    }

    public static class GuardListBean {
        /**
         * guard_id : 4
         * long_time : 7
         * coin : 300
         * image : http://192.168.1.210/zhibo/Public/Uploads/gift/4.png
         * nickname_logo : 2
         * is_texiao : 2
         * is_room_out : 2
         * is_gift : 2
         * type : 0
         * gold_marks : 1
         */

        private String guard_id;
        private String long_time;
        private String coin;
        private String image;
        private String nickname_logo;
        private String is_texiao;
        private String is_room_out;
        private String is_gift;
        private String type;
        private String gold_marks;

        public String getGuard_id() {
            return guard_id;
        }

        public void setGuard_id(String guard_id) {
            this.guard_id = guard_id;
        }

        public String getLong_time() {
            return long_time;
        }

        public void setLong_time(String long_time) {
            this.long_time = long_time;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getNickname_logo() {
            return nickname_logo;
        }

        public void setNickname_logo(String nickname_logo) {
            this.nickname_logo = nickname_logo;
        }

        public String getIs_texiao() {
            return is_texiao;
        }

        public void setIs_texiao(String is_texiao) {
            this.is_texiao = is_texiao;
        }

        public String getIs_room_out() {
            return is_room_out;
        }

        public void setIs_room_out(String is_room_out) {
            this.is_room_out = is_room_out;
        }

        public String getIs_gift() {
            return is_gift;
        }

        public void setIs_gift(String is_gift) {
            this.is_gift = is_gift;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getGold_marks() {
            return gold_marks;
        }

        public void setGold_marks(String gold_marks) {
            this.gold_marks = gold_marks;
        }
    }
}
