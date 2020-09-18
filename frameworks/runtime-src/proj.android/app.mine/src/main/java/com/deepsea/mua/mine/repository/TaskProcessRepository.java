package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.BlindDateBean;
import com.deepsea.mua.stub.entity.TaskProgressBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class TaskProcessRepository extends BaseRepository {

    @Inject
    public TaskProcessRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }


    public LiveData<Resource<TaskProgressBean>> fetchInfo() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<TaskProgressBean, BaseApiResult<TaskProgressBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<TaskProgressBean>>> createCall() {
                return mRetrofitApi.taskProgress();
            }

            @Override
            public TaskProgressBean processResponse(BaseApiResult<TaskProgressBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

}
