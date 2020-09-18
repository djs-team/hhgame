package com.deepsea.mua.stub.controller.active;

import android.app.Activity;

/**
 * Created by JUN on 2019/7/17
 */
public interface OnAppActiveListener {

    /**
     * app进入后台
     */
    void onAppJoinBackground();

    /**
     * app进入前台
     *
     * @param activity
     */
    void onAppJoinForeground(Activity activity);
}
