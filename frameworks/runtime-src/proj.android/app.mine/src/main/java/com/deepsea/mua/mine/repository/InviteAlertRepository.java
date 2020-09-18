package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.BlindDateBean;
import com.deepsea.mua.stub.entity.InviteAlertMemberBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class InviteAlertRepository extends BaseRepository {

    @Inject
    public InviteAlertRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }


    public LiveData<Resource<InviteAlertMemberBean>> fetchInfo(String code) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<InviteAlertMemberBean, BaseApiResult<InviteAlertMemberBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<InviteAlertMemberBean>>> createCall() {
                return mRetrofitApi.inviteMemberInfo(code);
            }

            @Override
            public InviteAlertMemberBean processResponse(BaseApiResult<InviteAlertMemberBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

}
