package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.MineRooms;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemMineRoomBinding;

/**
 * Created by JUN on 2019/4/19
 */
public class MineRoomsAdapter extends BaseBindingAdapter<MineRooms.MyroomListBean, ItemMineRoomBinding> {

    public MineRoomsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_mine_room;
    }

    @Override
    protected void bind(BindingViewHolder<ItemMineRoomBinding> holder, MineRooms.MyroomListBean item) {
        GlideUtils.circleImage(holder.binding.avatarIv, item.getRoom_image(), R.drawable.ic_place, R.drawable.ic_place);
        holder.binding.nickTv.setText(item.getRoom_name());
        holder.binding.uidTv.setText(item.getNickname());
        holder.binding.roomTagTv.setText(item.getRoom_type());
        holder.setVisible(holder.binding.roomTagTv, !TextUtils.isEmpty(item.getRoom_type()));
        if (item.getRoom_type().contains("相亲")){
            ViewBindUtils.setBackgroundRes(holder.binding.roomTagTv,R.drawable.icon_room_mine_blinddate);
        }else if (item.getRoom_type().contains("交友")){
            ViewBindUtils.setBackgroundRes(holder.binding.roomTagTv,R.drawable.icon_room_mine_makefriend);
        }
        holder.binding.renqiTv.setText(item.getVisitor_number() + "人气");
        holder.setVisible(holder.binding.lockIv, "1".equals(item.getRoom_lock()));
        holder.setVisible(holder.binding.liveIv, "1".equals(item.getIs_live()));

        int position = holder.getAdapterPosition();
        holder.setVisible(holder.binding.topLine, position != 0);
        holder.setVisible(holder.binding.bottomLine, item.isOwner());
    }
}
