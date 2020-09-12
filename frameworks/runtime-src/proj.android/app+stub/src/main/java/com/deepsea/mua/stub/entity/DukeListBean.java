package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/5/9
 */
public class DukeListBean {
    private List<DukeBean> roomduke_list;

    public List<DukeBean> getRoomduke_list() {
        return roomduke_list;
    }

    public void setRoomduke_list(List<DukeBean> roomduke_list) {
        this.roomduke_list = roomduke_list;
    }

    public static class DukeBean {
        /**
         * room_id : 32
         * coin : 1000.00
         * room_name : 哈哈哈哈
         * room_image : localhost.shopss.comhttp://qzapp.qlogo.cn/qzapp/101470077/C3415AED5C0449AF57FEE2925539AD9A/30
         * duke_id : 1
         * duke_image : localhost.shopss.com/Public/Uploads/gift/3.png
         */

        private String room_id;
        private String coin;
        private String room_name;
        private String room_image;
        private String duke_id;
        private String duke_image;
        private String room_type;

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
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

        public String getDuke_id() {
            return duke_id;
        }

        public void setDuke_id(String duke_id) {
            this.duke_id = duke_id;
        }

        public String getDuke_image() {
            return duke_image;
        }

        public void setDuke_image(String duke_image) {
            this.duke_image = duke_image;
        }

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }
    }
}
