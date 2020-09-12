package com.deepsea.mua.core.view.chat;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * 公屏消息管理器
 *
 * @author RyanLee
 */
public class SimpleChatManager<D extends BaseChatMsg> implements ISimpleChat<D> {
    private static final int DEFAULT_ITEM_SPACE = 0;
    private static final int DEFAULT_SCROLL_ITEM_NUM = 10;
    private static final int DEFAULT_MAX_CHAT_NUM = 100;
    private static final int DEFAULT_BUFFER_TIME = 400;

    private RecyclerView mChatView;
    private BaseChatAdapter<D> mAdapter;
    private LinearLayoutManager mLinearManager;
    private int mBufferTime = DEFAULT_BUFFER_TIME;
    private int mMaxChatNum = DEFAULT_MAX_CHAT_NUM;

    /**
     * Item间隔
     */
    private int mItemSpace = DEFAULT_ITEM_SPACE;
    /**
     * 默认滚动条数
     */
    private int mScrollItemNum = DEFAULT_SCROLL_ITEM_NUM;
    /**
     * 缓冲区
     */
    private IBufferChat<D> iBufferChat;


    SimpleChatManager(@NonNull RecyclerView mRecyclerView) {
        this.mChatView = mRecyclerView;
    }

    private void initBufferChat() {
        iBufferChat = new BufferChat<>(this, mBufferTime);
        iBufferChat.play();
    }

    /**
     * 监听是否滑动到最新
     */
    private void addScrollListener() {
        mChatView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleIndex = getLastVisibleIndex();
                    int itemSize = getItemSize();
                    if (lastVisibleIndex == itemSize - 1) {
                    }
                }
            }
        });
    }

    private void initChatView() {
        // 设置Item间距
//        mChatView.addItemDecoration(new ChatDecoration(mItemSpace));
        // 设置LayoutManager
        mLinearManager = new LinearLayoutManager(mChatView.getContext());
        mChatView.setLayoutManager(mLinearManager);
        mChatView.setAdapter(mAdapter);
    }


    @Override
    public void sendMultiMsg(List<D> list) {
        if (iBufferChat != null) {
            iBufferChat.addChat(list);
//            mChatView.scrollToPosition(mAdapter.getItemCount()-1);
        }
    }

    @Override
    public void sendSingleMsg(D chatMsg) {
        if (iBufferChat != null) {
            iBufferChat.addChat(chatMsg);
//            mChatView.scrollToPosition(mAdapter.getItemCount()-1);
        }
    }

    @Override
    public void updateChatView(List<D> mBufferLists) {
        mAdapter.addItemList(mBufferLists);
        removeOverItems();

        // 如果在底部，自动滚动到最新的消息
//        if (isAtBottom()) {
            runToBottom();
//        }
    }

    @Override
    public void release() {
        if (iBufferChat != null) {
            iBufferChat.release();
        }
    }

    private void removeOverItems() {
        int dataSize = getItemSize();
        final int goal = mMaxChatNum / 2;
        if (dataSize > mMaxChatNum) {
            int beyondSize = dataSize - goal;
            mAdapter.removeItems(0, beyondSize);
        }
    }

    /**
     * 是否处于底部
     *
     * @return true 是 false 否
     */
    private boolean isAtBottom() {
        boolean canScrollVertically = mChatView.canScrollVertically(1);

//        return mLinearManager.findLastVisibleItemPosition() == mAdapter.getItemCount() - 1;
        return !canScrollVertically;
    }

    private void runToBottom() {
        mChatView.post(new Runnable() {
            @Override
            public void run() {
                // 获取底部index
                int bottomIndex = mAdapter.getItemCount() - 1;
                // 获取最后一条可见的
                int lastVisibleIndex = mLinearManager.findLastVisibleItemPosition();
//                if (bottomIndex - lastVisibleIndex >= mScrollItemNum) {
//                    // 如果最后一条可见的Item和数据源最后一条Item的间隔超过mScrollItemNum
//                    // 则先移动到最后mScrollItemNum条
//                    mChatView.scrollToPosition(bottomIndex - mScrollItemNum);
//                }
                mChatView.scrollToPosition(bottomIndex);
            }
        });
    }

    private int getLastVisibleIndex() {
        return mLinearManager.findLastCompletelyVisibleItemPosition();
    }

    private int getItemSize() {
        return mAdapter.getItemCount();
    }

    private int getDataSize() {
        return mAdapter.getItemCount() - 1;
    }

    public void setAdapter(BaseChatAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void ready() {
        initChatView();
//        addScrollListener();
        initBufferChat();
    }

    public void setBufferTime(int bufferTime) {
        if (bufferTime < 0) {
            bufferTime = DEFAULT_BUFFER_TIME;
        }
        this.mBufferTime = bufferTime;
    }

    public void setMaxChatNum(int maxChatNum) {
        if (maxChatNum < DEFAULT_MAX_CHAT_NUM)
            return;

        this.mMaxChatNum = maxChatNum;
    }
}
