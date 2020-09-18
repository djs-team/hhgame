package com.deepsea.mua.voice.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.voice.dialog.FullServiceUserDialog;
import com.deepsea.mua.voice.fragment.FullServiceManFragment;
import com.deepsea.mua.voice.fragment.FullServiceWomenFragment;

/**
 * Created by JUN on 2019/4/10
 */
public class FullServiceSortAdapter extends FragmentPagerAdapter {

    private BaseFragment[] fragments = null;
    private final String[] RANK_TITLES;

    public FullServiceSortAdapter(FragmentManager fm, FullServiceUserDialog.OnMicroListener mListener,int hongId,String roomId) {
        super(fm);
        RANK_TITLES = new String[]{"男嘉宾", "女嘉宾"};
        fragments = new BaseFragment[]{FullServiceManFragment.newInstance(mListener, hongId, roomId), FullServiceWomenFragment.newInstance(mListener, hongId, roomId)};
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
