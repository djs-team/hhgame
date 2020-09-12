package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.CashWListBean;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;

import javax.inject.Inject;

/**
 * 
 */
public class CashWithdrawalDetailsRepository extends BaseRepository {

    @Inject
    public CashWithdrawalDetailsRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<CashWListBean>> getCashList(int page, int size, String stime, String etime,boolean isRedpackage) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<CashWListBean, BaseApiResult<CashWListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<CashWListBean>>> createCall() {
                if (isRedpackage){
                    return  mRetrofitApi.getRedpackageCashList(page,size,stime,etime);
                }else {
                    return mRetrofitApi.getCashList(page, size, stime, etime);
                }
            }

            @Override
            public CashWListBean processResponse(BaseApiResult<CashWListBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

}
