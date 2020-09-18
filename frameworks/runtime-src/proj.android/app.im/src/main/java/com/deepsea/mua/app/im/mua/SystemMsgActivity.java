package com.deepsea.mua.app.im.mua;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.deepsea.mua.app.im.Constant;
import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ActivitySystemMsgBinding;
import com.deepsea.mua.im.domain.EaseUser;
import com.deepsea.mua.im.utils.EaseUserUtils;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.client.hyphenate.IEMMessageListener;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/8/27
 */
public class SystemMsgActivity extends BaseActivity<ActivitySystemMsgBinding> {

    @Inject
    ViewModelFactory mFactory;
    private SystemMsgViewModel mViewModel;

    private String username;

    private SystemMsgAdapter mAdapter;


    private EMConversation mEMConversation;

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {
        username = savedInstanceState.getString(Constant.EXTRA_USER_ID);
    }

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        username = intent.getStringExtra(Constant.EXTRA_USER_ID);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constant.EXTRA_USER_ID, username);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_msg;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mFactory).get(SystemMsgViewModel.class);
        mEMConversation = EMClient.getInstance().chatManager().getConversation(username);
        EMClient.getInstance().chatManager().addMessageListener(mEMMessageListener);
        initTitle();
        initRecyclerView();
        initRefreshLayout();
    }

    private void initTitle() {
        EaseUser user = EaseUserUtils.getUserInfo(username);
        if (user != null) {
            mBinding.titleBar.setTitle(user.getNickname());
        }
    }

    private void initRecyclerView() {
        mAdapter = new SystemMsgAdapter(mContext);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter.setOnItemLongClickListener((view, position) -> {
            showConversationPop(view, position);
            return false;
        });
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> refresh());
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> loadMore());
        mBinding.refreshLayout.autoRefresh();
    }

    private void refresh() {
        mViewModel.refresh(mEMConversation).observe(this, list -> {
            mEMConversation.markAllMessagesAsRead();
            mAdapter.setNewData(list);
            mBinding.refreshLayout.finishRefresh();
            mBinding.refreshLayout.setEnableLoadMore(list != null && list.size() == mViewModel.getPageSize());
        });
    }

    private void loadMore() {
        mViewModel.loadMore(mEMConversation).observe(this, list -> {
            mEMConversation.markAllMessagesAsRead();
            mAdapter.addData(list);
            mBinding.refreshLayout.finishLoadMore();
            mBinding.refreshLayout.setEnableLoadMore(list != null && list.size() == mViewModel.getPageSize());
        });
    }

    private void showConversationPop(View anchor, int position) {

    }

    private EMMessageListener mEMMessageListener = new IEMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            for (EMMessage message : list) {
                String from = message.getFrom();

                if (from.equals(username) || message.getTo().equals(username) || message.conversationId().equals(username)) {
                    refresh();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        EMClient.getInstance().chatManager().removeMessageListener(mEMMessageListener);
        super.onDestroy();
    }
}
