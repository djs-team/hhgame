package com.deepsea.mua.stub.entity;

public class UserRedPacket {
    private String UserId;
    private String HeadImageUrl;
    private String NickName;
    private float Value;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getHeadImageUrl() {
        return HeadImageUrl;
    }

    public void setHeadImageUrl(String headImageUrl) {
        HeadImageUrl = headImageUrl;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public float getValue() {
        return Value;
    }

    public void setValue(float value) {
        Value = value;
    }
}
