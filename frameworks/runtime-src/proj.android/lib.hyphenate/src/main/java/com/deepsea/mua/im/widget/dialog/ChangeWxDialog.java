package com.deepsea.mua.im.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.im.R;
import com.deepsea.mua.im.databinding.DialogChangeWxBinding;

/**
 * Created by JUN on 2018/9/27
 */
public class ChangeWxDialog extends BaseDialog<DialogChangeWxBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * @param v
         * @param dialog
         */
        void onClick(View v, Dialog dialog);
    }

    public ChangeWxDialog(@NonNull Context context) {
        super(context);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_change_wx;
    }

    @Override
    protected float getWidthPercent() {
        return 0.77F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }


    public ChangeWxDialog setRightButton(String str, OnClickListener cli) {
        initButton(mBinding.btnSubmit, str, cli);
        return this;
    }


    private void initButton(Button btn, String str, OnClickListener cli) {
        btn.setText(str);
        if (cli == null) {
            cli = (v, dia) -> dismiss();
        }
        final OnClickListener c = cli;
        btn.setOnClickListener(v -> c.onClick(v, ChangeWxDialog.this)
        );
    }

    /**
     * 获取微信号
     * @return 微信号
     */
    public String getWxNo() {
        return mBinding.etWxNo.getText().toString().trim();
    }

}
