package com.deepsea.mua.mine.adapter;

import android.content.Context;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemGuardBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.LookGuardUserVo;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * 守护榜adapter
 */
public class GuardAdapter extends BaseBindingAdapter<LookGuardUserVo.GuardMemberlistBean, ItemGuardBinding> {


    public GuardAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_guard;
    }

    @Override
    protected void bind(BindingViewHolder<ItemGuardBinding> holder, LookGuardUserVo.GuardMemberlistBean item) {
        GlideUtils.circleImage(holder.binding.rankIv, item.getAvatar(), R.drawable.ic_place, R.drawable.ic_place);
        ViewBindUtils.setText(holder.binding.tvName, item.getNickname());
        SexResUtils.setSexImgInFindPage(holder.binding.rlSex, holder.binding.sexIv, item.getSex());
        ViewBindUtils.setText(holder.binding.tvAge, String.valueOf(item.getAge()));
        ViewBindUtils.RxClicks(holder.binding.rankIv, o -> {
            PageJumpUtils.jumpToProfile(item.getUserId());
        });
        ViewBindUtils.setText(holder.binding.tvIntimacy, "亲密值" + item.getIntimacy());

    }


}
