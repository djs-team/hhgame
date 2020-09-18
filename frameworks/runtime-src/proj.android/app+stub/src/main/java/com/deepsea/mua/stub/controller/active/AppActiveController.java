package com.deepsea.mua.stub.controller.active;

import android.app.Activity;

import com.deepsea.mua.stub.app.ActivityCache;
import com.deepsea.mua.stub.app.IActivityLifecycle;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/7/17
 */
public class AppActiveController extends IActivityLifecycle {

    //app进入后台
    private boolean isAppOnBackground;

    private OnAppActiveListener mListener;

    @Inject
    public AppActiveController() {
        addActivityLifecycle();
    }

    public void setOnAppActiveListener(OnAppActiveListener listener) {
        this.mListener = listener;
    }

    private void addActivityLifecycle() {
        ActivityCache.getInstance().addActivityLifecycle(this);
    }

    public void removeActivityLifecycle() {
        ActivityCache.getInstance().removeActivityLifecycle(this);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isAppOnBackground) {
            isAppOnBackground = false;

            if (mListener != null) {
                mListener.onAppJoinForeground(activity);
            }
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (!ActivityCache.getInstance().isAppOnForeground()) {
            isAppOnBackground = true;

            if (mListener != null) {
                mListener.onAppJoinBackground();
            }
        }
    }
}
