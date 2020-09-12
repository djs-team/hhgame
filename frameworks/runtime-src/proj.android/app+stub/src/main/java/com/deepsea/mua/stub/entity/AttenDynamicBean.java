package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * author : liyaxing
 * date   : 2019/3/23 12:50
 * desc   :
 */
public class AttenDynamicBean {


    public List<DynamicVoListBean> dynamicVoList;//关注的动态
    public List<FllowUserListBean> fllowUserList;//推荐关注的人


    public static class DynamicVoListBean {


        /**
         * auditStatus : AUDIT_SUCCESS
         * commentCount : 0
         * content : 原来眼泪可以瞬间爆棚，那一刻只想哭！
         * createTime : 1552650892000
         * createUserId : 1105313136069234688
         * deleteFlag : false
         * dynamicId : 1106524159029071872
         * dynamicMinAgo : 7天前
         * dynamicShareId :
         * dynamicType : RELEASE
         * dynamicUserVo : {"age":25,"birthday":788889600000,"iconUrl":"https://kkorange-read.oss-cn-beijing.aliyuncs.com/1105313136069234688/1552362377296","iconValidId":"","identificationFlag":false,"knighthoodIcon":"https://kkorange-read.oss-cn-beijing.aliyuncs.com/1099953106494910464/1100376313010266114.png","knighthoodId":"1085221190734740672","knighthoodMedal":"https://kkorange-read.oss-cn-beijing.aliyuncs.com/1098853639261519872/you1099945593474478080.png","knighthoodName":"游侠","level":0,"minAgo":"7天前","roomId":"1105603872808685568","roomImage":"https://kkorange-read.oss-cn-beijing.aliyuncs.com/1105313136069234688/1552362377296","roomName":"蓝潇吖","roomTypeName":"秀场","roomUnique":"9960552","sex":"FEMALE","signature":"","startFlag":true,"thirdPartyNo":"1105603872808685569","userId":"1105313136069234688","username":"蓝潇吖"}
         * enableFlag : false
         * fileId :
         * fileType :
         * fileUrls :
         * followFlag : true
         * giftCount : 0
         * giftFlag : false
         * myselfFlag : false
         * praiseCount : 2
         * praiseFlag : false
         * previewUrl :
         * readCount : 10
         * selectedFlag : false
         * shareContent :
         * shareCount : 0
         * shareUserId :
         * shareUserName :
         * topicIds : 1103316773262499840
         * topicNames : 每日打卡
         * updateTime : 1553314075000
         * updateUserId : 1109063703062302720
         * userId : 1105313136069234688
         */

        public String auditStatus;
        public String commentCount;
        public String content;
        public String createTime;
        public String createUserId;
        public boolean deleteFlag;
        public String dynamicId;
        public String dynamicMinAgo;
        public String dynamicShareId;
        public String dynamicType;
        public DynamicUserVoBean dynamicUserVo;
        public boolean enableFlag;
        public String fileId;
        public String fileType;
        public String fileUrls;
        public boolean followFlag;
        public String giftCount;
        public boolean giftFlag;
        public boolean myselfFlag;
        public String praiseCount;
        public boolean praiseFlag;
        public String previewUrl;
        public String readCount;
        public boolean selectedFlag;
        public String shareContent;
        public int shareCount;
        public String shareUserId;
        public String shareUserName;
        public String topicIds;
        public String topicNames;
        public long updateTime;
        public String updateUserId;
        public String userId;

        @Override
        public String toString() {
            return "DynamicVoListBean{" +
                    "auditStatus='" + auditStatus + '\'' +
                    ", commentCount=" + commentCount +
                    ", content='" + content + '\'' +
                    ", createTime=" + createTime +
                    ", createUserId='" + createUserId + '\'' +
                    ", deleteFlag=" + deleteFlag +
                    ", dynamicId='" + dynamicId + '\'' +
                    ", dynamicMinAgo='" + dynamicMinAgo + '\'' +
                    ", dynamicShareId='" + dynamicShareId + '\'' +
                    ", dynamicType='" + dynamicType + '\'' +
                    ", dynamicUserVo=" + dynamicUserVo +
                    ", enableFlag=" + enableFlag +
                    ", fileId='" + fileId + '\'' +
                    ", fileType='" + fileType + '\'' +
                    ", fileUrls='" + fileUrls + '\'' +
                    ", followFlag=" + followFlag +
                    ", giftCount=" + giftCount +
                    ", giftFlag=" + giftFlag +
                    ", myselfFlag=" + myselfFlag +
                    ", praiseCount=" + praiseCount +
                    ", praiseFlag=" + praiseFlag +
                    ", previewUrl='" + previewUrl + '\'' +
                    ", readCount=" + readCount +
                    ", selectedFlag=" + selectedFlag +
                    ", shareContent='" + shareContent + '\'' +
                    ", shareCount=" + shareCount +
                    ", shareUserId='" + shareUserId + '\'' +
                    ", shareUserName='" + shareUserName + '\'' +
                    ", topicIds='" + topicIds + '\'' +
                    ", topicNames='" + topicNames + '\'' +
                    ", updateTime=" + updateTime +
                    ", updateUserId='" + updateUserId + '\'' +
                    ", userId='" + userId + '\'' +
                    '}';
        }

        public static class DynamicUserVoBean {
            /**
             * age : 25
             * birthday : 788889600000
             * iconUrl : https://kkorange-read.oss-cn-beijing.aliyuncs.com/1105313136069234688/1552362377296
             * iconValidId :
             * identificationFlag : false
             * knighthoodIcon : https://kkorange-read.oss-cn-beijing.aliyuncs.com/1099953106494910464/1100376313010266114.png
             * knighthoodId : 1085221190734740672
             * knighthoodMedal : https://kkorange-read.oss-cn-beijing.aliyuncs.com/1098853639261519872/you1099945593474478080.png
             * knighthoodName : 游侠
             * level : 0
             * minAgo : 7天前
             * roomId : 1105603872808685568
             * roomImage : https://kkorange-read.oss-cn-beijing.aliyuncs.com/1105313136069234688/1552362377296
             * roomName : 蓝潇吖
             * roomTypeName : 秀场
             * roomUnique : 9960552
             * sex : FEMALE
             * signature :
             * startFlag : true
             * thirdPartyNo : 1105603872808685569
             * userId : 1105313136069234688
             * username : 蓝潇吖
             */

            public String age;
            public long birthday;
            public String iconUrl;
            public String iconValidId;
            public boolean identificationFlag;
            public String knighthoodIcon;
            public String knighthoodId;
            public String knighthoodMedal;
            public String knighthoodName;
            public String level;
            public String minAgo;
            public String roomId;
            public String roomImage;
            public String roomName;
            public String roomTypeName;
            public String roomUnique;
            public String sex;
            public String signature;
            public boolean startFlag;
            public String thirdPartyNo;
            public String userId;
            public String username;
            public String levelImg;
            public String levelRight;
            @Override
            public String toString() {
                return "DynamicUserVoBean{" +
                        "age=" + age +
                        ", birthday=" + birthday +
                        ", iconUrl='" + iconUrl + '\'' +
                        ", iconValidId='" + iconValidId + '\'' +
                        ", identificationFlag=" + identificationFlag +
                        ", knighthoodIcon='" + knighthoodIcon + '\'' +
                        ", knighthoodId='" + knighthoodId + '\'' +
                        ", knighthoodMedal='" + knighthoodMedal + '\'' +
                        ", knighthoodName='" + knighthoodName + '\'' +
                        ", level=" + level +
                        ", minAgo='" + minAgo + '\'' +
                        ", roomId='" + roomId + '\'' +
                        ", roomImage='" + roomImage + '\'' +
                        ", roomName='" + roomName + '\'' +
                        ", roomTypeName='" + roomTypeName + '\'' +
                        ", roomUnique='" + roomUnique + '\'' +
                        ", sex='" + sex + '\'' +
                        ", signature='" + signature + '\'' +
                        ", startFlag=" + startFlag +
                        ", thirdPartyNo='" + thirdPartyNo + '\'' +
                        ", userId='" + userId + '\'' +
                        ", username='" + username + '\'' +
                        '}';
            }
        }
    }

    public static class FllowUserListBean  {
        /**
         * age : 25
         * birthday : 788889600000
         * iconUrl : https://kkorange-read.oss-cn-beijing.aliyuncs.com/1105336474082660352/1552641995981
         * iconValidId :
         * identificationFlag : false
         * knighthoodIcon : https://kkorange-read.oss-cn-beijing.aliyuncs.com/1099953106494910464/1100376313010266114.png
         * knighthoodId : 1085221190734740672
         * knighthoodMedal : https://kkorange-read.oss-cn-beijing.aliyuncs.com/1098853639261519872/you1099945593474478080.png
         * knighthoodName : 游侠
         * level : 0
         * minAgo :
         * roomId : 1105663450955042816
         * roomImage : https://kkorange-read.oss-cn-beijing.aliyuncs.com/1105336474082660352/1552641995981
         * roomName : 【陪玩】Ys.女神
         * roomTypeName : 娱乐
         * roomUnique : 7219384
         * sex : FEMALE
         * signature : 应聘跳槽＋V:jcg6662
         * startFlag : true
         * thirdPartyNo : 1105663450955042817
         * userId : 1105336474082660352
         * username : 【陪玩】Ys.女神
         */

        public String age;
        public long birthday;
        public String iconUrl;
        public String iconValidId;
        public boolean identificationFlag;
        public String knighthoodIcon;
        public String knighthoodId;
        public String knighthoodMedal;
        public String knighthoodName;
        public int level;
        public String minAgo;
        public String roomId;
        public String roomImage;
        public String roomName;
        public String roomTypeName;
        public String roomUnique;
        public String sex;
        public String signature;
        public boolean startFlag;
        public boolean isFollow;
        public String thirdPartyNo;
        public String userId;
        public String username;
        public String levelImg;
        public String levelRight;
        @Override
        public String toString() {
            return "FllowUserListBean{" +
                    "age=" + age +
                    ", birthday=" + birthday +
                    ", iconUrl='" + iconUrl + '\'' +
                    ", iconValidId='" + iconValidId + '\'' +
                    ", identificationFlag=" + identificationFlag +
                    ", knighthoodIcon='" + knighthoodIcon + '\'' +
                    ", knighthoodId='" + knighthoodId + '\'' +
                    ", knighthoodMedal='" + knighthoodMedal + '\'' +
                    ", knighthoodName='" + knighthoodName + '\'' +
                    ", level=" + level +
                    ", minAgo='" + minAgo + '\'' +
                    ", roomId='" + roomId + '\'' +
                    ", roomImage='" + roomImage + '\'' +
                    ", roomName='" + roomName + '\'' +
                    ", roomTypeName='" + roomTypeName + '\'' +
                    ", roomUnique='" + roomUnique + '\'' +
                    ", sex='" + sex + '\'' +
                    ", isFollow='" + isFollow + '\'' +
                    ", signature='" + signature + '\'' +
                    ", startFlag=" + startFlag +
                    ", thirdPartyNo='" + thirdPartyNo + '\'' +
                    ", userId='" + userId + '\'' +
                    ", username='" + username + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AttenDynamicBean{" +
                "dynamicVoList=" + dynamicVoList +
                ", fllowUserList=" + fllowUserList +
                '}';
    }
}
