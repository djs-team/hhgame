package com.deepsea.mua.app.im.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.ApplyFriendListBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class FriendRequestRepository extends BaseRepository {

    @Inject
    public FriendRequestRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<ApplyFriendListBean>> getApplyFriendList(String signature, int pageNumber) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<ApplyFriendListBean, BaseApiResult<ApplyFriendListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<ApplyFriendListBean>>> createCall() {
                return mRetrofitApi.getApplyFriendList(signature,pageNumber);
            }

            @Override
            public ApplyFriendListBean processResponse(BaseApiResult<ApplyFriendListBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
 public LiveData<Resource<ApplyFriendListBean>> getMyApplyList(String signature, int pageNumber) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<ApplyFriendListBean, BaseApiResult<ApplyFriendListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<ApplyFriendListBean>>> createCall() {
                return mRetrofitApi.getMyApplyList(signature,pageNumber);
            }

            @Override
            public ApplyFriendListBean processResponse(BaseApiResult<ApplyFriendListBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    /**
     * 通过好友请求--不是好友
     * @param map
     * @return
     */
    public LiveData<Resource<BaseApiResult>> passFriendly(Map<String,String> map) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.passFriendly(map);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
              return source;
            }
        });
    }
    /**
     * 通过好友请求--是好友
     * @param map
     * @return
     */
    public LiveData<Resource<BaseApiResult>> agreeFriendly(Map<String,String> map) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.agreeFriendly(map);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
    /**
     * 拒绝好友请求
     * @param map
     * @return
     */
    public LiveData<Resource<BaseApiResult>> delFriendly(Map<String,String> map) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.delFriendly(map);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
              return source;
            }
        });
    }
}
