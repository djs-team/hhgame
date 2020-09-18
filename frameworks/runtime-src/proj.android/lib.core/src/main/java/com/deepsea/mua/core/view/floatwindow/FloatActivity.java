package com.deepsea.mua.core.view.floatwindow;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import com.deepsea.mua.core.utils.RomUtils;
import com.deepsea.mua.core.view.floatwindow.permission.PermissionListener;
import com.deepsea.mua.core.view.floatwindow.permission.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @Copyright © 2019 Analysys Inc. All rights reserved.
 * @Description: 用于在内部自动申请权限
 * @Version: 1.0.9
 * @Create: 2017/01/22 17:08:22
 * @Author: yhao
 */
public class FloatActivity extends FragmentActivity {

    private static List<PermissionListener> mPermissionListenerList;
    private static PermissionListener mPermissionListener;

    private static boolean mNoListener;

    public static synchronized void request(Context context, PermissionListener permissionListener) {
        if (PermissionUtil.hasPermission(context)) {
            if (permissionListener != null) {
                permissionListener.onSuccess();
            }
            return;
        }
        if (mPermissionListenerList == null) {
            mPermissionListenerList = new ArrayList<>();
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
            Intent intent = new Intent(context, FloatActivity.class);
            context.startActivity(intent);
        }
        if (permissionListener != null) {
            mPermissionListenerList.add(permissionListener);
        }
    }

    public static synchronized void request(PermissionListener permissionListener, Context context) {
        mNoListener = true;
        mPermissionListener = permissionListener;
        Intent intent = new Intent(context, FloatActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mNoListener) {
            RomUtils.applyPermission(this, () -> {
                if (!RomUtils.checkFloatWindowPermission(this)) {
                    // 授权失败
                    if (mPermissionListener != null) {
                        mPermissionListener.onFail();
                    }
                } else {
                    //授权成功
                    if (mPermissionListener != null) {
                        mPermissionListener.onSuccess();
                    }
                }
            });
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                requestAlertWindowPermission();
            }
        }
    }

    @TargetApi(23)
    private void requestAlertWindowPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RomUtils.onActivityResult(this, requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (PermissionUtil.hasPermissionOnActivityResult(this)) {
                mPermissionListener.onSuccess();
            } else {
                mPermissionListener.onFail();
            }
        }

        finish();
    }

}
