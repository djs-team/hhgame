package com.deepsea.mua.stub.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import java.util.List;

/**
 * Created by JUN on 2019/5/5
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mData;
    private List<String> mTitles;
    private SparseArray<Fragment> fragments;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new SparseArray<>();
    }

    public void setNewData(List<Fragment> list) {
        this.mData = list;
        notifyDataSetChanged();
    }

    public void setNewData(List<Fragment> list, List<String> titles) {
        this.mData = list;
        this.mTitles = titles;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = fragments.get(position);
        if (fragment == null) {
            fragment = mData.get(position);
            fragments.put(position, fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles != null && position < mTitles.size() ? mTitles.get(position) : null;
    }
}
