package com.deepsea.mua.stub.controller;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.entity.model.RoomMsgBean;
import com.deepsea.mua.stub.entity.socket.receive.JoinUser;
import com.deepsea.mua.stub.entity.socket.ReceiveMessage;
import com.deepsea.mua.stub.entity.socket.receive.ReceivePresent;
import com.deepsea.mua.stub.entity.socket.RoomManager;
import com.deepsea.mua.stub.entity.socket.receive.SmashEggBean;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.entity.socket.receive.NotifyHelpToUserParam;
import com.deepsea.mua.stub.entity.socket.receive.UpMicroMsg;
import com.deepsea.mua.stub.model.RoomModel;
import com.deepsea.mua.stub.utils.span.CenterImageSpan;
import com.deepsea.mua.stub.utils.span.IdenImageSpan;
import com.deepsea.mua.stub.utils.span.LevelImageSpan;
import com.deepsea.mua.stub.utils.span.LevelResUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by JUN on 2019/3/26
 */
public class RoomMsgHandler {

    private Context mContext;
    private RoomModel mRoomModel;
    private OnMsgEventListener mListener;

    public interface OnMsgEventListener {
        /**
         * @param uid
         */
        void onMsgClick(String uid);

        void onMsgLongClick(String uName);
    }

    public RoomMsgHandler(Context context) {
        this.mContext = context;
    }

    public void setRoomModel(RoomModel roomModel) {
        mRoomModel = roomModel;
    }

    public void setOnMsgEventListener(OnMsgEventListener listener) {
        this.mListener = listener;
    }


    private void appendLevel(SpannableStringBuilder spannable, int level) {
        if (level <= 0)
            return;
        String temp = "" + level;
        spannable.append(temp);
        Drawable levelDraw = ResUtils.getDrawable(mContext, LevelResUtils.getLevelRes(level));
        levelDraw.setBounds(0, 0, levelDraw.getIntrinsicWidth(), levelDraw.getIntrinsicHeight() * 2 / 3);
        LevelImageSpan levelSpan = new LevelImageSpan(levelDraw, mContext);
        spannable.setSpan(levelSpan, spannable.length() - temp.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private void appendGuard(SpannableStringBuilder spannable, String guardStr) {
        if (TextUtils.isEmpty(guardStr))
            return;
        String temp = guardStr;
        spannable.append(temp);
        Drawable guardDraw = ResUtils.getDrawable(mContext, R.drawable.bg_guard_item_sign);
        guardDraw.setBounds(0, 0, temp.length() * 30, guardDraw.getIntrinsicHeight());
        spannable.setSpan(new LevelImageSpan(guardDraw, mContext), spannable.length() - temp.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private void appendNobility(SpannableStringBuilder spannable, int level) {
        if (level <= 0)
            return;
        String temp = "NOBILITY";
        spannable.append(temp);
        Drawable nobilityDraw = ResUtils.getDrawable(mContext, LevelResUtils.getNobilityRes(level));
        nobilityDraw.setBounds(0, 0, nobilityDraw.getIntrinsicWidth(), nobilityDraw.getIntrinsicHeight());
        spannable.setSpan(new CenterImageSpan(nobilityDraw), spannable.length() - temp.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private void appendNick(SpannableStringBuilder spannable, String nick) {
        if (nick == null) {
            nick = "null";
        }
        spannable.append(nick);
        ForegroundColorSpan msgSpan = new ForegroundColorSpan(0xFF808080);
        spannable.setSpan(msgSpan, spannable.length() - nick.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private void appendNick(SpannableStringBuilder spannable, String nick, int sex) {
        if (nick == null) {
            nick = "null";
        }
        spannable.append(nick);
        int sexColor = 0xFFFF2B7C;
        if (sex == 1) {
            sexColor = 0xFF6E6EFF;
        } else if (sex == 2) {
            sexColor = 0xFFFF2B7C;
        }
        ForegroundColorSpan msgSpan = new ForegroundColorSpan(sexColor);
        spannable.setSpan(msgSpan, spannable.length() - nick.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    public static float sp2px(Context context, float sp) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scaledDensity;
    }

    private void appendMsg(SpannableStringBuilder spannable, String msg, int level) {
        spannable.append(msg);
        ForegroundColorSpan msgSpan = new ForegroundColorSpan(0xFFFFFFFF);
        spannable.setSpan(msgSpan, spannable.length() - msg.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

    }

    private void appendSysMsg(SpannableStringBuilder spannable, String msg, int level) {
        spannable.append(msg);
        ForegroundColorSpan msgSpan = new ForegroundColorSpan(0xFFFFFFFF);
        spannable.setSpan(msgSpan, spannable.length() - msg.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private void setNickClick(SpannableStringBuilder spannable, String nick, String uid, int start) {
        if (nick == null) {
            return;
        }
        spannable.setSpan(new ClickableSpan() {

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(ds.getColor());
            }

            @Override
            public void onClick(@NonNull View widget) {
                if (mListener != null) {
                    mListener.onMsgClick(uid);
                }
            }

        }, start, start + nick.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    private void appendIden(SpannableStringBuilder spannable, int userIdentity, String uid) {
        //普通用户
        if (userIdentity == 0)
            return;
        boolean onHostMicro = isOnHostMicro(uid);
        String temp = onHostMicro ? "主持" : "管理";
        spannable.append(temp);
        Drawable idenDraw = ResUtils.getDrawable(mContext, LevelResUtils.getUserIdentity(onHostMicro ? 2 : 1));
        idenDraw.setBounds(0, 0, idenDraw.getIntrinsicWidth(), idenDraw.getIntrinsicHeight());
        IdenImageSpan idenSpan = new IdenImageSpan(idenDraw, mContext);
        spannable.setSpan(idenSpan, spannable.length() - temp.length(), spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
    }

    /**
     * 该用户是否在主持麦上
     *
     * @param uid
     * @return
     */
    private boolean isOnHostMicro(String uid) {
        if (mRoomModel.getHostMicro() != null && mRoomModel.getHostMicro().getUser() != null) {
            WsUser user = mRoomModel.getHostMicro().getUser();
            return TextUtils.equals(user.getUserId(), uid);
        }
        return false;
    }

    /**
     * 进入房间
     *
     * @return
     */
    public RoomMsgBean getJoinRoomMsg(JoinUser user) {
        RoomMsgBean result = null;
        if (user != null) {
            result = new RoomMsgBean();
            result.setNormal(false);
            result.setUid(user.getUserId());
            result.setuName(user.getName());

            SpannableStringBuilder builder = new SpannableStringBuilder();
            int start = builder.length();
            result.setAvatar(user.getAvatar());
            String desc = getUserInfo(user.getCity(), user.getAge(), user.getName());
            appendNick(builder, desc, user.getSex());
            appendSysMsg(builder, " 进入房间", user.getUserLevel());
            setNickClick(builder, user.getName(), user.getUserId(), start);
            result.setMsg(builder);
        }
        return result;
    }

    /**
     * 管理员消息
     *
     * @param manager
     * @return
     */
    public RoomMsgBean getRoomManagerMsg(RoomManager manager) {
        RoomMsgBean result = null;
        if (manager != null) {
            result = new RoomMsgBean();
            result.setNormal(false);
            result.setUid(manager.getUserId());
            result.setuName(manager.getName());
            SpannableStringBuilder builder = new SpannableStringBuilder();
            appendGuard(builder, result.getGuardSign());
            int start = builder.length();
            appendNick(builder, manager.getName(), manager.getUserLevel());
            appendSysMsg(builder, " 成为管理员", manager.getUserLevel());
            setNickClick(builder, manager.getName(), manager.getUserId(), start);
            result.setMsg(builder);
        }
        return result;
    }

    /**
     * 广播礼物消息
     *
     * @param model
     * @return
     */
    public List<RoomMsgBean> getBroadGiftMsg(ReceivePresent model) {
        List<RoomMsgBean> list = null;
        if (model != null && !model.getGiveGiftDatas().isEmpty()) {
            list = new ArrayList<>();
            String url = model.getGiftData().getImage();
            for (ReceivePresent.GiveGiftDatasBean bean : model.getGiveGiftDatas()) {
                RoomMsgBean result = new RoomMsgBean();
                result.setGuardSign(model.getGuardSign());
                result.setNormal(false);
                SpannableStringBuilder builder = new SpannableStringBuilder();

                result.setUid(model.getGiftGiver().getUserId());
                result.setuName(model.getGiftGiver().getName());
                result.setAvatar(model.getGiftGiver().getHeadImageUrl());
                int start = builder.length();
                appendGuard(builder, result.getGuardSign());
                String desc = getUserInfo(model.getGiftGiver().getCity(), model.getGiftGiver().getAge(), model.getGiftGiver().getName());
                appendNick(builder, desc, model.getGiftGiver().getSex());
                appendSysMsg(builder, "打赏", model.getUserLevel());
                setNickClick(builder, desc, model.getGiftGiver().getUserId(), start);
//                start = builder.length();
                appendNick(builder, bean.getTargetName() + " ", bean.getSex());
                setNickClick(builder, bean.getTargetName(), "12148", desc.length()+2);
                result.setUrl(url);
                result.setCount(model.getCount());
                result.setStart(builder.length());
                result.setMsg(builder);
                list.add(result);
            }
        }
        return list;
    }

    private String getUserInfo(String city, String age, String nickName) {
        StringBuilder descBuild = new StringBuilder();

        if (!TextUtils.isEmpty(city)) {
            descBuild.append(city);
        }
        if (!TextUtils.isEmpty(age)) {
            if (TextUtils.isEmpty(city)) {
                descBuild.append(age).append("岁");
            } else {
                descBuild.append("|").append(age).append("岁");
            }
        }
        String desc = "";
        if (!TextUtils.isEmpty(descBuild.toString())) {
            desc = ("(" + descBuild.toString() + ")");
        }
        desc = nickName + desc;
        return desc;
    }

    /**
     * 用户上麦消息
     *
     * @param micro
     * @return
     */
    public RoomMsgBean getUpMicroMsg(UpMicroMsg micro) {
        RoomMsgBean result = null;
        if (micro != null) {
            result = new RoomMsgBean();
            result.setGuardSign(micro.getGuardSign());
            result.setNormal(false);

            SpannableStringBuilder builder = new SpannableStringBuilder();
            int start = builder.length();
            WsUser wsUser = micro.getMicroInfo().getUser();
            result.setUid(wsUser.getUserId());
            result.setuName(wsUser.getName());
            result.setAvatar(wsUser.getHeadImageUrl());
            appendGuard(builder, result.getGuardSign());

            String desc = getUserInfo(wsUser.getCity(), wsUser.getAge(), wsUser.getName());

            appendNick(builder, desc, wsUser.getSex());
            appendSysMsg(builder, " 上麦了", micro.getUserLevel());
            setNickClick(builder, wsUser.getName(), wsUser.getUserId(), start);
            result.setMsg(builder);
        }
        return result;
    }

    /**
     * 普通聊天消息
     *
     * @param msgBean
     * @return
     */
    public RoomMsgBean getReceiveMsg(ReceiveMessage msgBean) {
        RoomMsgBean result = null;
        if (msgBean != null) {
            result = new RoomMsgBean();
            result.setNormal(true);
            JoinUser user = msgBean.getUser();
            result.setGuardSign(user.getGuardSign());
            SpannableStringBuilder builder = new SpannableStringBuilder();
            result.setAvatar(user.getAvatar());
            result.setUid(user.getUserId());
            result.setuName(user.getName());
            result.setAvatar(user.getAvatar());
            int start = builder.length();
            appendGuard(builder, result.getGuardSign());
            String desc = getUserInfo(user.getCity(), user.getAge(), user.getName());
            appendNick(builder, desc + "：", user.getSex());
            setNickClick(builder, user.getName(), user.getUserId(), start);
            if (!TextUtils.isEmpty(msgBean.getMsg())) {
                appendMsg(builder, msgBean.getMsg(), user.getUserLevel());
            }
            if (!TextUtils.isEmpty(msgBean.getEmojiurl())) {
                result.setUrl(msgBean.getEmojiurl());
                result.setStart(builder.length());
            }
            result.setMsg(builder);
            result.setLevel(user.getUserLevel());

        }
        return result;
    }

    /**
     * 砸蛋消息
     *
     * @param bean
     * @return
     */
    public RoomMsgBean getSmashMsg(SmashEggBean bean) {
        RoomMsgBean result = null;
        if (bean != null) {
            result = new RoomMsgBean();
            result.setGuardSign(bean.getGuardSign());
            result.setNormal(true);
            SpannableStringBuilder builder = new SpannableStringBuilder();
            int start = builder.length();
            //appendNick
            String nick = String.format(Locale.CHINA, "恭喜%s 自动砸" + (bean.getEggType() == 0 ? "金" : "银") + "蛋得到 ", bean.getUserNickName());
            builder.append(nick);
            ForegroundColorSpan mainSpan = new ForegroundColorSpan(0xFFFFFFFF);
            builder.setSpan(mainSpan, builder.length() - nick.length(), builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            int sexColor = 0xFFFF4D4D;
            if (bean.getSex() == 1) {
                sexColor = 0xFF6E6EFF;
            } else if (bean.getSex() == 2) {
                sexColor = 0xFFFF2B7C;
            }
            ForegroundColorSpan msgSpan = new ForegroundColorSpan(sexColor);
            builder.setSpan(msgSpan, builder.length() - nick.length() + 2, builder.length() - nick.length() + 2 + bean.getUserNickName().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //setNickClick
            result.setUid(bean.getUserId());
            result.setuName(bean.getUserNickName());
            appendLevel(builder, bean.getUserLevel());
            appendGuard(builder, result.getGuardSign());
            setNickClick(builder, nick, bean.getUserId(), start);
            result.setStart(builder.length());
            result.setMsg(builder);
        }
        return result;
    }

    /**
     * 助力
     *
     * @param bean
     * @return
     */
    public RoomMsgBean getNofifyHelpMsg(NotifyHelpToUserParam bean) {
        RoomMsgBean result = null;
        if (bean != null) {
            result = new RoomMsgBean();
            result.setNormal(true);
            SpannableStringBuilder builder = new SpannableStringBuilder();
            int start = builder.length();
            //appendNick
            String nick = String.format(Locale.CHINA, "%s邀请好友给%s助力了" + bean.getCoin() + "朵玫瑰", bean.getUserName(), bean.getTargetName());
            builder.append(nick);
            appendGuard(builder, result.getGuardSign());
            ForegroundColorSpan mainSpan = new ForegroundColorSpan(0xFFFFFFFF);
            builder.setSpan(mainSpan, builder.length() - nick.length(), builder.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            int userSex = getColorFromSex(bean.getUserSex());
            int targetSex = getColorFromSex(bean.getTargetSex());
            ForegroundColorSpan msgSpan = new ForegroundColorSpan(userSex);
            builder.setSpan(msgSpan, builder.length() - nick.length(), builder.length() - nick.length() + bean.getUserName().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ForegroundColorSpan msgSpan2 = new ForegroundColorSpan(targetSex);
            builder.setSpan(msgSpan2, builder.length() - nick.length() + 5 + bean.getUserName().length(), builder.length() - nick.length() + bean.getUserName().length() + 5 + bean.getTargetName().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            //setNickClick
            result.setUid(String.valueOf(bean.getUserId()));
            result.setuName(bean.getUserName());
            setNickClick(builder, nick, String.valueOf(bean.getUserId()), start);
            result.setStart(builder.length());
            result.setMsg(builder);
        }
        return result;
    }

    private int getColorFromSex(int sex) {
        int userSex = 0xFFFF4D4D;
        if (sex == 1) {
            userSex = 0xFF6E6EFF;
        } else if (sex == 2) {
            userSex = 0xFFFF2B7C;
        }
        return userSex;
    }

    /**
     * 踢出房间消息
     *
     * @param nick
     * @param uid
     * @return
     */
    public RoomMsgBean getKickRoomMsg(String nick, String uid) {
        RoomMsgBean result = new RoomMsgBean();
        result.setNormal(false);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        appendSysMsg(builder, "用户" + nick + "已踢出房间", 0);
        result.setUid(uid);
        result.setuName(nick);
        setNickClick(builder, nick, uid, 2);
        result.setMsg(builder);
        return result;
    }
}
