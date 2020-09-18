package com.deepsea.mua.voice.activity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.FansRankVpAdapter;
import com.deepsea.mua.voice.databinding.ActivityFansRankBinding;

/**
 * Created by JUN on 2019/6/28
 * 粉丝贡献榜
 */
@Route(path = ArouterConst.PAGE_FANS_RANK)
public class FansRankActivity extends BaseActivity<ActivityFansRankBinding> {

    private FansRankVpAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fans_rank;
    }

    @Override
    protected void initView() {
        initViewPager();
    }

    private void initViewPager() {
        mAdapter = new FansRankVpAdapter(getSupportFragmentManager());
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.viewPager.setNoScroll(true);
        mBinding.tabLayout.setViewPager(mBinding.viewPager);
    }
}
