package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.websocket.WsocketListener;
import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.entity.socket.receive.GetGuardItemListParam;
import com.deepsea.mua.stub.entity.socket.receive.GetMicroRanksParam;
import com.deepsea.mua.stub.entity.socket.receive.GuardItem;
import com.deepsea.mua.stub.entity.socket.receive.MicroRankData;
import com.deepsea.mua.stub.mvp.NewSubscriberCallBack;
import com.deepsea.mua.stub.network.HttpHelper;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.eventbus.MarqueeEvent;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.GuardGroupAdapter;
import com.deepsea.mua.voice.adapter.GuardListHeaderAdapter;
import com.deepsea.mua.voice.adapter.UserFansAdapter;
import com.deepsea.mua.voice.databinding.DialogGuardGroupMineBinding;
import com.deepsea.mua.voice.databinding.DialogUserFansBinding;
import com.deepsea.mua.voice.viewmodel.RoomViewModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import okhttp3.Response;


/**
 * Created by JUN on 2018/9/27
 * 守护团
 */
public class GuardGroupDialog extends BaseDialog<DialogGuardGroupMineBinding> {
    private RoomViewModel roomViewModel;

    public GuardGroupDialog(@NonNull Context context, RoomViewModel roomViewModel) {
        super(context);
        this.roomViewModel = roomViewModel;
        addSocketListener();
    }


    public interface OnClickListener {
        /**
         * 点击回调
         */

        void onClick(String userId);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_guard_group_mine;
    }

    @Override
    protected float getWidthPercent() {
        return 1F;
    }


    GuardGroupAdapter mAdapter;
    private int page = 0;
    private String guardUserId;

    public void setMsg(String userId) {
        this.guardUserId = userId;
        mAdapter = new GuardGroupAdapter(mContext);
        mBinding.rvFans.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rvFans.setAdapter(mAdapter);


        mBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                roomViewModel.getGuardItemList(page, guardUserId);
            }
        });
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                roomViewModel.getGuardItemList(page, guardUserId);

            }
        });
        mBinding.refreshLayout.autoRefresh();
        ViewBindUtils.RxClicks(mBinding.tvImmediatelyGuard, o -> {
            if (onClickListener != null) {
                onClickListener.onClick(userId);
            }
        });
    }

    private void setViewData(boolean isGuard, String nickName, String guardHeard, int userCount) {
        ViewBindUtils.setText(mBinding.tvName, nickName + "的守护团");
        ViewBindUtils.setText(mBinding.tvNum, "（" + userCount + "人）");
        GlideUtils.circleImage(mBinding.ivHead, guardHeard, R.drawable.ic_place, R.drawable.ic_place);
        if (isGuard == false && !TextUtils.equals(UserUtils.getUser().getUid(), guardUserId)) {
            mBinding.consGroup.setVisibility(View.VISIBLE);
            mBinding.refreshLayout.setVisibility(View.GONE);
        } else {
            mBinding.refreshLayout.setVisibility(View.VISIBLE);
            mBinding.consGroup.setVisibility(View.GONE);
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
                case 139:
                    GetGuardItemListParam data = JsonConverter.fromJson(message, GetGuardItemListParam.class);
                    List<GuardItem> itemList = data.getGuardItems();
                    if (itemList != null && itemList.size() > 0) {
                        if (page == 0) {
                            mAdapter.setNewData(itemList);
                        } else {
                            mAdapter.addData(itemList);
                        }
                    }
                    if (page == 0) {
                        setViewData(data.isGuard(), data.getGuardName(), data.getGuardHead(), data.getUserCount());

                        mBinding.refreshLayout.finishRefresh();
                    } else {
                        mBinding.refreshLayout.finishLoadMore();

                    }
                    if (data.getAllPage() > page) {
                        mBinding.refreshLayout.setEnableLoadMore(true);
                    } else {
                        mBinding.refreshLayout.setEnableLoadMore(false);
                    }
                    break;

            }
        }

        public void onFailure(Throwable t, Response response) {
        }
    };


    @Override
    public void dismiss() {
        super.dismiss();
        removeSocketListener();
    }
}
