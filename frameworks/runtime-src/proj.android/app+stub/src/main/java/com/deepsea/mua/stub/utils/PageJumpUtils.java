package com.deepsea.mua.stub.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.deepsea.mua.im.EaseConstant;
import com.deepsea.mua.stub.app.ActivityCache;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.entity.PushEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by JUN on 2019/9/11
 */
public class PageJumpUtils {

    /**
     * 跳转H5
     *
     * @param url
     */
    public static void jumpToWeb(String url) {
        jumpToWeb("", url);
    }

    /**
     * 跳转H5页面
     *
     * @param title
     * @param url
     */
    public static void jumpToWeb(String title, String url) {
        if (!TextUtils.isEmpty(url)) {
            ArouterUtils.build(ArouterConst.PAGE_WEB)
                    .withString("title", title)
                    .withString("url", url)
                    .navigation();
        }
    }

    /**
     * 推送跳转
     */
    public static void jumpPushDeeplink(PushEvent event) {
        if (event == null || event.getDeepLink() == null) {
            return;
        }
        if (event.getDeepLink().equals("msgPage") || event.getDeepLink().equals("roomPage") || event.getDeepLink().equals("profilePage") || event.getDeepLink().equals("feedPage")) {
            EventBus.getDefault().post(new FragmentTabEvent(event.getDeepLink()));
        } else if (event.getDeepLink().equals("profileId")) {
            //
            jumpToProfile(String.valueOf(event.getValue()));
        } else if (event.getDeepLink().equals("roomId")) {
            //房间
            if (AppManager.getAppManager().currentActivity() != null) {
                new RoomJoinController().startJump(String.valueOf(event.getValue()), -1, false, -1, -1, 5, AppManager.getAppManager().currentActivity());
            }

        } else if (event.getDeepLink().equals("feedId")) {
            jumpToDynamicDetail(String.valueOf(event.getValue()), false);
        } else if (event.getDeepLink().equals("walletPage")) {
            jumpToWallte();
        } else if (event.getDeepLink().equals("followPage")) {
            jumpToFollowAndFan(0);
        } else if (event.getDeepLink().equals("followerPage")) {
            jumpToFollowAndFan(1);
        } else if (event.getDeepLink().equals("visitPage")) {
            jumpToVisitors();
        } else if (event.getDeepLink().equals("invitePage")) {
            jumpToMineInvitors();
        }
    }

    /**
     * 跳转个人主页
     *
     * @param uid
     */
    public static void jumpToProfile(String uid) {
        ArouterUtils.build(ArouterConst.PAGE_PROFILE)
                .withString("uid", uid)
                .navigation(null, new NavCallback() {
                    @Override
                    public void onFound(Postcard postcard) {
                        if (postcard != null) {
                            Object activity = ActivityCache.getInstance().getActivity(postcard.getDestination());
                            if (activity instanceof Activity) {
                                ((Activity) activity).finish();
                            }
                        }
                    }

                    @Override
                    public void onArrival(Postcard postcard) {
                    }
                });
    }

    /**
     * 跳转充值
     *
     * @param balance
     */
    public static void jumpToRecharge(String balance) {
        ArouterUtils.build(ArouterConst.PAGE_RECHARGE)
                .withString("balance", balance)
                .navigation();
    }

    /**
     * 跳转聊天设置
     */

    public static void jumpToChatSetting(String uid) {
        ArouterUtils.build(ArouterConst.PAGE_MSG_CHAT_SETTING)
                .withString("uid", uid)
                .navigation();
    }




    /**
     * 跳转充值 {@link Activity#startActivityForResult(Intent, int)}
     *
     * @param activity
     * @param balance
     */
    public static void jumpToRechargeDialog(Activity activity, String balance, boolean isWelfare, String rmb, String chargeid) {
        ArouterUtils.build(ArouterConst.PAGE_RECHARGE_DIALOG)
                .withString("balance", balance)
                .withBoolean("isWelfare", isWelfare)
                .withString("wf_rmb", rmb)
                .withString("wf_chargeid", chargeid)
                .navigation(activity, Constant.BALANCE_CODE);
    }


    /**
     * 地区选择 {@link Activity#startActivityForResult(Intent, int)}
     *
     * @param activity
     */
    public static void jumpScreenAreaDialog(Activity activity) {
        ArouterUtils.build(ArouterConst.PAGE_SCREEN_DIALOG)
                .navigation(activity, Constant.RequestCode.AREA_CODE);
    }

    /**
     * 关注/粉丝
     *
     * @param pos
     */
    public static void jumpToFollowAndFan(int pos) {
        ArouterUtils.build(ArouterConst.PAGE_ME_FOLLOW_AND_FAN)
                .withInt("pos", pos)
                .navigation();
    }

    /**
     * 最近访客
     */
    public static void jumpToVisitors() {
        ArouterUtils.build(ArouterConst.PAGE_ME_VISITORS)
                .navigation();
    }

    /**
     * 砸蛋
     */
    public static void jumpToEgg() {
        ArouterUtils.build(ArouterConst.PAGE_EGG)
                .navigation();
    }

    /**
     * 最近访客
     */
    public static void jumpToMineInvitors() {
        ArouterUtils.build(ArouterConst.PAGE_ME_MINE_INVITOR)
                .navigation();
    }



    /**
     * 跳转充值
     */
    public static void jumpToWallte() {
        ArouterUtils.build(ArouterConst.PAGE_ME_WALLET)
                .navigation();
    }


    /**
     * 跳转充值 {@link Activity#startActivityForResult(Intent, int)}
     *
     * @param activity
     * @param balance
     */
    public static void jumpToRecharge(Activity activity, String balance) {
        ArouterUtils.build(ArouterConst.PAGE_RECHARGE)
                .withString("balance", balance)
                .navigation(activity, Constant.BALANCE_CODE);
    }



    /**
     * 跳转到私聊
     *
     * @param uid
     */
    public static void jumpToChat(String uid) {
        ArouterUtils.build(ArouterConst.PAGE_CHAT)
                .withString("userId", uid)
                .navigation();
    }

    /**
     * 跳转到私聊
     *
     * @param uid
     */
    public static void jumpToChat(String uid, String uname, String onlineState) {
        ArouterUtils.build(ArouterConst.PAGE_CHAT)
                .withString("userId", uid)
                .withString(EaseConstant.EXTRA_USER_NICKNAME, uname)
                .withString(EaseConstant.EXTRA_USER_ONLINE, onlineState)
                .navigation();
    }

    /**
     * 跳转到聊天
     *
     * @param uid
     * @param chatType
     */
    public static void jumpToChat(String uid, int chatType) {
        ArouterUtils.build(ArouterConst.PAGE_CHAT)
                .withString("uid", uid)
                .withInt("chatType", chatType)
                .navigation();
    }

    /**
     * 跳转到动态详情
     *
     * @param id        帖子id
     * @param isComment 是不是要弹出评论列表
     */
    public static void jumpToDynamicDetail(String id, boolean isComment) {
        ArouterUtils.build(ArouterConst.PAGE_DYNAMIC_DETAIL)
                .withString("ID", id)
                .withBoolean("ISCOMMENT", isComment)
                .navigation();
    }

    /**
     * 添加好友页
     */

    public static void jumpToFriendAdd(String touid, String photo, String nickName) {
        ArouterUtils.build(ArouterConst.PAGE_MSG_ADDFRIEND)
                .withString("touid", touid)
                .withString("photo", photo)
                .withString("nickName", nickName)
                .navigation();
    }

    /**
     * 添加好友页
     */

    public static void jumpToFriendAddForResult(Activity mContext, String touid, String photo, String nickName, boolean isAddNoGift) {
        ArouterUtils.build(ArouterConst.PAGE_MSG_ADDFRIEND)
                .withString("touid", touid)
                .withString("photo", photo)
                .withString("nickName", nickName)
                .withBoolean("isAddNoGift", isAddNoGift)
                .navigation(mContext, Constant.RequestCode.RQ_addFriend);
    }

    /**
     * 守护榜
     */
    public static void jumpToGuard(Context mContext, String userid) {
        ArouterUtils.build(ArouterConst.PAGE_ME_MINE_GUARD)
                .withString("userid", userid)
                .navigation(mContext);
    }




    /**
     * 举报
     *
     * @param uid
     */
    public static void jumpToReport(String uid) {
        ArouterUtils.build(ArouterConst.PAGE_REPORT)
                .withString("uid", uid)
                .navigation();
    }

}
