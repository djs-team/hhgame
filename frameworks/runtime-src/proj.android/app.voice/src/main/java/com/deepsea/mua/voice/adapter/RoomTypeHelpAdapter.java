package com.deepsea.mua.voice.adapter;

import android.content.Context;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.RoomModeHelpVo;
import com.deepsea.mua.stub.entity.RoomTagListBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemRoomModeHelpBinding;
import com.deepsea.mua.voice.databinding.ItemRoomTypeSelectBinding;

/**
 * Created by JUN on 2019/10/14
 */
public class RoomTypeHelpAdapter extends BaseBindingAdapter<RoomModeHelpVo, ItemRoomModeHelpBinding> {


    public RoomTypeHelpAdapter(Context context) {
        super(context);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_room_mode_help;
    }


    @Override
    protected void bind(BindingViewHolder<ItemRoomModeHelpBinding> holder, RoomModeHelpVo item) {
        ViewBindUtils.setText(holder.binding.tvTagName, item.getTag());
        ViewBindUtils.setText(holder.binding.tvTagValue, item.getValue());
    }
}
