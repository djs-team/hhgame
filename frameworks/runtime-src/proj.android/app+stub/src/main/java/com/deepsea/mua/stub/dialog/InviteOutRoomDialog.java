package com.deepsea.mua.stub.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogInviteOutRoomBinding;
import com.deepsea.mua.stub.entity.HeartBeatBean;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

import io.reactivex.functions.Consumer;

/**
 * Created by JUN on 2018/9/27
 */
public class InviteOutRoomDialog extends BaseDialog<DialogInviteOutRoomBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * @param v
         * @param dialog
         */
        void onDisagreeClick(View v, Dialog dialog);

        void onAgreeClick(View v, Dialog dialog);

        void onDismiss();
    }

    private OnClickListener listener;

    public InviteOutRoomDialog(@NonNull Context context) {
        super(context);
        ViewBindUtils.RxClicks(mBinding.tvInviteDisagree, o -> {
            dismiss();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_invite_out_room;
    }

    @Override
    protected float getWidthPercent() {
        return 0.77F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }


    public InviteOutRoomDialog setListener(OnClickListener cli) {
        setCancelable(false);
        this.listener = cli;
        initButton(mBinding.tvInviteDisagree, mBinding.tvInviteAgree);
        return this;
    }

    public void setContent(HeartBeatBean.InviteListBean bean) {
        //红娘
        String hongAvatar = bean.getHongAvatar();
        if (!TextUtils.isEmpty(hongAvatar)) {
            GlideUtils.loadImage(mBinding.ivMatchmakerBigAvatar, hongAvatar, R.drawable.ic_place_room_bg, R.drawable.ic_place_room_bg);
            GlideUtils.circleImage(mBinding.ivMatchmakerSmallAvatar, hongAvatar, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        }
        Consumer<Object> consumer = o -> {
            PageJumpUtils.jumpToProfile(bean.getHongId());
        };
        ViewBindUtils.RxClicks(mBinding.ivMatchmakerBigAvatar, consumer);
        ViewBindUtils.RxClicks(mBinding.ivMatchmakerSmallAvatar, consumer);
        ViewBindUtils.setText(mBinding.tvMatchmakerName, bean.getHongName());
//        int isFree = Integer.valueOf(bean.getFree());
//        if (isFree == 2) {
//            //收费
//            ViewBindUtils.setText(mBinding.tvInviteAgree, "同意（" + bean.isUpCost() + "）玫瑰");
//        }
        //游客
        String guestId = bean.getGuestId();
        boolean hasGuest = !TextUtils.isEmpty(guestId) && Integer.valueOf(guestId) > 0;
        ViewBindUtils.setVisible(mBinding.rlBeinvited, hasGuest);
        if (hasGuest) {
            Consumer<Object> consumer1 = o -> {
                PageJumpUtils.jumpToProfile(bean.getGuestId());
            };
            ViewBindUtils.RxClicks(mBinding.ivBeinvitedBigAvatar, consumer1);
            ViewBindUtils.RxClicks(mBinding.ivMatchmakerSmallAvatar, consumer1);
            String guestAvatar = bean.getGuestAvatar();
            if (!TextUtils.isEmpty(guestAvatar)) {
                GlideUtils.loadImage(mBinding.ivBeinvitedBigAvatar, guestAvatar, R.drawable.ic_place_room_bg, R.drawable.ic_place_room_bg);
                GlideUtils.circleImage(mBinding.ivBeinvitedSmallAvatar, guestAvatar, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            }
            ViewBindUtils.setText(mBinding.tvBeinvitedName, bean.getGuestName());
            StringBuilder info = new StringBuilder();
            info.append(bean.getAge());
            info.append("岁");
            if (!TextUtils.isEmpty(bean.getCity())) {
                info.append(" |");
                info.append(" " + bean.getCity());
            }
            if (Integer.valueOf(bean.getStature()) > 0) {
                info.append(" |");
                info.append(" " + bean.getStature());
            }
            ViewBindUtils.setText(mBinding.tvBeinviteIntroduction, info.toString());
            SexResUtils.setSexImg(mBinding.ivBeinvitedSex, String.valueOf(bean.getSex()));
        }

    }

    private void initButton(TextView diaAgree, TextView agree) {
        if (listener != null) {
            diaAgree.setOnClickListener(v -> listener.onDisagreeClick(v, InviteOutRoomDialog.this));
            agree.setOnClickListener(v -> listener.onAgreeClick(v, InviteOutRoomDialog.this));
        }

    }

    @Override
    public void dismiss() {
        if (listener != null) {
            listener.onDismiss();
            listener = null;
        }
        super.dismiss();
    }
}
