package com.deepsea.mua.voice.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.beauty.VideoManager;
import com.android.beauty.capture.VideoCaptureFrame;
import com.android.beauty.connector.SinkConnector;
import com.android.beauty.faceunity.FURenderer;
import com.android.beauty.faceunity.entity.FaceBean;
import com.android.beauty.faceunity.utils.Constant;
import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.entity.socket.send.JoinRoom;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.ForbiddenStateUtils;
import com.deepsea.mua.stub.utils.FriendsUtils;
import com.deepsea.mua.stub.utils.ProfileUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.GuardHeaderAdapter;
import com.deepsea.mua.voice.databinding.LayoutMicroFaceBinding;
import com.deepsea.mua.voice.utils.AppConstant;
import com.deepsea.mua.stub.utils.SongStateUtils;
import com.deepsea.mua.voice.utils.ISVGAParser;
import com.deepsea.mua.voice.utils.MatchMakerUtils;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.AgoraVideoFrame;
import io.agora.rtc.video.VideoCanvas;

/**
 * Created by JUN on 2019/10/17
 */
public class MicroFaceView extends FrameLayout {

    private static final String TAG = "MicroFaceView";

    private LayoutMicroFaceBinding mBinding;

    private Context mContext;
    private boolean needBeauty = false;
    private OnMicUserListener onMicUserListener;
    private OnApplauseListener onApplauseListener;
    private OnMpClickListener onMpClickListener;


    public interface OnApplauseListener {
        void applauseSinger(WsUser user);
    }

    public interface OnMpClickListener {
        void onClick(WsUser user);
    }

    public void setOnMpClickListener(OnMpClickListener onMpClickListener) {
        this.onMpClickListener = onMpClickListener;
    }

    public void setOnApplauseListener(OnApplauseListener onApplauseListener) {
        this.onApplauseListener = onApplauseListener;
    }

    public interface OnMicUserListener {
        void sendSingleGift(WsUser user);

        void addFriend(String uid, String imgUrl, String nickName);

        void fansList(WsUser user);

        void sendOneRose(String userId);

        void downMicro(String userId, int level, int number);//下麦

        void microOperate(String userId, boolean isOpen);//闭麦/禁言
    }

    public void setOnMicUserListener(OnMicUserListener onMicUserListener) {
        this.onMicUserListener = onMicUserListener;
    }

    public MicroFaceView(@NonNull Context context) {
        this(context, null);
    }

    public MicroFaceView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    FURenderer mFURenderer = null;

    public MicroFaceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_micro_face, this, true);
    }

    private void toggleView(boolean isMatchMaker) {
        ViewBindUtils.setVisible(mBinding.userLayout, !isMatchMaker);
        ViewBindUtils.setVisible(mBinding.matchMakerLayout, isMatchMaker);
        ViewBindUtils.setText(mBinding.labelTv, isMatchMaker ? "主持" : "嘉宾");
    }

    RoomData.MicroInfosBean mMicroInfoBean = null;

    public void setMicroData(RoomData.MicroInfosBean bean) {

        WsUser user = bean.getUser();
        mBinding.fmGroup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMpClickListener != null) {
                    onMpClickListener.onClick(user);
                }
            }
        });
        if (user != null) {
            if (bean.isRelease()) {
                setSurfaceViewVisible();
                return;
            }
            removeSurfaceView();
            toggleView(bean.getType() == 0);
            ViewBindUtils.setVisible(mBinding.rlUserTopOperate, true);
//            ViewBindUtils.setVisible(mBinding.rlRose, true);
            //红娘
            GlideUtils.circleImage(mBinding.matchAvatar, user.getHeadImageUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            mBinding.matchMaker.setText(user.getName());
            //普通
            GlideUtils.circleImage(mBinding.avatarIv, user.getHeadImageUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            mBinding.nickTv.setText(user.getName());
            mBinding.infoTv.setText(ProfileUtils.getProfile(user.getAge(), user.getStature(), user.getSex()));
            mBinding.rlDefaultEmpty.setVisibility(GONE);
            mBinding.rlEmptyMan.setVisibility(GONE);
            mBinding.rlEmptyWoman.setVisibility(GONE);
            boolean isMine = UserUtils.getUser().getUid().equals(user.getUserId());
            ViewBindUtils.setVisible(mBinding.rlOperateSendgift, !isMine);
            ViewBindUtils.setVisible(mBinding.ivGiveRose, !isMine);
            ViewBindUtils.setVisible(mBinding.rlDownMp, MatchMakerUtils.isRoomOwner() || isMine);
            mBinding.ivGiveRose.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMicUserListener != null) {
                        onMicUserListener.sendOneRose(user.getUserId());
                    }
                }
            });

            mBinding.rlOperateSendgift.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMicUserListener != null) {
                        onMicUserListener.sendSingleGift(user);
                    }
                }
            });
            //玫瑰
            ViewBindUtils.setText(mBinding.tvRose, bean.getRolse() != 0 ? String.valueOf(bean.getRolse()) : "");
            boolean visible = (!isMine && !FriendsUtils.getInstance().isMyFriend(user.getUserId()));
            ViewBindUtils.setVisible(mBinding.addfriendTv, visible);
            ViewBindUtils.setVisible(mBinding.addfriendCommonuserTv, visible);
            mBinding.addfriendTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMicUserListener != null) {
                        onMicUserListener.addFriend(user.getUserId(), user.getHeadImageUrl(), user.getName());
                    }
                }
            });
            mBinding.addfriendCommonuserTv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onMicUserListener != null) {
                        onMicUserListener.addFriend(user.getUserId(), user.getHeadImageUrl(), user.getName());
                    }
                }
            });
//            ViewBindUtils.setVisible(mBinding.rlOperateApplause, !UserUtils.getUser().getUid().equals(user.getUserId()) && SongStateUtils.getSingleton2().getConsertUserId().equals(user.getUserId()));
//            ViewBindUtils.RxClicks(mBinding.rlOperateApplause, o -> {
//                if (onApplauseListener != null) {
//                    onApplauseListener.applauseSinger(user);
//                }
//            });
            ViewBindUtils.RxClicks(mBinding.rlDownMp, o -> {
                if (onMicUserListener != null) {
                    onMicUserListener.downMicro(user.getUserId(), bean.getType(), bean.getNumber());
                }
            });

            boolean isDisabledMicro = ForbiddenStateUtils.getForbiddenMicState(user.getUserId());
            boolean isForbiddenLB = ForbiddenStateUtils.getForbiddenLBstate(user.getUserId());
            upDataMicroState(isDisabledMicro && isForbiddenLB);
            ViewBindUtils.RxClicks(mBinding.ivCloseMp, o -> {
                if (onMicUserListener != null) {
                    onMicUserListener.microOperate(user.getUserId(), mBinding.ivCloseMp.isSelected());
                    upDataMicroState(!mBinding.ivCloseMp.isSelected());

                }
            });
            mBinding.tvUserInfo.setText(ProfileUtils.getProfile(user.getAge(), user.getStature(), user.getSex()));
            List<String> list = new ArrayList<>();
            setRankHeards(bean.getRoseRanks(), user.getUserId());
            if (guardHeaderAdapter != null) {
                guardHeaderAdapter.setOnItemClickListener(new BaseBindingAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (onMicUserListener != null) {
                            user.setType(bean.getType());
                            user.setNumber(bean.getNumber());
                            onMicUserListener.fansList(user);
                        }
                    }
                });
            }
            ViewBindUtils.setVisible(mBinding.llMicroOperate, true);

            addSurfaceView(Integer.parseInt(user.getUserId()));
        } else {
            releaseLayout(bean.getType());
        }
    }

    public void releaseLayout(int myType) {
        ViewBindUtils.setVisible(mBinding.llMicroOperate, false);
        mBinding.ivGiveRose.setVisibility(GONE);
        mBinding.ivGiveRose.setOnClickListener(null);
        mBinding.tvUserInfo.setText("");
        mBinding.matchMakerLayout.setVisibility(GONE);
        mBinding.userLayout.setVisibility(GONE);
        JoinRoom joinRoom = com.deepsea.mua.stub.utils.AppConstant.getInstance().getJoinRoom();
        int type = myType;
        boolean isRoomOwner = MatchMakerUtils.isRoomOwner();
        if (joinRoom != null && (type == 1) && !SongStateUtils.getSingleton2().isChangeView()) {
            mBinding.rlDefaultEmpty.setVisibility(GONE);
            mBinding.rlEmptyMan.setVisibility(VISIBLE);
            mBinding.rlEmptyWoman.setVisibility(GONE);
            ViewBindUtils.setText(mBinding.tvAppmicroDesc, isRoomOwner ? "邀请上麦" : "申请上麦");
//                ViewBindUtils.setVisible(mBinding.rlManAppmicroDesc, !isRoomOwner);
            ViewBindUtils.setVisible(mBinding.tvManMicroCost, !isRoomOwner);
            int microCost = AppConstant.getInstance().getMicroCost();
            ViewBindUtils.setText(mBinding.tvManMicroCost, String.format("%d朵玫瑰", microCost));
        } else if (joinRoom != null && type == 2 && !SongStateUtils.getSingleton2().isChangeView()) {
            mBinding.rlDefaultEmpty.setVisibility(GONE);
            mBinding.rlEmptyMan.setVisibility(GONE);
            mBinding.rlEmptyWoman.setVisibility(VISIBLE);
            ViewBindUtils.setText(mBinding.tvWomanMicroDesc, isRoomOwner ? "邀请女嘉宾" : "女嘉宾专属位");
            ViewBindUtils.setVisible(mBinding.ivWomenMicroBg, true);
        } else {
            mBinding.tvDefaultDesc.setText(isRoomOwner ? "邀请上麦" : "申请上麦");
            mBinding.rlDefaultEmpty.setVisibility(VISIBLE);
            mBinding.rlEmptyMan.setVisibility(GONE);
            mBinding.rlEmptyWoman.setVisibility(GONE);
        }
        mBinding.rlOperateSendgift.setOnClickListener(null);
        mBinding.rlOperateSendgift.setVisibility(GONE);
        mBinding.addfriendCommonuserTv.setOnClickListener(null);
        mBinding.addfriendCommonuserTv.setVisibility(GONE);
        mBinding.addfriendTv.setOnClickListener(null);
        mBinding.addfriendTv.setVisibility(GONE);
//        mBinding.rlOperateApplause.setVisibility(GONE);
        removeSurfaceView();
    }

    public void setRoleNum(int roseNum) {
        ViewBindUtils.setText(mBinding.tvRose, String.valueOf(roseNum));
    }

    GuardHeaderAdapter guardHeaderAdapter = null;

    private void setRankHeards(List<String> heads, String userId) {
//        boolean musicMode = SongStateUtils.getSingleton2().isChangeView();
//        String singerId = SongStateUtils.getSingleton2().getConsertUserId();
//        boolean isHideHeads = musicMode && !userId.equals(singerId);
//        ViewBindUtils.setVisible(mBinding.ivRankUsers, !isHideHeads);
        guardHeaderAdapter = new GuardHeaderAdapter(mContext);
        mBinding.ivRankUsers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mBinding.ivRankUsers.setAdapter(guardHeaderAdapter);
        guardHeaderAdapter.setNewData(CollectionUtils.sortRankHeads(heads));
        guardHeaderAdapter.setOnItemClickListener(new BaseBindingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

    }

    public void updateRankHeads(List<String> heads) {
        if (guardHeaderAdapter != null) {
            guardHeaderAdapter.setNewData(heads);
        }

    }


    private int mImageWidth;
    private int mImageHeight;
    int myTextureId;
    private SinkConnector<VideoCaptureFrame> mEffectHandler = new SinkConnector<VideoCaptureFrame>() {
        @Override
        public int onDataAvailable(VideoCaptureFrame data) {
            mImageHeight = data.mFormat.getHeight();
            mImageWidth = data.mFormat.getWidth();
            myTextureId = data.mTextureId;
            try {
                myTextureId = mFURenderer.onDrawFrame(needBeauty, data.mImage, data.mTextureId,
                        data.mFormat.getWidth(), data.mFormat.getHeight());

                sendRecordingData(myTextureId, data.mImage, data.mFormat.getPixelFormat(), data.mTexMatrix, data.mRotation, data.mTimeStamp / Constant.NANO_IN_ONE_MILLI_SECOND);
            } catch (Exception e) {

            }
            return myTextureId;
        }
    };

    protected void sendRecordingData(int texId, final byte[] data, int format, float[] tex_matrix, int mRotation, final long timeStamp) {
        if (mVideoManager != null && mVideoManager.getmVideoSource() != null) {
            if (mVideoManager.getmVideoSource().getConsumer() != null) {

                mVideoManager.getmVideoSource().getConsumer().consumeByteArrayFrame(data, AgoraVideoFrame.FORMAT_TEXTURE_2D, mImageWidth, mImageHeight, mRotation, timeStamp);
            } else
                mVideoManager.connectEffectHandler(mEffectHandler);
        }
    }

    private GLSurfaceView mGLSurfaceViewLocal;
    private FrameLayout mLocalViewContainer;
    private VideoManager mVideoManager;
    int height = 1024;
    int width = 760;
    private int flag = 0;


    public void setVidio() {
        FaceBean bean = AppConstant.getInstance().getFaceBean();
        needBeauty = bean != null;

        mFURenderer = new FURenderer
                .Builder(mContext)
                .createEGLContext(false)
                .setNeedFaceBeauty(needBeauty)
                .setOnFUDebugListener(new FURenderer.OnFUDebugListener() {
                    @Override
                    public void onFpsChange(double fps, double renderTime) {
                        Log.d(TAG, "FURenderer.onFpsChange, fps: " + fps + ", renderTime: " + renderTime);
                    }
                })
                .inputTextureType(FURenderer.FU_ADM_FLAG_EXTERNAL_OES_TEXTURE)
                .build();
        mFURenderer.onSurfaceCreated();
        mFURenderer.setFaceParam(bean);
        if (mGLSurfaceViewLocal != null && mGLSurfaceViewLocal.getParent() != null) {
            ((ViewGroup) mGLSurfaceViewLocal.getParent()).removeView(mGLSurfaceViewLocal);
        }

        mGLSurfaceViewLocal = new GLSurfaceView(mContext);
//        mGLSurfaceViewLocal.setBackgroundColor(getResources().getColor(R.color.btn_pressed_green_solid));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mGLSurfaceViewLocal.setOutlineProvider(new TextureVideoViewOutlineProvider(DisplayUtil.dip2px(mContext, 8)));
            mGLSurfaceViewLocal.setClipToOutline(true);
        }
        mLocalViewContainer = mBinding.faceContainer;
        if (mBinding.faceContainer != null) {
            mBinding.faceContainer.removeAllViews();
        }
        mLocalViewContainer.addView(mGLSurfaceViewLocal,
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);

        mVideoManager = VideoManager.createInstance(mContext);
//        mVideoManager.deallocate();
//        mHandler.sendEmptyMessageDelayed(1, 800);
        mVideoManager.allocate(width, height, -1, com.android.beauty.constant.Constant.CAMERA_FACING_FRONT);
        mVideoManager.setRenderView(mGLSurfaceViewLocal);
        mVideoManager.connectEffectHandler(mEffectHandler);
//        mVideoManager.detachToRTCEngine();
        mVideoManager.attachToRTCEngine(AgoraClient.create().rtcEngine());
//        mVideoManager.stopCapture();
        mVideoManager.startCapture();


    }

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//
//                    break;
//            }
//        }
//    };

    boolean mFUInit = false;
    SurfaceView surfaceV;

    private void addSurfaceView(int uid) {
        if (TextUtils.equals(String.valueOf(uid), UserUtils.getUser().getUid())) {
            Log.d("mainfaceview", "mine：" + uid);

            boolean hasFaceBeauty = SharedPrefrencesUtil.getData(mContext, "hasFaceBeauty", "hasFaceBeauty", com.deepsea.mua.stub.utils.Constant.isBeautyOpen);
            if (hasFaceBeauty) {
                setVidio();
            } else {
                AgoraClient.create().release();
                surfaceV = AgoraClient.create().prepare();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    surfaceV.setOutlineProvider(new TextureVideoViewOutlineProvider(DisplayUtil.dip2px(mContext, 8)));
                    surfaceV.setClipToOutline(true);
                }
                if (surfaceV.getParent() != null) {
                    ((ViewGroup) surfaceV.getParent()).removeView(surfaceV);
                }
                mBinding.faceContainer.addView(surfaceV, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

            }
        } else {

            surfaceV = RtcEngine.CreateRendererView(AppUtils.getApp());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                surfaceV.setOutlineProvider(new TextureVideoViewOutlineProvider(DisplayUtil.dip2px(mContext, 8)));
                surfaceV.setClipToOutline(true);
            }
//            if (surfaceV.getParent() != null) {
//                mBinding.faceContainer.removeView(surfaceV);
//            }
            Log.d("mainfaceview", "other：" + uid);
            mBinding.faceContainer.addView(surfaceV, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            AgoraClient.create().rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));

        }
    }

    public void release() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                if (mHandler != null) {
//                    mHandler.removeMessages(1);
//                    mHandler.removeCallbacksAndMessages(null);
//                    mHandler = null;
//                }
                Log.d("exit", "release");
//        mVideoManager = VideoManager.createInstance(mContext);
                if (mVideoManager != null) {
                    Log.d("exit", "stopCapture");
                    mVideoManager.connectEffectHandler(null);
                    mVideoManager.detachToRTCEngine();
                    mVideoManager.stopCapture();
                    mVideoManager.deallocate();
                    mVideoManager = null;
                }
                if (mFURenderer != null) {
                    mFURenderer.onSurfaceDestroyed();
                    mFURenderer = null;
                }

            }
        }).start();

        if (mLocalViewContainer != null) {
            mLocalViewContainer.removeAllViews();
            mGLSurfaceViewLocal = null;
        }
        mFUInit = false;
        if (surfaceV != null && surfaceV.getParent() != null) {
            ((ViewGroup) surfaceV.getParent()).removeView(surfaceV);
            surfaceV = null;
        }
        removeSurfaceView();
        System.gc();
    }


    public void removeSurfaceView() {
//        release();
        try {
            mBinding.faceContainer.removeAllViews();
            if (mFURenderer != null) {
                mFURenderer.onSurfaceDestroyed();
//                mVideoManager.connectEffectHandler(null);
                mVideoManager.detachToRTCEngine();
                mVideoManager.stopCapture();
            }
        } catch (Exception e) {

        }
    }

    public void setSurfaceViewVisible() {
        int viewCount = mBinding.faceContainer.getChildCount();
        if (viewCount > 0) {
            mBinding.faceContainer.getChildAt(0).setVisibility(GONE);
        }
    }


    public void updateFriendState(RoomData.MicroInfosBean bean) {
        Log.d("AG_EX_AV", JsonConverter.toJson(bean));
        try {
            WsUser user = bean.getUser();
            boolean isMine = !UserUtils.getUser().getUid().equals(user.getUserId());
            boolean visible = (isMine && !FriendsUtils.getInstance().isMyFriend(user.getUserId()));
            ViewBindUtils.setVisible(mBinding.addfriendTv, visible);
            ViewBindUtils.setVisible(mBinding.addfriendCommonuserTv, visible);
        } catch (Exception e) {

        }
    }

    public void upDataMicroState(boolean isOpenMicro) {
        mBinding.ivCloseMp.setSelected(isOpenMicro);
    }
}
