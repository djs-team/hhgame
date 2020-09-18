package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/5/5
 */
public class ChargeBean {
    private List<ChargeListBean> charge_list;
    private int is_first;//是否首充1:是   2:不是

    public int getIs_first() {
        return is_first;
    }

    public void setIs_first(int is_first) {
        this.is_first = is_first;
    }

    public List<ChargeListBean> getCharge_list() {
        return charge_list;
    }

    public void setCharge_list(List<ChargeListBean> charge_list) {
        this.charge_list = charge_list;
    }

    public static class ChargeListBean {
        /**
         * chargeid : 54
         * rmb : 30.00
         * diamond : 2250
         * present : 60
         * chargemsg : 充了一小笔寻币，想找人聊聊。
         * vipgift : 150
         * coinimg :
         */

        private String chargeid;
        private String rmb;
        private String diamond;
        private String present;
        private String chargemsg;
        private String vipgift;
        private String coinimg;
        private String paytype;

        public String getChargeid() {
            return chargeid;
        }

        public void setChargeid(String chargeid) {
            this.chargeid = chargeid;
        }

        public String getRmb() {
            return rmb;
        }

        public void setRmb(String rmb) {
            this.rmb = rmb;
        }

        public String getDiamond() {
            return diamond;
        }

        public void setDiamond(String diamond) {
            this.diamond = diamond;
        }

        public String getPresent() {
            return present;
        }

        public void setPresent(String present) {
            this.present = present;
        }

        public String getChargemsg() {
            return chargemsg;
        }

        public void setChargemsg(String chargemsg) {
            this.chargemsg = chargemsg;
        }

        public String getVipgift() {
            return vipgift;
        }

        public void setVipgift(String vipgift) {
            this.vipgift = vipgift;
        }

        public String getCoinimg() {
            return coinimg;
        }

        public void setCoinimg(String coinimg) {
            this.coinimg = coinimg;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }
    }
}
