package com.deepsea.mua.stub.entity;

/**
 * 作者：liyaxing  2019/9/2 17:54
 * 类别 ：
 */
public class IdcardBean {

    /**
     * result : {"realName":"李xxx","details":{"addrCode":"420606","sex":1,"length":18,"birth":"1984-02-15","addr":"湖北省襄樊市樊城区","checkBit":"1"},"cardNo":"4206061"}
     * reason : 认证通过
     * error_code : 0
     * ordersign : 2017052722072914949571005
     */
    public ResultEntity result;
    public String reason;
    public int error_code;
    public String ordersign;

    public class ResultEntity {
        /**
         * realName : 李xxx
         * details : {"addrCode":"420606","sex":1,"length":18,"birth":"1984-02-15","addr":"湖北省襄樊市樊城区","checkBit":"1"}
         * cardNo : 4206061
         */
        public String realName;
        public DetailsEntity details;
        public String cardNo;

        public class DetailsEntity {
            /**
             * addrCode : 420606    地区编码
             * sex : 1          性别
             * length : 18         身份证位数
             * birth : 1984-02-15     出生日期
             * addr : 湖北省襄樊市樊城区     身份证所在地
             * checkBit : 1          身份证最后一位
             */
            public String addrCode;
            public int sex;
            public int length;
            public String birth;
            public String addr;
            public String checkBit;

            @Override
            public String toString() {
                return "DetailsEntity{" +
                        "addrCode='" + addrCode + '\'' +
                        ", sex=" + sex +
                        ", length=" + length +
                        ", birth='" + birth + '\'' +
                        ", addr='" + addr + '\'' +
                        ", checkBit='" + checkBit + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ResultEntity{" +
                    "realName='" + realName + '\'' +
                    ", details=" + details +
                    ", cardNo='" + cardNo + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "IdcardBean{" +
                "result=" + result +
                ", reason='" + reason + '\'' +
                ", error_code=" + error_code +
                ", ordersign='" + ordersign + '\'' +
                '}';
    }
}
