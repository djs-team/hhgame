package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.IncomeAdapter;
import com.deepsea.mua.mine.adapter.IncomeRedpackageAdapter;
import com.deepsea.mua.mine.databinding.ActivityIncomeDetailsBinding;
import com.deepsea.mua.mine.databinding.ActivityRedpackageIncomeDetailsBinding;
import com.deepsea.mua.mine.viewmodel.IncomeDetailsModel;
import com.deepsea.mua.stub.adapter.RecyclerAdapterWithHF;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.IncomeListBean;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.TimeUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * @zu 收入记录
 */
public class IncomeRedpackageDetailsActivity extends BaseActivity<ActivityRedpackageIncomeDetailsBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private IncomeDetailsModel mViewModel;
    private IncomeRedpackageAdapter mAdapter;
    private RecyclerAdapterWithHF mAdapterWithHF;
    private String sTime = "";
    private String eTime = "";
    private final int request_code_select_date = 1001;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_redpackage_income_details;
    }

    @Override
    protected void initView() {
        eTime = TimeUtils.getTodayStr();
        sTime = TimeUtils.getYesTodayStr();
        ViewBindUtils.setText(mBinding.tvSearchDate, sTime + " — " + eTime);
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(IncomeDetailsModel.class);
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
            startActivityForResult(intent, request_code_select_date);
        });
        subscribeClick(rl_eye, o -> {
            ivEye.setSelected(!ivEye.isSelected());
            tv_balance.setText(ivEye.isSelected() ? totalPriceStr : "******");
            SharedPrefrencesUtil.saveData(mContext, "redPackageAccountShow", "redPackageAccountShow", ivEye.isSelected());
        });
    }

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }


    private void initRecyclerView() {
        mAdapter = new IncomeRedpackageAdapter(mContext);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        mAdapterWithHF.setManagerType(RecyclerAdapterWithHF.TYPE_MANAGER_LINEAR);
        mAdapterWithHF.setRecycleView(mBinding.recyclerView);
        mBinding.recyclerView.setAdapter(mAdapterWithHF);
        addHeader();
    }

    private TextView tv_balance;
    private RelativeLayout rl_eye;
    private ImageView ivEye;
    String totalPriceStr = "";

    private void addHeader() {
        View view = View.inflate(mContext, R.layout.item_income_redpackage_account, null);
        tv_balance = view.findViewById(R.id.tv_balance);
        rl_eye = view.findViewById(R.id.rl_eye);
        ivEye = view.findViewById(R.id.iv_eye);
        mAdapterWithHF.addHeader(view);
        mAdapterWithHF.showHeaderView(true);
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

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.redPackageRefresh(sTime, eTime).observe(this, new BaseObserver<IncomeListBean>() {
                @Override
                public void onSuccess(IncomeListBean result) {
                    if (result != null) {
                        totalPriceStr = "￥" + FormatUtils.subZeroAndDot(String.valueOf(result.getTotal_price()));
                        tv_balance.setText(totalPriceStr);

                        boolean isAccountShow = SharedPrefrencesUtil.getData(mContext, "redPackageAccountShow", "redPackageAccountShow", true);
                        ivEye.setSelected(isAccountShow);
                        tv_balance.setText(ivEye.isSelected() ? totalPriceStr : "******");
                        IncomeListBean.PageinfoBean pageInfo = result.getPageinfo();
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
            mViewModel.redPackageLoadMore(sTime, eTime).observe(this, new BaseObserver<IncomeListBean>() {
                @Override
                public void onSuccess(IncomeListBean result) {
                    if (result != null) {
                        totalPriceStr = "￥" + result.getTotal_price();
                        tv_balance.setText(totalPriceStr);
                        IncomeListBean.PageinfoBean pageInfo = result.getPageinfo();
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
}
