package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityBlueRoseExchangeRecordsBinding;
import com.deepsea.mua.mine.databinding.ItemExchangeBlueRoseReocrdsBinding;
import com.deepsea.mua.mine.viewmodel.WalletViewModel;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.adapter.RecyclerAdapterWithHF;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.ExchangeBlueRoseRecordListParam;
import com.deepsea.mua.stub.entity.ExchangeBuleRoseVo;
import com.deepsea.mua.stub.entity.socket.receive.BreakEggRecord;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

/**
 * @author zu
 * 兑换蓝玫瑰
 */

public class ExchangeBlueRoseExchangeActivity extends BaseActivity<ActivityBlueRoseExchangeRecordsBinding> {
    List<BreakEggRecord> data;

    private ExchangeRecordsAdapter mAdapter;

    private RecyclerAdapterWithHF mAdapterWithHF;
    @Inject
    ViewModelFactory mModelFactory;
    private WalletViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_blue_rose_exchange_records;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(WalletViewModel.class);
        initRecyclerView();
        initRefreshLayout();
    }

    private void initRecyclerView() {
        mAdapter = new ExchangeRecordsAdapter(mContext);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        mAdapterWithHF.setManagerType(RecyclerAdapterWithHF.TYPE_MANAGER_LINEAR);
        mAdapterWithHF.setRecycleView(mBinding.recyclerView);
        mBinding.recyclerView.setAdapter(mAdapterWithHF);
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
        });
        View view = View.inflate(mContext, R.layout.item_exchange_blue_rose_reocrds_head, null);
        mAdapterWithHF.addHeader(view);
        mAdapterWithHF.showHeaderView(true);
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setFooterAccentColor(R.color.white);
        mBinding.refreshLayout.setMaterialHeader();
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.refreshBlueExchangeInfo().observe(this, new BaseObserver<ExchangeBlueRoseRecordListParam>() {
                @Override
                public void onSuccess(ExchangeBlueRoseRecordListParam result) {
                    if (result != null) {
                        ExchangeBlueRoseRecordListParam.PageinfoBean pageInfo = result.getPageinfo();
                        mAdapter.setNewData(result.getList());
                        mBinding.refreshLayout.finishRefresh();
                        mBinding.refreshLayout.setEnableLoadMore(Integer.valueOf(pageInfo.getPage()) < pageInfo.getTotalPage());
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
            mViewModel.loadModeBlueExchangeInfo().observe(this, new BaseObserver<ExchangeBlueRoseRecordListParam>() {
                @Override
                public void onSuccess(ExchangeBlueRoseRecordListParam result) {
                    if (result != null) {
                        ExchangeBlueRoseRecordListParam.PageinfoBean pageInfo = result.getPageinfo();
                        mAdapter.addData(result.getList());
                        mBinding.refreshLayout.finishLoadMore();
                        mBinding.refreshLayout.setEnableLoadMore(Integer.valueOf(pageInfo.getPage()) < pageInfo.getTotalPage());
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

    private static class ExchangeRecordsAdapter extends BaseBindingAdapter<ExchangeBuleRoseVo, ItemExchangeBlueRoseReocrdsBinding> {

        public ExchangeRecordsAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.item_exchange_blue_rose_reocrds;
        }

        @Override
        protected void bind(BindingViewHolder<ItemExchangeBlueRoseReocrdsBinding> holder, ExchangeBuleRoseVo item) {
            ViewBindUtils.setText(holder.binding.timeTv, item.getTime());
            holder.binding.giftTv.setText(item.getGift_name());
            holder.binding.numTv.setText(item.getGift_num() + "个");
            holder.binding.roseNumTv.setText(item.getValue() + "枝");
        }
    }

}
