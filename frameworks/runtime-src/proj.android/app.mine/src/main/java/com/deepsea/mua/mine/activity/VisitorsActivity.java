package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.VisitorsAdapter;
import com.deepsea.mua.mine.databinding.ActivityVisitorsBinding;
import com.deepsea.mua.mine.viewmodel.VisitorsViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.VisitorBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
@Route(path = ArouterConst.PAGE_ME_VISITORS)

public class VisitorsActivity extends BaseActivity<ActivityVisitorsBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private VisitorsViewModel mViewModel;

    private VisitorsAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_visitors;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(VisitorsViewModel.class);
        initRecyclerView();
        initRefreshLayout();
    }

    private void initRecyclerView() {
        mAdapter = new VisitorsAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
            VisitorBean.HistoryListBean item = mAdapter.getItem(position);
            PageJumpUtils.jumpToProfile(item.getUser_id());
        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.refresh().observe(this, new BaseObserver<VisitorBean>() {
                @Override
                public void onSuccess(VisitorBean result) {
                    if (result != null) {
                        VisitorBean.PageInfo pageInfo = result.getPageInfo();
                        mAdapter.setNewData(result.getHistory_list());
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
            mViewModel.loadMore().observe(this, new BaseObserver<VisitorBean>() {
                @Override
                public void onSuccess(VisitorBean result) {
                    if (result != null) {
                        VisitorBean.PageInfo pageInfo = result.getPageInfo();
                        mAdapter.addData(result.getHistory_list());
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
