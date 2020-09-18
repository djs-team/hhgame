package com.deepsea.mua.stub.client.agora;

import io.agora.rtc.IRtcEngineEventHandler;

/**
 * Created by JUN on 2019/8/7
 */
public abstract class IAgoraEventHandler extends IRtcEngineEventHandler {

    /**
     * 加入频道回调
     *
     * @param channel 频道名
     * @param uid     用户 ID
     * @param elapsed 从 joinChannel 开始到发生此事件过去的时间（毫秒)
     */
    public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
    }

    /**
     * 重新加入频道回调
     * 有时候由于网络原因，客户端可能会和服务器失去连接，SDK 会进行自动重连，自动重连成功后触发此回调方法
     *
     * @param channel 频道名
     * @param uid     用户 ID
     * @param elapsed 从开始重连到重连成功的时间（毫秒）
     */
    public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
    }

    /**
     * 提示频道内谁正在说话以及说话者音量的回调
     *
     * @param speakers    每个说话者的用户 ID 和音量信息的数组
     * @param totalVolume
     */
    public void onAudioVolumeIndication(IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {
    }

    /**
     * 远端用户/主播加入当前频道回调
     *
     * @param uid
     * @param elapsed
     */
    public void onUserJoined(int uid, int elapsed) {
    }
   public void onConnectionStateChanged(int state, int reason){

    }

    /**
     * 远端用户（通信模式）/主播（直播模式）离开当前频道回调
     *
     * @param uid    主播 ID
     * @param reason 离线原因
     */
    public void onUserOffline(int uid, int reason) {
    }

    /**
     * 远端用户停止/恢复发送音频流回调
     *
     * @param uid   用户 ID
     * @param muted
     */
    public void onUserMuteAudio(int uid, boolean muted) {
    }

    /**
     * 麦克风状态已改变回调
     * {@link io.agora.rtc.RtcEngine#enableLocalAudio(boolean) enableLocalAudio}: Whether to enable the microphone to create the local audio stream.
     *
     * @param enabled
     */
    public void onMicrophoneEnabled(boolean enabled) {
    }

    /**
     * 用户角色已切换回调
     *
     * @param oldRole
     * @param newRole
     */
    public void onClientRoleChanged(int oldRole, int newRole) {
    }

    /**
     * 离开频道回调
     *
     * @param stats
     */
    public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
    }

    /**
     * 发生警告回调
     *
     * @param warn
     */
    public void onWarning(int warn) {
    }

    /**
     * 发生错误回调
     *
     * @param err
     */
    public void onError(int err) {
    }
}
