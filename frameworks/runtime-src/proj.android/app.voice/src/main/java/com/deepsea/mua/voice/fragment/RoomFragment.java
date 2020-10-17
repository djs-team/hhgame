package com.deepsea.mua.voice.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.RecyclerAdapterWithHF;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.dialog.SexEditDialog;
import com.deepsea.mua.stub.entity.HomeInfo;
import com.deepsea.mua.stub.entity.RoomsBean;
import com.deepsea.mua.stub.entity.VoiceBanner;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.GridItemDecoration;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.ViewUtils;
import com.deepsea.mua.stub.utils.WrapGridLayoutManager;
import com.deepsea.mua.stub.utils.eventbus.CitySortEvent;
import com.deepsea.mua.stub.utils.eventbus.InviteDialogCloseEvent;
import com.deepsea.mua.stub.utils.eventbus.OpenRoom;
import com.deepsea.mua.stub.utils.eventbus.ShowRankStepOne;
import com.deepsea.mua.stub.utils.eventbus.ShowRankStepTwo;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.RoomAdapter;
import com.deepsea.mua.voice.databinding.FragmentRoomBinding;
import com.deepsea.mua.voice.viewmodel.RoomsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/17
 */
public class RoomFragment extends BaseFragment<FragmentRoomBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private RoomsViewModel mViewModel;
    @Inject
    RoomJoinController mRoomJump;

    private RoomAdapter mAdapter;
    private RecyclerAdapterWithHF mAdapterWithHF;

    private String roomType;
    private String age;
    private String city;
    private String city_two;
    private String city_three;


    public static BaseFragment newInstance(String roomType) {
        RoomFragment instance = new RoomFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString("roomType", roomType);
            instance.setArguments(bundle);
        } else {
            bundle.putString("roomType", roomType);
        }
        return instance;
    }

    @Override
    protected boolean isLazyView() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_room;
    }

    @Override
    protected void initView(View view) {
        registerEventBus(this);
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(RoomsViewModel.class);
        roomType = mBundle.getString("roomType");
        initRecyclerView();
        initRefreshLayout();
    }

    private void initRecyclerView() {
        mAdapter = new RoomAdapter(mContext);
        mAdapter.setOnItemClickListener(new BaseBindingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String sex = UserUtils.getUser().getSex();
                if (sex.equals("0")) {
                    SexEditDialog sexEditDialog = new SexEditDialog(mContext);
                    sexEditDialog.show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", Constant.isBeautyOpen);
                            if (!hasFaceBeauty || AppConstant.getInstance().isRtcEngineDestroy()) {
                                AgoraClient.create().release();
                                AgoraClient.create().setUpAgora(getContext().getApplicationContext(), "30870262f27a4642a99e67cc1851f90a");
                            }
                        }
                    }).start();
                    mRoomJump.startJump(mAdapter.getItem(position).getRoom_id(), Integer.valueOf(roomType), mContext, null);
                }
            }
        });
        mBinding.recyclerView.setLayoutManager(new WrapGridLayoutManager(mContext, 2));
        mBinding.recyclerView.addItemDecoration(new GridItemDecoration(2, ResUtils.dp2px(mContext, 16)));
        mAdapterWithHF = new RecyclerAdapterWithHF(mAdapter);
        mAdapterWithHF.setManagerType(RecyclerAdapterWithHF.TYPE_MANAGER_GRID);
        mAdapterWithHF.setRecycleView(mBinding.recyclerView);
        mBinding.recyclerView.setAdapter(mAdapterWithHF);


        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.setHasFixedSize(true);
//解决数据加载完成后, 没有停留在顶部的问题
        mBinding.recyclerView.setFocusable(false);
        addFooter();
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setMaterialHeader();
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            refresh();
//            getBanners();
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> loadMore());
        mBinding.refreshLayout.autoRefresh();
    }

    private void addFooter() {
        View view = View.inflate(mContext, R.layout.item_no_more_data, null);
        mAdapterWithHF.addFooter(view);
        mAdapterWithHF.showFooterView(false);
    }

    private void refresh() {
        mViewModel.refresh(roomType, age, city, city_two, city_three).observe(this, new BaseObserver<RoomsBean>() {
            @Override
            public void onSuccess(RoomsBean result) {
                mBinding.refreshLayout.finishRefresh();
                if (result != null) {
                    if (result.getRoom_list() != null && result.getRoom_list().size() > 0) {
                        setRecommendRoom(result.getRoom_list().get(0));
                        RoomsBean.PageInfoBean pageInfo = result.getPageInfo();
                        if (result.getRoom_list().size() > 1) {
                            mAdapter.setNewData(result.getRoom_list().subList(1, result.getRoom_list().size()));
                        }
                        boolean enableLoadMore = pageInfo.getPage() < pageInfo.getTotalPage();
                        mBinding.refreshLayout.setEnableLoadMore(enableLoadMore);
                        mAdapterWithHF.showFooterView(!enableLoadMore);
                    } else {
                        mAdapter.setNewData(null);
                        ViewBindUtils.setVisible(mBinding.tlTimeRecommend, false);
                    }
                }
            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
                mBinding.refreshLayout.finishRefresh();
            }
        });
    }

    private void setRecommendRoom(HomeInfo.RoomBean topRoom) {
        ViewBindUtils.setVisible(mBinding.tlTimeRecommend, true);
        GlideUtils.roundImage(mBinding.ivRecommondUserbg, topRoom.getFm_room_image(), R.drawable.ic_place_room_bg, R.drawable.ic_place_room_bg, 10);
        ViewBindUtils.setText(mBinding.tvRoomName, "欢迎来到" + topRoom.getRoom_name() + "的直播间");
        ViewBindUtils.setVisible(mBinding.roomLockRl, TextUtils.equals("1", topRoom.getRoom_lock()));
        ViewBindUtils.RxClicks(mBinding.tlTimeRecommend, o -> {
            String sex = UserUtils.getUser().getSex();
            if (sex.equals("0")) {
                SexEditDialog sexEditDialog = new SexEditDialog(mContext);
                sexEditDialog.show();
            } else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        boolean hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", Constant.isBeautyOpen);
                        if (!hasFaceBeauty || AppConstant.getInstance().isRtcEngineDestroy()) {
                            AgoraClient.create().release();
                            AgoraClient.create().setUpAgora(getContext().getApplicationContext(), "30870262f27a4642a99e67cc1851f90a");
                        }
                    }
                }).start();
                mRoomJump.startJump(topRoom.getRoom_id(), Integer.valueOf(roomType), mContext, null);
            }
        });
    }


    private void loadMore() {
        mViewModel.loadMore(roomType, age, city, city_two, city_three).observe(this, new BaseObserver<RoomsBean>() {
            @Override
            public void onSuccess(RoomsBean result) {
                if (result != null) {
                    RoomsBean.PageInfoBean pageInfo = result.getPageInfo();
                    mAdapter.addData(result.getRoom_list());
                    mBinding.refreshLayout.finishLoadMore();
                    boolean enableLoadMore = pageInfo.getPage() < pageInfo.getTotalPage();
                    mBinding.refreshLayout.setEnableLoadMore(enableLoadMore);
                    mAdapterWithHF.showFooterView(!enableLoadMore);
                }
            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
                mBinding.refreshLayout.finishLoadMore();
                mViewModel.pageNumber--;
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sortCity(CitySortEvent citySortEvent) {
        boolean fragmentVisible = getUserVisibleHint();
        if (!fragmentVisible) {
            return;
        }
        age = citySortEvent.getAge();
        city = citySortEvent.getCity();
        city_two = citySortEvent.getCity_two();
        city_three = citySortEvent.getCity_three();
        refresh();
    }


    @Override
    public void onDestroy() {
        mRoomJump.destroy();
        unregisterEventBus(this);
        super.onDestroy();
    }
}
