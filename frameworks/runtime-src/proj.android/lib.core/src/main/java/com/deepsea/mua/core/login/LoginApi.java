package com.deepsea.mua.core.login;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JUN on 2018/9/28
 */
public class LoginApi implements Handler.Callback {

    private static final String TAG = "LoginApi";

    private static final int MSG_AUTH_CANCEL = 1;
    private static final int MSG_AUTH_ERROR = 2;
    private static final int MSG_AUTH_COMPLETE = 3;

    private String platform;
    private Activity mContext;
    private Handler handler;

    private OnLoginListener listener;

    public LoginApi() {
        handler = new Handler(Looper.getMainLooper(), this);
    }

    public void setOnLoginListener(OnLoginListener listener) {
        this.listener = listener;
    }

    public void setPlatform(String platform, Activity mContext) {
        this.platform = platform;
        this.mContext = mContext;

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_AUTH_CANCEL: {
                if (listener != null) {
                    listener.onCancel();
                }
            }
            break;
            case MSG_AUTH_ERROR: {
                Throwable t = (Throwable) msg.obj;
                if (listener != null) {
                    listener.onError(t.getMessage());
                }
            }
            break;
            case MSG_AUTH_COMPLETE: {
                ApiUser apiUser = (ApiUser) msg.obj;
                if (listener != null) {
                    listener.onLogin(apiUser);
                }
            }
            break;
        }
        return false;
    }

    public void login() {
        new Thread(this::loginFunc).start();
    }

    private void loginFunc() {
        if (TextUtils.isEmpty(platform)) {
            return;
        }
        UMShareAPI shareAPI = UMShareAPI.get(mContext);
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        shareAPI.setShareConfig(config);
        shareAPI.getPlatformInfo(mContext, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                platformComplete(share_media, map);
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Message msg = new Message();
                msg.what = MSG_AUTH_ERROR;
                msg.arg2 = i;
                msg.obj = throwable;
                handler.sendMessage(msg);
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Message msg = new Message();
                msg.what = MSG_AUTH_CANCEL;
                msg.arg2 = i;
                msg.obj = share_media.getName();
                handler.sendMessage(msg);
            }
        });

    }

    private void platformComplete(SHARE_MEDIA share_media, Map<String, String> res) {
        Message msg = new Message();
        msg.what = MSG_AUTH_COMPLETE;

        ApiUser apiUser = new ApiUser();
        apiUser.setPlatform(share_media.getName());
        apiUser.setOpenId(res.get("uid"));

        apiUser.setUserName( res.get("name"));
        apiUser.setUserIcon(res.get("iconurl"));
        apiUser.setSex(res.get("gender"));
        apiUser.setRes(res);

        msg.obj = apiUser;
        handler.sendMessage(msg);
    }
}
