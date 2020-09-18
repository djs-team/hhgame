package com.deepsea.mua.stub.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogUnauthorizedBinding;
import com.deepsea.mua.stub.utils.PageJumpUtils;

/**
 * 作者：liyaxing  2019/9/2 18:25
 * 类别 ：身份未认证弹框
 */
public class UnauthorizedDialog extends BaseDialog<DialogUnauthorizedBinding> {


    public UnauthorizedDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected float getWidthPercent() {
        return 0.80F;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_unauthorized;
    }

    @Override
    protected void initListener() {
        mBinding.cancel.setOnClickListener(v -> {
            dismiss();
        });
        mBinding.confirm.setOnClickListener(v -> {
            PageJumpUtils.jumpToAuth(0);
            dismiss();
        });
    }


}
