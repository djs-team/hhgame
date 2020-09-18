package com.deepsea.mua.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityFollowFanBinding;
import com.deepsea.mua.mine.fragment.FanFragment;
import com.deepsea.mua.mine.fragment.FollowFragment;
import com.deepsea.mua.stub.adapter.ViewPagerAdapter;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.utils.ArouterConst;

import java.util.Arrays;

/**
 * Created by JUN on 2019/5/5
 * 关注/粉丝
 */
@Route(path = ArouterConst.PAGE_ME_FOLLOW_AND_FAN)

public class FollowAndFanActivity extends BaseActivity<ActivityFollowFanBinding> {

    private ViewPagerAdapter mAdapter;
    @Autowired(name = "pos")
     int pos;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_follow_fan;
    }

    @Override
    protected void initView() {
        initTabLayout();
        initViewPager();
    }

    private void initTabLayout() {
    }

    private void initViewPager() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment[] fragments = new Fragment[]{new FollowFragment(), new FanFragment()};
        mAdapter.setNewData(Arrays.asList(fragments), Arrays.asList("关注", "粉丝"));
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        mBinding.viewPager.setCurrentItem(pos);
    }

    @Override
    protected void initListener() {
        mBinding.backIv.setOnClickListener(v -> finish());
    }
}
