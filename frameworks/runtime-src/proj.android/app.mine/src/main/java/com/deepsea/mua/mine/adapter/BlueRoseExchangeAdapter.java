package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemBlueRoseExchangeBinding;
import com.deepsea.mua.mine.databinding.ItemIncomeDetailsBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.BlueRoseExchange;
import com.deepsea.mua.stub.entity.IncomeListItemBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/7
 */
public class BlueRoseExchangeAdapter extends BaseBindingAdapter<BlueRoseExchange, ItemBlueRoseExchangeBinding> {


    public BlueRoseExchangeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_blue_rose_exchange;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_blue_rose_exchange;
    }

    @Override
    protected void bind(BindingViewHolder<ItemBlueRoseExchangeBinding> holder, BlueRoseExchange item) {
      GlideUtils.loadImage(holder.binding.giftIv,item.getGift_image());
      ViewBindUtils.setText(holder.binding.giftDesc,"蓝玫瑰："+item.getGift_blue_coin()+"\n"+"礼物数量："+item.getPack_num());


    }
}
