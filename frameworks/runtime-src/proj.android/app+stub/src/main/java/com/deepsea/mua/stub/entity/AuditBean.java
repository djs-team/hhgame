package com.deepsea.mua.stub.entity;

/**
 * Created by JUN on 2019/7/19
 */
public class AuditBean {
    private String Token;
    private String DurationSeconds;

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getDurationSeconds() {
        return DurationSeconds;
    }

    public void setDurationSeconds(String DurationSeconds) {
        this.DurationSeconds = DurationSeconds;
    }
}
