package com.deepsea.mua.stub.entity;

import java.util.List;

public class TaskProgressBean {

    private List<DataBean> week_data;
    private List<DataBean> month_data;

    public List<DataBean> getWeek_data() {
        return week_data;
    }

    public void setWeek_data(List<DataBean> week_data) {
        this.week_data = week_data;
    }

    public List<DataBean> getMonth_data() {
        return month_data;
    }

    public void setMonth_data(List<DataBean> month_data) {
        this.month_data = month_data;
    }

    public static class DataBean {


        /**
         * utid : 1857
         * task_name : 玫瑰总流水
         * task_desc : 玫瑰总流水到达200朵，就可以领取上面的任务奖励。
         * task_type : 3
         * create_time : 1578644711845
         * finish_time : 0
         * lose_efficacy_time : 0
         * state : 1
         * conditions : [{"CdtType":7,"IsShow":true,"Step":0,"FinishStep":600}]
         * step : 0
         * finishstep : 600
         */

        private String utid;
        private String task_name;
        private String task_desc;
        private String task_type;
        private String create_time;
        private String finish_time;
        private String lose_efficacy_time;
        private String state;
        private String conditions;
        private double step;
        private double finishstep;

        public String getUtid() {
            return utid;
        }

        public void setUtid(String utid) {
            this.utid = utid;
        }

        public String getTask_name() {
            return task_name;
        }

        public void setTask_name(String task_name) {
            this.task_name = task_name;
        }

        public String getTask_desc() {
            return task_desc;
        }

        public void setTask_desc(String task_desc) {
            this.task_desc = task_desc;
        }

        public String getTask_type() {
            return task_type;
        }

        public void setTask_type(String task_type) {
            this.task_type = task_type;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getFinish_time() {
            return finish_time;
        }

        public void setFinish_time(String finish_time) {
            this.finish_time = finish_time;
        }

        public String getLose_efficacy_time() {
            return lose_efficacy_time;
        }

        public void setLose_efficacy_time(String lose_efficacy_time) {
            this.lose_efficacy_time = lose_efficacy_time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getConditions() {
            return conditions;
        }

        public void setConditions(String conditions) {
            this.conditions = conditions;
        }

        public double getStep() {
            return step;
        }

        public void setStep(double step) {
            this.step = step;
        }

        public double getFinishstep() {
            return finishstep;
        }

        public void setFinishstep(double finishstep) {
            this.finishstep = finishstep;
        }
    }

}
