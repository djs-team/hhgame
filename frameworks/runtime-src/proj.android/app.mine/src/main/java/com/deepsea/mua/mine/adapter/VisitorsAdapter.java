package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemVisitorsBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.VisitorBean;
import com.deepsea.mua.stub.utils.ProfileUtils;
import com.deepsea.mua.stub.utils.SexResUtils;

/**
 * Created by JUN on 2019/5/7
 */
public class VisitorsAdapter extends BaseBindingAdapter<VisitorBean.HistoryListBean, ItemVisitorsBinding> {

    public VisitorsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_visitors;
    }

    @Override
    protected void bind(BindingViewHolder<ItemVisitorsBinding> holder, VisitorBean.HistoryListBean item) {
        GlideUtils.circleImage(holder.binding.avatarIv, item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        holder.binding.nickTv.setText(item.getNickname());
        holder.binding.vipLevelTv.setText("V" + item.getIs_vip());
        holder.setVisible(holder.binding.vipLevelTv, item.getIs_vip() > 0);
        holder.binding.descTv.setText(item.getIntro());
        holder.binding.onlineTimeTv.setText(item.getCtime());
        SexResUtils.setSexImg(holder.binding.sexIv, item.getSex());
        holder.binding.infoTv.setText(ProfileUtils.getProfile(item.getAge(), item.getCity(), item.getStature()));
    }
}
