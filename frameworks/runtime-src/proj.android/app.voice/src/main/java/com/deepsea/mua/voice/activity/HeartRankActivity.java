package com.deepsea.mua.voice.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ActivityHeartRankBinding;
import com.deepsea.mua.voice.fragment.TotalRankFragment;

/**
 * Created by JUN on 2019/8/29
 * 心动榜
 */
public class HeartRankActivity extends BaseActivity<ActivityHeartRankBinding> {

    private HeartRankVpAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_heart_rank;
    }

    @Override
    protected void initView() {
        initViewPager();
    }

    private void initViewPager() {
        mAdapter = new HeartRankVpAdapter(getSupportFragmentManager());
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.viewPager.setNoScroll(true);
        mBinding.tabLayout.setViewPager(mBinding.viewPager);
    }


    private class HeartRankVpAdapter extends FragmentPagerAdapter {

        private SparseArray<BaseFragment> fragments;
        private final String[] RANK_TITLES;
        private final String[] RANK_STATUS;

        public HeartRankVpAdapter(FragmentManager fm) {
            super(fm);
            RANK_TITLES = new String[]{"日榜", "周榜", "月榜"};
            RANK_STATUS = new String[]{"1", "2", "3"};
            fragments = new SparseArray<>(RANK_TITLES.length);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = fragments.get(position);
            if (fragment == null) {
                fragment = TotalRankFragment.newInstance("2", RANK_STATUS[position]);
                fragments.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return RANK_TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return RANK_TITLES[position];
        }
    }
}
