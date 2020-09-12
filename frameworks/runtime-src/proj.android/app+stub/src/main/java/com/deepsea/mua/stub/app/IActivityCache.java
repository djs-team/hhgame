package com.deepsea.mua.stub.app;

import android.app.Activity;
import android.app.Application;

import java.util.LinkedHashMap;

/**
 * Created by JUN on 2018/12/19
 */
public interface IActivityCache {

    void init(Application app);

    /**
     * 获取栈顶Activity
     *
     * @return
     */
    Activity getTopActivity();

    /**
     * 获取Activity队列
     *
     * @return
     */
    LinkedHashMap<Class<? extends Activity>, Activity> getActivityMap();

    /**
     * 获取指定的Activity
     *
     * @param activityClass
     * @param <T>
     * @return
     */
    <T> T getActivity(Class<T> activityClass);

    /**
     * 关闭所有Activity
     */
    void finishAll();

    /**
     * 关闭指定的Activity
     *
     * @param clazz
     */
    void finish(Class<? extends Activity> clazz);

    /**
     * 添加Activity生命周期监听
     *
     * @param lifecycle
     */
    void addActivityLifecycle(IActivityLifecycle lifecycle);

    /**
     * 移除Activity生命周期监听
     *
     * @param lifecycle
     */
    void removeActivityLifecycle(IActivityLifecycle lifecycle);

    /**
     * App是否在前台
     *
     * @return
     */
    boolean isAppOnForeground();
}
