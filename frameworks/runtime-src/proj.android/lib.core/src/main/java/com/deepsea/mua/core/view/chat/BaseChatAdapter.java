package com.deepsea.mua.core.view.chat;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * @author RyanLee
 */
public abstract class BaseChatAdapter<D extends BaseChatMsg> extends RecyclerView.Adapter<BaseChatViewHolder<D>> {

    public abstract void addItem(D chatMsg);

    public abstract void addItemList(List<D> list);

    public abstract void removeItems(int startPos, int endPos);
}
