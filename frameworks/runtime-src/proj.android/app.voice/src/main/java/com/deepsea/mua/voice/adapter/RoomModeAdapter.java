package com.deepsea.mua.voice.adapter;

import android.content.Context;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.RoomTags;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemRoomModeBinding;

/**
 * Created by JUN on 2019/4/15
 */
public class RoomModeAdapter extends BaseBindingAdapter<RoomTags.ModeListBean, ItemRoomModeBinding> {

    private int mSelectPos = 0;

    public RoomModeAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_room_mode;
    }

    @Override
    protected void bind(BindingViewHolder<ItemRoomModeBinding> holder, RoomTags.ModeListBean item) {
        int pos = holder.getAdapterPosition();
        holder.setVisible(holder.binding.selectIv, mSelectPos == pos);
        holder.binding.modeTv.setText(item.getRoom_mode());
    }

    public void setSelectPos(int pos) {
        this.mSelectPos = pos;
        notifyDataSetChanged();
    }

    public String getSelectModeId() {
        return getItem(mSelectPos).getMode_id();
    }
    public String getSelectModeName() {
        return getItem(mSelectPos).getRoom_mode();
    }
}
