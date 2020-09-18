package com.deepsea.mua.stub.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.app.ActivityCache;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.client.hyphenate.HyphenateClient;
import com.deepsea.mua.stub.controller.OnlineController;
import com.deepsea.mua.stub.controller.RoomMiniController;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by JUN on 2019/5/8
 */
public class LogoutUtils {

    /**
     * 退出登录，跳转登录页
     */
    public static void logout(Context mContext) {
        try {
            RoomMiniController.getInstance().destroy();
            Activity activity = ActivityCache.getInstance().getTopActivity();
            UserUtils.clearUser();
            MobclickAgent.onProfileSignOff();
            HyphenateClient.getInstance().logout(true, null);
            AgoraClient.create().clearAgoraEventHandler();
            WsocketManager.create().clearWsocketListener();
            WsocketManager.create().stopConnect();
            OnlineController.getInstance(mContext).stopObservable();
            Intent intent = new Intent();
            intent.setClassName(activity, "com.deepsea.mua.ui.login.activity.LoginMainActivity");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            AppConstant.getInstance().setLoginOut(true);
            activity.startActivity(intent);
            activity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
