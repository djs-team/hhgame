package com.deepsea.mua.stub.utils;

import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.entity.GetAwardTaskId;
import com.deepsea.mua.stub.entity.socket.receive.TaskItemMsg;

import java.util.ArrayList;
import java.util.List;

public class TaskUtils {

    public static List<Integer> getConditionTypePosition(int conditionType) {
        List<Integer> integerList = new ArrayList<>();
        integerList.clear();
        List<TaskItemMsg> itemMsgs = RoomController.getInstance().getTaskItemMsgList();
        for (int i = 0; i < itemMsgs.size(); i++) {
            if (itemMsgs.get(i).getConditionType() == conditionType) {
                integerList.add(i);
            }
        }
        return integerList;
    }

    public static String getTaskExplain() {
        String explain = "\n每日任务统计每日的下列数据：\n";
        List<TaskItemMsg> itemMsgs = RoomController.getInstance().getTaskItemMsgList();
        for (TaskItemMsg msg : itemMsgs) {
            explain += ("• " + msg.getTaskDesc() + "\n");
        }
        return explain;
    }

    public static TaskItemMsg findTaskItemById(int taskId) {
        List<TaskItemMsg> itemMsgs = RoomController.getInstance().getTaskItemMsgList();
        for (int i = 0; i < itemMsgs.size(); i++) {
            if (itemMsgs.get(i).getTaskId() == taskId) {
                return itemMsgs.get(i);
            }
        }
        return null;
    }

    public static void setTaskCountStateById(int taskId, boolean isStartCount) {
        List<TaskItemMsg> itemMsgs = RoomController.getInstance().getTaskItemMsgList();
        for (int i = 0; i < itemMsgs.size(); i++) {
            if (itemMsgs.get(i).getTaskId() == taskId) {
                itemMsgs.get(i).setStartCount(isStartCount);
            }
        }
    }

    public static List<GetAwardTaskId> getTaskListAwards() {
        List<GetAwardTaskId> data = new ArrayList<>();
        List<TaskItemMsg> itemMsgs = RoomController.getInstance().getTaskItemMsgList();
        for (int i = 0; i < itemMsgs.size(); i++) {
            if (itemMsgs.get(i).getState() == 4) {
                GetAwardTaskId getAwardTaskId = new GetAwardTaskId();
                getAwardTaskId.setTaskId(itemMsgs.get(i).getTaskId());
                data.add(getAwardTaskId);
            }
        }
        return data;
    }

    public static TaskItemMsg isCanGetAward() {
        TaskItemMsg taskItemMsg = null;
        List<TaskItemMsg> itemMsgs = RoomController.getInstance().getTaskItemMsgList();
        for (int i = 0; i < itemMsgs.size(); i++) {
            if (itemMsgs.get(i).getPreTaskId() > 0 && (itemMsgs.get(i).getState() == 2 || itemMsgs.get(i).getState() == 4)) {
                taskItemMsg = itemMsgs.get(i);
            }
        }
        return taskItemMsg;
    }

    public static TaskItemMsg getConditionTask() {
        TaskItemMsg taskItemMsg = null;
        List<TaskItemMsg> itemMsgs = RoomController.getInstance().getTaskItemMsgList();
        for (int i = 0; i < itemMsgs.size(); i++) {
            if (itemMsgs.get(i).getPreTaskId() > 0) {
                taskItemMsg = itemMsgs.get(i);
            }
        }
        return taskItemMsg;
    }

    public static int findTaskPositionById(int taskId) {
        int position = -1;
        List<TaskItemMsg> itemMsgs = RoomController.getInstance().getTaskItemMsgList();
        for (int i = 0; i < itemMsgs.size(); i++) {
            if (itemMsgs.get(i).getTaskId() == taskId) {
                return i;
            }
        }
        return position;
    }

    public static boolean hasUnFinishedTask() {
        List<TaskItemMsg> itemMsgs = RoomController.getInstance().getTaskItemMsgList();
        if (itemMsgs == null) {
            return false;
        }
        for (TaskItemMsg itemMsg : itemMsgs) {
            if (itemMsg.getState() == 1) {
                return true;
            }
        }
        return false;

    }
}
