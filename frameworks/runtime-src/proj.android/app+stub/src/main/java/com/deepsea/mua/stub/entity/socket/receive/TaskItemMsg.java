package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class TaskItemMsg implements Comparable<TaskItemMsg> {

    private int TaskId;
    private String TaskName;
    private String TaskDesc;
    private int ConditionType;
    private boolean IsTimer;
    private int Step;
    private int FinishStep;
    private int State; //1为进行中，2为已完成，3为失效,4,可领取，5已完成，待确认
    private List<AwardItem> Awards;
    private boolean isFemaleGuestOnMicro;
    private int StartCounting;
    private int StopCounting;
    private boolean isStartCount;
    private int PreTaskId;//没有为0，有大于零
    private int TaskType;// 0  每日任务  1 重复任务

    public int getTaskType() {
        return TaskType;
    }

    public void setTaskType(int taskType) {
        TaskType = taskType;
    }

    public boolean isStartCount() {
        return isStartCount;
    }

    public int getPreTaskId() {
        return PreTaskId;
    }

    public void setPreTaskId(int preTaskId) {
        PreTaskId = preTaskId;
    }

    public void setStartCount(boolean startCount) {
        isStartCount = startCount;
    }

    public int getStartCounting() {
        return StartCounting;
    }

    public void setStartCounting(int startCounting) {
        StartCounting = startCounting;
    }

    public int getStopCounting() {
        return StopCounting;
    }

    public void setStopCounting(int stopCounting) {
        StopCounting = stopCounting;
    }

    //    public enum ConditionType
//    {
//        /// <summary>
//        /// 身份验证
//        /// </summary>
//        IdentityVerify = 1,
//        /// <summary>
//        /// 开播（次）
//        /// </summary>
//        OpenRoomNumber = 2,
//        /// <summary>
//        /// 开播（时长）
//        /// </summary>
//        OpenRoomTime = 3,
//        /// <summary>
//        /// 女嘉宾上麦（时长）
//        /// </summary>
//        OnMicroWithFemaleGuest = 4,
//        /// <summary>
//        /// 相亲（次）
//        /// </summary>
//        BlindDate = 5,
//        /// <summary>
//        /// 访问人数
//        /// </summary>
//        AccessNumbers = 6,
//        /// <summary>
//        /// 房间里玫瑰花费
//        /// </summary>
//        RoseSpentInRoom = 7,
//    }
    public boolean isFemaleGuestOnMicro() {
        return isFemaleGuestOnMicro;
    }

    public void setFemaleGuestOnMicro(boolean femaleGuestOnMicro) {
        isFemaleGuestOnMicro = femaleGuestOnMicro;
    }

    public boolean isTimer() {
        return IsTimer;
    }

    public void setTimer(boolean timer) {
        IsTimer = timer;
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

    public String getTaskDesc() {
        return TaskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        TaskDesc = taskDesc;
    }

    public int getConditionType() {
        return ConditionType;
    }

    public void setConditionType(int conditionType) {
        ConditionType = conditionType;
    }

    public int getStep() {
        return Step;
    }

    public void setStep(int step) {
        Step = step;
    }

    public int getFinishStep() {
        return FinishStep;
    }

    public void setFinishStep(int finishStep) {
        FinishStep = finishStep;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public List<AwardItem> getAwards() {
        return Awards;
    }

    public void setAwards(List<AwardItem> awards) {
        Awards = awards;
    }

    @Override
    public int compareTo(TaskItemMsg o) {
        int i = this.getTaskId() - o.getTaskId();//先按照年龄排序
        if (i == 0) {
            return this.getTaskId() - o.getTaskId();//如果年龄相等了再用分数进行排序
        }
        return i;

    }
}
