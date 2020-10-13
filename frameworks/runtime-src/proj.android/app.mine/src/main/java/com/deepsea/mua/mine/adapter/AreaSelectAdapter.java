package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemAreaBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.AreaVo;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/5
 */
public class AreaSelectAdapter extends BaseBindingAdapter<AreaVo, ItemAreaBinding> {

    public AreaSelectAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_area;
    }

    @Override
    protected void bind(BindingViewHolder<ItemAreaBinding> holder, AreaVo item) {
        ViewBindUtils.setText(holder.binding.tvArea, item.getName());
    }


}
