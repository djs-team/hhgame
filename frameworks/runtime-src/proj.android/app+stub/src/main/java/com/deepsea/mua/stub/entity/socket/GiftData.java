package com.deepsea.mua.stub.entity.socket;

/**
 * Created by JUN on 2019/9/5
 */
public class GiftData {
    private String Name;
    private String Image;
    private String GiftAnimation;
    private String Animation;
    //礼物分类 1 大类 2小类
    private int ClassType;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getGiftAnimation() {
        return GiftAnimation;
    }

    public void setGiftAnimation(String GiftAnimation) {
        this.GiftAnimation = GiftAnimation;
    }

    public String getAnimation() {
        return Animation;
    }

    public void setAnimation(String Animation) {
        this.Animation = Animation;
    }

    public int getClassType() {
        return ClassType;
    }

    public void setClassType(int ClassType) {
        this.ClassType = ClassType;
    }
}
