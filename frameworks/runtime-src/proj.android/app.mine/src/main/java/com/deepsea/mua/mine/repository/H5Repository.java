package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.AuthenticationBean;
import com.deepsea.mua.stub.entity.ProfileBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/12
 */
public class H5Repository extends BaseRepository {

    @Inject
    public H5Repository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }
    public LiveData<Resource<AuditBean>> getVerifyToken(String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<AuditBean, BaseApiResult<AuditBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<AuditBean>>> createCall() {
                return mRetrofitApi.getVerifyToken(signature);
            }

            @Override
            public AuditBean processResponse(BaseApiResult<AuditBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> createapprove(String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.createapprove(signature);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                if (source != null) {
                    return source;
                }
                return null;
            }
        });
    }
}
