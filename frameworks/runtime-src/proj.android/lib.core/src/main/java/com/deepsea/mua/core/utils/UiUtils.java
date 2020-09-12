package com.deepsea.mua.core.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by JUN on 2018/10/11
 */
public class UiUtils {

    /**
     * 隐藏输入键盘
     *
     * @param focusView
     */
    public static void hideKeyboard(View focusView) {
        if (focusView != null) {
            InputMethodManager imm = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示软键盘
     *
     * @param focusView
     */
    public static void showKeyboard(View focusView) {
        if (focusView != null) {
            InputMethodManager imm = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED);
            }
        }
    }

    public static void toggleSoftInput(View focusView) {
        if (focusView != null) {
            InputMethodManager imm = (InputMethodManager) focusView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }
    }

    /**
     * 处理软键盘
     *
     * @param window
     * @param isShow
     */
    public static void dealKeyborad(Window window, boolean isShow) {
        if (window != null) {
            if (isShow) {
                showKeyboard(window.getCurrentFocus());
            } else {
                hideKeyboard(window.getCurrentFocus());
            }
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param focusView
     * @param ev
     */
    public static boolean hideKeyboard(View focusView, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isHideKeyboard(focusView, ev)) {
                hideKeyboard(focusView);
                return true;
            }
        }
        return false;
    }

    /**
     * 判定是否需要隐藏
     *
     * @param v
     * @param ev
     * @return
     */
    private static boolean isHideKeyboard(View v, MotionEvent ev) {
        if (v != null) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            return !(ev.getX() > left) || !(ev.getX() < right) || !(ev.getY() > top)
                    || !(ev.getY() < bottom);
        }
        return false;
    }

    /**
     * 复制
     *
     * @param context
     * @param text
     */
    public static void copy(Context context, String text) {
        if (TextUtils.isEmpty(text))
            return;
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        ClipData mClipData = ClipData.newPlainText(null, text);
        // 把数据集设置（复制）到剪贴板
        if (clipboard != null) {
            clipboard.setPrimaryClip(mClipData);
        }
    }

    /**
     * 粘贴
     *
     * @param context
     */
    public static String paste(Context context) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            // 获取剪贴板的剪贴数据集
            ClipData clipData = clipboard.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                // 从数据集中获取（粘贴）第一条文本数据
                return clipData.getItemAt(0).getText().toString();
            }
        }
        return null;
    }
}
