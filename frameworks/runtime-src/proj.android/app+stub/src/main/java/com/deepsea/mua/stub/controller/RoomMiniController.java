package com.deepsea.mua.stub.controller;

import android.app.Activity;

import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.core.utils.RomUtils;
import com.deepsea.mua.core.view.floatwindow.FloatActivity;
import com.deepsea.mua.core.view.floatwindow.permission.PermissionListener;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.app.ActivityCache;
import com.deepsea.mua.stub.dialog.AAlertDialog;

/**
 * Created by JUN on 2019/9/2
 */
public class RoomMiniController {

    private static volatile RoomMiniController sInstance;

    private RoomMiniUtils mMiniUtils;

    public static RoomMiniController getInstance() {
        if (sInstance == null) {
            synchronized (RoomMiniController.class) {
                if (sInstance == null) {
                    sInstance = new RoomMiniController();
                }
            }
        }
        return sInstance;
    }

    private RoomMiniController() {
        mMiniUtils = new RoomMiniUtils();
    }

    public void setOffsetY(int offsetY) {
        mMiniUtils.setOffsetY(offsetY);
    }

    public boolean isMini() {
//        return mMiniUtils.isShowing();
        return RoomController.getInstance().getRoomModel() != null;
    }



    public void mini(Activity activity) {
        if (RomUtils.checkFloatWindowPermission(activity)) {
            activity.finish();
            mMiniUtils.show(AppUtils.getApp());
        } else {
            AAlertDialog dialog = new AAlertDialog(activity);
            dialog.setTitle("悬浮窗权限", R.color.black, 15);
            dialog.setMessage("您的手机没有授予悬浮窗权限，请开启后再试", R.color.black, 15);
            dialog.setLeftButton("暂不开启", R.color.black, (v, dialog1) -> {
                dialog1.dismiss();
//                RoomController.getInstance().release();
//                activity.finish();
            });
            dialog.setRightButton("现在去开启", R.color.black, (v, dialog1) -> {
                dialog1.dismiss();
                FloatActivity.request(new PermissionListener() {
                    @Override
                    public void onSuccess() {
                        activity.finish();
                        mMiniUtils.show(AppUtils.getApp());
                    }

                    @Override
                    public void onFail() {
                    }
                }, activity);
            });
            dialog.show();
        }
    }

    public void removeFloatWindow() {
        mMiniUtils.destroy();
    }

    public void destroy() {
        try {
            Class<?> clazz = Class.forName("com.deepsea.mua.voice.activity.RoomActivity");
            Activity activity = (Activity) ActivityCache.getInstance().getActivity(clazz);
            if (activity != null) {
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        removeFloatWindow();
        RoomController.getInstance().release();
    }
}
