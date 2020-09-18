package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogChargeTypeBinding;
import com.deepsea.mua.stub.utils.ViewBindUtils;

import java.util.List;

/**
 * Created by JUN on 2019/5/5
 */
public class ChargeTypeDialog extends BaseDialog<DialogChargeTypeBinding> {

    private OnChargeTypeListener mListener;

    public interface OnChargeTypeListener {
        void onAliPay();

        void onWxPay();

        void onHaibeiPay();
    }

    public ChargeTypeDialog(@NonNull Context context) {
        super(context);
    }

    public void setPayTypes(List<String> payTypes) {
        if (payTypes != null) {
            if (payTypes.size() == 2) {
                ViewBindUtils.setVisible(mBinding.aliPay, true);
                ViewBindUtils.setVisible(mBinding.wxPay, true);
            } else if (payTypes.size() == 1) {
                if (payTypes.get(0).equals("1")) {
                    ViewBindUtils.setVisible(mBinding.aliPay, true);
                    ViewBindUtils.setVisible(mBinding.wxPay, false);
                } else if (payTypes.get(0).equals("2")) {
                    ViewBindUtils.setVisible(mBinding.aliPay, false);
                    ViewBindUtils.setVisible(mBinding.wxPay, true);
                }
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_charge_type;
    }

    @Override
    protected void initListener() {
        mBinding.aliPay.setOnClickListener(v -> {
            mBinding.aliSelectIv.setVisibility(View.VISIBLE);
            mBinding.wxSelectIv.setVisibility(View.GONE);
            mBinding.haibeiSelectIv.setVisibility(View.GONE);
            if (mListener != null) {
                mListener.onAliPay();
            }
            dismiss();
        });
        mBinding.wxPay.setOnClickListener(v -> {
            mBinding.wxSelectIv.setVisibility(View.VISIBLE);
            mBinding.aliSelectIv.setVisibility(View.GONE);
            mBinding.haibeiSelectIv.setVisibility(View.GONE);

            if (mListener != null) {
                mListener.onWxPay();
            }
            dismiss();
        });
        mBinding.haibeiPay.setOnClickListener(v -> {
            mBinding.haibeiSelectIv.setVisibility(View.VISIBLE);
            mBinding.aliSelectIv.setVisibility(View.GONE);
            mBinding.wxSelectIv.setVisibility(View.GONE);
            if (mListener != null) {
                mListener.onHaibeiPay();
            }
            dismiss();
        });
    }

    public void setOnChargeTypeListener(OnChargeTypeListener listener) {
        this.mListener = listener;
    }
}
