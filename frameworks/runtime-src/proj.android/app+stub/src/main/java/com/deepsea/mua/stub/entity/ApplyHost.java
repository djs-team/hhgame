package com.deepsea.mua.stub.entity;

import java.util.List;

public class ApplyHost {

    /**
     * result : [{"up":"累计连麦时长≥36000小时","down":"已完成连麦时长:0小时","right":"去完成"},{"up":"单场收到总礼物价值≥5200玫瑰","down":"单场收到礼物总价值:0玫瑰","right":"去完成"},{"up":"收到1个\u201c星主持\u201d礼物","down":"收到\u201c星主持\u201d礼物数:0个","right":"去完成"},{"up":"成功邀请10人注册登录","down":"已成功邀请人数:0人","right":"去邀请"}]
     * attestation : 1
     * state : 1
     */

    private String attestation;
    private int state;
    private List<ResultBean> result;
    private String info_up;
    private String info_down;

    public String getInfo_up() {
        return info_up;
    }

    public void setInfo_up(String info_up) {
        this.info_up = info_up;
    }

    public String getInfo_down() {
        return info_down;
    }

    public void setInfo_down(String info_down) {
        this.info_down = info_down;
    }

    public String getAttestation() {
        return attestation;
    }

    public void setAttestation(String attestation) {
        this.attestation = attestation;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * up : 累计连麦时长≥36000小时
         * down : 已完成连麦时长:0小时
         * right : 去完成
         */

        private String up;
        private String down;
        private String right;
        private int type;//1:随机进入前6直播间   2:跳转我的邀请码页面
        private int status;//1:未完成    2:已完成

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUp() {
            return up;
        }

        public void setUp(String up) {
            this.up = up;
        }

        public String getDown() {
            return down;
        }

        public void setDown(String down) {
            this.down = down;
        }

        public String getRight() {
            return right;
        }

        public void setRight(String right) {
            this.right = right;
        }
    }
}
