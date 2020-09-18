package com.deepsea.mua.stub.network;

/**
 * Created by JUN on 2019/4/14
 */
public interface HttpConst {

    int CONNECT_TIME_OUT = 10;
    int WRITE_TIME_OUT = 10;
    int READ_TIME_OUT = 10;

    int HTTP_ERROR_CODE = -10000;

    int RESULT_SUCCESS = 200;
    int RESULT_TOKEN_EXPIRED = 401;

    int ERR_BASE = 1000;
    int ERR_UNKNOWN = ERR_BASE + 1;
    int ERR_NETWORK = ERR_BASE + 2;
    int ERR_JSONPARSE = ERR_BASE + 3;

    String SERVER_ERROR = "服务器异常";
    String NETWORK_UNABLE = "当前网络不可用，请检查网络设置";
}
