package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/6
 */
public class ExchangeMdRepository extends BaseRepository {

    @Inject
    public ExchangeMdRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<WalletBean>> initExchange() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<WalletBean, BaseApiResult<WalletBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<WalletBean>>> createCall() {
                return mRetrofitApi.initExchange();
            }

            @Override
            public WalletBean processResponse(BaseApiResult<WalletBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<WalletBean>> redpacketInitExchange() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<WalletBean, BaseApiResult<WalletBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<WalletBean>>> createCall() {
                return mRetrofitApi.redpacketInitExchange();
            }

            @Override
            public WalletBean processResponse(BaseApiResult<WalletBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> mdExchange(String amount, boolean isRedPackage) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                if (isRedPackage) {
                    return mRetrofitApi.redpacketExchangeRose(amount);

                } else {
                    return mRetrofitApi.mdExchange(amount);
                }
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
}
