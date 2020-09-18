package com.deepsea.mua.stub.entity.socket.receive;

public class UpdateFinishTaskToClientParam {
    private int TaskId;
    private int MsgId;
    private String Code;
    private String TaskName;
    private int AwardType;
    private int AwardNum;


    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getTaskId() {
        return TaskId;
    }

    public void setTaskId(int taskId) {
        TaskId = taskId;
    }

    public String getTaskName() {
        return TaskName;
    }

    public void setTaskName(String taskName) {
        TaskName = taskName;
    }

    public int getAwardType() {
        return AwardType;
    }

    public void setAwardType(int awardType) {
        AwardType = awardType;
    }

    public int getAwardNum() {
        return AwardNum;
    }

    public void setAwardNum(int awardNum) {
        AwardNum = awardNum;
    }
}
