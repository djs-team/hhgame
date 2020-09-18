package com.deepsea.mua.stub.entity.socket.receive;

import java.io.Serializable;

public class BreakEggRecord implements Serializable {
    private int EggType;
    private int Count;
    private String GiftImage;
    private long Time;
    private String GiftName;

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        Time = time;
    }

    public String getGiftName() {
        return GiftName;
    }

    public void setGiftName(String giftName) {
        GiftName = giftName;
    }

    public int getEggType() {
        return EggType;
    }

    public void setEggType(int eggType) {
        EggType = eggType;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public String getGiftImage() {
        return GiftImage;
    }

    public void setGiftImage(String giftImage) {
        GiftImage = giftImage;
    }
}
