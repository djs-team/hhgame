package com.deepsea.mua.stub.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ResUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by JUN on 2018/9/21
 */
public class ViewBindUtils {

    /**
     * 设置文本
     *
     * @param view
     * @param text
     */
    public static void setText(View view, String text) {
        TextView textView = (TextView) view;
        textView.setText(text);
    }

    /**
     * 设置文本根据资源id
     *
     * @param view
     * @param textId
     */
    public static void setText(View view, @StringRes int textId) {
        TextView textView = (TextView) view;
        textView.setText(textId);
    }

    /**
     * 设置html文本
     *
     * @param textView
     * @param source
     */
    public static void setHtml(TextView textView, String source) {
        if (textView != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                textView.setText(Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY));
            } else {
                textView.setText(Html.fromHtml(source));
            }
        }
    }

    /**
     * 设置图片资源
     *
     * @param view
     * @param resId
     */
    public static void setImageRes(ImageView view, int resId) {
        view.setImageResource(resId);
    }

    /**
     * 设置图片url
     *
     * @param context
     * @param view
     * @param url
     */
    public static void setImageUrl(Context context, ImageView view, String url) {
        GlideUtils.loadImage(view, url);
    }

    /**
     * 设置图片
     *
     * @param view
     * @param bitmap
     */
    public static void setImageBitmap(ImageView view, Bitmap bitmap) {
        if (view != null && bitmap != null) {
            view.setImageBitmap(bitmap);
        }
    }

    /**
     * 设置drawable
     *
     * @param view
     * @param drawable
     */
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        if (view != null && drawable != null) {
            view.setImageDrawable(drawable);
        }
    }

    /**
     * 设置enable
     *
     * @param view
     * @param enable
     */
    public static void setEnable(View view, boolean enable) {
        view.setEnabled(enable);
    }

    /**
     * 设置字体颜色
     *
     * @param view
     * @param colorId
     */
    public static void setTextColor(View view, @ColorRes int colorId) {
        TextView textView = (TextView) view;
        textView.setTextColor(ResUtils.getColor(AppUtils.getApp(), colorId));
    }

    /**
     * 设置背景图
     *
     * @param view
     * @param drawable
     */
    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 设置背景
     *
     * @param view
     * @param resId
     */
    public static void setBackgroundRes(View view, int resId) {
        view.setBackgroundResource(resId);
    }


    /**
     * 设置是否可见
     *
     * @param view
     * @param visible
     */
    public static void setVisible(View view, boolean visible) {
        if (visible) {
            if (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE) {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置提醒
     *
     * @param view
     * @param hint
     */
    public static void setHint(View view, String hint) {
        TextView textView = (TextView) view;
        textView.setHint(hint);
    }

    /**
     * 设置提醒根据资源id
     *
     * @param view
     * @param hintId
     */
    public static void setHint(View view, int hintId) {
        TextView textView = (TextView) view;
        textView.setHint(hintId);
    }

    /**
     * 处理点击事件l
     *
     * @param view
     * @param consumer
     */
    public static Disposable RxClicks(View view, Consumer<Object> consumer) {
        return RxView.clicks(view)
                .throttleFirst(Const.VIEW_THROTTLE_TIME, TimeUnit.MILLISECONDS)
                .subscribe(consumer);
    }
}
