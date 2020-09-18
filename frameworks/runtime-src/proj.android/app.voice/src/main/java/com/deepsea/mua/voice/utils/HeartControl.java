package com.deepsea.mua.voice.utils;

import android.content.Context;
import android.util.Log;

import com.deepsea.mua.stub.utils.SongStateUtils;
import com.deepsea.mua.voice.viewmodel.RoomViewModel;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class HeartControl {
    private static final int PING_INTERVAL = 3 * 1000; //3s

    private HeartControl() {
    }

    private static Context context;

    public static HeartControl getInstance(Context mContext) {
        context = mContext;
        return OnlineControllerHolder.sInstance;
    }

    private static class OnlineControllerHolder {
        private static final HeartControl sInstance = new HeartControl();
    }

    private Subscription mHeartSubscription;
    private final int heartPadding = 10 * 1000;

    public interface CallBack {
        void onResult();
    }

    public void startHeartBeatObservable(RoomViewModel mViewModel, CallBack callback) {
        mHeartSubscription = Observable.interval(0, heartPadding, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(aLong -> {
                            int count = SongStateUtils.getSingleton2().getHeartCount();
                            Log.d("heart", "count:" + count);
                            if (count <= 12) {
                                SongStateUtils.getSingleton2().setHeartCount(count + 1);
                                mViewModel.toSocketHeartBeat();
                            } else {
                                callback.onResult();
                            }
                        }
                );
    }


    public void stopHeartBeatObservable() {
        if (mHeartSubscription != null && (!mHeartSubscription.isUnsubscribed())) {
            SongStateUtils.getSingleton2().setHeartCount(0);
            mHeartSubscription.unsubscribe();
        }
    }
}
