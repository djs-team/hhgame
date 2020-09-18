package com.deepsea.mua.core.view.chat;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * @author RyanLee
 */
public abstract class BaseChatViewHolder<T> extends RecyclerView.ViewHolder implements IChatHolder<T> {
    @NonNull
    private SparseArray<View> mViews;


    public BaseChatViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    private View findViewById(@IdRes int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    protected View getView(@IdRes int viewId) {
        return findViewById(viewId);
    }
}
