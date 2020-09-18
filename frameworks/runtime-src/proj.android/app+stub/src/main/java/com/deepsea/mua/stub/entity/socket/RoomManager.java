package com.deepsea.mua.stub.entity.socket;

import com.deepsea.mua.stub.entity.socket.receive.JoinUser;

import java.util.List;

/**
 * Created by JUN on 2019/4/24
 */
public class RoomManager {
    /**
     * MsgId : 55
     * IsManager : false
     * UserId : 10061
     * Name : 乃颯颯
     * UserLevel : 1
     * VipLevel : 0
     * RoomGuardLevel : 0
     * UserGuardList : []
     * DukeLevel : 0
     */

    private int MsgId;
    private boolean IsManager;
    private String UserId;
    private String Name;
    private int UserLevel;
    private int VipLevel;
    private int RoomGuardLevel;
    private int DukeLevel;
    private List<JoinUser.UserGuardInfo> UserGuardList;

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int MsgId) {
        this.MsgId = MsgId;
    }

    public boolean isIsManager() {
        return IsManager;
    }

    public void setIsManager(boolean IsManager) {
        this.IsManager = IsManager;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(int UserLevel) {
        this.UserLevel = UserLevel;
    }

    public int getVipLevel() {
        return VipLevel;
    }

    public void setVipLevel(int VipLevel) {
        this.VipLevel = VipLevel;
    }

    public int getRoomGuardLevel() {
        return RoomGuardLevel;
    }

    public void setRoomGuardLevel(int RoomGuardLevel) {
        this.RoomGuardLevel = RoomGuardLevel;
    }

    public int getDukeLevel() {
        return DukeLevel;
    }

    public void setDukeLevel(int DukeLevel) {
        this.DukeLevel = DukeLevel;
    }

    public List<JoinUser.UserGuardInfo> getUserGuardList() {
        return UserGuardList;
    }

    public void setUserGuardList(List<JoinUser.UserGuardInfo> UserGuardList) {
        this.UserGuardList = UserGuardList;
    }
}
