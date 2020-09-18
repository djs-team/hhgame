package com.deepsea.mua.core.view.chat;

/**
 * @author RyanLee
 */
public interface IChatHolder<T> {
    /**
     * 数据绑定
     * @param data Object
     * @param position int
     */
    void bindData(T data, int position);
}
