package com.deepsea.mua.voice.adapter;

import android.content.Context;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.RoomTagListBean;
import com.deepsea.mua.stub.entity.RoomTags;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemRoomTagBinding;

import java.util.List;

/**
 * Created by JUN on 2019/4/15
 */
public class RoomNewTagsAdapter extends BaseBindingAdapter<RoomTagListBean.ModeListBean.ModeTagsBean, ItemRoomTagBinding> {

    private int mSelectPos = 0;

    public RoomNewTagsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_room_tag;
    }

    @Override
    protected void bind(BindingViewHolder<ItemRoomTagBinding> holder, RoomTagListBean.ModeListBean.ModeTagsBean item) {
        int pos = holder.getAdapterPosition();
        holder.binding.tagTv.setText(item.getTag_name());
        holder.binding.tagTv.setSelected(mSelectPos == pos);
        holder.binding.tagTv.setTextColor(mSelectPos == pos ? 0xFFFFFFFF : 0xFF333333);
    }

    @Override
    public void setNewData(List<RoomTagListBean.ModeListBean.ModeTagsBean> data) {
        mSelectPos = 0;
        super.setNewData(data);
    }

    public void setSelectPos(int pos) {
        this.mSelectPos = pos;
        notifyDataSetChanged();
    }

    public String getSelectTag() {
        return getItem(mSelectPos).getTags_id();
    }
}
