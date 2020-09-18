package com.deepsea.mua.stub.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JUN on 2019/4/30
 */
public class RankBean {
    private int Rank;
    @SerializedName("NickName")
    private String nickname;
    @SerializedName("HeadImageUrl")
    private String avatar;
    @SerializedName("Sex")
    private int sex;
    @SerializedName("UserLevel")
    private int user_lv;
    @SerializedName("VipLevel")
    private int vip_lv;
    private int DukeLevel;
    @SerializedName("Value")
    private String coin;
    @SerializedName("UserId")
    private String userid;
    @SerializedName("Age")
    private String age;
    @SerializedName("City")
    private String city;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getUser_lv() {
        return user_lv;
    }

    public void setUser_lv(int user_lv) {
        this.user_lv = user_lv;
    }

    public int getVip_lv() {
        return vip_lv;
    }

    public void setVip_lv(int vip_lv) {
        this.vip_lv = vip_lv;
    }

    public int getDukeLevel() {
        return DukeLevel;
    }

    public void setDukeLevel(int dukeLevel) {
        DukeLevel = dukeLevel;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
