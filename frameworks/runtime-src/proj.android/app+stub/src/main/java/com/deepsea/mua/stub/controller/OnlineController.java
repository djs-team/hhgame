package com.deepsea.mua.stub.controller;

import android.content.Context;
import android.util.Log;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.entity.HeartBeatBean;
import com.deepsea.mua.stub.entity.MaqueeBean;
import com.deepsea.mua.stub.mvp.NewSubscriberCallBack;
import com.deepsea.mua.stub.network.HttpHelper;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.AppUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.eventbus.HeartBeatEvent;
import com.deepsea.mua.stub.utils.eventbus.InviteDialogEvent;
import com.deepsea.mua.stub.utils.eventbus.MarqueeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2019/10/15
 * 用户在线状态更新
 */
public class OnlineController {


    private static final int PING_INTERVAL = 3 * 1000; //3s

    private OnlineController() {
    }

    private static Context context;

    public static OnlineController getInstance(Context mContext) {
        context = mContext;
        return OnlineControllerHolder.sInstance;
    }

    private static class OnlineControllerHolder {
        private static final OnlineController sInstance = new OnlineController();
    }

    private int isRequest;

    public int getIsRequest() {
        return isRequest;
    }

    private Subscription mSubscription;

    public void startObservable() {
        mSubscription = Observable.interval(0, PING_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(aLong -> sendOnlinePing());
    }

    public void stopObservable() {
        if (mSubscription != null && (!mSubscription.isUnsubscribed())) {
            mSubscription.unsubscribe();
        }
    }

    private Subscription mMarqueeSubscription;

    public void startMarqueeObservable() {
        mMarqueeSubscription = Observable.interval(0, 60 * 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(aLong -> sendMarqueePing());
    }

    public void stopMarqueeObservable() {
        if (mMarqueeSubscription != null && (!mMarqueeSubscription.isUnsubscribed())) {
            mMarqueeSubscription.unsubscribe();
        }
    }

    private void sendMarqueePing() {
        HttpHelper.instance().getApi(RetrofitApi.class).horseLamp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewSubscriberCallBack<MaqueeBean>() {
                    @Override
                    protected void onSuccess(MaqueeBean response) {
                        StringBuilder builder = new StringBuilder();
                        if (response != null) {
                            List<String> lamp = response.getLamp();
                            if (lamp != null && lamp.size() > 0) {
                                for (String s : lamp) {
                                    builder.append("        ");
                                    builder.append(s);
                                    builder.append("                      ");
                                }
                            }
                        }
                        AppConstant.getInstance().setMarquee(builder.toString());

                        EventBus.getDefault().post(new MarqueeEvent(builder.toString()));
                    }

                    @Override
                    protected void onError(int errorCode, String errorMsg) {

                    }
                });
    }

    //1069197
    private void sendOnlinePing() {
        HttpHelper.instance().getApi(RetrofitApi.class).addMember()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewSubscriberCallBack<HeartBeatBean>() {
                    @Override
                    protected void onError(int errorCode, String errorMsg) {

                    }

                    @Override
                    protected void onSuccess(HeartBeatBean response) {
                        boolean isBack = AppUtils.isRunBackground(context);
                        if (isBack) {
                            RoomController.getInstance().setRetry(true);
                        }
                        if (response != null) {
                            //是否有好友请求
                            isRequest = response.getIsRequest();
                            EventBus.getDefault().post(new HeartBeatEvent(isRequest));
                            //邀请弹框
                            List<HeartBeatBean.InviteListBean> inviteListBeans = response.getList();
                            if (response.getList() != null && response.getList().size() > 0) {
                                EventBus.getDefault().post(new InviteDialogEvent(inviteListBeans));
                            }
                        }
                    }
                });
    }
}
