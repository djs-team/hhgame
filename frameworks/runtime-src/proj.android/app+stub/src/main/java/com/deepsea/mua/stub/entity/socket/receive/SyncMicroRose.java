package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class SyncMicroRose {
    private int Level;
    private int Number;
    private int Rose;
    private List<String> RoseRanks;

    public List<String> getRoseRanks() {
        return RoseRanks;
    }

    public void setRoseRanks(List<String> roseRanks) {
        RoseRanks = roseRanks;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public int getRose() {
        return Rose;
    }

    public void setRose(int rose) {
        Rose = rose;
    }
}
