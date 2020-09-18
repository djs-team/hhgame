package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.ApplyFriendListBean;
import com.deepsea.mua.stub.entity.OnlinesBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class FullServiceSortRepository extends BaseRepository {

    @Inject
    public FullServiceSortRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<OnlinesBean>> OnlineList(Map<String, String> map) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<OnlinesBean, BaseApiResult<OnlinesBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<OnlinesBean>>> createCall() {
                return mRetrofitApi.OnlineList(map);
            }

            @Override
            public OnlinesBean processResponse(BaseApiResult<OnlinesBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
