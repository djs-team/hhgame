package com.deepsea.mua.stub.entity.socket.receive;

public class ShowGuardAnimationToClientParam {
    private int UserId;
    private String UserImage;
    private String UserName;
    private int TargetId;
    private String TargetImage;
    private String TargetName;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getTargetId() {
        return TargetId;
    }

    public void setTargetId(int targetId) {
        TargetId = targetId;
    }

    public String getTargetImage() {
        return TargetImage;
    }

    public void setTargetImage(String targetImage) {
        TargetImage = targetImage;
    }

    public String getTargetName() {
        return TargetName;
    }

    public void setTargetName(String targetName) {
        TargetName = targetName;
    }

}
