package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.CashInfo;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class CashWithdrawalRepository extends BaseRepository {

    @Inject
    public CashWithdrawalRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }


    public LiveData<Resource<BaseApiResult>> cash(String apliuserid, String cash, String totalcash, String type) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.cash(apliuserid, cash, totalcash, type);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
    public LiveData<Resource<BaseApiResult>> redpacketCash(String apliuserid, String cash, String totalcash, String type) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.redpacketCash(apliuserid, cash, totalcash, type);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<CashInfo>> fetchCashInfo() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<CashInfo, BaseApiResult<CashInfo>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<CashInfo>>> createCall() {
                return mRetrofitApi.cashinfo();
            }

            @Override
            public CashInfo processResponse(BaseApiResult<CashInfo> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
    public LiveData<Resource<CashInfo>> fetchCashRedpackageInfo() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<CashInfo, BaseApiResult<CashInfo>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<CashInfo>>> createCall() {
                return mRetrofitApi.redpacketCashinfo();
            }

            @Override
            public CashInfo processResponse(BaseApiResult<CashInfo> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
