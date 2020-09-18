package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.BaseMsg;
import com.deepsea.mua.stub.entity.socket.GiftData;
import com.deepsea.mua.stub.entity.socket.WsUser;

import java.util.List;

/**
 * Created by JUN on 2019/4/25
 * 接收到礼物
 */
public class ReceivePresentList extends BaseMsg {
   List<ReceivePresent> Args;

    public List<ReceivePresent> getArgs() {
        return Args;
    }

    public void setArgs(List<ReceivePresent> args) {
        Args = args;
    }
}
