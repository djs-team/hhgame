package com.deepsea.mua.stub.repository;

import com.deepsea.mua.stub.api.RetrofitApi;

/**
 * Created by JUN on 2019/4/8
 */
public class BaseRepository {

    protected final RetrofitApi mRetrofitApi;

    public BaseRepository(RetrofitApi retrofitApi) {
        this.mRetrofitApi = retrofitApi;
    }
}
