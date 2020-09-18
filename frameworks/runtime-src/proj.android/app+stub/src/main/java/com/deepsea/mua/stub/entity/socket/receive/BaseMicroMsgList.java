package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class BaseMicroMsgList {
    private List<BaseMicroMsg> Args;

    public List<BaseMicroMsg> getArgs() {
        return Args;
    }

    public void setArgs(List<BaseMicroMsg> args) {
        Args = args;
    }
}
