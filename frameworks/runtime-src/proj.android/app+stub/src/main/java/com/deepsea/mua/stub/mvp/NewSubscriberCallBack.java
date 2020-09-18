package com.deepsea.mua.stub.mvp;

import android.content.Context;
import android.content.Intent;

import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.core.utils.LogUtils;
import com.deepsea.mua.stub.network.ResultValid;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ArouterUtils;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * author :  liyaxing
 * date   : 2019/5/9 9:43
 * desc   :
 */
public abstract class NewSubscriberCallBack<T> extends Subscriber<ResponseModel<T>> {

    @Override
    public void onNext(ResponseModel<T> response) {
        LogUtils.i("NewSubscriberCallBack--" + response.toString());
        if (response.code == 5000 || response.code == 5001 || response.code == 5002) {
            Context context = AppUtils.getApp().getApplicationContext();
            Intent intent = new Intent();
            intent.setAction(ResultValid.ACTION_TOKEN_EXPIRED);
            context.sendBroadcast(intent);

        }
        if (response.code == 3000) {
            ArouterUtils.build(ArouterConst.PAGE_UPDATE)
                    .withString("url", response.apk_url)
                    .navigation();
        }
        if (response.code == 200) {
            onSuccess(response.data);
        } else {
            onError(response.code, response.desc);
        }
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            onError(0, "服务器错误，请检查！");
        } else if (e instanceof JsonParseException || e instanceof JSONException
                || e instanceof ParseException) {
            onError(0, "解析错误！");
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException
                || e instanceof UnknownHostException) {
            onError(0, "网络连接异常，请检查网络！");
        } else {
            onError(0, e.getMessage());
        }

    }

    protected abstract void onSuccess(T response);

    protected abstract void onError(int errorCode, String errorMsg);


}
