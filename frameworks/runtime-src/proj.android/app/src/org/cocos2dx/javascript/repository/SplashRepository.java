package org.cocos2dx.javascript.repository;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.core.utils.JsonConverter;
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
 * Created by JUN on 2019/4/15
 */
public class SplashRepository extends BaseRepository {

    @Inject
    public SplashRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<User>> autologin() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<User, BaseApiResult<UserBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<UserBean>>> createCall() {
                LocationVo vo= AppConstant.getInstance().getLocationVo();

                if (vo != null) {
                    Log.d("location", JsonConverter.toJson(vo));
                    return mRetrofitApi.autologin(vo.getLongitude(), vo.getLatitude(), vo.getProvince(), vo.getCity(), vo.getArea());
                } else {
                    Log.d("location", "splash_null");
                    return mRetrofitApi.autologin(0, 0, "", "", "");
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
}
