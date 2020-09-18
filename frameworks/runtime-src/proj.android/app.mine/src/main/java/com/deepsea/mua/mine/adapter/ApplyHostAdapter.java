package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemApplyHostBinding;
import com.deepsea.mua.mine.databinding.ItemFansBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.ApplyHost;
import com.deepsea.mua.stub.entity.FollowFanBean;
import com.deepsea.mua.stub.utils.ProfileUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/5
 */
public class ApplyHostAdapter extends BaseBindingAdapter<ApplyHost.ResultBean, ItemApplyHostBinding> {

    private OnMyClickListener mListener;

    public ApplyHostAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_apply_host;
    }

    @Override
    protected void bind(BindingViewHolder<ItemApplyHostBinding> holder, ApplyHost.ResultBean item) {
        ViewBindUtils.setText(holder.binding.tvTaskTitle, item.getUp());
        ViewBindUtils.setText(holder.binding.tvTaskFinish, item.getDown());
        ViewBindUtils.setText(holder.binding.tvOperate, item.getRight());
        ViewBindUtils.RxClicks(holder.binding.tvOperate, o -> {
            if (mListener != null) {
                mListener.confirm(item.getType());
            }
        });
        ViewBindUtils.setEnable(holder.binding.tvOperate, item.getStatus() == 1);
    }

    public void setmListener(OnMyClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnMyClickListener {

        void confirm(int type);
    }
}
