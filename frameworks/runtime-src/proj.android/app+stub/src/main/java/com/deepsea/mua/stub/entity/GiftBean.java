package com.deepsea.mua.stub.entity;

import java.io.Serializable;

/**
 * Created by JUN on 2019/7/26
 */
public class GiftBean implements Serializable {
    //礼物id
    private String gift_id;
    //礼物名称
    private String gift_name;
    //礼物财富值
    private int gift_number;
    //礼物币
    private int gift_coin;
    //礼物图标
    private String gift_image;
    //礼物类型 1普通礼物 2动画礼物 3免费礼物4蓝玫瑰礼物
    private String gift_type;
    //svga动画
    private String gift_animation;
    //小动画
    private String animation;
    //礼物分类 1 大类 2小类
    private String class_type;
    //全服广播 1未广播 2广播
    private int broadcast;
    //礼物状态 1 上架 2 下架
    private int status;

    public String getGift_id() {
        return gift_id;
    }

    public void setGift_id(String gift_id) {
        this.gift_id = gift_id;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public int getGift_number() {
        return gift_number;
    }

    public void setGift_number(int gift_number) {
        this.gift_number = gift_number;
    }

    public int getGift_coin() {
        return gift_coin;
    }

    public void setGift_coin(int gift_coin) {
        this.gift_coin = gift_coin;
    }

    public String getGift_image() {
        return gift_image;
    }

    public void setGift_image(String gift_image) {
        this.gift_image = gift_image;
    }

    public String getGift_type() {
        return gift_type;
    }

    public void setGift_type(String gift_type) {
        this.gift_type = gift_type;
    }

    public String getGift_animation() {
        return gift_animation;
    }

    public void setGift_animation(String gift_animation) {
        this.gift_animation = gift_animation;
    }

    public String getAnimation() {
        return animation;
    }

    public void setAnimation(String animation) {
        this.animation = animation;
    }

    public String getClass_type() {
        return class_type;
    }

    public void setClass_type(String class_type) {
        this.class_type = class_type;
    }

    public int getBroadcast() {
        return broadcast;
    }

    public void setBroadcast(int broadcast) {
        this.broadcast = broadcast;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
