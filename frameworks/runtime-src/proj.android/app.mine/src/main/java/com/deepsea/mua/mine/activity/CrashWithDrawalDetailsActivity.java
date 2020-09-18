package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.CashWithdrawalAdapter;
import com.deepsea.mua.mine.databinding.ActivityCrashWithdrawalDetailsBinding;
import com.deepsea.mua.mine.viewmodel.CashWithdrawalDetailsModel;
import com.deepsea.mua.stub.adapter.RecyclerAdapterWithHF;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.CashWListBean;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.TimeUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * @zu 提现记录
 */
public class CrashWithDrawalDetailsActivity extends BaseActivity<ActivityCrashWithdrawalDetailsBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private CashWithdrawalDetailsModel mViewModel;
    private CashWithdrawalAdapter mAdapter;
    private RecyclerAdapterWithHF mAdapterWithHF;
    private String sTime = "";
    private String eTime = "";
    private final int request_code_select_date = 1001;
    boolean isRedpackage;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crash_withdrawal_details;
    }

    @Override
    protected void initView() {
        isRedpackage = getIntent().getBooleanExtra("isRedpackage", false);
        eTime = TimeUtils.getTodayStr();
        sTime = TimeUtils.getYesTodayStr();
        ViewBindUtils.setText(mBinding.tvSearchDate, sTime + " — " + eTime);
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(CashWithdrawalDetailsModel.class);
        initRecyclerView();
        initRefreshLayout();
    }


    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.consTitlebar.getRightTv(), o -> {
            Intent intent = new Intent(mContext, QueryDateDetailsActivity.class);
            intent.putExtra("sTime", sTime);
            intent.putExtra("eTime", eTime);
            intent.putExtra("isRedpackage", isRedpackage);
            startActivityForResult(intent, request_code_select_date);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && resultCode == Activity.RESULT_OK) {
            if (requestCode == request_code_select_date) {
                String startDate = data.getStringExtra("startDate");
                String endDate = data.getStringExtra("endDate");
                this.sTime = startDate;
                this.eTime = endDate;
                ViewBindUtils.setText(mBinding.tvSearchDate, startDate + " — " + endDate);
                mBinding.refreshLayout.autoRefresh();

            }
        }
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    private void initRecyclerView() {
        mAdapter = new CashWithdrawalAdapter(mContext);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        mAdapterWithHF.setManagerType(RecyclerAdapterWithHF.TYPE_MANAGER_LINEAR);
        mAdapterWithHF.setRecycleView(mBinding.recyclerView);
        mBinding.recyclerView.setAdapter(mAdapterWithHF);
        addHeader();
    }

    private TextView tv_balance;

    private void addHeader() {
        View view = View.inflate(mContext, R.layout.item_cash_withdrawal_account, null);
        tv_balance = view.findViewById(R.id.tv_balance);
        mAdapterWithHF.addHeader(view);
        mAdapterWithHF.showHeaderView(true);
    }


    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.refresh(sTime, eTime, isRedpackage).observe(this, new BaseObserver<CashWListBean>() {
                @Override
                public void onSuccess(CashWListBean result) {
                    if (result != null) {
                        tv_balance.setText("￥" + FormatUtils.subZeroAndDot(String.valueOf(result.getTotal_price())));
                        CashWListBean.PageinfoBean pageInfo = result.getPageinfo();
                        mAdapter.setNewData(result.getCash_list());
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
            mViewModel.loadMore(sTime, eTime, isRedpackage).observe(this, new BaseObserver<CashWListBean>() {
                @Override
                public void onSuccess(CashWListBean result) {
                    if (result != null) {
                        tv_balance.setText("￥" + result.getTotal_price());
                        CashWListBean.PageinfoBean pageInfo = result.getPageinfo();
                        mAdapter.addData(result.getCash_list());
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
}
