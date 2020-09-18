package com.deepsea.mua.voice.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.OnlineUser;
import com.deepsea.mua.voice.dialog.MicManagerDialog;
import com.deepsea.mua.voice.dialog.SongManagerDialog;
import com.deepsea.mua.voice.fragment.RecentActiveFragment;
import com.deepsea.mua.voice.fragment.SongApointmentFragment;
import com.deepsea.mua.voice.fragment.SongBanchangFragment;
import com.deepsea.mua.voice.fragment.SongOriginalFragment;
import com.deepsea.mua.voice.fragment.SongPlayingFragment;
import com.deepsea.mua.voice.fragment.SongRankFragment;
import com.deepsea.mua.voice.fragment.SortApplyMicFragment;
import com.deepsea.mua.voice.fragment.SortVisitorInRoomFragment;

import java.util.List;

/**
 * Created by JUN on 2019/4/10
 */
public class SongManageAdapter extends FragmentPagerAdapter {

    private BaseFragment[] fragments = null;
    private final String[] RANK_TITLES;


    public SongManageAdapter(FragmentManager fm, SongManagerDialog.OnManageListener manageListener, String mRoomId, SongManagerDialog.onViewPagerHeightListener pagerHeightListener) {
        super(fm);
        RANK_TITLES = new String[]{"原唱", "伴唱", "正在播放", "预约列表"};
        fragments = new BaseFragment[]{SongOriginalFragment.newInstance(1), SongBanchangFragment.newInstance(2), SongPlayingFragment.newInstance(manageListener, pagerHeightListener), SongApointmentFragment.newInstance()};
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
