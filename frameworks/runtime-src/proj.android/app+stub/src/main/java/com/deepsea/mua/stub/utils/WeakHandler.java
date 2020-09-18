package com.deepsea.mua.stub.utils;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by JUN on 2019/7/20
 */
public abstract class WeakHandler<T> extends Handler {

    private WeakReference<T> mTReference;

    public WeakHandler(T t) {
        mTReference = new WeakReference<T>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        T t = mTReference.get();
        if (t != null) {
            handleMessage(t, msg);
            return;
        }
        super.handleMessage(msg);
    }

    protected abstract void handleMessage(T object, Message msg);
}
