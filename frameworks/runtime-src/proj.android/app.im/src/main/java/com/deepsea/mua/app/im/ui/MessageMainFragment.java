package com.deepsea.mua.app.im.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.adapter.MessageMainAdapter;
import com.deepsea.mua.app.im.databinding.FragmentMessageMainBinding;
import com.deepsea.mua.app.im.mua.MyFriendActivity;
import com.deepsea.mua.app.im.viewmodel.FriendListViewModel;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.client.hyphenate.IEMMessageListener;
import com.deepsea.mua.stub.entity.MessageNumVo;
import com.deepsea.mua.stub.utils.LogoutUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.HeartBeatEvent;
import com.deepsea.mua.stub.utils.eventbus.UpHxUnreadMsg;
import com.deepsea.mua.stub.utils.eventbus.UpdateUnreadMsgEvent;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

public class MessageMainFragment extends BaseFragment<FragmentMessageMainBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_message_main;
    }

    private MessageMainAdapter mAdapter;
    @Inject
    ViewModelFactory mFactory;
    private FriendListViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus(this);
        EMClient.getInstance().chatManager().removeMessageListener(mEMListener);
    }

    @Override
    protected void initView(View view) {
        registerEventBus(this);
        mViewModel = ViewModelProviders.of(this, mFactory).get(FriendListViewModel.class);
        EMClient.getInstance().chatManager().addMessageListener(mEMListener);
        mAdapter = new MessageMainAdapter(getChildFragmentManager());
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.viewPager.setNoScroll(true);
        mBinding.viewPager.setOffscreenPageLimit(2);
        mBinding.rlTabMsg.setOnClickListener(v -> {
            mBinding.viewPager.setCurrentItem(0);
            mBinding.msgTab.setSelected(true);
            mBinding.sysTab.setSelected(false);
            mBinding.viewMsg.setVisibility(View.VISIBLE);
            mBinding.viewSys.setVisibility(View.GONE);
        });
        mBinding.rlTabSys.setOnClickListener(v -> {
            mBinding.viewPager.setCurrentItem(1);
            mBinding.msgTab.setSelected(false);
            mBinding.sysTab.setSelected(true);
            mBinding.viewMsg.setVisibility(View.GONE);
            mBinding.viewSys.setVisibility(View.VISIBLE);
        });
        mBinding.msgTab.setSelected(true);
        mBinding.ivFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MyFriendActivity.class));
            }
        });
        mBinding.ivBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.ivBrush.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.brushType(mBinding.msgTab.isSelected() ? 1 : 2);
                    }
                });
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("onResume", "messageMan");
        if (!hidden) {
            getMessageNum();
        }
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

    private EMMessageListener mEMListener = new IEMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            if (!hidden) {
                getMessageNum();
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            if (!hidden) {
                getMessageNum();
            }
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {
            if (!hidden) {
                getMessageNum();
            }
        }
    };

    public void setUnreadMsgCount(MessageNumVo result) {
        int applyUnreadNum = TextUtils.isEmpty(result.getApply_my()) ? 0 : Integer.valueOf(result.getApply_my());
        int myApplyUnreadNum = TextUtils.isEmpty(result.getMy_apply()) ? 0 : Integer.valueOf(result.getMy_apply());
        int sysUnreadNum = TextUtils.isEmpty(result.getSystem_num()) ? 0 : Integer.valueOf(result.getSystem_num());
        int chatCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        ViewBindUtils.setVisible(mBinding.tvMsgUnread, chatCount > 0);
        ViewBindUtils.setText(mBinding.tvMsgUnread, String.valueOf(chatCount));
        ViewBindUtils.setVisible(mBinding.tvSysUnread, sysUnreadNum > 0);
        ViewBindUtils.setText(mBinding.tvSysUnread, String.valueOf(sysUnreadNum));
        int friendCount = applyUnreadNum + myApplyUnreadNum;
        ViewBindUtils.setVisible(mBinding.tvFriendUnread, friendCount > 0);
        ViewBindUtils.setText(mBinding.tvFriendUnread, String.valueOf(friendCount));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateUnreadMsgEvent msgTimeEvent) {
        getMessageNum();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HeartBeatEvent msgTimeEvent) {
        if (msgTimeEvent.getIsRequest() == 1) {
            getMessageNum();
        }
    }

    @Subscribe
    public void onEvent(UpHxUnreadMsg event) {
        getMessageNum();
    }

    private boolean hidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden) {
            getMessageNum();
        }
    }


}

