package com.deepsea.mua.stub.entity.socket;

/**
 * Created by JUN on 2019/7/29
 */
public class SmashBean {
    private String Id;
    private int Count;
    private int TotalCount;
    private GiftData GiftData;
    private int TotalCoin;

    public int getTotalCoin() {
        return TotalCoin;
    }

    public void setTotalCoin(int totalCoin) {
        TotalCoin = totalCoin;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public com.deepsea.mua.stub.entity.socket.GiftData getGiftData() {
        return GiftData;
    }

    public void setGiftData(com.deepsea.mua.stub.entity.socket.GiftData giftData) {
        GiftData = giftData;
    }
}
