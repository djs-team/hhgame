package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.entity.socket.receive.SongRankData;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemShareChoseMicroBinding;
import com.deepsea.mua.voice.databinding.ItemSongRankBinding;

import java.util.List;

/**
 * Created by JUN on 2019/4/23
 */
public class NotifyHelpAdapter extends BaseBindingAdapter<RoomData.MicroInfosBean, ItemShareChoseMicroBinding> {


    public NotifyHelpAdapter(Context context) {
        super(context);
    }


    @Override
    public void setNewData(List<RoomData.MicroInfosBean> data) {
        super.setNewData(data);

    }

    @Override
    public void addData(List<RoomData.MicroInfosBean> data) {
        super.addData(data);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_share_chose_micro;
    }


    @Override
    protected void bind(BindingViewHolder<ItemShareChoseMicroBinding> holder, RoomData.MicroInfosBean item) {
        WsUser user=item.getUser();

        StringBuilder sb = new StringBuilder();
        if (user.getAge() > 0) {
            sb.append(user.getAge()).append("岁");
        }
        if (user.getStature() > 0) {
            sb.append(sb.length() == 0 ? "" : " | ").append(user.getStature()).append("cm");
        }
        if (!TextUtils.isEmpty(user.getCity())) {
            sb.append(sb.length() == 0 ? "" : " | ").append(user.getCity());
        }
        ViewBindUtils.setText(holder.binding.tvDesc,sb.toString());
        ViewBindUtils.setText(holder.binding.tvName,user.getName());
        GlideUtils.loadImage(holder.binding.ivAvatar,user.getHeadImageUrl(),R.drawable.ic_place_avatar,R.drawable.ic_place_avatar);


    }

}