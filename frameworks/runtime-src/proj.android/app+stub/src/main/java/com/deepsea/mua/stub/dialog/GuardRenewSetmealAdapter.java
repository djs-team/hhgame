package com.deepsea.mua.stub.dialog;

import android.content.Context;
import android.graphics.Color;

import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.databinding.ItemGuardRenewSetmealBinding;
import com.deepsea.mua.stub.entity.FollowFanBean;
import com.deepsea.mua.stub.entity.RenewInitVo;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/5
 */
public class GuardRenewSetmealAdapter extends BaseBindingAdapter<RenewInitVo.DataBean, ItemGuardRenewSetmealBinding> {

    private OnMyClickListener mListener;

    public GuardRenewSetmealAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_guard_renew_setmeal;
    }

    private int selectPos = 0;

    public int getSelectPos() {
        return selectPos;
    }

    @Override
    protected void bind(BindingViewHolder<ItemGuardRenewSetmealBinding> holder, RenewInitVo.DataBean item) {
        ViewBindUtils.setText(holder.binding.tvSetmealDay, String.valueOf(Integer.valueOf(item.getLong_time()) / 24));
        ViewBindUtils.setText(holder.binding.tvSetmealOriginalPrice,"原价"+item.getOri_coin() + "元");
        ViewBindUtils.setText(holder.binding.tvSetmealCurrentPrice, item.getCoin() + "元");
        if (selectPos == holder.getLayoutPosition()) {
            //选中的
            ViewBindUtils.setBackgroundRes(holder.binding.consGroup, R.drawable.bg_gradients_radius_10);
            ViewBindUtils.setTextColor(holder.binding.tvSetmealDay, R.color.white);
            ViewBindUtils.setTextColor(holder.binding.tvSetmealCurrentPrice, R.color.white);
            ViewBindUtils.setTextColor(holder.binding.tvSetmealOriginalPrice, R.color.white);
            holder.binding.line.setBackgroundColor(Color.parseColor("#ffffffff"));
        } else {
            //未选中的
            ViewBindUtils.setBackgroundRes(holder.binding.consGroup, R.drawable.bg_grayborder_radius_10);
            ViewBindUtils.setTextColor(holder.binding.tvSetmealDay, R.color.black);
            ViewBindUtils.setTextColor(holder.binding.tvSetmealCurrentPrice, R.color.black);
            holder.binding.tvSetmealOriginalPrice.setTextColor(Color.parseColor("#818181"));
            holder.binding.line.setBackgroundColor(Color.parseColor("#000000"));

        }
        ViewBindUtils.RxClicks(holder.binding.consGroup, o -> {
            selectPos = holder.getLayoutPosition();
            notifyDataSetChanged();
            if (mListener != null) {
                mListener.onChecked(holder.getLayoutPosition(),Integer.valueOf(item.getLong_time()),item.getCoin(),item.getId(),item.getCharge_id(),item.getPaytype());
            }
        });

    }

    public void setOnMyListener(OnMyClickListener listener) {
        this.mListener = listener;
    }

    public interface OnMyClickListener {


        void onChecked(int pos,int hour,String coin,String guard_id,String charge_id,String payTypeStr);
    }
}
