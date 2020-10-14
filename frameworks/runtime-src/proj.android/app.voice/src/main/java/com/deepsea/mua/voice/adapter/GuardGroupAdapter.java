package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.GuardInfoBean;
import com.deepsea.mua.stub.entity.LookGuardUserVo;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.entity.socket.receive.GuardItem;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemGuardGroupBinding;
import com.deepsea.mua.voice.databinding.ItemUserFansBinding;

/**
 * Created by JUN on 2019/10/14
 */
public class GuardGroupAdapter extends BaseBindingAdapter<GuardItem, ItemGuardGroupBinding> {


    public GuardGroupAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_guard_group;
    }


    @Override
    protected void bind(BindingViewHolder<ItemGuardGroupBinding> holder, GuardItem item) {
        int pos = holder.getLayoutPosition();
        WsUser user = item.getUserInfo();
        GlideUtils.circleImage(holder.binding.rankIv, user.getHeadImageUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        ViewBindUtils.RxClicks(holder.binding.rankIv, o -> {
            PageJumpUtils.jumpToProfile(user.getUserId());
        });
        holder.binding.tvName.setText(user.getName());
        SexResUtils.setSexImgInFindPage(holder.binding.rlSex, holder.binding.sexIv, String.valueOf(user.getSex()));
        ViewBindUtils.setText(holder.binding.tvAge, String.valueOf(user.getAge()));
        ViewBindUtils.setVisible(holder.binding.tvAge, user.getAge() != 0);
        ViewBindUtils.setVisible(holder.binding.tvLocation, !TextUtils.isEmpty(user.getCity()));
        if (!TextUtils.isEmpty(user.getCity())) {
            ViewBindUtils.setText(holder.binding.tvLocation, user.getCity());
        }
        ViewBindUtils.setVisible(holder.binding.ivLocation, !TextUtils.isEmpty(user.getCity()));
        ViewBindUtils.setText(holder.binding.tvIntimacy, "亲密值" + item.getIntimacy());
        ViewBindUtils.setText(holder.binding.tvDueDate, "还有" + item.getDays() + "到期");

    }
}
