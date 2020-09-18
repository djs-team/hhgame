package com.deepsea.mua.voice.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by JUN on 2019/4/15
 */
public class ViewAnimUtils {
    public enum Property {
        HEIGHT,
        WIDTH,
        WIDTH_HEIGHT
    }

    public static ValueAnimator ofInt(final View target, final Property property, int time, Animator.AnimatorListener listener, final int... values) {
        ValueAnimator anim = ValueAnimator.ofInt(values);
        anim.addUpdateListener(animation -> {
            try {
                ViewGroup.LayoutParams lp = target.getLayoutParams();
                int value = (int) animation.getAnimatedValue();
                switch (property) {
                    case WIDTH:
                        lp.width = value;
                        break;
                    case HEIGHT:
                        lp.height = value;
                        break;
                    case WIDTH_HEIGHT:
                        lp.height = value;
                        lp.width = value;
                        break;
                }
                target.setLayoutParams(lp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        if (listener != null) {
            anim.addListener(listener);
        }
        anim.setDuration(time);
        anim.start();
        return anim;
    }

    public static void setWidthHeight(View target, Property property, int... values) {
        ViewGroup.LayoutParams lp = target.getLayoutParams();
        switch (property) {
            case WIDTH:
                lp.width = values[0];
                break;
            case HEIGHT:
                lp.height = values[0];
                break;
            case WIDTH_HEIGHT:
                lp.width = values[0];
                lp.height = values[1];
                break;
        }
        target.setLayoutParams(lp);
    }

    public static int getHeight(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredHeight();
    }

    public static int getWidth(View view) {
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        return view.getMeasuredWidth();
    }
}
