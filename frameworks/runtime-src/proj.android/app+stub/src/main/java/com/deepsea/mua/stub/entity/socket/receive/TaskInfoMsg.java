package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class TaskInfoMsg {


    /**
     * MsgId : 82
     * TaskItems : [{"TaskId":2,"TaskName":"开播","TaskDesc":"开播- 是否开播  只要主持新建并且进入了房间，就奖励¥2；","ConditionType":0,"IsTimer":false,"Step":0,"FinishStep":0,"State":1,"Awards":[{"AwardType":0,"AwardCount":2,"ItemId":0}]},{"TaskId":3,"TaskName":"直播3小时","TaskDesc":"直播- 主持共计开播时间汇总，到精确到分钟；主持开播3小时，则奖励¥28；","ConditionType":0,"IsTimer":false,"Step":0,"FinishStep":0,"State":1,"Awards":[{"AwardType":0,"AwardCount":28,"ItemId":0}]},{"TaskId":4,"TaskName":"女嘉宾连麦10分钟","TaskDesc":"♂位- 女嘉宾位的开播时间汇总，精确到分钟；女嘉宾为就位累计10分钟，则奖励¥20","ConditionType":0,"IsTimer":false,"Step":0,"FinishStep":0,"State":1,"Awards":[{"AwardType":0,"AwardCount":20,"ItemId":0}]},{"TaskId":5,"TaskName":"男女嘉宾连麦","TaskDesc":"♂♀连麦 - 是否有男女嘉宾共同连麦的情况发生；如果有则奖励¥10；","ConditionType":0,"IsTimer":false,"Step":0,"FinishStep":0,"State":1,"Awards":[{"AwardType":0,"AwardCount":10,"ItemId":0}]},{"TaskId":6,"TaskName":"视频房访问人数达15人","TaskDesc":"人数- 访问该房间的人数（不是人次，需要多次访问去重）；如果累计达到15人，则奖励¥10","ConditionType":0,"IsTimer":false,"Step":0,"FinishStep":0,"State":1,"Awards":[{"AwardType":0,"AwardCount":10,"ItemId":0}]},{"TaskId":7,"TaskName":"视频房消耗玫瑰达1500朵","TaskDesc":"玫瑰- 该房间玫瑰的总流水，包括上麦花费和送礼花费；达到则奖励¥80","ConditionType":0,"IsTimer":false,"Step":0,"FinishStep":0,"State":1,"Awards":[{"AwardType":0,"AwardCount":80,"ItemId":0}]}]
     */

    private int MsgId;
    private List<TaskInfoItemMsg> TaskInfos;

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int MsgId) {
        this.MsgId = MsgId;
    }

    public List<TaskInfoItemMsg> getTaskItems() {
        return TaskInfos;
    }

    public void setTaskItems(List<TaskInfoItemMsg> TaskInfos) {
        this.TaskInfos = TaskInfos;
    }


}
