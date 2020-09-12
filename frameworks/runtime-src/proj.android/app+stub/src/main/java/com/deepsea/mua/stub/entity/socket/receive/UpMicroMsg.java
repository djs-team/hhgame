package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.BaseMsg;
import com.deepsea.mua.stub.entity.socket.RoomData;

import java.util.List;

/**
 * Created by JUN on 2019/4/18
 */
public class UpMicroMsg extends BaseMsg {
    private int UserLevel;
    private int VipLevel;
    private int RoomGuardLevel;
    private int DukeLevel;
    private int UserIdentity;
    private RoomData.MicroInfosBean MicroInfo;
    private List<JoinUser.UserGuardInfo> UserGuardList;
    private String GuardSign;

    public String getGuardSign() {
        return GuardSign;
    }

    public void setGuardSign(String guardSign) {
        GuardSign = guardSign;
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

    public RoomData.MicroInfosBean getMicroInfo() {
        return MicroInfo;
    }

    public void setMicroInfo(RoomData.MicroInfosBean microInfo) {
        MicroInfo = microInfo;
    }

    public List<JoinUser.UserGuardInfo> getUserGuardList() {
        return UserGuardList;
    }

    public void setUserGuardList(List<JoinUser.UserGuardInfo> userGuardList) {
        UserGuardList = userGuardList;
    }
}
