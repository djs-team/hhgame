package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.RoomSearchs;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemSearchRoomBinding;

/**
 * Created by JUN on 2019/4/19
 */
public class RoomSearchAdapter extends BaseBindingAdapter<RoomSearchs.RoomMsgBean, ItemSearchRoomBinding> {

    public RoomSearchAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_search_room;
    }

    @Override
    protected void bind(BindingViewHolder<ItemSearchRoomBinding> holder, RoomSearchs.RoomMsgBean item) {
        GlideUtils.circleImage(holder.binding.avatarIv, item.getRoom_image(), R.drawable.ic_place, R.drawable.ic_place);
        holder.binding.nickTv.setText(item.getRoom_name());
        String roomId = item.getRoom_id();
        if (!TextUtils.isEmpty(item.getPretty_room_id())) {
            roomId = item.getPretty_room_id();
        }
        holder.binding.uidTv.setText("ID: " + roomId);
        holder.binding.roomTagTv.setText(item.getRoom_type());
        holder.setVisible(holder.binding.roomTagTv, !TextUtils.isEmpty(item.getRoom_type()));
    }
}
