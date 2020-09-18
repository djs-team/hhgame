package com.deepsea.mua.voice.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.InviteOnmicroData;
import com.deepsea.mua.stub.entity.OnlinesBean;
import com.deepsea.mua.stub.entity.PageBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.FullServiceUsersAdapter;
import com.deepsea.mua.voice.adapter.FullServiceUsersForManyAdapter;
import com.deepsea.mua.voice.databinding.FragmentFullServiceManBinding;
import com.deepsea.mua.voice.dialog.MicManagerDialog;
import com.deepsea.mua.voice.utils.AppConstant;
import com.deepsea.mua.voice.utils.inter.OnManageListener;
import com.deepsea.mua.voice.viewmodel.FullServiceSortModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class SortRecentActiveForManyFragment extends BaseFragment<FragmentFullServiceManBinding> {
    private static OnManageListener mListener;
    private int canSelectMicroNum;
    private int onMicroCost;
    private String sex;

    public SortRecentActiveForManyFragment() {

    }

    public static SortRecentActiveForManyFragment newInstance(OnManageListener listener, int canSelectMicroNum,int onMicroCost,String sex) {
        mListener = listener;
        SortRecentActiveForManyFragment instance = new SortRecentActiveForManyFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putInt("canSelectMicroNum", canSelectMicroNum);
            bundle.putInt("onMicroCost", onMicroCost);
            bundle.putString("sex", sex);
            instance.setArguments(bundle);
        } else {
            bundle.putInt("canSelectMicroNum", canSelectMicroNum);
            bundle.putInt("onMicroCost", onMicroCost);
            bundle.putString("sex", sex);
        }
        return instance;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_full_service_man;
    }

    private FullServiceSortModel mViewModel;
    @Inject
    ViewModelFactory mModelFactory;

    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(FullServiceSortModel.class);
        canSelectMicroNum = mBundle.getInt("canSelectMicroNum");
        onMicroCost = mBundle.getInt("onMicroCost");
        sex=mBundle.getString("sex");
        ViewBindUtils.setText(mBinding.tvCostNum, "(" + onMicroCost + "朵玫瑰)");
        initRecyclerView();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.refresh(sex).observe(this, new BaseObserver<OnlinesBean>() {
                @Override
                public void onSuccess(OnlinesBean result) {
                    mBinding.refreshLayout.finishRefresh();
                    if (result != null) {
                        mAdapter.setNewData(result.getList());
                        PageBean pageInfo = result.getPageInfo();
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
            mViewModel.loadMore(sex).observe(this, new BaseObserver<OnlinesBean>() {
                @Override
                public void onSuccess(OnlinesBean result) {
                    mBinding.refreshLayout.finishLoadMore();
                    if (result != null) {
                        mAdapter.addData(result.getList());
                        PageBean pageInfo = result.getPageInfo();
                        mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
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

    FullServiceUsersForManyAdapter mAdapter;

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mBinding.recyclerView.getItemAnimator() != null) {
            mBinding.recyclerView.getItemAnimator().setChangeDuration(0);
        }
        mAdapter = new FullServiceUsersForManyAdapter(mContext, canSelectMicroNum);
        mAdapter.setOnSelectMicUserListener(new FullServiceUsersForManyAdapter.OnSelectMicUserListener() {
            @Override
            public void onDisableSelectListener(int maxNum) {
                ToastUtils.showToast("最多只能邀请" + maxNum + "人");
            }
        });
        mAdapter.setManager(true);
        mBinding.recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.llInviteFree, o -> {
            if (mListener != null) {
                invite(1);
            }
        });
        subscribeClick(mBinding.llInviteCost, o -> {
            if (mListener != null) {
                invite(2);
            }
        });
    }
    private void invite(int free){
        List<InviteOnmicroData> list = mAdapter.getSelectData();
        if (list != null && list.size() > 0) {
            String ids = "";
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    ids = String.valueOf(list.get(i).getId());
                } else {
                    ids += ("," + list.get(i).getId());
                }
            }
            mListener.onInviteClick(ids, free, AppConstant.getInstance().getTabType());
        } else {
            toastShort("邀请人不能为空");
        }
    }
}
