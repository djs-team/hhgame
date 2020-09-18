package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.KeyboardUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogBindSuccessBinding;
import com.deepsea.mua.mine.databinding.DialogInputBinding;
import com.deepsea.mua.stub.callback.CommonCallback;

import java.text.Format;

/**
 * Created by JUN on 2019/10/18
 */
public class InputBindSuccessDialog extends BaseDialog<DialogBindSuccessBinding> {

    private CommonCallback<String> mCallback;

    private String mOldValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public InputBindSuccessDialog(@NonNull Context context) {
        super(context);
        mBinding.rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_bind_success;
    }

    @Override
    protected float getWidthPercent() {
        return 0.9F;
    }

    @Override
    protected void initListener() {
        mBinding.ivSubmit.setOnClickListener(v -> {

            String input = mBinding.editText.getText().toString();
            if (TextUtils.isEmpty(input)) {
                ToastUtils.showToast("邀请码不能为空");
                return;
            }

            KeyboardUtils.hideSoftInput(mBinding.editText);
            dismiss();

            if (TextUtils.equals(mOldValue, input))
                return;

            if (mCallback != null) {
                mCallback.onSuccess(input);
            }
        });
    }

    public void setEnsureCallback(CommonCallback<String> callback) {
        mCallback = callback;
    }

    public InputBindSuccessDialog setContent( String coin) {
        mBinding.tvInvitecodeDesc.setText(String.format("  如果您受邀下载并注册合合有约\n交友平台，您和邀请人都会获得%s朵\n玫瑰作为奖励。", coin));
        return this;
    }

    public void setData(String value) {

        this.mOldValue = value;
        mBinding.editText.setText(value);
        if (!TextUtils.isEmpty(value)) {
            mBinding.editText.setSelection(mBinding.editText.length());
        }
    }
}
