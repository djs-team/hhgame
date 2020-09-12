package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogExchangeBinding;

/**
 * Created by JUN on 2019/7/25
 */
public class ExchangeDialog extends BaseDialog<DialogExchangeBinding> {

    public interface OnExchangedListener {
        void onExchanged();

        void onBack();
    }

    private OnExchangedListener mListener;

    public ExchangeDialog(@NonNull Context context) {
        super(context);
        setCancelable(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_exchange;
    }

    @Override
    protected float getDimAmount() {
        return 0.4F;
    }

    @Override
    protected float getWidthPercent() {
        return 0.66F;
    }

    @Override
    protected void initListener() {
        mBinding.exchangeTv.setOnClickListener(v -> {
            dismiss();
            if (mListener != null) {
                mListener.onExchanged();
            }
        });
        mBinding.backTv.setOnClickListener(v -> {
            dismiss();
            if (mListener != null) {
                mListener.onBack();
            }
        });
        mBinding.confirmTv.setOnClickListener(v -> {
            dismiss();
        });
    }

    public void setStatus(boolean success) {
        mBinding.statusTv.setText(success ? "兑换成功" : "兑换失败");
    }

    public void setOnExchangedListener(OnExchangedListener listener) {
        this.mListener = listener;
    }
}
