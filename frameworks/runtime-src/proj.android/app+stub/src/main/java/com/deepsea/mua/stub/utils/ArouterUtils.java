package com.deepsea.mua.stub.utils;

import android.app.Application;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by JUN on 2019/4/14
 */
public class ArouterUtils {

    public static void init(Application application) {
        ARouter.init(application);
    }

    public static synchronized void openLog() {
        ARouter.openLog();
        ARouter.openDebug();
    }

    public static void navigation(String path) {
        ARouter.getInstance().build(path)
                .navigation();
    }

    public static Postcard build(String path) {
        return ARouter.getInstance().build(path);
    }
}
