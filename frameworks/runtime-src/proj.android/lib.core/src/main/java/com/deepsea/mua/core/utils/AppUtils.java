package com.deepsea.mua.core.utils;

import android.app.Application;

/**
 * Created by JUN on 2019/3/20
 */
public class AppUtils {

    private static Application application = null;

    public static void init(Application app) {
        if (application == null) {
            application = app;
        }
    }

    public static Application getApp() {
        if (application == null) {
            throw new RuntimeException("please invoke ApplicationUtils.init(app) on Application#onCreate()"
                    + " and register your Application in manifest.");
        }
        return (Application) application.getApplicationContext();
    }
}
