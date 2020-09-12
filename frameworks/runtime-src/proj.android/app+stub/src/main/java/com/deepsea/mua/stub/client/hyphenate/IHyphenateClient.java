package com.deepsea.mua.stub.client.hyphenate;

import android.content.Context;

import com.deepsea.mua.im.domain.EaseUser;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;

/**
 * Created by JUN on 2019/8/19
 */
public abstract class IHyphenateClient {

    protected int mHyphenateStatus = DISCONNECT;
    protected int mChatRoomStatus = NONE;

    /*
     * 环信状态
     */
    public static final int LOGIN = 0x10;

    public static final int CONNECTED = 0x11;

    public static final int DISCONNECT = 0x12;

    public static final int LOGOUT = 0x13;

    /*
     *聊天室状态
     */
    public static final int JOIN_ROOM = 0x14;

    public static final int LEAVE_ROOM = 0x15;

    public static final int IN_ROOM = 0x16;

    public static final int NONE = 0x17;


    public interface HyphenateCallback {
        void apply();

        void onError(int code, String msg);
    }

    public interface HyphenateDelegate {
        void init(Context context);

        void onLogin();

        void onLogout();

        void saveContact(EaseUser user);
    }

    /**
     * 初始化
     *
     * @param context
     */
    public abstract void init(Context context);

    /**
     * 释放资源
     */
    public abstract void release();

    /**
     * 是否登录
     *
     * @return
     */
    public abstract boolean isLoggedIn();

    public abstract void setHyphenateDelegate(HyphenateDelegate delegate);

    /**
     * 保存联系人
     *
     * @param user
     */
    public abstract void saveContact(EaseUser user);

    /**
     * 获取环信状态
     *
     * @return
     */
    public int getHyphenateStatus() {
        return mHyphenateStatus;
    }

    /**
     * 修改环信状态
     *
     * @param hyphenateStatus
     */
    public void setHyphenateStatus(int hyphenateStatus) {
        this.mHyphenateStatus = hyphenateStatus;
    }

    /**
     * 获取聊天室状态
     *
     * @return
     */
    public int getChatRoomStatus() {
        return mChatRoomStatus;
    }

    /**
     * 修改聊天室状态
     *
     * @param chatRoomStatus
     */
    public void setChatRoomStatus(int chatRoomStatus) {
        this.mChatRoomStatus = chatRoomStatus;
    }

    /**
     * 登录
     *
     * @param id
     * @param password
     * @param callback
     */
    public abstract void login(String id, String password, final EMCallBack callback);

    /**
     * 退出登录
     *
     * @param unbindDeviceToken
     * @param callback
     */
    public abstract void logout(boolean unbindDeviceToken, final EMCallBack callback);

    /**
     * 发送消息
     *
     * @param message
     */
    public abstract void sendMessage(EMMessage message);

    /**
     * 发送消息
     *
     * @param message
     * @param p       消息回调
     */
    public abstract void sendMessage(EMMessage message, HyphenateCallback p);

    /**
     * 添加连接监听
     *
     * @param listener
     */
    public abstract void addConnectionListener(EMConnectionListener listener);

    /**
     * 移除监听
     *
     * @param listener
     */
    public abstract void removeConnectionListener(EMConnectionListener listener);

    /**
     * 添加消息监听
     *
     * @param listener
     */
    public abstract void addMessageListener(EMMessageListener listener);

    /**
     * 移除消息监听
     *
     * @param listener
     */
    public abstract void removeMessageListener(EMMessageListener listener);

    /**
     * 进入聊天室
     *
     * @param roomId
     * @param p
     */
    public abstract void joinChatRoom(String roomId, HyphenateCallback p);

    /**
     * 离开房间
     *
     * @param roomId
     */
    public abstract void leaveChatRoom(String roomId);
}
