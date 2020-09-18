package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;
import android.util.Log;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.AuthenticationBean;
import com.deepsea.mua.stub.entity.EmojiBean;
import com.deepsea.mua.stub.entity.FirstRechargeVo;
import com.deepsea.mua.stub.entity.FriendInfoListBean;
import com.deepsea.mua.stub.entity.GetAwardTaskId;
import com.deepsea.mua.stub.entity.GiftBean;
import com.deepsea.mua.stub.entity.InitCash;
import com.deepsea.mua.stub.entity.InviteOnMicroBean;
import com.deepsea.mua.stub.entity.InviteOnmicroData;
import com.deepsea.mua.stub.entity.PackBean;
import com.deepsea.mua.stub.entity.RecommendRoomResult;
import com.deepsea.mua.stub.entity.RenewInitVo;
import com.deepsea.mua.stub.entity.SendGetAwardTaskIdBean;
import com.deepsea.mua.stub.entity.SmashParamBean;
import com.deepsea.mua.stub.entity.VoiceBanner;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.entity.socket.MultiSend;
import com.deepsea.mua.stub.entity.socket.SingleSend;
import com.deepsea.mua.stub.entity.socket.send.GiveBlueRoseParam;
import com.deepsea.mua.stub.model.RoomModel;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.voice.repository.RoomRepository;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/3/26
 */
public class RoomViewModel extends ViewModel {

    private static final String TAG = "RoomViewModel";

    private final RoomRepository mRepository;
    public MediatorLiveData<Integer> mWsLiveData = new MediatorLiveData<>();

    private RoomModel mRoomModel;

    @Inject
    public RoomViewModel(RoomRepository repository) {
        mRepository = repository;
    }

    public void setRoomModel(RoomModel roomModel) {
        this.mRoomModel = roomModel;
    }

    public LiveData<Resource<BaseApiResult>> attention_member(String uid, String type) {
        return mRepository.attention_member(uid, type);
    }

    public LiveData<Resource<List<GiftBean>>> getGifts(String status) {
        String type = "1";
        return mRepository.getGifts(type, status, SignatureUtils.signWith(type));
    }

    public LiveData<Resource<List<GiftBean>>> getGuardGifts(String type) {
        String status = "1";
        return mRepository.getGifts(type, status, SignatureUtils.signWith(type));
    }

    public LiveData<Resource<List<EmojiBean.EmoticonBean>>> getEmojis() {
        String type = "1";
        return mRepository.getEmojis(type, SignatureUtils.signWith(type));
    }

    public LiveData<Resource<List<VoiceBanner.BannerListBean>>> getBanners() {
        return mRepository.getBanners();
    }

    public LiveData<Resource<List<PackBean>>> getMePacks() {
        return mRepository.getMePacks(SignatureUtils.signByToken());
    }

    public LiveData<SmashParamBean> unitPrice() {
        MediatorLiveData<SmashParamBean> liveData = new MediatorLiveData<>();
        liveData.addSource(mRepository.unitPrice(SignatureUtils.signByToken()), new BaseObserver<SmashParamBean>() {
            @Override
            public void onSuccess(SmashParamBean result) {
                liveData.setValue(result);
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                liveData.setValue(null);
            }
        });
        return liveData;
    }

    /**
     * 是否在主持麦上
     *
     * @param uid
     * @return
     */
    public boolean isOnHostMicro(String uid) {
        if (mRoomModel != null && mRoomModel.getHostMicro() != null && mRoomModel.getHostMicro().getUser() != null) {
            return TextUtils.equals(mRoomModel.getHostMicro().getUser().getUserId(), uid);
        }
        return false;
    }

    private void postMsgId(int msgId) {
        mWsLiveData.postValue(msgId);
    }

    private boolean sendMessage(String message) {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(message).getAsJsonObject();
        int msgId = object.get("MsgId").getAsInt();
        postMsgId(msgId);
        return RoomController.getInstance().sendRoomMsg(message);
    }

    private boolean sendMessageNoLoading(String message) {
        return RoomController.getInstance().sendRoomMsg(message);
    }

    /**
     * 退出房间
     */
    public void leaveRoom() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 3);
        sendMessageNoLoading(jsonObject.toString());
    }

    public int level = -1,
            number;

    /**
     * 麦位花费
     *
     * @param level
     * @param number
     */
    public void microCost(int level, int number) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 74);
        sendMessage(jsonObject.toString());
        this.level = level;
        this.number = number;
    }

    /**
     * 上麦
     */
    public void upMicro(int level, int number, boolean force) {
        if (level != -1) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("MsgId", 6);
            jsonObject.addProperty("Level", level);
            jsonObject.addProperty("Number", number);
            jsonObject.addProperty("Force", force);
            Log.d("upMicro", JsonConverter.toJson(jsonObject.toString()));
            sendMessage(jsonObject.toString());
        }
        this.level = this.number = -1;
    }

    /**
     * 邀请上老板麦
     *
     * @param aTargetUserId 小于或等于0为自己上麦，否则为将指定ID的用户抱上麦
     */
    public void toOnWheat(String aTargetUserId, boolean force) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 6);
        jsonObject.addProperty("TargetUserId", aTargetUserId);
        jsonObject.addProperty("Force", force);
        sendMessage(jsonObject.toString());
    }

    /**
     * socket 60s心跳
     */
    public void toSocketHeartBeat() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 300);
        sendMessage(jsonObject.toString());
        Log.d("heart", jsonObject.toString());
    }

    /**
     * 下麦
     *
     * @param level
     * @param number
     */
    public void downMicro(int level, int number) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 8);
        jsonObject.addProperty("Level", level);
        jsonObject.addProperty("Number", number);
        sendMessage(jsonObject.toString());
    }

    /**
     * 踢出房间
     *
     * @param uid
     */
    public void removeRoom(String uid) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 5);
        jsonObject.addProperty("UserId", uid);
        sendMessage(jsonObject.toString());
    }

    /**
     * 清空心动值
     *
     * @param level
     * @param number
     */
    public void cleanXd(int level, int number) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 36);
        jsonObject.addProperty("Level", level);
        jsonObject.addProperty("Number", number);
        sendMessage(jsonObject.toString());
    }

    /**
     * 获取用户信息
     *
     * @param uid
     */
    public void getUserInfo(String uid) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 46);
        jsonObject.addProperty("UserId", uid);
        sendMessage(jsonObject.toString());
    }

    /**
     * 禁言---不能打字聊天
     *
     * @param isDisableMsg true为禁言，false为取消禁言
     * @param id           用户id
     * @param disableMsgId 消息表id
     */
    public void forbidChat(boolean isDisableMsg, String id, int disableMsgId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 46);
        jsonObject.addProperty("IsDisableMsg", isDisableMsg);
        jsonObject.addProperty("Id", id);
        if (disableMsgId != 0) {
            jsonObject.addProperty("DisableMsgId ", disableMsgId);
        }
        sendMessage(jsonObject.toString());
    }

    /**
     * 禁言/取消禁言
     *
     * @param uid
     * @param isForbidden
     */
    public void forbiddenMsg(String uid, boolean isForbidden) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 28);
        jsonObject.addProperty("IsDisableMsg", isForbidden);
        jsonObject.addProperty("Id", uid);
        sendMessage(jsonObject.toString());
    }

    /**
     * 禁麦
     *
     * @param level
     * @param number
     */
    public void forbidMicro(int level, int number) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 10);
        jsonObject.addProperty("Level", level);
        jsonObject.addProperty("Number", number);
        sendMessage(jsonObject.toString());
    }

    /**
     * 取消禁麦
     *
     * @param level
     * @param number
     */
    public void unforbidMicro(int level, int number) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 11);
        jsonObject.addProperty("Level", level);
        jsonObject.addProperty("Number", number);
        sendMessage(jsonObject.toString());
    }

    /**
     * 倒计时
     *
     * @param level
     * @param number
     * @param duration
     */
    public void countDown(int level, int number, int duration) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 38);
        jsonObject.addProperty("Level", level);
        jsonObject.addProperty("Number", number);
        jsonObject.addProperty("Duration", duration);
        sendMessage(jsonObject.toString());
    }

    /**
     * 锁麦
     *
     * @param level
     * @param number
     */
    public void lockMicro(int level, int number) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 14);
        jsonObject.addProperty("Level", level);
        jsonObject.addProperty("Number", number);
        sendMessage(jsonObject.toString());
    }

    /**
     * 取消锁麦
     *
     * @param level
     * @param number
     */
    public void unlockMicro(int level, int number) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 15);
        jsonObject.addProperty("Level", level);
        jsonObject.addProperty("Number", number);
        sendMessage(jsonObject.toString());
    }

    /**
     * 开启心动值
     *
     * @param isOpen
     */
    public void openHeart(boolean isOpen) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 26);
        jsonObject.addProperty("IsHeadBeat", isOpen);
        sendMessage(jsonObject.toString());
    }

    /**
     * 开启自由麦
     *
     * @param isOpen
     */
    public void openFree(boolean isOpen) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 24);
        jsonObject.addProperty("IsFreeMicro", isOpen);
        sendMessage(jsonObject.toString());
    }

    /**
     * 锁定/解锁房间
     *
     * @param isLock
     */
    public void lockRoom(boolean isLock) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 33);
        jsonObject.addProperty("IsLock", isLock);
        sendMessage(jsonObject.toString());
    }

    /**
     * 取消排麦
     */
    public void cancelSort() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 53);
        sendMessage(jsonObject.toString());
    }

    /**
     * 麦序置顶
     *
     * @param uid
     */
    public void topMicro(String uid) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 22);
        jsonObject.addProperty("Id", uid);
        sendMessage(jsonObject.toString());
    }

    /**
     * 拒绝上麦
     *
     * @param uid
     */
    public void refuseUpMicro(String uid) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 23);
        jsonObject.addProperty("Id", uid);
        sendMessage(jsonObject.toString());
    }

    /**
     * 赠送礼物
     *
     * @param sendModel
     */
    public void multiSend(MultiSend sendModel) {
        sendModel.setMsgId(29);
        sendMessage(JsonConverter.toJson(sendModel));
    }

    /**
     * 打赏用户
     *
     * @param singleSend
     */
    public void singleSend(SingleSend singleSend) {
        singleSend.setMsgId(58);
        sendMessage(JsonConverter.toJson(singleSend));
    }

    /**
     * 在线用户
     *
     * @param pageNumber
     */
    public void onlineUser(int pageNumber, int Sex) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 57);
        jsonObject.addProperty("Index", pageNumber);
        jsonObject.addProperty("Sex", Sex);
        sendMessage(jsonObject.toString());
    }

    /**
     * 修改房间标签
     *
     * @param roomTagId
     */
    public void saveRoomTag(String roomTagId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 43);
        jsonObject.addProperty("TagId", roomTagId);
        sendMessage(jsonObject.toString());
    }

    /**
     * 发送表情
     *
     * @param id
     */
    public void sendEmotion(String id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 61);
        jsonObject.addProperty("EmoticonId", id);
        sendMessage(jsonObject.toString());
    }

    /**
     * 购买锤子数量
     *
     * @param count
     */
    public boolean buyHammer(String count, int buyMode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 65);
        jsonObject.addProperty("Count", count);
        jsonObject.addProperty("BuyMode", buyMode);

        return sendMessageNoLoading(jsonObject.toString());
    }

    /**
     * 砸蛋
     *
     * @param count
     */
    public boolean smashEggs(String count, int eggType) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 66);
        jsonObject.addProperty("Count", count);
        jsonObject.addProperty("EggType", eggType);
        return sendMessageNoLoading(jsonObject.toString());
    }

    /*
     * 邀请上麦
     *
     * @param targetUserId 邀请人id
     * @param free         1免费 0付费
     */
    public void inviteMphone(int free, List<InviteOnmicroData> ids, int Level) {
        InviteOnMicroBean bean = new InviteOnMicroBean();
        bean.setTargetUserIds(ids);
        bean.setFree(free);
        bean.setMsgId(70);
        String json = JsonConverter.toJson(bean);
        sendMessage(json);
    }

    /**
     * 接受上麦邀请
     *
     * @param agree 是否同意
     */
    public void agreeInviteMp(boolean agree, boolean force) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 72);
        jsonObject.addProperty("Agree", agree ? 1 : 0);
        jsonObject.addProperty("Force", force);

        sendMessage(jsonObject.toString());
    }

    /**
     * 接受上麦邀请
     *
     * @param agree 是否同意
     */
    public void agreeInviteMpOutRoom(boolean agree, int id, boolean force) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 72);
        jsonObject.addProperty("Agree", agree ? 1 : 0);
        jsonObject.addProperty("InviteMicroId", id);
        jsonObject.addProperty("Force", force);
        sendMessage(jsonObject.toString());
    }

    /**
     *
     */
    public void reConnect() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 78);
        sendMessage(jsonObject.toString());
    }

    /**
     * 退出房间
     */
    public void exitRoom() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 3);
        sendMessage(jsonObject.toString());
    }


    //麦位：0为主持位，1为男嘉宾为，2为女嘉宾为，3为嘉宾位，4为沙发位
    public LiveData<Resource<BaseApiResult>> inviteUp(int inviterId, String inviteeId, int roomId, int free, int micro_level, int micro_cost) {
        return mRepository.inviteUp(inviterId, inviteeId, roomId, free, micro_level, micro_cost);
    }

    /**
     * 开播发送推送消息
     *
     * @return
     */
    public LiveData<Resource<BaseApiResult>> pushRoomMsg() {
        return mRepository.pushRoomMsg();
    }

    /**
     * 好友列表
     *
     * @return
     */
    public LiveData<Resource<FriendInfoListBean>> getFriendList() {
        return mRepository.getFriendList();
    }


    public LiveData<Resource<AuditBean>> getVerifyToken() {
        String signature = SignatureUtils.signByToken();
        return mRepository.getVerifyToken(signature);
    }

    public LiveData<Resource<BaseApiResult>> createapprove() {
        String signature = SignatureUtils.signByToken();
        return mRepository.createapprove(signature);
    }

    /**
     * 分享
     */
    public void getShareDataParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 87);
        sendMessage(jsonObject.toString());
    }

    /**
     * //（1为原唱，2为伴唱）
     */
    public void getSongListParam(int SongMode) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 89);
        jsonObject.addProperty("SongMode", SongMode);
        sendMessage(jsonObject.toString());
    }

    /**
     * 切歌
     *
     * @param
     */
    public void changeSongParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 93);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 正在播放参数
     *
     * @param
     */
    public void getPlaySongParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 97);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 开始下载歌曲
     *
     * @param
     */
    public void startDownloadSongParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 110);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 下载歌曲完成
     *
     * @param
     */
    public void downloadSongSuccessParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 111);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
        Log.d("SongSuccess", jsonObject.toString());
    }

    /**
     * 102暂停，105同步暂停
     *
     * @param
     */
    public void sysnSongPause(boolean isPause) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 102);
        jsonObject.addProperty("IsPause", isPause);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 116,砸蛋记录获取
     */
    public void getBreakEggRecordParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 116);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());

    }

    /**
     * 117,砸蛋记录获取 分页
     */
    public void getBreakEggRecordParam(int page) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 117);
        jsonObject.addProperty("Page", page);

        RoomController.getInstance().sendRoomMsg(jsonObject.toString());

    }

    /**
     * 114,加好友发送
     */
    public void notifyAddFriend(String targetId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 114);
        jsonObject.addProperty("TargetId", targetId);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());

    }

    /**
     * 120 赠送蓝玫瑰
     *
     * @param userIds
     * @param blueRose
     */
    public void giveBlueRose(List<Integer> userIds, int blueRose) {
        GiveBlueRoseParam bean = new GiveBlueRoseParam();
        bean.setBlueRose(blueRose);
        bean.setUserIds(userIds);
        bean.setMsgId(120);
        String json = JsonConverter.toJson(bean);
        sendMessage(json);
    }

    /**
     * 122 发送歌词
     *
     * @param lyric 歌词
     */
    public void notifySongLyricToClientParam(String lyric) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 123);
        jsonObject.addProperty("Lyric", lyric);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
        Log.d("songStateParam", jsonObject.toString());
    }

    /**
     * 126粉丝榜
     */
    public void getMicroRank(int Page, int Level, int Number) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 126);
        jsonObject.addProperty("Page", Page);
        jsonObject.addProperty("Level", Level);
        jsonObject.addProperty("Number", Number);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
        Log.d("AG_EX_AV", "getMicroRank:" + jsonObject.toString());

    }


    public LiveData<Resource<WalletBean>> wallet() {
        return mRepository.wallet();
    }

    public int pageNumber;

    public LiveData<Resource<RecommendRoomResult>> refreshRecommen(String roomId) {
        pageNumber = 1;
        return mRepository.recommendRoom(pageNumber, roomId);
    }

    public LiveData<Resource<RecommendRoomResult>> loadMoreRecommend(String roomId) {
        pageNumber++;
        return mRepository.recommendRoom(pageNumber, roomId);
    }

    public LiveData<Resource<FirstRechargeVo>> judgeFirstRecharge() {
        return mRepository.firstRecharge();
    }

    public LiveData<Resource<BaseApiResult>> shareCallback() {
        return mRepository.shareCallback();
    }

    public LiveData<Resource<BaseApiResult>> defriend(String uid) {
        return mRepository.defriend(uid, SignatureUtils.signWith(uid));
    }

    public LiveData<Resource<BaseApiResult>> blackout(String uid) {
        return mRepository.blackout(uid, SignatureUtils.signWith(uid));
    }

    public LiveData<Resource<RenewInitVo>> initGuard(String target_id) {
        return mRepository.initGuard(target_id);
    }

    /**
     * 微信支付
     *
     * @param rmb
     * @return
     */
    public LiveData<Resource<WxOrder>> wxpay(String rmb, String chargeid, String target_id, String guard_id, String long_day) {
        String uid = UserUtils.getUser().getUid();
        return mRepository.wxpay(rmb, Constant.CHARGE_Guard, Constant.CHARGE_ACT_J, uid, chargeid, target_id, guard_id, long_day);
    }


    /**
     * 支付宝支付
     *
     * @param rmb
     * @return
     */
    public LiveData<Resource<String>> alipay(String rmb, String chargeid, String target_id, String guard_id, String long_day) {
        String uid = UserUtils.getUser().getUid();
        return mRepository.alipay(rmb, Constant.CHARGE_Guard, Constant.CHARGE_ACT_J, uid, chargeid, target_id, guard_id, long_day);
    }

    /**
     * 138,红包规则
     */
    public void getRedPacketPlayDesc() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 138);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());

    }

    /**
     * 抢红包
     */
    public void robRedPacket() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 133);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
        Log.d("AG_EX_AV", jsonObject.toString());

    }
}

