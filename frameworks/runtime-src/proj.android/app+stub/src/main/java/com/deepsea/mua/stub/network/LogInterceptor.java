package com.deepsea.mua.stub.network;

import android.util.Log;

import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by JUN on 2019/3/22
 */
public class LogInterceptor {

//    private static final String TAG = "okhttp";

    public static Interceptor logInterceptor() {
        return chain -> {
            Request request = chain.request();
//            Log.e(TAG, "request:" + request.toString());
            long t1 = System.nanoTime();
            okhttp3.Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
//            Log.e(TAG, String.format(Locale.getDefault(), "Received response for %s in %.1fms%n%s",
//                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
//            Log.e(TAG, "response body:" + content);
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, content))
                    .build();

        };
    }
}
