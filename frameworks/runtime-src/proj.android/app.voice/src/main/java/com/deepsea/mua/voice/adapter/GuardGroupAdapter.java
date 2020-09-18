package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.GuardInfoBean;
import com.deepsea.mua.stub.entity.LookGuardUserVo;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemGuardGroupBinding;
import com.deepsea.mua.voice.databinding.ItemUserFansBinding;

/**
 * Created by JUN on 2019/10/14
 */
public class GuardGroupAdapter extends BaseBindingAdapter<LookGuardUserVo.GuardMemberlistBean, ItemGuardGroupBinding> {


    public GuardGroupAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_guard_group;
    }


    @Override
    protected void bind(BindingViewHolder<ItemGuardGroupBinding> holder, LookGuardUserVo.GuardMemberlistBean item) {
        int pos = holder.getLayoutPosition();
        GlideUtils.circleImage(holder.binding.rankIv, item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        ViewBindUtils.RxClicks(holder.binding.rankIv, o -> {
            PageJumpUtils.jumpToProfile(String.valueOf(item.getUserId()));
        });
        holder.binding.tvName.setText(item.getNickname());
        SexResUtils.setSexImgInFindPage(holder.binding.rlSex, holder.binding.sexIv, String.valueOf(item.getSex()));
        ViewBindUtils.setText(holder.binding.tvAge, String.valueOf(item.getAge()));
        ViewBindUtils.setVisible(holder.binding.tvAge, item.getAge() != 0);
        ViewBindUtils.setVisible(holder.binding.tvLocation, !TextUtils.isEmpty(item.getCity()));
        if (!TextUtils.isEmpty(item.getCity())) {
            ViewBindUtils.setText(holder.binding.tvLocation, item.getCity());
        }
        ViewBindUtils.setText(holder.binding.tvIntimacy, "亲密值" + item.getIntimacy());

    }
}
