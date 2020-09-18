package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.FansRankBean;
import com.deepsea.mua.stub.entity.TotalRank;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/30
 */
public class RankRepository extends BaseRepository {

    @Inject
    public RankRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<List<TotalRank>>> ranklist(String type, String status, String roomid) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<TotalRank>, BaseApiResult<List<TotalRank>>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<List<TotalRank>>>> createCall() {
                return mRetrofitApi.ranklist(type, status, roomid);
            }

            @Override
            public List<TotalRank> processResponse(BaseApiResult<List<TotalRank>> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<List<FansRankBean>>> fansRanks(String signature, String status) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<FansRankBean>, BaseApiResult<List<FansRankBean>>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<List<FansRankBean>>>> createCall() {
                return mRetrofitApi.fansRanks(signature, status);
            }

            @Override
            public List<FansRankBean> processResponse(BaseApiResult<List<FansRankBean>> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
