package com.deepsea.mua.stub.client.app;

import android.content.Context;

import com.deepsea.mua.stub.utils.Function;


/**
 * Created by JUN on 2019/8/19
 */
public abstract class IAppClient {

    protected int mAppStatus = NONE;

    public static final int LOGIN = 0x50;

    public static final int LOGOUT = 0x51;

    public static final int RUNNING = 0x52;

    public static final int NONE = 0x53;

    /**
     * 初始化
     *
     * @param context
     */
    public abstract void init(Context context);

    /**
     * 释放资源
     */
    public abstract void release();

    /**
     * 获取app状态
     *
     * @return
     */
    public int getAppStatus() {
        return mAppStatus;
    }

    /**
     * 修改app状态
     *
     * @param appStatus
     */
    public void setAppStatus(int appStatus) {
        mAppStatus = appStatus;
    }

    /**
     * app 是否处于 RUNNING 状态
     *
     * @return
     */
    public abstract boolean isRunning();

    /**
     * 登录成功
     *
     * @param uid
     */
    public abstract void login(String uid);

    /**
     * 退出
     */
    public abstract void logout(Function function);
}
