package com.deepsea.mua.stub.apiaddress;

import com.deepsea.mua.stub.utils.SPUtils;

/**
 * Created by JUN on 2019/7/12
 */
public class BaseAddress {
    private static final String HOST_KEY = "host_key";
    public static final String wcUrl = "ws://";
    //本地
//     private static final String RELEASE_URL = "https://test-lin.hehe555.com:85/";
//    private static final String RELEASE_WSOCKET = "http://10.66.66.77:95/Master/GetGate";
////    外网测试
    private static final String RELEASE_URL = "https://test-lin.hehe555.com:85/";
//    private static final String RELEASE_WSOCKET = "http://10.66.6.207:444/Master/GetGate";
    private static final String RELEASE_WSOCKET = "https://test-win.hehe555.com:444/Master/GetGate";


    ////    正式
    public static boolean isRelease = true;
//    private static final String RELEASE_URL = "https://lin01.hehe555.com:85/";
//    private static final String RELEASE_WSOCKET = "https://win02.hehe555.com:444/Master/GetGate";

    public String getLrcCode() {
        return "gbk";
    }

    public String getHostUrl() {
        String url = RELEASE_URL;
        return url;
    }

    public String getSocketUrl() {
        String url = RELEASE_WSOCKET;
        return url;
    }

    public String getHostType() {
        return SPUtils.getString(HOST_KEY, HostType.RELEASE);
    }

    public void setHostType(String hostType) {
        SPUtils.put(HOST_KEY, hostType);
    }

    public interface HostType {
        String LOCAL = "local";
        String DEBUG = "debug";
        String RELEASE = "release";
    }
}
