package com.deepsea.mua.mine.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemInvitationMineBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.ApplyFriendBean;
import com.deepsea.mua.stub.entity.InviteItemBean;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.StateUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/7
 */
public class InvitationMineAdapter extends BaseBindingAdapter<InviteItemBean, ItemInvitationMineBinding> {


    public InvitationMineAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_invitation_mine;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_invitation_mine;
    }

    @Override
    protected void bind(BindingViewHolder<ItemInvitationMineBinding> holder, InviteItemBean item) {
        GlideUtils.circleImage(holder.binding.avatarIv, item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        ViewBindUtils.setText(holder.binding.nickTv, item.getNickname());
        SexResUtils.setSexImg(holder.binding.sexIv, item.getSex());
        ViewBindUtils.setText(holder.binding.tvRegisterRealDate, item.getRegister_time());
        ViewBindUtils.RxClicks(holder.binding.avatarIv, o -> {
            PageJumpUtils.jumpToProfile(item.getId());
        });
        StringBuilder info = new StringBuilder();
        info.append(item.getAge());
        info.append("岁");
        if (!TextUtils.isEmpty(item.getCity())) {
            info.append(" |");
            info.append(" " + item.getCity());
        }
        String isOnline = item.getIs_online();

        info.append(" |");
        if (isOnline.equals("1")) {
            info.append(" " + "在线");
        } else {
            info.append(" " + "离线");
        }
        ViewBindUtils.setText(holder.binding.infoTv, info.toString());
        String attestation = (item.getAttestation().equals("0") ? "未实名认证" : "已实名认证") + "  +" + item.getSend_coin() + "玫瑰";
        ViewBindUtils.setText(holder.binding.tvAuthenState, attestation);
        ViewBindUtils.setVisible(holder.binding.ivOnmicro,item.getState().equals("3"));
        if (item.getState().equals("3")) {
            GlideUtils.loadGif(holder.binding.ivOnmicro, R.drawable.icon_is_onmicro);
        }
    }
}
