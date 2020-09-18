package com.deepsea.mua.voice.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.voice.fragment.FansRankFragment;

/**
 * Created by JUN on 2019/4/10
 */
public class FansRankVpAdapter extends FragmentPagerAdapter {

    private SparseArray<BaseFragment> fragments;
    private final String[] RANK_TITLES;
    private final String[] RANK_STATUS;

    public FansRankVpAdapter(FragmentManager fm) {
        super(fm);
        RANK_TITLES = new String[]{"日榜", "周榜", "月榜"};
        RANK_STATUS = new String[]{"1", "2", "3"};
        fragments = new SparseArray<>(RANK_TITLES.length);
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = fragments.get(position);
        if (fragment == null) {
            fragment = FansRankFragment.newInstance(RANK_STATUS[position]);
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
