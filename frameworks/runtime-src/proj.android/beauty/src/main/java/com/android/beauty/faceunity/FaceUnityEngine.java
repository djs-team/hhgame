package com.android.beauty.faceunity;

import android.app.Application;
import android.content.Context;


/**
 * Created by JUN on 2019/9/12
 */
public class FaceUnityEngine {

    private static volatile FaceUnityEngine sEngine;


    private Context mContext;

    public static FaceUnityEngine create() {
        if (sEngine == null) {
            synchronized (FaceUnityEngine.class) {
                if (sEngine == null) {
                    sEngine = new FaceUnityEngine();
                }
            }
        }

        return sEngine;
    }


    public void register(Application app) {
        this.mContext = app.getApplicationContext();
        FURenderer.initFURenderer(mContext);
    }


    public Context getContext() {
        return mContext;
    }



}
