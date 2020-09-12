package com.deepsea.mua.stub.client.agora;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;

import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.stub.client.app.AppClient;
import com.deepsea.mua.stub.utils.CacheUtils;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

/**
 * Created by JUN on 2019/8/6
 */
public class AgoraClient extends IAgoraClient {

    private RtcEngine mRtcEngine;
    private String mAppId;
    private AgoraEventCenter mEventCenter;

    private String mToken;
    private String mChannelName;
    private int mOptionalUid;

    private BeautyOptions mBeautyOptions = new BeautyOptions();

    public void setmBeautyOptions(BeautyOptions mBeautyOptions) {
        this.mBeautyOptions = mBeautyOptions;
    }

    private SurfaceView mLocalSurfaceView;

    private static volatile IAgoraClient mInstance;

    private AgoraClient() {
    }


    public static IAgoraClient create() {
        if (mInstance == null) {
            synchronized (AgoraClient.class) {
                if (mInstance == null) {
                    mInstance = new AgoraClient();
                }
            }
        }

        return mInstance;
    }

    @Override
    public void setUpAgora(Context context, String appId) {
        try {
//            if (mRtcEngine == null || !TextUtils.equals(mAppId, appId)) {
            this.mAppId = appId;
            this.mEventCenter = new AgoraEventCenter();
            clearAgoraEventHandler();
            mRtcEngine = RtcEngine.create(context.getApplicationContext(), mAppId, mEventCenter.mAgoraEventHandler);
            mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
//                        new VideoEncoderConfiguration.VideoDimensions(240, 288),
                    new VideoEncoderConfiguration.VideoDimensions(480, 576),
                    VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_10,
                    133,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
            mRtcEngine.enableWebSdkInteroperability(true);
            setAudioProfile(Constants.AUDIO_PROFILE_MUSIC_STANDARD, Constants.AUDIO_SCENARIO_GAME_STREAMING);
            setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            enableAudioVolumeIndication(500, 3);
            mRtcEngine.enableVideo();
            mRtcEngine.enableLocalVideo(true);
            addAgoraEventHandler(mEventHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public SurfaceView prepare() {
        if (mLocalSurfaceView == null) {
            mLocalSurfaceView = RtcEngine.CreateRendererView(AppUtils.getApp());
            int result = rtcEngine().setupLocalVideo(new VideoCanvas(mLocalSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
        }
        return mLocalSurfaceView;
    }

    @Override
    public void release() {
        mEventCenter.clearAgoraEventHandler();
        mToken = null;
        mChannelName = null;
        if (mLocalSurfaceView != null) {
            rtcEngine().setupLocalVideo(null);
            mLocalSurfaceView = null;
        }
    }

    private final IAgoraEventHandler mEventHandler = new IAgoraEventHandler() {
        @Override
        public void onUserJoined(int uid, int elapsed) {
            super.onUserJoined(uid, elapsed);
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            mAgoraStatus = IN_ROOM;
            mRole = Constants.CLIENT_ROLE_AUDIENCE;
        }

        @Override
        public void onClientRoleChanged(int oldRole, int newRole) {
            mRole = newRole;
        }

        @Override
        public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
            if (mAgoraStatus == IN_ROOM && AppClient.getInstance().isRunning()) {
                joinChannel(mToken, mChannelName, "", mOptionalUid);
            } else {
                mAgoraStatus = NONE;
                if (mEventCenter != null) {
                    mEventCenter.clearAgoraEventHandler();
                }

                mRole = Constants.CLIENT_ROLE_AUDIENCE;
            }
        }
    };

    @Override
    public RtcEngine rtcEngine() {
        return mRtcEngine;
    }

    @Override
    public void releaseEngine() {
        mRtcEngine = null;
        mAppId = "";
    }


    public void setmRtcEngine(RtcEngine mRtcEngine) {
        this.mRtcEngine = mRtcEngine;
    }

    @Override
    public void enableBeautyEffect(boolean enable) {
        if (mRtcEngine != null) {
            mRtcEngine.setBeautyEffectOptions(enable, mBeautyOptions);

        }
    }

    @Override
    public int getRole() {
        return mRole;
    }

    @Override
    public void addAgoraEventHandler(IAgoraEventHandler handler) {
        if (mEventCenter != null) {
            mEventCenter.addAgoraEventHandler(handler);
        }
    }

    @Override
    public void removeAgoraEventHandler(IAgoraEventHandler handler) {
        if (mEventCenter != null) {
            mEventCenter.removeAgoraEventHandler(handler);
        }
    }

    @Override
    public void clearAgoraEventHandler() {
        if (mEventCenter != null) {
            mEventCenter.clearAgoraEventHandler();
        }
    }

    @Override
    public void setAudioProfile(int profile, int scenario) {
        if (mRtcEngine != null) {
            mRtcEngine.setAudioProfile(profile, scenario);
        }
    }

    @Override
    public void setChannelProfile(int profile) {
        if (mRtcEngine != null) {
            mRtcEngine.setChannelProfile(profile);
        }
    }

    @Override
    public int enableAudioVolumeIndication(int interval, int smooth) {
        if (mRtcEngine != null) {
            return mRtcEngine.enableAudioVolumeIndication(interval, smooth, false);
        }
        return 0;
    }

    @Override
    public int joinChannel(String token, String channelName, String optionalInfo, int optionalUid) {

        if (mRtcEngine != null) {
            this.mAgoraStatus = JOIN_ROOM;
            this.mToken = token;
            this.mChannelName = channelName;
            this.mOptionalUid = optionalUid;
            int result = mRtcEngine.joinChannel(token, channelName, optionalInfo, optionalUid);
            if (result < 0) {
                mAgoraStatus = NONE;
            }
            return result;
        }
        return 0;
    }


    /**
     * 开/关本地音频发送
     *
     * @param muted true：麦克风静音   false：取消静音（默认）
     * @return
     */
    @Override
    public int muteLocalAudioStream(boolean muted) {
        int result = -1;
        if (mRtcEngine != null) {
            result = mRtcEngine.muteLocalAudioStream(muted);
        }
        return result;
    }

    @Override
    public int enableLocalAudio(boolean enabled) {
        int result = -1;
        if (mRtcEngine != null) {
            result = mRtcEngine.enableLocalAudio(enabled);
        }
        return result;
    }

    @Override
    public int setMuteRemoteAudioStream(boolean flag) {
        return mRtcEngine.muteAllRemoteAudioStreams(flag);
    }

    @Override
    public int enableLocalVideo(boolean enabled) {
        int result = -1;
        if (mRtcEngine != null) {
            result = mRtcEngine.enableLocalVideo(enabled);
        }
        return result;
    }

    @Override
    public int muteLocalVideoStream(boolean muted) {
        int result = -1;
        if (mRtcEngine != null) {
            result = mRtcEngine.muteLocalVideoStream(muted);
        }
        return result;
    }

    @Override
    public int muteRemoteAudioStream(int uid, boolean muted) {
        int result = -1;
        if (mRtcEngine != null) {
            result = mRtcEngine.muteRemoteAudioStream(uid, muted);
        }
        return result;
    }

    @Override
    public int enableDualStreamMode(boolean enabled) {
        int result = -1;
        if (mRtcEngine != null) {
            result = mRtcEngine.enableDualStreamMode(enabled);
        }
        return result;
    }

    @Override
    public int setClientRole(int role) {
        int result = -1;
        if (mRtcEngine != null) {
            result = mRtcEngine.setClientRole(role);
        }
        return result;
    }

    @Override
    public void leaveChannel() {
        if (mRtcEngine != null) {
            mAgoraStatus = LEVEL_ROOM;
            mRtcEngine.leaveChannel();
        }
    }

}
