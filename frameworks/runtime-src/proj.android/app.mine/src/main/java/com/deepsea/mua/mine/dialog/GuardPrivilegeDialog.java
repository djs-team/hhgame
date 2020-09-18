package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogGuardPrivilegeBinding;
import com.deepsea.mua.mine.databinding.DialogGuardRenewCloseBinding;

/**
 * 守护特权
 *
 */
public class GuardPrivilegeDialog extends BaseDialog<DialogGuardPrivilegeBinding> {



    public GuardPrivilegeDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_guard_privilege;
    }

    @Override
    protected float getWidthPercent() {
        return 0.75F;
    }

    @Override
    protected void initListener() {
        mBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

    }


}
