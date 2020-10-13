package com.deepsea.mua.voice.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.LinearLayout;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.core.websocket.WsocketListener;
import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.entity.RoomTags;
import com.deepsea.mua.stub.model.RoomModel;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.GridItemDecoration;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.RoomModeAdapter;
import com.deepsea.mua.voice.adapter.RoomTagsAdapter;
import com.deepsea.mua.voice.databinding.ActivityRoomModeSetBinding;
import com.deepsea.mua.voice.utils.ViewAnimUtils;
import com.deepsea.mua.voice.viewmodel.RoomCreateViewModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import javax.inject.Inject;

import okhttp3.Response;

/**
 * Created by JUN on 2019/4/1
 * 房间设置/房间模式选择
 */
public class RoomModeSetActivity extends BaseActivity<ActivityRoomModeSetBinding> {
    @Inject
    ViewModelFactory mViewModelFactory;
    private RoomCreateViewModel mViewModel;
    private RoomModeAdapter mModeAdapter;
    private RoomTagsAdapter mTagsAdapter;

    private boolean isExpand = true;
    private int mModeRvHeight;
    private boolean isAnimationRunning;
    private int modelId;
    private int tagId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_mode_set;
    }

    public static Intent newIntent(Context context, int modelId, int tagId) {
        Intent intent = new Intent(context, RoomModeSetActivity.class);
        intent.putExtra("modelId", modelId);
        intent.putExtra("tagId", tagId);
        return intent;
    }

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        modelId = intent.getIntExtra("modelId", -1);
        tagId = intent.getIntExtra("tagId", -1);
    }

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {
        modelId = savedInstanceState.getInt("modelId", -1);
        tagId = savedInstanceState.getInt("tagId", -1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("modelId", modelId);
        outState.putInt("tagId", tagId);
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RoomCreateViewModel.class);
        initTagsRv();
        initModeRv();
        getTagList();
        addSocketListener();
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.modeLayout, o -> {
            expandModeRv();
        });
        subscribeClick(mBinding.createTv, o -> {
            String mTagId = mTagsAdapter.getSelectTag();
            if (TextUtils.isEmpty(mTagId)) {
                toastShort("请选择房间标签");
                return;
            }
            String mModeId = mModeAdapter.getSelectModeId();
            if (TextUtils.isEmpty(mModeId)) {
                toastShort("请选择房间模式");
                return;
            }

            showProgress();

            mViewModel.setRoomTagAndModel(mTagId, mModeId);
        });
    }

    private void initTagsRv() {
        mTagsAdapter = new RoomTagsAdapter(mContext);
        mTagsAdapter.setOnItemClickListener((view, position) -> {
            mTagsAdapter.setSelectPos(position);
        });
        mBinding.labelRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mBinding.labelRv.addItemDecoration(new GridItemDecoration(4, ResUtils.dp2px(mContext, 8)));
        mBinding.labelRv.setNestedScrollingEnabled(false);
        mBinding.labelRv.setHasFixedSize(true);
        mBinding.labelRv.setAdapter(mTagsAdapter);
    }

    private void initModeRv() {
        mModeAdapter = new RoomModeAdapter(mContext);
        mModeAdapter.setOnItemClickListener((view, position) -> {
            RoomTags.ModeListBean item = mModeAdapter.getItem(position);
            mBinding.modeTv.setText(item.getRoom_mode());
            mModeAdapter.setSelectPos(position);

            mTagsAdapter.setNewData(item.getMode_tags());
        });
        mBinding.modeRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.modeRv.setNestedScrollingEnabled(false);
        mBinding.modeRv.setHasFixedSize(true);
        mBinding.modeRv.setAdapter(mModeAdapter);
    }

    private void getTagList() {
        mViewModel.getTagsList().observe(this, new BaseObserver<RoomTags>() {
            @Override
            public void onSuccess(RoomTags result) {
                if (result != null) {
                    List<RoomTags.ModeListBean> list = result.getMode_list();
                    mModeAdapter.setNewData(list);
                    if (!CollectionUtils.isEmpty(list)) {
                        mTagsAdapter.setNewData(list.get(0).getMode_tags());
                        mBinding.modeTv.setText(list.get(0).getRoom_mode());

                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getMode_id().equals(String.valueOf(modelId))) {
                            mModeAdapter.setSelectPos(i);
                            List<RoomTags.TagListBean> tagListBeans = list.get(i).getMode_tags();
                            mTagsAdapter.setNewData(tagListBeans);
                            for (int j = 0; j < tagListBeans.size(); j++) {
                                if (tagListBeans.get(j).getTag_id().equals(String.valueOf(tagId))) {
                                    mTagsAdapter.setSelectPos(j);
                                    return;
                                }
                            }
                            return;
                        }
                    }
                }
            }
        });
    }

    private void expandModeRv() {
        if (mModeRvHeight == 0) {
            mBinding.modeRv.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mModeRvHeight = mBinding.modeRv.getMeasuredHeight();
        }
        if (!isAnimationRunning) {
            int start = isExpand ? mModeRvHeight : 0;
            int end = isExpand ? 0 : mModeRvHeight;
            ViewAnimUtils.ofInt(mBinding.modeRv, ViewAnimUtils.Property.HEIGHT, 300, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimationRunning = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isExpand = !isExpand;
                    isAnimationRunning = false;
                    mBinding.modeArrowIv.setImageResource(isExpand ? R.drawable.ic_arrow_b_gray : R.drawable.ic_arrow_r_gray);
                }
            }, start, end);
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
                //修改房间名称
                case 43:
                    RoomModel roomModel = RoomController.getInstance().getRoomModel();
                    if (roomModel != null) {
                        roomModel.getRoomData().getRoomData().setRoomType(Integer.valueOf(mModeAdapter.getSelectModeId()));
                        roomModel.getRoomData().getRoomData().setRoomTags(Integer.valueOf(mTagsAdapter.getSelectTag()));
                    }
                    hideProgress();
                    toastShort("设置成功");
                    Intent intent = new Intent();
                    intent.putExtra("modelId", Integer.valueOf(mModeAdapter.getSelectModeId()));
                    intent.putExtra("modelSetName", String.valueOf(mModeAdapter.getSelectModeName()));
                    intent.putExtra("tagId", Integer.valueOf(mTagsAdapter.getSelectTag()));
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
            }
        }

        public void onFailure(Throwable t, Response response) {
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeSocketListener();
    }
}
