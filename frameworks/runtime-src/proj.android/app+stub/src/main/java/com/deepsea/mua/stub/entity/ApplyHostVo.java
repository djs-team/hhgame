package com.deepsea.mua.stub.entity;

public class ApplyHostVo {

    /**
     * result : {"one":"累计连麦时长>10小时","two":"守护团人数5人","three":"成功邀请10人注册登录"}
     * status : 10
     */

    private ResultBean result;
    private String status;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class ResultBean {
        /**
         * one : 累计连麦时长>10小时
         * two : 守护团人数5人
         * three : 成功邀请10人注册登录
         */

        private String one;
        private String two;
        private String three;

        public String getOne() {
            return one;
        }

        public void setOne(String one) {
            this.one = one;
        }

        public String getTwo() {
            return two;
        }

        public void setTwo(String two) {
            this.two = two;
        }

        public String getThree() {
            return three;
        }

        public void setThree(String three) {
            this.three = three;
        }
    }
}
