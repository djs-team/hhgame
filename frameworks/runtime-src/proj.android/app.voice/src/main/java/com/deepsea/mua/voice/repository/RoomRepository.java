package com.deepsea.mua.voice.repository;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.stub.api.RetrofitApi;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.AuthenticationBean;
import com.deepsea.mua.stub.entity.EmojiBean;
import com.deepsea.mua.stub.entity.FirstRechargeVo;
import com.deepsea.mua.stub.entity.FriendInfoListBean;
import com.deepsea.mua.stub.entity.GiftBean;
import com.deepsea.mua.stub.entity.GiftInfoBean;
import com.deepsea.mua.stub.entity.InitCash;
import com.deepsea.mua.stub.entity.PackBean;
import com.deepsea.mua.stub.entity.RecommendRoomResult;
import com.deepsea.mua.stub.entity.RenewInitVo;
import com.deepsea.mua.stub.entity.SmashParamBean;
import com.deepsea.mua.stub.entity.VoiceBanner;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.repository.BaseRepository;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.SignatureUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/3/28
 */
public class RoomRepository extends BaseRepository {

    @Inject
    public RoomRepository(RetrofitApi retrofitApi) {
        super(retrofitApi);
    }



    public LiveData<Resource<List<GiftBean>>> getGifts(String type, String status, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<GiftBean>, BaseApiResult<GiftInfoBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<GiftInfoBean>>> createCall() {
                return mRetrofitApi.getGifts(type, status, signature);
            }

            @Override
            public List<GiftBean> processResponse(BaseApiResult<GiftInfoBean> source) {
                if (source != null && source.getData() != null) {
                    return source.getData().getGift_info();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<List<EmojiBean.EmoticonBean>>> getEmojis(String type, String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<EmojiBean.EmoticonBean>, BaseApiResult<EmojiBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<EmojiBean>>> createCall() {
                return mRetrofitApi.getEmojis(type, signature);
            }

            @Override
            public List<EmojiBean.EmoticonBean> processResponse(BaseApiResult<EmojiBean> source) {
                if (source != null && source.getData() != null) {
                    return source.getData().getEmoticon_list();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<List<VoiceBanner.BannerListBean>>> getBanners() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<VoiceBanner.BannerListBean>, BaseApiResult<VoiceBanner>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<VoiceBanner>>> createCall() {
                return mRetrofitApi.bannerlist("2");
            }

            @Override
            public List<VoiceBanner.BannerListBean> processResponse(BaseApiResult<VoiceBanner> source) {
                if (source != null && source.getData() != null) {
                    return source.getData().getBannerList();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<List<PackBean>>> getMePacks(String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<List<PackBean>, BaseApiResult<List<PackBean>>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<List<PackBean>>>> createCall() {
                return mRetrofitApi.getMePacks(signature);
            }

            @Override
            public List<PackBean> processResponse(BaseApiResult<List<PackBean>> source) {
                if (source != null && source.getData() != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<SmashParamBean>> unitPrice(String signature) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<SmashParamBean, BaseApiResult<SmashParamBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<SmashParamBean>>> createCall() {
                return mRetrofitApi.unitPrice(signature);
            }

            @Override
            public SmashParamBean processResponse(BaseApiResult<SmashParamBean> source) {
                if (source != null && source.getData() != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    /**
     * 发送礼物
     *
     * @return
     */
    public LiveData<Resource<BaseApiResult>> inviteUp(int inviterId, String inviteeId, int roomId, int free, int micro_level, int micro_cost) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.inviteUp(inviterId, inviteeId, roomId, free, micro_level, micro_cost);
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    /**
     * 开播消息
     *
     * @return
     */
    public LiveData<Resource<BaseApiResult>> pushRoomMsg() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.pushRoomMsg();
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }

    /**
     * 获取好友列表
     *
     * @return
     */
    public LiveData<Resource<FriendInfoListBean>> getFriendList() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<FriendInfoListBean, BaseApiResult<FriendInfoListBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<FriendInfoListBean>>> createCall() {
                return mRetrofitApi.getFriendList(SignatureUtils.signByToken());
            }

            @Override
            public FriendInfoListBean processResponse(BaseApiResult<FriendInfoListBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
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

    public LiveData<Resource<WalletBean>> wallet() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<WalletBean, BaseApiResult<WalletBean>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<WalletBean>>> createCall() {
                return mRetrofitApi.wallet();
            }

            @Override
            public WalletBean processResponse(BaseApiResult<WalletBean> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<RecommendRoomResult>> recommendRoom(int page, String roomId) {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<RecommendRoomResult, BaseApiResult<RecommendRoomResult>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<RecommendRoomResult>>> createCall() {
                return mRetrofitApi.recommendRoom(page, roomId);
            }

            @Override
            public RecommendRoomResult processResponse(BaseApiResult<RecommendRoomResult> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
            }
        });
    }

    public LiveData<Resource<FirstRechargeVo>> firstRecharge() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<FirstRechargeVo, BaseApiResult<FirstRechargeVo>>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult<FirstRechargeVo>>> createCall() {
                return mRetrofitApi.firstRecharge();
            }

            @Override
            public FirstRechargeVo processResponse(BaseApiResult<FirstRechargeVo> source) {
                if (source != null) {
                    return source.getData();
                }
                return null;
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
    public LiveData<Resource<BaseApiResult>> shareCallback() {
        return HttpUtils.requestNoCache(new HttpCallback.NoCacheCallback<BaseApiResult, BaseApiResult>() {
            @Override
            public LiveData<ApiResponse<BaseApiResult>> createCall() {
                return mRetrofitApi.shareCallBack();
            }

            @Override
            public BaseApiResult processResponse(BaseApiResult source) {
                return source;
            }
        });
    }
}
