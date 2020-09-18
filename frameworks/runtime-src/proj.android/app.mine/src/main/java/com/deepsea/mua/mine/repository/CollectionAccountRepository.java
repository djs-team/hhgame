package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.CashInfo;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class CollectionAccountRepository extends BaseRepository {

    @Inject
    public CollectionAccountRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
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

    public LiveData<Resource<BaseApiResult>> bindaplipay(String apliuserid, String type,String justpic,String backpic) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.bindAlipay(apliuserid, type,justpic,backpic);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
    public LiveData<Resource<OSSConfigBean>> getOssConfig() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<OSSConfigBean, BaseApiResult<OSSConfigBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<OSSConfigBean>>> createCall() {
                return mRetrofitApi.getOssConfigHeadiv();
            }

            @Override
            public OSSConfigBean processResponse(BaseApiResult<OSSConfigBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
