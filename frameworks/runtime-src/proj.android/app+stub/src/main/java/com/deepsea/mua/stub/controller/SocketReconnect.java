package com.deepsea.mua.stub.controller;

import android.util.Log;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.network.HttpHelper;
import com.deepsea.mua.stub.utils.AppManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2019/7/1
 * 房间内掉线重连
 */
public class SocketReconnect {

    private String mRoomId;

    private boolean stop;

    public void reconnect(String roomId) {
        this.mRoomId = roomId;
        getSocketUrl();
    }

    public void getSocketUrl() {
        showLog("获取socket地址");
        if (!AppManager.getAppManager().currentActivity().getComponentName().getClassName().equals("com.deepsea.mua.voice.activity.RoomActivity")) {
            stop = false;
        }
        //获取wsurl
        Call<String> call = HttpHelper.instance().getApi(RetrofitApi.class).getWsUrl(AddressCenter.getAddress().getSocketUrl());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    if (!stop) {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
                        showLog("获取socket地址成功 " + response.body());
                        RoomController.getInstance().startConnect(response.body(), mRoomId);
                    }
                } else {
                    try {
                        if (!stop) {
                            if (response.errorBody() != null) {
                                showLog("获取socket地址失败 " + response.errorBody().string());
                            }
                            getSocketUrl();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (!stop) {
                    showLog("获取socket地址失败 " + t.getMessage());
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    getSocketUrl();

                }
            }
        });
    }

    private Subscription mSubscription;

    //    private void startReconectObsever() {
//            mSubscription = Observable.interval(0, 3000, TimeUnit.MILLISECONDS)
//                    .subscribeOn(Schedulers.computation())
//                    .subscribe(aLong -> {getSocketUrl();});
//
//
//    }
//    public void stopObservable() {
//        if (mSubscription != null && (!mSubscription.isUnsubscribed())) {
//            mSubscription.unsubscribe();
//        }
//    }
    public void stopConnect() {
        this.stop = true;
    }

    private void showLog(String message) {
        Log.d("mua", "掉线重连 " + message);
    }
}
