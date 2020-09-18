package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/4/19
 */
public class MineRooms {
    private List<MyroomListBean> myroom_list;
    private List<MyroomListBean> myroom_member;

    public List<MyroomListBean> getMyroom_list() {
        return myroom_list;
    }

    public void setMyroom_list(List<MyroomListBean> myroom_list) {
        this.myroom_list = myroom_list;
    }

    public List<MyroomListBean> getMyroom_member() {
        return myroom_member;
    }

    public void setMyroom_member(List<MyroomListBean> myroom_member) {
        this.myroom_member = myroom_member;
    }

    public static class MyroomListBean {
        /**
         * room_id : 31
         * room_name : gagaga
         * room_type : 派单
         * room_lock : 0
         * visitor_number : 0
         * is_live : 0
         * room_image : localhost.shopss.com/Public/Uploads/image/logo.png
         * nickname : 1006010974319
         */

        private String room_id;
        private String room_name;
        private String room_type;
        private String room_lock;
        private String visitor_number;
        private String is_live;
        private String room_image;
        private String nickname;
        private boolean isOwner;

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
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

        public String getIs_live() {
            return is_live;
        }

        public void setIs_live(String is_live) {
            this.is_live = is_live;
        }

        public String getRoom_image() {
            return room_image;
        }

        public void setRoom_image(String room_image) {
            this.room_image = room_image;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public boolean isOwner() {
            return isOwner;
        }

        public void setOwner(boolean owner) {
            isOwner = owner;
        }
    }
}
