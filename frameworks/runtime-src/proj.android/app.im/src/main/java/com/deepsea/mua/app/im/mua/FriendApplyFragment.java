package com.deepsea.mua.app.im.mua;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.FragmentFriendApplyBinding;
import com.deepsea.mua.app.im.viewmodel.FriendRequestViewModel;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.ApplyFriendListBean;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/3/22
 */
public class FriendApplyFragment extends BaseFragment<FragmentFriendApplyBinding> {
    @Inject
    ViewModelFactory mModelFactory;
    private FriendRequestViewModel mViewModel;
    private FriendApplyAdapter mAdapter;
    private String type = "";

    public static BaseFragment newInstance(String type) {
        FriendApplyFragment instance = new FriendApplyFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString("type", type);
            instance.setArguments(bundle);
        } else {
            bundle.putString("type", type);
        }
        return instance;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend_apply;
    }

    @Override
    protected void initView(View view) {

        mViewModel = ViewModelProviders.of(this, mModelFactory).get(FriendRequestViewModel.class);
        type = mBundle.getString("type");
        initRecyclerView();
        initRefreshLayout();

    }


    private void initRecyclerView() {
        mAdapter = new FriendApplyAdapter(mContext);

        mAdapter.setOnItemClickListener((view, position) -> {

        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }




    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.refresh(type).observe(this, new BaseObserver<ApplyFriendListBean>() {
                @Override
                public void onSuccess(ApplyFriendListBean result) {
                    if (result != null) {
                        ApplyFriendListBean.PageinfoBean pageInfo = result.getPageInfo();
                        mAdapter.setNewData(result.getList());
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
            mViewModel.loadMore(type).observe(this, new BaseObserver<ApplyFriendListBean>() {
                @Override
                public void onSuccess(ApplyFriendListBean result) {
                    if (result != null) {
                        ApplyFriendListBean.PageinfoBean pageInfo = result.getPageInfo();
                        mAdapter.addData(result.getList());
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
