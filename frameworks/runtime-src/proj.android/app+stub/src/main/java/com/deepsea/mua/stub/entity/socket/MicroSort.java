package com.deepsea.mua.stub.entity.socket;

/**
 * Created by JUN on 2019/4/23
 * 用户申请上非自由麦
 */
public class MicroSort {

    private int MsgId;
    private MicroOrder MicroOrderData;
    private  int GuardState;
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

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public MicroOrder getMicroOrderData() {
        return MicroOrderData;
    }

    public void setMicroOrderData(MicroOrder microOrderData) {
        MicroOrderData = microOrderData;
    }
}
