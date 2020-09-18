package com.deepsea.mua.stub.entity;

/**
 * Created by JUN on 2019/5/10
 */
public class GuardInfo {
    /**
     * guard_info : {"guard_id":"4","image":"http://192.168.1.210/zhibo/Public/Uploads/gift/4.png","nickname_logo":"2","is_texiao":"2","is_room_out":"2","is_gift":"2","name":"丽丽","room_id":"","user_id":"10060","countdown_day":"5","gold_marks":"1"}
     */
    private GuardBean guard_info;

    public GuardBean getGuard_info() {
        return guard_info;
    }

    public void setGuard_info(GuardBean guard_info) {
        this.guard_info = guard_info;
    }

    public static class GuardBean {
        /**
         * guard_id : 4
         * image : http://192.168.1.210/zhibo/Public/Uploads/gift/4.png
         * nickname_logo : 2
         * is_texiao : 2
         * is_room_out : 2
         * is_gift : 2
         * name : 丽丽
         * room_id :
         * user_id : 10060
         * countdown_day : 5
         * gold_marks : 1
         */
        private String guard_id;
        private String image;
        private String nickname_logo;
        private String is_texiao;
        private String is_room_out;
        private String is_gift;
        private String name;
        private String room_id;
        private String user_id;
        private int countdown_day;
        private String gold_marks;

        public String getGuard_id() {
            return guard_id;
        }

        public void setGuard_id(String guard_id) {
            this.guard_id = guard_id;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getGold_marks() {
            return gold_marks;
        }

        public void setGold_marks(String gold_marks) {
            this.gold_marks = gold_marks;
        }
    }
}
