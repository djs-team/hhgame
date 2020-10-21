package com.deepsea.mua.app.im.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.GiftListBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class GiftPanelRepository extends BaseRepository {

    @Inject
    public GiftPanelRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<GiftListBean>> refresh(String signature,String is_room) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<GiftListBean, BaseApiResult<GiftListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<GiftListBean>>> createCall() {
                return mRetrofitApi.getFriendGiftList(signature,is_room);
            }

            @Override
            public GiftListBean processResponse(BaseApiResult<GiftListBean> source) {
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
