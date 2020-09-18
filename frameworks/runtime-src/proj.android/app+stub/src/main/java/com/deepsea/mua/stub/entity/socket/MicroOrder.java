package com.deepsea.mua.stub.entity.socket;

import java.io.Serializable;

/**
 * Created by JUN on 2019/10/23
 */
public class MicroOrder implements Serializable {
    private WsUser User;
    private int Sex;
    private int Level;
    private int Number;
    private boolean IsFree;
    private String  Age;
    private String City;
    private String  Stature;

    public boolean isFree() {
        return IsFree;
    }

    public void setFree(boolean free) {
        IsFree = free;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getStature() {
        return Stature;
    }

    public void setStature(String stature) {
        Stature = stature;
    }

    public WsUser getUser() {
        return User;
    }

    public void setUser(WsUser User) {
        this.User = User;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int Sex) {
        this.Sex = Sex;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int Level) {
        this.Level = Level;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int Number) {
        this.Number = Number;
    }
}
