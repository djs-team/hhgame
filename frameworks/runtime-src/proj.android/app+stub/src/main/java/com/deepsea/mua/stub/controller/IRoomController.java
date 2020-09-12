package com.deepsea.mua.stub.controller;

import com.deepsea.mua.stub.entity.EmojiBean;
import com.deepsea.mua.stub.entity.ShareBeanItem;
import com.deepsea.mua.stub.entity.UserRedPacket;
import com.deepsea.mua.stub.entity.model.RoomMsgBean;
import com.deepsea.mua.stub.entity.socket.BaseMsg;
import com.deepsea.mua.stub.entity.socket.CountDown;
import com.deepsea.mua.stub.entity.socket.EmotionBean;
import com.deepsea.mua.stub.entity.socket.receive.JoinUser;
import com.deepsea.mua.stub.entity.socket.MicroSort;
import com.deepsea.mua.stub.entity.socket.MicroUser;
import com.deepsea.mua.stub.entity.socket.MpInvited;
import com.deepsea.mua.stub.entity.socket.OnlineUser;
import com.deepsea.mua.stub.entity.socket.ReceiveMessage;
import com.deepsea.mua.stub.entity.socket.receive.ReceivePresent;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.receive.BaseMicroMsg;
import com.deepsea.mua.stub.entity.socket.receive.DownMicroMsg;
import com.deepsea.mua.stub.entity.socket.receive.ShowGuardAnimationToClientParam;
import com.deepsea.mua.stub.entity.socket.receive.SongInfo;
import com.deepsea.mua.stub.entity.socket.receive.SyncMicroRose;
import com.deepsea.mua.stub.entity.socket.receive.UpMicroMsg;
import com.deepsea.mua.stub.entity.socket.receive.UpdateFinishTaskToClientParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateGuardSignToClientParam;
import com.deepsea.mua.stub.model.RoomModel;

import java.util.List;

/**
 * Created by JUN on 2019/8/21
 */
public interface IRoomController {

    //已连接
    int CONNECTED = 0x41;

    //未连接
    int DISCONNECT = 0x42;

    //加入房间
    int JOIN_ROOM = 0x43;

    //离开房间
    int LEVEL_ROOM = 0x44;

    //在房间中
    int IN_ROOM = 0x45;

    /**
     * 初始化
     */
    void init();

    /**
     * 获取房间model
     *
     * @return
     */
    RoomModel getRoomModel();

    /**
     * 获取当前socket状态
     *
     * @return
     */
    int getSocketStatus();

    /**
     * 是否在房间中
     *
     * @return
     */
    boolean inRoom();

    /**
     * 是否在麦位上
     *
     * @return
     */
    boolean isOnMphone();

    /**
     * 初始化环信聊天室
     *
     * @param chatRoomId
     */
    void initHyphenateChatRoom(String chatRoomId);

    /**
     * 获取环信未读消息数目
     *
     * @return
     */
    int getHyphenateUnreadCount();

    /**
     * 获取环信聊天室ID
     *
     * @return
     */
    String getChatRoomId();

    /**
     * 发送公屏文本消息
     *
     * @param text
     */
    void sendTextMsg(String text);

    /**
     * 发送公屏表情
     *
     * @param bean
     */
    void sendEmoJiMsg(EmojiBean.EmoticonBean bean);

    /**
     * 初始化声网聊天室
     */
    void initAgoraEngineAndJoinChannel();

    /**
     * 获取声网聊天室ID
     *
     * @return
     */
    String getAgoraChnnelId();

    void release();

    void destroy();

    /*
    ---------------------------------- WebSocket
     */

    /**
     * 连接socket
     *
     * @param url    socket url
     * @param roomId 房间id
     */
    void startConnect(String url, String roomId);

    /**
     * 处理加入房间事件
     *
     * @param listener
     */
    void setOnJoinRoomListener(JoinRoomListener listener);

    /**
     * Socket消息处理
     *
     * @param view
     */
    void setRoomView(IRoomView view);

    /**
     * 发送房间内socket消息
     *
     * @param obj
     * @param <T>
     * @return
     */
    <T extends BaseMsg> boolean sendRoomMsg(T obj);

    /**
     * 发送房间内socket消息
     *
     * @param text
     * @return
     */
    boolean sendRoomMsg(String text);

    /**
     * 发送socket消息
     *
     * @param text
     * @return
     */
    boolean sendNormalMsg(String text);

    /**
     * 更新用户昵称
     *
     * @param nickname
     */
    void updateSelfNick(String nickname);

    interface JoinError {
        int DEFAULT_CODE = 100;

        int PARENT_LOCK = 10;

        int SERVER_BUSY = 11;
    }

    interface JoinRoomListener {

        void onSuccess();

        void onError(int code, String msg);
    }

    interface IHyphenateAndAgora {
        void onHyphenateMessage(ReceiveMessage message);

        void onNewMessage(int unreadCount);

        void onClientRoleChanged(int oldRole, int newRole);

        void onAudioVolumeIndication(List<String> uids);
    }

    interface ISocketListener extends IHyphenateAndAgora {
        void onOpen();

        void onMessage(String text);

        void onReconnected();
    }

    interface IRoomView extends ISocketListener {

        /**
         * 点击消息体
         *
         * @param uid
         */
        void onMsgClick(String uid);

        void onMsgLongClick(String uName);

        /**
         * 添加公屏消息
         *
         * @param list
         */
        void onAddMsg(List<RoomMsgBean> list);

        /**
         * 房间热度值
         *
         * @param visitorNum
         */
        void onVisitorNum(String visitorNum);

        /**
         * 上麦
         *
         * @param bean
         */
        void onUpMicro(UpMicroMsg bean);

        /**
         * 下麦
         *
         * @param bean
         */
        void onDownMicro(DownMicroMsg bean);

        void onDownMicroSelf();

        /**
         * 禁麦、取消禁麦
         *
         * @param msg
         * @param isForbidden
         */
        void onForbiddenMicro(BaseMicroMsg msg, boolean isForbidden);

        /**
         * 锁麦、取消锁麦
         *
         * @param msg
         * @param isLock
         */
        void onLockMicro(BaseMicroMsg msg, boolean isLock);

        /**
         * 麦序变化
         */
        void onMicroSort();

        /**
         * 收到用户上麦请求
         */
        void receiveMicroRequest(MicroSort sort);

        void removeMicro();

        /**
         * 麦序类型变化
         *
         * @param isFreeMicro 自由麦序
         */
        void onMicroTypeChanged(boolean isFreeMicro);

        /**
         * 心动值开启、关闭
         *
         * @param isOpen
         */
        void onHeartValueOpened(boolean isOpen);

        /**
         * 赠礼
         *
         * @param code
         */
        void onMultiSend(int code);

        /**
         * 打赏
         *
         * @param code
         */
        void onSingleSend(int code);

        /**
         * 礼物事件分发
         *
         * @param bean
         */
        void onReceiveGift(ReceivePresent bean);

        /**
         * 麦位心动值变化
         *
         * @param msg
         * @param heartValue
         */
        void onHeartValue(BaseMicroMsg msg, int heartValue);

        /**
         * 倒计时
         *
         * @param bean
         */
        void onCountDown(CountDown bean);

        /**
         * 被踢出房间
         */
        void onKickRoom(String msg);

        /**
         * 用户信息
         *
         * @param user
         */
        void onUserInfo(MicroUser user);

        /**
         * 房间名称修改
         *
         * @param roomName
         */
        void roomNameChanged(String roomName);

        /**
         * 在线用户
         *
         * @param onlineUser
         */
        void onlineUser(OnlineUser onlineUser);

        /**
         * 排行榜
         *
         * @param list
         */
        void onRankList(List<String> list);

        /**
         * 发送动画表情回调
         */
        void onSendEmoJi();

        /**
         * 广播动画表情
         *
         * @param bean
         */
        void onShowEmoJi(EmotionBean bean);

        /**
         * 广播动画表情结果
         *
         * @param bean
         * @param url  结果url
         */
        void onEmoJiResult(EmotionBean bean, String url);

        /**
         * 房主信息更新
         */
        void onOwnerUpdated();

        /**
         * 余额不足
         */
        void onBalanceNoEnough();

        /**
         * 账户余额
         *
         * @param balance
         */
        void onBalance(String balance);

        /**
         * 麦位价格
         *
         * @param cost
         */
        void onMicroCost(String cost);

        /**
         * 收到上麦邀请
         *
         * @param bean
         */
        void onMpInvited(MpInvited bean);

        /**
         * 红娘下播
         */
        void onMatcherExited(int code, String msg);

        void onRequestMicroGuestChanged(int inMicroManNum, int inMicroWomanNum);

        void onOnlineGuestChanged(int onlineManNum, int onlineWomanNum);

        /**
         * 锁定房间--不在麦上退出
         */
        void lockRoomExitUnInMic();

        //退出房间
        void exitRoom();

        //重连
        void sendReConnetMsg();

        void upDataMicInfo(List<RoomData.MicroInfosBean> bean);

        void leaveSWChannel();

        void showAlert(int code, String msg);

        void onMicroDownExitRoom();


        void gotoCertification();//前去实名认证

        void resetInRoomInviteParam();

        void onInmicroSuccessCallback();//上麦成功回调

        void share(ShareBeanItem shareBeanItem);//上麦成功回调

        void startPlaySong(SongInfo param);//嘉宾收到播放歌曲的信息

        void syncPlaySongVolume(int volume);//同步音量

        void syncPlaySongPause(int isPause, int consertUserId);//同步暂停

        void syncPlaySongRePlay();//同步重唱

        void syncPlaySongCutSong(int state, int consertUserId);//同步切歌

        void onReconnectSuccess();//重连成功

        void syncSongStartDwonLoad();//开始下载

        void syncSongSuccessDwonLoad(int success);//下载成功

        void changeMicroView(SongInfo songInfo);

        void updateSongRankParam(int rank);

        void updateSongAppointmentNum(int num);

        void notifyAddFriend(String uid);//加好友通知

        void receiveLrc(String lrc);//同步歌词

        void syncDemandSong(String name);//预约歌曲同步给红娘

        void syncMicroRose(SyncMicroRose syncMicroRose);//同步麦位玫瑰数

        void updateExclusiveHint(String msg);//同步专属房间提示

        void updateWheatCards(int cardNum);//同步上麦卡数量

        void leaveRoomUserToOther(String userId);//当一个用户退出房间后，通知房间内其他人

        void updateGuardSign(UpdateGuardSignToClientParam bean);//同步守护标签

        void updateMicro();//当一个用户退出房间后，通知房间内其他人

        void GuardPersonJoinRoom(JoinUser isRoomGuard);//守护者进入房间

        void showGuardGif(ShowGuardAnimationToClientParam bean);//弹出守护动画

        void showTaskFinish(UpdateFinishTaskToClientParam bean);//任务完成

        void showRedPackageRule(String content);//红包规则

        void notifyRedPacketProgress(boolean visible, float progress);//红包进度

        void startRobRedPacket(String alert);//开始抢红包

        void  showRobRedpacketResultList(List<UserRedPacket> redPackets);//抢红包结果

        void  showCenterHtmlToast(String content);

    }
}
