package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.RoomDesc;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/8
 */
public class RoomSetRepository extends BaseRepository {

    @Inject
    public RoomSetRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<RoomDesc>> roomDesc(String room_id, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RoomDesc, BaseApiResult<RoomDesc>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RoomDesc>>> createCall() {
                return mRetrofitApi.room_detail(room_id, signature);
            }

            @Override
            public RoomDesc processResponse(BaseApiResult<RoomDesc> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
