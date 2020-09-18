package com.deepsea.mua.stub.network;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.deepsea.mua.core.network.AbsDataSource;
import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.core.utils.AbsentLiveData;
import com.deepsea.mua.core.utils.NetWorkUtils;
import com.deepsea.mua.core.utils.ToastUtils;

/**
 * Created by JUN on 2019/4/8
 */
public class HttpUtils {

    private static final AppExecutors mAppExecutors;

    static {
        mAppExecutors = new AppExecutors();
    }

    /**
     * 请求并保存数据
     *
     * @param callback
     * @param <R>      result
     * @param <S>      source
     * @return
     */
    public static <R, S> LiveData<Resource<R>> request(@NonNull HttpCallback.CacheCallback<R, S> callback) {
        return new AbsDataSource<R, S>(mAppExecutors) {
            @Override
            protected boolean shouldCache() {
                return true;
            }

            @Override
            protected void saveCallResult(@NonNull S item) {
                callback.saveCallResult(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable R data) {
                return callback.shouldFetch(data);
            }

            @NonNull
            @Override
            protected LiveData<R> loadFromDb() {
                return callback.loadFromDb();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<S>> createCall() {
                return callback.createCall();
            }
        }.asLiveData();
    }

    /**
     * 请求数据
     *
     * @param callback
     * @param <R>
     * @param <S>
     * @return
     */
    public static <R, S> LiveData<Resource<R>> requestNoCache(@NonNull HttpCallback.NoCacheCallback<R, S> callback) {
        return new AbsDataSource<R, S>(mAppExecutors) {
            @Override
            protected boolean shouldCache() {
                return false;
            }

            @Override
            protected void saveCallResult(@NonNull S item) {

            }

            @Override
            protected boolean shouldFetch(@Nullable R data) {
                return false;
            }

            @NonNull
            @Override
            protected LiveData<R> loadFromDb() {
                return AbsentLiveData.create();
            }

            @Override
            protected R processResponse(S s) {
                return callback.processResponse(s);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<S>> createCall() {
                return callback.createCall();
            }
        }.asLiveData();
    }

    public static boolean IsNetWorkEnable(Context context) {
        if (!NetWorkUtils.IsNetWorkEnable(context)) {
            ToastUtils.showToast(HttpConst.NETWORK_UNABLE);
            return false;
        }
        return true;
    }
}
