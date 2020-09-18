package com.deepsea.mua.mine.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.WalletRecordAdapter;
import com.deepsea.mua.mine.databinding.FragmentMdBinding;
import com.deepsea.mua.mine.viewmodel.WalletRecordViewModel;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.MDRecord;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/6
 */
public class MDFragment extends BaseFragment<FragmentMdBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private WalletRecordViewModel mViewModel;

    private WalletRecordAdapter mAdapter;

    private String type;

    public static BaseFragment newInstance(String type) {
        MDFragment instance = new MDFragment();
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
        return R.layout.fragment_md;
    }

    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(WalletRecordViewModel.class);
        type = mBundle.getString("type");
        initRecyclerView();
        initRefreshLayout();
    }

    private void initRecyclerView() {
        mAdapter = new WalletRecordAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.refresh(type).observe(this, new BaseObserver<MDRecord>() {
                @Override
                public void onError(String msg, int code) {
                    super.onError(msg, code);
                    mBinding.refreshLayout.finishRefresh();
                }

                @Override
                public void onSuccess(MDRecord result) {
                    if (result != null) {
                        MDRecord.PageInfoBean pageInfo = result.getPageInfo();
                        mAdapter.setNewData(result.getMoney_record());
                        mBinding.refreshLayout.finishRefresh();
                        mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                    }
                }
            });
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mViewModel.loadMore(type).observe(this, new BaseObserver<MDRecord>() {
                @Override
                public void onError(String msg, int code) {
                    super.onError(msg, code);
                    mBinding.refreshLayout.finishLoadMore();
                    mViewModel.pageNumber--;
                }

                @Override
                public void onSuccess(MDRecord result) {
                    if (result != null) {
                        MDRecord.PageInfoBean pageInfo = result.getPageInfo();
                        mAdapter.addData(result.getMoney_record());
                        mBinding.refreshLayout.finishLoadMore();
                        mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                    }
                }
            });
        });
        mBinding.refreshLayout.autoRefresh();
    }
}