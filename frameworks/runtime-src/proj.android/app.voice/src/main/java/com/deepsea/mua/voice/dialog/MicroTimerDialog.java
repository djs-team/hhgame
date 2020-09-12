package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogMicroTimerBinding;

/**
 * Created by JUN on 2019/4/20
 */
public class MicroTimerDialog extends BaseDialog<DialogMicroTimerBinding> {

    public interface OnTimerListener {

        /**
         * 倒计时
         *
         * @param time 时长，单位秒
         */
        void onTimer(int time);
    }

    private OnTimerListener mListener;

    public MicroTimerDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected float getWidthPercent() {
        return 0.78F;
    }

    @Override
    protected float getDimAmount() {
        return 0.5F;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_micro_timer;
    }

    @Override
    protected void initListener() {
        ViewBindUtils.RxClicks(mBinding.closeIv, o -> {
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.time30s, o -> {
            if (mListener != null) {
                mListener.onTimer(30);
            }
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.time1m, o -> {
            if (mListener != null) {
                mListener.onTimer(60);
            }
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.time2m, o -> {
            if (mListener != null) {
                mListener.onTimer(120);
            }
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.time5m, o -> {
            if (mListener != null) {
                mListener.onTimer(300);
            }
            dismiss();
        });
    }

    public void setOnTimerListener(OnTimerListener listener) {
        this.mListener = listener;
    }
}
