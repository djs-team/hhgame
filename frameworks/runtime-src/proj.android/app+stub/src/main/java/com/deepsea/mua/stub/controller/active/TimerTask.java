package com.deepsea.mua.stub.controller.active;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by JUN on 2019/7/17
 */
public class TimerTask {

    @Inject
    public TimerTask() {
    }

    private Disposable mSubscribe;

    private OnTimerListener mListener;

    public void setOnTimerListener(OnTimerListener listener) {
        this.mListener = listener;
    }

    public void startTimer(long count) {
        startTimer(count, 1, TimeUnit.SECONDS);
    }

    public void startTimer(long count, long period, TimeUnit unit) {
        if (count <= 0) {
            return;
        }

        stopTimer();

        mSubscribe = Observable.interval(0, period, unit)
                .take(count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (aLong + 1 == count) {
                        if (mListener != null) {
                            mListener.onTimerFinished();
                        }

                        if (!mSubscribe.isDisposed()) {
                            mSubscribe.dispose();
                        }
                    }
                });
    }

    public void stopTimer() {
        if (mSubscribe != null && !mSubscribe.isDisposed()) {
            mSubscribe.dispose();
        }
    }
}
