package com.deepsea.mua.app.im.mua;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.adapter.FriendRequestAdapter;
import com.deepsea.mua.app.im.databinding.ActivityFriendRequestBinding;
import com.deepsea.mua.app.im.viewmodel.FriendRequestViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.ApplyFriendBean;
import com.deepsea.mua.stub.entity.ApplyFriendListBean;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * @zu 好友申请列表
 */
public class FriendRequestActivity extends BaseActivity<ActivityFriendRequestBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private FriendRequestViewModel mViewModel;

    private FriendRequestAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friend_request;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(FriendRequestViewModel.class);
        initRecyclerView();
        initRefreshLayout();
    }


    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    private void initRecyclerView() {
        mAdapter = new FriendRequestAdapter(mContext);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setMyClickListener(new FriendRequestAdapter.OnMyClickListener() {
            @Override
            public void onAgreeClick(int pos, ApplyFriendBean bean) {
                String touid = bean.getUser_id();
                String signature = SignatureUtils.signByToken();
                String gift_id = bean.getGift_id();
                String apply_id = bean.getApply_id();
                Map<String, String> map = new HashMap<>();
                map.put("touid", touid);
                map.put("signature", signature);
                if (!gift_id.equals("0")) {
                    map.put("gift_id", gift_id);
                }
                map.put("apply_id", apply_id);
                onAgreeEvent(pos, map, String.valueOf(bean.getIs_friend()));
            }

            @Override
            public void onRefuseClick(int pos, ApplyFriendBean bean) {
                String touid = bean.getUser_id();
                String signature = SignatureUtils.signByToken();
                String gift_id = bean.getGift_id();
                Map<String, String> map = new HashMap<>();
                map.put("touid", touid);
                map.put("signature", signature);
                if (!gift_id.equals("0")) {
                    map.put("gift_id", gift_id);
                }
                map.put("apply_id", bean.getApply_id());
                onRefuseEvent(pos, map);
            }
        });
    }

    protected void sendTextMessage(String toChatUsername, String myNickName) {
        EMMessage message = EMMessage.createTxtSendMessage(myNickName + "接受了好友请求，开聊吧", toChatUsername);
        EMClient.getInstance().chatManager().sendMessage(message);

    }

    private void onAgreeEvent(int position, Map<String, String> map, String isFriend) {
        mViewModel.passFriendly(map, isFriend).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                if (result != null) {
                    ApplyFriendBean bean = mAdapter.getItem(position);
                    bean.setMstatus("1");
                    mAdapter.notifyItemChanged(position);
                    //send message
                    sendTextMessage(bean.getUser_id(), UserUtils.getUser().getNickname());
                    Log.d("friend", "onAgreeEvent-success" + result.getCode() + ";" + result.getDesc());

                }

            }

            @Override
            public void onError(String msg, int code) {
                Log.d("friend", "onAgreeEvent-error" + code + ";" + msg);
                toastShort(msg);
            }
        });
    }

    private void onRefuseEvent(int position, Map<String, String> map) {
        mViewModel.delFriendly(map).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                if (result != null) {
                    toastShort(result.getDesc());
                    mAdapter.getItem(position).setMstatus("2");
                    mAdapter.notifyItemChanged(position);
                    Log.d("friend", "onRefuseEvent-success" + result.getCode() + ";" + result.getDesc());

                }

            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
                Log.d("friend", "onRefuseEvent-error" + code + ";" + msg);
            }
        });
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.refresh("1").observe(this, new BaseObserver<ApplyFriendListBean>() {
                @Override
                public void onSuccess(ApplyFriendListBean result) {
                    if (result != null) {
                        Log.d("friend", "refresh-success");
                        ApplyFriendListBean.PageinfoBean pageInfo = result.getPageInfo();
                        mAdapter.setNewData(result.getList());
                        mBinding.refreshLayout.finishRefresh();
                        mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    Log.d("friend", "refresh-error" + code + ";" + msg);

                    toastShort(msg);
                    mBinding.refreshLayout.finishRefresh();
                }
            });
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mViewModel.loadMore("1").observe(this, new BaseObserver<ApplyFriendListBean>() {
                @Override
                public void onSuccess(ApplyFriendListBean result) {
                    if (result != null) {
                        ApplyFriendListBean.PageinfoBean pageInfo = result.getPageInfo();
                        mAdapter.addData(result.getList());
                        mBinding.refreshLayout.finishLoadMore();
                        mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    toastShort(msg);
                    mBinding.refreshLayout.finishLoadMore();
                    mViewModel.pageNumber--;
                }
            });
        });
        mBinding.refreshLayout.autoRefresh();
    }
}
