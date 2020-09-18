package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * 作者：liyaxing  2019/8/23 19:15
 * 类别 ：
 */
public class DynamicDetailReplylistBean {


    /**
     * page : 1
     * list : [{"reply_uid":"1001069","reply_atuid_sex":"2","createtime":"1970-01-01 08:00:00","reply_id":"4","reply_atuid":"1001069","reply_atuid_atnickname":"哦呦","reply_uid_sex":"2","reply_uid_nickname":"哦呦","reply_uid_avatar":"/dir1/1001069_5d5a62c7c5eaa.jpg","reply_content":"回帖","reply_atuid_avatar":"/dir1/1001069_5d5a62c7c5eaa.jpg"}]
     * pagenum : 15
     */
    public String page;
    public List<ListEntity> list;
    public String pagenum;

    public class ListEntity {
        /**
         * reply_uid : 1001069
         * reply_atuid_sex : 2
         * createtime : 1970-01-01 08:00:00
         * reply_id : 4
         * reply_atuid : 1001069
         * reply_atuid_atnickname : 哦呦
         * reply_uid_sex : 2
         * reply_uid_nickname : 哦呦
         * reply_uid_avatar : /dir1/1001069_5d5a62c7c5eaa.jpg
         * reply_content : 回帖
         * reply_type : 1 回帖  2  评论回帖
         * reply_atuid_avatar : /dir1/1001069_5d5a62c7c5eaa.jpg
         */
        public String reply_uid;
        public String reply_atuid_sex;
        public String createtime;
        public String reply_id;
        public String reply_atuid;
        public String reply_atuid_atnickname;
        public String reply_uid_sex;
        public String reply_uid_nickname;
        public String reply_uid_avatar;
        public String reply_content;
        public String reply_atuid_avatar;
        public String reply_type;
        public int reply_uid_age;
        public String reply_uid_city;
        public String forum_id;
        public int reply_num;

        @Override
        public String toString() {
            return "ListEntity{" +
                    "reply_uid='" + reply_uid + '\'' +
                    ", reply_atuid_sex='" + reply_atuid_sex + '\'' +
                    ", createtime='" + createtime + '\'' +
                    ", reply_id='" + reply_id + '\'' +
                    ", reply_atuid='" + reply_atuid + '\'' +
                    ", reply_atuid_atnickname='" + reply_atuid_atnickname + '\'' +
                    ", reply_uid_sex='" + reply_uid_sex + '\'' +
                    ", reply_uid_nickname='" + reply_uid_nickname + '\'' +
                    ", reply_uid_avatar='" + reply_uid_avatar + '\'' +
                    ", reply_content='" + reply_content + '\'' +
                    ", reply_atuid_avatar='" + reply_atuid_avatar + '\'' +
                    ", reply_type='" + reply_type + '\'' +
                    ", reply_uid_age='" + reply_uid_age + '\'' +
                    ", reply_uid_city='" + reply_uid_city + '\'' +
                    ", reply_num=" + reply_num +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DynamicDetailReplylistBean{" +
                "page='" + page + '\'' +
                ", list=" + list +
                ", pagenum='" + pagenum + '\'' +
                '}';
    }
}
