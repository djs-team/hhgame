package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.BaseMsg;
import com.deepsea.mua.stub.entity.socket.RoomData;

import java.util.List;

/**
 * Created by JUN on 2019/4/18
 */
public class UpMicroMsgList extends BaseMsg {
   private List<UpMicroMsg> Args;

    public List<UpMicroMsg> getArgs() {
        return Args;
    }

    public void setArgs(List<UpMicroMsg> args) {
        Args = args;
    }
}
