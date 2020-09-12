package com.deepsea.mua.stub.network;

import android.content.Context;
import android.content.Intent;

import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ArouterUtils;

import retrofit2.Response;

/**
 * Created by JUN on 2019/4/8
 */
public class ResultValid {

    private static final int CODE_SUCCESS = 200;
    private static final int CODE_ERROR = 500;
    private static final int CODE_UPDATE_APK = 3000;

    public static final String ACTION_TOKEN_EXPIRED = "com.deepsea.mua.token_expired";

    public static <R> boolean codeValid(Response<R> response) {
        R body = response.body();
        if (body instanceof BaseApiResult) {
            BaseApiResult result = (BaseApiResult) body;

            int code = result.getCode();
            tokenExpired(code);
            if (code == CODE_UPDATE_APK) {
                updateApp(result.getApk_url());
            }

            return code == CODE_SUCCESS;
        }
        return true;
    }

    public static <R> String message(Response<R> response) {
        R body = response.body();
        if (body instanceof BaseApiResult) {
            BaseApiResult result = (BaseApiResult) body;

            return result.getDesc();
        }
        return null;
    }

    public static <R> int code(Response<R> response) {
        R body = response.body();
        if (body instanceof BaseApiResult) {
            BaseApiResult result = (BaseApiResult) body;

            return result.getCode();
        }
        return CODE_ERROR;
    }

    /**
     * token过期
     *
     * @param code
     */
    private static void tokenExpired(int code) {
        if (code == 5000 || code == 5001 || code == 5002) {
            Context context = AppUtils.getApp().getApplicationContext();
            Intent intent = new Intent();
            intent.setAction(ACTION_TOKEN_EXPIRED);
            context.sendBroadcast(intent);
        }
    }

    private static void updateApp(String url) {
        ArouterUtils.build(ArouterConst.PAGE_UPDATE)
                .withString("url", url)
                .navigation();
    }
}
