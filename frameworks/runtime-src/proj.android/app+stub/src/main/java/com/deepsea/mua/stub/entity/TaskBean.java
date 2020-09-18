package com.deepsea.mua.stub.entity;

import java.util.List;

public class TaskBean {

    /**
     * new_data : [{"name":"上传头像","desc":"上传真实头像，大幅提高曝光率","state":"1","reward":2,"type":"6"},{"name":"实名认证","desc":"完成实名认证，并通过审核","state":"1","reward":2,"type":"7"},{"name":"完善资料","desc":"完整填写全部基本资料和征友条件","state":"1","reward":2,"type":"8"},{"name":"绑定微信","desc":"成功绑定微信，快捷登录","state":"1","reward":2,"type":"9"},{"name":"首次充值","desc":"充值2元可得30玫瑰，多充多送","state":"1","reward":0,"type":"10"},{"name":"首次上麦","desc":"首次在直播间上麦且不低于1分钟","state":"1","reward":2,"type":"11"},{"name":"首次发言","desc":"首次在直播间打字发言","state":"1","reward":2,"type":"12"},{"name":"首次送礼物","desc":"首次送给Ta礼物","state":"1","reward":2,"type":"13"},{"name":"首次加好友","desc":"首次成功添加好友","state":"1","reward":2,"type":"14"},{"name":"首次KTV点歌","desc":"首次使用直播间KTV点歌功能","state":"1","reward":2,"type":"15"}]
     * task_data : [{"name":"每日充值","desc":"成功充值任意金额","state":"1","reward":1,"type":"16"},{"name":"分享","desc":"今日首次分享","state":"1","reward":1,"type":"17"},{"name":"上麦5分钟","desc":"今日单次上麦5分钟","state":"1","reward":1,"type":"18"},{"name":"邀请好友","desc":"通过邀请链接，成功邀请好友注册下载","state":"1","reward":10,"type":"19"}]
     * all_num : 0
     */

    private int all_num;
    private List<TaskItem> new_data;
    private List<TaskItem> task_data;

    public int getAll_num() {
        return all_num;
    }

    public void setAll_num(int all_num) {
        this.all_num = all_num;
    }

    public List<TaskItem> getNew_data() {
        return new_data;
    }

    public void setNew_data(List<TaskItem> new_data) {
        this.new_data = new_data;
    }

    public List<TaskItem> getTask_data() {
        return task_data;
    }

    public void setTask_data(List<TaskItem> task_data) {
        this.task_data = task_data;
    }

    public static class TaskItem {
        /**
         * name : 上传头像
         * desc : 上传真实头像，大幅提高曝光率
         * state : 1
         * reward : 2
         * type : 6
         */

        private String name;
        private String desc;
        private String state;//任务状态：
        //1:进行中  2:已完成   3:失效   4:未领取奖励
        private String reward;
        private String type;
        private String receive_type;

        public String getReceive_type() {
            return receive_type;
        }

        public void setReceive_type(String receive_type) {
            this.receive_type = receive_type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getReward() {
            return reward;
        }

        public void setReward(String reward) {
            this.reward = reward;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

}
