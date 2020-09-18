package com.deepsea.mua.voice.adapter;

import android.content.Context;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.FollowFanBean;
import com.deepsea.mua.stub.entity.socket.receive.DisableMsgData;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemForbiddenTimeBinding;

/**
 * Created by JUN on 2019/5/5
 */
public class ForbiddenTimeAdapter extends BaseBindingAdapter<DisableMsgData, ItemForbiddenTimeBinding> {


    public ForbiddenTimeAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_forbidden_time;
    }

    private int defaultPos = 0;


    @Override
    protected void bind(BindingViewHolder<ItemForbiddenTimeBinding> holder, DisableMsgData item) {
        holder.binding.ivTimeSwitch.setSelected(holder.getLayoutPosition() == defaultPos);
        ViewBindUtils.RxClicks(holder.binding.consGroup, o -> {
            defaultPos = holder.getLayoutPosition();
            notifyDataSetChanged();
        });
        ViewBindUtils.setText(holder.binding.tvTime, item.getTime() + "分钟");

    }

    public int getDisableMsgId() {
        return getItem(defaultPos).getId();
    }

}
