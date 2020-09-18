package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.VisitorBean;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class VisitorsRepository extends BaseRepository {

    @Inject
    public VisitorsRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<VisitorBean>> visitorMember(int page, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<VisitorBean, BaseApiResult<VisitorBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<VisitorBean>>> createCall() {
                return mRetrofitApi.visitorMember(page, signature);
            }

            @Override
            public VisitorBean processResponse(BaseApiResult<VisitorBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
