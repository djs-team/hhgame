package org.cocos2dx.javascript.repository;

import android.arch.lifecycle.LiveData;
import android.text.TextUtils;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.LocationVo;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.entity.UserBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;
import com.deepsea.mua.stub.utils.AppConstant;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/10/16
 */
public class RegisterRepository extends BaseRepository {

    @Inject
    public RegisterRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<OSSConfigBean>> getOssConfig() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<OSSConfigBean, BaseApiResult<OSSConfigBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<OSSConfigBean>>> createCall() {
                return mRetrofitApi.getOssConfigHeadiv();
            }

            @Override
            public OSSConfigBean processResponse(BaseApiResult<OSSConfigBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<User>> register(String username, String avatar, String nickname, int age, String sex, String inviteCode, String registration_id, String wx_id) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<User, BaseApiResult<UserBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<UserBean>>> createCall() {
                return mRetrofitApi.register(new HashMap<String, String>() {{
                    put("username", username);
                    put("avatar", avatar);
                    put("nickname", nickname);
                    put("age", String.valueOf(age));
                    put("sex", sex);
                    put("registration_id", registration_id);
                    if (!TextUtils.isEmpty(inviteCode)) {
                        put("referrerd", inviteCode);
                    }
                    LocationVo vo = AppConstant.getInstance().getLocationVo();
                    if (vo != null) {
                        put("longitude", String.valueOf(vo.getLongitude()));
                        put("latitude", String.valueOf(vo.getLatitude()));
                        put("state", String.valueOf(vo.getProvince()));
                        put("city", String.valueOf(vo.getCity()));
                        put("locality", String.valueOf(vo.getArea()));
                    }
                    if (!TextUtils.isEmpty(wx_id)) {
                        put("wx_id", wx_id);
                    }
                }});
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
