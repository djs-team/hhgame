package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.BindWx;
import com.deepsea.mua.stub.entity.CheckBindWx;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class SettingRepository extends BaseRepository {

    @Inject
    public SettingRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<BaseApiResult>> logout() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.logout();
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> cancell() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.cancell();
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> setFeedback(String content, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.setFeedback(content, signature);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> unBindWx(String wx_id) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.unBindWx(wx_id);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<CheckBindWx>> isBindWx() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<CheckBindWx, BaseApiResult<CheckBindWx>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<CheckBindWx>>> createCall() {
                return mRetrofitApi.isBindWx();
            }

            @Override
            public CheckBindWx processResponse(BaseApiResult<CheckBindWx> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<BaseApiResult<BindWx>>> bindWx(String wx_id) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult<BindWx>, BaseApiResult<BindWx>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<BindWx>>> createCall() {
                return mRetrofitApi.bindWx(wx_id);
            }

            @Override
            public BaseApiResult<BindWx> processResponse(BaseApiResult<BindWx> source) {
                if (source != null) {
                    return source;
                }
                return null;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> bindPhone(String phone, String pcode) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.bindPhone(phone, pcode);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                if (source != null) {
                    return source;
                }
                return null;
            }
        });
    }
    public LiveData<Resource<BaseApiResult>> sendSMS(String phone, String type) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.sendSMS(phone, type);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
}
