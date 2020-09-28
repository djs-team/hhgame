package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogWxbindResultBinding;

/**
 * Created by JUN on 2019/10/18
 */
public class WxBindResultDialog extends BaseDialog<DialogWxbindResultBinding> {

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

    @Override
    public boolean isHeightFullScree() {
        return false;
    }

    public WxBindResultDialog(@NonNull Context context) {
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

    }

    public void setDialogType(int type) {
        //1绑定成功 2 解绑成功


    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_wxbind_result;
    }

    @Override
    protected float getWidthPercent() {
        return 0.77F;
    }

    @Override
    protected void initListener() {

    }


}
