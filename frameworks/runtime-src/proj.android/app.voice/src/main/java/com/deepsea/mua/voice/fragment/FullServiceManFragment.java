package com.deepsea.mua.voice.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.OnlinesBean;
import com.deepsea.mua.stub.entity.PageBean;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.FullServiceUsersAdapter;
import com.deepsea.mua.voice.databinding.FragmentFullServiceManBinding;
import com.deepsea.mua.voice.dialog.FullServiceUserDetailDialog;
import com.deepsea.mua.voice.dialog.FullServiceUserDialog;
import com.deepsea.mua.voice.viewmodel.FullServiceSortModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class FullServiceManFragment extends BaseFragment<FragmentFullServiceManBinding> {
    private static FullServiceUserDialog.OnMicroListener mListener;

    public FullServiceManFragment() {
    }

    private String roomId;
    private int hongId;

    public static FullServiceManFragment newInstance(FullServiceUserDialog.OnMicroListener listener, int hongId, String roomId) {
        mListener = listener;
        FullServiceManFragment instance = new FullServiceManFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString("roomId", roomId);
            bundle.putInt("hongId", hongId);
            instance.setArguments(bundle);
        } else {
            bundle.putString("roomId", roomId);
            bundle.putInt("hongId", hongId);
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
        roomId = mBundle.getString("roomId");
        hongId = mBundle.getInt("hongId");
        initRecyclerView();
        initRefreshLayout();
    }


    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.refresh("1").observe(this, new BaseObserver<OnlinesBean>() {
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
            mViewModel.loadMore("1").observe(this, new BaseObserver<OnlinesBean>() {
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
    FullServiceUsersAdapter mAdapter;

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mBinding.recyclerView.getItemAnimator() != null) {
            mBinding.recyclerView.getItemAnimator().setChangeDuration(0);
        }
        mAdapter = new FullServiceUsersAdapter(mContext);
        mAdapter.setManager(true);
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setOnMicroListener(new FullServiceUsersAdapter.OnMicroListener() {
            @Override
            public void ItemClick(int position, OnlinesBean.NearbyBean bean) {
                showFullServiceUserDetail(bean);
            }
        });
    }

    FullServiceUserDetailDialog fullServiceUserDetailDialog;

    private void showFullServiceUserDetail(OnlinesBean.NearbyBean bean) {
        fullServiceUserDetailDialog = new FullServiceUserDetailDialog(mContext);
        fullServiceUserDetailDialog.setContent(bean);
        fullServiceUserDetailDialog.setButton(new FullServiceUserDetailDialog.OnClickListener() {
            @Override
            public void onInviteClick(String uid) {
                if (mListener != null) {
                    mListener.onInviteClick(uid);
                }
            }

            @Override
            public void sendGift(String uid) {

            }

            @Override
            public void addFriend(String uid) {
//                ToastUtils.showToast("加好友");

            }
        });
        fullServiceUserDetailDialog.show();

    }


}
