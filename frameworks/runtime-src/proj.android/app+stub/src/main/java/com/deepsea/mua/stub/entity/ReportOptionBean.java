package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * 作者：liyaxing  2019/8/23 17:57
 * 类别 ：
 */
public class ReportOptionBean {


    /**
     * code : 200
     * data : [{"id":"1","content":"色情"},{"id":"2","content":"反动"}]
     * desc : 操作成功
     */
    public int code;
    public List<DataEntity> data;
    public String desc;

    public class DataEntity {
        /**
         * id : 1
         * content : 色情
         */
        public String id;
        public String content;
    }
}
