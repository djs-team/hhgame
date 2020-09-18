package com.deepsea.mua.stub.entity;

import java.util.HashMap;
import java.util.Map;

public class VoiceRegulationVo {
    private int localVoiceReverbPreset = 0;
    private Map<Integer, Integer> localVoiceEqualization;
    private double localVoicePitch = 1;
    private Map<Integer, Integer> localVoiceReverb;

    public int getLocalVoiceReverbPreset() {
        return localVoiceReverbPreset;
    }

    public void setLocalVoiceReverbPreset(int localVoiceReverbPreset) {
        this.localVoiceReverbPreset = localVoiceReverbPreset;
    }

    public Map<Integer, Integer> getLocalVoiceEqualization() {
        return localVoiceEqualization;
    }

    public void setLocalVoiceEqualization(Map<Integer, Integer> localVoiceEqualization) {
        this.localVoiceEqualization = localVoiceEqualization;
    }

    public double getLocalVoicePitch() {
        return localVoicePitch;
    }

    public void setLocalVoicePitch(double localVoicePitch) {
        this.localVoicePitch = localVoicePitch;
    }

    public Map<Integer, Integer> getLocalVoiceReverb() {
        return localVoiceReverb;
    }

    public void setLocalVoiceReverb(Map<Integer, Integer> localVoiceReverb) {
        this.localVoiceReverb = localVoiceReverb;
    }

}
