package com.deepsea.mua.stub.entity;

public class LocalVoiceReverbPresetVo {
    private int roomSize;
    private int wetDelay;
    private int strength;
    private int wetLevel;
    private int dryLevel;

    public LocalVoiceReverbPresetVo() {
    }

    public LocalVoiceReverbPresetVo(int roomSize, int wetDelay, int strength, int wetLevel, int dryLevel) {
        this.roomSize = roomSize;
        this.wetDelay = wetDelay;
        this.strength = strength;
        this.wetLevel = wetLevel;
        this.dryLevel = dryLevel;
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }

    public int getWetDelay() {
        return wetDelay;
    }

    public void setWetDelay(int wetDelay) {
        this.wetDelay = wetDelay;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getWetLevel() {
        return wetLevel;
    }

    public void setWetLevel(int wetLevel) {
        this.wetLevel = wetLevel;
    }

    public int getDryLevel() {
        return dryLevel;
    }

    public void setDryLevel(int dryLevel) {
        this.dryLevel = dryLevel;
    }
}
