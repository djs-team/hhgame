package com.deepsea.mua.stub.entity.socket.send;

import com.deepsea.mua.stub.entity.socket.BaseMsg;

/**
 * Created by JUN on 2019/8/22
 */
public class SendTokenMsg extends BaseMsg {
    private String UserToken;

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        UserToken = userToken;
    }
}
