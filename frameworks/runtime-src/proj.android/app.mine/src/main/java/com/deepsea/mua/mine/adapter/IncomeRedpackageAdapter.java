package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemIncomeDetailsBinding;
import com.deepsea.mua.mine.databinding.ItemIncomeRedpackageDetailsBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.IncomeListItemBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/7
 */
public class IncomeRedpackageAdapter extends BaseBindingAdapter<IncomeListItemBean, ItemIncomeRedpackageDetailsBinding> {


    public IncomeRedpackageAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_income_redpackage_details;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_income_redpackage_details;
    }

    @Override
    protected void bind(BindingViewHolder<ItemIncomeRedpackageDetailsBinding> holder, IncomeListItemBean item) {
        ViewBindUtils.setText(holder.binding.tvType, item.getType());
        ViewBindUtils.setText(holder.binding.tvRedpakageAccount, "+ " + item.getValue());
        ViewBindUtils.setText(holder.binding.tvTime, item.getTime());
        ViewBindUtils.setText(holder.binding.tvTypeDesc, item.getDesc());
    }
}
