package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/4/17
 */
public class HomeInfo {

    private List<RoomList> room_list;

    public List<RoomList> getRoom_list() {
        return room_list;
    }

    public void setRoom_list(List<RoomList> room_list) {
        this.room_list = room_list;
    }

    public static class RoomList {

        private String name;
        private String id;
        private List<RoomBean> list;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<RoomBean> getList() {
            return list;
        }

        public void setList(List<RoomBean> list) {
            this.list = list;
        }
    }

    public static class RoomBean {
        private String room_id;
        private String visitor_number;
        private String room_name;
        private String room_type;
        private String room_tags;
        //锁定状态:0未锁定 1锁定
        private String room_lock;
        private String fm_room_image;
        private String fm_nickname;
        private String fm_sex;
        private int fm_age;
        private String fm_city;
        private String maker_avatar;
        private String maker_nickname;
        private int xq_type;
        private int right_corn;//右上角图标：0:不显示   1:红包

        public int getRight_corn() {
            return right_corn;
        }

        public void setRight_corn(int right_corn) {
            this.right_corn = right_corn;
        }

        public int getXq_type() {
            return xq_type;
        }

        public void setXq_type(int xq_type) {
            this.xq_type = xq_type;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getVisitor_number() {
            return visitor_number;
        }

        public void setVisitor_number(String visitor_number) {
            this.visitor_number = visitor_number;
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

        public String getFm_room_image() {
            return fm_room_image;
        }

        public void setFm_room_image(String fm_room_image) {
            this.fm_room_image = fm_room_image;
        }

        public String getFm_nickname() {
            return fm_nickname;
        }

        public void setFm_nickname(String fm_nickname) {
            this.fm_nickname = fm_nickname;
        }

        public String getFm_sex() {
            return fm_sex;
        }

        public void setFm_sex(String fm_sex) {
            this.fm_sex = fm_sex;
        }

        public int getFm_age() {
            return fm_age;
        }

        public void setFm_age(int fm_age) {
            this.fm_age = fm_age;
        }

        public String getFm_city() {
            return fm_city;
        }

        public void setFm_city(String fm_city) {
            this.fm_city = fm_city;
        }

        public String getMaker_avatar() {
            return maker_avatar;
        }

        public void setMaker_avatar(String maker_avatar) {
            this.maker_avatar = maker_avatar;
        }

        public String getMaker_nickname() {
            return maker_nickname;
        }

        public void setMaker_nickname(String maker_nickname) {
            this.maker_nickname = maker_nickname;
        }
    }
}
