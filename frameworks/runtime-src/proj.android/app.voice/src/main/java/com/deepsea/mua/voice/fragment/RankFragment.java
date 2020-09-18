package com.deepsea.mua.voice.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.websocket.WsocketListener;
import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.databinding.HeaderRoomRankBinding;
import com.deepsea.mua.stub.entity.RankBean;
import com.deepsea.mua.stub.entity.socket.RoomRank;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.span.LevelResUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.RoomRankAdapter;
import com.deepsea.mua.voice.databinding.FragmentRankBinding;
import com.deepsea.mua.voice.databinding.FragmentRankMainBinding;
import com.deepsea.mua.voice.viewmodel.RankViewModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.Response;

/**
 * Created by JUN on 2019/4/10
 */
public class RankFragment extends BaseFragment<FragmentRankMainBinding> {

    private RoomRankAdapter mAdapter;

    @Inject
    ViewModelFactory mModelFactory;
    private RankViewModel mViewModel;

    //1 财富榜 2 魅力榜
    private String mType;
    //1 日 2周 3月
    private String mStatus;
    //房间id
    private String mRoomId;

    //是否是财富榜单
    private boolean isWealth;

    private HeaderRoomRankBinding mHeaderBinding;

    public static RankFragment newInstance(String type, String status, String roomId) {
        RankFragment instance = new RankFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString("type", type);
            bundle.putString("status", status);
            bundle.putString("roomId", roomId);
            instance.setArguments(bundle);
        } else {
            bundle.putString("type", type);
            bundle.putString("status", status);
            bundle.putString("roomId", roomId);
        }
        return instance;
    }

    @Override
    protected boolean isLazyView() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_rank_main;
    }

    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(RankViewModel.class);
        mType = mBundle.getString("type");
        mStatus = mBundle.getString("status");
        mRoomId = mBundle.getString("roomId");
        isWealth = TextUtils.equals(mType, "1");
        initRecyclerView();
        initRefreshLayout();
    }

    private void addSocketListener() {
        WsocketManager.create().addWsocketListener(mWsocketListener);
    }

    private void removeSocketListener() {
        WsocketManager.create().removeWsocketListener(mWsocketListener);
    }

    private WsocketListener mWsocketListener = new WsocketListener() {
        public void onMessage(String message) {
            Log.d("rank", message);
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(message).getAsJsonObject();
            int msgId = object.get("MsgId").getAsInt();
            switch (msgId) {
                case 59:
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        removeSocketListener();
                        RoomRank roomRank = JsonConverter.fromJson(message, RoomRank.class);
                        if (page == 0) {
                            setData(roomRank.getRankDatas());
                            setTopRanks(roomRank.getRankDatas());
                            mBinding.refreshLayout.finishRefresh();

                        } else {
                            mAdapter.addData(roomRank.getRankDatas());
                            mBinding.refreshLayout.finishLoadMore();
                        }

                        if (roomRank.getAllPage() > page) {
                            mBinding.refreshLayout.setEnableLoadMore(true);
                        } else {
                            mBinding.refreshLayout.setEnableLoadMore(false);
                        }
                    }
                    break;
            }
        }

        @Override
        public void onClosed(int code, String reason) {
            finishRefresh();
        }

        @Override
        public void onFailure(Throwable t, Response response) {
            finishRefresh();
        }

        private void finishRefresh() {
            if (getActivity() != null && !getActivity().isFinishing()) {
                mBinding.refreshLayout.finishRefresh();
            }
        }
    };

    @Override
    public void onDestroy() {
        removeSocketListener();
        super.onDestroy();
    }

    private void initRecyclerView() {
        mAdapter = new RoomRankAdapter(mContext, R.layout.item_room_rank, null);
        mAdapter.setWealth(isWealth);
        mAdapter.setOnItemClickListener((adapter, view, position) -> jumpToProfile(mAdapter.getItem(position).getUserid()));
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);

        mHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.header_room_rank, null, false);
        mAdapter.addHeaderView(mHeaderBinding.getRoot());

        boolean selected = !isWealth;
        mHeaderBinding.goldAvatarBg.setSelected(selected);
        mHeaderBinding.goldAvatarIv.setSelected(selected);
        mHeaderBinding.goldNumTv.setSelected(selected);
        mHeaderBinding.silverAvatarBg.setSelected(selected);
        mHeaderBinding.silverAvatarIv.setSelected(selected);
        mHeaderBinding.silverNumTv.setSelected(selected);
        mHeaderBinding.copperAvatarBg.setSelected(selected);
        mHeaderBinding.copperAvatarIv.setSelected(selected);
        mHeaderBinding.copperNumTv.setSelected(selected);
    }

    private int page = 0;

    private void initRefreshLayout() {
        mBinding.refreshLayout.setMaterialHeader();
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            addSocketListener();
            page = 0;
            boolean send = mViewModel.ranklist(mType, mStatus, page);
            if (!send) {
                mBinding.refreshLayout.finishRefresh();
            }
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            addSocketListener();
            page++;
            boolean send = mViewModel.ranklist(mType, mStatus, page);
            if (!send) {
                mBinding.refreshLayout.finishLoadMore();
            }
        });

        mBinding.refreshLayout.autoRefresh();
    }

    private void setData(List<RankBean> result) {
        LinearLayout headerLayout = mAdapter.getHeaderLayout();
        if (headerLayout != null) {
            boolean empty = CollectionUtils.isEmpty(result);
            int height = empty ? ViewGroup.LayoutParams.MATCH_PARENT : ViewGroup.LayoutParams.WRAP_CONTENT;
            headerLayout.getLayoutParams().height = height;
            mHeaderBinding.getRoot().getLayoutParams().height = height;
            mHeaderBinding.emptyTv.setVisibility(empty ? View.VISIBLE : View.GONE);
            mHeaderBinding.emptyTv.setTextColor(isWealth ? 0xFFEC83B3 : 0xFFFF8461);
        }

        List<RankBean> datas = null;
        if (result != null && result.size() > 3) {
            datas = result.subList(3, result.size());
        }

        mAdapter.setNewData(datas);
    }

    private void setTopRanks(List<RankBean> result) {
        if (result == null) {
            result = new ArrayList<>();
        }

        String noRank = "虚位以待~";
        //top 1
        int index = 0;
        if (index < result.size()) {
            RankBean bean = result.get(index);
            GlideUtils.circleImage(mHeaderBinding.goldAvatarIv, bean.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            mHeaderBinding.goldNameTv.setText(bean.getNickname());
            mHeaderBinding.goldLevelTv.setVisibility(bean.getUser_lv() > 0 ? View.VISIBLE : View.INVISIBLE);
            mHeaderBinding.goldLevelTv.setText(String.format(Locale.CHINA, "%d", bean.getUser_lv()));
            mHeaderBinding.goldLevelTv.setBackgroundResource(LevelResUtils.getLevelRes(bean.getUser_lv()));
            mHeaderBinding.goldNumTv.setText(FormatUtils.subZeroAndDot(bean.getCoin()));
            mHeaderBinding.goldUidTv.setText(String.format(Locale.CHINA, "ID: %s", bean.getUserid()));

            mHeaderBinding.goldAvatarIv.setOnClickListener(v -> {
                jumpToProfile(bean.getUserid());
            });
        } else {
            mHeaderBinding.goldNameTv.setText(noRank);
        }
        //top 2
        index = 1;
        if (index < result.size()) {
            RankBean bean = result.get(index);
            GlideUtils.circleImage(mHeaderBinding.silverAvatarIv, bean.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            mHeaderBinding.silverNameTv.setText(bean.getNickname());
            mHeaderBinding.silverLevelTv.setVisibility(bean.getUser_lv() > 0 ? View.VISIBLE : View.INVISIBLE);
            mHeaderBinding.silverLevelTv.setText(String.format(Locale.CHINA, "%d", bean.getUser_lv()));
            mHeaderBinding.silverLevelTv.setBackgroundResource(LevelResUtils.getLevelRes(bean.getUser_lv()));
            mHeaderBinding.silverNumTv.setText(FormatUtils.subZeroAndDot(bean.getCoin()));
            mHeaderBinding.silverUidTv.setText(String.format(Locale.CHINA, "ID: %s", bean.getUserid()));

            mHeaderBinding.silverAvatarIv.setOnClickListener(v -> {
                jumpToProfile(bean.getUserid());
            });
        } else {
            mHeaderBinding.silverNameTv.setText(noRank);
        }
        //top 3
        index = 2;
        if (index < result.size()) {
            RankBean bean = result.get(index);
            GlideUtils.circleImage(mHeaderBinding.copperAvatarIv, bean.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            mHeaderBinding.copperNameTv.setText(bean.getNickname());
            mHeaderBinding.copperLevelTv.setVisibility(bean.getUser_lv() > 0 ? View.VISIBLE : View.INVISIBLE);
            mHeaderBinding.copperLevelTv.setText(String.format(Locale.CHINA, "%d", bean.getUser_lv()));
            mHeaderBinding.copperLevelTv.setBackgroundResource(LevelResUtils.getLevelRes(bean.getUser_lv()));
            mHeaderBinding.copperNumTv.setText(FormatUtils.subZeroAndDot(bean.getCoin()));
            mHeaderBinding.copperUidTv.setText(String.format(Locale.CHINA, "ID: %s", bean.getUserid()));

            mHeaderBinding.copperAvatarIv.setOnClickListener(v -> {
                jumpToProfile(bean.getUserid());
            });
        } else {
            mHeaderBinding.copperNameTv.setText(noRank);
        }
    }

    private void jumpToProfile(String uid) {
        PageJumpUtils.jumpToProfile(uid);
    }
}
