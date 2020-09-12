package com.deepsea.mua.voice.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.deepsea.mua.core.websocket.WsocketListener;
import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.RoomDesc;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ActivityRoomSetBinding;
import com.deepsea.mua.voice.viewmodel.RoomSetViewModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.inject.Inject;

import okhttp3.Response;

/**
 * Created by JUN on 2019/4/1
 * 房间设置
 */
public class RoomSetActivity extends BaseActivity<ActivityRoomSetBinding> {

    @Inject
    ViewModelProvider.Factory mFactory;
    private RoomSetViewModel mViewModel;

    private String roomId;
    private int userIdentity;

    private String mWelStr;
    private String mPlayStr;
    private String modeName;
    private int modelId;
    private int tagId;

    public static Intent newIntent(Context context, String roomId, int userIdentity, int modelId, int tagId, String modeName) {
        Intent intent = new Intent(context, RoomSetActivity.class);
        intent.putExtra("roomId", roomId);
        intent.putExtra("userIdentity", userIdentity);
        intent.putExtra("modeName", modeName);
        intent.putExtra("modelId", modelId);
        intent.putExtra("tagId", tagId);
        return intent;
    }

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        roomId = intent.getStringExtra("roomId");
        modeName = intent.getStringExtra("modeName");
        userIdentity = intent.getIntExtra("userIdentity", 0);
        modelId = intent.getIntExtra("modelId", -1);
        tagId = intent.getIntExtra("tagId", -1);
    }

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {
        roomId = savedInstanceState.getString("roomId");
        modeName = savedInstanceState.getString("modeName");
        userIdentity = savedInstanceState.getInt("userIdentity");
        modelId = savedInstanceState.getInt("modelId", -1);
        tagId = savedInstanceState.getInt("tagId", -1);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_set;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mFactory).get(RoomSetViewModel.class);
        getRoomDesc();
        addSocketListener();
    }

    private void getRoomDesc() {
        mViewModel.roomDesc(roomId).observe(this,
                new BaseObserver<RoomDesc>() {
                    @Override
                    public void onSuccess(RoomDesc result) {
                        if (result != null) {
                            RoomDesc.RoomDescBean roomDesc = result.getRoom_desc();

                            int maxLength = 5;
                            String roomWelcomes = roomDesc.getRoom_welcomes();
                            mWelStr = roomWelcomes;
                            if (roomWelcomes != null && roomWelcomes.length() > maxLength) {
                                roomWelcomes = roomWelcomes.substring(0, 5) + "...";
                            }
                            String room_desc = roomDesc.getRoom_desc();
                            mPlayStr = room_desc;
                            if (room_desc != null && room_desc.length() > maxLength) {
                                room_desc = room_desc.substring(0, 5) + "...";
                            }
                            mBinding.roomNameTv.setText(roomDesc.getRoom_name());
                            mBinding.tvModeName.setText(modeName);
                            mBinding.welTv.setText(roomWelcomes);
                            mBinding.playTv.setText(room_desc);
                        }
                    }
                });
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
            int maxLength = 5;
            switch (msgId) {
                //修改房间名称
                case 50:
                    String name = object.get("Name").getAsString();
                    mBinding.roomNameTv.setText(name);
                    break;
                //玩法介绍
                case 52:
                    String room_desc = object.get("Desc").getAsString();
                    mPlayStr = room_desc;
                    if (room_desc != null && room_desc.length() > maxLength) {
                        room_desc = room_desc.substring(0, 5) + "...";
                    }
                    mBinding.playTv.setText(room_desc);
                    break;
                //欢迎语
                case 35:
                    String welcomMsg = object.get("WelcomMsg").getAsString();
                    mWelStr = welcomMsg;
                    if (welcomMsg != null && welcomMsg.length() > maxLength) {
                        welcomMsg = welcomMsg.substring(0, 5) + "...";
                    }
                    mBinding.welTv.setText(welcomMsg);
                    break;
                case 43:
                    break;
            }
        }

        public void onFailure(Throwable t, Response response) {
        }
    };

    @Override
    protected void initListener() {
        subscribeClick(mBinding.roomNameLayout, o -> {
            startActivity(RoomNameSetActivity.newIntent(mContext, mBinding.roomNameTv.getText().toString()));
        });
        subscribeClick(mBinding.playIntroLayout, o -> {
            startActivity(RoomWelSetActivity.newIntent(mContext, RoomWelSetActivity.TYPE_PLAY, mPlayStr));
        });
        subscribeClick(mBinding.welLayout, o -> {
            startActivity(RoomWelSetActivity.newIntent(mContext, RoomWelSetActivity.TYPE_WEL, mWelStr));
        });

        subscribeClick(mBinding.translateLayout, o -> {
            startActivityForResult(RoomModeSetActivity.newIntent(mContext, modelId, tagId), REQUEST_MODESET);
        });

    }

    private final int REQUEST_MODESET = 1001;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_MODESET && data != null) {
                modelId = data.getIntExtra("modelId", -1);
                tagId = data.getIntExtra("tagId", -1);
                modeName = data.getStringExtra("modelSetName");
                ViewBindUtils.setText(mBinding.tvModeName, modeName);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("roomId", roomId);
        outState.putString("modeName", modeName);
        outState.putInt("userIdentity", userIdentity);
        outState.putInt("modelId", modelId);
        outState.putInt("tagId", tagId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeSocketListener();
    }
}
