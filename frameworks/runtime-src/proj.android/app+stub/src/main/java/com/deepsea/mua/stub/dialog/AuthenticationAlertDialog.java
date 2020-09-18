package com.deepsea.mua.stub.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogAlertBinding;
import com.deepsea.mua.stub.databinding.DialogAuthenticationBinding;
import com.deepsea.mua.stub.entity.AuthenticationBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2018/9/27
 */
public class AuthenticationAlertDialog extends BaseDialog<DialogAuthenticationBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * @param v
         * @param dialog
         */
        void onClick(View v, Dialog dialog);
    }


    public AuthenticationAlertDialog(@NonNull Context context) {
        super(context);
        ViewBindUtils.RxClicks(mBinding.confirmTv, o -> {
            dismiss();
        });
    }

    public void setContent(String alert) {
        mBinding.tvContent.setText(alert);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_authentication;
    }

    @Override
    protected float getWidthPercent() {
        return 0.77F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }


}
