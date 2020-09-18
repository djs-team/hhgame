package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.websocket.WsocketListener;
import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.adapter.RecyclerAdapterWithHF;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.entity.GuardInfoBean;
import com.deepsea.mua.stub.entity.LookGuardUserVo;
import com.deepsea.mua.stub.entity.MaqueeBean;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.entity.socket.receive.GetMicroRanksParam;
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
import com.umeng.commonsdk.statistics.proto.Response;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2018/9/27
 * 守护团
 */
public class GuardGroupDialog extends BaseDialog<DialogGuardGroupMineBinding> {

    public GuardGroupDialog(@NonNull Context context) {
        super(context);

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
    private int page = 1;

    public void setMsg(String userId) {
        mAdapter = new GuardGroupAdapter(mContext);
        mBinding.rvFans.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rvFans.setAdapter(mAdapter);


        mBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                fetchGuardData(userId, page);

            }
        });
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                fetchGuardData(userId, page);

            }
        });
        mBinding.refreshLayout.autoRefresh();
        ViewBindUtils.RxClicks(mBinding.tvImmediatelyGuard, o -> {
            if (onClickListener != null) {
                onClickListener.onClick(userId);
            }
        });
    }

    private void setViewData(LookGuardUserVo data) {
        LookGuardUserVo.UserInfoBean userInfoBean = data.getUser_info();
        ViewBindUtils.setText(mBinding.tvName, userInfoBean.getNickname() + "的守护团");
        GlideUtils.circleImage(mBinding.ivHead, userInfoBean.getAvatar(), R.drawable.ic_place, R.drawable.ic_place);
//        ViewBindUtils.setText(mBinding.tvName,userInfoBean.get()+"的守护团");
        int isGuard = data.getIs_guard();
        if (isGuard == 2&& !TextUtils.equals(UserUtils.getUser().getUid(),userInfoBean.getId())) {
            mBinding.consGroup.setVisibility(View.VISIBLE);
            mBinding.refreshLayout.setVisibility(View.GONE);
        } else {
            mBinding.refreshLayout.setVisibility(View.VISIBLE);
            mBinding.consGroup.setVisibility(View.GONE);
        }

    }

    private void fetchGuardData(String userId, int page) {
        HttpHelper.instance().getApi(RetrofitApi.class).getGuardUserList(userId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NewSubscriberCallBack<LookGuardUserVo>() {
                    @Override
                    protected void onSuccess(LookGuardUserVo result) {
                        if (result != null) {
                            if (page == 1) {
                                setViewData(result);
                                LookGuardUserVo.PageInfoBean pageInfo = result.getPageInfo();
                                mAdapter.setNewData(result.getGuard_memberlist());
                                mBinding.refreshLayout.finishRefresh();
                                mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                            } else {
                                LookGuardUserVo.PageInfoBean pageInfo = result.getPageInfo();
                                mAdapter.addData(result.getGuard_memberlist());
                                mBinding.refreshLayout.finishLoadMore();
                                mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                            }
                        }
                    }

                    @Override
                    protected void onError(int errorCode, String errorMsg) {

                    }
                });
    }

}
