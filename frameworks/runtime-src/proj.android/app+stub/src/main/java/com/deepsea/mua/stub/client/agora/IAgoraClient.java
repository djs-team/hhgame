package com.deepsea.mua.stub.client.agora;

import android.content.Context;
import android.view.SurfaceView;

import io.agora.rtc.Constants;
import io.agora.rtc.RtcEngine;

/**
 * Created by JUN on 2019/8/6
 */
public abstract class IAgoraClient {

    //用户角色
    protected int mRole = Constants.CLIENT_ROLE_AUDIENCE;

    protected int mAgoraStatus = NONE;

    public static final int NONE = 0x21;

    public static final int JOIN_ROOM = 0x22;

    public static final int LEVEL_ROOM = 0x23;

    public static final int IN_ROOM = 0x24;

    /**
     * 配置agora
     *
     * @param context
     * @param appId
     */
    public abstract void setUpAgora(Context context, String appId);

    /**
     * 初始化本地视图
     *
     * @return
     */
    public abstract SurfaceView prepare();

    /**
     * 释放资源
     */
    public abstract void release();

    /**
     * 获取声网状态
     *
     * @return
     */
    public int getAgoraStatus() {
        return mAgoraStatus;
    }

    /**
     * 修改声网状态
     *
     * @param agoraStatus
     */
    public void setAgoraStatus(int agoraStatus) {
        mAgoraStatus = agoraStatus;
    }

    /**
     * 获取声网客户端
     *
     * @return
     */
    public abstract RtcEngine rtcEngine();
    public abstract void releaseEngine();

    /**
     * 开、关本地美颜功能
     *
     * @param enable true    开启
     *               false   关闭
     */
    public abstract void enableBeautyEffect(boolean enable);

    /**
     * 获取当前角色
     *
     * @return {@link io.agora.rtc.Constants#CLIENT_ROLE_AUDIENCE}
     * {@link io.agora.rtc.Constants#CLIENT_ROLE_BROADCASTER}
     */
    public abstract int getRole();

    /**
     * 添加声网事件回调
     *
     * @param handler
     */
    public abstract void addAgoraEventHandler(IAgoraEventHandler handler);

    /**
     * 移除声网事件回调
     *
     * @param handler
     */
    public abstract void removeAgoraEventHandler(IAgoraEventHandler handler);

    /**
     * 清空声网事件回调
     */
    public abstract void clearAgoraEventHandler();

    /**
     * 设置音频编码配置
     *
     * @param profile
     * @param scenario
     */
    public abstract void setAudioProfile(int profile, int scenario);

    /**
     * 设置频道模式
     *
     * @param profile
     */
    public abstract void setChannelProfile(int profile);

    /**
     * 启用说话者音量提示
     *
     * @param interval 指定音量提示的时间间隔
     *                 返回音量提示的间隔，单位为毫秒。建议设置到大于 200 毫秒。最小不得少于 10 毫秒，否则会收不到 onAudioVolumeIndication 回调
     * @param smooth   平滑系数，指定音量提示的灵敏度。取值范围为 [0, 10]，建议值为 3，数字越大，波动越灵敏；数字越小，波动越平滑
     * @return 0：方法调用成 < 0：方法调用失败
     */
    public abstract int enableAudioVolumeIndication(int interval, int smooth);

    /**
     * 加入频道
     *
     * @param token        在 App 服务器端生成的用于鉴权的 Token
     * @param channelName  标识通话的频道名称，长度在 64 字节以内的字符串。以下为支持的字符集范围（共 89 个字符）
     * @param optionalInfo （非必选项）开发者需加入的任何附加信息。一般可设置为空字符串，或频道相关信息。该信息不会传递给频道内的其他用户
     * @param optionalUid  用户 ID，32 位无符号整数。建议设置范围：1 到 (2^32-1)，并保证唯一性
     * @return
     */
    public abstract int joinChannel(String token, String channelName, String optionalInfo, int optionalUid);


    /**
     * 停止/恢复发送本地音频流
     * 静音/取消静音。该方法用于允许/禁止往网络发送本地音频流
     * 成功调用该方法后，远端会触发{@link io.agora.rtc.IRtcEngineEventHandler#onUserMuteAudio(int, boolean)}  回调
     *
     * @param muted true：停止发送本地音频流
     *              false：继续发送本地音频流（默认）
     * @return
     */
    public abstract int muteLocalAudioStream(boolean muted);

    /**
     * 开/关本地音频采集
     * 当 App 加入频道时，它的语音功能默认是开启的。该方法可以关闭或重新开启本地语音功能，即停止或重新开始本地音频采集。
     * <p>
     * 该方法不影响接收或播放远端音频流，enableLocalAudio(false) 适用于只听不发的用户场景
     * <p>
     * 语音功能关闭或重新开启后，会收到回调{@link io.agora.rtc.IRtcEngineEventHandler#onMicrophoneEnabled(boolean)}
     *
     * @param enabled true：重新开启本地语音功能，即开启本地语音采集（默认）
     *                false：关闭本地语音功能，即停止本地语音采集
     * @return
     */
    public abstract int enableLocalAudio(boolean enabled);

    /**
     * 接收/停止接收所有音频流
     *
     * @param enabled true：停止接收所有远端音频流    false：继续接收所有远端音频流（默认）
     * @return
     */
    public abstract int setMuteRemoteAudioStream(boolean enabled);

    /**
     * 开/关本地视频采集
     * <p>
     * 该方法禁用或重新启用本地视频采集。不影响接收远端视频
     * <p>
     * 成功禁用或启用本地视频采集后，远端会触发 onUserEnableLocalVideo 回调
     * <p>
     * ****该方法设置的是内部引擎为启用或禁用状态，在 leaveChannel 后仍然有效。
     *
     * @param enabled
     * @return
     */
    public abstract int enableLocalVideo(boolean enabled);

    /**
     * 停止/恢复发送本地视频流
     *
     * @param muted true：不发送本地视频流 false：发送本地视频流（默认）
     * @return
     */
    public abstract int muteLocalVideoStream(boolean muted);

    //停止/恢复接收指定音频流。
    public abstract int muteRemoteAudioStream(int uid, boolean muted);

    /**
     * 开/关视频双流模式
     * <p>
     * 该方法设置单流（默认）或者双流模式。发送端开启双流模式后，接收端可以选择接收大流还是小流。其中，大流指高分辨率、高码率的视频流，小流指低分辨率、低码率的视频流
     *
     * @param enabled true：双流  false：单流（默认）
     * @return
     */

    public abstract int enableDualStreamMode(boolean enabled);

    /**
     * 设置直播场景下的用户角色
     * 在加入频道前，用户需要通过本方法设置观众（默认）或主播模式。在加入频道后，用户可以通过本方法切换用户模式
     * <p>
     * 直播模式下，如果你在加入频道后调用该方法切换用户角色，调用成功后，本地会触发 {@link io.agora.rtc.IRtcEngineEventHandler#onClientRoleChanged(int, int)} 回调；
     * 远端会触发 {@link io.agora.rtc.IRtcEngineEventHandler#onUserJoined(int, int) }
     * {@link io.agora.rtc.IRtcEngineEventHandler#onUserOffline(int, int)}(USER_OFFLINE_BECOME_AUDIENCE) 回调
     *
     * @param role 用户角色 CLIENT_ROLE_BROADCASTER(1)：直播频道中的主播
     *             CLIENT_ROLE_AUDIENCE(2)：直播频道中的观众
     * @return 0：方法调用成功 < 0：方法调用失败
     */
    public abstract int setClientRole(int role);

    /**
     * 离开频道
     * 成功调用该方法离开频道后，本地会触发 onLeaveChannel 回调；通信模式下的用户和直播模式下的主播离开频道后，远端会触发 onUserOffline 回调
     */
    public abstract void leaveChannel();
}
