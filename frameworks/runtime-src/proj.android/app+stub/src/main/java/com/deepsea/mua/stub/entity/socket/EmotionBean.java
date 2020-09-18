package com.deepsea.mua.stub.entity.socket;

/**
 * Created by JUN on 2019/7/10
 */
public class EmotionBean {
    private String ResultUrl;
    private String EmoticonAnimationUrl;
    private int MicroLevel;
    private int MicroNumber;

    public String getResultUrl() {
        return ResultUrl;
    }

    public void setResultUrl(String resultUrl) {
        ResultUrl = resultUrl;
    }

    public String getEmoticonAnimationUrl() {
        return EmoticonAnimationUrl;
    }

    public void setEmoticonAnimationUrl(String emoticonAnimationUrl) {
        EmoticonAnimationUrl = emoticonAnimationUrl;
    }

    public int getMicroLevel() {
        return MicroLevel;
    }

    public void setMicroLevel(int microLevel) {
        MicroLevel = microLevel;
    }

    public int getMicroNumber() {
        return MicroNumber;
    }

    public void setMicroNumber(int microNumber) {
        MicroNumber = microNumber;
    }
}
