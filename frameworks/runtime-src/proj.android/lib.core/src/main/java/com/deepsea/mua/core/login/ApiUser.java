package com.deepsea.mua.core.login;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JUN on 2018/9/28
 */
public class ApiUser implements Serializable {
    public static final String PLATFORM_QQ = "qq";
    public static final String PLATFORM_WECHAT = "wechat";

    private String platform;
    private String openId;
    private String userName;
    private String userIcon;
    private String sex;
    private Map<String, String> res;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public Map<String, String> getRes() {
        return res;
    }

    public void setRes(Map<String, String> res) {
        this.res = res;
    }
}
