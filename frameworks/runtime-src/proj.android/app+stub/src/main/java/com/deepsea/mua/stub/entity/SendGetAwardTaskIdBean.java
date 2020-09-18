package com.deepsea.mua.stub.entity;

import java.util.List;

public class SendGetAwardTaskIdBean {
    private List<GetAwardTaskId> TaskIds;
    private int MsgId;

    public List<GetAwardTaskId> getTaskIds() {
        return TaskIds;
    }

    public void setTaskIds(List<GetAwardTaskId> taskIds) {
        TaskIds = taskIds;
    }

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }
}
