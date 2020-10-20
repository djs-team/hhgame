package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.RoomSearchs;
import com.deepsea.mua.stub.utils.StateUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.span.LevelResUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemSearchPeopleBinding;

/**
 * Created by JUN on 2019/4/19
 */
public class MemberSearchAdapter extends BaseBindingAdapter<RoomSearchs.MemberListBean, ItemSearchPeopleBinding> {

    public MemberSearchAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_search_people;
    }

    @Override
    protected void bind(BindingViewHolder<ItemSearchPeopleBinding> holder, RoomSearchs.MemberListBean item) {
        GlideUtils.circleImage(holder.binding.avatarIv, item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        holder.binding.nickTv.setText(item.getNickname());
        holder.binding.uidTv.setText("ID: " + item.getPretty_id());
        //sex
        if (TextUtils.equals(item.getSex(), "1")) {
            holder.setVisible(holder.binding.rlSex, true);
            holder.binding.sexIv.setImageResource(R.drawable.icon_find_sex_man);
            holder.binding.rlSex.setBackgroundResource(com.deepsea.mua.stub.R.drawable.bg_sex_man);
        } else if (TextUtils.equals(item.getSex(), "2")) {
            holder.binding.rlSex.setBackgroundResource(com.deepsea.mua.stub.R.drawable.bg_sex_women);
            holder.setVisible(holder.binding.rlSex, true);
            holder.binding.sexIv.setImageResource(R.drawable.icon_find_sex_women);
        } else {
            holder.setVisible(holder.binding.rlSex, false);
        }
        ViewBindUtils.setText(holder.binding.tvOnline, item.getOnline_str());
        if (item.getOnline_str() != null) {
            if (item.getOnline_str().equals("离线")) {
                holder.binding.tvOnline.setTextColor(Color.parseColor("#ff818181"));
            } else if (item.getOnline_str().equals("刚刚在线")) {
                holder.binding.tvOnline.setTextColor(Color.parseColor("#fffbd711"));
            } else if (item.getOnline_str().equals("在线")) {
                holder.binding.tvOnline.setTextColor(Color.parseColor("#ff0fe770"));
            }
        }
        String location = "";
        location += TextUtils.isEmpty(item.getCity()) ? "" : item.getCity();
        location += TextUtils.isEmpty(item.getCity_two()) ? "" : item.getCity_two();
        ViewBindUtils.setText(holder.binding.tvLocation, location);
        ViewBindUtils.setVisible(holder.binding.ivLocation, !TextUtils.isEmpty(location.trim()));
        ViewBindUtils.setText(holder.binding.tvAge, item.getAge() == 0 ? "" : String.valueOf(item.getAge()));
        StateUtils.setRoomState(holder.binding.tvState, item.getState());
//        boolean chatState = !TextUtils.isEmpty(item.getState()) && item.getState().equals("6");
//        ViewBindUtils.setVisible(holder.binding.tvState, chatState);
    }
}
