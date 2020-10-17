package com.deepsea.mua.stub.entity;

import java.util.List;

public class RenewInitVo {


    /**
     * data : [{"long_time":"720","coin":"15","ori_coin":"15"},{"long_time":"2160","coin":"36","ori_coin":"45"},{"long_time":"8760","coin":"108","ori_coin":"180"}]
     * nickname : 啧啧
     * id : 10375
     * is_auto : 2
     */

    private String nickname;
    private String id;
    private int is_auto;
    private int is_shouhu;
    private long etime;

    private List<DataBean> data;

    public long getEtime() {
        return etime;
    }

    public void setEtime(long etime) {
        this.etime = etime;
    }

    public int getIs_shouhu() {
        return is_shouhu;
    }

    public void setIs_shouhu(int is_shouhu) {
        this.is_shouhu = is_shouhu;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIs_auto() {
        return is_auto;
    }

    public void setIs_auto(int is_auto) {
        this.is_auto = is_auto;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * long_time : 720
         * coin : 15
         * ori_coin : 15
         */
        private String id;
        private String paytype;
        private String long_time;
        private String coin;
        private String ori_coin;
        private String charge_id;

        public String getCharge_id() {
            return charge_id;
        }

        public void setCharge_id(String charge_id) {
            this.charge_id = charge_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getLong_time() {
            return long_time;
        }

        public void setLong_time(String long_time) {
            this.long_time = long_time;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getOri_coin() {
            return ori_coin;
        }

        public void setOri_coin(String ori_coin) {
            this.ori_coin = ori_coin;
        }
    }
}
