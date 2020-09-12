package com.deepsea.mua.stub.network;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.stub.utils.ApkUtils;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by JUN on 2019/7/4
 */
public class HttpHeaderInterceptor {

    public static Interceptor headerInterceptor() {
        return chain -> {
            Context context = AppUtils.getApp().getApplicationContext();
            Request build = chain.request().newBuilder()
                    .addHeader("xy-platform", "Android," + Build.VERSION.RELEASE)
                    .addHeader("xy-channel", ApkUtils.getChannelName(context))
                    .addHeader("xy-device", Build.MODEL)
                    .addHeader("xy-id", context.getPackageName())
                    .addHeader("xy-version", ApkUtils.getApkVersionName(context) + "," + ApkUtils.getApkVersionCode(context))
                    .build();
            return chain.proceed(build);
        };
    }
}
