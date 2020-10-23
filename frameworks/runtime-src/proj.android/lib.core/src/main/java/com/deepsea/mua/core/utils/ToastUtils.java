package com.deepsea.mua.core.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.widget.Toast;

import com.deepsea.mua.core.R;

/**
 * Created by JUN on 2018/9/28
 */
public class ToastUtils {

    private static Toast mToast = null;
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void showToast(CharSequence text) {
        showToast(AppUtils.getApp(), text, Toast.LENGTH_SHORT);
    }

    public static void showToast(final Context context, final CharSequence text, final int duration) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            show(context, text, duration);
        } else {
            mHandler.post(() -> show(context, text, duration));
        }
    }

    public static void showToastCenter(final Context context, final CharSequence text, final int duration) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showCenter(context, text, duration);
        } else {
            mHandler.post(() -> showCenter(context, text, duration));
        }
    }

    private static void show(Context context, CharSequence text, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, duration);
        mToast.setText(text);
        mToast.show();
    }

    private static void showCenter(Context context, CharSequence text, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, text, duration);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setText(text);
        mToast.show();
    }

    public static void showHtmlCenter(Context context, String text, int duration) {
        if (mToast != null) {
            mToast.cancel();
        }
        String strHtml = "<font color='#ffffff'>" + text + "'</font>'";
        mToast = Toast.makeText(context, Html.fromHtml(strHtml), duration);
        mToast.getView().setBackgroundColor(Color.parseColor("#00FFFFFF"));
        mToast.setGravity(Gravity.CENTER, 0, 0);
        Spanned sp = Html.fromHtml(text);
        mToast.setText(sp);
        mToast.show();
    }

}
