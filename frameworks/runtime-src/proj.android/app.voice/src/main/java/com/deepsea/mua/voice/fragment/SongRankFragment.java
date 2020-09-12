package com.deepsea.mua.voice.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.websocket.WsocketListener;
import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.RankListResult;
import com.deepsea.mua.stub.entity.socket.receive.SongInfo;
import com.deepsea.mua.stub.entity.socket.receive.SongRankData;
import com.deepsea.mua.stub.entity.socket.receive.SongRankListParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateAppointmentSongListParam;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.SongApointmentAdapter;
import com.deepsea.mua.voice.adapter.SongRankAdapter;
import com.deepsea.mua.voice.databinding.FragmentSongAppointmentBinding;
import com.deepsea.mua.voice.databinding.FragmentSongRankBinding;
import com.deepsea.mua.voice.utils.MatchMakerUtils;
import com.deepsea.mua.voice.viewmodel.SongOriginalViewModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import javax.inject.Inject;

import okhttp3.Response;

/**
 * 排行
 */
public class SongRankFragment extends BaseFragment<FragmentSongRankBinding> {
    String mRoomId;

    public static SongRankFragment newInstance(String mRoomId) {
        SongRankFragment instance = new SongRankFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString("mRoomId", mRoomId);
            instance.setArguments(bundle);
        } else {
            bundle.putString("mRoomId", mRoomId);
        }
        return instance;
    }

    private void addSocketListener() {
        WsocketManager.create().addWsocketListener(mWsocketListener);
    }

    private void removeSocketListener() {
        WsocketManager.create().removeWsocketListener(mWsocketListener);
    }

    private WsocketListener mWsocketListener = new WsocketListener() {
        public void onMessage(String message) {
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(message).getAsJsonObject();
            int msgId = object.get("MsgId").getAsInt();
            switch (msgId) {
                case 118:
//                    Log.d("AG_EX_AV", "118返回：" + message);
                    SongRankListParam updateAppointmentSongListParam = JsonConverter.fromJson(message, SongRankListParam.class);
                    List<SongRankData> songInfos = updateAppointmentSongListParam.getSongRankDatas();
                    if (songInfos != null && songInfos.size() > 0) {
                        if (defaultPage == 0) {
                            mAdapter.setNewData(songInfos);
                        } else {
                            mAdapter.addData(songInfos);
                        }
                    }
                    if (defaultPage == 0) {
                        mBinding.refreshLayout.finishRefresh();
                    } else {
                        mBinding.refreshLayout.finishLoadMore();

                    }
                    if (updateAppointmentSongListParam.getAllPage() > defaultPage) {
                        mBinding.refreshLayout.setEnableLoadMore(true);
                    } else {
                        mBinding.refreshLayout.setEnableLoadMore(false);
                    }
                    break;
                case 108:
                    mBinding.refreshLayout.autoRefresh();
                    break;
            }
        }

        public void onFailure(Throwable t, Response response) {
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_song_rank;
    }

    private SongOriginalViewModel mViewModel;
    @Inject
    ViewModelFactory mModelFactory;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        Log.d("AG_EX_AV", "118：hidden---"+hidden );

    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d("AG_EX_AV", "118：onResume" );

    }

    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(SongOriginalViewModel.class);
        addSocketListener();
        mRoomId = mBundle.getString("mRoomId");
        initRecyclerView();
        initRefreshLayout();
        mViewModel.getRoomRanksParam(0);

    }


    private int defaultPage = 0;

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            defaultPage = 0;
            mViewModel.getRoomRanksParam(0);
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            defaultPage++;
            mViewModel.getRoomRanksParam(defaultPage);
        });
    }

    SongRankAdapter mAdapter;

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mBinding.recyclerView.getItemAnimator() != null) {
            mBinding.recyclerView.getItemAnimator().setChangeDuration(0);
        }
        mAdapter = new SongRankAdapter(mContext);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeSocketListener();
    }

}
