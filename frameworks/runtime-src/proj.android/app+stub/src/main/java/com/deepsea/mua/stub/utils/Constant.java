package com.deepsea.mua.stub.utils;

/**
 * Created by JUN on 2019/4/3
 */
public interface Constant {

    //自由麦
    String FREE_MP = "2";
    //非自由麦
    String UNFREE_MP = "1";

    //禁言
    String FORBIDDEN = "1";

    //m豆
    String MD = "MD";
    //钻石
    String DIAMOND = "DIAMOND";

    //购买类型 1充值 2守护
    String CHARGE_NORMAL = "1";
    String CHARGE_Guard = "2";

    //状态 状态 0充值 1续费守护 2激活守护
    String CHARGE_ACT_N = "0";
    String CHARGE_ACT_X = "1";
    String CHARGE_ACT_J = "2";

    //支付方式
    String CHARGE_WX = "weixin";
    String CHARGE_ALI = "alipay";
    String CHARGE_HAIBEI = "haipay";
    String BALANCE = "BALANCE";
    String HHGAME_PHOTO = "HHGAME_PHOTO";
    String RECHARGE_WELFARE = "RECHARGE_WELFARE";
    String RECHARGE_SUCCESS = "RECHARGE_SUCCESS";



    int BALANCE_CODE = 1000;
    String Province = "province";
    String City = "city";
    String AREA = "area";
    String Age = "age";
    String URL = "url";


    //在原有访问地址拼接尾缀?x-oss-process=image/resize,m_fill,h_高度,w_宽度,提高图片加载速度,访问OSS定义缩略图,提高用户体验
    String OSS_SMALL_PIC = "?x-oss-process=image/resize,m_fill,h_150,w_150";

    class ConditionType {
        public static final int OpenRoomNumber = 2;
        public static final int OpenRoomTime = 3;
        public static final int OnMicroWithFemaleGuest = 4;
    }

    public static final boolean isBeautyOpen = true;

    class RequestCode {
        public static final int RQ_addFriend = 2001;
        public static final int RQ_operate_wx = 2002;
        public static final int RQ_BLUE_ROSE_EXCHANGE = 2003;
        public static final int RQ_RED_ROSE_EXCHANGE = 2004;
        public static final int AREA_CODE = 2005;
        public static final int RQ_TASK_CODE = 2006;
        public static final int RQ_BIND_PHONE = 2007;
    }

    class MessageCode {
        public static final int msg_getoss = 1001;
    }

}
