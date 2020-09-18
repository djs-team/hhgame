package com.deepsea.mua.stub.entity;

import com.deepsea.mua.stub.entity.socket.receive.TaskInfoItemMsg;

import java.util.List;

public class GetTaskAwardsParam {
    private TaskInfoItemMsg FinishedTask;
    private String Code;

    public TaskInfoItemMsg getFinishedTask() {
        return FinishedTask;
    }

    public void setFinishedTask(TaskInfoItemMsg finishedTask) {
        FinishedTask = finishedTask;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
