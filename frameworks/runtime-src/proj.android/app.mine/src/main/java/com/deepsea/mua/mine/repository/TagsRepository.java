package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.BlindDateBean;
import com.deepsea.mua.stub.entity.TagBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class TagsRepository extends BaseRepository {

    @Inject
    public TagsRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }


    public LiveData<Resource<TagBean>> fetchInfo() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<TagBean, BaseApiResult<TagBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<TagBean>>> createCall() {
                return mRetrofitApi.hobby_feature();
            }

            @Override
            public TagBean processResponse(BaseApiResult<TagBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
    public LiveData<Resource<BaseApiResult>> edit(String hobby_id,String feature_id) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.hobby_feature_edit(hobby_id,feature_id);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
}
