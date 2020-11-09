package com.deepsea.mua.stub.api;

import android.arch.lifecycle.LiveData;

import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.BlockVo;
import com.deepsea.mua.stub.entity.CheckBlackVo;
import com.deepsea.mua.stub.entity.LookGuardUserVo;
import com.deepsea.mua.stub.entity.ApplyFriendListBean;
import com.deepsea.mua.stub.entity.AreaVo;
import com.deepsea.mua.stub.entity.AttenDynamicBean;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.BindWx;
import com.deepsea.mua.stub.entity.BlindDateBean;
import com.deepsea.mua.stub.entity.CashInfo;
import com.deepsea.mua.stub.entity.CashWListBean;
import com.deepsea.mua.stub.entity.ChargeBean;
import com.deepsea.mua.stub.entity.CheckBindWx;
import com.deepsea.mua.stub.entity.DukeListBean;
import com.deepsea.mua.stub.entity.DynamicDetailReplylistBean;
import com.deepsea.mua.stub.entity.DynamicLisistBean;
import com.deepsea.mua.stub.entity.EmojiBean;
import com.deepsea.mua.stub.entity.ExchangeMdDetailListBean;
import com.deepsea.mua.stub.entity.FansRankBean;
import com.deepsea.mua.stub.entity.FirstRechargeVo;
import com.deepsea.mua.stub.entity.FriendInfoListBean;
import com.deepsea.mua.stub.entity.GiftInfoBean;
import com.deepsea.mua.stub.entity.GiftListBean;
import com.deepsea.mua.stub.entity.GuardInfo;
import com.deepsea.mua.stub.entity.GuardInfoBean;
import com.deepsea.mua.stub.entity.GuardListBean;
import com.deepsea.mua.stub.entity.GuardResultBean;
import com.deepsea.mua.stub.entity.GuardTypeBean;
import com.deepsea.mua.stub.entity.HaiPayBean;
import com.deepsea.mua.stub.entity.HeartBeatBean;
import com.deepsea.mua.stub.entity.IncomeListBean;
import com.deepsea.mua.stub.entity.InitCash;
import com.deepsea.mua.stub.entity.IsCreateRoomVo;
import com.deepsea.mua.stub.entity.JumpRoomVo;
import com.deepsea.mua.stub.entity.MDRecord;
import com.deepsea.mua.stub.entity.MaqueeBean;
import com.deepsea.mua.stub.entity.MessageNumVo;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.entity.OnlinesBean;
import com.deepsea.mua.stub.entity.PackBean;
import com.deepsea.mua.stub.entity.PresentWallBean;
import com.deepsea.mua.stub.entity.ProfileBean;
import com.deepsea.mua.stub.entity.ProfileModel;
import com.deepsea.mua.stub.entity.RankListResult;
import com.deepsea.mua.stub.entity.RecommendRoomResult;
import com.deepsea.mua.stub.entity.RenewInitVo;
import com.deepsea.mua.stub.entity.ReportListBean;
import com.deepsea.mua.stub.entity.ReportsBean;
import com.deepsea.mua.stub.entity.RoomDesc;
import com.deepsea.mua.stub.entity.RoomManagers;
import com.deepsea.mua.stub.entity.RoomModeHelpBean;
import com.deepsea.mua.stub.entity.RoomModes;
import com.deepsea.mua.stub.entity.RoomSearchs;
import com.deepsea.mua.stub.entity.RoomTagListBean;
import com.deepsea.mua.stub.entity.RoomTags;
import com.deepsea.mua.stub.entity.RoomsBean;
import com.deepsea.mua.stub.entity.SearchManagers;
import com.deepsea.mua.stub.entity.SmashParamBean;
import com.deepsea.mua.stub.entity.SystemMsgListBean;
import com.deepsea.mua.stub.entity.TagBean;
import com.deepsea.mua.stub.entity.TaskBean;
import com.deepsea.mua.stub.entity.UserBean;
import com.deepsea.mua.stub.entity.VipBean;
import com.deepsea.mua.stub.entity.VoiceBanner;
import com.deepsea.mua.stub.entity.VoiceRoomBean;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.mvp.ResponseModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.deepsea.mua.stub.entity.FaceRequestBean;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by JUN on 2019/3/22
 */
public interface RetrofitApi {

    /**
     * 登录
     *
     * @param username
     * @param vertify
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/login")
    LiveData<ApiResponse<BaseApiResult<UserBean>>> login(
            @Field("username") String username,
            @Field("vertify") String vertify,
            @Field("longitude") double longitude,
            @Field("latitude") double latitude,
            @Field("state") String state,
            @Field("city") String city,
            @Field("locality") String locality,
            @Field("registration_id") String registration_id);

    /**
     * 麻将- 登录
     *
     * @param username
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/login")
    LiveData<ApiResponse<BaseApiResult<UserBean>>> login(
            @Field("username") String username,
            @Field("nickname") String nickname,
            @Field("avatar") String avatar
    );

    /**
     * 麻将- 微信登录
     *
     * @param wx_id
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/wx_login")
    LiveData<ApiResponse<BaseApiResult<UserBean>>> wxLogin(
            @Field("wx_id") String wx_id,
            @Field("registration_id") String registration_id,
            @Field("nickname") String nickname,
            @Field("avatar") String avatar
    );

    /**
     * 一键登录
     *
     * @param login_token token
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/one_login")
    LiveData<ApiResponse<BaseApiResult<UserBean>>> oneLogin(
            @Field("login_token") String login_token,
            @Field("longitude") double longitude,
            @Field("latitude") double latitude,
            @Field("state") String state,
            @Field("city") String city,
            @Field("locality") String locality,
            @Field("registration_id") String registration_id);

    /**
     * 自动登录
     *
     * @return
     */
    @GET("index.php/Api/Member/autologin")
    LiveData<ApiResponse<BaseApiResult<UserBean>>> autologin(@Query("longitude") double longitude,
                                                             @Query("latitude") double latitude,
                                                             @Query("state") String state,
                                                             @Query("city") String city,
                                                             @Query("locality") String locality);

    /**
     * /**
     * 获取验证码
     *
     * @param phone
     * @param type
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Sms/sendSMS")
    LiveData<ApiResponse<BaseApiResult>> sendSMS(
            @Field("phone") String phone,
            @Field("type") String type);

    /**
     * 微信登录注册时验证手机号是否已注册
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/wx_phone_register")
    LiveData<ApiResponse<BaseApiResult>> isWxPhoneRegister(
            @Field("username") String username,
            @Field("vertify") String vertify);

    /**
     * 微信登录接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/wx_login")
    LiveData<ApiResponse<BaseApiResult<UserBean>>> thirdlogin(
            @Field("wx_id") String wx_id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("state") String state,
            @Field("city") String city,
            @Field("locality") String locality,
            @Field("registration_id") String registration_id);

    /**
     * 绑定手机
     *
     * @param openid
     * @param type
     * @param payload
     * @param username
     * @param vertify
     * @return
     */
    @GET("index.php/Api/Member/bindmobile")
    LiveData<ApiResponse<BaseApiResult<UserBean>>> bindmobile(
            @Query("openid") String openid,
            @Query("type") String type,
            @Query("payload") String payload,
            @Query("username") String username,
            @Query("vertify") String vertify);


    /**
     * 已经绑定过三方 是否解除绑定接口
     *
     * @param openid
     * @param type
     * @param payload
     * @param username
     * @return
     */
    @GET("index.php/Api/Member/removebind")
    LiveData<ApiResponse<BaseApiResult<UserBean>>> removebind(
            @Query("openid") String openid,
            @Query("type") String type,
            @Query("payload") String payload,
            @Query("username") String username);

    /**
     * 注册
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/register")
    LiveData<ApiResponse<BaseApiResult<UserBean>>> register(
            @FieldMap HashMap<String, String> map
    );

    /**
     * 获取创建房间标签
     *
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Tags/getTagsList")
    LiveData<ApiResponse<BaseApiResult<RoomTagListBean>>> getTagsList(
            @Field("signature") String signature);

    /**
     * 房间类型帮助
     *
     * @return
     */
    @GET("index.php/Api/Languageroom/room_type_help")
    LiveData<ApiResponse<BaseApiResult<RoomModeHelpBean>>> getRoomTypeHelp();

    /**
     * 获取创建房间标签
     *
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Tags/getTagsList")
    LiveData<ApiResponse<BaseApiResult<RoomTags>>> getOldTagsList(
            @Field("signature") String signature);

    /**
     * 是否可以创建房间
     *
     * @return
     */
    @POST("index.php/Api/Languageroom/iscreateroom")
    LiveData<ApiResponse<BaseApiResult<IsCreateRoomVo>>> iscreateroom();

    /**
     * 创建语音聊天室
     *
     * @return
     */
    @POST("index.php/Api/Languageroom/create")
    LiveData<ApiResponse<BaseApiResult<VoiceRoomBean>>> create();

    /**
     * 房间信息
     *
     * @param room_id
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Languageroom/room_detail")
    LiveData<ApiResponse<BaseApiResult<RoomDesc>>> room_detail(
            @Field("room_id") String room_id,
            @Field("signature") String signature);

    /**
     * 获取举报列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Problem/getList")
    LiveData<ApiResponse<BaseApiResult<ReportsBean>>> getReports(
            @Field("type") String type,
            @Field("signature") String signature);

    /**
     * 举报接口
     *
     * @return
     */
    @GET("index.php/Api/Problem/report_room")
    LiveData<ApiResponse<BaseApiResult>> report_room(
            @Query("report_roomid") String report_roomid,
            @Query("report_content") String report_content);

    /**
     * 举报用户
     *
     * @param to_uid
     * @param contents
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/complain")
    LiveData<ApiResponse<BaseApiResult>> complain(
            @Field("to_uid") String to_uid,
            @Field("contents") String contents,
            @Field("signature") String signature);

    /**
     * 举报用户-麻将
     *
     * @return
     */
    @GET("index.php/Api/Report/addReport")
    LiveData<ApiResponse<BaseApiResult>> reportUser(@QueryMap Map<String, String> map);

    /**
     * 管理员列表接口
     *
     * @param room_id
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/RoomManager/getList")
    LiveData<ApiResponse<BaseApiResult<RoomManagers>>> getManagers(
            @Field("room_id") String room_id,
            @Field("signature") String signature);

    /**
     * 管理员搜索功能
     *
     * @param room_id
     * @param search
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/RoomManager/ManagerListSearch")
    LiveData<ApiResponse<BaseApiResult<SearchManagers>>> searchManagers(
            @Field("room_id") String room_id,
            @Field("search") String search,
            @Field("signature") String signature);

    /**
     * 用户体系个人详情接口
     *
     * @param user_id
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/user_info")
    LiveData<ApiResponse<BaseApiResult<ProfileBean>>> user_info(
            @Field("user_id") String user_id,
            @Field("signature") String signature);


    /**
     * 拉黑用户
     *
     * @param user_id
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/defriend")
    LiveData<ApiResponse<BaseApiResult>> defriend(
            @Field("user_id") String user_id,
            @Field("signature") String signature);

    /**
     * 取消拉黑
     *
     * @param user_id
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/blackout")
    LiveData<ApiResponse<BaseApiResult>> blackout(
            @Field("user_id") String user_id,
            @Field("signature") String signature);

    /**
     * 取消拉黑
     *
     * @param user_id 被查询的用户id
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/isBlack")
    LiveData<ApiResponse<BaseApiResult<CheckBlackVo>>> isBlack(
            @Field("user_id") String user_id);

    /**
     * 微信支付
     *
     * @param rmb
     * @param action
     * @param is_active
     * @param uid
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Order/pay")
    LiveData<ApiResponse<BaseApiResult<WxOrder>>> wxpay(
            @Field("rmb") String rmb,
            @Field("action") String action,
            @Field("type") String type,
            @Field("is_active") String is_active,
            @Field("uid") String uid,
            @Field("chargeid") String chargeid,
            @Field("target_id") String target_id,
            @Field("guard_id") String guard_id,
            @Field("long_day") String long_day);

    /**
     * 海贝支付
     *
     * @param rmb
     * @param action
     * @param is_active
     * @param uid
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Order/pay")
    LiveData<ApiResponse<BaseApiResult<HaiPayBean>>> haipay(
            @Field("rmb") String rmb,
            @Field("action") String action,
            @Field("type") String type,
            @Field("is_active") String is_active,
            @Field("uid") String uid,
            @Field("chargeid") String chargeid,
            @Field("target_id") String target_id,
            @Field("guard_id") String guard_id,
            @Field("long_day") String long_day);

    /**
     * 支付宝支付
     *
     * @param rmb
     * @param action
     * @param type
     * @param is_active
     * @param uid
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Order/pay")
    LiveData<ApiResponse<BaseApiResult<String>>> alipay(
            @Field("rmb") String rmb,
            @Field("action") String action,
            @Field("type") String type,
            @Field("is_active") String is_active,
            @Field("uid") String uid,
            @Field("chargeid") String chargeid,
            @Field("target_id") String target_id,
            @Field("guard_id") String guard_id,
            @Field("long_day") String long_day
    );


    /**
     * 获取WebSocket地址
     *
     * @return
     */
    @GET()
    Call<String> getWsUrl(@Url String url);

    /**
     * 获取房间类型
     *
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/RoomMode/getList")
    LiveData<ApiResponse<BaseApiResult<RoomModes>>> getRoomModes(
            @Field("signature") String signature);

    /**
     * 获取房间列表
     *
     * @param room_type
     * @param page
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Languageroom/getRoomList")
    LiveData<ApiResponse<BaseApiResult<RoomsBean>>> getRoomList(
            @Field("room_type") String room_type,
            @Field("page") int page,
            @Field("signature") String signature,
            @Field("age") String age,
            @Field("city") String city,
            @Field("city_two") String city_two,
            @Field("city_three") String city_three
    );

    /**
     * 最近访问房间
     *
     * @return
     */
    @GET("index.php/Api/Search/visited")
    LiveData<ApiResponse<BaseApiResult<RoomSearchs>>> visited();


    /**
     * 查看更多用户和房间接口
     *
     * @param is_more 1 查看用户 2 查看房间
     * @return
     */
    @GET("index.php/Api/Search/getmoremsg")
    LiveData<ApiResponse<BaseApiResult<RoomSearchs>>> getmoremsg(
            @Query("is_more") String is_more);

    /**
     * 搜索
     *
     * @param search
     * @param type   1 模糊搜索 2 个人搜索3 房间搜索
     * @return
     */
    @GET("index.php/Api/Search/searchmsg")
    LiveData<ApiResponse<BaseApiResult<RoomSearchs>>> roomSearch(
            @Query("search") String search,
            @Query("type") String type);


    /**
     * 礼物列表接口
     *
     * @param type
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Gift/getList")
    LiveData<ApiResponse<BaseApiResult<GiftInfoBean>>> getGifts(
            @Field("type") String type,
            @Field("status") String status,
            @Field("signature") String signature);

    /**
     * 礼物列表接口(包含已下架礼物)
     *
     * @param type
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Resource/Allgift")
    Observable<ResponseModel<GiftInfoBean>> getAllGifts(
            @Field("type") String type,
            @Field("signature") String signature);

    /**
     * 表情
     *
     * @param type
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Emoticon/getList")
    LiveData<ApiResponse<BaseApiResult<EmojiBean>>> getEmojis(
            @Field("type") String type,
            @Field("signature") String signature);


    /**
     * 提现记录
     *
     * @param size
     * @param page
     * @return
     */
    @GET("index.php/Api/AliPay/cashlist")
    LiveData<ApiResponse<BaseApiResult<CashWListBean>>> getCashList(
            @Query("page") int page,
            @Query("size") int size,
            @Query("stime") String stime,
            @Query("etime") String etime);

    /**
     * 提现记录
     *
     * @param size
     * @param page
     * @return
     */
    @GET("index.php/Api/AliPay/redpacket_cashlist")
    LiveData<ApiResponse<BaseApiResult<CashWListBean>>> getRedpackageCashList(
            @Query("page") int page,
            @Query("size") int size,
            @Query("stime") String stime,
            @Query("etime") String etime);

    @GET("index.php/Api/AliPay/incomelist")
    LiveData<ApiResponse<BaseApiResult<IncomeListBean>>> getIncomeList(
            @Query("page") int page,
            @Query("size") int size,
            @Query("stime") String stime,
            @Query("etime") String etime);

    @GET("index.php/Api/AliPay/redpacket_incomelist")
    LiveData<ApiResponse<BaseApiResult<IncomeListBean>>> getRedPackageIncomeList(
            @Query("page") int page,
            @Query("size") int size,
            @Query("stime") String stime,
            @Query("etime") String etime);

    /**
     * 兑换记录
     *
     * @param page
     * @param size
     * @param stime
     * @param etime
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/AliPay/exchangeInfo")
    LiveData<ApiResponse<BaseApiResult<ExchangeMdDetailListBean>>> getExchangeDetailsList(
            @Field("page") int page,
            @Field("size") int size,
            @Field("stime") String stime,
            @Field("etime") String etime);

    /**
     * 红包兑换记录
     *
     * @param page
     * @param size
     * @param stime
     * @param etime
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/AliPay/redpacket_exchangeInfo")
    LiveData<ApiResponse<BaseApiResult<ExchangeMdDetailListBean>>> getRedpacketExchangeDetailsList(
            @Field("page") int page,
            @Field("size") int size,
            @Field("stime") String stime,
            @Field("etime") String etime);


    /**
     * 我的钱包
     *
     * @return
     */
    @POST("index.php/Api/AliPay/initmymoney")
    LiveData<ApiResponse<BaseApiResult<WalletBean>>> wallet();

    /**
     * 初始化提现接口
     *
     * @return
     */
    @POST("index.php/Api/AliPay/initcash")
    LiveData<ApiResponse<BaseApiResult<InitCash>>> initCash();


    /**
     * 余额兑换玫瑰
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/AliPay/exchangeRose")
    LiveData<ApiResponse<BaseApiResult>> exchangeRose(@Field("coin") int coin);


    /**
     * 提现页面接口
     *
     * @return
     */
    @POST("index.php/Api/AliPay/cashinfo")
    LiveData<ApiResponse<BaseApiResult<CashInfo>>> cashinfo();

    /**
     * 红包提现页面接口
     *
     * @return
     */
    @POST("index.php/Api/AliPay/redpacket_cashinfo")
    LiveData<ApiResponse<BaseApiResult<CashInfo>>> redpacketCashinfo();


    /**
     * 充值列表接口
     *
     * @return
     */
    @GET("index.php/Api/Order/chargelist")
    LiveData<ApiResponse<BaseApiResult<ChargeBean>>> chargelist();


    /**
     * m豆明细
     *
     * @param page
     * @return
     */
    @GET("index.php/Api/order/mbdetails")
    LiveData<ApiResponse<BaseApiResult<MDRecord>>> mbdetails(
            @Query("page") int page);

    /**
     * 钻石明细
     *
     * @param page
     * @return
     */
    @GET("index.php/Api/order/diamondtails")
    LiveData<ApiResponse<BaseApiResult<MDRecord>>> diamondtails(
            @Query("page") int page);

    /**
     * 初始化兑换接口
     *
     * @return
     */
    @POST("index.php/Api/AliPay/initExchange")
    LiveData<ApiResponse<BaseApiResult<WalletBean>>> initExchange();

    /**
     * 初始化兑换接口
     *
     * @return
     */
    @POST("index.php/Api/AliPay/redpacket_initExchange")
    LiveData<ApiResponse<BaseApiResult<WalletBean>>> redpacketInitExchange();


    /**
     * 兑换M豆
     *
     * @param coin
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/AliPay/exchangeRose")
    LiveData<ApiResponse<BaseApiResult>> mdExchange(
            @Field("coin") String coin);

    /**
     * 兑换M豆
     *
     * @param coin
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/AliPay/redpacket_exchangeRose")
    LiveData<ApiResponse<BaseApiResult>> redpacketExchangeRose(
            @Field("coin") String coin);

    /**
     * 退出登录
     *
     * @return
     */
    @GET("index.php/Api/Member/logout")
    LiveData<ApiResponse<BaseApiResult>> logout();


    /**
     * 意见反馈
     *
     * @param content
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Feedback/setFeedback")
    LiveData<ApiResponse<BaseApiResult>> setFeedback(
            @Field("content") String content,
            @Field("signature") String signature);

    /**
     * 更新主播粉丝开播提醒按钮
     *
     * @param type
     * @return
     */
    @GET("index.php/Api/Member/fansmenusatatus")
    LiveData<ApiResponse<BaseApiResult>> fansmenusatatus(
            @Query("type") String type);


    /**
     * 上传头像
     *
     * @param body
     * @return
     */
    @POST("index.php/Api/Member/avatar")
    LiveData<ApiResponse<BaseApiResult>> uploadAvatar(
            @Body RequestBody body);

    /**
     * 获取阿里云配置
     *
     * @param
     * @return
     */
    @GET("index.php/api/alists/getststoken")
    LiveData<ApiResponse<BaseApiResult<OSSConfigBean>>> getOssConfigHeadiv();

    /**
     * 上传头像阿里云验证
     *
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/api/user/setavatar")
    LiveData<ApiResponse<BaseApiResult>> setavatar(@Field("avatar") String avatar);

    /**
     * 修改用户信息
     *
     * @param profile
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/edit")
    LiveData<ApiResponse<BaseApiResult>> updateUserInfo(
            @Field("profile") String profile,
            @Field("signature") String signature);

    /**
     * 是否绑定微信
     *
     * @return
     */
    @POST("index.php/Api/Member/is_band_wx")
    LiveData<ApiResponse<BaseApiResult<CheckBindWx>>> isBindWx();

    /**
     * 绑定微信
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/member/bind_phone")
    LiveData<ApiResponse<BaseApiResult>> bindPhone(@Field("phone") String phone, @Field("vertify") String vertify);

    /**
     * 绑定微信
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/band_wx")
    LiveData<ApiResponse<BaseApiResult<BindWx>>> bindWx(@Field("wx_id") String wx_id);

    /**
     * 解绑微信
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/out_set_band")
    LiveData<ApiResponse<BaseApiResult>> unBindWx(@Field("wx_id") String wx_id);

    /**
     * 爵位列表
     *
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Duke/getList")
    LiveData<ApiResponse<BaseApiResult<DukeListBean>>> dukeList(
            @Field("signature") String signature);

    /**
     * 守护列表
     *
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Guard/getList")
    LiveData<ApiResponse<BaseApiResult<GuardListBean>>> guardList(
            @Field("signature") String signature);

    /**
     * vip
     *
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Vip/getList")
    LiveData<ApiResponse<BaseApiResult<VipBean>>> getVip(
            @Field("signature") String signature);


    /**
     * 会员中心守护详细信息
     *
     * @param user_id
     * @param room_id
     * @param countdown_day
     * @param guard_level
     * @param gold_marks
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Guard/guard_info")
    LiveData<ApiResponse<BaseApiResult<GuardInfo>>> guard_info(
            @Field("user_id") String user_id,
            @Field("room_id") String room_id,
            @Field("countdown_day") String countdown_day,
            @Field("guard_level") String guard_level,
            @Field("gold_marks") String gold_marks,
            @Field("signature") String signature);

    /**
     * 会员守护个人或房间续费列表接口
     *
     * @param user_id
     * @param room_id
     * @param countdown_day
     * @param guard_level
     * @param type
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Guard/guard_change")
    LiveData<ApiResponse<BaseApiResult<GuardTypeBean>>> guard_change(
            @Field("user_id") String user_id,
            @Field("room_id") String room_id,
            @Field("countdown_day") String countdown_day,
            @Field("guard_level") String guard_level,
            @Field("type") String type,
            @Field("signature") String signature);

    /**
     * 首页轮播接口列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Languageroom/bannerlist")
    LiveData<ApiResponse<BaseApiResult<VoiceBanner>>> bannerlist(
            @Field("type") String type);

    /**
     * 充值权限
     *
     * @return
     */
    @POST("index.php/Api/Monitoring/checkSatus")
    LiveData<ApiResponse<BaseApiResult>> checkSatus();

    /**
     * 绑定支付宝接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/AliPay/bindaplipay")
    LiveData<ApiResponse<BaseApiResult>> bindAlipay(@Field("apliuserid") String apliuserid, @Field("type") String type, @Field("justpic") String justpic, @Field("backpic") String backpic);

    /**
     * 提现申请接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/AliPay/cash")
    LiveData<ApiResponse<BaseApiResult>> cash(@Field("apliuserid") String apliuserid, @Field("cash") String cash, @Field("totalcash") String totalcash, @Field("type") String type);

    /**
     * 红包提现申请接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/AliPay/redpacket_cash")
    LiveData<ApiResponse<BaseApiResult>> redpacketCash(@Field("apliuserid") String apliuserid, @Field("cash") String cash, @Field("totalcash") String totalcash, @Field("type") String type);

    /**
     * 实人认证发起请求操作
     *
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Approvice/getVerifyToken")
    LiveData<ApiResponse<BaseApiResult<AuditBean>>> getVerifyToken(
            @Field("signature") String signature);

    /**
     * 获取认证资料接口
     *
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Approvice/createapprove")
    LiveData<ApiResponse<BaseApiResult>> createapprove(
            @Field("signature") String signature);

    /**
     * 推荐码绑定
     *
     * @param referrer_code 推荐码
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/bindReferrer")
    LiveData<ApiResponse<BaseApiResult>> bindReferrer(
            @Field("referrer_code") String referrer_code);

    /**
     * 我的背包列表
     *
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/Mepack_list")
    LiveData<ApiResponse<BaseApiResult<List<PackBean>>>> getMePacks(
            @Field("signature") String signature);

    /**
     * 获取砸蛋信息
     *
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/Unit_price")
    LiveData<ApiResponse<BaseApiResult<SmashParamBean>>> unitPrice(
            @Field("signature") String signature);

    /**
     * 嘉宾累计相亲时长
     *
     * @return
     */
    @POST("index.php/Api/Member/blindDate")
    LiveData<ApiResponse<BaseApiResult<BlindDateBean>>> blindDate();

    /**
     * 个性化标签
     *
     * @return
     */
    @POST("index.php/Api/Member/hobby_feature")
    LiveData<ApiResponse<BaseApiResult<TagBean>>> hobby_feature();

    /**
     * 个性化标签修改接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/hobby_feature_edit")
    LiveData<ApiResponse<BaseApiResult>> hobby_feature_edit(@Field("hobby_id") String hobby_id, @Field("feature_id") String feature_id);


    /**
     * 举报内容选项
     *
     * @return
     */
    @GET("index.php/api/report/option")
    LiveData<ApiResponse<BaseApiResult<List<ReportListBean>>>> getReportList();

    /**
     * 举报帖子
     *
     * @param forum_id         举报帖子id
     * @param report_content   举报内容
     * @param report_option_id 举报选项id
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/api/report/report")
    Observable<ResponseModel<AttenDynamicBean>> toReportDynamic(@Field("forum_id") String forum_id,
                                                                @Field("report_content") String report_content,
                                                                @Field("report_option_id") String report_option_id,
                                                                @Field("repely_id") String repely_id);


    /**
     * 自己删帖
     *
     * @param forum_id 帖子id
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/api/forum/delforum")
    Observable<ResponseModel<AttenDynamicBean>> toDelMyDynamic(@Field("forum_id") String forum_id);

    /**
     * 帖子列表（个人）
     *
     * @param page 当前页数
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/api/forum/selfforumlist")
    Observable<ResponseModel<DynamicLisistBean>> getMyDynamicList(@Field("page") String page, @Field("pagenum") String pagenum);


    /**
     * 帖子列表（全部）
     *
     * @param page 当前页数
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/api/forum/forumlist")
    Observable<ResponseModel<DynamicLisistBean>> getDynamicList(@Field("page") String page, @Field("pagenum") String pagenum);

    /**
     * 帖子详情
     *
     * @param forum_id
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/api/forum/detail")
    Observable<ResponseModel<DynamicLisistBean.ListEntity>> getDynamicDetail(@Field("forum_id") String forum_id);

    /**
     * 评论回复
     *
     * @param forum_id    帖子id
     * @param type        回复类型1回帖，2评论
     * @param content     回复内容
     * @param reply_atuid @uid
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/api/forum/addreply")
    Observable<ResponseModel<AttenDynamicBean>> getDynamicAddReply(
            @Field("forum_id") String forum_id,
            @Field("type") String type,
            @Field("content") String content,
            @Field("reply_atuid") String reply_atuid,
            @Field("reply_id") String reply_id);


    /**
     * 帖子详情回复评论列表
     *
     * @param forum_id 帖子id
     * @param page     当前页数
     * @param pagenum  每页个数
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/api/forum/replylist")
    Observable<ResponseModel<DynamicDetailReplylistBean>> getDynamicReplyList(
            @Field("forum_id") String forum_id,
            @Field("page") String page,
            @Field("pagenum") String pagenum);


    @GET("index.php/api/alists/getststoken")
    Observable<ResponseModel<OSSConfigBean>> getOssConfig();

    /**
     * 关注、取消关注
     *
     * @param userided 被关注用户id
     * @param type     1关注2取消关注
     * @return
     */
    @GET("index.php/Api/Attention/attention_member")
    Observable<ResponseModel<String>> toAttention(@Query("userided") String userided, @Query("type") String type);


    /**
     * 拉黑用户
     *
     * @param user_id
     * @param signature
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/defriend")
    Observable<ResponseModel<String>> toDefriend(
            @Field("user_id") String user_id,
            @Field("signature") String signature);


    /**
     * 礼物墙列表
     *
     * @param
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/api/gift/giftranklist")
    Observable<ResponseModel<List<PresentWallBean>>> getPresenList(
            @Field("touid") String touid
    );

    /**
     * 实名认证
     *
     * @param
     * @param
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/api/user/realuser")
    Observable<ResponseModel<String>> realuser(@Field("name") String name, @Field("idcard") String idcard);


    /**
     * 修改性别
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Languageroom/setSex")
    Observable<ResponseModel<BaseApiResult>> sexEdit(@Field("sex") String sex);

    /**
     * 用户在线状态更新
     *
     * @return
     */
    @POST("index.php/Api/MemberOnline/addMember")
    Observable<ResponseModel<HeartBeatBean>> addMember();

    /**
     * 用户在线状态更新
     *
     * @return
     */
    @POST("index.php/Api/userinfo/pushFriendMsg")
    Observable<ResponseModel<BaseApiResult>> pushFriendMsg();

    /**
     * 附近的人
     *
     * @param page
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/MemberOnline/OnlineList")
    LiveData<ApiResponse<BaseApiResult<OnlinesBean>>> OnlineList(@Field("page") int page);

    /**
     * 附近的人---new
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/MemberOnline/OnlineList")
    LiveData<ApiResponse<BaseApiResult<OnlinesBean>>> OnlineList(@FieldMap Map<String, String> map);

    /**
     * 附近的人---new
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/MemberOnline/findOnlineList")
    LiveData<ApiResponse<BaseApiResult<OnlinesBean>>> findOnlineList(@FieldMap Map<String, String> map);

    /**
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/MemberOnline/rankingList")
    LiveData<ApiResponse<BaseApiResult<RankListResult>>> rankingList(@FieldMap Map<String, String> map);

    /**
     * 获取用户编辑信息
     *
     * @return
     */
    @POST("index.php/Api/Member/getMenusList")
    LiveData<ApiResponse<BaseApiResult<ProfileModel>>> getMenusList();

    /**
     * 获取征友条件
     *
     * @return
     */
    @POST("index.php/Api/Member/getConditionList")
    LiveData<ApiResponse<BaseApiResult<LinkedHashMap<String, ProfileModel.MenuBean>>>> getConditionList();

    /**
     * 好友申请列表
     *
     * @param signature
     * @return
     */
    @GET("index.php/Api/Friend/applyFriendList")
    LiveData<ApiResponse<BaseApiResult<ApplyFriendListBean>>> getApplyFriendList(@Query("signature") String signature, @Query("page") int page);

    /**
     * 好友申请列表
     *
     * @param signature
     * @return
     */
    @GET("index.php/Api/Friend/myFriendList")
    LiveData<ApiResponse<BaseApiResult<ApplyFriendListBean>>> getMyApplyList(@Query("signature") String signature, @Query("page") int page);

    /**
     * 同意好友请求
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Friend/passFriendly")
    LiveData<ApiResponse<BaseApiResult>> passFriendly(
            @FieldMap Map<String, String> map);

    /**
     * 同意好友请求--已是好友
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Friend/passFriendly")
    LiveData<ApiResponse<BaseApiResult>> agreeFriendly(
            @FieldMap Map<String, String> map);

    /**
     * 拒绝好友请求
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Friend/delFriendly")
    LiveData<ApiResponse<BaseApiResult>> delFriendly(
            @FieldMap Map<String, String> map);

    /**
     * 加好友
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Friend/addFriendly")
    LiveData<ApiResponse<BaseApiResult>> addFriendly(
            @FieldMap Map<String, String> map);

    /**
     * 好友列表
     *
     * @return
     */
    @GET("index.php/Api/Friend/getFriendList")
    LiveData<ApiResponse<BaseApiResult<FriendInfoListBean>>> getFriendList(
            @Query("signature") String signature);

    /**
     * 系统消息
     *
     * @return
     */
    @GET("index.php/Api/Pushmessage/systemList")
    LiveData<ApiResponse<BaseApiResult<SystemMsgListBean>>> getSystemMsgList(
            @Query("signature") String signature,
            @Query("page") int page
    );

    /**
     * 系统消息删除
     *
     * @param id
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Pushmessage/systemDel")
    LiveData<ApiResponse<BaseApiResult>> systemDel(@Field("id") String id);

    /**
     * 消息未读数量
     *
     * @return
     */
    @GET("index.php/Api/Friend/messageNum")
    LiveData<ApiResponse<BaseApiResult<MessageNumVo>>> getMessageNum();

    /**
     * 跑马灯
     *
     * @return
     */
    @GET("index.php/Api/Feedback/horseLamp")
    Observable<ResponseModel<MaqueeBean>> horseLamp();

    /**
     * 支付宝支付
     *
     * @param rmb
     * @param action
     * @param type
     * @param is_active
     * @param uid
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Order/pay")
    Observable<ResponseModel<String>> js_alipay(
            @Field("rmb") String rmb,
            @Field("action") String action,
            @Field("type") String type,
            @Field("is_active") String is_active,
            @Field("uid") String uid,
            @Field("chargeid") String chargeid,
            @Field("target_id") String target_id,
            @Field("guard_id") String guard_id,
            @Field("long_day") String long_day
    );

    @FormUrlEncoded
    @POST("index.php/Api/Order/pay")
    Observable<ResponseModel<WxOrder>> js_wxpay(
            @Field("rmb") String rmb,
            @Field("action") String action,
            @Field("type") String type,
            @Field("is_active") String is_active,
            @Field("uid") String uid,
            @Field("chargeid") String chargeid,
            @Field("target_id") String target_id,
            @Field("guard_id") String guard_id,
            @Field("long_day") String long_day);

    /**
     * 礼物列表
     *
     * @return
     */
    @GET("index.php/Api/Friend/friendGiftList")
    LiveData<ApiResponse<BaseApiResult<GiftListBean>>> getFriendGiftList(
            @Query("signature") String signature, @Query("is_room") String is_room);

    /**
     * 发送礼物
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Friend/sendGift")
    LiveData<ApiResponse<BaseApiResult>> sendGift(@FieldMap Map<String, String> map);

    /**
     * 邀请上麦
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Keng/inviteUp")
    LiveData<ApiResponse<BaseApiResult>> inviteUp(
            @Field("inviterId") int inviterId,
            @Field("inviteeId") String inviteeId,
            @Field("roomId") int roomId,
            @Field("free") int free,
            @Field("micro_level") int micro_level,
            @Field("micro_cost") int micro_cost);

    @POST("index.php/Api/Languageroom/pushRoomMsg")
    LiveData<ApiResponse<BaseApiResult>> pushRoomMsg();

    /**
     * 邀请同意拒绝接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Keng/inviteHandle")
    LiveData<ApiResponse<BaseApiResult>> inviteHandle(
            @Field("inviterId") int inviterId,
            @Field("inviteeId") int inviteeId,
            @Field("roomId") int roomId,
            @Field("status") int status,
            @Field("id") int id);

    /**
     * /**
     * 随机跳转前六直播间接口
     *
     * @return
     */
    @POST("index.php/Api/Languageroom/jump_room")
    LiveData<ApiResponse<BaseApiResult<JumpRoomVo>>> jumpRoom();

    /**
     * 申请主持接口
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Languageroom/apply_info")
    LiveData<ApiResponse<BaseApiResult>> applyInfo(@Field("wx_name") String wx_name, @Field("age") String age);


    /**
     * 获取省市县接口
     *
     * @return
     */
    @GET("index.php/Api/Member/getArea")
    LiveData<ApiResponse<BaseApiResult<List<AreaVo>>>> getArea(@Query("level") int level, @Query("pcode") int pcode);

    @GET("index.php/Api/Member/getArea")
    Observable<ResponseModel<List<AreaVo>>> getAreaInProfile(@Query("level") int level, @Query("pcode") int pcode);

    /**
     * 房间内推荐列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Languageroom/recommend_room")
    LiveData<ApiResponse<BaseApiResult<RecommendRoomResult>>> recommendRoom(@Field("page") int page, @Field("now_roomid") String now_roomid);

    /**
     * 房间内首充福利弹框接口
     *
     * @return
     */
    @POST("index.php/Api/Order/room_charge")
    LiveData<ApiResponse<BaseApiResult<FirstRechargeVo>>> firstRecharge();

    /**
     * 守护榜
     *
     * @param page 当前页数
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/api/MemberOnline/rankingList_shouhu")
    LiveData<ApiResponse<BaseApiResult<GuardResultBean>>> getGuardList(@Field("userid") String userid, @Field("page") int page, @Field("pagenum") int pagenum);

    /**
     * 任务中心页接口
     *
     * @return
     */
    @POST("index.php/Api/Member/task_list")
    LiveData<ApiResponse<BaseApiResult<TaskBean>>> taskList();

    /**
     * 领取任务
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Member/task_reward_receive")
    LiveData<ApiResponse<BaseApiResult>> taskReceive(@Field("type") String type, @Field("task_id") String task_id);


    /**
     * 我的守护/守护我的
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Guard/getList")
    Observable<ResponseModel<GuardInfoBean>> guardGuardListForId(@Field("type") String type,
                                                                 @Field("page") int page);

    /**
     * 我的守护/守护我的
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Guard/initGuard")
    LiveData<ApiResponse<BaseApiResult<RenewInitVo>>> initGuard(@Field("target_id") String target_id);

    /**
     * 我的守护/守护我的
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Guard/getList")
    LiveData<ApiResponse<BaseApiResult<GuardInfoBean>>> guardInfoList(@Field("type") String type,
                                                                      @Field("page") int page);

    /**
     * 查看用户守护榜
     *
     * @return
     */
    @FormUrlEncoded
    @POST("index.php/Api/Guard/getUserList")
    Observable<ResponseModel<LookGuardUserVo>> getGuardUserList(@Field("userId") String userId, @Field("page") int page);

    @FormUrlEncoded
    @POST("index.php/Api/Guard/getUserList")
    LiveData<ApiResponse<BaseApiResult<LookGuardUserVo>>> getGuardUserListForLive(@Field("userId") String userId, @Field("page") int page);

    /**
     * 黑名单
     *
     * @return
     */
    @POST("index.php/Api/Member/listBlack")
    LiveData<ApiResponse<BaseApiResult<List<BlockVo>>>> getBlockList();

    /**
     * 分享回调
     *
     * @return
     */
    @POST("index.php/Api/Member/share")
    LiveData<ApiResponse<BaseApiResult>> shareCallBack();

    /**
     * 美艳参数
     *
     * @return
     */
    @GET("index.php/Api/Member/getMakeFace")
    LiveData<ApiResponse<BaseApiResult<FaceRequestBean>>> getMakeFace();
}
