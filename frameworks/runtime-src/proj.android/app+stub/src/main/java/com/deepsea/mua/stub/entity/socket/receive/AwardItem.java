package com.deepsea.mua.stub.entity.socket.receive;

public class AwardItem {
    /// 0为钻石，1为玫瑰，2为道具
    private int AwardType;
    private int AwardCount;
    private int ItemId;

    public int getAwardType() {
        return AwardType;
    }

    public void setAwardType(int awardType) {
        AwardType = awardType;
    }

    public int getAwardCount() {
        return AwardCount;
    }

    public void setAwardCount(int awardCount) {
        AwardCount = awardCount;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }
}
