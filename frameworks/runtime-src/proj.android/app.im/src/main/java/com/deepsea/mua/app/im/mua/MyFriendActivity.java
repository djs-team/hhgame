package com.deepsea.mua.app.im.mua;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.adapter.FriendAddAdapter;
import com.deepsea.mua.app.im.databinding.ActivityFriendAddBinding;
import com.deepsea.mua.app.im.databinding.ActivityMyFriendsBinding;
import com.deepsea.mua.app.im.viewmodel.AddFriendViewModel;
import com.deepsea.mua.app.im.viewmodel.FriendListViewModel;
import com.deepsea.mua.app.im.viewmodel.FriendRequestViewModel;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AddFriendWithNoGiftDialog;
import com.deepsea.mua.stub.entity.MessageNumVo;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.hyphenate.chat.EMClient;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


/**
 * Created by JUN on 2019/5/7
 */
public class MyFriendActivity extends BaseActivity<ActivityMyFriendsBinding> {

    @Inject
    ViewModelFactory mFactory;
    private FriendListViewModel mViewModel;


    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_friends;
    }

    MyFriendsAdapter mAdapter;

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mFactory).get(FriendListViewModel.class);

        mAdapter = new MyFriendsAdapter(getSupportFragmentManager());
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.viewPager.setNoScroll(true);
        mBinding.viewPager.setOffscreenPageLimit(3);
        mBinding.myfriendTab.setSelected(true);
        getMessageNum();

    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.rlTabFriend, o -> {
            mBinding.viewPager.setCurrentItem(0);
            mBinding.myfriendTab.setSelected(true);
            mBinding.friendapplyTab.setSelected(false);
            mBinding.myapplyTab.setSelected(false);
            mBinding.viewMyfriend.setVisibility(View.VISIBLE);
            mBinding.viewFriendapply.setVisibility(View.GONE);
            mBinding.viewMyapply.setVisibility(View.GONE);
        });
        subscribeClick(mBinding.rlTabFriendapply, o -> {
            mBinding.viewPager.setCurrentItem(1);
            mBinding.myfriendTab.setSelected(false);
            mBinding.friendapplyTab.setSelected(true);
            mBinding.myapplyTab.setSelected(false);
            mBinding.viewMyfriend.setVisibility(View.GONE);
            mBinding.viewFriendapply.setVisibility(View.VISIBLE);
            mBinding.viewMyapply.setVisibility(View.GONE);
            mBinding.tvFriendapplyUnread.setVisibility(View.GONE);


        });
        subscribeClick(mBinding.rlTabMyapply, o -> {
            mBinding.viewPager.setCurrentItem(2);
            mBinding.myfriendTab.setSelected(false);
            mBinding.friendapplyTab.setSelected(false);
            mBinding.myapplyTab.setSelected(true);
            mBinding.viewMyfriend.setVisibility(View.GONE);
            mBinding.viewFriendapply.setVisibility(View.GONE);
            mBinding.viewMyapply.setVisibility(View.VISIBLE);
            mBinding.tvMyapplyUnread.setVisibility(View.GONE);

        });
    }

    //未读消息数量
    private void getMessageNum() {
        mViewModel.getMessageNum().observe(this, new BaseObserver<MessageNumVo>() {
            @Override
            public void onSuccess(MessageNumVo result) {
                if (result != null) {
                    setUnreadMsgCount(result);
                }
            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
            }
        });
    }

    public void setUnreadMsgCount(MessageNumVo result) {
        int applyUnreadNum = TextUtils.isEmpty(result.getApply_my()) ? 0 : Integer.valueOf(result.getApply_my());
        int myApplyUnreadNum = TextUtils.isEmpty(result.getMy_apply()) ? 0 : Integer.valueOf(result.getMy_apply());
        ViewBindUtils.setVisible(mBinding.tvFriendapplyUnread, applyUnreadNum > 0);
        ViewBindUtils.setText(mBinding.tvFriendapplyUnread, String.valueOf(applyUnreadNum));

        ViewBindUtils.setVisible(mBinding.tvMyapplyUnread, myApplyUnreadNum > 0);
        ViewBindUtils.setText(mBinding.tvMyapplyUnread, String.valueOf(myApplyUnreadNum));

    }

}
