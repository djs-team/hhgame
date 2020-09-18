package com.deepsea.mua.kit.app;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.NightLockBean;
import com.deepsea.mua.stub.entity.YoungerTimeBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/7/17
 */
public class MuaRepository extends BaseRepository {

    @Inject
    public MuaRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<YoungerTimeBean>> checkLock() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<YoungerTimeBean, BaseApiResult<YoungerTimeBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<YoungerTimeBean>>> createCall() {
                return mRetrofitApi.checkLock();
            }

            @Override
            public YoungerTimeBean processResponse(BaseApiResult<YoungerTimeBean> source) {
                return source.getData();
            }
        });
    }


    public LiveData<Resource<BaseApiResult>> checkPop() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.checkPop();
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<NightLockBean>> checkConsLock() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<NightLockBean, BaseApiResult<NightLockBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<NightLockBean>>> createCall() {
                return mRetrofitApi.checkConsLock();
            }

            @Override
            public NightLockBean processResponse(BaseApiResult<NightLockBean> source) {
                return source.getData();
            }
        });
    }
}
