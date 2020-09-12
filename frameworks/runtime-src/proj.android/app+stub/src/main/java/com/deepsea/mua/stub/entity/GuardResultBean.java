package com.deepsea.mua.stub.entity;

import java.util.List;

public class GuardResultBean {

    /**
     * list : [{"id":"10347","nickname":"来咯哦哦","avatar":"http://face-test-01.oss-cn-beijing.aliyuncs.com/Avatar/register/1578377713743.jpg","sex":"2","birthday":"1998-01-01 00:00:00","age":22,"num":27},{"id":"10070","nickname":"李晓龙女号","avatar":"http://face-test-01.oss-cn-beijing.aliyuncs.com/Avatar/register/20191203144303.jpg","sex":"2","birthday":"1992-01-01 00:00:00","age":28,"num":11},{"id":"10428","nickname":"懿湾玥牙er～","avatar":"http://face-test-01.oss-cn-beijing.aliyuncs.com//Avatar/10428/1584863725086.jpg","sex":"2","birthday":"1996-01-01 00:00:00","age":24,"num":1}]
     * pageInfo : {"page":"1","pageNum":10,"totalPage":1}
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
         * pageNum : 10
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
         * id : 10347
         * nickname : 来咯哦哦
         * avatar : http://face-test-01.oss-cn-beijing.aliyuncs.com/Avatar/register/1578377713743.jpg
         * sex : 2
         * birthday : 1998-01-01 00:00:00
         * age : 22
         * num : 27
         */

        private String id;
        private String nickname;
        private String avatar;
        private String sex;
        private String birthday;
        private int age;
        private int num;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
