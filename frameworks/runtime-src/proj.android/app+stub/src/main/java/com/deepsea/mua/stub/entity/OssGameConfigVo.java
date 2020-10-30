package com.deepsea.mua.stub.entity;

import java.io.Serializable;

public class OssGameConfigVo implements Serializable {
    private String accessKeyIdOss;
    private String accessKeySecretOss;
    private String bucketName;
    private String endpoint;
    private String expiration;
    private String token;

    public String getAccessKeyIdOss() {
        return accessKeyIdOss;
    }

    public void setAccessKeyIdOss(String accessKeyIdOss) {
        this.accessKeyIdOss = accessKeyIdOss;
    }

    public String getAccessKeySecretOss() {
        return accessKeySecretOss;
    }

    public void setAccessKeySecretOss(String accessKeySecretOss) {
        this.accessKeySecretOss = accessKeySecretOss;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
