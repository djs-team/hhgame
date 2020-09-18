package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogNumberRangeBinding;
import com.deepsea.mua.stub.callback.CommonCallback;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.RegulerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUN on 2019/10/19
 */
public class NumberRangeDialog extends BaseDialog<DialogNumberRangeBinding> {

    private CommonCallback<String> mCallback;

    private List<String> mDatas = new ArrayList<>();

    private String mSuffix = "";

    public NumberRangeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_number_range;
    }

    @Override
    protected float getWidthPercent() {
        return 0.92F;
    }

    @Override
    protected void initListener() {
        mBinding.ensureTv.setOnClickListener(v -> {
            dismiss();
            if (mCallback != null) {
                String num1 = String.valueOf(mBinding.wheelNo1.getCurrentItem());
                String num2 = String.valueOf(mBinding.wheelNo2.getCurrentItem());

                if (!TextUtils.isEmpty(mSuffix)) {
                    num1 = num1.substring(0, num1.length() - mSuffix.length());
                    num2 = num2.substring(0, num2.length() - mSuffix.length());
                }
                num1= RegulerUtils.matchNum(num1);
                num2= RegulerUtils.matchNum(num2);
                if (FormatUtils.moreThan(num1, num2)) {
                    mCallback.onSuccess(num2 + "," + num1);
                } else {
                    mCallback.onSuccess(num1 + "," + num2);
                }
            }
        });
    }

    public void setEnsureCallback(CommonCallback<String> callback) {
        mCallback = callback;
    }

    public NumberRangeDialog setTitle(String title) {
        mBinding.titleTv.setText(title);
        return this;
    }

    public void setSuffix(String suffix) {
        if (!TextUtils.isEmpty(suffix)) {
            mSuffix = suffix;
        }
    }

    public void setData(String range, String value) {
        if (TextUtils.isEmpty(range) || !range.contains(","))
            return;

        String[] split = range.split(",");
        int minValue = 0;
        int maxValue = 0;
        if (!TextUtils.isEmpty(value) && value.contains(",")) {
            String[] arr = value.split(",");
            minValue = Integer.parseInt(RegulerUtils.matchNum(arr[0]));
            maxValue = Integer.parseInt(RegulerUtils.matchNum(arr[1]));
        }

        setData(Integer.parseInt(RegulerUtils.matchNum(split[0])), Integer.parseInt(RegulerUtils.matchNum(split[1])), minValue, maxValue);
    }

    public void setData(int min, int max, int minValue, int maxValue) {
        mDatas.clear();
        for (int i = min; i <= max; i++) {
            mDatas.add(i + mSuffix);
        }
        mBinding.wheelNo1.setEntries(mDatas);
        mBinding.wheelNo2.setEntries(mDatas);

        mBinding.wheelNo1.setCurrentIndex(mDatas.indexOf(minValue + mSuffix));
        mBinding.wheelNo2.setCurrentIndex(mDatas.indexOf(maxValue + mSuffix));
    }
}
