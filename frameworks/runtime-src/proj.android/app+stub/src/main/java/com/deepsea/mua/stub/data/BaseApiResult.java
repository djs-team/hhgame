package com.deepsea.mua.stub.data;

/**
 * Created by JUN on 2019/4/2
 */
public class BaseApiResult<T> {

    private int code;
    private String desc;
    private T data;
    private String apk_url;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getApk_url() {
        return apk_url;
    }

    public void setApk_url(String apk_url) {
        this.apk_url = apk_url;
    }

    public boolean isSuccessful() {
        return code == 200;
    }
}
