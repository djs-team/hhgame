package com.deepsea.mua.app.im.mua;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.deepsea.mua.app.im.mua.FriendMessageFragment;
import com.deepsea.mua.app.im.mua.GiftChatFragment;
import com.deepsea.mua.app.im.mua.SystemMsgFragment;
import com.deepsea.mua.stub.base.BaseFragment;

/**
 * Created by JUN on 2019/4/10
 */
public class MyFriendsAdapter extends FragmentPagerAdapter {

    private BaseFragment[] fragments = null;
    private final String[] RANK_TITLES;
    private final String[] RANK_STATUS;

    public MyFriendsAdapter(FragmentManager fm) {
        super(fm);
        RANK_TITLES = new String[]{"我的好友", "好友申请", "我的申请"};
        RANK_STATUS = new String[]{"1", "2","3"};
        fragments = new BaseFragment[]{new FriendMineFragment(), FriendApplyFragment.newInstance("1"),FriendApplyFragment.newInstance("2")};
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
