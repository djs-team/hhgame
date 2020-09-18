package com.deepsea.mua.stub.entity.socket;

import java.util.List;

/**
 * Created by JUN on 2019/7/29
 */
public class SmashResultBean {
    private int Success;
    private int Harmmers;
    private SmashBean Gift;

    public int getSuccess() {
        return Success;
    }

    public void setSuccess(int success) {
        Success = success;
    }

    public int getHarmmers() {
        return Harmmers;
    }

    public void setHarmmers(int harmmers) {
        Harmmers = harmmers;
    }

    public SmashBean getGift() {
        return Gift;
    }

    public void setGift(SmashBean gift) {
        Gift = gift;
    }
}
