package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.BaseMsg;

/**
 * Created by JUN on 2019/8/22
 */
public class BaseReMsg extends BaseMsg {
    private int Success;

    public int getSuccess() {
        return Success;
    }

    public void setSuccess(int success) {
        Success = success;
    }
}
