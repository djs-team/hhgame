package com.deepsea.mua.mine.adapter;

import android.content.Context;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemRenewDaysBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/5
 */
public class GuardRenewDaysAdapter extends BaseBindingAdapter<String, ItemRenewDaysBinding> {

    private OnMyClickListener mListener;

    public GuardRenewDaysAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_renew_days;
    }

    private int defaultPos = 0;

    public int getDefaultPos() {
        return defaultPos;
    }

    @Override
    protected void bind(BindingViewHolder<ItemRenewDaysBinding> holder, String item) {
        holder.binding.ivRenewCheck.setSelected(holder.getLayoutPosition() == defaultPos);
        ViewBindUtils.RxClicks(holder.binding.consGroup, o -> {
            defaultPos = holder.getLayoutPosition();
            notifyDataSetChanged();
            if (mListener != null) {
                mListener.onCheckedDay();
            }
        });

    }

    public void setOnMyListener(OnMyClickListener listener) {
        this.mListener = listener;
    }

    public interface OnMyClickListener {


        void onCheckedDay();
    }
}
