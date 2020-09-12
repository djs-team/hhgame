package com.deepsea.mua.stub.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by JUN on 2019/6/28
 */
public class ApkUtils {

    public static String getHandSetInfo(Context con) {
        String handSetInfo =
                /**
                 "手机型号:" + android.os.Build.MODEL +
                 ",SDK版本:" + android.os.Build.VERSION.SDK +
                 **/
                ",系统版本:" + android.os.Build.VERSION.RELEASE +
                        ",软件版本:" + getApkVersionName(con);
        return handSetInfo;
    }

    /**
     * 获取当前版本名
     */
    public static String getApkVersionName(Context con) {
        try {

            PackageInfo packageInfo = con.getApplicationContext().getPackageManager().getPackageInfo(con.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前版本号
     */
    public static int getApkVersionCode(Context con) {
        try {
            PackageInfo packageInfo = con.getApplicationContext().getPackageManager().getPackageInfo(con.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取渠道名
     *
     * @param context
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Context context) {
//        if (context == null) {
//            return null;
//        }
        String channelCode = SharedPrefrencesUtil.getData(context, "channelCode", "channelCode", "");
        if (!TextUtils.isEmpty(channelCode)) {
            return channelCode;
        }else {
            return "public";
        }
//        String channelName = null;
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            if (packageManager != null) {
//                ApplicationInfo applicationInfo = packageManager.
//                        getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//                if (applicationInfo != null) {
//                    if (applicationInfo.metaData != null) {
//                        channelName = String.valueOf(applicationInfo.metaData.get("UMENG_CHANNEL"));
//                    }
//                }
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return channelName;
    }
}
