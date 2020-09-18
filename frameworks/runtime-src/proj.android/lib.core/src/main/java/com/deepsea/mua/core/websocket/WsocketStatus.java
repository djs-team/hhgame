package com.deepsea.mua.core.websocket;

/**
 * @author rabtman
 */

public interface WsocketStatus {

    int CONNECTED = 1;
    int CONNECTING = 0;
    int RECONNECT = 2;
    int DISCONNECTED = -1;

    interface CODE {
        int NORMAL_CLOSE = 1000;
    }

    interface TIP {
        String NORMAL_CLOSE = "normal close";
    }
}
