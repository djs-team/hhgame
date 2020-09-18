package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.OnlinesBean;
import com.deepsea.mua.stub.entity.RankListResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/10/14
 */
public class SongRepository extends BaseRepository {

    @Inject
    public SongRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }


    public LiveData<Resource<RankListResult>> rankingList(int page,int type, int date, String roomId) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RankListResult, BaseApiResult<RankListResult>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RankListResult>>> createCall() {
                Map<String,String> map=new HashMap<>();
                map.put("page",String.valueOf(page));
//                map.put("pagenum",String.valueOf(pagenum));
                map.put("type",String.valueOf(type));
                map.put("date", String.valueOf(date));
                map.put("roomId",String.valueOf(roomId));
                return mRetrofitApi.rankingList(map);
            }

            @Override
            public RankListResult processResponse(BaseApiResult<RankListResult> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

}
