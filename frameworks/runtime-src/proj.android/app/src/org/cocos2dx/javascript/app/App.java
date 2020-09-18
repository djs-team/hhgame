package org.cocos2dx.javascript.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.android.beauty.faceunity.FaceUnityEngine;
import com.deepsea.mua.advertisement.TTAdManagerHolder;
import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.kit.app.MuaEngine;
import com.deepsea.mua.stub.app.ActivityCache;
import com.deepsea.mua.stub.controller.RoomMiniController;
import com.deepsea.mua.stub.event.EventController;
import com.deepsea.mua.stub.utils.SPUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.fm.openinstall.OpenInstall;

import org.cocos2dx.javascript.di.AppInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class App extends MultiDexApplication implements HasActivityInjector {
    private static App app;
    public static String START_TIME_KEY = "start_time_key";

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    AppExecutors mExecutors;
    private static Context mContext;

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mContext = getApplicationContext();
        AppInjector.init(app);
        MuaEngine.openLog();
        MuaEngine.create().register(this);
        SPUtils.put(START_TIME_KEY, System.currentTimeMillis());
        if (isMainProcess()) {
            OpenInstall.init(this);
        }
        FaceUnityEngine.create().register(this);
        TTAdManagerHolder.init(this);//穿山甲

    }

    public static App getApp() {
        return app;
    }

    public static Context getContext() {
        return mContext;
    }

    public void exitApp() {
        ActivityCache.getInstance().finishAll();
        RoomMiniController.getInstance().destroy();
        UserUtils.releaseUser();
        EventController.getEventController().release();
    }
    public boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }
}
