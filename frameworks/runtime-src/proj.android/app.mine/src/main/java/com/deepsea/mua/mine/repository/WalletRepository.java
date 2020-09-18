package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.BlueRoseExchange;
import com.deepsea.mua.stub.entity.ExchangeBlueRoseRecordListParam;
import com.deepsea.mua.stub.entity.InitCash;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.mvp.ResponseModel;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class WalletRepository extends BaseRepository {

    @Inject
    public WalletRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<WalletBean>> wallet() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<WalletBean, BaseApiResult<WalletBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<WalletBean>>> createCall() {
                return mRetrofitApi.wallet();
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

    public LiveData<Resource<InitCash>> initCash() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<InitCash, BaseApiResult<InitCash>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<InitCash>>> createCall() {
                return mRetrofitApi.initCash();
            }

            @Override
            public InitCash processResponse(BaseApiResult<InitCash> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<List<BlueRoseExchange>>> initBlueExchange() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<BlueRoseExchange>, BaseApiResult<List<BlueRoseExchange>>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<List<BlueRoseExchange>>>> createCall() {
                return mRetrofitApi.initBlueExchange();
            }

            @Override
            public List<BlueRoseExchange> processResponse(BaseApiResult<List<BlueRoseExchange>> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
    public LiveData<Resource<ExchangeBlueRoseRecordListParam>> blueExchangeInfo(int page) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<ExchangeBlueRoseRecordListParam, BaseApiResult<ExchangeBlueRoseRecordListParam>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<ExchangeBlueRoseRecordListParam>>> createCall() {
                return mRetrofitApi.blueExchangeInfo(page);
            }

            @Override
            public ExchangeBlueRoseRecordListParam processResponse(BaseApiResult<ExchangeBlueRoseRecordListParam> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> checkSatus() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.checkSatus();
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> exchangeRose(int coin) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.exchangeRose(coin);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> exchangeBlue(String num, String giftId) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.exchangeBlue(num, giftId);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
}
