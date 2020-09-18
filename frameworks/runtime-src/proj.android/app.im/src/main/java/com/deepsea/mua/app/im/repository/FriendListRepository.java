package com.deepsea.mua.app.im.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.FriendInfoListBean;
import com.deepsea.mua.stub.entity.MessageNumVo;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;
import com.deepsea.mua.stub.utils.SignatureUtils;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class FriendListRepository extends BaseRepository {

    @Inject
    public FriendListRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    /**
     * 获取好友列表
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
