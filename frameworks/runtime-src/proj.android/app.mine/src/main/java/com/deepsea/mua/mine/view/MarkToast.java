package com.deepsea.mua.mine.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ToastMarkBinding;

import java.lang.reflect.Field;

/**
 * Created by JUN on 2019/7/16
 */
public class MarkToast extends Toast {

    private static Toast lastInstance;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    public MarkToast(Context context) {
        super(context);
    }

    public static MarkToast makeCustomText(Context context, CharSequence text,
                                           int duration) {
        MarkToast toast = new MarkToast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(getToastView(context, text));

//        try {
//            Object mTN = getField(toast.getClass().getSuperclass(), toast, "mTN");
//            if (mTN != null) {
//                Object mParams = getField(mTN.getClass(), mTN, "mParams");
//                if (mParams != null
//                        && mParams instanceof WindowManager.LayoutParams) {
//                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
//                    params.windowAnimations = R.style.anim_view;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return toast;
    }

    @Override
    public void show() {
        if (lastInstance != null) {
            lastInstance.cancel();
        }
        super.show();
        lastInstance = this;
    }

    @Override
    public void cancel() {
        super.cancel();
        lastInstance = null;
    }

    public static MarkToast makeCustomText(Context context, int text,
                                           int duration) {
        return makeCustomText(context, context.getString(text), duration);
    }

    public static View getToastView(Context context, CharSequence msg) {

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ToastMarkBinding binding = DataBindingUtil.inflate(inflate, R.layout.toast_mark, null, false);

        binding.tipsTv.setText(msg);

        return binding.getRoot();
    }

    public static int getWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    private static Object getField(Class<?> clz, Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = clz.getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }
}
