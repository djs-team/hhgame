package com.deepsea.mua.voice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.deepsea.mua.core.dialog.BaseDialog;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.entity.OnlinesBean;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogFullServiceUserDetailsBinding;

import io.reactivex.functions.Consumer;

/**
 * Created by JUN on 2018/9/27
 */
public class FullServiceUserDetailDialog extends BaseDialog<DialogFullServiceUserDetailsBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         */
        //邀请上麦
        void onInviteClick(String uid);

        //送礼/打赏
        void sendGift(String uid);

        //加好友
        void addFriend(String uid);
    }

    public FullServiceUserDetailDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_full_service_user_details;
    }

    @Override
    protected float getWidthPercent() {
        return 0.77F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }

    OnlinesBean.NearbyBean data;

    public void setContent(OnlinesBean.NearbyBean bean) {
        this.data = bean;
        ViewBindUtils.setText(mBinding.nickTv, data.getNickname());
        ViewBindUtils.setText(mBinding.tvLv, "LV" + data.getLv_dengji());
        SexResUtils.setSexImg(mBinding.ivSex, data.getSex());
        GlideUtils.circleImage(mBinding.avatarIv, data.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        StringBuilder info = new StringBuilder();
        info.append(data.getAge());
        info.append("岁");
        if (!TextUtils.isEmpty(data.getCity())) {
            info.append(" |");
            info.append(" " + data.getCity());
        }
        if (data.getStature() != 0) {
            info.append(" |");
            info.append(" " + data.getStature());
        }
        ViewBindUtils.setText(mBinding.infoTv, info.toString());
    }

    public FullServiceUserDetailDialog setButton(OnClickListener cli) {
        initButton(mBinding.tvInvite, cli);
        return this;
    }

    private void initButton(TextView tv_invite, OnClickListener cli) {
        if (cli != null) {
            final OnClickListener c = cli;
            tv_invite.setOnClickListener(v -> c.onInviteClick(data.getId()));
        }
        Consumer<Object> consumer = o -> {

            PageJumpUtils.jumpToProfile(data.getId());
        };
        ViewBindUtils.RxClicks(mBinding.avatarIv, consumer);
        ViewBindUtils.RxClicks(mBinding.tvPersonalCenter, consumer);
        ViewBindUtils.RxClicks(mBinding.tvSendGift, o -> {
            if (cli != null) {
                cli.sendGift(data.getId());
            }
        });
        ViewBindUtils.RxClicks(mBinding.tvAddFriend, o -> {
            if (cli != null) {
                PageJumpUtils.jumpToFriendAdd(data.getId(), data.getAvatar(), data.getNickname());
            }
        });
    }


}
