package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogUnbindConfrimBinding;
import com.deepsea.mua.mine.databinding.DialogUnbindResultBinding;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/10/18
 */
public class WxUnbindResultDialog extends BaseDialog<DialogUnbindResultBinding> {

    public interface OnClickListener {
        public void onClickOk();
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public WxUnbindResultDialog(@NonNull Context context) {
        super(context);
        mBinding.ivConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClickOk();
                }
                dismiss();
            }
        });
        mBinding.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }



    @Override
    protected int getLayoutId() {
        return R.layout.dialog_unbind_result;
    }

    @Override
    protected float getWidthPercent() {
        return 0.77F;
    }

    @Override
    protected void initListener() {

    }


}
