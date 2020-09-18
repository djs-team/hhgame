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
import com.deepsea.mua.stub.entity.NightLockBean;
import com.deepsea.mua.stub.entity.YoungerTimeBean;
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

    public LiveData<Resource<YoungerTimeBean>> checkLock() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<YoungerTimeBean, BaseApiResult<YoungerTimeBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<YoungerTimeBean>>> createCall() {
                return mRetrofitApi.checkLock();
            }

            @Override
            public YoungerTimeBean processResponse(BaseApiResult<YoungerTimeBean> source) {
                return source.getData();
            }
        });
    }


    public LiveData<Resource<BaseApiResult>> checkPop() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.checkPop();
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<NightLockBean>> checkConsLock() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<NightLockBean, BaseApiResult<NightLockBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<NightLockBean>>> createCall() {
                return mRetrofitApi.checkConsLock();
            }

            @Override
            public NightLockBean processResponse(BaseApiResult<NightLockBean> source) {
                return source.getData();
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
}
