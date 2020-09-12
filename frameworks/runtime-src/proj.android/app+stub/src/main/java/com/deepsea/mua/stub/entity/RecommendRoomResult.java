package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * 作者：liyaxing  2019/8/23 19:15
 * 类别 ：
 */
public class RecommendRoomResult {


    /**
     * list : [{"id":"116","room_type":"5","avatar":"Avatar/register/1578972909463.jpg","stature":"0","birthday":"2001-01-01 00:00:00","sex":"2","state":1,"age":26},{"id":"118","room_type":"5","avatar":"Avatar/100103/1573906210934.jpg","stature":"177","birthday":"1988-01-01 00:00:00","sex":"2","age":32},{"id":"187","room_type":"5","avatar":"Avatar/register/1573573193458.jpg","stature":"0","birthday":"2001-01-01 00:00:00","sex":"2","age":19},{"id":"201","room_type":"5","avatar":"Avatar/register/1573573073660.jpg","stature":"0","birthday":"2001-01-01 00:00:00","sex":"2","age":19}]
     * pageInfo : {"page":"1","pageNum":20,"totalPage":1}
     */

    private PageInfoBean pageInfo;
    private List<ListBean> list;

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class PageInfoBean {
        /**
         * page : 1
         * pageNum : 20
         * totalPage : 1
         */

        private String page;
        private int pageNum;
        private int totalPage;

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
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

    public static class ListBean {

        /**
         * id : 116
         * room_type : 5
         * avatar : Avatar/register/1578972909463.jpg
         * stature : 0
         * birthday : 2001-01-01 00:00:00
         * sex : 2
         * state : 1
         * age : 26
         * nickname : 阿斯蒂芬
         */

        private String id;
        private String room_type;
        private String avatar;
        private String stature;
        private String birthday;
        private String sex;
        private int state;
        private int age;
        private String nickname;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getStature() {
            return stature;
        }

        public void setStature(String stature) {
            this.stature = stature;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}

