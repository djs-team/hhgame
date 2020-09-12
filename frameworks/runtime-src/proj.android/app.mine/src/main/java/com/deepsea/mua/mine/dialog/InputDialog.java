package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.KeyboardUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogInputBinding;
import com.deepsea.mua.stub.callback.CommonCallback;

/**
 * Created by JUN on 2019/10/18
 */
public class InputDialog extends BaseDialog<DialogInputBinding> {

    private CommonCallback<String> mCallback;

    private String mOldValue;

    public InputDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_input;
    }

    @Override
    protected float getWidthPercent() {
        return 0.92F;
    }

    @Override
    protected void initListener() {
        mBinding.ensureTv.setOnClickListener(v -> {

            String input = mBinding.editText.getText().toString();
            if (TextUtils.isEmpty(input)) {
                ToastUtils.showToast("昵称不能为空");
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
        mBinding.cancelTv.setOnClickListener(v -> {
            KeyboardUtils.hideSoftInput(mBinding.editText);
            dismiss();
        });
    }

    public void setEnsureCallback(CommonCallback<String> callback) {
        mCallback = callback;
    }

    public InputDialog setTitle(String title) {
        mBinding.titleTv.setText(title);
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
