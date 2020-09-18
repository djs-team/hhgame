package com.deepsea.mua.voice.adapter;

import android.content.Context;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.RoomTagListBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemRoomFunctionSelectBinding;
import com.deepsea.mua.voice.databinding.ItemRoomTypeSelectBinding;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by JUN on 2019/10/14
 */
public class RoomTypeSelectAdapter extends BaseBindingAdapter<RoomTagListBean.ModeListBean, ItemRoomTypeSelectBinding> {


    public RoomTypeSelectAdapter(Context context) {
        super(context);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_room_type_select;
    }

    private int selected = 0;

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    @Override
    protected void bind(BindingViewHolder<ItemRoomTypeSelectBinding> holder, RoomTagListBean.ModeListBean item) {
        if (selected == holder.getLayoutPosition()) {
            holder.binding.ivFunction.setSelected(true);
        } else {
            holder.binding.ivFunction.setSelected(false);
        }
        ViewBindUtils.setText(holder.binding.tvFunctionName,item.getRoom_mode());


    }
    public String getSelectModeId() {
        return getItem(selected).getMode_id();
    }
}
