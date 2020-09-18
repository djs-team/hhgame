package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogAgeWheelBinding;
import com.deepsea.mua.mine.databinding.DialogDoubleWheelBinding;
import com.deepsea.mua.stub.callback.CommonCallback;
import com.deepsea.mua.stub.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JUN on 2019/10/18
 */
public class AgeWheelDialog extends BaseDialog<DialogAgeWheelBinding> {

    private CommonCallback<String> mCallback;


    public AgeWheelDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_age_wheel;
    }

    @Override
    protected float getWidthPercent() {
        return 0.92F;
    }

    @Override
    protected void initListener() {
        mBinding.confirmTv.setOnClickListener(v -> {
            dismiss();
            if (mCallback != null) {
                mCallback.onSuccess(mBinding.wheelNo1.getCurrentItem() + "-" + mBinding.wheelNo2.getCurrentItem());
            }
        });

    }

    public void setEnsureCallback(CommonCallback<String> callback) {
        mCallback = callback;
    }

    public AgeWheelDialog setTitle(String title) {
        mBinding.titleTv.setText(title);
        return this;
    }

    public void setData(List<String> ageOneList, String ageOneValue, List<String> ageTwoList, String ageTwoValue) {
        mBinding.wheelNo1.setEntries(ageOneList);
        mBinding.wheelNo2.setEntries(ageTwoList);
        int ageOneIndex = ageOneList.contains(ageOneValue) ? ageOneList.indexOf(ageOneValue) : 0;
        int ageTwoIndex = ageTwoList.contains(ageTwoValue) ? ageTwoList.indexOf(ageTwoValue) : 0;
        mBinding.wheelNo1.setCurrentIndex(ageOneIndex);
        mBinding.wheelNo2.setCurrentIndex(ageTwoIndex);
    }
}
