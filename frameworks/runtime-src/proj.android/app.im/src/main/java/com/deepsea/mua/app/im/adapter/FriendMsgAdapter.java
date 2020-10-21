package com.deepsea.mua.app.im.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.widget.TextView;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ItemFriendListBinding;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.im.utils.EaseCommonUtils;
import com.deepsea.mua.im.utils.EaseSmileUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.FriendInfoBean;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.StateUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.util.DateUtils;

import java.util.Date;

/**
 * Created by JUN on 2019/5/7
 */
public class FriendMsgAdapter extends BaseBindingAdapter<FriendInfoBean, ItemFriendListBinding> {


    public FriendMsgAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_friend_list;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_friend_list;
    }

    @Override
    protected void bind(BindingViewHolder<ItemFriendListBinding> holder, FriendInfoBean item) {
        GlideUtils.circleImage(holder.binding.ivPhoto, item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        SexResUtils.setSexImgInFindPage(holder.binding.rlSex, holder.binding.sexIv, item.getSex());
        ViewBindUtils.setText(holder.binding.ageTv, String.valueOf(item.getAge()));
        ViewBindUtils.setVisible(holder.binding.ageTv, item.getAge() != 0);
        holder.binding.tvUsername.setText(item.getNickname());
        String city = item.getCity();
        ViewBindUtils.setText(holder.binding.tvLocation, !TextUtils.isEmpty(city) ? city : "");
        ViewBindUtils.setVisible(holder.binding.ivLocation, !TextUtils.isEmpty(city));
        if (item.getTime() != 0) {
            holder.binding.tvTime.setText(DateUtils.getTimestampString(new Date(item.getTime())));
        }
        if (item.getLastMsg() != null) {
            holder.binding.tvLastMessage.setText(EaseSmileUtils.getSmiledText(mContext, EaseCommonUtils.getMessageDigest(item.getLastMsg(), mContext)),
                    TextView.BufferType.SPANNABLE);
        }
        if (item.getUnReadCount() > 0) {
            ViewBindUtils.setVisible(holder.binding.unreadMsgNumber, true);
            ViewBindUtils.setText(holder.binding.unreadMsgNumber, item.getUnReadCount() + "");
        } else {
            ViewBindUtils.setVisible(holder.binding.unreadMsgNumber, false);
        }

        String str = item.getOnline_str();
        if (str.contains("离线") || str.contains("刚刚在线")) {
            ViewBindUtils.setVisible(holder.binding.tvStateDesc, false);
            ViewBindUtils.setVisible(holder.binding.tvStateBg, true);
            holder.binding.tvStateBg.setWithBackgroundColor(Color.parseColor("#b5b5b6"));
        } else if (str.contains("在线")) {
            ViewBindUtils.setVisible(holder.binding.tvStateDesc, false);
            ViewBindUtils.setVisible(holder.binding.tvStateBg, true);
            holder.binding.tvStateBg.setWithBackgroundColor(Color.parseColor("#10E770"));
        } else {
            ViewBindUtils.setVisible(holder.binding.tvStateDesc, true);
            ViewBindUtils.setVisible(holder.binding.tvStateBg, false);
            StateUtils.setState(holder.binding.tvStateDesc, item.getOnline_str());
        }


    }
}
