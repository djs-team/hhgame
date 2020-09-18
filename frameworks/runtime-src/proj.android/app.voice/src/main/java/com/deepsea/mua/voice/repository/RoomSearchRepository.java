package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.RoomSearchs;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/19
 */
public class RoomSearchRepository extends BaseRepository {

    @Inject
    public RoomSearchRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<RoomSearchs>> visited() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RoomSearchs, BaseApiResult<RoomSearchs>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RoomSearchs>>> createCall() {
                return mRetrofitApi.visited();
            }

            @Override
            public RoomSearchs processResponse(BaseApiResult<RoomSearchs> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<RoomSearchs>> getmoremsg(String is_more) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RoomSearchs, BaseApiResult<RoomSearchs>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RoomSearchs>>> createCall() {
                return mRetrofitApi.getmoremsg(is_more);
            }

            @Override
            public RoomSearchs processResponse(BaseApiResult<RoomSearchs> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<RoomSearchs>> roomSearch(String search, String type) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RoomSearchs, BaseApiResult<RoomSearchs>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RoomSearchs>>> createCall() {
                return mRetrofitApi.roomSearch(search, type);
            }

            @Override
            public RoomSearchs processResponse(BaseApiResult<RoomSearchs> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
