package com.deepsea.mua.stub.entity.socket;

/**
 * Created by JUN on 2019/10/24
 */
public class MpInvited {
    private String NickName;
    private int Free; //1免费 0付费
    private String Cost;

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public int getFree() {
        return Free;
    }

    public void setFree(int free) {
        Free = free;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }
}
