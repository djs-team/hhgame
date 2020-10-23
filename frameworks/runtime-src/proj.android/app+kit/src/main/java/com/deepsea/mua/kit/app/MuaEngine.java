package com.deepsea.mua.kit.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.http.HttpResponseCache;

import com.cxz.networklib.NetworkManager;
import com.deepsea.mua.app.im.HxHelper;
import com.deepsea.mua.core.log.Logg;
import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.core.utils.ViewDataBindingComponent;
import com.deepsea.mua.core.wxpay.WxCons;
import com.deepsea.mua.faceliveness.helper.AuditHelper;
import com.deepsea.mua.kit.BuildConfig;
import com.deepsea.mua.stub.app.ActivityCache;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.client.app.AppClient;
import com.deepsea.mua.stub.controller.RoomMiniController;
import com.deepsea.mua.stub.event.EventController;
import com.deepsea.mua.stub.utils.ArouterUtils;
import com.deepsea.mua.stub.utils.UncaughtException;
import com.deepsea.mua.stub.utils.UserUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
//import com.umeng.socialize.PlatformConfig;

import java.io.File;
import java.io.IOException;

import cn.jiguang.verifysdk.api.JVerificationInterface;
import cn.jpush.android.api.JPushInterface;
import me.jessyan.autosize.AutoSize;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.autosize.onAdaptListener;
import me.jessyan.autosize.utils.ScreenUtils;

/**
 * Created by JUN on 2019/9/12
 */
public class MuaEngine {

    private static volatile MuaEngine sEngine;

    private AppExecutors mExecutors;

    private Context mContext;

    public static MuaEngine create() {
        if (sEngine == null) {
            synchronized (MuaEngine.class) {
                if (sEngine == null) {
                    sEngine = new MuaEngine();
                }
            }
        }

        return sEngine;
    }

    private MuaEngine() {
        mExecutors = new AppExecutors();
    }

    public void register(Application app) {
        this.mContext = app.getApplicationContext();
        AppUtils.init(app);

        // 支持在子进程中统计自定义事件
        UMConfigure.setProcessEvent(true);
        UMConfigure.init(app, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin(WxCons.APP_ID_WX,WxCons.APP_SECRET_WX);
        HxHelper.getInstance().init();
        AppClient.getInstance().init(app);
        AgoraClient.create().setUpAgora(app, BuildConfig.Agora_ID);
        ArouterUtils.init(app);
        AuditHelper.initialize(mContext);

        mExecutors.diskIO().execute(() -> {
            UncaughtException.getInstance().init();
            initAutoSize();
            setSVGAcache();
            ActivityCache.getInstance().init(app);
            DataBindingUtil.setDefaultComponent(new ViewDataBindingComponent());
        });
        JPushInterface.setDebugMode(true);
        JPushInterface.init(mContext);
        JVerificationInterface.init(mContext);
        NetworkManager.getDefault().init(app);
        CrashReport.initCrashReport(mContext, "ba56317fbb", true);


    }

    public Context getContext() {
        return mContext;
    }

    public static void openLog() {
        ArouterUtils.openLog();
        Logg.openLog();
        UncaughtException.getInstance().openLog();
        AutoSizeConfig.getInstance().setLog(true);
    }

    /**
     * 屏幕自动适配初始化
     */
    private void initAutoSize() {
        AutoSize.initCompatMultiProcess(mContext);
        AutoSizeConfig.getInstance()
                //是否屏蔽系统字体大小对 AndroidAutoSize 的影响
                .setExcludeFontScale(true)
                //屏幕适配监听器
                .setOnAdaptListener(new onAdaptListener() {
                    @Override
                    public void onAdaptBefore(Object target, Activity activity) {
                        AutoSizeConfig.getInstance().setScreenWidth(ScreenUtils.getScreenSize(activity)[0]);
                        AutoSizeConfig.getInstance().setScreenHeight(ScreenUtils.getScreenSize(activity)[1]);
                    }

                    @Override
                    public void onAdaptAfter(Object target, Activity activity) {
                    }
                });
    }

    /**
     * 设置SVGAPlayer缓存
     */
    private void setSVGAcache() {
        try {
            File cacheDir = new File(mContext.getCacheDir(), "http");
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitApp() {
        ActivityCache.getInstance().finishAll();
        RoomMiniController.getInstance().destroy();
        UserUtils.releaseUser();
        EventController.getEventController().release();
    }
}
