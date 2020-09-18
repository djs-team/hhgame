package com.deepsea.mua.app.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ItemFriendListBinding;
import com.deepsea.mua.app.im.databinding.ItemFriendMineBinding;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.im.utils.EaseCommonUtils;
import com.deepsea.mua.im.utils.EaseSmileUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.FriendInfoBean;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.hyphenate.util.DateUtils;

import java.util.Date;

/**
 * Created by JUN on 2019/5/7
 */
public class FriendListAdapter extends BaseBindingAdapter<FriendInfoBean, ItemFriendMineBinding> {


    public FriendListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_friend_mine;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_friend_mine;
    }

    @Override
    protected void bind(BindingViewHolder<ItemFriendMineBinding> holder, FriendInfoBean item) {
        GlideUtils.circleImage(holder.binding.ivPhoto, item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        SexResUtils.setSexImgInFindPage(holder.binding.rlSex, holder.binding.sexIv, item.getSex());
        ViewBindUtils.setText(holder.binding.ageTv, String.valueOf(item.getAge()));
        ViewBindUtils.setVisible(holder.binding.ageTv, item.getAge() != 0);
        holder.binding.tvUsername.setText(item.getNickname());
        String city = item.getCity();
        ViewBindUtils.setText(holder.binding.tvLocation, !TextUtils.isEmpty(city) ? city : "");
        ViewBindUtils.setVisible(holder.binding.ivLocation, !TextUtils.isEmpty(city));
        holder.binding.tvLastMessage.setText(item.getIntro());
        if (item.getUnReadCount() > 0) {
            ViewBindUtils.setVisible(holder.binding.unreadMsgNumber, true);
            ViewBindUtils.setText(holder.binding.unreadMsgNumber, item.getUnReadCount() + "");
        } else {
            ViewBindUtils.setVisible(holder.binding.unreadMsgNumber, false);
        }
        ViewBindUtils.setText(holder.binding.tvOnline, item.getOnline_str());
        if (!TextUtils.isEmpty(item.getOnline_str())){
            if (item.getOnline_str().equals("在线")){
                ViewBindUtils.setTextColor(holder.binding.tvOnline, com.deepsea.mua.stub.R.color.color_FE770);
            }else if (item.getOnline_str().equals("刚刚在线")){
                ViewBindUtils.setTextColor(holder.binding.tvOnline, com.deepsea.mua.stub.R.color.color_FBD711);
            }else if (item.getOnline_str().equals("离线")){
                ViewBindUtils.setTextColor(holder.binding.tvOnline, com.deepsea.mua.stub.R.color.color_818181);

            }

        }


    }
}
