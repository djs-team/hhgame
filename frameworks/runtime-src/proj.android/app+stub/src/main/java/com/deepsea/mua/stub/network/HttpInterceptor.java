package com.deepsea.mua.stub.network;

import android.os.Build;

import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.utils.UserUtils;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;

/**
 * Created by JUN on 2019/3/22
 */
public class HttpInterceptor {

    /**
     * 拦截器Interceptors
     * 统一的请求参数
     */
    public static Interceptor httpInterceptor() {
        return chain -> {
            String token = "";
            User user = UserUtils.getUser();
            if (user != null && user.getToken() != null) {
                token = user.getToken();
            }

            Request request = chain.request();
            if (request.method().equals("POST")) {

                if (request.body() instanceof FormBody) {
                    FormBody.Builder bodyBuilder = new FormBody.Builder();
                    FormBody formBody = (FormBody) request.body();
                    for (int i = 0; i < formBody.size(); i++) {
                        bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                    }
                    formBody = bodyBuilder
                            .addEncoded("token", token)
                            .addEncoded("device", Build.MODEL)
                            .addEncoded("version", Build.VERSION.RELEASE)
                            .build();
                    request = request.newBuilder().post(formBody).build();

                } else if (request.body() instanceof MultipartBody) {

                    MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    MultipartBody formBody = (MultipartBody) request.body();
                    for (int i = 0; i < formBody.size(); i++) {
                        bodyBuilder.addPart(formBody.part(i));
                    }
                    formBody = bodyBuilder
                            .addFormDataPart("token", token)
                            .addFormDataPart("device", Build.MODEL)
                            .addFormDataPart("version", Build.VERSION.RELEASE)
                            .build();
                    request = request.newBuilder().post(formBody).build();

                } else {
                    FormBody.Builder bodyBuilder = new FormBody.Builder();
                    FormBody formBody = bodyBuilder
                            .addEncoded("token", token)
                            .addEncoded("device", Build.MODEL)
                            .addEncoded("version", Build.VERSION.RELEASE)
                            .build();
                    request = request.newBuilder().post(formBody).build();
                }
            } else {
                HttpUrl httpUrl = request.url().newBuilder()
                        .addQueryParameter("token", token)
                        .addQueryParameter("device", Build.MODEL)
                        .addQueryParameter("version", Build.VERSION.RELEASE)
                        .build();
                request = request.newBuilder().url(httpUrl).build();
            }
            return chain.proceed(request);
        };
    }
}