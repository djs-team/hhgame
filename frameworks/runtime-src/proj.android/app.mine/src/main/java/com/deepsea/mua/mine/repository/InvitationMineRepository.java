package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.FollowFanBean;
import com.deepsea.mua.stub.entity.InviteListBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class InvitationMineRepository extends BaseRepository {


    @Inject
    public InvitationMineRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<InviteListBean>> getMyInviteUserList(int page,int pageNum) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<InviteListBean, BaseApiResult<InviteListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<InviteListBean>>> createCall() {
                return mRetrofitApi.getMyInviteUserList(page, pageNum);
            }

            @Override
            public InviteListBean processResponse(BaseApiResult<InviteListBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> attention_member(String uid, String type) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.attention_member(uid, type);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
}
