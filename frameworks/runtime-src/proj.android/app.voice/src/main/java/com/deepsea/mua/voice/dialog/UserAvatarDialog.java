package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.socket.MicroUser;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.utils.FriendsUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogUserAvatarBinding;
import com.deepsea.mua.stub.utils.ForbiddenStateUtils;
import com.deepsea.mua.voice.utils.MatchMakerUtils;

/**
 * Created by JUN on 2019/3/28
 */
public class UserAvatarDialog extends BaseDialog<DialogUserAvatarBinding> {

    public interface OnAvatarListener {
        /**
         * 下自己麦
         */
        void onUpDownMicro(boolean isOnMicro);

        /**
         * 房主强制下别人麦
         *
         * @param level  麦型
         * @param number 麦位
         */
        void onForceDownMp(String uid, int level, int number);

        /**
         * 禁言
         *
         * @param wsUser
         */
        void onForbidden(WsUser wsUser, boolean isDisableMsg);

        /**
         * 查看用户详情
         *
         * @param uid
         */
        void onProfile(String uid);

        /**
         * 关注/取消
         *
         * @param uid
         * @param type 1关注2取消关注
         */
        void onFollow(String uid, String type);

        /**
         * 打赏
         *
         * @param uid
         */
        void onSendGift(String uid);

        /**
         * 移出房间
         *
         * @param uid
         */
        void onRemove(String uid);

        /**
         * 清空心动值
         *
         * @param level
         * @param number
         */
        void onCleanHeart(int level, int number);

        /**
         * 开始倒计时
         *
         * @param level
         * @param number
         */
        void onDownTimer(int level, int number);

        /**
         * 闭麦
         *
         * @param isDisabledMicro
         * @param level
         * @param number
         */
        void onCloseMicro(String uid, boolean isDisabledMicro, int level, int number);

        /**
         * 邀请上麦
         *
         * @param nick
         * @param uid
         * @param sex
         */
        void inviteMphone(String nick, String uid, int sex);

        /**
         * 加好友
         *
         * @param uid
         */
        void addFriend(String uid, String imgUrl, String nickName, boolean isMyFriend);

        /**
         * 聊天
         *
         * @param uname
         */
        void chatUser(String uname);

        /**
         * 拉黑
         *
         * @param uid
         */
        void blockUser(String uid, boolean isBlock);

        /**
         * 举报
         *
         * @param uid
         */
        void reportUser(String uid);
    }

    private OnAvatarListener mListener;

    private MicroUser mMicroUser;
    private String mMicroUserId;
    private boolean isDisableMsg;
    private boolean isDisabledMicro;

    public UserAvatarDialog(@NonNull Context context) {
        super(context);
        mBinding.setDlg(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_user_avatar;
    }

    @Override
    protected float getWidthPercent() {
        return 1F;
    }

    public void setOnAvatarListener(OnAvatarListener listener) {
        this.mListener = listener;
    }

    public MicroUser getMicroUser() {
        return mMicroUser;
    }

    /**
     * @param microUser 当前查看的用户信息
     */
    public void setData(MicroUser microUser) {
        User self = UserUtils.getUser();
        if (self == null || microUser == null || microUser.getSuccess() != 1)
            return;

        this.mMicroUser = microUser;
        WsUser user = mMicroUser.getUser();
        this.mMicroUserId = user.getUserId();
        this.isDisableMsg = microUser.isIsDisableMsg();
        this.isDisabledMicro = ForbiddenStateUtils.getForbiddenMicState(user.getUserId()) || ForbiddenStateUtils.getForbiddenLBstate(user.getUserId());
        boolean isShowAddFriend = !self.getUid().equals(user.getUserId()) && !FriendsUtils.getInstance().isMyFriend(user.getUserId());
        if (isShowAddFriend) {
            ViewBindUtils.setText(mBinding.addfriendTv, "送礼物加好友");
        } else {
            ViewBindUtils.setText(mBinding.addfriendTv, "私聊好友");
        }
        GlideUtils.circleImage(mBinding.avatarIv, user.getHeadImageUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        ViewBindUtils.setText(mBinding.nickTv, user.getName());
        SexResUtils.setSexImgInFindPage(mBinding.rlSex, mBinding.sexIv, mMicroUser.getSex());
        ViewBindUtils.setText(mBinding.levelTv, "LV" + mMicroUser.getLevel());

        StringBuilder sb = new StringBuilder();
        if (user.getAge() > 0) {
            sb.append(user.getAge()).append("岁");
        }
        if (user.getStature() > 0) {
            sb.append(sb.length() == 0 ? "" : "  ").append(user.getStature()).append("cm");
        }

        mBinding.profileTv.setText(sb.toString());
        ViewBindUtils.setVisible(mBinding.profileTv, !TextUtils.isEmpty(sb.toString()));
        boolean hasCity = !TextUtils.isEmpty(user.getCity());
        if (hasCity) {
            ViewBindUtils.setText(mBinding.tvLocation, user.getCity());
        }
        ViewBindUtils.setVisible(mBinding.ivLocation, hasCity);

        mBinding.forbiddenTv.setText(isDisableMsg ? "取消禁言" : "禁言");
        mBinding.forbiddenTv.setSelected(isDisableMsg);

        mBinding.closeMpTv.setText(isDisabledMicro ? "取消闭麦" : "闭麦");
        mBinding.closeMpTv.setSelected(isDisabledMicro);
        ViewBindUtils.setText(mBinding.blockTv, microUser.isBlock() ? "取消拉黑" : "拉黑");


        //自己
        if (TextUtils.equals(self.getUid(), user.getUserId())) {
//            mBinding.downMicroMine.setText(microUser.isIsOnMicro() ? "结束相亲" : "申请相亲");
//            mBinding.downMicroMine.setVisibility(View.VISIBLE);
//            mBinding.managerLayout.setVisibility(View.GONE);
            ViewBindUtils.setVisible(mBinding.closeMpTv, true);

            ViewBindUtils.setVisible(mBinding.forbiddenTv, false);
            ViewBindUtils.setVisible(mBinding.inviteFreeTv, false);
            ViewBindUtils.setVisible(mBinding.inviteNofreeTv, false);
            ViewBindUtils.setVisible(mBinding.blockTv, false);
            ViewBindUtils.setVisible(mBinding.removeRoom, false);
            ViewBindUtils.setVisible(mBinding.reportUserTv, false);
            ViewBindUtils.setVisible(mBinding.addfriendTv, false);
            ViewBindUtils.setVisible(mBinding.llBottomOperate, false);
            ViewBindUtils.setVisible(mBinding.line, false);

        }
        //红娘
        else if (MatchMakerUtils.isRoomOwner()) {
            ViewBindUtils.setVisible(mBinding.inviteFreeTv, !microUser.isIsOnMicro());
            ViewBindUtils.setVisible(mBinding.inviteNofreeTv, !microUser.isIsOnMicro());
            ViewBindUtils.setVisible(mBinding.closeMpTv, true);
            ViewBindUtils.setVisible(mBinding.forbiddenTv, true);
            ViewBindUtils.setVisible(mBinding.inviteFreeTv, true);
            ViewBindUtils.setVisible(mBinding.inviteNofreeTv, true);
            ViewBindUtils.setVisible(mBinding.blockTv, true);
            ViewBindUtils.setVisible(mBinding.removeRoom, true);
            ViewBindUtils.setVisible(mBinding.reportUserTv, true);
            ViewBindUtils.setVisible(mBinding.addfriendTv, true);
            ViewBindUtils.setVisible(mBinding.llBottomOperate, true);
            ViewBindUtils.setVisible(mBinding.line, true);
        }
        //看别人
        else {
            ViewBindUtils.setVisible(mBinding.inviteFreeTv, !microUser.isIsOnMicro());
            ViewBindUtils.setVisible(mBinding.inviteNofreeTv, !microUser.isIsOnMicro());
            ViewBindUtils.setVisible(mBinding.closeMpTv, true);
            ViewBindUtils.setVisible(mBinding.forbiddenTv, false);
            ViewBindUtils.setVisible(mBinding.inviteFreeTv, false);
            ViewBindUtils.setVisible(mBinding.inviteNofreeTv, false);
            ViewBindUtils.setVisible(mBinding.blockTv, false);
            ViewBindUtils.setVisible(mBinding.removeRoom, false);
            ViewBindUtils.setVisible(mBinding.reportUserTv, true);
            ViewBindUtils.setVisible(mBinding.addfriendTv, true);
            ViewBindUtils.setVisible(mBinding.llBottomOperate, true);
            ViewBindUtils.setVisible(mBinding.line, true);
        }
        boolean isCanSendGift = MatchMakerUtils.isCanSendGift();
        if (isCanSendGift) {
            ViewBindUtils.setVisible(mBinding.handselTv, true);
        } else {
            if (microUser.getUserIdentity() == 2) {
                ViewBindUtils.setVisible(mBinding.handselTv, false);
            } else {
                ViewBindUtils.setVisible(mBinding.handselTv, true);

            }
        }
    }

    public void onClick(View view) {
        //头像
        if (view == mBinding.avatarIv) {
            if (mListener != null) {
                mListener.onProfile(mMicroUser.getUser().getUserId());
            }
        }

        //打赏
        else if (view == mBinding.handselTv) {
            if (mListener != null) {
                mListener.onSendGift(mMicroUser.getUser().getUserId());
            }
        }
//        //上/下自己麦
//        else if (view == mBinding.downMicroMine) {
//            if (mListener != null) {
//                mListener.onUpDownMicro(mMicroUser.isIsOnMicro());
//            }
//        }
//        //强制下麦
//        if (view == mBinding.downMpTv) {
//            if (mListener != null) {
//                mListener.onForceDownMp(mMicroUser.getUser().getUserId(), mMicroUser.getType(), mMicroUser.getNumber());
//            }
//        }
        //禁言
        if (view == mBinding.forbiddenTv) {
            if (mListener != null) {
//                mListener.onForbidden(mMicroUserId, isDisableMsg, mMicroUser.getType(), mMicroUser.getNumber());
                mMicroUser.setIsDisableMsg(!mMicroUser.isIsDisableMsg());
                mBinding.forbiddenTv.setText(mMicroUser.isIsDisableMsg() ? "取消禁言" : "禁言");
                mListener.onForbidden(mMicroUser.getUser(), mMicroUser.isIsDisableMsg());
            }
        }
        //移出房间
        if (view == mBinding.removeRoom) {
            if (mListener != null) {
                mListener.onRemove(mMicroUserId);
            }
        }
        //加好友
        if (view == mBinding.addfriendTv) {
            if (mListener != null) {
                mListener.addFriend(mMicroUserId, mMicroUser.getUser().getHeadImageUrl(), mMicroUser.getUser().getName(), mBinding.addfriendTv.getText().toString().trim().equals("私聊好友"));
            }
        }
        //清空魅力值
//        if (view == mBinding.cleanHeartTv) {
//            if (mListener != null) {
//                mListener.onCleanHeart(mMicroUser.getType(), mMicroUser.getNumber());
//            }
//        }
        //开始倒计时
//        if (view == mBinding.timeTv) {
//            if (mListener != null) {
//                mListener.onDownTimer(mMicroUser.getType(), mMicroUser.getNumber());
//            }
//        }
        //闭麦
        if (view == mBinding.closeMpTv) {
            if (mListener != null) {
                mListener.onCloseMicro(mMicroUserId, isDisabledMicro, mMicroUser.getType(), mMicroUser.getNumber());
            }
        }
        //邀请上麦-免费
        if (view == mBinding.inviteFreeTv) {
            if (mListener != null) {
                mListener.inviteMphone(mMicroUser.getUser().getName(), mMicroUserId, 1);
            }
        }
        //邀请上麦-收费
        if (view == mBinding.inviteNofreeTv) {
            if (mListener != null) {
                mListener.inviteMphone(mMicroUser.getUser().getName(), mMicroUserId, 0);
            }
        }
        if (view == mBinding.tvGuardList) {
            PageJumpUtils.jumpToGuard(mContext, mMicroUser.getUser().getUserId());
        }
        if (view == mBinding.tvAite) {
            if (mListener != null) {
                mListener.chatUser(mMicroUser.getUser().getName());
            }
        }
        if (view == mBinding.blockTv) {
            if (mListener != null) {
                mMicroUser.setBlock(!mMicroUser.isBlock());
                ViewBindUtils.setText(mBinding.blockTv, mMicroUser.isBlock() ? "取消拉黑" : "拉黑");
                mListener.blockUser(mMicroUserId, mMicroUser.isBlock());
            }
        }
        if (view == mBinding.reportUserTv) {
            if (mListener != null) {
                mListener.reportUser(mMicroUserId);
            }
        }
        dismiss();
    }


}
