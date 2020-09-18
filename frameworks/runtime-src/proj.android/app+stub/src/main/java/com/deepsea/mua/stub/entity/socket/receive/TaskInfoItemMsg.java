package com.deepsea.mua.stub.entity.socket.receive;

public class TaskInfoItemMsg {
    private int TaskId;
    private int Step;
    private int State;

    public int getTaskId() {
        return TaskId;
    }

    public void setTaskId(int taskId) {
        TaskId = taskId;
    }

    public int getStep() {
        return Step;
    }

    public void setStep(int step) {
        Step = step;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }
}
