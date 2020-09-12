package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemWalletRecordBinding;
import com.deepsea.mua.stub.entity.WalletRecord;

/**
 * Created by JUN on 2019/5/6
 */
public class WalletRecordAdapter extends BaseBindingAdapter<WalletRecord, ItemWalletRecordBinding> {

    public WalletRecordAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_wallet_record;
    }

    @Override
    protected void bind(BindingViewHolder<ItemWalletRecordBinding> holder, WalletRecord item) {
        holder.binding.descTv.setText(item.getContent());
        holder.binding.timeTv.setText(item.getAddtime());
        if ("expend".equals(item.getType())) {
            holder.binding.numTv.setText("-" + item.getCoin());
            holder.binding.numTv.setTextColor(0xFF9B9B9B);
        } else {
            holder.binding.numTv.setText("+" + item.getCoin());
            holder.binding.numTv.setTextColor(0xFFFF2F78);
        }
    }
}
