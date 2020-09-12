package com.deepsea.mua.app.im.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class AddFriendRepository extends BaseRepository {

    @Inject
    public AddFriendRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    /**
     * 送礼加好友请求
     * @param map
     * @return
     */
    public LiveData<Resource<BaseApiResult>> addFriendly(Map<String,String> map) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.addFriendly(map);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
              return source;
            }
        });
    }
    /**
     * 检查是否有充值权限
     * @return
     */
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
}
