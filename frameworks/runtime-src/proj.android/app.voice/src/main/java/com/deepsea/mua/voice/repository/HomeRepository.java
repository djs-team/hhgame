package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.IsCreateRoomVo;
import com.deepsea.mua.stub.entity.RankList;
import com.deepsea.mua.stub.entity.RoomModes;
import com.deepsea.mua.stub.entity.VoiceBanner;
import com.deepsea.mua.stub.entity.VoiceRoomBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/17
 */
public class HomeRepository extends BaseRepository {

    @Inject
    public HomeRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<RoomModes>> getRoomModes(String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RoomModes, BaseApiResult<RoomModes>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RoomModes>>> createCall() {
                return mRetrofitApi.getRoomModes(signature);
            }

            @Override
            public RoomModes processResponse(BaseApiResult<RoomModes> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
    public LiveData<Resource<RankList>> indexrank() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RankList, BaseApiResult<RankList>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RankList>>> createCall() {
                return mRetrofitApi.indexrank();
            }

            @Override
            public RankList processResponse(BaseApiResult<RankList> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<IsCreateRoomVo>> iscreateroom() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<IsCreateRoomVo, BaseApiResult<IsCreateRoomVo>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<IsCreateRoomVo>>> createCall() {
                return mRetrofitApi.iscreateroom();
            }

            @Override
            public IsCreateRoomVo processResponse(BaseApiResult<IsCreateRoomVo> source) {
                return source.getData();
            }
        });
    }

    public LiveData<Resource<VoiceRoomBean.RoomInfoBean>> create() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<VoiceRoomBean.RoomInfoBean, BaseApiResult<VoiceRoomBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<VoiceRoomBean>>> createCall() {

                return mRetrofitApi.create();
            }

            @Override
            public VoiceRoomBean.RoomInfoBean processResponse(BaseApiResult<VoiceRoomBean> source) {
                if (source != null) {
                    return source.getData().getRoom_info();
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
