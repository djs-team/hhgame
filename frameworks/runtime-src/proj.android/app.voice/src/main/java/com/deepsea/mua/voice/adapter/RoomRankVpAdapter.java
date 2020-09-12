package com.deepsea.mua.voice.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.voice.fragment.RoomRankFragment;

/**
 * Created by JUN on 2019/4/10
 */
public class RoomRankVpAdapter extends FragmentPagerAdapter {

    private SparseArray<BaseFragment> fragments;
    private final String[] RANK_TITLES;
    private final String[] RANK_TYPES;

    private String mRoomId;

    public RoomRankVpAdapter(FragmentManager fm, String roomId) {
        super(fm);
        RANK_TITLES = new String[]{"财富", "魅力"};
        RANK_TYPES = new String[]{"1", "2"};
        fragments = new SparseArray<>(RANK_TITLES.length);

        this.mRoomId = roomId;
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = fragments.get(position);
        if (fragment == null) {
            fragment = RoomRankFragment.newInstance(RANK_TYPES[position], mRoomId);
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
