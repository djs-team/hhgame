package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemFansBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.FollowFanBean;
import com.deepsea.mua.stub.utils.ProfileUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/5
 */
public class FansAdapter extends BaseBindingAdapter<FollowFanBean.UserInfoBean, ItemFansBinding> {

    private OnFollowListener mListener;

    public FansAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_fans;
    }

    @Override
    protected void bind(BindingViewHolder<ItemFansBinding> holder, FollowFanBean.UserInfoBean item) {
        GlideUtils.circleImage(holder.binding.avatarIv, item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        holder.binding.nickTv.setText(item.getNickname());
        holder.binding.vipLevelTv.setText("V" + item.getIs_vip());
        holder.setVisible(holder.binding.vipLevelTv, item.getIs_vip() > 0);
        holder.binding.descTv.setText(item.getIntro());

        boolean isFollow = "1".equals(item.getStatus());
        holder.binding.followTv.setTextColor(isFollow ? 0xFF9B9B9B : 0xFFFFFFFF);
        holder.binding.followTv.setText(isFollow ? "取消关注" : "关注");
        holder.binding.followTv.setSelected(isFollow);
        holder.binding.followTv.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onFollow(item.getUserid(), isFollow, holder.getLayoutPosition());
            }
        });

        SexResUtils.setSexImg(holder.binding.sexIv, item.getSex());
        holder.binding.infoTv.setText(ProfileUtils.getProfile(item.getAge(), item.getCity(), item.getStature()));
        ViewBindUtils.setVisible(holder.binding.ivOnmicro,item.getState().equals("3"));
        if (item.getState().equals("3")) {
            GlideUtils.loadGif(holder.binding.ivOnmicro, R.drawable.icon_is_onmicro);
        }
    }

    public void setOnFollowListener(OnFollowListener listener) {
        this.mListener = listener;
    }

    public interface OnFollowListener {

        /**
         * @param uid
         * @param isFollow
         * @param pos
         */
        void onFollow(String uid, boolean isFollow, int pos);
    }
}
