package com.deepsea.mua.stub.entity.socket;

/**
 * Created by JUN on 2019/4/27
 */
public class SingleSend {

    private int MsgId;
    //礼物id
    private String GiftId;
    //被送人id
    private String Id;
    //礼物数量
    private int Count;
    //是否使用背包礼物
    private boolean IsUseBag;

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public String getGiftId() {
        return GiftId;
    }

    public void setGiftId(String giftId) {
        GiftId = giftId;
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

    public boolean isUseBag() {
        return IsUseBag;
    }

    public void setUseBag(boolean useBag) {
        IsUseBag = useBag;
    }
}
