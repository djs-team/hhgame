package com.deepsea.mua.stub.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by JUN on 2019/7/30
 */
public class SmashParamBean implements Serializable {
    public SmashParamBean() {
    }

    private int unit_price;
    private int bluerose_price;
    private long max_packnum;
    private List<GiftBean> gift_list;

    public int getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(int unit_price) {
        this.unit_price = unit_price;
    }

    public int getBluerose_price() {
        return bluerose_price;
    }

    public void setBluerose_price(int bluerose_price) {
        this.bluerose_price = bluerose_price;
    }

    public long getMax_packnum() {
        return max_packnum;
    }

    public void setMax_packnum(long max_packnum) {
        this.max_packnum = max_packnum;
    }

    public List<GiftBean> getGift_list() {
        return gift_list;
    }

    public void setGift_list(List<GiftBean> gift_list) {
        this.gift_list = gift_list;
    }
}
