package org.cocos2dx.javascript.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.FaceRequestBean;
import com.deepsea.mua.stub.entity.FriendInfoListBean;
import com.deepsea.mua.stub.entity.JumpRoomVo;
import com.deepsea.mua.stub.entity.MessageNumVo;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;
import com.deepsea.mua.stub.utils.SignatureUtils;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/7/17
 */
public class MainRepository extends BaseRepository {

    @Inject
    public MainRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }



    /**
     * 获取好友列表
     *
     * @return
     */
    public LiveData<Resource<FriendInfoListBean>> getFriendList() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<FriendInfoListBean, BaseApiResult<FriendInfoListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<FriendInfoListBean>>> createCall() {
                return mRetrofitApi.getFriendList(SignatureUtils.signByToken());
            }

            @Override
            public FriendInfoListBean processResponse(BaseApiResult<FriendInfoListBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    /**
     * 随机跳转前六直播间接口
     *
     * @return
     */
    public LiveData<Resource<JumpRoomVo>> jumpRoom() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<JumpRoomVo, BaseApiResult<JumpRoomVo>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<JumpRoomVo>>> createCall() {
                return mRetrofitApi.jumpRoom();
            }

            @Override
            public JumpRoomVo processResponse(BaseApiResult<JumpRoomVo> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    /**
     * 消息未读数量
     *
     * @return
     */
    public LiveData<Resource<MessageNumVo>> getMessageNum() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<MessageNumVo, BaseApiResult<MessageNumVo>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<MessageNumVo>>> createCall() {
                return mRetrofitApi.getMessageNum();
            }

            @Override
            public MessageNumVo processResponse(BaseApiResult<MessageNumVo> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
    public LiveData<Resource<FaceRequestBean>> getMakeFace() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<FaceRequestBean, BaseApiResult<FaceRequestBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<FaceRequestBean>>> createCall() {
                return mRetrofitApi.getMakeFace();
            }

            @Override
            public FaceRequestBean processResponse(BaseApiResult<FaceRequestBean> source) {
                return source.getData();
            }
        });
    }
}
