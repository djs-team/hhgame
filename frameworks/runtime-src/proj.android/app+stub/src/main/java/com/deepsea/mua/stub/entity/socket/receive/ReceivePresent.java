package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.BaseMsg;
import com.deepsea.mua.stub.entity.socket.GiftData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.entity.socket.receive.JoinUser;

import java.util.List;

/**
 * Created by JUN on 2019/4/25
 * 接收到礼物
 */
public class ReceivePresent extends BaseMsg {
    private String RoomId;
    private String RoomName;
    private int UserLevel;
    private int VipLevel;
    private int RoomGuardLevel;
    private int DukeLevel;
    private int UserIdentity;
    private int VisitorNum;
    private int Count;
    private WsUser GiftGiver;
    private List<GiveGiftDatasBean> GiveGiftDatas;
    private List<JoinUser.UserGuardInfo> UserGuardList;
    private com.deepsea.mua.stub.entity.socket.GiftData GiftData;
    private String GiftId;
    private String GuardSign;

    public String getGuardSign() {
        return GuardSign;
    }

    public void setGuardSign(String guardSign) {
        GuardSign = guardSign;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
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

    public int getVisitorNum() {
        return VisitorNum;
    }

    public void setVisitorNum(int visitorNum) {
        VisitorNum = visitorNum;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public WsUser getGiftGiver() {
        return GiftGiver;
    }

    public void setGiftGiver(WsUser giftGiver) {
        GiftGiver = giftGiver;
    }

    public List<GiveGiftDatasBean> getGiveGiftDatas() {
        return GiveGiftDatas;
    }

    public void setGiveGiftDatas(List<GiveGiftDatasBean> giveGiftDatas) {
        GiveGiftDatas = giveGiftDatas;
    }

    public List<JoinUser.UserGuardInfo> getUserGuardList() {
        return UserGuardList;
    }

    public void setUserGuardList(List<JoinUser.UserGuardInfo> userGuardList) {
        UserGuardList = userGuardList;
    }

    public GiftData getGiftData() {
        return GiftData;
    }

    public void setGiftData(GiftData giftData) {
        GiftData = giftData;
    }

    public String getGiftId() {
        return GiftId;
    }

    public void setGiftId(String giftId) {
        GiftId = giftId;
    }

    public static class GiveGiftDatasBean {
        private MicroBean Micro;
        private String TargetName;
        private int  Sex;

        public int getSex() {
            return Sex;
        }

        public void setSex(int sex) {
            Sex = sex;
        }

        public MicroBean getMicro() {
            return Micro;
        }

        public void setMicro(MicroBean Micro) {
            this.Micro = Micro;
        }

        public String getTargetName() {
            return TargetName;
        }

        public void setTargetName(String TargetName) {
            this.TargetName = TargetName;
        }

        public static class MicroBean {
            private int Level;
            private int Number;

            public int getLevel() {
                return Level;
            }

            public void setLevel(int Level) {
                this.Level = Level;
            }

            public int getNumber() {
                return Number;
            }

            public void setNumber(int Number) {
                this.Number = Number;
            }
        }
    }
}
