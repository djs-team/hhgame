package com.deepsea.mua.voice.adapter;

import android.content.Context;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.RoomTags;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemRoomTagBinding;

import java.util.List;

/**
 * Created by JUN on 2019/4/15
 */
public class RoomTagsAdapter extends BaseBindingAdapter<RoomTags.TagListBean, ItemRoomTagBinding> {

    private int mSelectPos = 0;

    public RoomTagsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_room_tag;
    }

    @Override
    protected void bind(BindingViewHolder<ItemRoomTagBinding> holder, RoomTags.TagListBean item) {
        int pos = holder.getAdapterPosition();
        holder.binding.tagTv.setText(item.getTag_name());
        holder.binding.tagTv.setSelected(mSelectPos == pos);
        holder.binding.tagTv.setTextColor(mSelectPos == pos ? 0xFFFFFFFF : 0xFF333333);
    }

    @Override
    public void setNewData(List<RoomTags.TagListBean> data) {
        mSelectPos = 0;
        super.setNewData(data);
    }

    public void setSelectPos(int pos) {
        this.mSelectPos = pos;
        notifyDataSetChanged();
    }

    public String getSelectTag() {
        return getItem(mSelectPos).getTag_id();
    }
}
