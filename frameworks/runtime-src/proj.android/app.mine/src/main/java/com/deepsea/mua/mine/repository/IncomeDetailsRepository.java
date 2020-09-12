package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.IncomeListBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 *
 */
public class IncomeDetailsRepository extends BaseRepository {

    @Inject
    public IncomeDetailsRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<IncomeListBean>> getIncomeList(int page, int size, String stime, String etime) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<IncomeListBean, BaseApiResult<IncomeListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<IncomeListBean>>> createCall() {
                return mRetrofitApi.getIncomeList(page, size, stime, etime);
            }

            @Override
            public IncomeListBean processResponse(BaseApiResult<IncomeListBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<IncomeListBean>> getRedPackageIncomeList(int page, int size, String stime, String etime) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<IncomeListBean, BaseApiResult<IncomeListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<IncomeListBean>>> createCall() {
                return mRetrofitApi.getRedPackageIncomeList(page, size, stime, etime);
            }

            @Override
            public IncomeListBean processResponse(BaseApiResult<IncomeListBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
