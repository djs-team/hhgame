package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.MDRecord;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/6
 */
public class WalletRecordRepository extends BaseRepository {

    @Inject
    public WalletRecordRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<MDRecord>> mbdetails(int pageNumber) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<MDRecord, BaseApiResult<MDRecord>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<MDRecord>>> createCall() {
                return mRetrofitApi.mbdetails(pageNumber);
            }

            @Override
            public MDRecord processResponse(BaseApiResult<MDRecord> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<MDRecord>> diamondtails(int pageNumber) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<MDRecord, BaseApiResult<MDRecord>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<MDRecord>>> createCall() {
                return mRetrofitApi.diamondtails(pageNumber);
            }

            @Override
            public MDRecord processResponse(BaseApiResult<MDRecord> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
