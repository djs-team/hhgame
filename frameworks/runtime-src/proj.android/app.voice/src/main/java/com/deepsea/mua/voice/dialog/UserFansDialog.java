package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.websocket.WsocketListener;
import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.entity.socket.receive.GetMicroRanksParam;
import com.deepsea.mua.stub.entity.socket.receive.MicroRankData;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.NotifyHelpAdapter;
import com.deepsea.mua.voice.adapter.UserFansAdapter;
import com.deepsea.mua.voice.databinding.DialogNotifyHelpBinding;
import com.deepsea.mua.voice.databinding.DialogUserFansBinding;
import com.deepsea.mua.voice.viewmodel.RoomViewModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.commonsdk.statistics.proto.Response;

import java.util.List;

/**
 * Created by JUN on 2018/9/27
 * 粉丝榜
 */
public class UserFansDialog extends BaseDialog<DialogUserFansBinding> {
    private RoomViewModel roomViewModel;

    public UserFansDialog(@NonNull Context context, RoomViewModel roomViewModel) {
        super(context);
        this.roomViewModel = roomViewModel;
        addSocketListener();
    }

    public interface OnClickListener {
        /**
         * 点击回调
         */

        void onClick(WsUser bean);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_user_fans;
    }

    @Override
    protected float getWidthPercent() {
        return 1F;
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
                case 126:
                    Log.d("AG_EX_AV", "126 返回:" + message);
                    GetMicroRanksParam getMicroRanksParam = JsonConverter.fromJson(message, GetMicroRanksParam.class);
                    ViewBindUtils.setText(mBinding.tvRoseCount, "玫瑰:" + getMicroRanksParam.getAllRolse());
                    List<MicroRankData> rankData = getMicroRanksParam.getMicroRankDatas();
                    if (rankData != null && rankData.size() > 0) {
                        if (page > 0) {
                            mAdapter.addData(rankData);
                            mBinding.refreshLayout.finishLoadMore();

                        } else {
                            mAdapter.setNewData(rankData);
                            mBinding.refreshLayout.finishRefresh();

                        }
                    }
                    if (page > 0) {
                        mBinding.refreshLayout.finishLoadMore();

                    } else {
                        mBinding.refreshLayout.finishRefresh();
                    }
                    break;

            }
        }

        public void onFailure(Throwable t, Response response) {

        }
    };

    UserFansAdapter mAdapter;
    private int page = 0;

    public void setMsg(WsUser data) {
        mAdapter = new UserFansAdapter(mContext);
        mBinding.rvFans.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rvFans.setAdapter(mAdapter);
        roomViewModel.getMicroRank(page, data.getType(), data.getNumber());
        mBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                roomViewModel.getMicroRank(page, data.getType(), data.getNumber());
            }
        });
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                roomViewModel.getMicroRank(page, data.getType(), data.getNumber());

            }
        });
        ViewBindUtils.setText(mBinding.tvName, data.getName() + "的粉丝榜");
        GlideUtils.circleImage(mBinding.ivHead, data.getHeadImageUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);


    }

    @Override
    public void dismiss() {
        super.dismiss();
        removeSocketListener();
    }
}
