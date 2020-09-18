package com.deepsea.mua.stub.entity;

import com.hyphenate.chat.EMMessage;

public class FriendInfoBean {

    /**
     * user_id : 1069161
     * nickname : 你别闹
     * avatar : http://image.xiaokongping.com//Avatar/1069161/1570876455705.jpg
     * sex : 1
     * city : 广西
     * online : 1
     * age : 26
     */

    private String user_id;
    private String nickname;
    private String avatar;
    private String sex;
    private String city;
    private String online;
    private int age;
    private String state;
    private String lv_dengji;
    private String stature;
    private EMMessage lastMsg;
    private long time;
    private int unReadCount;
    private String online_str;
    private String intro;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getOnline_str() {
        return online_str;
    }

    public void setOnline_str(String online_str) {
        this.online_str = online_str;
    }

    public String getLv_dengji() {
        return lv_dengji;
    }

    public void setLv_dengji(String lv_dengji) {
        this.lv_dengji = lv_dengji;
    }

    public String getStature() {
        return stature;
    }

    public void setStature(String stature) {
        this.stature = stature;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public EMMessage getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(EMMessage lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
