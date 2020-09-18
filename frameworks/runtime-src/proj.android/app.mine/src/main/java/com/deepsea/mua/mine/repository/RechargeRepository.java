package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.ChargeBean;
import com.deepsea.mua.stub.entity.HaiPayBean;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;
import com.deepsea.mua.stub.utils.Constant;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/16
 */
public class RechargeRepository extends BaseRepository {

    @Inject
    public RechargeRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    /**
     * 微信支付
     *
     * @param rmb
     * @param type
     * @param is_active
     * @param uid
     * @return
     */
    public LiveData<Resource<WxOrder>> wxpay(String rmb, String type, String is_active, String uid, String chargeid
            , String target_id, String guard_id, String long_day) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<WxOrder, BaseApiResult<WxOrder>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<WxOrder>>> createCall() {
                return mRetrofitApi.wxpay(rmb, Constant.CHARGE_WX, type, is_active, uid, chargeid,
                        target_id, guard_id, long_day);
            }

            @Override
            public WxOrder processResponse(BaseApiResult<WxOrder> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    /**
     * 海贝支付
     *
     * @param rmb
     * @param type
     * @param is_active
     * @param uid
     * @return
     */
    public LiveData<Resource<HaiPayBean>> haipay(String rmb, String type, String is_active, String uid, String chargeid
            , String target_id, String guard_id, String long_day) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<HaiPayBean, BaseApiResult<HaiPayBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<HaiPayBean>>> createCall() {
                return mRetrofitApi.haipay(rmb, Constant.CHARGE_HAIBEI, type, is_active, uid, chargeid
                        , target_id, guard_id, long_day);
            }

            @Override
            public HaiPayBean processResponse(BaseApiResult<HaiPayBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    /**
     * 支付宝支付
     *
     * @param rmb
     * @param type
     * @param is_active
     * @param uid
     * @return
     */
    public LiveData<Resource<String>> alipay(String rmb, String type, String is_active, String uid, String chargeid,
                                             String target_id, String guard_id, String long_day) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<String, BaseApiResult<String>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<String>>> createCall() {
                return mRetrofitApi.alipay(rmb, Constant.CHARGE_ALI, type, is_active, uid, chargeid, target_id, guard_id, long_day);
            }

            @Override
            public String processResponse(BaseApiResult<String> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<ChargeBean>> chargelist() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<ChargeBean, BaseApiResult<ChargeBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<ChargeBean>>> createCall() {
                return mRetrofitApi.chargelist();
            }

            @Override
            public ChargeBean processResponse(BaseApiResult<ChargeBean> source) {
                if (source != null && source.getData() != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }
}
