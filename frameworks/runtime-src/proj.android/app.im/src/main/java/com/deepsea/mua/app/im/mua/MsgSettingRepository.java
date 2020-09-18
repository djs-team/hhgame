package com.deepsea.mua.app.im.mua;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.CheckBlackVo;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/9/12
 */
public class MsgSettingRepository extends BaseRepository {

    @Inject
    public MsgSettingRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<BaseApiResult>> fansmenusatatus(String type) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.fansmenusatatus(type);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> defriend(String uid, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.defriend(uid, signature);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> blackout(String uid, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.blackout(uid, signature);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<CheckBlackVo>> isBlack(String uid) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<CheckBlackVo, BaseApiResult<CheckBlackVo>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<CheckBlackVo>>> createCall() {
                return mRetrofitApi.isBlack(uid);
            }

            @Override
            public CheckBlackVo processResponse(BaseApiResult<CheckBlackVo> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
