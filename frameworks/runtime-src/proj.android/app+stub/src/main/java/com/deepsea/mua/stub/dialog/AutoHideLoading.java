package com.deepsea.mua.stub.dialog;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.deepsea.mua.core.databinding.DialogLoadingBinding;
import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.R;

/**
 * Created by JUN on 2019/6/30
 */
public class AutoHideLoading extends BaseDialog<DialogLoadingBinding> {

    //超时时长
    private long mTimeOut = 5000;

    public int tag = -10000;
    public float mDimAmount = 0F;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public AutoHideLoading(@NonNull Context context) {
        this(context, null);
    }

    public AutoHideLoading(@NonNull Context context, String message) {
        super(context);
        setCancelable(false);
        TextView loadingTv = findViewById(R.id.loading_tv);
        loadingTv.setText(message);
        loadingTv.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
    }

    @Override
    protected float getDimAmount() {
        return mDimAmount;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_loading;
    }

    public void setTimeOut(long timeOut) {
        mTimeOut = timeOut;
    }

    private void startTimeObservable() {
        mHandler.postDelayed(() -> {
            if (isShowing()) {
                dismiss();
            }
        }, mTimeOut);
    }

    @Override
    public void show() {
        startTimeObservable();
        super.show();
    }

    @Override
    public void dismiss() {
        try {
            mHandler.removeCallbacksAndMessages(null);
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
