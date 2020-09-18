package com.deepsea.mua.app.im.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ItemFriendRequestBinding;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.ApplyFriendBean;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.StateUtils;
import com.deepsea.mua.stub.utils.TimeUtils;

/**
 * Created by JUN on 2019/5/7
 */
public class FriendRequestAdapter extends BaseBindingAdapter<ApplyFriendBean, ItemFriendRequestBinding> {

    private OnMyClickListener myClickListener;

    public void setMyClickListener(OnMyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface OnMyClickListener {

        void onAgreeClick(int pos, ApplyFriendBean applyFriendBean);

        void onRefuseClick(int pos, ApplyFriendBean applyFriendBean);

    }

    public FriendRequestAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_friend_request;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_friend_request;
    }

    @Override
    protected void bind(BindingViewHolder<ItemFriendRequestBinding> holder, ApplyFriendBean item) {
        GlideUtils.circleImage(holder.binding.ivFriendPhoto, item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        holder.binding.ivFriendPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageJumpUtils.jumpToProfile(item.getUser_id());
            }
        });

        holder.binding.tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myClickListener != null) {
                    myClickListener.onAgreeClick(holder.getLayoutPosition(), item);
                }
            }
        });
        holder.binding.tvRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myClickListener != null) {
                    myClickListener.onRefuseClick(holder.getLayoutPosition(), item);
                }
            }
        });
        holder.binding.tvAgree2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myClickListener != null) {
                    myClickListener.onAgreeClick(holder.getLayoutPosition(), item);
                }
            }
        });
        holder.binding.tvRefuse2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myClickListener != null) {
                    myClickListener.onRefuseClick(holder.getLayoutPosition(), item);
                }
            }
        });
        //头像
        GlideUtils.circleImage(holder.binding.ivFriendPhoto, item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        //名称
        holder.binding.tvFriendName.setText(item.getNickname());
        //性别
        SexResUtils.setSexImg(holder.binding.ivFriendSex, item.getSex());
        //年龄地址
        StringBuilder info = new StringBuilder();
        info.append(item.getAge());
        info.append("岁");
        if (!TextUtils.isEmpty(item.getCity())) {
            info.append(" |");
            info.append(" " + item.getCity());
        }
        if (item.getState() != null) {
            String state = item.getState();
            if (!state.equals("0")) {
                info.append(" |");
                info.append(" " + StateUtils.getState(state));
            }
        }

        holder.binding.tvFriendAgeAndArea.setText(info.toString());
        String time = TimeUtils.time2Date(item.getCtime(), "");
        holder.binding.tvFriendRequestTime.setText(time);
        //礼物
        String giftId = item.getGift_id();
        if (giftId.equals("0")) {
            //没有礼物
            holder.binding.rlFriendGift.setVisibility(View.GONE);
            holder.binding.rlAddfriendGroup.setVisibility(View.VISIBLE);
        } else {
            //有礼物
            holder.binding.rlFriendGift.setVisibility(View.VISIBLE);
            holder.binding.rlAddfriendGroup.setVisibility(View.GONE);
            holder.binding.tvFriendWords.setText(item.getMsg());
            GlideUtils.loadImage(holder.binding.ivFiendGift, item.getGift_image());
        }
        if (item.getMstatus().equals("1")) {
            holder.binding.tvAgree.setVisibility(View.GONE);
            holder.binding.tvAgree2.setVisibility(View.GONE);
            holder.binding.tvRefuse.setVisibility(View.GONE);
            holder.binding.tvRefuse2.setVisibility(View.GONE);
            holder.binding.tvStatus.setText("已同意");
            holder.binding.tvStatu2.setText("已同意");
            holder.binding.tvStatu2.setVisibility(View.VISIBLE);
            holder.binding.tvStatus.setVisibility(View.VISIBLE);
        } else if (item.getMstatus().equals("2")) {
            holder.binding.tvAgree.setVisibility(View.GONE);
            holder.binding.tvAgree2.setVisibility(View.GONE);
            holder.binding.tvRefuse.setVisibility(View.GONE);
            holder.binding.tvRefuse2.setVisibility(View.GONE);
            holder.binding.tvStatus.setText("已拒绝");
            holder.binding.tvStatu2.setText("已拒绝");
            holder.binding.tvStatu2.setVisibility(View.VISIBLE);
            holder.binding.tvStatus.setVisibility(View.VISIBLE);
        } else {
            holder.binding.tvAgree.setVisibility(View.VISIBLE);
            holder.binding.tvAgree2.setVisibility(View.VISIBLE);
            holder.binding.tvRefuse.setVisibility(View.VISIBLE);
            holder.binding.tvRefuse2.setVisibility(View.VISIBLE);
            holder.binding.tvStatu2.setVisibility(View.GONE);
            holder.binding.tvStatus.setVisibility(View.GONE);
        }

    }
}
