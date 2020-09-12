package com.deepsea.mua.core.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.deepsea.mua.core.R;


/**
 * Created by JUN on 2018/9/19
 */
public abstract class BaseDialog<T extends ViewDataBinding> extends Dialog {

    protected final String TAG = getClass().getSimpleName();

    protected Context mContext;
    protected View view;
    protected T mBinding;

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.dialog);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false);
        view = mBinding.getRoot();
        setContentView(view);
        initListener();
        setDimAmount(getDimAmount());
    }

    protected abstract int getLayoutId();

    protected void initListener() {
    }

    /**
     * Dialog显示动画
     *
     * @return
     */
    protected int getAnimationResId() {
        return 0;
    }

    /**
     * Dialog显示宽度比例
     *
     * @return
     */
    protected float getWidthPercent() {
        return 1.0F;
    }

    /**
     * 蒙层
     *
     * @return
     */
    protected float getDimAmount() {
        return 0.5F;
    }

    public void setDimAmount(float dimAmount) {
        Window window = getWindow();
        if (null != window) {
            window.setDimAmount(dimAmount);
        }
    }

    private int getWidth() {
        return (int) (mContext.getResources().getDisplayMetrics().widthPixels * getWidthPercent());
    }

    public boolean isHeightFullScree() {
        return false;
    }


    @Override
    public void show() {
        if (!isShowing()) {
            Window window = getWindow();
            if (null != window) {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = getWidth();
                if (isHeightFullScree()) {
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                } else {
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                }
                window.setAttributes(lp);
//                window.setDimAmount(getDimAmount());
                window.setWindowAnimations(getAnimationResId());
            }
            super.show();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            super.dismiss();
        }
    }

    public void showAtBottom() {
        Window window = getWindow();
        if (null != window) {
            window.setGravity(Gravity.BOTTOM);
        }
        show();
    }

    public void showAtBottomAndRight() {
        Window window = getWindow();
        if (null != window) {
            window.setGravity(Gravity.BOTTOM | Gravity.RIGHT);

        }
        show();
    }
}
