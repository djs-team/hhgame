package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

/**
 * Created by JUN on 2019/4/18
 */
public class JoinUser {
    private String UserId;
    private String Name;
    private String Avatar;
    //用户等级
    private int UserLevel;
    //vip等级
    private int VipLevel;
    //房间守护等级
    private int RoomGuardLevel;
    //爵位等级
    private int DukeLevel;
    //用户身份
    private int UserIdentity;
    //用户守护
    private List<UserGuardInfo> UserGuardList;
    //性别
    private int Sex;
    private String GuardSign;
    private boolean IsRoomGuard;

    public boolean isRoomGuard() {
        return IsRoomGuard;
    }

    public void setRoomGuard(boolean roomGuard) {
        IsRoomGuard = roomGuard;
    }

    public String getGuardSign() {
        return GuardSign;
    }

    public void setGuardSign(String guardSign) {
        GuardSign = guardSign;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public int getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(int userLevel) {
        UserLevel = userLevel;
    }

    public int getVipLevel() {
        return VipLevel;
    }

    public void setVipLevel(int vipLevel) {
        VipLevel = vipLevel;
    }

    public int getRoomGuardLevel() {
        return RoomGuardLevel;
    }

    public void setRoomGuardLevel(int roomGuardLevel) {
        RoomGuardLevel = roomGuardLevel;
    }

    public int getDukeLevel() {
        return DukeLevel;
    }

    public void setDukeLevel(int dukeLevel) {
        DukeLevel = dukeLevel;
    }

    public int getUserIdentity() {
        return UserIdentity;
    }

    public void setUserIdentity(int userIdentity) {
        UserIdentity = userIdentity;
    }

    public List<UserGuardInfo> getUserGuardList() {
        return UserGuardList;
    }

    public void setUserGuardList(List<UserGuardInfo> userGuardList) {
        UserGuardList = userGuardList;
    }

    public static class UserGuardInfo {
        private String Name;
        private int Level;

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }

        public int getLevel() {
            return Level;
        }

        public void setLevel(int level) {
            Level = level;
        }
    }
}
