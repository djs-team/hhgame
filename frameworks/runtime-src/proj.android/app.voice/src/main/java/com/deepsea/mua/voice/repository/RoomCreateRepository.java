package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.RoomModeHelpBean;
import com.deepsea.mua.stub.entity.RoomModeHelpVo;
import com.deepsea.mua.stub.entity.RoomTagListBean;
import com.deepsea.mua.stub.entity.RoomTags;
import com.deepsea.mua.stub.entity.VoiceRoomBean;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/2
 */
public class RoomCreateRepository extends BaseRepository {

    @Inject
    public RoomCreateRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
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

    public LiveData<Resource<RoomTags>> getOldTagsList(String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RoomTags, BaseApiResult<RoomTags>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RoomTags>>> createCall() {
                return mRetrofitApi.getOldTagsList(signature);
            }

            @Override
            public RoomTags processResponse(BaseApiResult<RoomTags> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
    public LiveData<Resource<RoomTagListBean>> getTagsList(String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RoomTagListBean, BaseApiResult<RoomTagListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RoomTagListBean>>> createCall() {
                return mRetrofitApi.getTagsList(signature);
            }

            @Override
            public RoomTagListBean processResponse(BaseApiResult<RoomTagListBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }   public LiveData<Resource<RoomModeHelpBean>> getRoomTypeHelp() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RoomModeHelpBean, BaseApiResult<RoomModeHelpBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RoomModeHelpBean>>> createCall() {
                return mRetrofitApi.getRoomTypeHelp();
            }

            @Override
            public RoomModeHelpBean processResponse(BaseApiResult<RoomModeHelpBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
