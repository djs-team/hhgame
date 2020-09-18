package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.FollowFanBean;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class FollowFanRepository extends BaseRepository {

    @Inject
    public FollowFanRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<FollowFanBean>> getFollowFans(String type, int page) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<FollowFanBean, BaseApiResult<FollowFanBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<FollowFanBean>>> createCall() {
                return mRetrofitApi.getFollowFans(type, page);
            }

            @Override
            public FollowFanBean processResponse(BaseApiResult<FollowFanBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> attention_member(String uid, String type) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.attention_member(uid, type);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
}
