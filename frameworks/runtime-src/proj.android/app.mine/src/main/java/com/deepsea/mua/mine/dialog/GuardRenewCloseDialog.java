package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogGuardRenewCloseBinding;
import com.deepsea.mua.mine.databinding.DialogWxbindResultBinding;

/**
 * Created by JUN on 2019/10/18
 */
public class GuardRenewCloseDialog extends BaseDialog<DialogGuardRenewCloseBinding> {

    public interface OnClickListener {
        public void onClickOk();
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public GuardRenewCloseDialog(@NonNull Context context) {
        super(context);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_guard_renew_close;
    }

    @Override
    protected float getWidthPercent() {
        return 0.75F;
    }

    @Override
    protected void initListener() {
        mBinding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClickOk();
                }
                dismiss();
            }
        });
        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
