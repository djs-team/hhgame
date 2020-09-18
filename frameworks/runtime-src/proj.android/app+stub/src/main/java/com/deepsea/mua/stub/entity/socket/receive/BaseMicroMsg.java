package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.BaseMsg;

/**
 * Created by JUN on 2019/4/20
 */
public class BaseMicroMsg extends BaseMsg {
    private int Level;
    private int Number;

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
