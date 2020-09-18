package com.deepsea.mua.stub.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogApplyInfoBinding;
import com.deepsea.mua.stub.databinding.DialogCityScreeningBinding;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.view.DoubleSlideSeekBar;

/**
 * Created by JUN on 2018/9/27
 */
public class ApplyInfoDialog extends BaseDialog<DialogApplyInfoBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onClick(String wx, String age);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ApplyInfoDialog(@NonNull Context context) {
        super(context);
        init();
    }


    @Override
    public void dismiss() {
        super.dismiss();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_apply_info;
    }

    @Override
    protected float getWidthPercent() {
        return 0.77F;
    }

    @Override
    protected float getDimAmount() {
        return 0.5F;
    }


    public void init() {
        ViewBindUtils.RxClicks(mBinding.tvOk, o -> {
            if (onClickListener != null) {
                onClickListener.onClick(mBinding.etWxnum.getText().toString().trim(), mBinding.etAge.getText().toString().trim());
            }
            dismiss();

        });

    }

}
