package com.deepsea.mua.stub.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.core.utils.LogUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JUN on 2018/12/19
 */
public class ActivityCache implements IActivityCache, Application.ActivityLifecycleCallbacks {

    private static final String TAG = "ActivityCache";
    private static volatile IActivityCache mInstance = null;

    public static IActivityCache getInstance() {
        if (mInstance == null) {
            synchronized (ActivityCache.class) {
                if (mInstance == null) {
                    mInstance = new ActivityCache();
                }
            }
        }
        return mInstance;
    }

    private Activity topActivity;
    private LinkedHashMap<Class<? extends Activity>, Activity> activityMap = new LinkedHashMap<>();
    private List<IActivityLifecycle> mActivityLifecycles = new ArrayList<>();

    @Override
    public void init(Application app) {
        app.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public Activity getTopActivity() {
        return topActivity;
    }

    @Override
    public LinkedHashMap<Class<? extends Activity>, Activity> getActivityMap() {
        return activityMap;
    }

    @Override
    public <T> T getActivity(Class<T> activityClass) {
        return (T) activityMap.get(activityClass);
    }

    @Override
    public void finishAll() {
        try {
            for (Map.Entry<Class<? extends Activity>, Activity> key : activityMap.entrySet()) {
                Activity act = key.getValue();
                if (act != null) {
                    Log.d("finishAll", act.getLocalClassName());
                    if (!act.getLocalClassName().equals("org.cocos2dx.javascript.AppActivity")) {
                        act.finish();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish(Class<? extends Activity> clazz) {
        if (activityMap.containsKey(clazz)) {
            Activity activity = activityMap.get(clazz);
            activity.finish();
        }
    }

    @Override
    public void addActivityLifecycle(IActivityLifecycle lifecycle) {
        if (!mActivityLifecycles.contains(lifecycle)) {
            mActivityLifecycles.add(lifecycle);
        }
    }

    @Override
    public void removeActivityLifecycle(IActivityLifecycle lifecycle) {
        mActivityLifecycles.remove(lifecycle);
    }

    @Override
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) AppUtils.getApp().getSystemService(Application.ACTIVITY_SERVICE);
        String packageName = AppUtils.getApp().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager.getRunningAppProcesses();
        if (processInfos == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.processName.equals(packageName)
                    && info.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        getActivityMap().put(activity.getClass(), activity);
        for (IActivityLifecycle lifecycle : mActivityLifecycles) {
            lifecycle.onActivityCreated(activity, savedInstanceState);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        for (IActivityLifecycle lifecycle : mActivityLifecycles) {
            lifecycle.onActivityStarted(activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LogUtils.e(activity);
        topActivity = activity;
        for (IActivityLifecycle lifecycle : mActivityLifecycles) {
            lifecycle.onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        for (IActivityLifecycle lifecycle : mActivityLifecycles) {
            lifecycle.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        LogUtils.e(activity);
        for (IActivityLifecycle lifecycle : mActivityLifecycles) {
            lifecycle.onActivityStopped(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        for (IActivityLifecycle lifecycle : mActivityLifecycles) {
            lifecycle.onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtils.e(activity);
        getActivityMap().remove(activity.getClass());
        for (IActivityLifecycle lifecycle : mActivityLifecycles) {
            lifecycle.onActivityDestroyed(activity);
        }
    }
}
