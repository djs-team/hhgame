package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.MineRooms;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/19
 */
public class MineRoomsRepository extends BaseRepository {

    @Inject
    public MineRoomsRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<MineRooms>> myRoom(String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<MineRooms, BaseApiResult<MineRooms>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<MineRooms>>> createCall() {
                return mRetrofitApi.myRoom(signature);
            }

            @Override
            public MineRooms processResponse(BaseApiResult<MineRooms> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
