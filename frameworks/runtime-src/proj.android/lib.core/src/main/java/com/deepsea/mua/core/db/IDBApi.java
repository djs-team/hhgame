package com.deepsea.mua.core.db;

import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by tong on 16/5/4.
 */
public interface IDBApi {

    /**
     * 保存数据,默认key为类名
     *
     * @param object
     * @param <T>
     */
    <T> void insert(T object);

    /**
     * 删除数据,默认key为类名
     *
     * @param clazz
     * @param <T>
     */
    <T> void delete(Class<T> clazz);

    /**
     * 获取数据
     *
     * @param clazz
     * @param <T>
     * @return LiveData
     */
    <T> LiveData<T> find(Class<T> clazz);

    /**
     * 保存数据
     *
     * @param key
     * @param object
     * @param <T>
     */
    <T> void insert(String key, T object);

    /**
     * 删除数据
     *
     * @param key
     */
    void delete(String key);

    /**
     * 获取数据
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> LiveData<T> find(String key, Class<T> clazz);

    /**
     * 查找对应key的所有数据
     *
     * @param key
     * @param isLike
     * @param <T>
     * @return
     */
    <T> LiveData<List<T>> findAll(String key, Class<T> clazz, boolean isLike);

    /**
     * 删除对应key的所有数据
     *
     * @param key
     * @param isLike
     */
    void deleteAll(String key, boolean isLike);

    /**
     * 关闭数据库,app退出时关闭
     */
    void close();
}
