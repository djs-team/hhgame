package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.CashInfo;
import com.deepsea.mua.stub.entity.InviteCodeBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class InviteCodeRepository extends BaseRepository {

    @Inject
    public InviteCodeRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }


    public LiveData<Resource<InviteCodeBean>> getInviteDownUrl() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<InviteCodeBean, BaseApiResult<InviteCodeBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<InviteCodeBean>>> createCall() {
                return mRetrofitApi.getInviteDownUrl();
            }

            @Override
            public InviteCodeBean processResponse(BaseApiResult<InviteCodeBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> shareCallback() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.shareCallBack();
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
}
