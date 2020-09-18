package com.deepsea.mua.core.dialog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.deepsea.mua.core.R;


/**
 * Created by JUN on 2018/10/24
 */
public abstract class BasePopupWindow extends PopupWindow {

    private static final float ALPHA_MIN = 0F;
    private static final float ALPHA_MAX = 0.6F;
    private static final long ANIMATION_DURATION = 300;

    private final View mContentView;
    protected Context mContext;
    private WindowManager windowManager;
    private View maskView;
    private ValueAnimator valueAnimator;

    public BasePopupWindow(Context context) {
        super(context);
        this.mContext = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mContentView = View.inflate(mContext, getLayoutId(), null);
        setContentView(mContentView);
        initView();
        initListener();
        setAnimationStyle(R.style.pop_y_spread_anim);
        setHeight(initHeight());
        setWidth(initWidth());
        setOutsideTouchable(true);
        setFocusable(true);
        setTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        update();
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void initListener() {
    }

    protected int initHeight() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected int initWidth() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        View view = mContentView.findViewById(id);
        return (T) view;
    }

    protected boolean hasMask() {
        return true;
    }

    @Override
    public void showAsDropDown(View anchor) {
        addMask(anchor.getWindowToken());
        super.showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        addMask(anchor.getWindowToken());
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        addMask(anchor.getWindowToken());
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        addMask(parent.getWindowToken());
        super.showAtLocation(parent, gravity, x, y);
    }

    private void addMask(IBinder token) {
        if (!hasMask())
            return;
        WindowManager.LayoutParams wl = new WindowManager.LayoutParams();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.MATCH_PARENT;
//        wl.format = PixelFormat.TRANSLUCENT;//不设置这个弹出框的透明遮罩显示为黑色
        wl.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;//该Type描述的是形成的窗口的层级关系
        wl.token = token;//获取当前Activity中的View中的token,来依附Activity
        maskView = new View(mContext);
        maskView.setBackgroundColor(0x80000000);
        maskView.setFitsSystemWindows(false);
        maskView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    removeMask();
                    return true;
                }
                return false;
            }
        });
        startAnimation();
        /**
         * 通过WindowManager的addView方法创建View，产生出来的View根据WindowManager.LayoutParams属性不同，效果也就不同了。
         * 比如创建系统顶级窗口，实现悬浮窗口效果！
         */
        windowManager.addView(maskView, wl);
    }

    private void startAnimation() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(ALPHA_MIN, ALPHA_MAX);
            valueAnimator.setDuration(ANIMATION_DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (maskView != null) {
                        maskView.setAlpha(ALPHA_MAX * (float) animation.getAnimatedValue());
                    }
                }
            });
        } else {
            valueAnimator.cancel();
            valueAnimator.setFloatValues(ALPHA_MIN, ALPHA_MAX);
        }
        valueAnimator.start();
    }

    private void removeMask() {
        if (null != maskView) {
            windowManager.removeViewImmediate(maskView);
            maskView = null;
        }
    }

    @Override
    public void dismiss() {
        removeMask();
        super.dismiss();
    }
}
