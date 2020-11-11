package com.deepsea.mua.core.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.deepsea.mua.core.R;

/**
 * Created by JUN on 2019/7/8
 */
public abstract class BaseDialogFragment<T extends ViewDataBinding> extends DialogFragment {

    protected final String TAG = getClass().getSimpleName();

    protected Activity mContext;
    protected View view;
    protected T mBinding;

    protected int mGravity = Gravity.CENTER;

    @Override
    public void onAttach(Activity context) {
        Log.e(TAG, "onAttach: ");
        super.onAttach(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.e(TAG, "onCreateDialog: ");
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), null, false);
        view = mBinding.getRoot();
        Dialog dialog = new Dialog(mContext, getThemeResId());
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = isHeightMarch() ? WindowManager.LayoutParams.MATCH_PARENT : WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setDimAmount(getDimAmount());
            window.setWindowAnimations(getAnimationResId());
            window.setGravity(mGravity);
        }

        initView();
        initListener();
        return dialog;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected int getThemeResId() {
        return R.style.dialog;
    }

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
     * Dialog显示宽度比例
     *
     * @return
     */
    protected float getHeightPercent() {
        return 1.0F;
    }


    public boolean isHeightMarch() {
        return false;
    }


    /**
     * 蒙层
     *
     * @return
     */
    protected float getDimAmount() {
        return 0.5F;
    }

    private int getWidth() {
        return (int) (mContext.getResources().getDisplayMetrics().widthPixels * getWidthPercent());
    }

    private int getHeight() {
        return (int) (mContext.getResources().getDisplayMetrics().heightPixels * getWidthPercent());
    }

    public void show(FragmentManager manager) {
        super.show(manager, TAG);
    }

    public void showAtBottom(FragmentManager manager) {
        mGravity = Gravity.BOTTOM;
        show(manager);
    }
}
