package com.deepsea.mua.stub.entity;

public class ReportPicVo {
    private String localPath;
    private String onlinePath;

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getOnlinePath() {
        return onlinePath;
    }

    public void setOnlinePath(String onlinePath) {
        this.onlinePath = onlinePath;
    }

    public ReportPicVo(String localPath, String onlinePath) {
        this.localPath = localPath;
        this.onlinePath = onlinePath;
    }
}


