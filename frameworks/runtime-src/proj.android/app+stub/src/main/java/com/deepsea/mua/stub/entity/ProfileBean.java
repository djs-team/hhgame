package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/4/12
 */
public class ProfileBean {
    private UserInfoBean user_info;
    private GradeInfoBean grade_info;
    private NumberInfoBean number_info;
    private RoomInfoBean room_info;
    private List<MemberAvatarBean> member_avatar;
    private List<FansRank> rank_list;
    private SafetyBean monitoring_info;
    public List<PresentWallBean> rank_gift;
    public int gift_num;
    private int bind_type;

    public int getBind_type() {
        return bind_type;
    }

    public void setBind_type(int bind_type) {
        this.bind_type = bind_type;
    }

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

    public List<FansRank> getRank_list() {
        return rank_list;
    }

    public void setRank_list(List<FansRank> rank_list) {
        this.rank_list = rank_list;
    }

    public SafetyBean getMonitoring_info() {
        return monitoring_info;
    }

    public void setMonitoring_info(SafetyBean monitoring_info) {
        this.monitoring_info = monitoring_info;
    }

    public static class UserInfoBean {
        private String user_id;
        private String fansmenustatus;
        private String username;
        private String nickname;
        private String sex;
        private String intro;
        private String avatar;
        private int is_vip;
        private String status;
        private String role;
        private String roomnumber;
        private String totalcoin;
        private String diamond;
        private String birthday;
        private String city;
        private String city_two;
        private int age;
        private String attestation;
        private int lang_approve;
        private String mobile;
        private String login_status;
        private String login_time;
        private String twelve_animals;
        private String type;
        private String is_blocks;
        private String pretty_id;
        private String pretty_avatar;
        private String register_time;
        private String login_ip;
        private String education;
        private String marital_status;
        private String pay;
        private String profession;
        private String housing_status;
        private String charm_part;
        private String blood_type;
        private String together;
        private String live_together_marrge;
        private String friend_city;
        private String friend_age;
        private String friend_stature;
        private String friend_education;
        private String friend_pay;
        private int stature;
        private String is_friend;
        private String is_matchmaker;
        private String online;
        private String belongId;
        private String belongName;
        private String hobby;
        private String feature;
        private String coin_num;
        private int is_bind;
        private String shouhu_avatar;

        public String getCity_two() {
            return city_two;
        }

        public void setCity_two(String city_two) {
            this.city_two = city_two;
        }

        public String getShouhu_avatar() {
            return shouhu_avatar;
        }

        public void setShouhu_avatar(String shouhu_avatar) {
            this.shouhu_avatar = shouhu_avatar;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getCoin_num() {
            return coin_num;
        }

        public void setCoin_num(String coin_num) {
            this.coin_num = coin_num;
        }

        public int getIs_bind() {
            return is_bind;
        }

        public void setIs_bind(int is_bind) {
            this.is_bind = is_bind;
        }

        public String getBelongId() {
            return belongId;
        }

        public void setBelongId(String belongId) {
            this.belongId = belongId;
        }

        public String getBelongName() {
            return belongName;
        }

        public void setBelongName(String belongName) {
            this.belongName = belongName;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }


        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }

        public String getIs_matchmaker() {
            return is_matchmaker;
        }

        public void setIs_matchmaker(String is_matchmaker) {
            this.is_matchmaker = is_matchmaker;
        }

        public String getIs_friend() {
            return is_friend;
        }

        public void setIs_friend(String is_friend) {
            this.is_friend = is_friend;
        }

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

        public int getIs_vip() {
            return is_vip;
        }

        public void setIs_vip(int is_vip) {
            this.is_vip = is_vip;
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

        public String getDiamond() {
            return diamond;
        }

        public void setDiamond(String diamond) {
            this.diamond = diamond;
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

        public int getLang_approve() {
            return lang_approve;
        }

        public void setLang_approve(int lang_approve) {
            this.lang_approve = lang_approve;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getLogin_status() {
            return login_status;
        }

        public void setLogin_status(String login_status) {
            this.login_status = login_status;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIs_blocks() {
            return is_blocks;
        }

        public void setIs_blocks(String is_blocks) {
            this.is_blocks = is_blocks;
        }

        public String getPretty_id() {
            return pretty_id;
        }

        public void setPretty_id(String pretty_id) {
            this.pretty_id = pretty_id;
        }

        public String getPretty_avatar() {
            return pretty_avatar;
        }

        public void setPretty_avatar(String pretty_avatar) {
            this.pretty_avatar = pretty_avatar;
        }

        public String getRegister_time() {
            return register_time;
        }

        public void setRegister_time(String register_time) {
            this.register_time = register_time;
        }

        public String getLogin_ip() {
            return login_ip;
        }

        public void setLogin_ip(String login_ip) {
            this.login_ip = login_ip;
        }

        public String getEducation() {
            return education;
        }

        public void setEducation(String education) {
            this.education = education;
        }

        public String getMarital_status() {
            return marital_status;
        }

        public void setMarital_status(String marital_status) {
            this.marital_status = marital_status;
        }

        public String getPay() {
            return pay;
        }

        public void setPay(String pay) {
            this.pay = pay;
        }

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getHousing_status() {
            return housing_status;
        }

        public void setHousing_status(String housing_status) {
            this.housing_status = housing_status;
        }

        public String getCharm_part() {
            return charm_part;
        }

        public void setCharm_part(String charm_part) {
            this.charm_part = charm_part;
        }

        public String getBlood_type() {
            return blood_type;
        }

        public void setBlood_type(String blood_type) {
            this.blood_type = blood_type;
        }

        public String getTogether() {
            return together;
        }

        public void setTogether(String together) {
            this.together = together;
        }

        public String getLive_together_marrge() {
            return live_together_marrge;
        }

        public void setLive_together_marrge(String live_together_marrge) {
            this.live_together_marrge = live_together_marrge;
        }

        public String getFriend_city() {
            return friend_city;
        }

        public void setFriend_city(String friend_city) {
            this.friend_city = friend_city;
        }

        public String getFriend_age() {
            return friend_age;
        }

        public void setFriend_age(String friend_age) {
            this.friend_age = friend_age;
        }

        public String getFriend_stature() {
            return friend_stature;
        }

        public void setFriend_stature(String friend_stature) {
            this.friend_stature = friend_stature;
        }

        public String getFriend_education() {
            return friend_education;
        }

        public void setFriend_education(String friend_education) {
            this.friend_education = friend_education;
        }

        public String getFriend_pay() {
            return friend_pay;
        }

        public void setFriend_pay(String friend_pay) {
            this.friend_pay = friend_pay;
        }

        public int getStature() {
            return stature;
        }

        public void setStature(int stature) {
            this.stature = stature;
        }
    }

    public static class GradeInfoBean {
        private int lv_dengji;
        private int lh_dengji;
        private int percentage;
        private String level_number;
        private String hight_number;

        public int getLv_dengji() {
            return lv_dengji;
        }

        public void setLv_dengji(int lv_dengji) {
            this.lv_dengji = lv_dengji;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int apercentage) {
            this.percentage = apercentage;
        }

        public int getLh_dengji() {
            return lh_dengji;
        }

        public void setLh_dengji(int lh_dengji) {
            this.lh_dengji = lh_dengji;
        }

        public String getLevel_number() {
            return level_number;
        }

        public void setLevel_number(String level_number) {
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
        private int attention_count;
        private int follower_count;
        private int follower_new;
        private int history_count;
        private int history_new;

        public int getAttention_count() {
            return attention_count;
        }

        public void setAttention_count(int attention_count) {
            this.attention_count = attention_count;
        }

        public int getFollower_count() {
            return follower_count;
        }

        public void setFollower_count(int follower_count) {
            this.follower_count = follower_count;
        }

        public int getFollower_new() {
            return follower_new;
        }

        public void setFollower_new(int follower_new) {
            this.follower_new = follower_new;
        }

        public int getHistory_count() {
            return history_count;
        }

        public void setHistory_count(int history_count) {
            this.history_count = history_count;
        }

        public int getHistory_new() {
            return history_new;
        }

        public void setHistory_new(int history_new) {
            this.history_new = history_new;
        }
    }

    public static class RoomInfoBean {
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

    public static class FansRank {
        private String user_id;
        private String avatar;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    public static class SafetyBean {
        private String user_id;
        private int monitoring_status;
        private int parents_status;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getMonitoring_status() {
            return monitoring_status;
        }

        public void setMonitoring_status(int monitoring_status) {
            this.monitoring_status = monitoring_status;
        }

        public int getParents_status() {
            return parents_status;
        }

        public void setParents_status(int parents_status) {
            this.parents_status = parents_status;
        }
    }
}
