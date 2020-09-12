package com.deepsea.mua.voice.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.OnlineUser;
import com.deepsea.mua.voice.fragment.SortApplyMicForManyFragment;
import com.deepsea.mua.voice.fragment.SortFriendsFragment;
import com.deepsea.mua.voice.fragment.SortRecentActiveForManyFragment;
import com.deepsea.mua.voice.fragment.SortVisitorInRoomForManyFragment;
import com.deepsea.mua.voice.utils.inter.OnManageListener;

import java.util.List;

/**
 * Created by JUN on 2019/4/10
 */
public class MicManageForServenAdapter extends FragmentPagerAdapter {

    private BaseFragment[] fragments = null;
    private final String[] RANK_TITLES;


    public MicManageForServenAdapter(FragmentManager fm, OnManageListener mListener, List<MicroOrder> micMan, int pageNum, int canSelectMicroNum, int onMicroCost) {
        super(fm);
        RANK_TITLES = new String[]{"已申请", "房间内", "最近活跃", "好友"};
        fragments = new BaseFragment[]{SortApplyMicForManyFragment.newInstance(micMan, mListener), SortVisitorInRoomForManyFragment.newInstance(pageNum, onMicroCost, canSelectMicroNum, mListener), SortRecentActiveForManyFragment.newInstance(mListener, canSelectMicroNum, onMicroCost, ""), SortFriendsFragment.newInstance(mListener, canSelectMicroNum, onMicroCost, "")};
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = fragments[position];
        return fragment;
    }

    public void setApplyData(List<MicroOrder> data) {
        ((SortApplyMicForManyFragment) fragments[0]).setData(data);
    }

    public void setApplyTabType(int tabType) {
        ((SortApplyMicForManyFragment) fragments[0]).setTabType(tabType);
    }

    public void setVisitorInroomData(List<OnlineUser.UserBasis> data) {
        ((SortVisitorInRoomForManyFragment) fragments[1]).setData(data);
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
