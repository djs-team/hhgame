package com.deepsea.mua.app.im.adapter;

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
public class MessageMainAdapter extends FragmentPagerAdapter {

    private BaseFragment[] fragments = null;
    private final String[] RANK_TITLES;
    private final String[] RANK_STATUS;
    FriendMessageFragment friendMessageFragment = null;
//    SystemMsgFragment systemMsgFragment = null;

    public MessageMainAdapter(FragmentManager fm) {
        super(fm);
        RANK_TITLES = new String[]{"好友"};
        RANK_STATUS = new String[]{"1"};
        friendMessageFragment = FriendMessageFragment.newInstance();
//        systemMsgFragment = SystemMsgFragment.newInstance();
        fragments = new BaseFragment[]{friendMessageFragment};
    }

    public void brushType(int type) {
        if (type == 1) {
            friendMessageFragment.showBrushMsgDialog();
        }
//        else {
//            systemMsgFragment.showBrushMsgDialog();
//        }
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
