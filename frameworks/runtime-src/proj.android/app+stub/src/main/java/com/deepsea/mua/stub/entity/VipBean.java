package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/5/9
 */
public class VipBean {
    /**
     * user_info : {"user_id":"10000","username":"15210974319","sex":"3","nickname":"1006010974319","intro":null,"long_day":"60","vip_buytime":"2019-04-25 11:15:53","vip_status":1,"vip_dengji":2,"vip_image":"localhost.shopss.com/Public/Uploads/gift/2.png","vip_next_dengji":3,"exp_values":"68050","exp_next_values":"150000","percentages":80}
     * vip_list : [{"vip_id":"1","exp":"0","vip_image":"localhost.shopss.com/Public/Uploads/gift/1.png","chat_logo":"2","head_logo":"2","wheat_logo":"2","car_logo":"1","gift_logo":"1","first_logo":"1","notice_logo":"1"},{"vip_id":"2","exp":"60000","vip_image":"localhost.shopss.com/Public/Uploads/gift/2.png","chat_logo":"2","head_logo":"2","wheat_logo":"2","car_logo":"2","gift_logo":"1","first_logo":"1","notice_logo":"1"},{"vip_id":"3","exp":"150000","vip_image":"localhost.shopss.com/Public/Uploads/gift/3.png","chat_logo":"2","head_logo":"2","wheat_logo":"2","car_logo":"2","gift_logo":"2","first_logo":"1","notice_logo":"1"},{"vip_id":"4","exp":"400000","vip_image":"localhost.shopss.com/Public/Uploads/gift/4.png","chat_logo":"2","head_logo":"2","wheat_logo":"2","car_logo":"2","gift_logo":"2","first_logo":"1","notice_logo":"1"},{"vip_id":"5","exp":"800000","vip_image":"localhost.shopss.com/Public/Uploads/gift/5.png","chat_logo":"2","head_logo":"2","wheat_logo":"2","car_logo":"2","gift_logo":"2","first_logo":"2","notice_logo":"1"},{"vip_id":"6","exp":"1200000","vip_image":"localhost.shopss.com/Public/Uploads/gift/6.png","chat_logo":"2","head_logo":"2","wheat_logo":"2","car_logo":"2","gift_logo":"2","first_logo":"2","notice_logo":"1"},{"vip_id":"7","exp":"1800000","vip_image":"localhost.shopss.com/Public/Uploads/gift/7.png","chat_logo":"2","head_logo":"2","wheat_logo":"2","car_logo":"2","gift_logo":"2","first_logo":"2","notice_logo":"2"}]
     * vip_chargelist : [{"ids":"1","vip_rmb":"20.00","vip_days":"1","vip_content":"VIP包月"},{"ids":"2","vip_rmb":"58.00","vip_days":"3","vip_content":"VIP包季"},{"ids":"3","vip_rmb":"150.00","vip_days":"12","vip_content":"VIP包年"}]
     */
    private UserInfoBean user_info;
    private List<ChargeTypeBean> vip_chargelist;

    public List<ChargeTypeBean> getVip_chargelist() {
        return vip_chargelist;
    }

    public void setVip_chargelist(List<ChargeTypeBean> vip_chargelist) {
        this.vip_chargelist = vip_chargelist;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public static class UserInfoBean {
        /**
         * user_id : 10000
         * username : 15210974319
         * sex : 3
         * nickname : 1006010974319
         * intro : null
         * long_day : 60
         * vip_buytime : 2019-04-25 11:15:53
         * vip_status : 1
         * vip_dengji : 2
         * vip_image : localhost.shopss.com/Public/Uploads/gift/2.png
         * vip_next_dengji : 3
         * exp_values : 68050
         * exp_next_values : 150000
         * percentages : 80
         */

        private String user_id;
        private String username;
        private String sex;
        private String nickname;
        private String intro;
        private int long_day;
        private String vip_buytime;
        private int vip_status;
        private int vip_dengji;
        private String vip_image;
        private int vip_next_dengji;
        private String exp_values;
        private String exp_next_values;
        private String percentages;
        private String avatar;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getLong_day() {
            return long_day;
        }

        public void setLong_day(int long_day) {
            this.long_day = long_day;
        }

        public String getVip_buytime() {
            return vip_buytime;
        }

        public void setVip_buytime(String vip_buytime) {
            this.vip_buytime = vip_buytime;
        }

        public int getVip_status() {
            return vip_status;
        }

        public void setVip_status(int vip_status) {
            this.vip_status = vip_status;
        }

        public int getVip_dengji() {
            return vip_dengji;
        }

        public void setVip_dengji(int vip_dengji) {
            this.vip_dengji = vip_dengji;
        }

        public String getVip_image() {
            return vip_image;
        }

        public void setVip_image(String vip_image) {
            this.vip_image = vip_image;
        }

        public int getVip_next_dengji() {
            return vip_next_dengji;
        }

        public void setVip_next_dengji(int vip_next_dengji) {
            this.vip_next_dengji = vip_next_dengji;
        }

        public String getExp_values() {
            return exp_values;
        }

        public void setExp_values(String exp_values) {
            this.exp_values = exp_values;
        }

        public String getExp_next_values() {
            return exp_next_values;
        }

        public void setExp_next_values(String exp_next_values) {
            this.exp_next_values = exp_next_values;
        }

        public String getPercentages() {
            return percentages;
        }

        public void setPercentages(String percentages) {
            this.percentages = percentages;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class ChargeTypeBean {
        /**
         * ids : 1
         * vip_rmb : 20.00
         * vip_days : 1
         * vip_content : VIP包月
         */

        private String ids;
        private String vip_rmb;
        private int vip_days;
        private String vip_content;

        public String getIds() {
            return ids;
        }

        public void setIds(String ids) {
            this.ids = ids;
        }

        public String getVip_rmb() {
            return vip_rmb;
        }

        public void setVip_rmb(String vip_rmb) {
            this.vip_rmb = vip_rmb;
        }

        public int getVip_days() {
            return vip_days;
        }

        public void setVip_days(int vip_days) {
            this.vip_days = vip_days;
        }

        public String getVip_content() {
            return vip_content;
        }

        public void setVip_content(String vip_content) {
            this.vip_content = vip_content;
        }
    }
}
