package com.deepsea.mua.app.im.parse;

import java.util.List;

/**
 * Created by JUN on 2019/5/28
 */
public class HxUser {
    private UserInfoBean user_info;
    private GradeInfoBean grade_info;
    private NumberInfoBean number_info;
    private RoomInfoBean room_info;
    private List<MemberAvatarBean> member_avatar;

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public GradeInfoBean getGrade_info() {
        return grade_info;
    }

    public void setGrade_info(GradeInfoBean grade_info) {
        this.grade_info = grade_info;
    }

    public NumberInfoBean getNumber_info() {
        return number_info;
    }

    public void setNumber_info(NumberInfoBean number_info) {
        this.number_info = number_info;
    }

    public RoomInfoBean getRoom_info() {
        return room_info;
    }

    public void setRoom_info(RoomInfoBean room_info) {
        this.room_info = room_info;
    }

    public List<MemberAvatarBean> getMember_avatar() {
        return member_avatar;
    }

    public void setMember_avatar(List<MemberAvatarBean> member_avatar) {
        this.member_avatar = member_avatar;
    }

    public static class UserInfoBean {
        /**
         * user_id : 10000
         * fansmenustatus : 0
         * username : 15210974319
         * nickname : 测试
         * sex : 3
         * intro : 哈哈哈
         * avatar : http://mapi.57xun.com/Public/Uploads/user/2019-05-23/2fd4c54ed3bbf6c9f2711bd45bdaabf.png
         * status : 1
         * role : 2
         * roomnumber :
         * totalcoin : 100110.00
         * freecoin : 1970.00
         * diamond : 1190.00
         * free_diamond : 0.00
         * birthday : 2019-05-24
         * city : 北京朝阳区
         * attestation : 3
         * lang_approve : 3
         * mobile :
         * online : 1
         * login_time : 2019-05-27 18:29:30
         * twelve_animals : 双子座
         * type : 1
         * is_blocks : 2
         */

        private String user_id;
        private String fansmenustatus;
        private String username;
        private String nickname;
        private String sex;
        private String intro;
        private String avatar;
        private String status;
        private String role;
        private String roomnumber;
        private String totalcoin;
        private String freecoin;
        private String diamond;
        private String free_diamond;
        private String birthday;
        private String city;
        private String attestation;
        private String lang_approve;
        private String mobile;
        private String online;
        private String login_time;
        private String twelve_animals;
        private int type;
        private int is_blocks;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getFansmenustatus() {
            return fansmenustatus;
        }

        public void setFansmenustatus(String fansmenustatus) {
            this.fansmenustatus = fansmenustatus;
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

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getRoomnumber() {
            return roomnumber;
        }

        public void setRoomnumber(String roomnumber) {
            this.roomnumber = roomnumber;
        }

        public String getTotalcoin() {
            return totalcoin;
        }

        public void setTotalcoin(String totalcoin) {
            this.totalcoin = totalcoin;
        }

        public String getFreecoin() {
            return freecoin;
        }

        public void setFreecoin(String freecoin) {
            this.freecoin = freecoin;
        }

        public String getDiamond() {
            return diamond;
        }

        public void setDiamond(String diamond) {
            this.diamond = diamond;
        }

        public String getFree_diamond() {
            return free_diamond;
        }

        public void setFree_diamond(String free_diamond) {
            this.free_diamond = free_diamond;
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

        public String getAttestation() {
            return attestation;
        }

        public void setAttestation(String attestation) {
            this.attestation = attestation;
        }

        public String getLang_approve() {
            return lang_approve;
        }

        public void setLang_approve(String lang_approve) {
            this.lang_approve = lang_approve;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }

        public String getLogin_time() {
            return login_time;
        }

        public void setLogin_time(String login_time) {
            this.login_time = login_time;
        }

        public String getTwelve_animals() {
            return twelve_animals;
        }

        public void setTwelve_animals(String twelve_animals) {
            this.twelve_animals = twelve_animals;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIs_blocks() {
            return is_blocks;
        }

        public void setIs_blocks(int is_blocks) {
            this.is_blocks = is_blocks;
        }
    }

    public static class GradeInfoBean {
        /**
         * lv_dengji : 10
         * lh_dengji : 11
         * level_number : 100110
         * hight_number : 118500
         */

        private int lv_dengji;
        private int lh_dengji;
        private int level_number;
        private String hight_number;

        public int getLv_dengji() {
            return lv_dengji;
        }

        public void setLv_dengji(int lv_dengji) {
            this.lv_dengji = lv_dengji;
        }

        public int getLh_dengji() {
            return lh_dengji;
        }

        public void setLh_dengji(int lh_dengji) {
            this.lh_dengji = lh_dengji;
        }

        public int getLevel_number() {
            return level_number;
        }

        public void setLevel_number(int level_number) {
            this.level_number = level_number;
        }

        public String getHight_number() {
            return hight_number;
        }

        public void setHight_number(String hight_number) {
            this.hight_number = hight_number;
        }
    }

    public static class NumberInfoBean {
        /**
         * attention_count : 6
         * follower_count : 0
         * follower_new : 0
         * history_count : 4
         * history_new : 0
         */

        private String attention_count;
        private String follower_count;
        private String follower_new;
        private String history_count;
        private String history_new;

        public String getAttention_count() {
            return attention_count;
        }

        public void setAttention_count(String attention_count) {
            this.attention_count = attention_count;
        }

        public String getFollower_count() {
            return follower_count;
        }

        public void setFollower_count(String follower_count) {
            this.follower_count = follower_count;
        }

        public String getFollower_new() {
            return follower_new;
        }

        public void setFollower_new(String follower_new) {
            this.follower_new = follower_new;
        }

        public String getHistory_count() {
            return history_count;
        }

        public void setHistory_count(String history_count) {
            this.history_count = history_count;
        }

        public String getHistory_new() {
            return history_new;
        }

        public void setHistory_new(String history_new) {
            this.history_new = history_new;
        }
    }

    public static class RoomInfoBean {
        /**
         * room_id : 5
         * room_name : 招财的小屋
         * avatar : http://mapi.57xun.com/Public/Uploads/user/2019-05-24/5ce76bbf0094b.jpg
         */

        private String room_id;
        private String room_name;
        private String avatar;

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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class MemberAvatarBean {
        /**
         * avatar_id : 2
         * photo_url : http://mapi.57xun.com/style/avatar/87f/7/5baf2e437b5e7.jpg
         */

        private String avatar_id;
        private String photo_url;

        public String getAvatar_id() {
            return avatar_id;
        }

        public void setAvatar_id(String avatar_id) {
            this.avatar_id = avatar_id;
        }

        public String getPhoto_url() {
            return photo_url;
        }

        public void setPhoto_url(String photo_url) {
            this.photo_url = photo_url;
        }
    }
}
