package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemExchangeMdDetailsBinding;
import com.deepsea.mua.mine.databinding.ItemIncomeDetailsBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.ExchangeMdDetailItem;
import com.deepsea.mua.stub.entity.IncomeListItemBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/7
 */
public class ExchangeMdDetailsAdapter extends BaseBindingAdapter<ExchangeMdDetailItem, ItemExchangeMdDetailsBinding> {


    public ExchangeMdDetailsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_exchange_md_details;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_exchange_md_details;
    }

    @Override
    protected void bind(BindingViewHolder<ItemExchangeMdDetailsBinding> holder, ExchangeMdDetailItem item) {
        ViewBindUtils.setText(holder.binding.balanceTv,"-￥"+item.getDiamond());
        ViewBindUtils.setText(holder.binding.numTv,"+"+item.getCoin()+"玫瑰");

    }
}
