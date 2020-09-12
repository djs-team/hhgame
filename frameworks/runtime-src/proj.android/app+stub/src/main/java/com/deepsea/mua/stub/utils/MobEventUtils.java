package com.deepsea.mua.stub.utils;

import android.content.Context;

import com.deepsea.mua.core.utils.DateUtils;
import com.deepsea.mua.stub.BuildConfig;
import com.deepsea.mua.stub.data.User;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JUN on 2019/6/28
 */
public class MobEventUtils {

    private static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    private static void onEvent(Context context, String eventId, Map<String, String> values) {
        if (!isDebug()) {
            if (values == null) {
                values = commonValues();
            }
            MobclickAgent.onEvent(context, eventId, values);
        }
    }

    private static Map<String, String> commonValues() {
        User user = UserUtils.getUser();
        HashMap<String, String> values = new HashMap<>();
        values.put("uid", user.getUid());
        values.put("nickname", user.getNickname());
        values.put("time", DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));

        return values;
    }

    /**
     * 登录
     *
     * @param context
     */
    public static void onLoginEvent(Context context) {
        onEvent(context, MobConst.EVENT_LOGIN, null);
    }

    /**
     * 注册
     *
     * @param context
     * @param mobile
     */
    public static void onRegisterEvent(Context context, String mobile) {
        Map<String, String> values = new HashMap<>();
        values.put("mobile", mobile);
        values.put("time", DateUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
        onEvent(context, MobConst.EVENT_REGISTER, values);
    }

    /**
     * 退出App
     *
     * @param context
     */
    public static void onExitEvent(Context context) {
        if (isDebug())
            return;
        MobclickAgent.onEvent(context, MobConst.EVENT_EXIT);
    }

    /**
     * 充值
     *
     * @param context
     * @param rechargeAmount
     */
    public static void onRechargeEvent(Context context, String rechargeAmount) {
        if (isDebug() || !FormatUtils.isNumber(rechargeAmount))
            return;
        try {
            int amount = Integer.parseInt(FormatUtils.removeDot(rechargeAmount));
            MobclickAgent.onEventValue(context, MobConst.EVENT_RECHARGE, commonValues(), amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入房间
     *
     * @param context
     */
    public static void onJoinRoom(Context context) {
        onEvent(context, MobConst.EVENT_JOIN_ROOM, null);
    }

    /**
     * 上麦
     *
     * @param context
     */
    public static void onUpMicro(Context context) {
        onEvent(context, MobConst.UP_MICRO_PHONE, null);
    }

    /**
     * 发送消息
     *
     * @param context
     */
    public static void onSendMsg(Context context) {
        onEvent(context, MobConst.SEND_MESSAGE, null);
    }

    /**
     * 发送表情
     *
     * @param context
     */
    public static void onSendEmoji(Context context) {
        onEvent(context, MobConst.SEND_EMOJI, null);
    }

    /**
     * 发送礼物
     *
     * @param context
     */
    public static void onSendGift(Context context) {
        onEvent(context, MobConst.SEND_GIFT, null);
    }

    /**
     * 查看麦位信息
     *
     * @param context
     */
    public static void onCheckMicro(Context context) {
        onEvent(context, MobConst.CHECK_MICRO, null);
    }
}
