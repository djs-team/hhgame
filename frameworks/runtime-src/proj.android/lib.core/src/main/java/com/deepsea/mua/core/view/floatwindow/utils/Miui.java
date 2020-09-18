package com.deepsea.mua.core.view.floatwindow.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import com.deepsea.mua.core.view.floatwindow.impl.FloatLifecycleReceiver;
import com.deepsea.mua.core.view.floatwindow.interfaces.ResumedListener;
import com.deepsea.mua.core.view.floatwindow.permission.PermissionListener;
import com.deepsea.mua.core.view.floatwindow.permission.PermissionUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Copyright © 2017 Analysys Inc. All rights reserved.
 * @Description: <pre>
 *   需要清楚：一个MIUI版本对应小米各种机型，基于不同的安卓版本，但是权限设置页跟MIUI版本有关
 *   测试TYPE_TOAST类型：
 *   7.0：
 *   小米      5        MIUI8         -------------------- 失败
 *   小米   Note2       MIUI9         -------------------- 失败
 *   6.0.1
 *   小米   5                         -------------------- 失败
 *   小米   红米note3                  -------------------- 失败
 *   6.0：
 *   小米   5                         -------------------- 成功
 *   小米   红米4A      MIUI8         -------------------- 成功
 *   小米   红米Pro     MIUI7         -------------------- 成功
 *   小米   红米Note4   MIUI8         -------------------- 失败
 *  * <p/>
 *  * 经过各种横向纵向测试对比，得出一个结论，就是小米对TYPE_TOAST的处理机制毫无规律可言！
 *  * 跟Android版本无关，跟MIUI版本无关，addView方法也不报错
 *  * 所以最后对小米6.0以上的适配方法是：不使用 TYPE_TOAST 类型，统一申请权限
 *               </pre>
 * @Version: 1.0.9
 * @Create: 2017/12/30 17:11:30
 * @Author: yhao
 */
public class Miui {

    private static final String MIUI = "ro.MIUI.ui.version.name";
    private static final String MIUI5 = "V5";
    private static final String MIUI6 = "V6";
    private static final String MIUI7 = "V7";
    private static final String MIUI8 = "V8";
    private static final String MIUI9 = "V9";
    private static List<PermissionListener> mPermissionListenerList;
    private static PermissionListener mPermissionListener;

    public static boolean rom() {
        L.d(" Miui  : " + Miui.getProp());
        return "Xiaomi".equals(Build.MANUFACTURER);
    }

    private static String getProp() {
        return Rom.getProp(MIUI);
    }

    /**
     * Android6.0以下申请权限
     */
    public static void requestPermission(final Context context, PermissionListener permissionListener) {
        if (PermissionUtil.hasPermission(context)) {
            permissionListener.onSuccess();
            return;
        }
        if (mPermissionListenerList == null) {
            mPermissionListenerList = new ArrayList<PermissionListener>();
            mPermissionListener = new PermissionListener() {
                @Override
                public void onSuccess() {
                    for (PermissionListener listener : mPermissionListenerList) {
                        listener.onSuccess();
                    }
                    mPermissionListenerList.clear();
                }

                @Override
                public void onFail() {
                    for (PermissionListener listener : mPermissionListenerList) {
                        listener.onFail();
                    }
                    mPermissionListenerList.clear();
                }
            };
            requestPermission(context);
        }
        mPermissionListenerList.add(permissionListener);
    }

    private static void requestPermission(final Context context) {
        String prop = getProp();
        if (MIUI5.equals(prop)) {
            reqForMiui5(context);
        } else if (MIUI6.equals(prop) || MIUI7.equals(prop)) {
            reqForMiui67(context);
        } else if (MIUI8.equals(prop) || MIUI9.equals(prop)) {
            reqForMiui89(context);
        }
        FloatLifecycleReceiver.setResumedListener(new ResumedListener() {
            @Override
            public void onResumed() {
                if (PermissionUtil.hasPermission(context)) {
                    mPermissionListener.onSuccess();
                } else {
                    mPermissionListener.onFail();
                }
            }
        });
    }

    private static void reqForMiui5(Context context) {
        String packageName = context.getPackageName();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Rom.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            L.e("intent is not available!");
        }
    }

    private static void reqForMiui67(Context context) {
        Intent intent = new Intent("MIUI.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.MIUI.securitycenter", "com.MIUI.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Rom.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            L.e("intent is not available!");
        }
    }

    private static void reqForMiui89(Context context) {
        Intent intent = new Intent("MIUI.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.MIUI.securitycenter", "com.MIUI.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Rom.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent = new Intent("MIUI.intent.action.APP_PERM_EDITOR");
            intent.setPackage("com.MIUI.securitycenter");
            intent.putExtra("extra_pkgname", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Rom.isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                L.e("intent is not available!");
            }
        }
    }

    /**
     * 有些机型在添加TYPE-TOAST类型时会自动改为TYPE_SYSTEM_ALERT，通过此方法可以屏蔽修改 但是...即使成功显示出悬浮窗，移动的话也会崩溃
     */
    @SuppressWarnings("unused")
    private static void addViewToWindow(WindowManager wm, View view, WindowManager.LayoutParams params) {
        setMiUIInternational(true);
        wm.addView(view, params);
        setMiUIInternational(false);
    }

    private static void setMiUIInternational(boolean flag) {
        try {
            Class<?> buildClazz = Class.forName("MIUI.os.Build");
            Field isInternational = buildClazz.getDeclaredField("IS_INTERNATIONAL_BUILD");
            isInternational.setAccessible(true);
            isInternational.setBoolean(null, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
