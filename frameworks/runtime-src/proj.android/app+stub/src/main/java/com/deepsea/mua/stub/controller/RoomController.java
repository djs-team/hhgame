package com.deepsea.mua.stub.controller;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.deepsea.mua.core.utils.AppUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.core.websocket.WsocketListener;
import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.app.ActivityCache;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.client.agora.IAgoraEventHandler;
import com.deepsea.mua.stub.client.hyphenate.HyphenateClient;
import com.deepsea.mua.stub.client.hyphenate.IEMMessageListener;
import com.deepsea.mua.stub.client.hyphenate.IHyphenateClient;
import com.deepsea.mua.stub.client.soket.SocketCons;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.entity.EmojiBean;
import com.deepsea.mua.stub.entity.ShareBeanItem;
import com.deepsea.mua.stub.entity.model.RoomMsgBean;
import com.deepsea.mua.stub.entity.socket.BaseMsg;
import com.deepsea.mua.stub.entity.socket.CountDown;
import com.deepsea.mua.stub.entity.socket.EmotionBean;
import com.deepsea.mua.stub.entity.socket.ForbiddenMsg;
import com.deepsea.mua.stub.entity.socket.receive.AccountUpdateMsgList;
import com.deepsea.mua.stub.entity.socket.receive.BaseMicroMsgList;
import com.deepsea.mua.stub.entity.socket.receive.DownMicroMsgList;
import com.deepsea.mua.stub.entity.socket.receive.JoinUser;
import com.deepsea.mua.stub.entity.socket.KickRoomBean;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.MicroSort;
import com.deepsea.mua.stub.entity.socket.MicroUser;
import com.deepsea.mua.stub.entity.socket.MpInvited;
import com.deepsea.mua.stub.entity.socket.OnlineUser;
import com.deepsea.mua.stub.entity.socket.ReceiveMessage;
import com.deepsea.mua.stub.entity.socket.receive.MicroTopArg;
import com.deepsea.mua.stub.entity.socket.receive.MicroTopArgList;
import com.deepsea.mua.stub.entity.socket.receive.NotifyOnlineHeadImageParam;
import com.deepsea.mua.stub.entity.socket.receive.ReceivePresent;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.RoomManager;
import com.deepsea.mua.stub.entity.socket.RoomRanks;
import com.deepsea.mua.stub.entity.socket.SmashBean;
import com.deepsea.mua.stub.entity.socket.receive.SmashEggBean;
import com.deepsea.mua.stub.entity.socket.UpdateSongRankParam;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.entity.socket.receive.AccountUpdateMsg;
import com.deepsea.mua.stub.entity.socket.receive.BaseMicroMsg;
import com.deepsea.mua.stub.entity.socket.receive.BaseReMsg;
import com.deepsea.mua.stub.entity.socket.receive.DownMicroMsg;
import com.deepsea.mua.stub.entity.socket.receive.GetRedPacketPlayDescParam;
import com.deepsea.mua.stub.entity.socket.receive.GuestNumMsg;
import com.deepsea.mua.stub.entity.socket.receive.JoinRoomMsg;
import com.deepsea.mua.stub.entity.socket.receive.JoinUserList;
import com.deepsea.mua.stub.entity.socket.receive.LeaveRoomParam;
import com.deepsea.mua.stub.entity.socket.receive.NextSongParam;
import com.deepsea.mua.stub.entity.socket.receive.NotifyAddFriendToClientParam;
import com.deepsea.mua.stub.entity.socket.receive.NotifyHelpToUserParam;
import com.deepsea.mua.stub.entity.socket.receive.NotifyRedPacketProgressParam;
import com.deepsea.mua.stub.entity.socket.receive.NotifyRedPacketResultToClientParam;
import com.deepsea.mua.stub.entity.socket.receive.PlaySongParam;
import com.deepsea.mua.stub.entity.socket.receive.ReceivePresentList;
import com.deepsea.mua.stub.entity.socket.receive.ShowGuardAnimationToClientParam;
import com.deepsea.mua.stub.entity.socket.receive.SmashEggBeanList;
import com.deepsea.mua.stub.entity.socket.receive.StartPlaySongParam;
import com.deepsea.mua.stub.entity.socket.receive.SyncDemandSongParam;
import com.deepsea.mua.stub.entity.socket.receive.SyncMicroRose;
import com.deepsea.mua.stub.entity.socket.receive.SyncMicroRoseList;
import com.deepsea.mua.stub.entity.socket.receive.SyncSongStateParam;
import com.deepsea.mua.stub.entity.socket.receive.SyncSongVoiceParam;
import com.deepsea.mua.stub.entity.socket.receive.TaskItemMsg;
import com.deepsea.mua.stub.entity.socket.receive.UpMicroMsg;
import com.deepsea.mua.stub.entity.socket.receive.UpMicroMsgList;
import com.deepsea.mua.stub.entity.socket.receive.UpdateAppointmentSongListParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateFinishTaskToClientParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateGuardSignToClientParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateHintParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateSongLyricParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateUserBalanceParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateWheatCards;
import com.deepsea.mua.stub.entity.socket.send.JoinRoom;
import com.deepsea.mua.stub.entity.socket.send.SendTokenMsg;
import com.deepsea.mua.stub.model.RoomModel;
import com.deepsea.mua.stub.network.HttpConst;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.ForbiddenStateUtils;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.InMicroMemberUtils;
import com.deepsea.mua.stub.utils.MobEventUtils;
import com.deepsea.mua.stub.utils.SongStateUtils;
import com.deepsea.mua.stub.utils.TimeUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static io.agora.rtc.Constants.CONNECTION_STATE_FAILED;

/**
 * Created by JUN on 2019/8/21
 */
public class RoomController implements IRoomController, RoomMsgHandler.OnMsgEventListener {

//    private static final String TAG = "RoomController";

    private static final String AUDIO_PERMISSION = "android.permission.RECORD_AUDIO";
    private static final String CAMERA_PERMISSION = "android.permission.CAMERA";

    public static final int DEFAULT_MSG_MAX_NUM = 100;

    public void setmAgoraChannelJoined(boolean mAgoraChannelJoined) {
        this.mAgoraChannelJoined = mAgoraChannelJoined;
    }

    private boolean mAgoraChannelJoined; //是否加入声网频道

    private int mSocketStatus = DISCONNECT;


    //环信聊天室ID
    private String mChatRoomId;
    //声网ID
    private String mAgoraChannelId = "";


    public void setmRoomId(String mRoomId) {
        this.mRoomId = mRoomId;
    }

    //房间ID
    private String mRoomId;

    private JoinRoomListener mJoinListener;
    private IRoomView mView;

    private RoomModel mRoomModel;


    public List<TaskItemMsg> getTaskItemMsgList() {
        return taskItemMsgList;
    }

    public void setTaskItemMsgList(List<TaskItemMsg> taskItemMsgList) {
        this.taskItemMsgList = taskItemMsgList;
    }

    private List<TaskItemMsg> taskItemMsgList = new ArrayList<>();
    private RoomMsgHandler mMsgHandler;
    private MicroSortHandler mSortHandler;

    private SocketReconnect mSocketReconnect;

    private Handler mHandler = new Handler(Looper.getMainLooper());


    public static void setmInstance(RoomController mInstance) {
        RoomController.mInstance = mInstance;
    }

    private static volatile RoomController mInstance;
    private boolean isRetry = false;

    public void setRetry(boolean retry) {
        isRetry = retry;
    }

    public boolean isRetry() {
        return isRetry;
    }

    private RoomController() {
        initWebSocket();
    }

    public String getmAgoraChannelId() {
        return mAgoraChannelId;
    }


    public static RoomController getInstance() {
        if (mInstance == null) {
            synchronized (RoomController.class) {
                if (mInstance == null) {
                    mInstance = new RoomController();
                }
            }
        }

        return mInstance;
    }

    private void initWebSocket() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> {
        }
//                Logg.d(TAG, message)
        );
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        WsocketManager.create().setOkHttpClient(new OkHttpClient().newBuilder()
                .addInterceptor(httpLoggingInterceptor)
//                .pingInterval(10, TimeUnit.SECONDS)
//                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build());
        WsocketManager.create().setNeedReconnect(false);
    }

    @Override
    public void init() {
        mAgoraChannelJoined = false;
        if (mRoomModel == null) {
            mRoomModel = new RoomModel();
            mRoomModel.setMsgs(new ArrayList<>());
        }
        if (mMsgHandler == null) {
            mMsgHandler = new RoomMsgHandler(AppUtils.getApp());
            mMsgHandler.setOnMsgEventListener(this);
        }
        if (mSortHandler == null) {
            mSortHandler = new MicroSortHandler();
        }
        if (mSocketReconnect != null) {
            mSocketReconnect = null;
        }
        if (mSocketReconnect == null) {
            mSocketReconnect = new SocketReconnect();
        }
    }

    @Override
    public RoomModel getRoomModel() {
        return mRoomModel;
    }

    public MicroSortHandler getMicroSortHandler() {
        return mSortHandler;
    }

    @Override
    public int getSocketStatus() {
        return mSocketStatus;
    }

    @Override
    public boolean inRoom() {
        return (mSocketStatus == IN_ROOM) || (mSocketStatus == 67);
    }

    @Override
    public boolean isOnMphone() {
        if (mRoomModel != null) {
            return mRoomModel.isOnMp();

        }
        return false;
    }

    @Override
    public void initHyphenateChatRoom(String chatRoomId) {
        this.mChatRoomId = chatRoomId;
        HyphenateClient.getInstance().joinChatRoom(chatRoomId, new IHyphenateClient.HyphenateCallback() {
            @Override
            public void apply() {
//                Logg.d(TAG, "加入环信聊天室成功");
            }

            @Override
            public void onError(int code, String msg) {
//                Logg.d(TAG, "加入环信聊天室失败");

                initHyphenateChatRoom(chatRoomId);
            }
        });
        HyphenateClient.getInstance().addMessageListener(mEMListener);
    }

    @Override
    public int getHyphenateUnreadCount() {
        int count = 0;
        if (HyphenateClient.getInstance().isLoggedIn()) {
            count = EMClient.getInstance().chatManager().getUnreadMessageCount();
        }
        return count;
    }

    @Override
    public String getChatRoomId() {
        return mChatRoomId;
    }

    @Override
    public void sendTextMsg(String text) {
        JoinUser user = mRoomModel.getUser();
        if (user == null)
            return;
        EMMessage message = EMMessage.createTxtSendMessage(text, mChatRoomId);
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setAttribute("XYType", "TEXT");
        message.setAttribute("XYUser", JsonConverter.toJson(user));
        HyphenateClient.getInstance().sendMessage(message, new IHyphenateClient.HyphenateCallback() {
            @Override
            public void apply() {
                Log.d("msg", "消息成功回调 " + message.toString());
                MobEventUtils.onSendMsg(AppUtils.getApp());
                ReceiveMessage sendMsg = new ReceiveMessage();
                sendMsg.setMsg(text);
                sendMsg.setUser(user);
                onHyphenateMessage(sendMsg);
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("MsgId", 130);
                jsonObject.addProperty("Msg", text);
                sendNormalMsg(jsonObject.toString());

            }

            @Override
            public void onError(int code, String msg) {
//                Logg.d(TAG, "消息发送失败 " + msg);
                if (code == 602) {
                    initHyphenateChatRoom(mChatRoomId);
                }
                showToast("消息发送失败 " + code);
            }
        });
    }

    public void sendMsg(String text) {
        JoinUser user = mRoomModel.getUser();
//        Logg.d(TAG, "发送消息 " + text + "  " + user);
        if (user == null)
            return;
        EMMessage message = EMMessage.createTxtSendMessage(text, mChatRoomId);
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setAttribute("XYType", "TEXT");
        message.setAttribute("XYUser", JsonConverter.toJson(user));
        HyphenateClient.getInstance().sendMessage(message, new IHyphenateClient.HyphenateCallback() {
            @Override
            public void apply() {
//                Logg.d(TAG, "消息成功回调 " + message.toString());
                MobEventUtils.onSendMsg(AppUtils.getApp());
                ReceiveMessage sendMsg = new ReceiveMessage();
//                if (user != null) {
//                    user.setGuardSign(mRoomModel.getRoomData().getGuardSign());
//                }
                sendMsg.setMsg(text);
                sendMsg.setUser(user);
                onHyphenateMessage(sendMsg);

            }

            @Override
            public void onError(int code, String msg) {
//                Logg.d(TAG, "消息发送失败 " + msg);
                if (code == 602) {
                    initHyphenateChatRoom(mChatRoomId);
                }
                showToast("消息发送失败 " + code);
            }
        });
    }

    @Override
    public void sendEmoJiMsg(EmojiBean.EmoticonBean bean) {
        JoinUser user = mRoomModel.getUser();
        if (user == null)
            return;
        EMMessage message = EMMessage.createTxtSendMessage(" ", mChatRoomId);
        message.setChatType(EMMessage.ChatType.ChatRoom);
        message.setAttribute("XYType", "EMOJI");
        message.setAttribute("XYUser", JsonConverter.toJson(user));
        message.setAttribute("XYEmoji", JsonConverter.toJson(bean));
        HyphenateClient.getInstance().sendMessage(message, new IHyphenateClient.HyphenateCallback() {
            @Override
            public void apply() {
//                Logg.d(TAG, "消息成功回调 " + message.toString());
                MobEventUtils.onSendEmoji(AppUtils.getApp());
                ReceiveMessage sendMsg = new ReceiveMessage();
                sendMsg.setEmojianim(bean.getAnimation());
                sendMsg.setEmojiurl(bean.getFace_image());
                sendMsg.setUser(user);
                onHyphenateMessage(sendMsg);
            }

            @Override
            public void onError(int code, String msg) {
//                Logg.d(TAG, "消息发送失败 " + msg);
                showToast("消息发送失败 " + code);
            }
        });
    }

    @Override
    public void initAgoraEngineAndJoinChannel() {
        if (mAgoraChannelJoined) {
            return;
        }
        int cameraPermission = ContextCompat.checkSelfPermission(AppUtils.getApp(), CAMERA_PERMISSION);
        int audioPermission = ContextCompat.checkSelfPermission(AppUtils.getApp(), AUDIO_PERMISSION);
        if (cameraPermission == audioPermission && cameraPermission == PackageManager.PERMISSION_GRANTED) {
            if (!TextUtils.isEmpty(mAgoraChannelId) && UserUtils.getUser() != null) {
                mAgoraChannelJoined = true;
                AgoraClient.create().addAgoraEventHandler(mRtcEngineHandler);
                // 当 joinChannel api 中填入 0 时，agora 服务器会生成一个唯一的随机数，并在 onJoinChannelSuccess 回调中返回
                Log.d("mainfaceview", "other：" + mAgoraChannelId);

                AgoraClient.create().joinChannel(null, mAgoraChannelId, "", Integer.parseInt(UserUtils.getUser().getUid()));
                int role = mRoomModel.isOnMp() ? Constants.CLIENT_ROLE_BROADCASTER : Constants.CLIENT_ROLE_AUDIENCE;
                AgoraClient.create().setClientRole(role);
                AgoraClient.create().enableBeautyEffect(true);
            }
        }

    }

    public void switchRoom() {
        AgoraClient.create().rtcEngine().switchChannel(null, mAgoraChannelId);

    }

    @Override
    public String getAgoraChnnelId() {
        return mAgoraChannelId;
    }

    @Override
    public void release() {
        mHandler.removeCallbacksAndMessages(null);

        releaseHyphenateAndAgora();

        //socket
        mSocketStatus = LEVEL_ROOM;
        mJoinListener = null;
        mView = null;
        isRetry = false;
        mAgoraChannelId = "";
        WsocketManager.create().removeWsocketListener(mWsocketListener);
        WsocketManager.create().stopConnect();
        mSocketStatus = DISCONNECT;

        mRoomId = null;
        mRoomModel = null;
        if (mSortHandler != null) {
            mSortHandler.clearOrders();
            mSortHandler = null;
        }
        if (mSocketReconnect != null) {
            mSocketReconnect.stopConnect();
            mSocketReconnect = null;
        }
        if (mMsgHandler != null) {
            mMsgHandler.setOnMsgEventListener(null);
            mMsgHandler = null;
        }
        taskItemMsgList = null;
        mInstance = null;
    }

    @Override
    public void destroy() {
        mView = null;
        mJoinListener = null;
    }

    public void releaseHyphenateAndAgora() {
        //Hyphenate
        if (!TextUtils.isEmpty(mChatRoomId)) {
            HyphenateClient.getInstance().removeMessageListener(mEMListener);
            HyphenateClient.getInstance().leaveChatRoom(mChatRoomId);
            mChatRoomId = null;
        }
        //Agora
        if (!TextUtils.isEmpty(mAgoraChannelId)) {
            AgoraClient.create().removeAgoraEventHandler(mRtcEngineHandler);
            AgoraClient.create().leaveChannel();
            mAgoraChannelId = null;
            mAgoraChannelJoined = false;
        }
    }

    @Override
    public void startConnect(String url, String roomId) {
        this.mRoomId = roomId;
        if (mSocketStatus == CONNECTED) {
            WsocketManager.create().stopConnect();
        }
        WsocketManager.create().addWsocketListener(mWsocketListener);
        if (!TextUtils.isEmpty(url) && url.contains(":")) {
            WsocketManager wsocketManager = WsocketManager.create();
            wsocketManager.setWsUrl(url);
            wsocketManager.startConnect();
        } else {
            onJoinError();
        }
    }

    @Override
    public void setOnJoinRoomListener(JoinRoomListener listener) {
        this.mJoinListener = listener;
    }

    @Override
    public void setRoomView(IRoomView view) {
        this.mView = view;
    }

    @Override
    public <T extends BaseMsg> boolean sendRoomMsg(T obj) {
        return sendRoomMsg(JsonConverter.toJson(obj));
    }

    @Override
    public boolean sendRoomMsg(String text) {

        if (!HttpUtils.IsNetWorkEnable(AppUtils.getApp())) {
            return false;
        }
        if (inRoom()) {
            return WsocketManager.create().sendMessage(text);
        }
        if (mView != null) {
            mView.showAlert(1, "与聊天室断开连接");
        }

        return false;
    }

    @Override
    public boolean sendNormalMsg(String text) {
        return WsocketManager.create().sendMessage(text);
    }

    @Override
    public void updateSelfNick(String nickname) {
        if (mRoomModel != null && mRoomModel.getUser() != null) {
            mRoomModel.getUser().setName(nickname);
        }
    }

    private EMMessageListener mEMListener = new IEMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> list) {
            for (EMMessage message : list) {

                long sysTime = System.currentTimeMillis();
                long msgTime = message.getMsgTime();
                boolean isShowTime = ((sysTime - msgTime) / 1000) < 60 * 10;
                if (message.getChatType() == EMMessage.ChatType.ChatRoom && isShowTime) {
                    EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
                    ReceiveMessage msgBean = null;
                    try {
                        msgBean = new ReceiveMessage();
                        msgBean.setUser(JsonConverter.fromJson(message.getStringAttribute("XYUser"), JoinUser.class));
                        msgBean.setMsg(txtBody.getMessage());
                        String type = message.getStringAttribute("XYType");
                        if (TextUtils.equals(type, "EMOJI")) {
                            EmojiBean.EmoticonBean emojiBean = JsonConverter.fromJson(message.getStringAttribute("XYEmoji"), EmojiBean.EmoticonBean.class);
                            msgBean.setEmojiurl(emojiBean.getFace_image());
                            msgBean.setEmojianim(emojiBean.getAnimation());
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }

                    onHyphenateMessage(msgBean);

                }
            }
            onNewMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> list) {
            onNewMessage();
        }

        @Override
        public void onMessageRecalled(List<EMMessage> list) {
            onNewMessage();
        }

        private void onNewMessage() {
            mHandler.post(() -> {
                if (mView != null) {
                    mView.onNewMessage(getHyphenateUnreadCount());
                }
            });
        }
    };

    private void onHyphenateMessage(ReceiveMessage msgBean) {
        if (msgBean.getUser() == null)
            return;

        mHandler.post(() -> {
            RoomMsgBean receiveMsg = mMsgHandler.getReceiveMsg(msgBean);
            addRoomMsg(Collections.singletonList(receiveMsg));
            if (mView != null) {
                mView.onHyphenateMessage(msgBean);
            }
        });
    }

    private final IAgoraEventHandler mRtcEngineHandler = new IAgoraEventHandler() {


        @Override
        public void onUserMuteAudio(final int uid, final boolean muted) {
//            Log.d(TAG, "onUserMuteAudio: uid = " + uid + " muted = " + muted);
        }

        @Override
        public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
            if (uid == 0) {
                if (txQuality > 4 || rxQuality > 4) {
                    showToast("当前网络条件不佳");
                }
            }
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
//            Log.d(TAG, "onUserJoined: " + uid);
        }

        @Override
        public void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed) {
//            Log.d(TAG, "onRemoteVideoStateChanged: " + uid + " state = " + state);
        }

        @Override
        public void onFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {
//            Log.d(TAG, "onFirstRemoteVideoFrame: uid = " + uid + " width = " + width + " height = " + height + " elapsed = " + elapsed);
        }

        @Override
        public void onClientRoleChanged(int oldRole, int newRole) {
//            Log.d(TAG, "onClientRoleChanged: oldRole = " + oldRole + "  newRole = " + newRole);

            if (newRole == Constants.CLIENT_ROLE_BROADCASTER) {
//                AgoraClient.create().muteLocalAudioStream(true);
                mRoomModel.setMute(true);
            }
//            mHandler.post(() -> {
//                if (mView != null) {
//                    mView.onClientRoleChanged(oldRole, newRole);
//                }
//            });
        }

        @Override
        public void onConnectionStateChanged(int state, int reason) {
            Log.d("StateChanged", state + ";" + reason);
            if (state == CONNECTION_STATE_FAILED) {
                AgoraClient.create().leaveChannel();
                mAgoraChannelJoined = false;
                initAgoraEngineAndJoinChannel();
            }

        }

        @Override
        public void onLocalVideoStateChanged(int localVideoState, int error) {
            super.onLocalVideoStateChanged(localVideoState, error);
            Log.d("VStateChanged", localVideoState + ";" + error);

        }

        @Override
        public void onJoinChannelSuccess(final String channel, final int uid, int elapsed) {
//            Log.d(TAG, "onJoinChannelSuccess: " + channel);
        }

        @Override
        public void onAudioVolumeIndication(final IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {
            List<String> uids = new ArrayList<>();
            for (IRtcEngineEventHandler.AudioVolumeInfo info : speakers) {
                //The user ID of the speaker. The uid of the local user is 0.
//                Log.d(TAG, "onAudioVolumeIndication: " + info.uid + "  " + info.volume);

                if (info.volume == 0) {
                    continue;
                }

                if (info.uid == 0) {
                    uids.add(UserUtils.getUser().getUid());
                } else {
                    uids.add(String.valueOf(info.uid));
                }
            }

            mHandler.post(() -> {
                if (mView != null) {
                    mView.onAudioVolumeIndication(uids);
                }
            });
        }
    };
    private int flag = 0;

    public int getFlag() {
        return flag;
    }

    private long lastClickTime = 0;
    private WsocketListener mWsocketListener = new WsocketListener() {

        @Override
        public void onOpen(Response response) {
//            Log.d(TAG, "onOpen: ");
            flag = 0;

            mSocketStatus = CONNECTED;
//            long now = System.currentTimeMillis();
//            if (now - lastClickTime > 1000) {
//                lastClickTime = now;
            requestConnect();
            if (mView != null) {
                mView.onOpen();
            }
//            }


        }

        public void onMessage(String text) {
            flag = 0;
//            Log.d(TAG, "onMessage: " + text);
            if (mView != null) {
                mView.onMessage(text);
            }
            if (mRoomModel != null) {
                dispatchMessage(text);
            }
        }

        @Override
        public void onClosed(int code, String reason) {
//            Log.d(TAG, "onClosed: " + reason);
            if (mView != null) {
                mView.onMicroDownExitRoom();
            }
        }

        public void onFailure(Throwable t, Response response) {
//            Log.d(TAG, "onFailure: " + t.getMessage());
            onJoinError();
            reconnect(t.getMessage());
        }


        public void reconnect(String msg) {

            mAgoraChannelJoined = false;
            mSocketStatus = DISCONNECT;
            AgoraClient.create().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
            releaseHyphenateAndAgora();
            WsocketManager.create().removeWsocketListener(mWsocketListener);
            mSocketReconnect.reconnect(mRoomId);
            mSortHandler.clearOrders();
        }
    };

    public void requestConnect() {
        mSocketStatus = JOIN_ROOM;
        SendTokenMsg msg = new SendTokenMsg();
        msg.setMsgId(SocketCons.SEND_TOKEN);
        msg.setUserToken(UserUtils.getUser().getToken());
        sendNormalMsg(JsonConverter.toJson(msg));
    }

    private void onJoinError() {
        onJoinError(JoinError.DEFAULT_CODE, HttpConst.SERVER_ERROR);
    }

    private void onJoinError(String msg) {
        onJoinError(JoinError.DEFAULT_CODE, msg);
    }

    public void onJoinError(int code, String msg) {
        if (mJoinListener != null) {
            mJoinListener.onError(code, msg);
            mRoomId = null;
            WsocketManager.create().removeWsocketListener(mWsocketListener);
            if (mSocketStatus == CONNECTED) {
                WsocketManager.create().stopConnect();
                releaseHyphenateAndAgora();
            }
        } else {
            try {
                mSocketReconnect.reconnect(mRoomId);

            } catch (Exception e) {
                mJoinListener.onError(code, msg);

            }

        }
    }


    private void showToast(String msg) {
        ToastUtils.showToast(msg);
    }

    public RoomData.MicroInfosBean getMicroByType(int type, int number) {
        if (type == 0) {
            return mRoomModel.getHostMicro();
        } else if (type == 4) {
            return mRoomModel.getmSofaMicro();
        }
        List<RoomData.MicroInfosBean> list = mRoomModel.getMicros();
        if (!CollectionUtils.isEmpty(list)) {
            for (RoomData.MicroInfosBean bean : list) {
                if (bean.getType() == type && bean.getNumber() == number) {
                    return bean;
                }
            }
        }
        return null;
    }


    private void dispatchMessage(String message) {
        Log.d("AG_EX_AV", message);
        BaseReMsg msg = JsonConverter.fromJson(message, BaseReMsg.class);
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(message).getAsJsonObject();
        switch (msg.getMsgId()) {
            //发送token回调
            case SocketCons.SEND_TOKEN: {
                switch (msg.getSuccess()) {
                    case SocketCons.SUCCESS:
                        isRetry = false;
                        //发送进入房间消息
                        JoinRoom joinRoom = AppConstant.getInstance().getJoinRoom();
                        if (joinRoom == null) {
                            joinRoom = new JoinRoom();
                        }
                        Log.d("AG_EX_AV", "发送进入房间消息" + JsonConverter.toJson(joinRoom));

                        joinRoom.setMsgId(SocketCons.JOIN_ROOM);
                        joinRoom.setRoomId(mRoomId);
                        sendNormalMsg(JsonConverter.toJson(joinRoom));
                        break;
                    //该用户不存在
                    case SocketCons.Error.USER_NO_EXIST:
                        onJoinError("服务器连接失败");
                        break;
                    case SocketCons.Error.RE_CONNECT:
                        //发送78消息--重连请求
                        isRetry = true;
                        if (mView != null) {
                            mView.sendReConnetMsg();
                        } else {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("MsgId", 78);
                            sendRoomMsg(jsonObject.toString());
                        }
                        break;
                    default:
                        onJoinError();
                        break;
                }
                break;
            }
            //进入房间回调
            case SocketCons.JOIN_ROOM: {
                switch (msg.getSuccess()) {
                    case SocketCons.SUCCESS:
                        mSocketStatus = IN_ROOM;
                        JoinRoomMsg joinRoomMsg = JsonConverter.fromJson(message, JoinRoomMsg.class);
                        String chatRoomId = joinRoomMsg.getHuanxinRoomId();
                        String agoraRoomId = joinRoomMsg.getShengwangRoomId();
                        if (TextUtils.isEmpty(agoraRoomId)) {
                            agoraRoomId = mRoomId;
                        }

                        initHyphenateChatRoom(chatRoomId);
                        this.mAgoraChannelId = agoraRoomId;
                        initAgoraEngineAndJoinChannel();
                        break;
                    //房间被锁
                    case SocketCons.Error.ROOM_LOCK:
                        onJoinError("房间被锁");
                        break;
                    //被踢出房间
                    case SocketCons.Error.ROOM_KICK:
                        onJoinError("您在两分钟后再进入房间");
                        break;
                    //家长模式、青少年模式
                    case SocketCons.Error.PARENT_LOCK:
                        onJoinError(JoinError.PARENT_LOCK, "");
                        break;
                    //排队
                    case SocketCons.Error.SERVER_BUSY:
                        onJoinError(JoinError.SERVER_BUSY, "服务器繁忙");
                        break;
                    //红娘不在麦上
                    case 7:
                        onJoinError("房间已关闭");
                        if (mView != null) {
                            mView.onMatcherExited(0, "房间已关闭");
                        }
                        break;
                    case 8:
                        showToast("嘉宾位已经有嘉宾了");
                        break;
                    case 9:
                        showToast("玫瑰余额不足，你不能进入专属视频房。");
                        break;
                    case 10:
                        JoinRoomMsg joinBean = JsonConverter.fromJson(message, JoinRoomMsg.class);
                        onJoinError(joinBean.getCode());
                        break;
                    case 11:
                        joinBean = JsonConverter.fromJson(message, JoinRoomMsg.class);
                        String time = joinBean.getBanTime() != -1 ? TimeUtils.secondToTime(joinBean.getBanTime()) : "永久禁播";
                        showEnsureAlert("主播处于封禁状态，暂时不能开播\n禁播原因：" + joinBean.getCode() + "\n" + "禁播结束倒计时：" + time);
                        break;
                    case SocketCons.Error.Maintenance:
                        onJoinError("服务器维护中。。。");
                        break;
                    case -127:
                        //进入房间未知错误
                        //发送进入房间消息
                        JoinRoom joinRoom = new JoinRoom();
                        joinRoom.setMsgId(SocketCons.JOIN_ROOM);
                        joinRoom.setRoomId(mRoomId);
                        sendNormalMsg(JsonConverter.toJson(joinRoom));
                        break;
                    default:
                        onJoinError();
                        break;
                }
                break;
            }
            //退出房间
            case SocketCons.EXIT_ROOM:
                if (msg.getSuccess() == 1) {
                    //退房间
                    if (mView != null) {
                        mView.exitRoom();
                    }
                }
                break;
            //进入房间
            case SocketCons.USER_JOIN_ROOM: {
                JoinUserList userList = JsonConverter.fromJson(message, JoinUserList.class);
                for (JoinUser user : userList.getArgs()) {
//                 JoinUser user = JsonConverter.fromJson(message, JoinUser.class);
                    boolean addJoinMsg = true;
                    if (TextUtils.equals(user.getUserId(), UserUtils.getUser().getUid())) {
                        addJoinMsg = mRoomModel.getUser() == null;
                        mRoomModel.setUser(user);
                    }

                    if (addJoinMsg) {
                        RoomMsgBean msgBean = mMsgHandler.getJoinRoomMsg(user);
                        addRoomMsg(Collections.singletonList(msgBean));
                    }
                    syncGuardPersonJoin(user);

                    if (object.has("VisitorNum")) {
                        int visitorNum = object.get("VisitorNum").getAsInt();
                        if (mRoomModel.getRoomData() != null) {
                            mRoomModel.getRoomData().setVisitorNum(visitorNum);
                        }
                        if (mView != null) {
                            mView.onVisitorNum(FormatUtils.formatTenThousand(visitorNum));
                        }
                    }
                }
                break;
            }
            //房间信息
            case SocketCons.ROOM_INFO: {

                RoomData roomData = JsonConverter.fromJson(message, RoomData.class);
                mRoomModel.setRoomId(roomData.getRoomData().getId());
                mMsgHandler.setRoomModel(mRoomModel);
                List<MicroOrder> orders = new ArrayList<>();
                if (roomData.getLeftMicroOrders() != null) {
                    orders.addAll(roomData.getLeftMicroOrders());
                }
                if (roomData.getRightMicroOrders() != null) {
                    orders.addAll(roomData.getRightMicroOrders());
                }
                mSortHandler.setNewMicroSorts(orders);
                roomData.setMicroOrders(orders);
                JoinRoom joinRoom = new JoinRoom();
                joinRoom.setExclusiveRoom(roomData.getRoomData().isExclusiveRoom());
                joinRoom.setOpenVideoFrame(roomData.getRoomData().isOpenVideoFrame());
                joinRoom.setOpenPickSong(roomData.getRoomData().isOpenPickSong());
                joinRoom.setOpenMediaLibrary(roomData.getRoomData().isOpenMediaLibrary());
                joinRoom.setOpenBreakEgg(roomData.getRoomData().isOpenBreakEgg());
                joinRoom.setCloseCamera(roomData.getRoomData().isCloseCamera());
                joinRoom.setOpenRedPacket(roomData.getRoomData().isOpenRedPacket());
                joinRoom.setRoomId(roomData.getRoomData().getRoomId());
                joinRoom.setRoomMode(roomData.getRoomData().getRoomType());
                joinRoom.setRoomName(roomData.getRoomData().getRoomName());
                AppConstant.getInstance().setJoinRoom(joinRoom);
                mRoomModel.setRoomData(roomData);

                //麦序处理
                boolean isOnMp = false;
                List<RoomData.MicroInfosBean> list = mRoomModel.getMicros();
                if (list == null) {
                    list = new ArrayList<>();
                } else {
                    list.clear();
                }
                List<RoomData.MicroInfosBean> infos = roomData.getMicroInfos();
                for (RoomData.MicroInfosBean bean : infos) {
                    switch (bean.getType()) {
                        //红娘麦
                        case 0:
                            mRoomModel.setHostMicro(bean);
                            break;
                        case 4:
                            mRoomModel.setmSofaMicro(bean);
                            break;
                        default:
                            list.add(bean);
                            break;
                    }

                    if (!isOnMp) {
                        isOnMp = bean.getUser() != null && TextUtils.equals(bean.getUser().getUserId(), UserUtils.getUser().getUid());
                    }
                }
                mRoomModel.setMicros(list);

                mRoomModel.setOnMp(isOnMp);

                //房间榜单
                List<String> ranks = roomData.getRanks();
                if (ranks == null) {
                    ranks = new ArrayList<>();
                }
                int size = ranks.size();
                if (ranks.size() < 3) {
                    for (int i = 0; i < 3 - size; i++) {
                        ranks.add("");
                    }
                }
                roomData.setRanks(ranks);
                if (mView != null) {
                    mView.onRequestMicroGuestChanged(roomData.getWaitMicroLeftNumber(), roomData.getWaitMicroRightNumber());
                    mView.onOnlineGuestChanged(roomData.getOnlineLeftNumber(), roomData.getOnlineRightNumber());

                }
                if (mJoinListener != null) {
                    mJoinListener.onSuccess();
                    mJoinListener = null;
                }
                if (!TextUtils.isEmpty(roomData.getHuanxinRoomId())) {
                    initHyphenateChatRoom(roomData.getHuanxinRoomId());
                }
                if (!TextUtils.isEmpty(roomData.getShengwangRoomId())) {
                    this.mAgoraChannelId = roomData.getShengwangRoomId();
                    initAgoraEngineAndJoinChannel();
                }
                if (mView != null) {

                    mView.onReconnected();
                }
                mRoomModel.setRoomData(roomData);
                if (mView != null) {
                    mView.onMicroSort();
                }
//                if (mView != null) {
//                    mView.updateMicro();
//                }

                if (isRetry) {
//                    if (mView != null) {
//                        mView.showAlert("已重新连接聊天室");
//                    }
                    List<RoomData.MicroInfosBean> m = roomData.getMicroInfos();
                    int count = 0;
                    for (int i = 0; i < m.size(); i++) {
                        if (m.get(i).getUser() != null) {
                            count++;
                        }
                    }
                    if (m != null && count >= 2) {
                        if (mView != null) {
                            mView.upDataMicInfo(m);
                        }
                    }
                    //重连时候 避免后台把本人删除麦序  自己还能说话
                    if (mView != null) {
                        mView.leaveSWChannel();
                    }
                }
                break;
            }
            //移出房间回调
            case SocketCons.KICK_ROOM: {
                if (msg.getSuccess() == SocketCons.SUCCESS) {
                    showToast("移除成功");
                } else if (msg.getSuccess() == -49) {
                    showToast("该用户已经不在房间内");
                }
                break;
            }
            //上麦回调
            case SocketCons.UP_MICRO: {
                int success = msg.getSuccess();
                if (success == SocketCons.SUCCESS) {
                    MobEventUtils.onUpMicro(AppUtils.getApp());
                    if (mView != null) {
                        mView.onInmicroSuccessCallback();
                    }
                }
                //踢下麦
                else if (success == SocketCons.Error.KICK_MICRO) {
                    showToast("请3分钟后再上麦");
                }
                //无空麦位
                else if (success == 7) {
                    showToast("麦上无空位，请稍后再试");
                }
                //排序
                else if (success == 20 || success == 12) {
                    showEnsureAlert("您已进入申请队列，请等待主持\n邀请上麦");
                    RoomController.getInstance().sendMsg("申请上麦");
                }
                //实名认证
                else if (success == 8) {
//                    showEnsureAlert("请先去《我的》->《实名认证》进行实名");
                    if (mView != null) {
                        mView.gotoCertification();
                    }
                }
                //余额不足
                else if (success == 21) {
                    if (mView != null) {
                        mView.onBalanceNoEnough();
                    }
                }
                //未身份验证
                else if (success == 10) {

                    showToast("上麦失败");
                }
                break;
            }
            //上麦消息群发
            case SocketCons.USER_UP_MICRO: {
                UpMicroMsgList microMsgList = JsonConverter.fromJson(message, UpMicroMsgList.class);
                for (UpMicroMsg microMsg : microMsgList.getArgs()) {
//                    UpMicroMsg microMsg = JsonConverter.fromJson(message, UpMicroMsg.class);
                    RoomData.MicroInfosBean microInfo = microMsg.getMicroInfo();
                    if (TextUtils.equals(microInfo.getUser().getUserId(), UserUtils.getUser().getUid())) {
                        AgoraClient.create().setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
                        mRoomModel.setOnMp(true);
                        if (mView != null) {
                            mView.onClientRoleChanged(2, 1);
                        }
                    }
                    if (microInfo.getType() == 0) {
                        mRoomModel.setHostMicro(microInfo);
                    } else if (microInfo.getType() == 4) {
                        mRoomModel.setmSofaMicro(microInfo);
                    } else {
                        List<RoomData.MicroInfosBean> list = mRoomModel.getMicros();
                        if (!CollectionUtils.isEmpty(list)) {
                            for (int i = 0; i < list.size(); i++) {
                                RoomData.MicroInfosBean original = list.get(i);
                                if (original.getType() == microInfo.getType() && original.getNumber() == microInfo.getNumber()) {
                                    list.set(i, microInfo);
                                    break;
                                }
                            }
                        }
                    }
                    RoomMsgBean msgBean = mMsgHandler.getUpMicroMsg(microMsg);
                    addRoomMsg(Collections.singletonList(msgBean));
                    if (mView != null) {
                        mView.onUpMicro(microMsg);
                    }
                }
                break;
            }
            //下麦回调
            case SocketCons.DOWN_MICRO: {
                if (msg.getSuccess() == SocketCons.SUCCESS) {

                } else if (msg.getSuccess() == 6) {
                    if (mView != null) {
                        mView.onDownMicroSelf();
                    }
                } else {
                    showToast("下麦失败");

                }
                break;
            }
            //下麦消息群发
            case SocketCons.USER_DOWN_MICRO: {
                DownMicroMsgList microMsgList = JsonConverter.fromJson(message, DownMicroMsgList.class);
                for (DownMicroMsg microMsg : microMsgList.getArgs()) {
                    InMicroMemberUtils.getInstance().removeMicroMember(String.valueOf(microMsg.getLevel()));
                    boolean isMe = false;
                    if (microMsg.getLevel() == 0) {
                        if (mRoomModel.getHostMicro() == null || mRoomModel.getHostMicro().getUser() == null) {
                            return;
                        }
                        WsUser hostUser = mRoomModel.getHostMicro().getUser();
                        isMe = hostUser != null && TextUtils.equals(hostUser.getUserId(), UserUtils.getUser().getUid());
                        mRoomModel.getHostMicro().setUser(null);
//                    if (mView != null) {
//                        mView.onMatcherExited();
//                    }
                        break;
                    } else if (microMsg.getLevel() == 4) {
                        if (mRoomModel.getmSofaMicro() == null || mRoomModel.getmSofaMicro().getUser() == null) {
                            return;
                        }
                        WsUser sofaUser = mRoomModel.getmSofaMicro().getUser();
                        isMe = sofaUser != null && TextUtils.equals(sofaUser.getUserId(), UserUtils.getUser().getUid());
                        mRoomModel.getmSofaMicro().setUser(null);
                    } else {
                        List<RoomData.MicroInfosBean> list = mRoomModel.getMicros();
                        if (!CollectionUtils.isEmpty(list)) {
                            for (RoomData.MicroInfosBean bean : list) {
                                if (microMsg.getLevel() == bean.getType() && microMsg.getNumber() == bean.getNumber()) {
                                    isMe = bean.getUser() != null && TextUtils.equals(bean.getUser().getUserId(), UserUtils.getUser().getUid());
                                    bean.setUser(null);
                                    break;
                                }
                            }
                        }
                    }
                    if (isMe) {  //自己下麦、被踢下麦
                        AgoraClient.create().setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
                        mRoomModel.setOnMp(false);
                        if (mView != null) {
                            mView.onClientRoleChanged(1, 2);
                        }
                        if (microMsg.isKick()) {
                            if (TextUtils.equals(UserUtils.getUser().getSex(), String.valueOf(microMsg.getLevel()))) {
                                mView.removeMicro();
                            }
                        }
                    }
                    if (mView != null) {
                        mView.onDownMicro(microMsg);
                    }
                }
                break;
            }
            //禁麦消息
            case 12: {

                BaseMicroMsgList microMsgList = JsonConverter.fromJson(message, BaseMicroMsgList.class);
                for (BaseMicroMsg microMsg : microMsgList.getArgs()) {
                    RoomData.MicroInfosBean micro = getMicroByType(microMsg.getLevel(), microMsg.getNumber());
                    micro.setIsDisabled(true);
                    boolean isMe = micro.getUser() != null && TextUtils.equals(micro.getUser().getUserId(), UserUtils.getUser().getUid());
                    if (isMe) {
                        AgoraClient.create().muteLocalAudioStream(true);
                    }

                    if (mView != null) {
                        mView.onForbiddenMicro(microMsg, true);
                    }
                }
                break;
            }
            //取消禁麦消息
            case 13: {
                BaseMicroMsgList microMsgList = JsonConverter.fromJson(message, BaseMicroMsgList.class);
                for (BaseMicroMsg microMsg : microMsgList.getArgs()) {
                    RoomData.MicroInfosBean micro = getMicroByType(microMsg.getLevel(), microMsg.getNumber());
                    micro.setIsDisabled(false);
                    boolean isMe = micro.getUser() != null && TextUtils.equals(micro.getUser().getUserId(), UserUtils.getUser().getUid());
                    if (isMe && !mRoomModel.isMute()) {
                        AgoraClient.create().muteLocalAudioStream(false);
                    }

                    if (mView != null) {
                        mView.onForbiddenMicro(microMsg, false);
                    }
                }
                break;
            }
            //锁麦返回值
            case 14: {
                //麦位有人，不能锁麦
                if (msg.getSuccess() == 90) {
                    showToast("麦位有人，不能锁麦");
                }
                break;
            }
            //锁麦群发
            case 16: {
                BaseMicroMsgList microMsgList = JsonConverter.fromJson(message, BaseMicroMsgList.class);
                for (BaseMicroMsg microMsg : microMsgList.getArgs()) {
                    RoomData.MicroInfosBean micro = getMicroByType(microMsg.getLevel(), microMsg.getNumber());
                    micro.setIsLocked(true);
                    if (mView != null) {
                        mView.onLockMicro(microMsg, true);
                    }
                }
                break;
            }
            //取消锁麦
            case 17: {
                BaseMicroMsg microMsg = JsonConverter.fromJson(message, BaseMicroMsg.class);
                RoomData.MicroInfosBean micro = getMicroByType(microMsg.getLevel(), microMsg.getNumber());
                micro.setIsLocked(false);
                if (mView != null) {
                    mView.onLockMicro(microMsg, false);
                }
                break;
            }
            //用户申请非自由麦
            case 20: {
                MicroSort sort = JsonConverter.fromJson(message, MicroSort.class);
                mSortHandler.addMicroOrder(sort.getMicroOrderData());
                if (mView != null) {
                    mView.receiveMicroRequest(sort);
                }
                if (sort.getMicroOrderData() != null && sort.getMicroOrderData().getUser() != null) {
                    String uid = sort.getMicroOrderData().getUser().getUserId();
                    boolean isFobiddenLb = ForbiddenStateUtils.getForbiddenLBstate(uid);
                    boolean isFobiddenMic = ForbiddenStateUtils.getForbiddenMicState(uid);
                    if (isFobiddenLb) {
                        ForbiddenStateUtils.saveForbiddenLBState(uid, false);
                        AgoraClient.create().muteRemoteAudioStream(Integer.valueOf(uid), false);
                    }
                    if (isFobiddenMic) {
                        ForbiddenStateUtils.saveForbiddenMicState(uid, false);
                        AgoraClient.create().muteLocalAudioStream(false);
                    }
                }

                break;
            }
            //删除非自由上麦列表
            case 21: {
                String uid = object.get("Id").getAsString();
                mSortHandler.removeMicroSort(uid);
                InMicroMemberUtils.getInstance().removeMicroOrder(uid);
                if (mView != null) {
                    mView.onMicroSort();
                    mView.leaveRoomUserToOther(uid);
                }
                break;
            }
            //同步自由麦
            case 25:
                if (object.has("IsFreeMicro")) {
                    boolean isFreeMicro = object.get("IsFreeMicro").getAsBoolean();
                    if (mRoomModel.getRoomData() != null) {
//                        mRoomModel.getRoomData().getRoomData().setIsFreeMicro(isFreeMicro);
                    }
                    if (!isFreeMicro) {
                        mSortHandler.clearOrders();
                    }

                    if (mView != null) {
                        mView.onMicroTypeChanged(isFreeMicro);
                        mView.onMicroSort();
                    }
                }
                break;
            //同步心动值
            case 27: {
                if (object.has("IsHeadBeat")) {
                    boolean isHeadBeat = object.get("IsHeadBeat").getAsBoolean();
                    if (mRoomModel.getRoomData() != null) {
//                        mRoomModel.getRoomData().getRoomData().setIsOpenHeartValue(isHeadBeat);
                    }

                    if (mView != null) {
                        mView.onHeartValueOpened(isHeadBeat);
                    }
                }
                break;
            }
            case 28:
                if (msg.getSuccess() == 1) {
                    ToastUtils.showToast("操作成功");
                } else {
                    ToastUtils.showToast("禁言失败");
                }

                break;
            //赠送礼物回调
            case 29: {
                if (msg.getSuccess() == 1) {
                    MobEventUtils.onSendGift(AppUtils.getApp());
                }
                if (mView != null) {
                    mView.onMultiSend(msg.getSuccess());
                }
                break;
            }
            //打赏用户回调
            case 58: {
                if (msg.getSuccess() == 1) {
                    MobEventUtils.onSendGift(AppUtils.getApp());
                }
                if (mView != null) {
                    mView.onSingleSend(msg.getSuccess());
                }
                break;
            }
            //广播赠送礼物消息
            case 30: {

                ReceivePresentList receivePresentList = JsonConverter.fromJson(message, ReceivePresentList.class);
                for (ReceivePresent bean : receivePresentList.getArgs()) {
                    addRoomMsg(mMsgHandler.getBroadGiftMsg(bean));
                    if (mView != null) {
                        mView.onReceiveGift(bean);
                    }
                }
            }
            //锁定/解锁房间
            case 33: {
                if (msg.getSuccess() == 1) {
                    if (mRoomModel.getRoomData() != null) {
                        boolean roomLock = mRoomModel.getRoomData().getRoomData().isRoomLock();
                        mRoomModel.getRoomData().getRoomData().setRoomLock(!roomLock);
                    }
                }
                break;
            }
            //心动值变化
            case 37: {
                int heartValue = object.get("HeartValue").getAsInt();
                BaseMicroMsg microMsg = JsonConverter.fromJson(message, BaseMicroMsg.class);
                RoomData.MicroInfosBean micro = getMicroByType(microMsg.getLevel(), microMsg.getNumber());
                micro.setXinDongZhi(heartValue);

                if (mView != null) {
                    mView.onHeartValue(microMsg, heartValue);
                }
                break;
            }
            //倒计时
            case 39: {
                CountDown bean = JsonConverter.fromJson(message, CountDown.class);
                RoomData.MicroInfosBean micro = getMicroByType(bean.getLevel(), bean.getNumber());
                micro.setDaojishiShijiandian(bean.getSpeechTime());
                micro.setDaojishiShichang(bean.getDuration());
                if (mView != null) {
                    mView.onCountDown(bean);
                }
                break;
            }
            //修改标签
            case 43: {
                if (msg.getSuccess() == 1) {
//                    mRoomData.setTagName(object.get("TagName").getAsString());
//                    mBinding.tagTv.setText(mRoomData.getTagName());
                }
                break;
            }
            //被踢出房间
            case 45: {
                KickRoomBean bean = JsonConverter.fromJson(message, KickRoomBean.class);
                if (TextUtils.equals(UserUtils.getUser().getUid(), bean.getUserId())) {
                    if (mView != null) {
                        mView.onKickRoom(bean.getCode());
                    }
                    if (RoomMiniController.getInstance().isMini()) {
                        RoomMiniController.getInstance().destroy();
                    }
                } else {
                    RoomMsgBean msgBean = mMsgHandler.getKickRoomMsg(bean.getNickName(), bean.getUserId());
                    addRoomMsg(Collections.singletonList(msgBean));
                }
                break;
            }
            //用户信息
            case 46: {
                if (mView != null) {
                    mView.onUserInfo(JsonConverter.fromJson(message, MicroUser.class));
                }
                break;
            }
            //被禁言/取消禁言
            case 47: {
                ForbiddenMsg bean = JsonConverter.fromJson(message, ForbiddenMsg.class);
                if (mRoomModel.getRoomData() != null) {
                    mRoomModel.getRoomData().setDisableMsg(bean.isIsDisableMsg());
                }
                break;
            }
            case 48:
                boolean isLock = object.get("IsLock").getAsBoolean();
                if (isLock) {
                    mView.lockRoomExitUnInMic();
                }

                break;
            //房间热度值变化
            case 54: {
                int heartValue = object.get("HeatValue").getAsInt();
                if (mRoomModel.getRoomData() != null) {
                    mRoomModel.getRoomData().setVisitorNum(heartValue);
                }
                if (mView != null) {
                    mView.onVisitorNum(FormatUtils.formatTenThousand(heartValue));
                }
                break;
            }
            //通知管理员身份变化
            case 55:
                RoomManager manager = JsonConverter.fromJson(message, RoomManager.class);
                if (TextUtils.equals(UserUtils.getUser().getUid(), manager.getUserId())) {
                    if (mRoomModel.getRoomData() != null) {
                        //是否是房主
                        if (mRoomModel.getRoomData().getUserIdentity() != 2) {
                            mRoomModel.getRoomData().setUserIdentity(manager.isIsManager() ? 1 : 0);
                        }
                    }
                }
                if (manager.isIsManager()) {
                    RoomMsgBean msgBean = mMsgHandler.getRoomManagerMsg(manager);
                    addRoomMsg(Collections.singletonList(msgBean));
                }
                break;
            //修改房间名称
            case 50: {
                String name = object.get("Name").getAsString();
                if (mRoomModel.getRoomData() != null) {
                    mRoomModel.getRoomData().getRoomData().setRoomName(name);
                }
                if (mView != null) {
                    mView.roomNameChanged(name);
                }
                break;
            }
            //玩法介绍
            case 52: {
                String desc = object.get("Desc").getAsString();
                if (mRoomModel.getRoomData() != null) {
                    mRoomModel.getRoomData().getRoomData().setRoomDesc(desc);
                }
                break;
            }
            //欢迎语
            case 35: {
                String welcomMsg = object.get("WelcomMsg").getAsString();
                if (mRoomModel.getRoomData() != null) {
                    mRoomModel.getRoomData().getRoomData().setRoomWelcomes(welcomMsg);
                }
                break;
            }
            //在线用户
            case 57: {
                if (mView != null) {
                    mView.onlineUser(JsonConverter.fromJson(message, OnlineUser.class));
                }
                break;
            }

            //发送表情回调
            case 61: {
                if (mView != null) {
                    mView.onSendEmoJi();
                }
                break;
            }
            //广播表情
            case 62: {
                EmotionBean emotion = JsonConverter.fromJson(message, EmotionBean.class);
                if (mView != null) {
                    mView.onShowEmoJi(emotion);
                }
                break;
            }
            //广播表情结果
            case 63: {
                EmotionBean emotion = JsonConverter.fromJson(message, EmotionBean.class);
                if (mView != null) {
                    mView.onEmoJiResult(emotion, emotion.getResultUrl());
                }
                mHandler.postDelayed(() -> {
                    if (mView != null) {
                        mView.onEmoJiResult(emotion, null);
                    }
                }, 2000);
                break;
            }
            //置顶麦序
            case 64: {
                MicroTopArgList microTopArgList = JsonConverter.fromJson(message, MicroTopArgList.class);
                for (MicroTopArg microTopArg : microTopArgList.getArgs()) {
                    String uid = String.valueOf(microTopArg.getId());
                    mSortHandler.topMicroSort(uid);
                    if (mView != null) {
                        mView.onMicroSort();
                    }
                }
                break;
            }
            //砸蛋消息
            case 67: {
                SmashEggBeanList eggBeanList = JsonConverter.fromJson(message, SmashEggBeanList.class);
                for (SmashEggBean eggBean : eggBeanList.getArgs()) {
                    RoomMsgBean smashMsg = mMsgHandler.getSmashMsg(eggBean);
                    List<SmashBean> smashBeans = new ArrayList<>();
                    smashBeans.clear();
                    smashBeans.add(eggBean.getGifts());
                    smashMsg.setList(smashBeans);
                    addRoomMsg(Collections.singletonList(smashMsg));
                }
                break;
            }
            case 68:   //修改头像
            case 69: { //修改昵称
                AccountUpdateMsgList accountMsgList = JsonConverter.fromJson(message, AccountUpdateMsgList.class);
                for (AccountUpdateMsg accountMsg : accountMsgList.getArgs()) {
                    //房主
                    String ownerUserId = mRoomModel.getRoomData().getRoomData().getOwnerUserId();
                    if (TextUtils.equals(accountMsg.getAccountId(), ownerUserId)) {
                        if (!TextUtils.isEmpty(accountMsg.getHeadUrl())) {
                            mRoomModel.getRoomData().setRoomOwnerHeadUrl(accountMsg.getHeadUrl());
                            if (mView != null) {
                                mView.onOwnerUpdated();
                            }
                        } else {
                            mRoomModel.getRoomData().setRoomOwnerNickName(accountMsg.getNickName());
                        }
                    }
                    //主持麦
                    WsUser hostUser = mRoomModel.getHostMicro().getUser();
                    if (hostUser != null && TextUtils.equals(accountMsg.getAccountId(), hostUser.getUserId())) {
                        if (!TextUtils.isEmpty(accountMsg.getHeadUrl())) {
                            mRoomModel.getHostMicro().getUser().setHeadImageUrl(accountMsg.getHeadUrl());
                        } else {
                            mRoomModel.getHostMicro().getUser().setName(accountMsg.getNickName());
                        }
                        if (mView != null) {
                            UpMicroMsg microMsg = new UpMicroMsg();
                            microMsg.setMicroInfo(mRoomModel.getHostMicro());
                            mView.onUpMicro(microMsg);
                        }
                        break;
                    }
                    //其它麦位
                    List<RoomData.MicroInfosBean> micros = mRoomModel.getMicros();
                    if (!CollectionUtils.isEmpty(micros)) {
                        for (RoomData.MicroInfosBean bean : micros) {
                            if (bean.getUser() != null && TextUtils.equals(accountMsg.getAccountId(), bean.getUser().getUserId())) {
                                if (!TextUtils.isEmpty(accountMsg.getHeadUrl())) {
                                    bean.getUser().setHeadImageUrl(accountMsg.getHeadUrl());
                                } else {
                                    bean.getUser().setName(accountMsg.getNickName());
                                }
                                if (mView != null) {
                                    UpMicroMsg microMsg = new UpMicroMsg();
                                    microMsg.setMicroInfo(bean);
                                    mView.onUpMicro(microMsg);
                                }
                                break;
                            }
                        }
                    }
                }
                break;
            }
            case 70: { //邀请上麦回调
                if (msg.getSuccess() == 1) {
                    ToastUtils.showToast("您邀请用户成功，请耐心等待");
                } else if (msg.getSuccess() == 3) {
                    ToastUtils.showToast("您已邀请用户，请耐心等待");
                } else if (msg.getSuccess() == 4) {
                    ToastUtils.showToast("该用户已在麦序，请勿重复邀请。");
                } else if (msg.getSuccess() == 5) {
                    ToastUtils.showToast("该用户已不在房间");
                }
                break;
            }
            case 71: { //收到上麦邀请
                if (mView != null) {
                    mView.onMpInvited(JsonConverter.fromJson(message, MpInvited.class));
                }
                break;
            }
            case 72: { //同意
                if (msg.getSuccess() == 21) {
                    if (mView != null) {
                        mView.onBalanceNoEnough();
                    }
                } else if (msg.getSuccess() == 8) {
//                    showEnsureAlert("请先去《我的》->《实名认证》进行实名");
                    if (mView != null) {
                        mView.gotoCertification();
                    }
                } else if (msg.getSuccess() == 1) {
                    if (mView != null) {
                        mView.resetInRoomInviteParam();
                    }
                    RoomController.getInstance().sendMsg("申请上麦");
                    showEnsureAlert("您已进入申请队列，请等待主持\n邀请上麦");
                }
                break;
            }
            case 73: { //账户余额变化
                if (object.has("Coin")) {
                    String balance = object.get("Coin").getAsString();
                    if (mRoomModel.getRoomData() != null) {
                        mRoomModel.getRoomData().setBalance(balance);
                    }
                    if (mView != null) {
                        mView.onBalance(balance);
                    }
                }
                break;
            }
            case 74: { //上麦价格回调
                if (object.has("Cost")) {
                    String cost = object.get("Cost").getAsString();
                    if (mView != null) {
                        mView.onMicroCost(cost);
                    }
                }
                break;
            }
            case 75:
                GuestNumMsg data = JsonConverter.fromJson(message, GuestNumMsg.class);
                if (mView != null) {
                    mView.onRequestMicroGuestChanged(data.getLeftNumber(), data.getRightNumber());
                }
                break;
            case 76:
                data = JsonConverter.fromJson(message, GuestNumMsg.class);
                if (mView != null) {
                    mView.onOnlineGuestChanged(data.getLeftNumber(), data.getRightNumber());
                } else {
                    if (data != null && mRoomModel.getRoomData() != null) {
                        mRoomModel.getRoomData().setOnlineLeftNumber(data.getLeftNumber());
                        mRoomModel.getRoomData().setOnlineRightNumber(data.getRightNumber());
                    }
                }
                break;
            case 77:
                String userId = object.get("UserId").getAsString();
                String nickName = object.get("NickName").getAsString();
                sendTextMessage(userId, nickName);
                showToast(nickName + "已成为您的好友");
                if (mView != null) {
                    mView.notifyAddFriend(String.valueOf(userId));
                }
                break;
            case 78:
                if (msg.getSuccess() == 1) {
                    if (mView != null) {
//                        mView.onReconnected();1
                        mView.onReconnectSuccess();
                    }
                    RoomData.MicroInfosBean mainMicro = mRoomModel.getHostMicro();
                    if (mainMicro.getUser() != null) {
                        String uid = mainMicro.getUser().getUserId();
                        boolean isFobiddenMic = ForbiddenStateUtils.getForbiddenMicState(uid);
                        if (isFobiddenMic) {
                            AgoraClient.create().muteLocalAudioStream(isFobiddenMic);
                        }
                        boolean isFobiddenLb = ForbiddenStateUtils.getForbiddenLBstate(uid);
                        if (isFobiddenLb) {
                            AgoraClient.create().muteRemoteAudioStream(Integer.valueOf(uid), isFobiddenLb);
                        }
                    }
                    List<RoomData.MicroInfosBean> microSorts = mRoomModel.getMicros();
                    for (RoomData.MicroInfosBean bean : microSorts) {
                        if (bean.getUser() != null) {
                            String uid = bean.getUser().getUserId();
                            boolean isFobiddenMic = ForbiddenStateUtils.getForbiddenMicState(uid);
                            if (isFobiddenMic) {
                                AgoraClient.create().muteLocalAudioStream(isFobiddenMic);
                            }
                            boolean isFobiddenLb = ForbiddenStateUtils.getForbiddenLBstate(uid);
                            if (isFobiddenLb) {
                                AgoraClient.create().muteRemoteAudioStream(Integer.valueOf(uid), isFobiddenLb);
                            }
                        }
                    }
                }
                break;
            case 79:
                LeaveRoomParam leaveRoomParam = JsonConverter.fromJson(message, LeaveRoomParam.class);
                if (mView != null) {
                    mView.onMatcherExited(leaveRoomParam.getLeaveRoomCode(), leaveRoomParam.getLeaveRoomMsg());
                }
                break;

            case 87:
                ShareBeanItem shareBeanItem = JsonConverter.fromJson(message, ShareBeanItem.class);
                if (shareBeanItem != null) {
                    if (mView != null) {
                        mView.share(shareBeanItem);
                    }
                }
                break;
            case 91:
//                Log.d("AG_EX_AV", "91返回：" + message);
                UpdateAppointmentSongListParam updateAppointmentSongListParam = JsonConverter.fromJson(message, UpdateAppointmentSongListParam.class);
                if (updateAppointmentSongListParam != null) {
                    mView.updateSongAppointmentNum(updateAppointmentSongListParam.getSongCount());
                }
                break;
            case 94:
//                Log.d("AG_EX_AV", "room94返回：" + message);
                PlaySongParam songParam = JsonConverter.fromJson(message, PlaySongParam.class);
                if (mView != null) {
                    mView.changeMicroView(songParam.getSongInfo());
                }
                break;

            case 101://嘉宾收到同步播放歌曲的消息
                StartPlaySongParam playSongParam = JsonConverter.fromJson(message, StartPlaySongParam.class);
                if (playSongParam != null && playSongParam.getSongInfo() != null && mView != null) {
                    mView.startPlaySong(playSongParam.getSongInfo());
                }
                break;
            case 105:
                if (mView != null) {
                    SyncSongStateParam songStateParam = JsonConverter.fromJson(message, SyncSongStateParam.class);
                    mView.syncPlaySongPause(songStateParam.isPause(), songStateParam.getConsertUserId());
                }
                break;
            case 106:
                SyncSongVoiceParam syncSongVoiceParam = JsonConverter.fromJson(message, SyncSongVoiceParam.class);
                if (mView != null) {
                    mView.syncPlaySongVolume(syncSongVoiceParam.getVolume());
                }
                break;
            case 107:
                if (mView != null) {
                    mView.syncPlaySongRePlay();
                }
                break;
            case 108://同步切歌
                NextSongParam nextSongParam = JsonConverter.fromJson(message, NextSongParam.class);
                if (mView != null) {
                    mView.syncPlaySongCutSong(nextSongParam.getState(), nextSongParam.getConsertUserId());
                }
                break;
            case 110:
//                Log.d("AG_EX_AV", "110:" + message);
                if (mView != null) {
                    mView.syncSongStartDwonLoad();
                }
                break;
            case 111:
//                Log.d("AG_EX_AV", "111:" + message);
                if (mView != null) {
                    mView.syncSongSuccessDwonLoad(msg.getSuccess());
                }
                break;
            case 113:
//                Log.d("AG_EX_AV", "113:" + message);
                UpdateSongRankParam updateSongRankParam = JsonConverter.fromJson(message, UpdateSongRankParam.class);
                if (mView != null) {
                    mView.updateSongRankParam(updateSongRankParam.getRank());
                }
                break;
            case 115:
//                Log.d("AG_EX_AV", "115:" + message);
                NotifyAddFriendToClientParam param = JsonConverter.fromJson(message, NotifyAddFriendToClientParam.class);
                if (mView != null) {
                    mView.notifyAddFriend(String.valueOf(param.getTargetId()));
                }
                break;
            case 119:
                NotifyHelpToUserParam notifyHelpToUserParam = JsonConverter.fromJson(message, NotifyHelpToUserParam.class);
                RoomMsgBean smashMsg = mMsgHandler.getNofifyHelpMsg(notifyHelpToUserParam);
                addRoomMsg(Collections.singletonList(smashMsg));
                break;
            case 120:
                if (msg.getSuccess() == 1) {
                    ToastUtils.showToast("赠送成功");
                } else if (msg.getSuccess() == 2) {
                    ToastUtils.showToast("发送的蓝玫瑰数不对");
                } else if (msg.getSuccess() == 3) {
                    ToastUtils.showToast("蓝玫瑰不足");
                }
                break;
            case 121:
                //同步麦位玫瑰数
                SyncMicroRoseList syncMicroRoseList = JsonConverter.fromJson(message, SyncMicroRoseList.class);
                if (mView != null) {
                    for (SyncMicroRose syncMicroRose : syncMicroRoseList.getArgs()) {
                        mView.syncMicroRose(syncMicroRose);
                    }
                }
                break;
            case 122:
                UpdateSongLyricParam updateSongLyricParam = JsonConverter.fromJson(message, UpdateSongLyricParam.class);
                if (mView != null) {
                    mView.receiveLrc(updateSongLyricParam.getLyric());
                }
                break;
            case 124:
                SyncDemandSongParam syncDemandSongParam = JsonConverter.fromJson(message, SyncDemandSongParam.class);
                if (mView != null) {
                    mView.syncDemandSong(syncDemandSongParam.getDemandSongUserName());
                }
                break;
            case 125:
                UpdateHintParam updateHintParam = JsonConverter.fromJson(message, UpdateHintParam.class);
                if (mView != null) {
                    mView.updateExclusiveHint(updateHintParam.getMsg());
                }
                break;
            case 127:
                UpdateWheatCards updateWheatCards = JsonConverter.fromJson(message, UpdateWheatCards.class);
                if (mView != null) {
                    mView.updateWheatCards(updateWheatCards.getNumbers());
                }
                break;
            case 128:
                UpdateGuardSignToClientParam updateGuardSignToClientParam = JsonConverter.fromJson(message, UpdateGuardSignToClientParam.class);
                if (mView != null) {
                    mView.updateGuardSign(updateGuardSignToClientParam);
                }
                break;
            case 129:
                ShowGuardAnimationToClientParam showGuardAnimationToClientParam = JsonConverter.fromJson(message, ShowGuardAnimationToClientParam.class);
                if (mView != null) {
                    mView.showGuardGif(showGuardAnimationToClientParam);
                }
                break;
            case 134://红包列表
                NotifyRedPacketResultToClientParam redList = JsonConverter.fromJson(message, NotifyRedPacketResultToClientParam.class);
                if (mView != null) {
                    mView.showRobRedpacketResultList(redList.getUserRedPackets());
                }
                break;
            case 135:
                String content = object.get("Message").toString();
                if (mView != null) {
                    mView.showCenterHtmlToast(content);
                }

                break;
            case 136:
                String alert = object.get("Msg").toString();
                if (mView != null) {
                    mView.startRobRedPacket(alert);
                }
                break;
            case 137:
                //广播红包进度
                NotifyRedPacketProgressParam notifyRedPacketProgressParam = JsonConverter.fromJson(message, NotifyRedPacketProgressParam.class);
                if (mView != null) {
                    mView.notifyRedPacketProgress(notifyRedPacketProgressParam.isVisible(), notifyRedPacketProgressParam.getProgress());
                }
                break;
            case 138:
                //红包玩法介绍
                GetRedPacketPlayDescParam redPacketPlayDescParam = JsonConverter.fromJson(message, GetRedPacketPlayDescParam.class);
                if (mView != null) {
                    mView.showRedPackageRule(redPacketPlayDescParam.getPlayingDesc());
                }
                break;
            case 140:
                NotifyOnlineHeadImageParam onlineHeadImageParam = JsonConverter.fromJson(message, NotifyOnlineHeadImageParam.class);
                if (mView != null) {
                    List<String> ranks = onlineHeadImageParam.getOnlineHeadImages();
                    if (ranks == null) {
                        ranks = new ArrayList<>();
                    }
                    int size = ranks.size();
                    if (ranks.size() < 3) {
                        for (int i = 0; i < 3 - size; i++) {
                            ranks.add("");
                        }
                    }
                    mView.updateOnlineHeads(ranks);
                }
                break;
            case 141:
                UpdateUserBalanceParam userBalanceParam=JsonConverter.fromJson(message,UpdateUserBalanceParam.class);
                if (mView!=null){
                    mView.upDateBalance(userBalanceParam.getBalance());
                }
                break;
            case 301://KeepaLive
                if (mView != null) {
                    mView.keepLive();
                }
                break;
        }
    }


    protected void sendTextMessage(String toChatUsername, String myNickName) {
        EMMessage message = EMMessage.createTxtSendMessage("Hi~" + myNickName + ",我已成为你的好友，开聊吧", toChatUsername);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    private void showEnsureAlert(String message) {
        try {
            Context context = ActivityCache.getInstance().getTopActivity();
            AAlertDialog dialog = new AAlertDialog(context);
            dialog.setMessage(message, R.color.black, 15);
            dialog.setButton("确定", R.color.gray, null);
            dialog.show();
        } catch (Exception e) {

        }
    }

    private void addRoomMsg(List<RoomMsgBean> list) {
        if (mRoomModel != null) {
            List<RoomMsgBean> msgs = mRoomModel.getMsgs();
            msgs.addAll(list);
            if (msgs.size() > DEFAULT_MSG_MAX_NUM) {
                msgs.subList(0, msgs.size() - DEFAULT_MSG_MAX_NUM / 2).clear();
            }
        }
        if (mView != null) {
            mView.onAddMsg(list);
        } else {
            syncAddMessage(list);
        }
    }

    private void syncAddMessage(List<RoomMsgBean> list) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mView != null) {
                    mView.onAddMsg(list);
                }
            }
        }, 1000);
    }

    private void syncGuardPersonJoin(JoinUser user) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mView != null) {
                    mView.GuardPersonJoinRoom(user);
                }
            }
        }, 1000);
    }

    @Override
    public void onMsgClick(String uid) {
        if (mView != null) {
            mView.onMsgClick(uid);
        }
    }

    @Override
    public void onMsgLongClick(String uName) {
        if (mView != null) {
            mView.onMsgLongClick(uName);
        }
    }
}
