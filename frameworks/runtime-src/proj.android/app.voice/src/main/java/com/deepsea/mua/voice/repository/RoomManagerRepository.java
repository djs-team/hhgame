package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.RoomManagers;
import com.deepsea.mua.stub.entity.SearchManagers;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/8
 */
public class RoomManagerRepository extends BaseRepository {

    @Inject
    public RoomManagerRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<List<RoomManagers.ListBean>>> getManagers(String room_id, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<RoomManagers.ListBean>, BaseApiResult<RoomManagers>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RoomManagers>>> createCall() {
                return mRetrofitApi.getManagers(room_id, signature);
            }

            @Override
            public List<RoomManagers.ListBean> processResponse(BaseApiResult<RoomManagers> source) {
                if (source != null && source.getData() != null) {
                    return source.getData().getList();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<List<RoomManagers.ListBean>>> searchManagers(String room_id, String search, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<RoomManagers.ListBean>, BaseApiResult<SearchManagers>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<SearchManagers>>> createCall() {
                return mRetrofitApi.searchManagers(room_id, search, signature);
            }

            @Override
            public List<RoomManagers.ListBean> processResponse(BaseApiResult<SearchManagers> source) {
                if (source != null && source.getData() != null) {
                    return source.getData().getMember_list();
                }
                return null;
            }
        });
    }
}
