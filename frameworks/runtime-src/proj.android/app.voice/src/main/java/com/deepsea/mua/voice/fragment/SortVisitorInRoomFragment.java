package com.deepsea.mua.voice.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.entity.InviteOnMicroBean;
import com.deepsea.mua.stub.entity.InviteOnmicroData;
import com.deepsea.mua.stub.entity.OnlinesBean;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.OnlineUser;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.ClickEvent;
import com.deepsea.mua.stub.utils.eventbus.ClickEventType;
import com.deepsea.mua.stub.utils.eventbus.UpdateInRoomMemberEvent;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.FullServiceUsersAdapter;
import com.deepsea.mua.voice.adapter.MicroSortAdapter;
import com.deepsea.mua.voice.adapter.VisitorInRoomAdapter;
import com.deepsea.mua.voice.databinding.FragmentFullServiceManBinding;
import com.deepsea.mua.voice.dialog.FullServiceUserDetailDialog;
import com.deepsea.mua.voice.dialog.MicManagerDialog;
import com.deepsea.mua.voice.utils.AppConstant;
import com.deepsea.mua.voice.utils.inter.OnManageListener;
import com.deepsea.mua.voice.viewmodel.FullServiceSortModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2019/5/5
 */
public class SortVisitorInRoomFragment extends BaseFragment<FragmentFullServiceManBinding> {
    private static OnManageListener mListener;
    private int pageNum;
    private int canSelectMicroNum;
    private String sex;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus(this);
    }

    public SortVisitorInRoomFragment() {
    }

    private List<OnlineUser.UserBasis> mData = new ArrayList<>();

    public static SortVisitorInRoomFragment newInstance(int pageNum, int canSelectMicroNum, String sex, OnManageListener listener) {
        mListener = listener;
        SortVisitorInRoomFragment instance = new SortVisitorInRoomFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putInt("pageNum", pageNum);
            bundle.putInt("canSelectMicroNum", canSelectMicroNum);
            bundle.putString("sex", sex);

            instance.setArguments(bundle);
        } else {
            bundle.putInt("pageNum", pageNum);
            bundle.putInt("canSelectMicroNum", canSelectMicroNum);
            bundle.putString("sex", sex);
        }
        return instance;
    }

    public void setData(List<OnlineUser.UserBasis> data) {
        try {
            mBinding.refreshLayout.finishRefresh();
            if (data != null && data.size() > 0) {
                if (pageNum == 0) {
                    this.mData = data;
                    adapter.setNewData(mData);
                } else {
                    adapter.addData(data);
                }
            }
            adapter.notifyDataSetChanged();


        } catch (Exception e) {

        }

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
        pageNum = mBundle.getInt("pageNum");
        sex = mBundle.getString("sex");

        initRecyclerView();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                startObservable();
                if (mListener != null) {
                    mListener.onSortInRoomRefreshClick();
                }
            }
        });
        mBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                startObservable();
                if (mListener != null) {
                    mListener.onSortInRoomLoadMoreClick();
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateInRoomMemberEvent event) {
        adapter.setUpMicroData();
        adapter.notifyDataSetChanged();
    }

    VisitorInRoomAdapter adapter;
    private Subscription mSubscription;

    public void startObservable() {
        mSubscription = Observable.interval(0, 3000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(aLong -> {
                    mBinding.refreshLayout.finishLoadMore();
                    stopObservable();
                });
    }

    public void stopObservable() {
        if (mSubscription != null && (!mSubscription.isUnsubscribed())) {
            mSubscription.unsubscribe();
        }
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mBinding.recyclerView.getItemAnimator() != null) {
            mBinding.recyclerView.getItemAnimator().setChangeDuration(0);
        }

        adapter = new VisitorInRoomAdapter(mContext, canSelectMicroNum);
        adapter.setNewData(mData);
        adapter.setOnSelectMicUserListener(new VisitorInRoomAdapter.OnSelectMicUserListener() {
            @Override
            public void onDisableSelectListener(int maxNum) {
                ToastUtils.showToast("最多只能邀请" + maxNum + "人");
            }
        });
        mBinding.recyclerView.setAdapter(adapter);

    }

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.llInviteCost, o -> {
            List<InviteOnmicroData> list = adapter.getSelectData();
            if (list != null && list.size() > 0) {
                mListener.onInviteClick(list, 0,AppConstant.getInstance().getTabType());
            } else {
                toastShort("邀请人不能为空");
            }
        });
        subscribeClick(mBinding.llInviteFree, o -> {
            List<InviteOnmicroData> list = adapter.getSelectData();
            if (list != null && list.size() > 0) {
                mListener.onInviteClick(list, 1, AppConstant.getInstance().getTabType());
            } else {
                toastShort("邀请人不能为空");
            }
        });
    }
}
