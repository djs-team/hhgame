package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.BaseMsg;
import com.deepsea.mua.stub.entity.socket.SmashBean;
import com.deepsea.mua.stub.entity.socket.receive.JoinUser;

import java.util.List;

/**
 * Created by JUN on 2019/7/30
 */
public class SmashEggBean extends BaseMsg {
    private String UserId;
    private String UserNickName;
    private int UserLevel;
    private int VipLevel;
    private int RoomGuardLevel;
    private List<JoinUser.UserGuardInfo> UserGuardList;

    private int DukeLevel;
    private int UserIdentity;
    private SmashBean Gift;
    private int Sex;
    private int EggType;
    private String GuardSign;
    private int GuardState;// 0不是主持守护，1是普通守护，2是榜一守护
    private String GuardHeadImage;

    public int getGuardState() {
        return GuardState;
    }

    public void setGuardState(int guardState) {
        GuardState = guardState;
    }

    public String getGuardHeadImage() {
        return GuardHeadImage;
    }

    public void setGuardHeadImage(String guardHeadImage) {
        GuardHeadImage = guardHeadImage;
    }

    public String getGuardSign() {
        return GuardSign;
    }

    public void setGuardSign(String guardSign) {
        GuardSign = guardSign;
    }

    public int getEggType() {
        return EggType;
    }

    public void setEggType(int eggType) {
        EggType = eggType;
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

    public String getUserNickName() {
        return UserNickName;
    }

    public void setUserNickName(String UserNickName) {
        this.UserNickName = UserNickName;
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

    public int getUserIdentity() {
        return UserIdentity;
    }

    public void setUserIdentity(int UserIdentity) {
        this.UserIdentity = UserIdentity;
    }

    public SmashBean getGifts() {
        return Gift;
    }

    public void setGift(SmashBean gift) {
        Gift = gift;
    }

    public List<JoinUser.UserGuardInfo> getUserGuardList() {
        return UserGuardList;
    }

    public void setUserGuardList(List<JoinUser.UserGuardInfo> userGuardList) {
        UserGuardList = userGuardList;
    }
}
