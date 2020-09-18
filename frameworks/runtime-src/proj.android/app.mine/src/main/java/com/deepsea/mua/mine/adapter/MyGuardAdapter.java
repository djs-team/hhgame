package com.deepsea.mua.mine.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.deepsea.mua.mine.fragment.GuardInfoFragment;
import com.deepsea.mua.stub.base.BaseFragment;

/**
 * Created by JUN on 2019/4/10
 */
public class MyGuardAdapter extends FragmentPagerAdapter {

    private BaseFragment[] fragments = null;
    private final String[] RANK_TITLES;
    private final String[] RANK_STATUS;

    public MyGuardAdapter(FragmentManager fm) {
        super(fm);
        RANK_TITLES = new String[]{"我守护的人", "守护我的人"};
        RANK_STATUS = new String[]{"1", "2"};
        fragments = new BaseFragment[]{ GuardInfoFragment.newInstance("1"),  GuardInfoFragment.newInstance("2")};
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = fragments[position];
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
