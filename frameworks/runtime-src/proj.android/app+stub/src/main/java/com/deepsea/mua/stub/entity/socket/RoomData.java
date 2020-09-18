package com.deepsea.mua.stub.entity.socket;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JUN on 2019/4/18
 */
public class RoomData {

    private int MsgId;
    //0 普通 1 管理员 2 房主
    private int UserIdentity;
    //是否禁言
    private boolean IsDisableMsg;
    //MODE
    private String ModeName;
    //账户余额
    private String Balance;
    //房主头像url
    private String RoomOwnerHeadUrl;
    //是否关注房主
    private boolean IsAttentionRoomOwner;
    //房主昵称
    private String RoomOwnerNickName;
    //锤子数量
    private int Hammers;
    //砸金蛋活动
    private boolean CanBreakEgg;
    //官方公告
    private String Tips;
    private long VisitorNum;
    private RoomDataBean RoomData;
    private List<MicroOrder> MicroOrders;//申请上麦的
    private List<MicroOrder> LeftMicroOrders;//申请上麦的男嘉宾
    private List<MicroOrder> RightMicroOrders;//申请上麦的女嘉宾
    private List<MicroInfosBean> MicroInfos;//麦上
    private List<String> Ranks;
    private int OnlineLeftNumber;
    private int OnlineRightNumber;
    private int WaitMicroLeftNumber;
    private int WaitMicroRightNumber;
    private String HuanxinRoomId;
    private String ShengwangRoomId;
    private boolean CanSendGiftToEmcee;
    private int SongCount;
    private boolean IsHelpShare;
    private int OnMicroCost;
    private int WheatCardCount;
    private boolean IsFirstCharge;
    private String GuardSign;


    public String getGuardSign() {
        return GuardSign;
    }

    public void setGuardSign(String guardSign) {
        GuardSign = guardSign;
    }

    public boolean isFirstCharge() {
        return IsFirstCharge;
    }

    public void setFirstCharge(boolean firstCharge) {
        IsFirstCharge = firstCharge;
    }

    public int getWheatCardCount() {
        return WheatCardCount;
    }

    public void setWheatCardCount(int wheatCardCount) {
        WheatCardCount = wheatCardCount;
    }

    public int getOnlineLeftNumber() {
        return OnlineLeftNumber;
    }

    public void setOnlineLeftNumber(int onlineLeftNumber) {
        OnlineLeftNumber = onlineLeftNumber;
    }

    public int getOnlineRightNumber() {
        return OnlineRightNumber;
    }

    public void setOnlineRightNumber(int onlineRightNumber) {
        OnlineRightNumber = onlineRightNumber;
    }

    public int getWaitMicroLeftNumber() {
        return WaitMicroLeftNumber;
    }

    public void setWaitMicroLeftNumber(int waitMicroLeftNumber) {
        WaitMicroLeftNumber = waitMicroLeftNumber;
    }

    public int getWaitMicroRightNumber() {
        return WaitMicroRightNumber;
    }

    public void setWaitMicroRightNumber(int waitMicroRightNumber) {
        WaitMicroRightNumber = waitMicroRightNumber;
    }

    public int getOnMicroCost() {
        return OnMicroCost;
    }

    public void setOnMicroCost(int onMicroCost) {
        OnMicroCost = onMicroCost;
    }

    public boolean isHelpShare() {
        return IsHelpShare;
    }

    public void setHelpShare(boolean helpShare) {
        IsHelpShare = helpShare;
    }

    public int getSongCount() {
        return SongCount;
    }

    public void setSongCount(int songCount) {
        SongCount = songCount;
    }

    public boolean isCanSendGiftToEmcee() {
        return CanSendGiftToEmcee;
    }

    public void setCanSendGiftToEmcee(boolean canSendGiftToEmcee) {
        CanSendGiftToEmcee = canSendGiftToEmcee;
    }

    public List<MicroOrder> getLeftMicroOrders() {
        return LeftMicroOrders;
    }

    public void setLeftMicroOrders(List<MicroOrder> leftMicroOrders) {
        LeftMicroOrders = leftMicroOrders;
    }

    public List<MicroOrder> getRightMicroOrders() {
        return RightMicroOrders;
    }

    public void setRightMicroOrders(List<MicroOrder> rightMicroOrders) {
        RightMicroOrders = rightMicroOrders;
    }

    public String getHuanxinRoomId() {
        return HuanxinRoomId;
    }

    public void setHuanxinRoomId(String huanxinRoomId) {
        HuanxinRoomId = huanxinRoomId;
    }

    public String getShengwangRoomId() {
        return ShengwangRoomId;
    }

    public void setShengwangRoomId(String shengwangRoomId) {
        ShengwangRoomId = shengwangRoomId;
    }

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int MsgId) {
        this.MsgId = MsgId;
    }

    public int getUserIdentity() {
        return UserIdentity;
    }

    public void setUserIdentity(int userIdentity) {
        UserIdentity = userIdentity;
    }

    public boolean isDisableMsg() {
        return IsDisableMsg;
    }

    public void setDisableMsg(boolean disableMsg) {
        IsDisableMsg = disableMsg;
    }

    public String getModeName() {
        return ModeName;
    }

    public void setModeName(String modeName) {
        ModeName = modeName;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }

    public String getRoomOwnerHeadUrl() {
        return RoomOwnerHeadUrl;
    }

    public void setRoomOwnerHeadUrl(String roomOwnerHeadUrl) {
        RoomOwnerHeadUrl = roomOwnerHeadUrl;
    }

    public boolean isAttentionRoomOwner() {
        return IsAttentionRoomOwner;
    }

    public void setAttentionRoomOwner(boolean attentionRoomOwner) {
        IsAttentionRoomOwner = attentionRoomOwner;
    }

    public String getRoomOwnerNickName() {
        return RoomOwnerNickName;
    }

    public void setRoomOwnerNickName(String roomOwnerNickName) {
        RoomOwnerNickName = roomOwnerNickName;
    }

    public int getHammers() {
        return Hammers;
    }

    public void setHammers(int hammers) {
        Hammers = hammers;
    }

    public boolean isCanBreakEgg() {
        return CanBreakEgg;
    }

    public void setCanBreakEgg(boolean canBreakEgg) {
        CanBreakEgg = canBreakEgg;
    }

    public String getTips() {
        return Tips;
    }

    public void setTips(String tips) {
        Tips = tips;
    }

    public long getVisitorNum() {
        return VisitorNum;
    }

    public void setVisitorNum(long visitorNum) {
        VisitorNum = visitorNum;
    }

    public RoomDataBean getRoomData() {
        return RoomData;
    }

    public void setRoomData(RoomDataBean RoomData) {
        this.RoomData = RoomData;
    }

    public List<MicroOrder> getMicroOrders() {
        return MicroOrders;
    }

    public void setMicroOrders(List<MicroOrder> microOrders) {
        MicroOrders = microOrders;
    }

    public List<MicroInfosBean> getMicroInfos() {
        return MicroInfos;
    }

    public void setMicroInfos(List<MicroInfosBean> MicroInfos) {
        this.MicroInfos = MicroInfos;
    }

    public List<String> getRanks() {
        return Ranks;
    }

    public void setRanks(List<String> ranks) {
        Ranks = ranks;
    }

    public static class RoomDataBean {
        private String Id;
        private String PrettyId;
        private String OwnerUserId;
        private String RoomName;
        private String RoomDesc;
        private String RoomWelcomes;
        private boolean RoomLock;
        private int RoomModel;
        private int RoomType;
        private Integer RoomTags;
        private boolean IsExclusiveRoom;
        private boolean IsCloseCamera;
        private boolean IsOpenRedPacket;
        private boolean IsOpenBreakEgg;
        private boolean IsOpenPickSong;
        private boolean IsOpenMediaLibrary;
        private boolean IsOpenVideoFrame;
        private String RoomId;

        public boolean isExclusiveRoom() {
            return IsExclusiveRoom;
        }

        public void setExclusiveRoom(boolean exclusiveRoom) {
            IsExclusiveRoom = exclusiveRoom;
        }

        public boolean isCloseCamera() {
            return IsCloseCamera;
        }

        public void setCloseCamera(boolean closeCamera) {
            IsCloseCamera = closeCamera;
        }

        public boolean isOpenRedPacket() {
            return IsOpenRedPacket;
        }

        public void setOpenRedPacket(boolean openRedPacket) {
            IsOpenRedPacket = openRedPacket;
        }

        public boolean isOpenBreakEgg() {
            return IsOpenBreakEgg;
        }

        public void setOpenBreakEgg(boolean openBreakEgg) {
            IsOpenBreakEgg = openBreakEgg;
        }

        public boolean isOpenPickSong() {
            return IsOpenPickSong;
        }

        public void setOpenPickSong(boolean openPickSong) {
            IsOpenPickSong = openPickSong;
        }

        public boolean isOpenMediaLibrary() {
            return IsOpenMediaLibrary;
        }

        public void setOpenMediaLibrary(boolean openMediaLibrary) {
            IsOpenMediaLibrary = openMediaLibrary;
        }

        public boolean isOpenVideoFrame() {
            return IsOpenVideoFrame;
        }

        public void setOpenVideoFrame(boolean openVideoFrame) {
            IsOpenVideoFrame = openVideoFrame;
        }

        public String getRoomId() {
            return RoomId;
        }

        public void setRoomId(String roomId) {
            RoomId = roomId;
        }

        public int getRoomType() {
            return RoomType;
        }

        public void setRoomType(int roomType) {
            RoomType = roomType;
        }

        public int getRoomModel() {
            return RoomModel;
        }

        public void setRoomModel(int roomModel) {
            RoomModel = roomModel;
        }

        public Integer getRoomTags() {
            return RoomTags;
        }

        public void setRoomTags(Integer roomTags) {
            RoomTags = roomTags;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getPrettyId() {
            return PrettyId;
        }

        public void setPrettyId(String prettyId) {
            PrettyId = prettyId;
        }

        public String getOwnerUserId() {
            return OwnerUserId;
        }

        public void setOwnerUserId(String OwnerUserId) {
            this.OwnerUserId = OwnerUserId;
        }

        public String getRoomName() {
            return RoomName;
        }

        public void setRoomName(String RoomName) {
            this.RoomName = RoomName;
        }

        public String getRoomDesc() {
            return RoomDesc;
        }

        public void setRoomDesc(String RoomDesc) {
            this.RoomDesc = RoomDesc;
        }

        public String getRoomWelcomes() {
            return RoomWelcomes;
        }

        public void setRoomWelcomes(String RoomWelcomes) {
            this.RoomWelcomes = RoomWelcomes;
        }

        public boolean isRoomLock() {
            return RoomLock;
        }

        public void setRoomLock(boolean RoomLock) {
            this.RoomLock = RoomLock;
        }
    }

    public static class MicroInfosBean {
        //0 红娘，1，男 ，2 女
        private int Type;
        private int Number;
        private boolean IsLocked;
        private boolean IsDisabled;
        private String DaojishiShijiandian;
        private int DaojishiShichang;
        private int XinDongZhi;
        private WsUser User;
        private int Rolse;
        private boolean isUpRose = false;
        private List<String> RoseRanks;
        private boolean  isOpenMp;

        public boolean isOpenMp() {
            return isOpenMp;
        }

        public void setOpenMp(boolean openMp) {
            isOpenMp = openMp;
        }

        public List<String> getRoseRanks() {
            return RoseRanks;
        }

        public void setRoseRanks(List<String> roseRanks) {
            RoseRanks = roseRanks;
        }

        public boolean isUpRose() {
            return isUpRose;
        }

        public void setUpRose(boolean upRose) {
            isUpRose = upRose;
        }

        public int getRolse() {
            return Rolse;
        }

        public void setRolse(int rolse) {
            Rolse = rolse;
        }

        private boolean isRelease;


        public boolean isRelease() {
            return isRelease;
        }

        public void setRelease(boolean release) {
            isRelease = release;
        }

        //表情玩法
        @SerializedName("EmoticonAnimationUrl")
        private String actionUrl;

        //礼物、表情url
        private String animUrl;
        //发送表情结果url
        private String resultUrl;

        public int getType() {
            return Type;
        }

        public void setType(int Type) {
            this.Type = Type;
        }

        public int getNumber() {
            return Number;
        }

        public void setNumber(int Number) {
            this.Number = Number;
        }

        public boolean isIsLocked() {
            return IsLocked;
        }

        public void setIsLocked(boolean IsLocked) {
            this.IsLocked = IsLocked;
        }

        public boolean isIsDisabled() {
            return IsDisabled;
        }

        public void setIsDisabled(boolean IsDisabled) {
            this.IsDisabled = IsDisabled;
        }

        public String getDaojishiShijiandian() {
            return DaojishiShijiandian;
        }

        public void setDaojishiShijiandian(String DaojishiShijiandian) {
            this.DaojishiShijiandian = DaojishiShijiandian;
        }

        public int getDaojishiShichang() {
            return DaojishiShichang;
        }

        public void setDaojishiShichang(int DaojishiShichang) {
            this.DaojishiShichang = DaojishiShichang;
        }

        public int getXinDongZhi() {
            return XinDongZhi;
        }

        public void setXinDongZhi(int XinDongZhi) {
            this.XinDongZhi = XinDongZhi;
        }

        public WsUser getUser() {
            return User;
        }

        public void setUser(WsUser User) {
            this.User = User;
        }

        public String getAnimUrl() {
            return animUrl;
        }

        public void setAnimUrl(String animUrl) {
            this.animUrl = animUrl;
        }

        public String getActionUrl() {
            return actionUrl;
        }

        public void setActionUrl(String actionUrl) {
            this.actionUrl = actionUrl;
        }

        public String getResultUrl() {
            return resultUrl;
        }

        public void setResultUrl(String resultUrl) {
            this.resultUrl = resultUrl;
        }
    }
}
