package com.deepsea.mua.core.websocket;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Response;
import okio.ByteString;

/**
 * Created by JUN on 2019/4/17
 */
public class WsMsgEvent {

    private final ConcurrentHashMap<WsocketListener, Integer> mWsocketListeners = new ConcurrentHashMap<>();
    private Handler mHandler = new Handler(Looper.getMainLooper());

    WsMsgEvent() {
    }

    public void addWsocketListener(WsocketListener listener) {
        this.mWsocketListeners.put(listener, 0);
    }

    public void removeWsocketListener(WsocketListener listener) {
        this.mWsocketListeners.remove(listener);
    }

    public void clearWsocketListener() {
        this.mWsocketListeners.clear();
    }

    public final WsocketListener mWsocketListener = new WsocketListener() {

        public void onOpen(Response response) {
            for (WsocketListener listener : mWsocketListeners.keySet()) {
                mHandler.post(() -> listener.onOpen(response));
            }
        }

        public void onMessage(String text) {
            for (WsocketListener listener : mWsocketListeners.keySet()) {
                mHandler.post(() -> listener.onMessage(text));
            }
        }

        public void onMessage(ByteString bytes) {
            for (WsocketListener listener : mWsocketListeners.keySet()) {
                mHandler.post(() -> listener.onMessage(bytes));
            }
        }

        public void onReconnect() {
            for (WsocketListener listener : mWsocketListeners.keySet()) {
                mHandler.post(listener::onReconnect);
            }
        }

        public void onClosing(int code, String reason) {
            for (WsocketListener listener : mWsocketListeners.keySet()) {
                mHandler.post(() -> listener.onClosing(code, reason));
            }
        }

        public void onClosed(int code, String reason) {
            for (WsocketListener listener : mWsocketListeners.keySet()) {
                mHandler.post(() -> listener.onClosed(code, reason));
            }
        }

        public void onFailure(Throwable t, Response response) {
            for (WsocketListener listener : mWsocketListeners.keySet()) {
                mHandler.post(() -> listener.onFailure(t, response));
            }
        }
    };
}
