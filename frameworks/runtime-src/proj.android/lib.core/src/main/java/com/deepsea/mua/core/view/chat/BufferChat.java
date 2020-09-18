package com.deepsea.mua.core.view.chat;

import android.os.Handler;
import android.os.Looper;


import java.util.ArrayList;
import java.util.List;

/**
 * 公屏缓冲池
 *
 * @author RyanLee
 */
public class BufferChat<D extends BaseChatMsg> implements IBufferChat<D> {
    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    private ISimpleChat<D> iSimpleChat;

    private List<D> mBufferLists;

    private int mUpdateTime;

    private static final Object LOCK = new Object();

    BufferChat(ISimpleChat chatManager, int bufferTime) {
        this.iSimpleChat = chatManager;
        this.mUpdateTime = bufferTime;
        mBufferLists = new ArrayList<>();
    }

    @Override
    public void play() {
        mUIHandler.removeCallbacks(this);
        mUIHandler.post(this);
    }

    @Override
    public void addChat(D chatMsg) {
        if (chatMsg == null) {
            return;
        }
        synchronized (LOCK) {
            mBufferLists.add(chatMsg);
        }
    }

    @Override
    public void addChat(List<D> chatLists) {
        if (chatLists == null || chatLists.isEmpty()) {
            return;
        }
        synchronized (LOCK) {
            mBufferLists.addAll(chatLists);
        }
    }

    @Override
    public void release() {
        mUIHandler.removeCallbacks(this);
        iSimpleChat = null;
        if (mBufferLists!=null) {
            mBufferLists.clear();
            mBufferLists = null;
        }

    }

    @Override
    public void run() {
        if (iSimpleChat == null) {
            return;
        }

        if (mBufferLists == null || mBufferLists.isEmpty()) {
            mUIHandler.removeCallbacks(this);
            mUIHandler.postDelayed(this, mUpdateTime);
            return;
        }

        synchronized (LOCK) {
            iSimpleChat.updateChatView(mBufferLists);
            mBufferLists.clear();
        }
        mUIHandler.removeCallbacks(this);
        mUIHandler.postDelayed(this, mUpdateTime);
    }

}
