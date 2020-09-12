package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.RoomsBean;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.VoiceBanner;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/17
 */
public class RoomsRepository extends BaseRepository {

    @Inject
    public RoomsRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<RoomsBean>> getRooms(String room_type, int page, String signature,String age,String city,String city_two,String city_three) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RoomsBean, BaseApiResult<RoomsBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RoomsBean>>> createCall() {
                return mRetrofitApi.getRoomList(room_type, page, signature,age,city,city_two,city_three);
            }

            @Override
            public RoomsBean processResponse(BaseApiResult<RoomsBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
    public LiveData<Resource<List<VoiceBanner.BannerListBean>>> getBanners() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<VoiceBanner.BannerListBean>, BaseApiResult<VoiceBanner>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<VoiceBanner>>> createCall() {
                return mRetrofitApi.bannerlist("1");
            }

            @Override
            public List<VoiceBanner.BannerListBean> processResponse(BaseApiResult<VoiceBanner> source) {
                if (source != null && source.getData() != null) {
                    return source.getData().getBannerList();
                }
                return null;
            }
        });
    }
}
