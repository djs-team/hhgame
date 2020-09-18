package com.deepsea.mua.voice.fragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.utils.UiUtils;
import com.deepsea.mua.core.websocket.WsocketListener;
import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.entity.socket.receive.GetSongListParam;
import com.deepsea.mua.stub.entity.socket.receive.SongParam;
import com.deepsea.mua.stub.entity.socket.send.JoinRoom;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.SongOriginalAdapter;
import com.deepsea.mua.voice.databinding.FragmentSongOriginalBinding;
import com.deepsea.mua.voice.dialog.SongAlertDialog;
import com.deepsea.mua.voice.dialog.SongerSelectDialog;
import com.deepsea.mua.voice.dialog.SongerSelectForManyDialog;
import com.deepsea.mua.voice.viewmodel.SongOriginalViewModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.inject.Inject;
import okhttp3.Response;

/**
 * Created by JUN on 2019/5/5
 */
public class SongBanchangFragment extends BaseFragment<FragmentSongOriginalBinding> {


    private int SongMode;
    private int roomMode = 0;

    public static SongBanchangFragment newInstance(int SongMode) {
        SongBanchangFragment instance = new SongBanchangFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putInt("SongMode", SongMode);
            instance.setArguments(bundle);
        } else {
            bundle.putInt("SongMode", SongMode);

        }
        return instance;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_song_original;
    }

    private SongOriginalViewModel mViewModel;
    @Inject
    ViewModelFactory mModelFactory;

    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(SongOriginalViewModel.class);
        SongMode = mBundle.getInt("SongMode");
        addSocketListener();
        initRecyclerView();
        initRefreshLayout();
        initSearchEdit();
        mViewModel.getSongListParam(SongMode, 0);
        JoinRoom joinRoom = AppConstant.getInstance().getJoinRoom();
        if (joinRoom != null) {
            roomMode = joinRoom.getRoomMode();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeSocketListener();
    }

    private void initSearchEdit() {
        mBinding.etSongSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                UiUtils.hideKeyboard(mBinding.etSongSearch);
                String text = mBinding.etSongSearch.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    search(text);
                }else {
                    mViewModel.getSongListParam(SongMode, 0);

                }
                mBinding.etSongSearch.clearFocus();
                return true;
            }
            return false;
        });

    }

    private void search(String searchName) {
        mViewModel.searchSongParam(SongMode, searchName);
    }

    private int defaultPage = 0;

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            defaultPage = 0;
            mViewModel.getSongListParam(SongMode, 0);
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            defaultPage++;
            mViewModel.getSongListParam(SongMode, defaultPage);
        });

    }

    SongOriginalAdapter mAdapter;

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mBinding.recyclerView.getItemAnimator() != null) {
            mBinding.recyclerView.getItemAnimator().setChangeDuration(0);
        }
        mAdapter = new SongOriginalAdapter(mContext);
        mAdapter.setOnSongOperateListener(new SongOriginalAdapter.OnSongOperateListener() {
            @Override
            public void AppointmentClick(int position, SongParam bean) {
                if (SongMode == 1) {
                    mViewModel.demandSongParam(bean.getId(), -1, -1);

                } else {
                    showAppointMentDialog(bean.getId());
                }
            }
        });
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    SongerSelectDialog songerSelectDialog = null;

    SongerSelectForManyDialog songerSelectForManyDialog = null;

    private void showAppointMentDialog(int songId) {
        if (roomMode > 6) {
            songerSelectForManyDialog = new SongerSelectForManyDialog(mContext);
            songerSelectForManyDialog.setListener(new SongerSelectForManyDialog.OnClickListener() {
                @Override
                public void onSelectClick(int Level, int Number) {
                    mViewModel.demandSongParam(songId, Level, Number);
                }
            });
            songerSelectForManyDialog.show();
        } else {
            songerSelectDialog = new SongerSelectDialog(mContext);
            songerSelectDialog.setListener(new SongerSelectDialog.OnClickListener() {
                @Override
                public void onSelectClick(int Level, int Number) {
                    mViewModel.demandSongParam(songId, Level, Number);
                }
            });
            songerSelectDialog.show();
        }
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
                case 89:
//                    Log.d("AG_EX_AV", "89:" + message);
                    GetSongListParam getSongListParam = JsonConverter.fromJson(message, GetSongListParam.class);
                    if (SongMode != getSongListParam.getSongMode()) {
                        return;
                    }

                    if (getSongListParam.getSongDatas() != null && getSongListParam.getSongDatas().size() > 0) {
                        if (defaultPage == 0) {
                            mAdapter.setNewData(getSongListParam.getSongDatas());
                        } else {
                            mAdapter.addData(getSongListParam.getSongDatas());
                        }
//                        String coin = getSongListParam.getSongDatas().get(0).getCoin();
//                        ViewBindUtils.setText(mBinding.tvSongCost, String.format(getString(R.string.song_flower_cost), coin));
                    }
                    if (defaultPage == 0) {
                        mBinding.refreshLayout.finishRefresh();
                    } else {
                        mBinding.refreshLayout.finishLoadMore();
//                        Log.d("AG_EX_AV", getSongListParam.getAllPage() + "；" + defaultPage);
                    }
                    if (getSongListParam.getAllPage() > defaultPage) {
                        mBinding.refreshLayout.setEnableLoadMore(true);
                    } else {
                        mBinding.refreshLayout.setEnableLoadMore(false);
                    }
                    break;
                case 90:
                    boolean fragmentVisible= getUserVisibleHint();
                    if (!fragmentVisible){
                        return;
                    }
//                    Log.d("AG_EX_AV", "90返回" + message);
                    int Success = object.get("Success").getAsInt();
                    if (Success == 1) {
                        toastShort("预约成功");
                        if (songerSelectDialog != null) {
                            songerSelectDialog.dismiss();
                        }
                    } else if (Success == 2) {

                        showAppointmentDialog("找不到麦位信息");
                    } else if (Success == 3) {

                        showAppointmentDialog("为麦上没有用户");
                    } else if (Success == 4) {

                        showAppointmentDialog("找不到歌曲信息");
                    } else if (Success == 5) {
                        showAppointmentDialog("点歌人玫瑰不足， 歌曲被取消");
                    } else if (Success == 6) {
                        showAppointmentDialog("预约列表有重复歌曲");
                    } else if (Success == 7) {
                        showAppointmentDialog("超过最大预约歌曲");
                    }
                    break;
                case 98:
//                    Log.d("AG_EX_AV", "98返回" + message);

                    getSongListParam = JsonConverter.fromJson(message, GetSongListParam.class);
                    boolean hasData=getSongListParam.getSongDatas() != null && getSongListParam.getSongDatas().size() > 0;

                    if (SongMode != getSongListParam.getSongMode()&&hasData) {
                        return;
                    }

                    if (getSongListParam.getSongDatas() != null && getSongListParam.getSongDatas().size() > 0) {
                        mAdapter.setNewData(getSongListParam.getSongDatas());
                        String coin = getSongListParam.getSongDatas().get(0).getCoin();
//                        ViewBindUtils.setText(mBinding.tvSongCost, String.format(getString(R.string.song_flower_cost), coin));
                    } else {
                        mAdapter.setNewData(getSongListParam.getSongDatas());
                    }
                    break;


            }
        }

        public void onFailure(Throwable t, Response response) {
        }
    };

    private void showAppointmentDialog(String alert) {
        SongAlertDialog dialog = new SongAlertDialog(mContext);
        dialog.setMsg(alert);
        dialog.showAtBottom();
    }

}
