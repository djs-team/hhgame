package com.deepsea.mua.voice.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.entity.MineRooms;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.MineRoomsAdapter;
import com.deepsea.mua.voice.databinding.ActivityMineRoomsBinding;
import com.deepsea.mua.voice.viewmodel.MineRoomViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/18
 */
public class MineRoomActivity extends BaseActivity<ActivityMineRoomsBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private MineRoomViewModel mViewModel;
    @Inject
    RoomJoinController mRoomJump;
    private MineRoomsAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_rooms;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(MineRoomViewModel.class);
        initRefreshLayout();
        initRecyclerView();

    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.addRoom, o -> {
            startActivity(new Intent(mContext, RoomCreateActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMyRooms();
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setMaterialHeader();
        mBinding.refreshLayout.setEnableLoadMore(false);
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> getMyRooms());
//        mBinding.refreshLayout.autoRefresh();
    }

    private void initRecyclerView() {
        mAdapter = new MineRoomsAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", Constant.isBeautyOpen);
                    if (!hasFaceBeauty || AppConstant.getInstance().isRtcEngineDestroy()) {
                        AgoraClient.create().release();
                        AgoraClient.create().setUpAgora(getApplicationContext(), "e0972168ff254d7aa05501cd85204692");
                    }
                }
            }).start();


            MineRooms.MyroomListBean item = mAdapter.getItem(position);
            mRoomJump.startJump(item.getRoom_id(), mContext);
        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private void getMyRooms() {
        mViewModel.myRoom().observe(this, new BaseObserver<MineRooms>() {
            @Override
            public void onSuccess(MineRooms result) {
                mBinding.refreshLayout.finishRefresh();
                if (result != null) {
                    List<MineRooms.MyroomListBean> list = result.getMyroom_list();
                    if (list != null) {
                        for (MineRooms.MyroomListBean bean : list) {
                            bean.setOwner(true);
                        }
                    } else {
                        list = new ArrayList<>();
                    }
                    mBinding.addRoom.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
                    List<MineRooms.MyroomListBean> member = result.getMyroom_member();
                    if (member != null) {
                        list.addAll(member);
                    }
                    mAdapter.setNewData(list);
                } else {
                    mAdapter.setNewData(null);
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                mBinding.refreshLayout.finishRefresh();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mRoomJump.destroy();
        super.onDestroy();
    }
}
