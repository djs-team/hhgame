package com.deepsea.mua.stub.client.hyphenate;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.deepsea.mua.core.log.Logg;
import com.deepsea.mua.im.domain.EaseUser;
import com.deepsea.mua.stub.client.app.AppClient;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.utils.UserUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUN on 2019/8/19
 */
public class HyphenateClient extends IHyphenateClient {

    private static final String TAG = "HyphenateClient";
    private static volatile IHyphenateClient mInstance;

    private static final int DEFAULT_MAX_REJOIN_ROOM = 4;

    private String mLoginedID;

    private String mJoinRoomID;
    //最大重连次数
    private int mMaxReJoinRoomNum = 0;

    private boolean isAddListener;

    private List<EMMessage> mFailureRoomMsgs;

    private HyphenateDelegate mDelegate;

    public static IHyphenateClient getInstance() {
        if (mInstance == null) {
            synchronized (HyphenateClient.class) {
                if (mInstance == null) {
                    mInstance = new HyphenateClient();
                }
            }
        }
        return mInstance;
    }

    private HyphenateClient() {
    }

    @Override
    public void init(Context context) {
//        HxHelper.getInstance().init(context);
        if (mDelegate != null) {
            mDelegate.init(context);
        }
    }

    @Override
    public void release() {
        removeConnectionListener(mConnectionListener);
        if (mFailureRoomMsgs != null) {
            mFailureRoomMsgs.clear();
        }
        try {
            EMClient.getInstance().chatroomManager().destroyChatRoom(mJoinRoomID);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

        mLoginedID = null;
        mJoinRoomID = null;
    }

    @Override
    public boolean isLoggedIn() {
        return mHyphenateStatus == CONNECTED;
    }

    @Override
    public void setHyphenateDelegate(HyphenateDelegate delegate) {
        this.mDelegate = delegate;
    }

    @Override
    public void saveContact(EaseUser user) {
        if (mDelegate != null) {
            mDelegate.saveContact(user);
        }
    }

    @Override
    public void login(String id, String password, EMCallBack callback) {
        if (mHyphenateStatus == LOGIN || mHyphenateStatus == LOGOUT || isLoggedIn())
            return;

        if (!isAddListener) {
            addConnectionListener(mConnectionListener);
            isAddListener = true;
        }

        mHyphenateStatus = LOGIN;
        mLoginedID = id;

        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(password)) {
            Logg.d(TAG, "login hx error user or password is empty");
            if (callback != null) {
                callback.onError(100, "user or password is empty");
            }
            return;
        }

        EMClient.getInstance().login(id, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                Logg.d(TAG, "login hx success");
                mLoginedID = null;

                if (callback != null) {
                    callback.onSuccess();
                }

                if (mDelegate != null) {
                    mDelegate.onLogin();
                }
            }

            @Override
            public void onError(int i, String s) {
                Logg.d(TAG, "login hx error i = " + i + " s = " + s);
                if (callback != null) {
                    callback.onError(i, s);
                }
                mHyphenateStatus = DISCONNECT;
                if (AppClient.getInstance().isRunning()) {
                    login(id, password, null);
                }
            }

            @Override
            public void onProgress(int i, String s) {
                if (callback != null) {
                    callback.onProgress(i, s);
                }
            }
        });
    }

    @Override
    public void logout(boolean unbindDeviceToken, EMCallBack callback) {
        int preHyphenateStatus = mHyphenateStatus;
        mHyphenateStatus = LOGOUT;
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {
            @Override
            public void onSuccess() {
                Logg.d(TAG, "logout hx success");
                mHyphenateStatus = DISCONNECT;
                if (callback != null) {
                    callback.onSuccess();
                }

                if (mDelegate != null) {
                    mDelegate.onLogout();
                }
            }

            @Override
            public void onError(int i, String s) {
                Logg.d(TAG, "logout hx error i = " + i + " s = " + s);
                if (mHyphenateStatus == LOGOUT) {
                    mHyphenateStatus = preHyphenateStatus;
                }

                if (callback != null) {
                    callback.onError(i, s);
                }

                if (mDelegate != null) {
                    mDelegate.onLogout();
                }
            }

            @Override
            public void onProgress(int i, String s) {
                if (callback != null) {
                    callback.onProgress(i, s);
                }
            }
        });
    }

    @Override
    public void sendMessage(EMMessage message) {
        sendMessage(message, null);
    }

    @Override
    public void sendMessage(EMMessage message, HyphenateCallback p) {
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Logg.d(TAG, "发送消息成功 " + message.toString());
                if (p != null) {
                    p.apply();
                }
            }

            @Override
            public void onError(int i, String s) {
                Logg.d(TAG, "发送消息失败 " + i + " s = " + s);
                if (message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    if (mFailureRoomMsgs == null) {
                        mFailureRoomMsgs = new ArrayList<>();
                    }
                    mFailureRoomMsgs.add(message);
                }
                if (p != null) {
                    p.onError(i, s);
                }
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        try {
            EMClient.getInstance().chatManager().sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
            Logg.d(TAG, "发送消息异常 " + e.getMessage() + "  " + message.toString());
            p.onError(-1, e.toString());
        }
    }

    @Override
    public void addConnectionListener(EMConnectionListener listener) {
        EMClient.getInstance().addConnectionListener(listener);
    }

    @Override
    public void removeConnectionListener(EMConnectionListener listener) {
        EMClient.getInstance().removeConnectionListener(listener);
    }

    @Override
    public void addMessageListener(EMMessageListener listener) {
        EMClient.getInstance().chatManager().addMessageListener(listener);
    }

    @Override
    public void removeMessageListener(EMMessageListener listener) {
        EMClient.getInstance().chatManager().removeMessageListener(listener);
    }

    @Override
    public void joinChatRoom(String roomId, HyphenateCallback p) {
        Logg.d(TAG, "roomId = " + roomId + " mChatRoomStatus = " + mChatRoomStatus);

        if (mChatRoomStatus != NONE && TextUtils.isEmpty(roomId)) {
            return;
        }

        if (!TextUtils.equals(mJoinRoomID, roomId)) {
            mMaxReJoinRoomNum = 0;
        }

        int preStatus = mChatRoomStatus;
        mChatRoomStatus = JOIN_ROOM;
        mJoinRoomID = roomId;
        EMClient.getInstance().chatroomManager().joinChatRoom(roomId, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom emChatRoom) {
                Logg.d(TAG, "join hx room success");
                mJoinRoomID = null;
                mChatRoomStatus = IN_ROOM;
                if (p != null) {
                    p.apply();
                }

                if (mFailureRoomMsgs != null) {
                    for (EMMessage message : mFailureRoomMsgs) {
                        Log.e(TAG, "send failure msg " + message.toString());
                        if (TextUtils.equals(roomId, message.getTo())) {
                            sendMessage(message, null);
                        }
                    }
                }
            }

            @Override
            public void onError(int i, String s) {
                Logg.d(TAG, "join hx room error i =  " + i + " s = " + s);
                mChatRoomStatus = preStatus;
                if (isLoggedIn() && mMaxReJoinRoomNum < DEFAULT_MAX_REJOIN_ROOM) {
                    mMaxReJoinRoomNum++;
                    joinChatRoom(mJoinRoomID, null);
                }
                //登录环信后加入房间
                else if (AppClient.getInstance().isRunning() && mChatRoomStatus == DISCONNECT) {
                    login(UserUtils.getUser().getUid(), UserUtils.getUser().getUid(), null);
                }

                if (p != null) {
                    p.onError(i, s);
                }
            }
        });
    }

    @Override
    public void leaveChatRoom(String roomId) {
        mJoinRoomID = null;
        mMaxReJoinRoomNum = 0;
        mChatRoomStatus = LEAVE_ROOM;
        if (mFailureRoomMsgs != null) {
            mFailureRoomMsgs.clear();
            mFailureRoomMsgs = null;
        }
        EMClient.getInstance().chatroomManager().leaveChatRoom(roomId);
        mChatRoomStatus = NONE;
    }

    private EMConnectionListener mConnectionListener = new EMConnectionListener() {
        @Override
        public void onConnected() {
            Logg.d(TAG, "环信连接成功");
            mHyphenateStatus = CONNECTED;

            if (!TextUtils.isEmpty(mJoinRoomID)) {
                joinChatRoom(mJoinRoomID, null);
            }
        }

        @Override
        public void onDisconnected(int i) {
            Logg.d(TAG, "环信断开连接 code " + i);
            mHyphenateStatus = DISCONNECT;

            if (i != EMError.USER_LOGIN_ANOTHER_DEVICE) {
                if (AppClient.getInstance().isRunning()) {
                    User user = UserUtils.getUser();
                    login(user.getUid(), user.getUid(), null);
                }
            } else {
                joinChatRoom(mJoinRoomID, null);
            }
        }
    };
}
