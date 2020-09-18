package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.BaseMsg;

import java.util.List;

/**
 * Created by JUN on 2019/4/19
 */
public class DownMicroMsgList extends BaseMsg {
    private List<DownMicroMsg> Args;

    public List<DownMicroMsg> getArgs() {
        return Args;
    }

    public void setArgs(List<DownMicroMsg> args) {
        Args = args;
    }
}
