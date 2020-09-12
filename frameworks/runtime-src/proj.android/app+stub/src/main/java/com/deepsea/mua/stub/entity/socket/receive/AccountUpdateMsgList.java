package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.BaseMsg;

import java.util.List;

/**
 * Created by JUN on 2019/9/9
 */
public class AccountUpdateMsgList extends BaseMsg {
   private List<AccountUpdateMsg> Args;

    public List<AccountUpdateMsg> getArgs() {
        return Args;
    }

    public void setArgs(List<AccountUpdateMsg> args) {
        Args = args;
    }
}
