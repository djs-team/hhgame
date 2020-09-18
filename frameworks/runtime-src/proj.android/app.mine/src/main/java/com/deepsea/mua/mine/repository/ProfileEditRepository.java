package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.entity.ProfileModel;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;
import com.deepsea.mua.stub.utils.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by JUN on 2019/4/16
 */
public class ProfileEditRepository extends BaseRepository {

    @Inject
    public ProfileEditRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<BaseApiResult>> uploadAvatar(String avatar, String signature) {
        File file = new File(avatar);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("signature", signature)
                .addFormDataPart("avatar", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();

        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.uploadAvatar(requestBody);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
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

    public LiveData<Resource<BaseApiResult>> setavatar(String avatar) {

        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.setavatar(avatar);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> updateUserInfo(String profile, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.updateUserInfo(profile, signature);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<ProfileModel>> getMenusList() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<ProfileModel, BaseApiResult<ProfileModel>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<ProfileModel>>> createCall() {
                return mRetrofitApi.getMenusList();
            }

            @Override
            public ProfileModel processResponse(BaseApiResult<ProfileModel> source) {
                if (source != null) {
                    ProfileModel data = source.getData();
                    List<ProfileModel.ProfileMenu> list = new ArrayList<>();

                    ProfileModel.MenuBean jbxx = data.getJbxx();
                    if (!CollectionUtils.isEmpty(jbxx.getMenus())) {
                        jbxx.getMenus().get(0).setMenuName(jbxx.getName());
                        list.addAll(jbxx.getMenus());
                    }

                    ProfileModel.MenuBean grzl = data.getGrzl();
                    if (!CollectionUtils.isEmpty(grzl.getMenus())) {
                        grzl.getMenus().get(0).setMenuName(grzl.getName());
                        list.addAll(grzl.getMenus());
                    }

                    data.setOptions(list);
                    return data;
                }
                return null;
            }
        });
    }

    public LiveData<Resource<List<ProfileModel.ProfileMenu>>> getConditionList() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<ProfileModel.ProfileMenu>, BaseApiResult<LinkedHashMap<String, ProfileModel.MenuBean>>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<LinkedHashMap<String, ProfileModel.MenuBean>>>> createCall() {
                return mRetrofitApi.getConditionList();
            }

            @Override
            public List<ProfileModel.ProfileMenu> processResponse(BaseApiResult<LinkedHashMap<String, ProfileModel.MenuBean>> source) {
                if (source != null && source.getData() != null && !source.getData().isEmpty()) {
                    List<ProfileModel.ProfileMenu> list = new ArrayList<>();

                    Set<Map.Entry<String, ProfileModel.MenuBean>> entries = source.getData().entrySet();
                    for (Map.Entry<String, ProfileModel.MenuBean> entry : entries) {
                        List<ProfileModel.ProfileMenu> menus = entry.getValue().getMenus();
                        if (menus != null) {
                            menus.get(0).setMenuName(entry.getValue().getName());
                            list.addAll(menus);
                        }
                    }

                    return list;
                }
                return null;
            }
        });
    }
}
