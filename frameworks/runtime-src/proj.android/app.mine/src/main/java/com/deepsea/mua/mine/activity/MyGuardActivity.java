package com.deepsea.mua.mine.activity;

import android.os.Bundle;
import android.view.View;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.MyGuardAdapter;
import com.deepsea.mua.mine.databinding.ActivityMyGuardBinding;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;


/**
 * Created by JUN on 2019/5/7
 */
public class MyGuardActivity extends BaseActivity<ActivityMyGuardBinding> {

    @Inject
    ViewModelFactory mModelFactory;


    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_guard;
    }

    MyGuardAdapter mAdapter;

    @Override
    protected void initView() {
        mAdapter = new MyGuardAdapter(getSupportFragmentManager());
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.viewPager.setNoScroll(true);
        mBinding.viewPager.setOffscreenPageLimit(2);
        mBinding.guardmineTab.setSelected(true);
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.rlTabGuardMine, o -> {
            mBinding.viewPager.setCurrentItem(0);
            mBinding.guardmineTab.setSelected(true);
            mBinding.guardbeTab.setSelected(false);
            mBinding.viewGuardmine.setVisibility(View.VISIBLE);
            mBinding.viewGuardbe.setVisibility(View.GONE);
        });
        subscribeClick(mBinding.rlTabGuardBe, o -> {
            mBinding.viewPager.setCurrentItem(1);
            mBinding.guardmineTab.setSelected(false);
            mBinding.guardbeTab.setSelected(true);
            mBinding.viewGuardmine.setVisibility(View.GONE);
            mBinding.viewGuardbe.setVisibility(View.VISIBLE);
        });

    }


}
