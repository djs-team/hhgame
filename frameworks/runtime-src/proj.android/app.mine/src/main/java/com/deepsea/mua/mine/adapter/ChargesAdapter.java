package com.deepsea.mua.mine.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemChargesBinding;
import com.deepsea.mua.stub.entity.ChargeBean;
import com.deepsea.mua.stub.utils.StringUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/5
 */
public class ChargesAdapter extends BaseBindingAdapter<ChargeBean.ChargeListBean, ItemChargesBinding> {

    private int mSelectPos = 0;
    private int is_first = 2;

    public ChargesAdapter(Context context) {
        super(context);
    }


    public void setIs_first(int is_first) {
        this.is_first = is_first;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_charges;
    }

    @Override
    public int getItemCount() {
        if (getData() != null && getData().size() > 9) {
            return 9;
        }
        return super.getItemCount();
    }

    @Override
    protected void bind(BindingViewHolder<ItemChargesBinding> holder, ChargeBean.ChargeListBean item) {
        holder.binding.moneyTv.setText(item.getRmb() + "元");
        holder.binding.mdTv.setText(item.getDiamond());
        holder.binding.chargeLayout.setSelected(mSelectPos == holder.getLayoutPosition());
        ViewBindUtils.setVisible(holder.binding.tvDiscount, is_first == 1 && holder.getLayoutPosition() == 0);//首冲福利
        ViewBindUtils.setText(holder.binding.tvDiscount ,"首冲福利");
        ViewBindUtils.setVisible(holder.binding.tvGiveRose, !TextUtils.isEmpty(item.getPresent())&&!item.getPresent().equals("0"));
        ViewBindUtils.setText(holder.binding.tvGiveRose, String.format("赠%s玫瑰", item.getPresent()));



    }

    public void setSelectPos(int selectPos) {
        if (mSelectPos != selectPos) {
            mSelectPos = selectPos;
            notifyDataSetChanged();
        }
    }

    public String getChargeAmount() {
        if (mSelectPos < getItemCount()) {
            return getItem(mSelectPos).getRmb();
        }
        return "0";
    }

    public String getChargeId() {
        if (mSelectPos < getItemCount()) {
            return getItem(mSelectPos).getChargeid();
        }
        return "";
    }

    public String getPayType() {
        if (mSelectPos < getItemCount()) {
            return getItem(mSelectPos).getPaytype();
        }
        return "";
    }
}
