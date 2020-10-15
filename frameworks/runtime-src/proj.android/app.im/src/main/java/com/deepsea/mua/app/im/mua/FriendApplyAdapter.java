package com.deepsea.mua.app.im.mua;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ItemFriendApplyBinding;
import com.deepsea.mua.app.im.databinding.ItemSystemMsgBinding;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.ApplyFriendBean;
import com.deepsea.mua.stub.entity.DynamicDetailReplylistBean;
import com.deepsea.mua.stub.entity.DynamicMsgExt;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.TimeUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.Locale;

/**
 * Created by JUN on 2019/8/27
 */
public class FriendApplyAdapter extends BaseBindingAdapter<ApplyFriendBean, ItemFriendApplyBinding> {

    private String type;

    public FriendApplyAdapter(Context context, String type) {
        super(context);
        this.type = type;
    }

    private OnMyClickListener myClickListener;

    public void setMyClickListener(OnMyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface OnMyClickListener {

        void onAgreeClick(int pos, ApplyFriendBean applyFriendBean);

        void onRefuseClick(int pos, ApplyFriendBean applyFriendBean);

        void onHeadClick(int pos, ApplyFriendBean applyFriendBean);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_friend_apply;
    }


    @Override
    protected void bind(BindingViewHolder<ItemFriendApplyBinding> holder, ApplyFriendBean item) {
        GlideUtils.circleImage(holder.binding.ivPhoto, item.getAvatar(), com.deepsea.mua.stub.R.drawable.ic_place, com.deepsea.mua.stub.R.drawable.ic_place);
        String state = item.getState();
        if (!TextUtils.isEmpty(state)) {
            if (state.equals("2") || state.equals("3")) {
                //相亲中
                ViewBindUtils.setVisible(holder.binding.tvState, true);
                holder.binding.tvState.setWithBackgroundColor(Color.parseColor("#773BE7"));
                ViewBindUtils.setText(holder.binding.tvState, "相亲中");
            } else if (state.equals("4") || state.equals("5")) {
                ViewBindUtils.setVisible(holder.binding.tvState, true);
                holder.binding.tvState.setWithBackgroundColor(Color.parseColor("#EF51B2"));
                ViewBindUtils.setText(holder.binding.tvState, "热聊中");
            } else if (state.equals("6")) {
                ViewBindUtils.setVisible(holder.binding.tvState, true);
                holder.binding.tvState.setWithBackgroundColor(Color.parseColor("#FDC003"));
                ViewBindUtils.setText(holder.binding.tvState, "开播中");
            } else {
                ViewBindUtils.setVisible(holder.binding.tvState, false);
            }
        }
        ViewBindUtils.setText(holder.binding.tvName, item.getNickname());
        SexResUtils.setSexImgInFindPage(holder.binding.rlSex, holder.binding.sexIv, item.getSex());
        ViewBindUtils.setText(holder.binding.tvAge, item.getAge() == 0 ? "" : String.valueOf(item.getAge()));
        String location = "";
        location += TextUtils.isEmpty(item.getCity()) ? "" : item.getCity();
        location += TextUtils.isEmpty(item.getCity_two()) ? "" : item.getCity_two();
        ViewBindUtils.setText(holder.binding.tvLocation, location);
        ViewBindUtils.setVisible(holder.binding.ivLocation, !TextUtils.isEmpty(location));
        ViewBindUtils.setVisible(holder.binding.tvLocation, !TextUtils.isEmpty(location));
        ViewBindUtils.setText(holder.binding.tvDesc, item.getIntro());
        ViewBindUtils.setVisible(holder.binding.tvDesc, !TextUtils.isEmpty(item.getIntro()));
        ViewBindUtils.setText(holder.binding.tvDate, TimeUtils.time2Date(item.getCtime(), "MM-dd"));
        ViewBindUtils.setText(holder.binding.tvApplyDesc, type.equals("1") ? "请求和你成为好友" : "申请成为ta的好友");
        ViewBindUtils.setText(holder.binding.tvId, "ID:" + item.getUser_id());
        String mStatus = item.getMstatus();
        if (mStatus.equals("1")) {
            ViewBindUtils.setText(holder.binding.tvOverdue, type.equals("1") ? "已添加" : "已成为好友");
            setAddfriendVisible(holder, false);
        } else if (mStatus.equals("2")) {
            String myApplyDesc = TextUtils.isEmpty(item.getGift_id()) ? "对方已经拒绝" : "对方已经拒绝|礼物已退回";
            ViewBindUtils.setText(holder.binding.tvOverdue, type.equals("1") ? "已拒绝" : myApplyDesc);
            setAddfriendVisible(holder, false);

        } else if (mStatus.equals("3")) {
            ViewBindUtils.setText(holder.binding.tvOverdue, type.equals("1") ? "申请中" : "等待对方通过");
            setAddfriendVisible(holder, type.equals("1") ? true : false);
        } else {
            String myApplyDesc = TextUtils.isEmpty(item.getGift_id()) ? "对方已经拒绝" : "对方已经拒绝|礼物已退回";
            ViewBindUtils.setText(holder.binding.tvOverdue, type.equals("1") ? "已过期" : myApplyDesc);
            setAddfriendVisible(holder, false);
        }
        ViewBindUtils.RxClicks(holder.binding.tvAgree, o -> {
            if (myClickListener != null) {
                myClickListener.onAgreeClick(holder.getLayoutPosition(), item);
            }
        });
        ViewBindUtils.RxClicks(holder.binding.tvDisagree, o -> {
            if (myClickListener != null) {
                myClickListener.onRefuseClick(holder.getLayoutPosition(), item);
            }
        });
        ViewBindUtils.RxClicks(holder.binding.ivPhoto, o -> {
            if (myClickListener != null) {
                myClickListener.onHeadClick(holder.getLayoutPosition(), item);
            }
        });
        ViewBindUtils.RxClicks(holder.binding.consGroup, o -> {
            PageJumpUtils.jumpToProfile(item.getUser_id());
        });

    }

    private void setAddfriendVisible(BindingViewHolder<ItemFriendApplyBinding> holder, boolean isVisible) {
        ViewBindUtils.setVisible(holder.binding.tvOverdue, !isVisible);
        ViewBindUtils.setVisible(holder.binding.tvAgree, isVisible);
        ViewBindUtils.setVisible(holder.binding.tvDisagree, isVisible);
    }


}
