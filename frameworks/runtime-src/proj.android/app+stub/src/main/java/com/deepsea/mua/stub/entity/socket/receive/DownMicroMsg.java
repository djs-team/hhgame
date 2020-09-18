package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.BaseMsg;

/**
 * Created by JUN on 2019/4/19
 */
public class DownMicroMsg extends BaseMsg {
    private int Level;
    private int Number;
    private boolean IsKick;

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

    public boolean isKick() {
        return IsKick;
    }

    public void setKick(boolean kick) {
        IsKick = kick;
    }
}
