package com.deepsea.mua.voice.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.entity.RoomTagListBean;
import com.deepsea.mua.stub.entity.RoomTags;
import com.deepsea.mua.stub.entity.VoiceRoomBean;
import com.deepsea.mua.stub.entity.socket.send.JoinRoom;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.GridItemDecoration;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.RoomFunctionSelectAdapter;
import com.deepsea.mua.voice.adapter.RoomModeAdapter;
import com.deepsea.mua.voice.adapter.RoomNewTagsAdapter;
import com.deepsea.mua.voice.adapter.RoomTagsAdapter;
import com.deepsea.mua.voice.adapter.RoomTypeSelectAdapter;
import com.deepsea.mua.voice.databinding.ActivityRoomCreateNewBinding;
import com.deepsea.mua.voice.viewmodel.RoomCreateViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/2
 */
public class RoomCreateNewActivity extends BaseActivity<ActivityRoomCreateNewBinding> {

    @Inject
    ViewModelFactory mViewModelFactory;
    private RoomCreateViewModel mViewModel;
    @Inject
    RoomJoinController mRoomJump;
    private boolean isAudioMicro = false;//是否允许上麦
    private boolean isExclusive = false;//是否专属房间
    private boolean is_open_red_packet = true;
    private boolean is_open_break_egg = true;
    private boolean is_open_pick_song = true;
    private boolean is_open_media_library = true;
    private boolean is_open_video_frame = true;
    private int roomMode = 5;
    private String roomId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_create_new;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RoomCreateViewModel.class);
        initModel();
        initFunction();
        initTagsRv();
        initOther();
        getTagList();
    }

    private void initOther() {

        VoiceRoomBean.RoomInfoBean roomInfo = (VoiceRoomBean.RoomInfoBean) getIntent().getSerializableExtra("roomInfo");
        roomId = roomInfo.getRoom_id();
        isAudioMicro = roomInfo.getIs_close_camera().equals("1");
        isExclusive = roomInfo.getIs_exclusive_room().equals("1");
        is_open_red_packet = roomInfo.getIs_open_red_packet().equals("1");
        is_open_break_egg = roomInfo.getIs_open_break_egg().equals("1");
        is_open_media_library = roomInfo.getIs_open_media_library().equals("1");
        is_open_pick_song = roomInfo.getIs_open_pick_song().equals("1");
        is_open_video_frame = roomInfo.getIs_open_video_frame().equals("1");
        if (isAudioMicro) {
            mBinding.llAudioMicroYes.setSelected(true);
            mBinding.llAudioMicroNo.setSelected(false);
        } else {
            mBinding.llAudioMicroYes.setSelected(false);
            mBinding.llAudioMicroNo.setSelected(true);
        }
        if (isExclusive) {
            mBinding.llExclusiveRoomYes.setSelected(true);
            mBinding.llExclusiveRoomNo.setSelected(false);
        } else {
            mBinding.llExclusiveRoomYes.setSelected(false);
            mBinding.llExclusiveRoomNo.setSelected(true);
        }
        ViewBindUtils.setText(mBinding.nameEdit, roomInfo.getRoom_name());
        roomMode = Integer.valueOf(roomInfo.getRoom_type());
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.llModeHelp, o -> {
            startActivity(new Intent(mContext, RoomModelHelpActivity.class));
        });
        subscribeClick(mBinding.createTv, o -> {
            if (TextUtils.isEmpty(mBinding.nameEdit.getText().toString().trim())) {
                toastShort("请输入房间名称");
            }
            openRoom();
        });
        subscribeClick(mBinding.llAudioMicroYes, o -> {
            isAudioMicro = true;
            mBinding.llAudioMicroYes.setSelected(true);
            mBinding.llAudioMicroNo.setSelected(false);
        });
        subscribeClick(mBinding.llAudioMicroNo, o -> {
            isAudioMicro = false;
            mBinding.llAudioMicroYes.setSelected(false);
            mBinding.llAudioMicroNo.setSelected(true);
        });
        subscribeClick(mBinding.llExclusiveRoomYes, o -> {
            isExclusive = true;
            mBinding.llExclusiveRoomYes.setSelected(true);
            mBinding.llExclusiveRoomNo.setSelected(false);
        });
        subscribeClick(mBinding.llExclusiveRoomNo, o -> {
            isExclusive = false;
            mBinding.llExclusiveRoomYes.setSelected(false);
            mBinding.llExclusiveRoomNo.setSelected(true);
        });
    }

    private void openRoom() {
        showProgress();
        JoinRoom joinRoom = new JoinRoom();
        joinRoom.setRoomName(mBinding.nameEdit.getText().toString());
        joinRoom.setCloseCamera(isAudioMicro);
        joinRoom.setExclusiveRoom(isExclusive);
        joinRoom.setOpenRedPacket(is_open_red_packet);
        joinRoom.setOpenBreakEgg(is_open_break_egg);
        joinRoom.setOpenMediaLibrary(is_open_media_library);
        joinRoom.setOpenPickSong(is_open_pick_song);
        joinRoom.setOpenVideoFrame(is_open_video_frame);
        joinRoom.setRoomMode(roomMode);
        AppConstant.getInstance().setJoinRoom(joinRoom);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                boolean hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", Constant.isBeautyOpen);
//                if (!hasFaceBeauty || AppConstant.getInstance().isRtcEngineDestroy()) {
//                    AgoraClient.create().release();
//                    AgoraClient.create().setUpAgora(getApplicationContext(), "e0972168ff254d7aa05501cd85204692");
//                }
//            }
//        }).start();
        mRoomJump.startJump(roomId, roomMode, mContext, new RoomJoinController.CallBack() {
            @Override
            public void onResult() {
                finish();
            }
        });

    }

    RoomTypeSelectAdapter roomModeAdapter;

    private void initModel() {
        roomModeAdapter = new RoomTypeSelectAdapter(mContext);
        mBinding.rvType.setLayoutManager(new GridLayoutManager(mContext, 3));
        mBinding.rvType.setAdapter(roomModeAdapter);
        roomModeAdapter.setOnItemClickListener(new BaseBindingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                changeModeValue(position);
            }
        });
    }

    private void changeModeValue(int position) {
        roomModeAdapter.setSelected(position);
        RoomTagListBean.ModeListBean modeData = roomModeAdapter.getItem(position);
        mTagsAdapter.setNewData(modeData.getMode_tags());
        String mode_id = roomModeAdapter.getData().get(position).getMode_id();
        boolean isBlindDate = mode_id.equals("5");
//        ViewBindUtils.setVisible(mBinding.llExclusiveRoom, isBlindDate);
        if (!isBlindDate) {
            isExclusive = false;
            mBinding.llExclusiveRoomNo.setSelected(true);
        }
        roomMode = Integer.valueOf(mode_id);
    }

    RoomFunctionSelectAdapter functionSelectAdapter;

    private void initFunction() {
        functionSelectAdapter = new RoomFunctionSelectAdapter(mContext);
        mBinding.rvFunction.setLayoutManager(new GridLayoutManager(mContext, 2));
        mBinding.rvFunction.setAdapter(functionSelectAdapter);
        functionSelectAdapter.setOnItemClickListener(new BaseBindingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Map<String, Boolean> booleanMap = functionSelectAdapter.getBooleanMap();
                String func = functionSelectAdapter.getData().get(position).getFunc();
                boolean selected = booleanMap.get(func);
                booleanMap.put(func, !selected);
                functionSelectAdapter.notifyDataSetChanged();
                initFuncValue(func, !selected);
            }
        });
    }

    private void initFuncValue(String func, boolean selected) {
        switch (func) {
            case "is_open_red_packet":
                is_open_red_packet = selected;
                break;
            case "is_open_break_egg":
                is_open_break_egg = selected;
                break;
            case "is_open_pick_song":
                is_open_pick_song = selected;
                break;
            case "is_open_media_library":
                is_open_media_library = selected;
                break;
            case "is_open_video_frame":
                is_open_video_frame = selected;
                break;
        }

    }

    private RoomNewTagsAdapter mTagsAdapter;

    private void initTagsRv() {
        mTagsAdapter = new RoomNewTagsAdapter(mContext);
        mTagsAdapter.setOnItemClickListener((view, position) -> {
            RoomTagListBean.ModeListBean.ModeTagsBean item = mTagsAdapter.getItem(position);
            mTagsAdapter.setSelectPos(position);
        });
        mBinding.labelRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mBinding.labelRv.addItemDecoration(new GridItemDecoration(4, ResUtils.dp2px(mContext, 8)));
        mBinding.labelRv.setNestedScrollingEnabled(false);
        mBinding.labelRv.setHasFixedSize(true);
        mBinding.labelRv.setAdapter(mTagsAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgress();
    }

    private void getTagList() {
        mViewModel.getNewTagsList().observe(this, new BaseObserver<RoomTagListBean>() {
            @Override
            public void onSuccess(RoomTagListBean result) {
                if (result != null) {
                    List<RoomTagListBean.ModeListBean> mode_list = result.getMode_list();
                    List<RoomTagListBean.RoomFuncBean> func_list = result.getRoom_func();
                    for (RoomTagListBean.RoomFuncBean bean : func_list) {
                        if (bean.getFunc().equals("is_open_red_packet")) {
                            bean.setSelected(is_open_red_packet);
                        } else if (bean.getFunc().equals("is_open_break_egg")) {
                            bean.setSelected(is_open_break_egg);
                        } else if (bean.getFunc().equals("is_open_pick_song")) {
                            bean.setSelected(is_open_pick_song);
                        } else if (bean.getFunc().equals("is_open_media_library")) {
                            bean.setSelected(is_open_media_library);
                        } else if (bean.getFunc().equals("is_open_video_frame")) {
                            bean.setSelected(is_open_video_frame);
                        }
                    }
                    functionSelectAdapter.setNewData(func_list);
                    roomModeAdapter.setNewData(mode_list);

                    if (!CollectionUtils.isEmpty(mode_list)) {
                        mTagsAdapter.setNewData(mode_list.get(0).getMode_tags());
                    }
                    int defaultMode = 0;
                    for (int i = 0; i < mode_list.size(); i++) {
                        if (roomMode == Integer.valueOf(mode_list.get(i).getMode_id())) {
                            defaultMode = i;
                        }
                    }
                    changeModeValue(defaultMode);

                }
            }
        });
    }

}
