package org.cocos2dx.javascript.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.LocationVo;
import com.deepsea.mua.stub.entity.UserBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;
import com.deepsea.mua.stub.utils.AppConstant;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/3/28
 */
public class LoginRepository extends BaseRepository {

    @Inject
    public LoginRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<User>> login(String account, String vertify, String registration_id) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<User, BaseApiResult<UserBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<UserBean>>> createCall() {
                LocationVo vo = AppConstant.getInstance().getLocationVo();
                if (vo != null) {
                    return mRetrofitApi.login(account, vertify, vo.getLongitude(), vo.getLatitude(), vo.getProvince(), vo.getCity(), vo.getArea(), registration_id);
                } else {
                    return mRetrofitApi.login(account, vertify, 0, 0, "", "", "", registration_id);

                }
            }

            @Override
            public User processResponse(BaseApiResult<UserBean> source) {
                if (source != null && source.getData() != null) {
                    return source.getData().getInfo();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<User>> oneLogin(String registration_id, String token) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<User, BaseApiResult<UserBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<UserBean>>> createCall() {
                LocationVo vo = AppConstant.getInstance().getLocationVo();
                if (vo != null) {
                    return mRetrofitApi.oneLogin(token, vo.getLongitude(), vo.getLatitude(), vo.getProvince(), vo.getCity(), vo.getArea(), registration_id);
                } else {
                    return mRetrofitApi.oneLogin(token, 0, 0, "", "", "", registration_id);

                }
            }

            @Override
            public User processResponse(BaseApiResult<UserBean> source) {
                if (source != null && source.getData() != null) {
                    return source.getData().getInfo();
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

    public LiveData<Resource<BaseApiResult>> isWxPhoneRegister(String username, String vertify) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.isWxPhoneRegister(username, vertify);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<UserBean>> thirdlogin(String openid, String latitude, String longitude, String state, String city, String locality, String registration_id) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<UserBean, BaseApiResult<UserBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<UserBean>>> createCall() {
                return mRetrofitApi.thirdlogin(openid, latitude, longitude, state, city, locality, registration_id);
            }

            @Override
            public UserBean processResponse(BaseApiResult<UserBean> source) {
                if (source != null && source.getData() != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<UserBean>> bindmobile(String openid, String type, String payload, String username, String vertify) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<UserBean, BaseApiResult<UserBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<UserBean>>> createCall() {
                return mRetrofitApi.bindmobile(openid, type, payload, username, vertify);
            }

            @Override
            public UserBean processResponse(BaseApiResult<UserBean> source) {
                if (source != null && source.getData() != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<User>> removebind(String openid, String type, String payload, String username) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<User, BaseApiResult<UserBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<UserBean>>> createCall() {
                return mRetrofitApi.removebind(openid, type, payload, username);
            }

            @Override
            public User processResponse(BaseApiResult<UserBean> source) {
                if (source != null && source.getData() != null) {
                    return source.getData().getInfo();
                }
                return null;
            }
        });
    }
}
