package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/5/5
 */
public class FollowFanBean {
    private PageinfoBean pageinfo;
    private List<UserInfoBean> user_info;

    public PageinfoBean getPageinfo() {
        return pageinfo;
    }

    public void setPageinfo(PageinfoBean pageinfo) {
        this.pageinfo = pageinfo;
    }

    public List<UserInfoBean> getUser_info() {
        return user_info;
    }

    public void setUser_info(List<UserInfoBean> user_info) {
        this.user_info = user_info;
    }

    public static class PageinfoBean {
        private int page;
        private int pageNum;
        private int totalPage;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }
    }

    public static class UserInfoBean {
        private String userid;
        private String username;
        private String sex;
        private String nickname;
        private String intro;
        private String status;
        private String avatar;
        private String birthday;
        private String roomnumber;
        private int is_vip;
        private String city;
        private int stature;
        private int age;
        private String state;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getRoomnumber() {
            return roomnumber;
        }

        public void setRoomnumber(String roomnumber) {
            this.roomnumber = roomnumber;
        }

        public int getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(int is_vip) {
            this.is_vip = is_vip;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public int getStature() {
            return stature;
        }

        public void setStature(int stature) {
            this.stature = stature;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
