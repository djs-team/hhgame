/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.deepsea.mua.stub.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic RecyclerView adapter that uses Data Binding & DiffUtil.
 *
 * @param <T> Type of the mData in the list
 * @param <V> The type of the ViewDataBinding
 */
public abstract class BaseBindingAdapter<T, V extends ViewDataBinding>
        extends RecyclerView.Adapter<BindingViewHolder<V>> {

    protected List<T> mData;
    protected Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public BaseBindingAdapter(Context context) {
        this.mContext = context;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    @Override
    public final BindingViewHolder<V> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        V binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), getLayoutId(), parent, false);
        return new BindingViewHolder<>(binding);
    }

    @Override
    public final void onBindViewHolder(@NonNull BindingViewHolder<V> holder, int position) {
        bind(holder, getItem(position));
        holder.binding.executePendingBindings();
        if (mOnItemClickListener != null) {
            holder.setOnClickListener(holder.itemView, o -> mOnItemClickListener.onItemClick(holder.itemView, position));
        }
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> mOnItemLongClickListener.onItemLongClick(v, position));
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public void setNewData(List<T> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public void addData(List<T> data) {
        if (data != null && !data.isEmpty()) {
            if (mData == null) {
                mData = new ArrayList<>();
            }
            int start = mData.size();
            mData.addAll(data);
            notifyItemRangeInserted(start, data.size());
        }
    }

    public void addData(T data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.add(data);
        notifyItemInserted(mData.size() - 1);
    }

    public void remove(int position) {
        if (mData != null && position < mData.size()) {
            mData.remove(position);
            notifyItemRemoved(position);
            if (position != mData.size() - 1) {
                notifyItemRangeChanged(position, mData.size() - position);
            }
        }
    }

    /**
     * 移除指定范围数据
     *
     * @param start (inclusive)
     * @param end   (exclusive)
     */
    public void remove(int start, int end) {
        if (mData != null && start < end && start < mData.size() && end < mData.size()) {
            mData.subList(start, end).clear();
            notifyItemRangeRemoved(start, end - start);
        }
    }

    public T getItem(int position) {
        if (mData != null && position < mData.size() && position >= 0) {
            return mData.get(position);
        }
        return null;
    }

    public List<T> getData() {
        return mData;
    }

    protected abstract int getLayoutId();

    protected abstract void bind(BindingViewHolder<V> holder, T item);
}
