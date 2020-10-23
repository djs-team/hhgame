package com.deepsea.mua.stub.controller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.BuildConfig;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.dialog.AutoHideLoading;
import com.deepsea.mua.stub.model.RoomModel;
import com.deepsea.mua.stub.network.HttpConst;
import com.deepsea.mua.stub.network.HttpHelper;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ArouterUtils;
import com.deepsea.mua.stub.utils.AudioPlaybackManager;
import com.deepsea.mua.stub.utils.MobEventUtils;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by JUN on 2019/4/13
 */
public class RoomJoinController implements IRoomController.JoinRoomListener {

    private Context mContext;
    private String mRoomId;
    private AutoHideLoading mLoadingDialog;

    //连接超时
    private static final long CONNECT_TIME_OUT = 8000;
    //点击间隔
    private long mPreClickTime = 0;
    private static final int sClickInterval = 1500;
    private boolean isInvite;
    private int inviteMicroId;
    private int free;
    private int cost;

    public interface CallBack {
        void onResult();
    }

    private CallBack mCallBack;


    @Inject
    public RoomJoinController() {
    }

    public void startJump(String roomId, int inviteMicroId, boolean isInvite, int free, int cost, int roomMode, Context context) {
        this.isInvite = isInvite;
        this.inviteMicroId = inviteMicroId;
        this.free = free;
        this.cost = cost;
        startJump(roomId, context);
    }

    public void startJump(String roomId, Context context) {
        AudioPlaybackManager.getInstance().stopAudio();//动态语音播放 停止
        this.mRoomId = roomId;
        this.mContext = context;

        if (System.currentTimeMillis() - mPreClickTime < sClickInterval) {
            return;
        }
        mPreClickTime = System.currentTimeMillis();

        RoomModel roomModel = RoomController.getInstance().getRoomModel();
        if (roomModel != null && TextUtils.equals(roomId, roomModel.getRoomId())) {

            if (RoomController.getInstance().inRoom()) {
                onSuccess();
                return;
            }
        } else {
            RoomMiniController.getInstance().destroy();
        }
        if (!isInvite) {
            showLoading();
        }
        if (!HttpUtils.IsNetWorkEnable(mContext)) {
            hideLoading();
            return;
        }
        RoomController.getInstance().init();
        RoomController.getInstance().setOnJoinRoomListener(this);
        getSocketUrlAndConnect();
    }

    public void startJump(String roomId, int roomMode, Context context, CallBack callBack) {
        this.mCallBack = callBack;
        AudioPlaybackManager.getInstance().stopAudio();//动态语音播放 停止
        this.mRoomId = roomId;
        this.mContext = context;

        if (System.currentTimeMillis() - mPreClickTime < sClickInterval) {
            return;
        }
        mPreClickTime = System.currentTimeMillis();

        RoomModel roomModel = RoomController.getInstance().getRoomModel();
        if (roomModel != null && TextUtils.equals(roomId, roomModel.getRoomId())) {

            if (RoomController.getInstance().inRoom()) {
                onSuccess();
                return;
            }
        } else {
            RoomMiniController.getInstance().destroy();
        }
        if (!isInvite) {
            showLoading();
        }
        if (!HttpUtils.IsNetWorkEnable(mContext)) {
            hideLoading();
            return;
        }
        RoomController.getInstance().init();
        RoomController.getInstance().setOnJoinRoomListener(this);
        getSocketUrlAndConnect();
    }

    private void getSocketUrlAndConnect() {
        showLog("获取socket地址");

        //获取wsurl
        Call<String> call = HttpHelper.instance().getApi(RetrofitApi.class).getWsUrl(AddressCenter.getAddress().getSocketUrl());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    showLog("获取socket地址成功 " + response.body());
                    RoomController.getInstance().startConnect(response.body(), mRoomId);
                } else {
                    try {
                        onError(IRoomController.JoinError.DEFAULT_CODE, HttpConst.SERVER_ERROR);
                        String error = "";
                        if (response.errorBody() != null) {
                            error = response.errorBody().string();
                        }
                        showLog("获取socket地址失败 " + error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                onError(IRoomController.JoinError.DEFAULT_CODE, HttpConst.SERVER_ERROR);
                showLog("获取socket地址失败 " + t.getMessage());
            }
        });
    }


    private void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new AutoHideLoading(mContext);
            mLoadingDialog.setTimeOut(CONNECT_TIME_OUT);
        }
        mLoadingDialog.show();
    }

    private void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void destroy() {
        hideLoading();
    }

    private void showLog(String message) {
        Log.d("mua", message);
    }

    @Override
    public void onSuccess() {
        hideLoading();
        RoomMiniController.getInstance().removeFloatWindow();
        String path = ArouterConst.PAGE_ROOM;
//        ArouterUtils.build(path).withBoolean("isInvite", isInvite).withInt("inviteMicroId", inviteMicroId).withInt("free", free).withInt("cost", cost).navigation();
        Intent intent = new Intent();
        intent.setClassName("com.hehegame.chess", "com.deepsea.mua.voice.activity.RoomActivity");
        intent.putExtra("isInvite", isInvite);
        intent.putExtra("inviteMicroId", inviteMicroId);
        intent.putExtra("free", free);
        intent.putExtra("cost", cost);
        mContext.startActivity(intent);

        MobEventUtils.onJoinRoom(mContext);
        if (mCallBack != null) {
            mCallBack.onResult();
        }
    }

    @Override
    public void onError(int code, String msg) {
        if (!TextUtils.isEmpty(msg)) {
            ToastUtils.showToast(msg);
        }
        hideLoading();
//        if (code == IRoomController.JoinError.PARENT_LOCK) {
//            ToastUtils.showToast(msg);
//        }
    }
}
