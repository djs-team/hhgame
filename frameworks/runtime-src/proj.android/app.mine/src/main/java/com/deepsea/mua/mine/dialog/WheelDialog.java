package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogWheelBinding;
import com.deepsea.mua.stub.utils.CollectionUtils;

import java.util.List;

/**
 * Created by JUN on 2019/10/14
 */
public class WheelDialog extends BaseDialog<DialogWheelBinding> {

    public interface WheelDialogListener {
        void onSelected(String value);
    }

    private WheelDialogListener mListener;

    public WheelDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_wheel;
    }

    @Override
    protected float getWidthPercent() {
        return 0.92F;
    }

    @Override
    protected void initListener() {
        mBinding.ensureTv.setOnClickListener(v -> {
            dismiss();
            if (mListener != null) {
                CharSequence cs = mBinding.wheelView.getCurrentItem();
                mListener.onSelected(cs == null ? "" : cs.toString());
            }
        });
    }

    private void initView() {
    }

    public WheelDialog setTitle(String title) {
        mBinding.titleTv.setText(title);
        return this;
    }

    public void setEntries(List<String> list) {
        mBinding.wheelView.setEntries(list);
    }

    public void setEntries(List<String> list, String value) {
        mBinding.wheelView.setEntries(list);
        int index = 0;
        if (!CollectionUtils.isEmpty(list) && !TextUtils.isEmpty(value)) {
            index = list.indexOf(value);
        }
        setCurrentIndex(index);
    }

    public void setCurrentIndex(int index) {
        mBinding.wheelView.setCurrentIndex(index);
    }

    public void setWheelDialogListener(WheelDialogListener listener) {
        this.mListener = listener;
    }
}
