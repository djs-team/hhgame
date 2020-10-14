package com.deepsea.mua.stub.entity.socket;

import java.io.Serializable;

/**
 * Created by JUN on 2019/4/26
 */
public class WsUser implements Serializable {
    private String Name;
    private String HeadImageUrl;
    private String UserId;
    private String PrettyId;
    private String PrettyAvatar;
    private String Age;
    private int Stature;
    private String City;
    private int Sex;
    private int VipLevel;
    private int Type;
    private int Number;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public int getVipLevel() {
        return VipLevel;
    }

    public void setVipLevel(int vipLevel) {
        VipLevel = vipLevel;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getHeadImageUrl() {
        return HeadImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        HeadImageUrl = headImageUrl;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPrettyId() {
        return PrettyId;
    }

    public void setPrettyId(String prettyId) {
        PrettyId = prettyId;
    }

    public String getPrettyAvatar() {
        return PrettyAvatar;
    }

    public void setPrettyAvatar(String prettyAvatar) {
        PrettyAvatar = prettyAvatar;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public int getStature() {
        return Stature;
    }

    public void setStature(int stature) {
        Stature = stature;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }
}
