package com.deepsea.mua.stub.entity.socket;

import java.util.List;

/**
 * Created by JUN on 2019/4/24
 */
public class MultiSend {

    private int MsgId;
    private boolean IsWholeMicro;
    private String GiftId;
    private int count;
    private boolean IsUseBag;
    private List<GiveGiftsMicros> Micros;

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public boolean isWholeMicro() {
        return IsWholeMicro;
    }

    public void setWholeMicro(boolean wholeMicro) {
        IsWholeMicro = wholeMicro;
    }

    public String getGiftId() {
        return GiftId;
    }

    public void setGiftId(String giftId) {
        GiftId = giftId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isUseBag() {
        return IsUseBag;
    }

    public void setUseBag(boolean useBag) {
        IsUseBag = useBag;
    }

    public List<GiveGiftsMicros> getMicros() {
        return Micros;
    }

    public void setMicros(List<GiveGiftsMicros> micros) {
        Micros = micros;
    }

    public static class GiveGiftsMicros {
        private int Level;
        private int Number;

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
    }
}
