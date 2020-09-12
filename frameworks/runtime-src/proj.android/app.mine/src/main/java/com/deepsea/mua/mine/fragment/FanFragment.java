package com.deepsea.mua.mine.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.FansAdapter;
import com.deepsea.mua.mine.databinding.FragmentFanBinding;
import com.deepsea.mua.mine.viewmodel.FollowFanViewModel;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.FollowFanBean;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class FanFragment extends BaseFragment<FragmentFanBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private FollowFanViewModel mViewModel;

    private FansAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_fan;
    }

    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(FollowFanViewModel.class);
        initRecyclerView();
        initRefreshLayout();
    }

    private void initRecyclerView() {
        mAdapter = new FansAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
            FollowFanBean.UserInfoBean item = mAdapter.getItem(position);
            PageJumpUtils.jumpToProfile(item.getUserid());
        });
        mAdapter.setOnFollowListener((uid, isFollow, pos) -> {
            String type = isFollow ? "2" : "1";
            mViewModel.attention_member(uid, type).observe(this, new ProgressObserver<BaseApiResult>(mContext) {
                @Override
                public void onSuccess(BaseApiResult result) {
                    if (result != null) {
                        toastShort(result.getDesc());
                        FollowFanBean.UserInfoBean item = mAdapter.getItem(pos);
                        item.setStatus(type);
                        mAdapter.notifyItemChanged(pos);
                    }
                }
            });
        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.refresh("2").observe(this, new BaseObserver<FollowFanBean>() {
                @Override
                public void onSuccess(FollowFanBean result) {
                    if (result != null) {
                        FollowFanBean.PageinfoBean pageInfo = result.getPageinfo();
                        mAdapter.setNewData(result.getUser_info());
                        mBinding.refreshLayout.finishRefresh();
                        mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    toastShort(msg);
                    mBinding.refreshLayout.finishRefresh();
                }
            });
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mViewModel.loadMore("2").observe(this, new BaseObserver<FollowFanBean>() {
                @Override
                public void onSuccess(FollowFanBean result) {
                    if (result != null) {
                        FollowFanBean.PageinfoBean pageInfo = result.getPageinfo();
                        mAdapter.addData(result.getUser_info());
                        mBinding.refreshLayout.finishLoadMore();
                        mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    toastShort(msg);
                    mBinding.refreshLayout.finishLoadMore();
                    mViewModel.pageNumber--;
                }
            });
        });
        mBinding.refreshLayout.autoRefresh();
    }
}
