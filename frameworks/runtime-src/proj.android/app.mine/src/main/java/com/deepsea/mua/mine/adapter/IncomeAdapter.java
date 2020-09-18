package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemCrashWithdrawalDetailsBinding;
import com.deepsea.mua.mine.databinding.ItemIncomeDetailsBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.CashWListItemBean;
import com.deepsea.mua.stub.entity.IncomeListItemBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/7
 */
public class IncomeAdapter extends BaseBindingAdapter<IncomeListItemBean, ItemIncomeDetailsBinding> {


    public IncomeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_income_details;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_income_details;
    }

    @Override
    protected void bind(BindingViewHolder<ItemIncomeDetailsBinding> holder, IncomeListItemBean item) {
        StringBuilder infoOneBuilder = new StringBuilder();
        infoOneBuilder.append(item.getTime());
        infoOneBuilder.append("\n支付人：");
        infoOneBuilder.append(item.getInitiatorId());
        infoOneBuilder.append("\n消耗玫瑰：");
        infoOneBuilder.append(item.getCoin());
        infoOneBuilder.append("支");
        StringBuilder infoTwoBuilder = new StringBuilder();
        infoTwoBuilder.append("收入类型：");
        infoTwoBuilder.append(item.getType());
//                infoTwoBuilder.append("    ");

        infoTwoBuilder.append("\n接收人：");
        infoTwoBuilder.append(item.getRecipientId());
        infoTwoBuilder.append("\n收到分成：¥");
        infoTwoBuilder.append(item.getDivide());
        ViewBindUtils.setText(holder.binding.tvIncomeInfoOne, infoOneBuilder.toString());
        ViewBindUtils.setText(holder.binding.tvIncomeInfoTwo, infoTwoBuilder.toString());

    }
}
