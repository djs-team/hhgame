package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogUnautherrorBinding;

/**
 * 作者：liyaxing  2019/9/2 18:25
 * 类别 ：身份未认证弹框
 */
public class UnauthErrorDialog extends BaseDialog<DialogUnautherrorBinding> {

    private UnauthErrorDialog.CreateListener mListener;

    public UnauthErrorDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected float getWidthPercent() {
        return 0.80F;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_unautherror;
    }

    @Override
    protected void initListener() {
        mBinding.confirm.setOnClickListener(v -> {
            if (mListener != null)
                mListener.onOk();
            dismiss();
        });
    }

    public interface CreateListener {
        void onOk();//去重新输入
    }

    public void setCreatListener(UnauthErrorDialog.CreateListener listener) {
        this.mListener = listener;
    }


}
