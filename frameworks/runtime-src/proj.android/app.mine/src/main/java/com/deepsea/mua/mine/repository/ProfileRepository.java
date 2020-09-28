package com.deepsea.mua.mine.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.AreaVo;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.BlockVo;
import com.deepsea.mua.stub.entity.GuardInfoBean;
import com.deepsea.mua.stub.entity.JumpRoomVo;
import com.deepsea.mua.stub.entity.LookGuardUserVo;
import com.deepsea.mua.stub.entity.ProfileBean;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.RenewInitVo;
import com.deepsea.mua.stub.entity.TaskBean;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;
import com.deepsea.mua.stub.utils.Constant;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/12
 */
public class ProfileRepository extends BaseRepository {

    @Inject
    public ProfileRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }

    public LiveData<Resource<ProfileBean>> user_info(String uid, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<ProfileBean, BaseApiResult<ProfileBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<ProfileBean>>> createCall() {
                return mRetrofitApi.user_info(uid, signature);
            }

            @Override
            public ProfileBean processResponse(BaseApiResult<ProfileBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> attention_member(String uid, String type) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.attention_member(uid, type);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> defriend(String uid, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.defriend(uid, signature);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> blackout(String uid, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.blackout(uid, signature);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<AuditBean>> getVerifyToken(String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<AuditBean, BaseApiResult<AuditBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<AuditBean>>> createCall() {
                return mRetrofitApi.getVerifyToken(signature);
            }

            @Override
            public AuditBean processResponse(BaseApiResult<AuditBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }




    public LiveData<Resource<BaseApiResult>> createapprove(String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.createapprove(signature);
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





    /**
     * 随机跳转前六直播间接口
     *
     * @return
     */
    public LiveData<Resource<JumpRoomVo>> jumpRoom() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<JumpRoomVo, BaseApiResult<JumpRoomVo>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<JumpRoomVo>>> createCall() {
                return mRetrofitApi.jumpRoom();
            }

            @Override
            public JumpRoomVo processResponse(BaseApiResult<JumpRoomVo> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> applyInfo(String wx, String age) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.applyInfo(wx, age);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<List<AreaVo>>> getArea(int level, int pcode) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<AreaVo>, BaseApiResult<List<AreaVo>>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<List<AreaVo>>>> createCall() {
                return mRetrofitApi.getArea(level, pcode);
            }

            @Override
            public List<AreaVo> processResponse(BaseApiResult<List<AreaVo>> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    /**
     * 查看用户守护榜
     *
     * @return
     */
    public LiveData<Resource<LookGuardUserVo>> getGuardList(String userid, int page) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<LookGuardUserVo, BaseApiResult<LookGuardUserVo>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<LookGuardUserVo>>> createCall() {
                return mRetrofitApi.getGuardUserListForLive(userid, page);
            }

            @Override
            public LookGuardUserVo processResponse(BaseApiResult<LookGuardUserVo> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    /**
     * 任务中心页接口
     *
     * @return
     */
    public LiveData<Resource<TaskBean>> getTaskList() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<TaskBean, BaseApiResult<TaskBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<TaskBean>>> createCall() {
                return mRetrofitApi.taskList();
            }

            @Override
            public TaskBean processResponse(BaseApiResult<TaskBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<BaseApiResult>> taskReceive(String type, String taskId) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.taskReceive(type, taskId);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    public LiveData<Resource<GuardInfoBean>> guardInfoList(String type, int page) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<GuardInfoBean, BaseApiResult<GuardInfoBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<GuardInfoBean>>> createCall() {
                return mRetrofitApi.guardInfoList(type, page);
            }

            @Override
            public GuardInfoBean processResponse(BaseApiResult<GuardInfoBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<RenewInitVo>> initGuard(String target_id) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RenewInitVo, BaseApiResult<RenewInitVo>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RenewInitVo>>> createCall() {
                return mRetrofitApi.initGuard(target_id);
            }

            @Override
            public RenewInitVo processResponse(BaseApiResult<RenewInitVo> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
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

    public LiveData<Resource<List<BlockVo>>> getBlockList() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<BlockVo>, BaseApiResult<List<BlockVo>>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<List<BlockVo>>>> createCall() {
                return mRetrofitApi.getBlockList();
            }

            @Override
            public List<BlockVo> processResponse(BaseApiResult<List<BlockVo>> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

}
