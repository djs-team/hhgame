package com.deepsea.mua.stub.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogFirstRechargeWelfareBinding;
import com.deepsea.mua.stub.entity.FirstRechargeVo;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2018/9/27
 */
public class RechargeFirstWelfareDialog extends BaseDialog<DialogFirstRechargeWelfareBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onClick();

        void onMyDismiss(boolean isClickRecharge);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public RechargeFirstWelfareDialog(@NonNull Context context) {
        super(context);
    }

    boolean isClickRecharge = false;

    @Override
    public void dismiss() {
        if (onClickListener != null) {
            onClickListener.onMyDismiss(isClickRecharge);
        }
        super.dismiss();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_first_recharge_welfare;
    }

    @Override
    protected float getWidthPercent() {
        return 1F;
    }

    @Override
    protected float getDimAmount() {
        return 0.5F;
    }


    public void init(FirstRechargeVo data) {
        ViewBindUtils.RxClicks(mBinding.tvOk, o -> {
            if (onClickListener != null) {
                onClickListener.onClick();
            }
            isClickRecharge = true;
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.rlClose, o -> {
            isClickRecharge = false;
            if (onClickListener != null) {
                dismiss();
            }

        });
        ViewBindUtils.setText(mBinding.tvRoseNum, data.getDiamond());
        ViewBindUtils.setText(mBinding.tvNowPrice, data.getRmb());
        ViewBindUtils.setText(mBinding.tvOldPrice, "原价"+data.getOld_rmb()+"元");
    }

}
