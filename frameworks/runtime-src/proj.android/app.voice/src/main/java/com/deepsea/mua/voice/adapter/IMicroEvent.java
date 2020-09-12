package com.deepsea.mua.voice.adapter;

import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;

import java.util.List;

/**
 * Created by JUN on 2019/8/5
 */
public interface IMicroEvent {

    int VoiceWave = 100;
    int NormalEmojiAnima = 101;
    int GameEmojiAnima = 102;
    int GameResult = 103;
    int GiftAnima = 104;
    int MicroUserChanged = 105;
    int CountDown = 106;
    int LockMicro = 107;
    int ForbiddenMicro = 108;
    int HeartValue = 109;
    int RoseValue = 110;
    int AddFriendState = 111;
    int UpdateRanks = 112;
    int ReleaseLayout = 113;
    int UpMpMicro = 114;//禁言 闭麦

    /**
     * 获取麦位索引
     *
     * @param type   麦位类型
     * @param number 麦位
     * @return
     */
    int getItemPos(int type, int number);

    /**
     * 音波
     *
     * @param pos
     */
    void onVoiceWave(int pos);

    /**
     * 普通表情动画
     *
     * @param pos
     * @param url
     */
    void onNormalEmojiAnima(int pos, String url);

    /**
     * 游戏表情动画
     *
     * @param pos
     * @param url
     */
    void onGameEmojiAnima(int pos, String url);

    /**
     * 游戏表情结果
     *
     * @param pos
     * @param url
     */
    void onGameResult(int pos, String url);

    /**
     * 礼物动画
     *
     * @param pos
     * @param url
     */
    void onGiftAnima(int pos, String url);

    /**
     * 麦位用户信息变化
     *
     * @param pos
     * @param user
     */
    void onMicroUserChanged(int pos, WsUser user);

    /**
     * 倒计时
     *
     * @param pos
     * @param countDown 倒计时时间点
     * @param duration  倒计时时长
     */
    void onCountDown(int pos, String countDown, int duration);

    /**
     * 锁麦
     *
     * @param pos
     * @param lock
     */
    void onLockMicro(int pos, boolean lock);

    /**
     * 禁麦
     *
     * @param pos
     * @param forbidden
     */
    void onForbiddenMicro(int pos, boolean forbidden);

    /**
     * 心动值
     *
     * @param pos
     * @param heartValue
     */
    void onHeartValue(int pos, int heartValue);

    /**
     * 更新麦位信息
     *
     * @param bean
     */
    void onUpdateMicro(RoomData.MicroInfosBean bean);

    /**
     * 更新是否是好友状态
     */
    void updateFriendState(int pos, RoomData.MicroInfosBean bean);


    void updateRanks(int pos, List<String> ranks);


    void upDateMpMicro(int level,int num,boolean isOpenMp);
}
