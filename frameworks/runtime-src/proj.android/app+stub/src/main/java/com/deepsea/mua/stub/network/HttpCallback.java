package com.deepsea.mua.stub.network;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.response.ApiResponse;

/**
 * Created by JUN on 2019/4/8
 */
public interface HttpCallback {

    /**
     * @param <R> result
     * @param <S> source
     */
    interface CacheCallback<R, S> {
        void saveCallResult(S item);

        boolean shouldFetch(R item);

        LiveData<R> loadFromDb();

        LiveData<ApiResponse<S>> createCall();
    }

    /**
     * @param <R> result
     * @param <S> source
     */
    interface NoCacheCallback<R, S> {
        LiveData<ApiResponse<S>> createCall();

        R processResponse(S source);
    }
}
