package com.deepsea.mua.stub.entity.socket.receive;

import com.deepsea.mua.stub.entity.socket.BaseMsg;
import com.deepsea.mua.stub.entity.socket.SmashBean;

import java.util.List;

/**
 * Created by JUN on 2019/7/30
 */
public class SmashEggBeanList extends BaseMsg {
   private List<SmashEggBean> Args;

    public List<SmashEggBean> getArgs() {
        return Args;
    }

    public void setArgs(List<SmashEggBean> args) {
        Args = args;
    }
}
