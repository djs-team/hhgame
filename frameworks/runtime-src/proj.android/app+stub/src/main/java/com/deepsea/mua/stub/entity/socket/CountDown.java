package com.deepsea.mua.stub.entity.socket;

import com.deepsea.mua.stub.entity.socket.receive.BaseMicroMsg;

/**
 * Created by JUN on 2019/4/22
 */
public class CountDown extends BaseMicroMsg {
    private String SpeechTime;
    private int Duration;

    public String getSpeechTime() {
        return SpeechTime;
    }

    public void setSpeechTime(String SpeechTime) {
        this.SpeechTime = SpeechTime;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int Duration) {
        this.Duration = Duration;
    }
}
