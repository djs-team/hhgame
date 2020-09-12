package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.entity.ReportListBean;
import com.deepsea.mua.stub.entity.ReportsBean;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/8
 */
public class RoomReportRepository extends BaseRepository {

    @Inject
    public RoomReportRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

        public LiveData<Resource<List<ReportListBean>>> getReports() {
            return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<ReportListBean>, BaseApiResult<List<ReportListBean>>>() {
                @Override
                public LiveData<ApiResponse<BaseApiResult<List<ReportListBean>>>> createCall() {
                    return mRetrofitApi.getReportList();
                }

                @Override
                public List<ReportListBean> processResponse(BaseApiResult<List<ReportListBean>> source) {
                    if (source != null) {
                        return source.getData();
                    }
                    return null;
                }
            });
        }

    public LiveData<Resource<BaseApiResult>> report_room(String report_roomid, String report_content) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.report_room(report_roomid, report_content);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> complain(Map<String,String> map) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.reportUser(map);
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
}
