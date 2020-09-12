package com.deepsea.mua.core.websocket;

import android.util.Log;

import com.deepsea.mua.core.log.Logg;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * @author rabtman
 */
public class WsocketManager extends WebSocketListener implements IWebSocket {

    private static final String TAG = "WsManager";

    //pong请求间隔秒
    private static final int PONG_INTERVAL = 10;
    //自动检查间隔秒
    private static final int OBSERVABLE_INTERVAL = 5000;
    //最高允许的连续失败次数
    private static final int ATTEMPT_TOLERANCE = 2;
    //重连次数
    private int mReconnectCount = 0;
    //是否允许自动重连
    private boolean isNeedReconnect;
    private String mWsUrl;
    private int mCurrentStatus = WsocketStatus.DISCONNECTED;

    private WebSocket mWebSocket;
    private Lock mLock;
    private OkHttpClient mOkHttpClient;
    private Disposable mDisposable;
    private WsMsgEvent mMsgEvent;
    private static volatile WsocketManager mInstance;

    private WsocketManager() {
        this.mLock = new ReentrantLock();
        this.mMsgEvent = new WsMsgEvent();
    }

    public static WsocketManager create() {
        if (mInstance == null) {
            synchronized (WsocketManager.class) {
                if (mInstance == null) {
                    mInstance = new WsocketManager();
                }
            }
        }
        return mInstance;
    }

    public void addWsocketListener(WsocketListener listener) {
        this.mMsgEvent.addWsocketListener(listener);
    }

    public void removeWsocketListener(WsocketListener listener) {
        this.mMsgEvent.removeWsocketListener(listener);
    }

    public void clearWsocketListener() {
        this.mMsgEvent.clearWsocketListener();
    }

    @Override
    public WebSocket getWebSocket() {
        return mWebSocket;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
    }

    public void setWsUrl(String wsUrl) {
        mWsUrl = wsUrl;
    }

    public void setNeedReconnect(boolean needReconnect) {
        isNeedReconnect = needReconnect;
    }

    @Override
    public void startConnect() {
        onStart();
        startObservable();
    }

    private synchronized void onStart() {
        switch (getCurrentStatus()) {
            case WsocketStatus.CONNECTED:
            case WsocketStatus.CONNECTING:
                break;
            default:
                setCurrentStatus(WsocketStatus.CONNECTING);
                initWs();
        }
    }

    private void initWs() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();
        }
        Request request = new Request.Builder()
                .url(mWsUrl)
                .build();
        mOkHttpClient.dispatcher().cancelAll();
        try {
            mLock.lockInterruptibly();
            try {
                mOkHttpClient.newWebSocket(request, this);
            } finally {
                mLock.unlock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startObservable() {
        stopObservable();
        mDisposable = Observable.interval(0, OBSERVABLE_INTERVAL, TimeUnit.MILLISECONDS)
                .filter(aLong -> isNeedReconnect)
                .map(aLong -> isConnected())
                .subscribeOn(Schedulers.computation())
                .subscribe(websocketAlive -> {
                    if (!websocketAlive) {
                        mReconnectCount++;
                        setCurrentStatus(WsocketStatus.RECONNECT);
                        onStart();
                    }
                });
    }

    private void stopObservable() {
        if (mDisposable != null && (!mDisposable.isDisposed())) {
            mDisposable.dispose();
        }
    }

    @Override
    public void stopConnect() {
        if (mCurrentStatus == WsocketStatus.DISCONNECTED) {
            return;
        }
        mReconnectCount = 0;
        setCurrentStatus(WsocketStatus.DISCONNECTED);
        stopObservable();
        if (mOkHttpClient != null) {
            mOkHttpClient.dispatcher().cancelAll();
        }
        if (mWebSocket != null) {
            boolean isClosed = mWebSocket.close(WsocketStatus.CODE.NORMAL_CLOSE, WsocketStatus.TIP.NORMAL_CLOSE);
        }
    }

    @Override
    public synchronized boolean isConnected() {
        return mWebSocket != null && mCurrentStatus == WsocketStatus.CONNECTED;
    }

    @Override
    public synchronized int getCurrentStatus() {
        return mCurrentStatus;
    }

    @Override
    public synchronized void setCurrentStatus(int currentStatus) {
        this.mCurrentStatus = currentStatus;
    }

    @Override
    public boolean sendMessage(String msg) {
        return send(msg);
    }

    @Override
    public boolean sendMessage(ByteString byteString) {
        return send(byteString);
    }

    private boolean send(Object msg) {
        boolean isSend = false;
        if (isConnected()) {
            if (msg instanceof String) {
                isSend = mWebSocket.send((String) msg);
            } else if (msg instanceof ByteString) {
                isSend = mWebSocket.send((ByteString) msg);
            }
            if (!isSend) {
                stopObservable();
            }
        }
        Logg.e(TAG, "send: " + msg + " isSend = " + isSend);
        return isSend;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Logg.e(TAG, "onOpen: " + response);
        mWebSocket = webSocket;
        setCurrentStatus(WsocketStatus.CONNECTED);
        mReconnectCount = 0;
        mMsgEvent.mWsocketListener.onOpen(response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Logg.e(TAG, "onMessage: " + text);
        mMsgEvent.mWsocketListener.onMessage(text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        mMsgEvent.mWsocketListener.onMessage(bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.e(TAG, "onClosing: " + reason);
        mMsgEvent.mWsocketListener.onClosing(code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Logg.e(TAG, "onClosed: " + reason);
        mMsgEvent.mWsocketListener.onClosed(code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        Logg.e(TAG, "onFailure: " + t.getMessage());
        setCurrentStatus(WsocketStatus.DISCONNECTED);
        mMsgEvent.mWsocketListener.onFailure(t, response);
    }
}
