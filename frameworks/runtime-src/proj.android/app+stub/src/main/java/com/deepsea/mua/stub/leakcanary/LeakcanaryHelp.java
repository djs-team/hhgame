//package com.deepsea.mua.stub.leakcanary;
//
//import android.app.Application;
//
//import com.squareup.leakcanary.LeakCanary;
//import com.squareup.leakcanary.RefWatcher;
//
///**
// * Created by JUN on 2019/8/8
// */
//public class LeakcanaryHelp {
//
//    private RefWatcher mRefWatcher;
//
//    private static volatile LeakcanaryHelp mInstance;
//
//    private LeakcanaryHelp() {
//    }
//
//    public static LeakcanaryHelp create() {
//        if (mInstance == null) {
//            synchronized (LeakcanaryHelp.class) {
//                if (mInstance == null) {
//                    mInstance = new LeakcanaryHelp();
//                }
//            }
//        }
//
//        return mInstance;
//    }
//
//    public void setupLeakCanary(Application application) {
//        if (LeakCanary.isInAnalyzerProcess(application)) {
//            mRefWatcher = RefWatcher.DISABLED;
//        }
//        mRefWatcher = LeakCanary.install(application);
//    }
//
//    public RefWatcher getRefWatcher() {
//        return mRefWatcher;
//    }
//}
