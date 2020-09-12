package com.deepsea.mua.mine.activity;

import android.content.Intent;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityAssistBinding;
import com.deepsea.mua.mine.databinding.ActivitySafetyCenterBinding;
import com.deepsea.mua.stub.base.BaseActivity;

/**
 * Created by JUN on 2019/8/9
 */
public class SafetyCenterActivity extends BaseActivity<ActivitySafetyCenterBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_safety_center;
    }

    @Override
    protected void initView() {
        subscribeClick(mBinding.accountCancellationLayout, o -> {
            startActivity(new Intent(mContext,CancellationActivity.class));
        });
    }
}
