package com.deepsea.mua.stub.entity.socket.receive;

public class NotifyHelpToUserParam {
    private int UserId;
    private String UserName;
    private int UserSex;
    private int TargetId;
    private String TargetName;
    private int TargetSex;
    private int Coin;

    public int getUserSex() {
        return UserSex;
    }

    public void setUserSex(int userSex) {
        UserSex = userSex;
    }

    public int getTargetSex() {
        return TargetSex;
    }

    public void setTargetSex(int targetSex) {
        TargetSex = targetSex;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int  getTargetId() {
        return TargetId;
    }

    public void setTargetId(int targetId) {
        TargetId = targetId;
    }

    public String getTargetName() {
        return TargetName;
    }

    public void setTargetName(String targetName) {
        TargetName = targetName;
    }

    public int getCoin() {
        return Coin;
    }

    public void setCoin(int coin) {
        Coin = coin;
    }
}
