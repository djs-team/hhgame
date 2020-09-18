package com.deepsea.mua.app.im.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.deepsea.mua.app.im.mua.GiftKnapsackFragment;
import com.deepsea.mua.app.im.mua.GiftPanelFragment;
import com.deepsea.mua.stub.base.BaseFragment;

/**
 * Created by JUN on 2019/4/10
 */
public class FriendAddAdapter extends FragmentPagerAdapter {

    private BaseFragment[] fragments = null;
    private final String[] RANK_TITLES;
    public interface  OnFriendAddListener{
        void  onSendRequest();
    }

    public FriendAddAdapter(FragmentManager fm, String touid, String toUserName,OnFriendAddListener mListener) {
        super(fm);
        RANK_TITLES = new String[]{"购买", "背包"};
        fragments = new BaseFragment[]{GiftPanelFragment.newInstance(touid,toUserName,mListener),GiftKnapsackFragment.newInstance(touid,toUserName,mListener)};
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
