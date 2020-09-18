package com.deepsea.mua.voice.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.entity.RoomModes;
import com.deepsea.mua.voice.fragment.RoomFragment;

import java.util.List;

/**
 * Created by JUN on 2019/4/17
 */
public class VoiceVpAdapter extends FragmentPagerAdapter {

    private SparseArray<BaseFragment> fragments;
    private List<RoomModes.RoomModeBean> mData;

    public VoiceVpAdapter(FragmentManager fm) {
        super(fm);
        fragments = new SparseArray<>();
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = fragments.get(position);
        if (fragment == null) {
            if (mData != null && position < mData.size()) {
                fragment = RoomFragment.newInstance(mData.get(position).getMode_id());
                fragments.put(position, fragment);
            }
        }
        return fragment;
    }

    public void setNewData(List<RoomModes.RoomModeBean> list) {
        mData = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mData != null && position < mData.size()) {
            return mData.get(position).getRoom_mode();
        }
        return "";
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
