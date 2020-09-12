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
import com.deepsea.mua.stub.entity.socket.receive.SongInfo;
import com.deepsea.mua.stub.entity.socket.receive.UpdateAppointmentSongListParam;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.SongApointmentAdapter;
import com.deepsea.mua.voice.databinding.FragmentSongAppointmentBinding;
import com.deepsea.mua.voice.utils.MatchMakerUtils;
import com.deepsea.mua.voice.viewmodel.SongOriginalViewModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import javax.inject.Inject;

import okhttp3.Response;

/**
 * Created by JUN on 2019/5/5
 */
public class SongApointmentFragment extends BaseFragment<FragmentSongAppointmentBinding> {

    public static SongApointmentFragment newInstance() {
        SongApointmentFragment instance = new SongApointmentFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            instance.setArguments(bundle);
        }
        return instance;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_song_appointment;
    }

    private SongOriginalViewModel mViewModel;
    @Inject
    ViewModelFactory mModelFactory;

    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(SongOriginalViewModel.class);
        addSocketListener();

        initRecyclerView();
        initRefreshLayout();
        mViewModel.getAppointmentSongListParam(0);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeSocketListener();
    }


    private int defaultPage = 0;

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            defaultPage=0;
            mViewModel.getAppointmentSongListParam(0);
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            defaultPage++;
            mViewModel.getAppointmentSongListParam(defaultPage);
        });
    }

    SongApointmentAdapter mAdapter;

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mBinding.recyclerView.getItemAnimator() != null) {
            mBinding.recyclerView.getItemAnimator().setChangeDuration(0);
        }
        mAdapter = new SongApointmentAdapter(mContext);
        mAdapter.setOnSongOperateListener(new SongApointmentAdapter.OnSongOperateListener() {
            @Override
            public void DelClick(int position, SongInfo bean) {
                String consertUserId = bean.getDemandUserId();
                String userId = UserUtils.getUser().getUid();
                if (consertUserId.equals(userId) || MatchMakerUtils.isRoomOwner()) {
                    mAdapter.remove(position);
                    mViewModel.deleteSongParam(bean.getId());
                }
            }

            @Override
            public void StickyClick(int position, SongInfo bean) {
                String consertUserId = bean.getConsertUserId();
                String userId = UserUtils.getUser().getUid();
                if (consertUserId.equals(userId) || MatchMakerUtils.isRoomOwner()) {
                    mViewModel.StickSongParam(bean.getId());
                }
            }
        });
        mBinding.recyclerView.setAdapter(mAdapter);
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
                case 91:
//                    Log.d("AG_EX_AV", "91返回：" + message);
                    UpdateAppointmentSongListParam updateAppointmentSongListParam = JsonConverter.fromJson(message, UpdateAppointmentSongListParam.class);
                    List<SongInfo> songInfos = updateAppointmentSongListParam.getSongInfos();
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
                case 92:
                    int success = object.get("Success").getAsInt();
                    if (success == 1) {
                        toastShort("置顶成功");
                    }
                    break;
                case 95:
                    success = object.get("Success").getAsInt();
                    if (success == 1) {
                        toastShort("删除成功");
                    }
                    break;
            }
        }

        public void onFailure(Throwable t, Response response) {
        }
    };

}
