package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemProfileTagBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;


/**
 * Created by JUN on 2019/5/5
 */
public class MyTagsAdapter extends BaseBindingAdapter<String, ItemProfileTagBinding> {


    public MyTagsAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_profile_tag;
    }

    @Override
    protected void bind(BindingViewHolder<ItemProfileTagBinding> holder, String item) {
        holder.binding.tvTag.setText(item);
        if (holder.getLayoutPosition()%3==0){
            holder.binding.tvTag.setTextColor(mContext.getResources().getColor(com.deepsea.mua.stub.R.color.color_38E9FF));
        }  else if (holder.getLayoutPosition()%3==1){
            holder.binding.tvTag.setTextColor(mContext.getResources().getColor(com.deepsea.mua.stub.R.color.color_B9AAC4));
        }  else if (holder.getLayoutPosition()%3==2){
            holder.binding.tvTag.setTextColor(mContext.getResources().getColor(com.deepsea.mua.stub.R.color.color_EEACC2));
        }

    }


}
