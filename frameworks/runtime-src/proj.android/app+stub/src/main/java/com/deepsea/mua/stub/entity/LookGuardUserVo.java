package com.deepsea.mua.stub.entity;

import java.util.List;

public class LookGuardUserVo {

    /**
     * guard_memberlist : [{"userId":"10045","guard_level":"1","creat_time":"2020-08-25 15:45:12","long_day":"30","is_auto":"2","intimacy":"0","image":" ","online":"1","state":"0","nickname":"我是女嘉宾","avatar":"http://face-test-01.oss-cn-beijing.aliyuncs.com//Avatar/10045/1597310057213.jpg","intro":"","sex":"2","birthday":"1988-01-01 00:00:00","city":" ","city_two":"","room_id":"","age":32,"end_time":"2020-09-24 15:45:12","countdown_day":29}]
     * pageInfo : {"page":1,"pageNum":20,"totalPage":1}
     * is_guard : 2
     * user_info : {"id":"12069","nickname":"游客0570336","avatar":"","sex":"1","age":19,"city":"","city_two":""}
     */

    private PageInfoBean pageInfo;
    private int is_guard;
    private UserInfoBean user_info;
    private List<GuardMemberlistBean> guard_memberlist;

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public int getIs_guard() {
        return is_guard;
    }

    public void setIs_guard(int is_guard) {
        this.is_guard = is_guard;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public List<GuardMemberlistBean> getGuard_memberlist() {
        return guard_memberlist;
    }

    public void setGuard_memberlist(List<GuardMemberlistBean> guard_memberlist) {
        this.guard_memberlist = guard_memberlist;
    }

    public static class PageInfoBean {
        /**
         * page : 1
         * pageNum : 20
         * totalPage : 1
         */

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
        /**
         * id : 12069
         * nickname : 游客0570336
         * avatar :
         * sex : 1
         * age : 19
         * city :
         * city_two :
         */

        private String id;
        private String nickname;
        private String avatar;
        private String sex;
        private int age;
        private String city;
        private String city_two;

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

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity_two() {
            return city_two;
        }

        public void setCity_two(String city_two) {
            this.city_two = city_two;
        }
    }

    public static class GuardMemberlistBean {
        /**
         * userId : 10045
         * guard_level : 1
         * creat_time : 2020-08-25 15:45:12
         * long_day : 30
         * is_auto : 2
         * intimacy : 0
         * image :
         * online : 1
         * state : 0
         * nickname : 我是女嘉宾
         * avatar : http://face-test-01.oss-cn-beijing.aliyuncs.com//Avatar/10045/1597310057213.jpg
         * intro :
         * sex : 2
         * birthday : 1988-01-01 00:00:00
         * city :
         * city_two :
         * room_id :
         * age : 32
         * end_time : 2020-09-24 15:45:12
         * countdown_day : 29
         */

        private String userId;
        private String guard_level;
        private String creat_time;
        private String long_day;
        private String is_auto;
        private String intimacy;
        private String image;
        private String online;
        private String state;
        private String nickname;
        private String avatar;
        private String intro;
        private String sex;
        private String birthday;
        private String city;
        private String city_two;
        private String room_id;
        private int age;
        private String end_time;
        private int countdown_day;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getGuard_level() {
            return guard_level;
        }

        public void setGuard_level(String guard_level) {
            this.guard_level = guard_level;
        }

        public String getCreat_time() {
            return creat_time;
        }

        public void setCreat_time(String creat_time) {
            this.creat_time = creat_time;
        }

        public String getLong_day() {
            return long_day;
        }

        public void setLong_day(String long_day) {
            this.long_day = long_day;
        }

        public String getIs_auto() {
            return is_auto;
        }

        public void setIs_auto(String is_auto) {
            this.is_auto = is_auto;
        }

        public String getIntimacy() {
            return intimacy;
        }

        public void setIntimacy(String intimacy) {
            this.intimacy = intimacy;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
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

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
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

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity_two() {
            return city_two;
        }

        public void setCity_two(String city_two) {
            this.city_two = city_two;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public int getCountdown_day() {
            return countdown_day;
        }

        public void setCountdown_day(int countdown_day) {
            this.countdown_day = countdown_day;
        }
    }
}
