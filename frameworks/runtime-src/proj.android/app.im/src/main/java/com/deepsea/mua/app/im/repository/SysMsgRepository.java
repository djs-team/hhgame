package com.deepsea.mua.app.im.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.SystemMsgListBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;
import com.deepsea.mua.stub.utils.SignatureUtils;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class SysMsgRepository extends BaseRepository {

    @Inject
    public SysMsgRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<SystemMsgListBean>> getSystemMsgList(int pageNum) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<SystemMsgListBean, BaseApiResult<SystemMsgListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<SystemMsgListBean>>> createCall() {
                return mRetrofitApi.getSystemMsgList(SignatureUtils.signByToken(),pageNum);
            }

            @Override
            public SystemMsgListBean processResponse(BaseApiResult<SystemMsgListBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
    /**
     * 开播发送推送消息
     *
     * @return
     */
    public LiveData<Resource<BaseApiResult>> systemDel(String id) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.systemDel(id);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
}
