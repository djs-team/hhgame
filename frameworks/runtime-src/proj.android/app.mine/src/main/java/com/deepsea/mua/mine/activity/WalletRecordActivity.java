package com.deepsea.mua.mine.activity;

import android.support.v4.app.Fragment;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityWalletRecordBinding;
import com.deepsea.mua.mine.fragment.MDFragment;
import com.deepsea.mua.stub.adapter.ViewPagerAdapter;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.utils.Constant;

import java.util.Arrays;

/**
 * Created by JUN on 2019/5/6
 * 钱包明细
 */
public class WalletRecordActivity extends BaseActivity<ActivityWalletRecordBinding> {

    private ViewPagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wallet_record;
    }

    @Override
    protected void initView() {
        initViewPager();
    }

    private void initViewPager() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        Fragment[] fragments = new Fragment[]{MDFragment.newInstance(Constant.MD), MDFragment.newInstance(Constant.DIAMOND)};
//        mAdapter.setNewData(Arrays.asList(fragments), Arrays.asList("玫    瑰", "钻石"));

        Fragment[] fragments = new Fragment[]{MDFragment.newInstance(Constant.MD)};
        mAdapter.setNewData(Arrays.asList(fragments),Arrays.asList("玫瑰"));
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }

    @Override
    protected void initListener() {
        mBinding.backIv.setOnClickListener(v -> finish());
    }
}
