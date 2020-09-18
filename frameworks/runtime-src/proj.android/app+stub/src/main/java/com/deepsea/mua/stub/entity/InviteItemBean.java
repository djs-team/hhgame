package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * 作者：liyaxing  2019/8/23 19:15
 * 类别 ：
 */
public class InviteItemBean {


    /**
     * id : 1043
     * birthday : 2001-01-01 00:00:00
     * is_online : 1
     * nickname : 张三
     * avatar : http://face-test-01.oss-cn-beijing.aliyuncs.com//Avatar/register/1573872046643.jpg
     * sex : 1
     * register_time : 2019-11-16 10:45:12
     * age : 18
     */

    private String id;
    private String birthday;
    private String is_online;
    private String nickname;
    private String avatar;
    private String sex;
    private String register_time;
    private int age;
    private String city;
    private String send_coin;
    private String attestation;//是否认证0：未认证   1：已认证
    private String state;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSend_coin() {
        return send_coin;
    }

    public void setSend_coin(String send_coin) {
        this.send_coin = send_coin;
    }

    public String getAttestation() {
        return attestation;
    }

    public void setAttestation(String attestation) {
        this.attestation = attestation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
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

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}