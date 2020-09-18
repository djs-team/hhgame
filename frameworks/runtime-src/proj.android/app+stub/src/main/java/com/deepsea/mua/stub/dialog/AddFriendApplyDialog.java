package com.deepsea.mua.stub.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogAddfriendApplyBinding;
import com.deepsea.mua.stub.databinding.DialogOpenVipBinding;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2018/9/27
 */
public class AddFriendApplyDialog extends BaseDialog<DialogAddfriendApplyBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onClick();
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public AddFriendApplyDialog(@NonNull Context context) {
        super(context);
        init();
    }


    @Override
    public void dismiss() {
        super.dismiss();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_addfriend_apply;
    }

    @Override
    protected float getWidthPercent() {
        return 0.6F;
    }

    @Override
    protected float getDimAmount() {
        return 0.5F;
    }


    public void init() {


    }

    @Override
    protected void initListener() {
        super.initListener();
        ViewBindUtils.RxClicks(mBinding.tvConfirm, o -> {
            if (onClickListener != null) {
                onClickListener.onClick();
            }
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.ivClose, o -> {
            dismiss();
        });
    }
}
