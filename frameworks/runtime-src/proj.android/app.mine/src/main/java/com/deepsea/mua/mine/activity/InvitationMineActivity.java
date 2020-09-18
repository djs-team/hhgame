package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.InvitationMineAdapter;
import com.deepsea.mua.mine.databinding.ActivityInvitationMineBinding;
import com.deepsea.mua.mine.viewmodel.InvitationMineViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.ApplyFriendBean;
import com.deepsea.mua.stub.entity.ApplyFriendListBean;
import com.deepsea.mua.stub.entity.FollowFanBean;
import com.deepsea.mua.stub.entity.InviteListBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * @zu 我的邀请
 */
@Route(path = ArouterConst.PAGE_ME_MINE_INVITOR)
public class InvitationMineActivity extends BaseActivity<ActivityInvitationMineBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private InvitationMineViewModel mViewModel;

    private InvitationMineAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invitation_mine;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(InvitationMineViewModel.class);
        initRecyclerView();
        initRefreshLayout();
    }

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.consTitlebar.getRightTv(), o -> {
            Intent intent = new Intent(mContext, InvitationCodeActivity.class);
            startActivity(intent);
        });
    }

    private void initRecyclerView() {
        mAdapter = new InvitationMineAdapter(mContext);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);

    }


    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.refresh().observe(this, new BaseObserver<InviteListBean>() {
                @Override
                public void onSuccess(InviteListBean result) {
                    if (result != null) {
                        Log.d("friend", "refresh-success");
                        InviteListBean.PageInfoBean pageInfo = result.getPageInfo();
                        mAdapter.setNewData(result.getList());
                        mBinding.refreshLayout.finishRefresh();
                        mBinding.refreshLayout.setEnableLoadMore(Integer.valueOf(pageInfo.getPage()) < pageInfo.getTotalPage());
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    Log.d("friend", "refresh-error" + code + ";" + msg);

                    toastShort(msg);
                    mBinding.refreshLayout.finishLoadMore();
                }
            });
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mViewModel.loadMore().observe(this, new BaseObserver<InviteListBean>() {
                @Override
                public void onSuccess(InviteListBean result) {
                    if (result != null) {
                        InviteListBean.PageInfoBean pageInfo = result.getPageInfo();
                        mAdapter.addData(result.getList());
                        mBinding.refreshLayout.finishLoadMore();
                        mBinding.refreshLayout.setEnableLoadMore(Integer.valueOf(pageInfo.getPage()) < pageInfo.getTotalPage());
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    toastShort(msg);
                    mBinding.refreshLayout.finishLoadMore();
                    mViewModel.page--;
                }
            });
        });
        mBinding.refreshLayout.autoRefresh();
    }

}
