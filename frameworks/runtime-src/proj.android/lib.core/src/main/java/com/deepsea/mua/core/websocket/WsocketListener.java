package com.deepsea.mua.core.websocket;

import okhttp3.Response;
import okio.ByteString;

/**
 * @author rabtma
 */
public class WsocketListener {
    public void onOpen(Response response) {
    }

    public void onMessage(String text) {
    }

    public void onMessage(ByteString bytes) {
    }

    public void onReconnect() {

    }

    public void onClosing(int code, String reason) {
    }

    public void onClosed(int code, String reason) {
    }

    public void onFailure(Throwable t, Response response) {
    }
}
