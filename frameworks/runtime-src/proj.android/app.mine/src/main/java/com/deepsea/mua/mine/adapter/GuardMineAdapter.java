package com.deepsea.mua.mine.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemGuardMineBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.ApplyFriendBean;
import com.deepsea.mua.stub.entity.GuardInfo;
import com.deepsea.mua.stub.entity.GuardInfoBean;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/8/27
 */
public class GuardMineAdapter extends BaseBindingAdapter<GuardInfoBean.GuardMemberlistBean, ItemGuardMineBinding> {
    private OnMyClickListener mListener;

    public void setmListener(OnMyClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnMyClickListener {

        void renew(String userId);

        void autoRenew(String is_auto);
    }

    private String type;

    public GuardMineAdapter(Context context, String type) {
        super(context);
        this.type = type;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_guard_mine;

    }


    @Override
    protected void bind(BindingViewHolder<ItemGuardMineBinding> holder, GuardInfoBean.GuardMemberlistBean item) {
        String state = item.getState();
        if (!TextUtils.isEmpty(state)) {
            ViewBindUtils.setVisible(holder.binding.tvState, true);
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
        } else {
            ViewBindUtils.setVisible(holder.binding.tvState, false);
        }
        ViewBindUtils.setText(holder.binding.tvName, item.getNickname());
        SexResUtils.setSexImgInFindPage(holder.binding.rlSex, holder.binding.sexIv, item.getSex());
        ViewBindUtils.setText(holder.binding.tvAge, item.getAge() == 0 ? "" : String.valueOf(item.getAge()));
        String location = "";
        location += TextUtils.isEmpty(item.getCity()) ? "" : item.getCity();
        location += TextUtils.isEmpty(item.getCity_two()) ? "" : item.getCity_two();
        ViewBindUtils.setVisible(holder.binding.ivLocation, !TextUtils.isEmpty(location.trim()));
        ViewBindUtils.setText(holder.binding.tvLocation, location);
        ViewBindUtils.setText(holder.binding.tvDesc, item.getIntro());
        ViewBindUtils.setText(holder.binding.tvDateEnd, "守护到期时间：" + item.getEnd_time());
        ViewBindUtils.setText(holder.binding.tvIntimacyValue, "亲密值：" + item.getIntimacy());
        ViewBindUtils.setText(holder.binding.tvDueDate, "还有" + item.getCountdown_day() + "天到期");
//        String is_auto = "";//是否自动续费  1:是   2：否
//        if (is_auto.equals("1")) {
//            ViewBindUtils.setText(holder.binding.tvAutoRenew, "关闭自助续费");
//        } else {
//            ViewBindUtils.setText(holder.binding.tvAutoRenew, "开通自助续费");
//        }
        ViewBindUtils.RxClicks(holder.binding.tvAutoRenew, o -> {
            if (mListener != null) {
//                mListener.autoRenew(is_auto);
            }
        });
        ViewBindUtils.RxClicks(holder.binding.ivPhoto, o -> {
            PageJumpUtils.jumpToProfile(item.getUserId());
        });
        ViewBindUtils.RxClicks(holder.binding.tvRenew, o -> {
            if (mListener != null) {
                mListener.renew(item.getUserId());
            }
        });
        ViewBindUtils.setVisible(holder.binding.tvRenew, type.equals("1"));
        ViewBindUtils.setVisible(holder.binding.tvAutoRenew, type.equals("1"));
        GlideUtils.circleImage(holder.binding.ivPhoto, item.getAvatar(), R.drawable.ic_place, R.drawable.ic_place);


    }

}
