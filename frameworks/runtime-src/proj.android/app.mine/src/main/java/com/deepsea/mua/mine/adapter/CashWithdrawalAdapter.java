package com.deepsea.mua.mine.adapter;

import android.content.Context;
import android.text.TextUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemCrashWithdrawalDetailsBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.CashWListItemBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/7
 */
public class CashWithdrawalAdapter extends BaseBindingAdapter<CashWListItemBean, ItemCrashWithdrawalDetailsBinding> {


    public CashWithdrawalAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_crash_withdrawal_details;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_crash_withdrawal_details;
    }

    @Override
    protected void bind(BindingViewHolder<ItemCrashWithdrawalDetailsBinding> holder, CashWListItemBean item) {
        StringBuilder infoOneBuilder = new StringBuilder();
        infoOneBuilder.append(item.getCashtime());
        infoOneBuilder.append("\n提现金额： ￥");
        infoOneBuilder.append(item.getCash());
        infoOneBuilder.append("\n支付宝：");
        infoOneBuilder.append(item.getApliuserid());
        infoOneBuilder.append("\n手续费：￥");
        infoOneBuilder.append(item.getCash_price());
        infoOneBuilder.append("\n税费：￥");
        infoOneBuilder.append(item.getCash_tax());
        ViewBindUtils.setText(holder.binding.tvCashwithdrawalInfoOne, infoOneBuilder.toString());
        StringBuilder infoTwoBuilder = new StringBuilder();
        infoTwoBuilder.append("状态：");
//        0待审核 1审核通过 2审核驳回
        String status = item.getStatus();
        if (status.equals("0")) {
            infoTwoBuilder.append("待审核");
        } else if (status.equals("1")) {
            infoTwoBuilder.append("审核通过");
        } else if (status.equals("2")) {
            infoTwoBuilder.append("审核驳回");
        }
        infoTwoBuilder.append("\n到账金额：￥");
        infoTwoBuilder.append(item.getReal_cash());
        if (!TextUtils.isEmpty(item.getDesc())) {
            infoTwoBuilder.append("\n备注：");
            infoTwoBuilder.append(item.getDesc());
        }
        ViewBindUtils.setText(holder.binding.tvCashwithdrawalInfoTwo, infoTwoBuilder.toString());
    }
}
