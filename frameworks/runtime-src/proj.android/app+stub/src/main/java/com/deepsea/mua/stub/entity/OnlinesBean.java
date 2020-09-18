package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/10/15
 */
public class OnlinesBean {




    private List<NearbyBean> list;
    private PageBean pageInfo;

    public List<NearbyBean> getList() {
        return list;
    }

    public void setList(List<NearbyBean> list) {
        this.list = list;
    }

    public PageBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public static class NearbyBean {
        private String id;
        private String sex;
        private String login_time;
        private String is_online;
        private String intro;
        private int age;
        private int stature;
        private String username;
        private String nickname;
        private String city;
        private String avatar;
        private int attention;
        private String lv_dengji;
        private String state;
        private String room_type;

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }

        public String getLv_dengji() {
            return lv_dengji;
        }

        public void setLv_dengji(String lv_dengji) {
            this.lv_dengji = lv_dengji;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getLogin_time() {
            return login_time;
        }

        public void setLogin_time(String login_time) {
            this.login_time = login_time;
        }

        public String getIs_online() {
            return is_online;
        }

        public void setIs_online(String is_online) {
            this.is_online = is_online;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getStature() {
            return stature;
        }

        public void setStature(int stature) {
            this.stature = stature;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getAttention() {
            return attention;
        }

        public void setAttention(int attention) {
            this.attention = attention;
        }
    }
}
