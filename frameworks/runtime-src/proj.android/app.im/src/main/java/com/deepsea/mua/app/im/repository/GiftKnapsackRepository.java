package com.deepsea.mua.app.im.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.GiftInfoBean;
import com.deepsea.mua.stub.entity.PackBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class GiftKnapsackRepository extends BaseRepository {

    @Inject
    public GiftKnapsackRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<List<PackBean>>> getMePacks(String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<PackBean>, BaseApiResult<List<PackBean>>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<List<PackBean>>>> createCall() {
                return mRetrofitApi.getMePacks(signature);
            }

            @Override
            public List<PackBean> processResponse(BaseApiResult<List<PackBean>> source) {
                if (source != null && source.getData() != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
    /**
     * 送礼加好友请求
     *
     * @param map
     * @return
     */
    public LiveData<Resource<BaseApiResult>> addFriendly(Map<String, String> map) {
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
}
