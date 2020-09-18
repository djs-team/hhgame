package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.ExchangeMdDetailListBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 *
 */
public class ExchangeMDDetailsRepository extends BaseRepository {

    @Inject
    public ExchangeMDDetailsRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<ExchangeMdDetailListBean>> getExchangeMDDetailsList(int page, int size, String stime, String etime,boolean isRedpackage) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<ExchangeMdDetailListBean, BaseApiResult<ExchangeMdDetailListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<ExchangeMdDetailListBean>>> createCall() {
                if (isRedpackage){
                    return mRetrofitApi.getRedpacketExchangeDetailsList(page, size, stime, etime);

                }else {
                    return mRetrofitApi.getExchangeDetailsList(page, size, stime, etime);
                }
            }

            @Override
            public ExchangeMdDetailListBean processResponse(BaseApiResult<ExchangeMdDetailListBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
